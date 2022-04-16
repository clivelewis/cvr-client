package io.github.clivelewis.cvrclient.query;


import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

public class MatchQuery implements Buildable {
	private MatchQuery() {}

	@JsonValue
	Map<String, String> fieldValue = new HashMap<>();
	CvrQuery parent;

	MatchQuery(String field, String value) {
		this.fieldValue.put(field, value);
	}

	@Override
	public CvrQuery build() {
		return this.parent;
	}

}
