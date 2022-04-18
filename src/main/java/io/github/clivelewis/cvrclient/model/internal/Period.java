package io.github.clivelewis.cvrclient.model.internal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Period {
	@JsonProperty("gyldigFra")
	private String validFrom;
	@JsonProperty("gyldigTil")
	private String validTo;
}
