package io.github.clivelewis.cvrclient;

import io.github.clivelewis.cvrclient.exception.CvrApiException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * Class for API calls to CVR. Uses Java 11+ HttpClient.
 * CVR uses Basic Authentication (Login/Password) and
 * provides POST endpoints to access their data.
 * @see HttpClient
 */
@Slf4j
class CvrApiClient {
	private static final String SEARCH_API_URL = "http://distribution.virk.dk/cvr-permanent/virksomhed/_search";

	private final HttpClient httpClient;

	public CvrApiClient(String cvrUsername, String cvrPassword) {
		this.httpClient = HttpClient.newBuilder()
				.connectTimeout(Duration.of(10, ChronoUnit.SECONDS))
				.version(HttpClient.Version.HTTP_2)
				.authenticator(getAuthenticator(cvrUsername, cvrPassword)).build();
	}


	private Authenticator getAuthenticator(String username, String password) {
		return new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password.toCharArray());
			}
		};
	}


	/** Send Synchronous request to CVR.
	 * @param jsonRequestBody JSON request body
	 * @return Response in a JSON format
	 * @throws CvrApiException if any other exception occurs or response status is not 200
	 */
	public String sendRequest(String jsonRequestBody) throws CvrApiException {
		HttpRequest request = HttpRequest.newBuilder().uri(getCvrSearchUri())
				.setHeader("Accept", "application/json")
				.setHeader("Content-type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(jsonRequestBody)).build();

		try {
			HttpResponse<String> response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());

			if (response.statusCode() != 200) {
				throw new CvrApiException("CVR responded with " + response.statusCode() + " status code instead of 200.");
			}

			return response.body();
		} catch (IOException e) {
			log.error("IOException while trying to send request to CVR API.");
			throw new CvrApiException(e);
		} catch (InterruptedException e) {
			log.error("Interrupted while trying to send request to CVR API.");
			throw new CvrApiException(e);
		}
	}

	private URI getCvrSearchUri() {
		try {
			return new URI(SEARCH_API_URL);
		} catch (URISyntaxException e) {
			log.error("Error while creating URI from input - {}", SEARCH_API_URL);
			throw new CvrApiException(e);
		}
	}
}
