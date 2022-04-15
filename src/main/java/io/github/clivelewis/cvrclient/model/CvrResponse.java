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
	private Hits result;

	@Data
	private static class Shards {
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
	public static class Hits {
		@JsonProperty("total")
		private Long total;
		@JsonProperty("max_score")
		private Double maxScore;

		private List<CvrBusinessInfo> businessInfoList = new ArrayList<>();

		@JsonProperty("hits")
		public void setBusinessInfoList(List<CvrBusinessHit> hits) {
			hits.forEach(hit -> businessInfoList.add(hit.getSource().getBusinessInfo()));
		}
	}
}
