package io.github.clivelewis.cvrclient.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonRootName("query")
public class CvrQuery {
	@JsonProperty("bool")
	private BoolQuery bool;

	private CvrQuery() {
	}

	public static CvrQueryBuilder builder() {
		return new CvrQueryBuilder();
	}


	public static class CvrQueryBuilder {
		private final CvrQuery cvrQuery;

		public CvrQueryBuilder() {
			this.cvrQuery = new CvrQuery();
		}

		public BoolQuery bool() {
			this.cvrQuery.bool = new BoolQuery();
			this.cvrQuery.bool.parent = this.cvrQuery;
			return this.cvrQuery.bool;
		}
	}

	public BoolQuery bool() {
		this.bool = new BoolQuery();
		this.bool.parent = this;

		return this.bool;
	}


	public static class BoolQuery {
		@JsonProperty("must")
		private List<MustQuery> mustQueries = new ArrayList<>();
		private CvrQuery parent;

		public MustQuery must() {
			MustQuery mustQuery = new MustQuery();
			mustQuery.parent = parent;
			this.mustQueries.add(mustQuery);
			return mustQuery;
		}

		public CvrQuery build() {
			return parent;
		}
	}

	public static class MustQuery {
		@JsonProperty("term")
		private Map<String, String> terms = new HashMap<>();
		private CvrQuery parent;


		public MustQuery addTerm(String name, String value) {
			this.terms.put(name, value);
			return this;
		}

		public CvrQuery build() {
			return this.parent;
		}
	}
}
