package io.github.clivelewis.cvrclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.github.clivelewis.cvrclient.model.CvrCompanyInfo;
import io.github.clivelewis.cvrclient.model.CvrResponse;
import io.github.clivelewis.cvrclient.query.CvrQuery;
import io.github.clivelewis.cvrclient.query.TERMS;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
public class CvrClient {
	private final CvrApiClient apiClient;
	private final ObjectMapper objectMapper;

	public CvrClient(String cvrUsername, String cvrPassword) {
		this.apiClient = new CvrApiClient(cvrUsername, cvrPassword);
		this.objectMapper = new ObjectMapper();
		this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		this.objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
	}


	public List<CvrCompanyInfo> find(CvrQuery cvrQuery) {
		CvrResponse cvrResponse = this.sendRequest(cvrQuery);

		if (cvrResponse != null) return cvrResponse.getResult().getCompanies();
		else return Collections.emptyList();
	}

	public Optional<CvrCompanyInfo> findCompanyByCvrNumber(Long cvrNumber) {
		CvrQuery query = CvrQuery.builder().bool().must().addTerm(TERMS.CVR, cvrNumber.toString()).build();
		CvrResponse cvrResponse = this.sendRequest(query);
		if (cvrResponse != null) {
			List<CvrCompanyInfo> companyInfoList = cvrResponse.getResult().getCompanies();
			if (companyInfoList != null && !companyInfoList.isEmpty()) return Optional.of(companyInfoList.get(0));
		}

		return Optional.empty();
	}

	public List<CvrCompanyInfo> findCompaniesByBranchCode(String branchCode) {
		CvrQuery query = CvrQuery.builder().bool().must().addTerm(TERMS.BRANCH_NUMBER, branchCode).build();
		CvrResponse cvrResponse = this.sendRequest(query);
		if (cvrResponse != null && cvrResponse.getResult() != null) return cvrResponse.getResult().getCompanies();

		return Collections.emptyList();
	}

	private CvrResponse sendRequest(CvrQuery query) {
		try {
			String jsonBody = this.objectMapper.writeValueAsString(query);
			String responseAsJson = this.apiClient.sendRequest(jsonBody);
			return this.objectMapper.readValue(responseAsJson, CvrResponse.class);
		} catch (JsonMappingException e) {
			log.error("Exception while mapping CvrQuery object to JSON", e);
		} catch (JsonProcessingException e) {
			log.error("Exception while processing response JSON", e);
		}

		return null;
	}

}
