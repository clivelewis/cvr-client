package io.github.clivelewis.cvrclient.query;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonRootName("query")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CvrQuery {
	@JsonProperty("bool")
	private BoolQuery bool;
	@JsonProperty("match")
	private MatchQuery matchQuery;

	private CvrQuery() {
	}

	public static CvrQueryBuilder builder() {
		return new CvrQueryBuilder();
	}


	public static class CvrQueryBuilder implements Buildable {
		private final CvrQuery cvrQuery;

		public CvrQueryBuilder() {
			this.cvrQuery = new CvrQuery();
		}

		public BoolQuery bool() {
			this.cvrQuery.bool = new BoolQuery();
			this.cvrQuery.bool.parent = this.cvrQuery;
			return this.cvrQuery.bool;
		}

		public MatchQuery match(String term, String value) {
			this.cvrQuery.matchQuery = new MatchQuery(term, value);
			this.cvrQuery.matchQuery.parent = cvrQuery;
			return this.cvrQuery.matchQuery;
		}

		@Override
		public CvrQuery build() {
			return this.cvrQuery;
		}
	}
}
