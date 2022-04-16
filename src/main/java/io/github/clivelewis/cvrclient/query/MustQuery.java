package io.github.clivelewis.cvrclient.query;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class MustQuery implements Buildable {

	@JsonProperty("term")
	private Map<String, String> terms = new HashMap<>();
	CvrQuery parent;


	public MustQuery addTerm(String name, String value) {
		this.terms.put(name, value);
		return this;
	}

	@Override
	public CvrQuery build() {
		return this.parent;
	}
}
