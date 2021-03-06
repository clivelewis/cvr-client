package io.github.clivelewis.cvrclient;

import io.github.clivelewis.cvrclient.exception.CvrApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.action.search.*;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.util.Objects;


/**
 * Abstraction layer over ElasticSearch's {@link RestHighLevelClient} class. <br>
 * Creates an instance of RestHighLevelClient and provides methods to call available API endpoints. <br><br>
 *
 * CVR uses ElasticSearch as their engine and provides API endpoints to make search requests using ElasticSearch queries. <br>
 * We cannot modify the data in the database so there is no reason to provide update/delete/create methods in this class. <br>
 * You can call getElasticRestClient() method if you want to access RestHighLevelClient directly.
 *
 */
@Slf4j
public class CvrApiClient {

	private final RestHighLevelClient elasticRestClient;


	CvrApiClient(String cvrUsername, String cvrPassword) {
		RestClientBuilder restClientBuilder = RestClient.builder(new HttpHost("distribution.virk.dk"));
		restClientBuilder.setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(getCredentialsProvider(cvrUsername, cvrPassword)));
		this.elasticRestClient = new RestHighLevelClient(restClientBuilder);
		log.info("ElasticSearch RestHighLevelClient Started!");
	}

	public SearchResponse searchInCompanyIndex(SearchRequest request) throws CvrApiException {
		request.indices("cvr-permanent/virksomhed");
		try {
			SearchResponse searchResponse = this.elasticRestClient.search(request, RequestOptions.DEFAULT);
			if (searchResponse.status().getStatus() != 200) {
				throw new CvrApiException("CVR responded with " + searchResponse.status().getStatus() + " status code instead of 200.");
			}

			return searchResponse;
		} catch (IOException e) {
			log.error("IOException while trying to send request to CVR API.");
			throw new CvrApiException(e);
		}
	}

	public SearchResponse searchScroll(SearchScrollRequest request) {

		try {
			SearchResponse searchResponse = this.elasticRestClient.scroll(request, RequestOptions.DEFAULT);
			if (searchResponse.status().getStatus() != 200) {
				throw new CvrApiException("CVR responded with " + searchResponse.status().getStatus() + " status code instead of 200.");
			}

			return searchResponse;
		} catch (IOException e) {
			log.error("IOException while sending a Scroll request to CVR API.");
			throw new CvrApiException(e);
		}
	}


	public ClearScrollResponse clearScroll(String scrollId) {
		Objects.requireNonNull(scrollId, "Scroll ID is required.");
		try {
			ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
			clearScrollRequest.addScrollId(scrollId);
			return this.elasticRestClient.clearScroll(clearScrollRequest, RequestOptions.DEFAULT);
		} catch (IOException e) {
			log.error("IOException while sending ClearScrollRequest.");
			throw new CvrApiException(e);
		}
	}

	public RestHighLevelClient getElasticRestClient() {
		return this.elasticRestClient;
	}

	private CredentialsProvider getCredentialsProvider(String username, String password) {
		final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(AuthScope.ANY,
				new UsernamePasswordCredentials(username, password));
		return credentialsProvider;
	}

}
