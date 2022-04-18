package io.github.clivelewis.cvrclient.model.internal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class IndustryInfo {
	@JsonProperty("branchekode")
	private String code;
	@JsonProperty("branchetekst")
	private String name;
	@JsonProperty("periode")
	private Period period;
	@JsonProperty("sidstOpdateret")
	private Date lastUpdated;
}
