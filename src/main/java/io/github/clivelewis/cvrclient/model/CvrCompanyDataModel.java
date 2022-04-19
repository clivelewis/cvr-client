package io.github.clivelewis.cvrclient.model;

import com.fasterxml.jackson.annotation.JsonRootName;
import io.github.clivelewis.cvrclient.annotations.CvrFieldRoot;

/**
 * Marker interface for all CVR data model classes. <br>
 * Required by some methods in {@link io.github.clivelewis.cvrclient.CvrClient}. <br>
 * Sets the root element name for Jackson deserialization. <br>
 * Sets the 'Vrvirksomhed' prefix for all {@link io.github.clivelewis.cvrclient.annotations.CvrField} annotations in implementing classes.
 */
@JsonRootName("Vrvirksomhed")
@CvrFieldRoot("Vrvirksomhed")
public interface CvrCompanyDataModel {
}
