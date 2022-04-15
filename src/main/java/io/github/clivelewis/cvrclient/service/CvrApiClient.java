package io.github.clivelewis.cvrclient.service;

import io.github.clivelewis.cvrclient.exception.CvrApiException;

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

class CvrApiClient {
	private static final String SEARCH_API_URL = "http://distribution.virk.dk/cvr-permanent/virksomhed/_search";

	private HttpClient httpClient;

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

	public String sendRequestAndGetBodyString() throws IOException, InterruptedException, URISyntaxException {
		HttpRequest request = HttpRequest.newBuilder().uri(new URI(SEARCH_API_URL)).POST(HttpRequest.BodyPublishers.noBody()).build();
		HttpResponse<String> response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());

		if (response.statusCode() != 200) {
			throw new CvrApiException("CVR responded with " + response.statusCode() + " status code instead of 200.");
		}

		return response.body();
	}

	public String sendRequest(String jsonRequestBody) throws IOException, InterruptedException, URISyntaxException {
		HttpRequest request = HttpRequest.newBuilder().uri(new URI(SEARCH_API_URL))
				.setHeader("Accept", "application/json")
				.setHeader("Content-type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(jsonRequestBody)).build();

		HttpResponse<String> response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());

		if (response.statusCode() != 200) {
			throw new CvrApiException("CVR responded with " + response.statusCode() + " status code instead of 200.");
		}

		return response.body();
	}

}
