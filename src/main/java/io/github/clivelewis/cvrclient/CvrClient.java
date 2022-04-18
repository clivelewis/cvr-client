package io.github.clivelewis.cvrclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.github.clivelewis.cvrclient.model.CvrCompanyDataModel;
import io.github.clivelewis.cvrclient.service.CvrFieldAnnotationProcessor;
import io.github.clivelewis.cvrclient.utils.TERMS;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.*;

@Slf4j
public class CvrClient {
	private final CvrApiClient apiClient;
	private final ObjectMapper objectMapper;

	public CvrClient(String cvrUsername, String cvrPassword) {
		this.apiClient = new CvrApiClient(cvrUsername, cvrPassword);
		this.objectMapper = new ObjectMapper();
		this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		this.objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
		this.objectMapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);
		log.info("CvrApiClient Started!");
	}


	public <T extends CvrCompanyDataModel> Optional<T> findCompanyByCvrNumber(Long cvrNumber, Class<T> resultModel) throws JsonProcessingException {
		Objects.requireNonNull(cvrNumber, "CVR Number is required.");
		Objects.requireNonNull(resultModel, "Result Model class in required.");

		log.info("Find Company with CVR {}", cvrNumber);

		SearchRequest searchRequest = new SearchRequest();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(new MatchQueryBuilder(TERMS.CVR, cvrNumber));

		processFieldAnnotations(resultModel, searchSourceBuilder);
		searchRequest.source(searchSourceBuilder);


		List<T> cvrResponse = this.sendCompanySearchRequest(searchRequest, resultModel);
		if (cvrResponse != null && !cvrResponse.isEmpty()) {
			if (cvrResponse.size() > 1)
				log.warn("Somehow found more than 1 company with the same CVR Number. Returning the first match.");

			return Optional.of(cvrResponse.get(0));
		} else return Optional.empty();
	}

	private <T extends CvrCompanyDataModel> void processFieldAnnotations(Class<T> resultModel, SearchSourceBuilder searchSourceBuilder) {
		Set<String> fields = CvrFieldAnnotationProcessor.getFields(resultModel);

		if (!fields.isEmpty()) {
			log.info("Filtering CVR response by fields: {}", fields);
			searchSourceBuilder.fetchSource(fields.toArray(String[]::new), null);
		}
	}

//	public List<CvrCompanyInfo> findAllCompaniesByBranchCode(String branchCode) {
//		SearchRequest searchRequest = new SearchRequest();
//		SearchSourceBuilder source = new SearchSourceBuilder();
//		source.query(new MatchQueryBuilder(TERMS.BRANCH_NUMBER, branchCode));
//		searchRequest.source(source);
//
//		List<Cz> cvrResponse = this.sendRequest(searchRequest);
//		if (cvrResponse == null) return Collections.emptyList();
//		else return cvrResponse;
//	}

	private <T> List<T> sendCompanySearchRequest(SearchRequest request, Class<T> resultModel) throws JsonProcessingException {
		Objects.requireNonNull(request, "Request can't be null");
		Objects.requireNonNull(resultModel, "Result Model Class can't be null");


		SearchHit[] searchHits = this.apiClient.searchInCompanyIndex(request);
		if (searchHits == null || searchHits.length == 0) return null;

		List<T> companyInfoList = new ArrayList<>();

		for (SearchHit searchHit : searchHits) {
			String sourceAsString = searchHit.getSourceAsString();
			companyInfoList.add(this.objectMapper.readValue(sourceAsString, resultModel));
		}
		return companyInfoList;
	}

	public CvrApiClient getCvrApiClient() {
		return this.apiClient;
	}

}
