package io.github.clivelewis.cvrclient.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CvrBusinessHit {
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
	public static class SourceInfo {
		@JsonProperty("Vrvirksomhed")
		private CvrBusinessInfo businessInfo;
	}
}
