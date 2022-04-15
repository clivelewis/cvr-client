package io.github.clivelewis.cvrclient.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.github.clivelewis.cvrclient.model.CvrBusinessInfo;
import io.github.clivelewis.cvrclient.model.CvrResponse;
import io.github.clivelewis.cvrclient.query.CvrQuery;
import io.github.clivelewis.cvrclient.query.TERMS;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class CvrClient {
	private final CvrApiClient apiClient;
	private final ObjectMapper objectMapper;

	public CvrClient(String cvrUsername, String cvrPassword) {
		this.apiClient = new CvrApiClient(cvrUsername, cvrPassword);
		this.objectMapper = new ObjectMapper();
		this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		this.objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
		this.objectMapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);
	}


	public CvrBusinessInfo findBusinessInfoByCvrNumber(Long cvrNumber) throws IOException, URISyntaxException, InterruptedException {
		CvrQuery query = CvrQuery.builder().bool().must().addTerm(TERMS.CVR, cvrNumber.toString()).build();
		String jsonBody = this.objectMapper.writeValueAsString(query);

		String s = this.apiClient.sendRequest(jsonBody);
		CvrResponse cvrResponse = this.objectMapper.readValue(s, CvrResponse.class);
		List<CvrBusinessInfo> businessInfoList = cvrResponse.getResult().getBusinessInfoList();
		if (businessInfoList.isEmpty()) return null;
		else return businessInfoList.get(0);
	}

	public List<CvrBusinessInfo> findAllCompaniesByBranchCode(String branchCode) throws IOException, URISyntaxException, InterruptedException {
		CvrQuery query = CvrQuery.builder().bool().must().addTerm(TERMS.BRANCH_NUMBER, branchCode).build();
		String jsonBody = this.objectMapper.writeValueAsString(query);

		String s = this.apiClient.sendRequest(jsonBody);

		CvrResponse cvrResponse = this.objectMapper.readValue(s, CvrResponse.class);
		return cvrResponse.getResult().getBusinessInfoList();
	}

}
