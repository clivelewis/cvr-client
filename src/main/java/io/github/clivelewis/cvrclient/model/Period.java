package io.github.clivelewis.cvrclient.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Period {
	@JsonProperty("gyldigFra")
	private String validFrom;
	@JsonProperty("gyldigTil")
	private String validTo;
}
