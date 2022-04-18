package io.github.clivelewis.cvrclient.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.clivelewis.cvrclient.annotations.CvrField;
import io.github.clivelewis.cvrclient.annotations.CvrFieldRoot;
import lombok.Data;

@Data
@CvrFieldRoot("Vrvirksomhed")
public class CvrCompanyOnlyCvrNumber implements CvrCompanyDataModel {

	@JsonProperty("cvrNummer")
	@CvrField("cvrNummer")
	private Long cvrNumber;
}
