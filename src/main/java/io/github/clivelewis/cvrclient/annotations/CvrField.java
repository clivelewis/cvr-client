package io.github.clivelewis.cvrclient.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Runtime annotation that should be used on fields in the CVR Data Model classes
 * so {@link io.github.clivelewis.cvrclient.CvrClient} can create a filter
 * that will tell CVR API which fields should be present in the response.
 *<p>
 * <strong>It's recommended to use this annotation because, if it is present,
 * CVR will return only fields that we actually need.</strong>
 * It can drastically reduce the response size and improve response time.
 *</p>
 * <p>
 * You should set this annotation for <strong>all</strong> fields in your model class because CVR
 * will return only specified fields.
 *</p>
 * <p>
 * Please note that this annotation <strong>WILL NOT</strong> bind response value to the field.
 * Use @JsonProperty for that.
 *</p>
 * <strong>Examples:</strong>
 * <br>
 * Tell CVR to return CVR Number of the company
 <pre>
 {@literal}@CvrField("Vrvirksomhed.cvrNummer")
 private String cvrNumber;
 </pre>

 * Or you can also use wildcards. In this example CVR will return
  * everything inside Vrvirksomhed tag (which is not the best idea
  * because then this is the root tag for all company data)
 <pre>
 {@literal}@CvrField("Vrvirksomhed.*")
 private String field;
 </pre>
 *
 * You can use {@link CvrFieldRoot} annotation on the class to set a prefix for all fields.
  * This example will tell CVR to return fields Vrvirksomhed.cvrNummer and Vrvirksomhed.sidstOpdateret.
 <pre>
 {@literal}@CvrFieldRoot("Vrvirksomhed")
 public class Model {
 	{@literal}@CvrField("cvrNummer")
 	private String cvrNumber;

 	{@literal}@CvrField("sidstOpdateret")
 	private Date latestUpdate;
 }
 </pre>
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CvrField {
	String value();
}
