package io.github.clivelewis.cvrclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.github.clivelewis.cvrclient.model.CvrCompanyDataModel;
import io.github.clivelewis.cvrclient.model.CvrScrollResponse;
import io.github.clivelewis.cvrclient.service.CvrFieldAnnotationProcessor;
import io.github.clivelewis.cvrclient.utils.TERMS;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.Closeable;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Main class. Creates an instance of {@link CvrApiClient} and provides
 * high-level methods to access data from CVR API. <br>
 * It is recommended to create 1 instance of this class and use it throughout your application.<br>
 * Main goal of this class is to create an easy-to-use abstraction layer over
 * API calls and ElasticSearch Query creation. <br><br>
 *
 * If you want to have more control (or expand functionality) you can access
 * ElasticSearch Java Client by calling apiClient().getElasticRestClient();
 *
 */
@Slf4j
public class CvrClient implements Closeable {
	private final CvrApiClient apiClient;
	private final ObjectMapper objectMapper;

	/**
	 * Create an instance of CvrClient.
	 * @param cvrUsername CVR username (or userId)
	 * @param cvrPassword CVR password
	 */
	public CvrClient(String cvrUsername, String cvrPassword) {
		this.apiClient = new CvrApiClient(cvrUsername, cvrPassword);
		this.objectMapper = new ObjectMapper();
		this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		this.objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
		this.objectMapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);
		log.info("CvrApiClient Started!");
	}

	/**
	 * Find company by CVR number.
	 * @param cvrNumber CVR Number
	 * @param resultModel Model class for deserialized JSON company data.
	 * @param <T> CvrCompanyDataModel implementation.
	 * @return Optional of CVR company data model.
	 * @throws JsonProcessingException If Jackson couldn't deserialize JSON to our model.
	 */
	public <T extends CvrCompanyDataModel> Optional<T> findCompanyByCvrNumber(Long cvrNumber, Class<T> resultModel) throws JsonProcessingException {
		Objects.requireNonNull(cvrNumber, "CVR Number is required.");
		Objects.requireNonNull(resultModel, "Result Model class in required.");

		log.info("Find Company with CVR {}", cvrNumber);

		SearchRequest searchRequest = new SearchRequest();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(new MatchQueryBuilder(TERMS.CVR, cvrNumber));

		processFieldAnnotations(resultModel, searchSourceBuilder);
		searchRequest.source(searchSourceBuilder);


		List<T> cvrResponse = this.searchInCompanyIndex(searchRequest, resultModel);
		if (cvrResponse != null && !cvrResponse.isEmpty()) {
			if (cvrResponse.size() > 1)
				log.warn("Somehow found more than 1 company with the same CVR Number. Returning the first match.");

			return Optional.of(cvrResponse.get(0));
		} else return Optional.empty();
	}


	/**
	 * Find all ACTIVE companies in a specified branch (industry).
	 * This method uses Search Scroll Query to retrieve large amount of entries.
	 * @param branchCode Branch (Industry) code.
	 * @param resultModel Model class for deserialized JSON company data.
	 * @param <T> CvrCompanyDataModel implementation.
	 * @return List of CVR company data models. Empty list if no data was found.
	 * @throws JsonProcessingException If Jackson couldn't deserialize JSON to our model.
	 */
	public <T extends CvrCompanyDataModel> List<T> findAllActiveCompaniesByBranchCode(String branchCode, Class<T> resultModel) throws JsonProcessingException {
		Objects.requireNonNull(branchCode, "Branch Code(Number) is required.");
		Objects.requireNonNull(resultModel, "Result Model class in required.");

		log.info("Find All active companies with main branch code {}", branchCode);

		final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(5L));
		SearchRequest searchRequest = new SearchRequest();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder.mustNot(QueryBuilders.existsQuery(TERMS.LIFECYCLE_END_DATE));
		boolQueryBuilder.must(new MatchQueryBuilder(TERMS.BRANCH_NUMBER, branchCode));

		searchSourceBuilder.query(boolQueryBuilder);
		searchSourceBuilder.size(20);
		processFieldAnnotations(resultModel, searchSourceBuilder);
		searchRequest.source(searchSourceBuilder);
		searchRequest.scroll(scroll);

		SearchResponse searchResponse = this.apiClient.searchInCompanyIndex(searchRequest);
		long totalHits = searchResponse.getHits().getTotalHits();
		log.info("Total active companies in branch: {}. ", totalHits);

		List<T> result = new ArrayList<>();
		String scrollId = searchResponse.getScrollId();
		SearchHit[] hits = searchResponse.getHits().getHits();
		while (hits != null && hits.length > 0) {
			if (result.size() % 1000 == 0) log.info("Processed {}/{} records", result.size(), totalHits);
			for (SearchHit hit : hits) {
				String sourceAsString = hit.getSourceAsString();
				result.add(this.objectMapper.readValue(sourceAsString, resultModel));
			}


			SearchScrollRequest searchScrollRequest = new SearchScrollRequest(scrollId);
			searchScrollRequest.scroll(scroll);
			searchResponse = apiClient.searchScroll(searchScrollRequest);

			scrollId = searchResponse.getScrollId();
			hits = searchResponse.getHits().getHits();
		}

		apiClient.clearScroll(scrollId);
		log.info("DONE! Processed {}/{} records", result.size(), totalHits);

		return result;
	}

	/** General method to make search requests in the CVR company index.
	 * Build your own SearchRequest object, provide CvrCompanyData model implementation and you are good to go.
	 * @param request any SearchRequest. Check ElasticSearch documentation to understand how to build queries.
	 * @param resultModel Model class for deserialized JSON company data.
	 * @param <T> CvrCompanyDataModel implementation.
	 * @return List of CVR company data models. Empty list if no data was found.
	 * @throws JsonProcessingException  If Jackson couldn't deserialize JSON to our model.
	 */
	public <T extends CvrCompanyDataModel> List<T> searchInCompanyIndex(SearchRequest request, Class<T> resultModel) throws JsonProcessingException {
		Objects.requireNonNull(request, "Request can't be null");
		Objects.requireNonNull(resultModel, "Result Model Class can't be null");


		List<String> elementsAsJson = this.searchInCompanyIndex(request);
		if (elementsAsJson == null || elementsAsJson.isEmpty()) return Collections.emptyList();

		List<T> companyInfoList = new ArrayList<>();

		for (String elementAsJson : elementsAsJson) {
			companyInfoList.add(this.objectMapper.readValue(elementAsJson, resultModel));
		}

		return companyInfoList;
	}

	/**
	 * General method to make search requests in the CVR company index.
	 * @param request any SearchRequest. Check ElasticSearch documentation to understand how to build queries.
	 * @return List where each element is a JSON representation of company data. Empty List if nothing was found or something went wrong.
	 */
	public List<String> searchInCompanyIndex(SearchRequest request) {
		Objects.requireNonNull(request, "Request can't be null");
		SearchResponse searchResponse = this.apiClient.searchInCompanyIndex(request);
		SearchHit[] searchHits = searchResponse.getHits().getHits();

		if (searchHits == null || searchHits.length == 0) return Collections.emptyList();

		return hitsToJson(searchHits);
	}

	public <T extends CvrCompanyDataModel> CvrScrollResponse<T> searchScroll(SearchScrollRequest request, Class<T> resultModel) throws JsonProcessingException {
		Objects.requireNonNull(request, "Request can't be null");

		Map<String, Object> searchScrollResult = this.searchScroll(request);

		CvrScrollResponse<T> cvrScrollResponse = new CvrScrollResponse<>();
		cvrScrollResponse.setScrollId(searchScrollResult.get("scrollId").toString());
		Object data = searchScrollResult.get("data");

		if (data instanceof List) {
			List<T> resultData = new ArrayList<>();
			List<String> dataAsJson = ((List<?>) data).stream().map(Object::toString).collect(Collectors.toList());
			for (String element : dataAsJson) {
				resultData.add(this.objectMapper.readValue(element, resultModel));
			}

			cvrScrollResponse.setData(resultData);
		} else cvrScrollResponse.setData(null);

		return cvrScrollResponse;
	}

	public Map<String, Object> searchScroll(SearchScrollRequest request) {
		Objects.requireNonNull(request, "Request can't be null");
		SearchResponse searchResponse = this.apiClient.searchScroll(request);
		SearchHit[] hits = searchResponse.getHits().getHits();

		List<String> dataAsJson = this.hitsToJson(hits);
		HashMap<String, Object> result = new HashMap<>();
		result.put("scrollId", searchResponse.getScrollId());
		result.put("data", dataAsJson);
		return result;
	}


	private List<String> hitsToJson(SearchHit[] searchHits) {
		List<String> resultAsJsonString = new ArrayList<>();
		for (SearchHit searchHit : searchHits) {
			String sourceAsString = searchHit.getSourceAsString();
			resultAsJsonString.add(sourceAsString);
		}
		return resultAsJsonString;
	}


	private <T extends CvrCompanyDataModel> void processFieldAnnotations(Class<T> resultModel, SearchSourceBuilder searchSourceBuilder) {
		Set<String> fields = CvrFieldAnnotationProcessor.getFields(resultModel);

		if (!fields.isEmpty()) {
			log.info("Filtering CVR response by fields: {}", fields);
			searchSourceBuilder.fetchSource(fields.toArray(String[]::new), null);
		}
	}

	/**
	 * Get instance of configured ObjectMapper that is used for all deserialization in this class.
	 * @return ObjectMapper instance
	 */
	public ObjectMapper objectMapper() {
		return this.objectMapper;
	}

	/** Get instance of {@link CvrApiClient}.
	 * @return CvrApiClient instance
	 */
	public CvrApiClient apiClient() {
		return this.apiClient;
	}

	/** Release resources.
	 */
	@Override
	public void close() throws IOException {
		if (this.apiClient() != null && this.apiClient().getElasticRestClient() != null) {
			this.apiClient().getElasticRestClient().close();
		}
	}
}
