package io.github.clivelewis.cvrclient.query;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class BoolQuery implements Buildable {
	@JsonProperty("must")
	List<MustQuery> mustQueries = new ArrayList<>();
	CvrQuery parent;

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
