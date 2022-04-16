package io.github.clivelewis.cvrclient.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CvrResponse {
	@JsonProperty("took")
	private Long took;
	@JsonProperty("timed_out")
	private Boolean timedOut;
	@JsonProperty("_shards")
	private Shards shards;
	@JsonProperty("hits")
	private SearchResult result;

	@Data
	static class Shards {
		@JsonProperty("total")
		private Integer total;
		@JsonProperty("successful")
		private Integer successful;
		@JsonProperty("skipped")
		private Integer skipped;
		@JsonProperty("failed")
		private Integer failed;
	}

	@Data
	static class CvrBusinessHit {
		@JsonProperty("_index")
		private String index;
		@JsonProperty("_type")
		private String indexType;
		@JsonProperty("_id")
		private String id;
		@JsonProperty("_score")
		private Double score;
		@JsonProperty("_source")
		private SourceInfo source;

		@Data
		static class SourceInfo {
			@JsonProperty("Vrvirksomhed")
			private CvrCompanyInfo businessInfo;
		}
	}

	@Data
	public static class SearchResult {
		@JsonProperty("total")
		private Long total;
		@JsonProperty("max_score")
		private Double maxScore;
		private List<CvrCompanyInfo> companies = new ArrayList<>();

		@JsonProperty("hits")
		public void setCompanies(List<CvrBusinessHit> hits) {
			hits.forEach(hit -> companies.add(hit.getSource().getBusinessInfo()));
		}
	}
}
