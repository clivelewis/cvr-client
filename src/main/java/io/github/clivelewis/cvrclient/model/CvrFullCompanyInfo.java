package io.github.clivelewis.cvrclient.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.clivelewis.cvrclient.model.internal.CompanyLifespanInfo;
import io.github.clivelewis.cvrclient.model.internal.CompanyNameInfo;
import io.github.clivelewis.cvrclient.model.internal.IndustryInfo;
import io.github.clivelewis.cvrclient.model.internal.Period;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Representation of the CVR Company Data response.<br>
 * Should contain <strong>all</strong> possible fields from the response body.<br>
 * See JsonProperty to understand what is the name of the field in the response body.<br>
 * All fields without mapping are stored in the <strong>fieldsWithoutMapping</strong> variable.
 * </p>
 */
@Data
public class CvrFullCompanyInfo implements CvrCompanyDataModel {
	@JsonProperty("cvrNummer")
	private Long cvrNumber;

	@JsonProperty("regNummer")
	private List<RegistrationNumberInfo> registrationNumbers;

	@JsonProperty("brancheAnsvarskode")
	private Long industryResponsibilityCode;

	@JsonProperty("reklamebeskyttet")
	private Boolean advertisementProtected;

	@JsonProperty("navne")
	private List<CompanyNameInfo> names;

	@JsonProperty("binavne")
	private List<CompanyNameInfo> additionalNames;

	@JsonProperty("postadresse")
	private List<AddressInfo> postalAddresses;

	@JsonProperty("beliggenhedsadresse")
	private List<AddressInfo> locationAddresses;

	@JsonProperty("telefonNummer")
	private List<ContactInfo> phoneNumbers;

	@JsonProperty("telefaxNummer")
	private List<ContactInfo> faxNumbers;

	@JsonProperty("sekundaertTelefonNummer")
	private List<ContactInfo> additionalPhoneNumbers;

	@JsonProperty("sekundaertTelefaxNummer")
	private List<ContactInfo> additionalFaxNumbers;

	@JsonProperty("elektroniskPost")
	private List<ContactInfo> emails;

	@JsonProperty("hjemmeside")
	private List<ContactInfo> websites;

	@JsonProperty("obligatoriskEmail")
	private List<ContactInfo> obligatoryEmails; // Always (or almost always) empty.

	@JsonProperty("livsforloeb")
	private List<CompanyLifespanInfo> businessLifespans;

	@JsonProperty("hovedbranche")
	private List<IndustryInfo> mainBranches;

	@JsonProperty("bibranche1")
	private List<IndustryInfo> additionalBranches;

	@JsonProperty("bibranche2")
	private List<IndustryInfo> additionalBranches2;

	@JsonProperty("bibranche3")
	private List<IndustryInfo> additionalBranches3;

	@JsonProperty("status")
	private List<StatusInfo> statuses;

	@JsonProperty("virksomhedsstatus")
	private List<BusinessStatusInfo> businessStatuses;

	@JsonProperty("virksomhedsform")
	private List<BusinessFormInfo> businessForms;

	@JsonProperty("aarsbeskaeftigelse")
	private List<AnnualEmploymentHistoryEntry> annualEmploymentHistory;

	@JsonProperty("kvartalsbeskaeftigelse")
	private List<QuarterlyEmploymentHistoryEntry> quarterlyEmploymentHistory;

	@JsonProperty("maanedsbeskaeftigelse")
	private List<MonthlyEmploymentHistoryEntry> monthlyEmploymentHistory;

	@JsonProperty("erstMaanedsbeskaeftigelse")
	private List<MonthlyEmploymentHistoryEntry> firstMonthlyEmploymentHistory;

	@JsonProperty("attributter")
	private List<AttributeInfo> attributes;

	@JsonProperty("deltagerRelation")
	private List<ParticipationInfo> participants;

	@JsonProperty("sidstOpdateret")
	private Date lastUpdate;

	private Map<String, Object> fieldsWithoutMapping = new HashMap<>();

	@JsonAnyGetter
	public Map<String, Object> getFieldsWithoutMapping() {
		return this.fieldsWithoutMapping;
	}

	@JsonAnySetter
	public void setFieldWithoutMapping(String field, Object value) {
		this.fieldsWithoutMapping.put(field, value);
	}

	@Data
	public static class RegistrationNumberInfo {
		@JsonProperty("regnummer")
		private String number;
		@JsonProperty("periode")
		private Period period;
		@JsonProperty("sidstOpdateret")
		private Date lastUpdated;
	}

	@Data
	public static class AddressInfo {
		@JsonProperty("landekode")
		private String countryCode;
		@JsonProperty("fritekst")
		private String comment;
		@JsonProperty("vejkode")
		private String streetCode;
		@JsonProperty("vejnavn")
		private String streetName;
		@JsonProperty("kommune")
		private MunicipalityInfo municipality;
		@JsonProperty("husnummerFra")
		private Integer houseNumberFrom;
		@JsonProperty("husnummerTil")
		private Integer houseNumberTo;
		@JsonProperty("etage")
		private String floor;
		@JsonProperty("conavn")
		private String nameOfCaretaker; // The one who is responsible for this place
		@JsonProperty("postboks")
		private String postBox;
		@JsonProperty("postnummer")
		private Integer zipCode;
		@JsonProperty("periode")
		private Period period;
		@JsonProperty("sidstOpdateret")
		private Date lastUpdated;

		@Data
		public static class MunicipalityInfo {
			@JsonProperty("kommuneKode")
			private Integer code;
			@JsonProperty("kommuneNavn")
			private String name;
			@JsonProperty("periode")
			private Period period;
			@JsonProperty("sidstOpdateret")
			private Date lastUpdated;
		}
	}


	@Data
	public static class ContactInfo {
		@JsonProperty("kontaktoplysning")
		private String value;
		@JsonProperty("hemmelig")
		private Boolean isSecret;
		@JsonProperty("periode")
		private Period period;
		@JsonProperty("sidstOpdateret")
		private Date lastUpdated;
	}

	@Data
	public static class StatusInfo {
		@JsonProperty("statuskode")
		private Integer code;
		@JsonProperty("statustekst")
		private String name;
		@JsonProperty("kreditoplysningkode")
		private Integer creditInformationCode;
		@JsonProperty("kreditoplysningtekst")
		private String creditInformationName;
		@JsonProperty("periode")
		private Period period;
		@JsonProperty("sidstOpdateret")
		private Date lastUpdated;
	}

	@Data
	public static class BusinessStatusInfo {
		@JsonProperty("status")
		private String status;
		@JsonProperty("periode")
		private Period period;
		@JsonProperty("sidstOpdateret")
		private Date lastUpdated;
	}

	@Data
	public static class BusinessFormInfo {
		@JsonProperty("virksomhedsformkode")
		private Integer code;
		@JsonProperty("kortBeskrivelse")
		private String abbreviation;
		@JsonProperty("langBeskrivelse")
		private String name;
		@JsonProperty("ansvarligDataleverandoer")
		private String responsibleDataProvider;
		@JsonProperty("periode")
		private Period period;
		@JsonProperty("sidstOpdateret")
		private Date lastUpdated;
	}

	@Data
	public static class AnnualEmploymentHistoryEntry {
		@JsonProperty("aar")
		private Short year;
		@JsonProperty("antalInklusivEjere")
		private Short numberOfEmployeesIncludingOwners;
		@JsonProperty("antalAnsatte")
		private Short numberOfEmployees;
		@JsonProperty("antalAarsvaerk")
		private Double numberOfManYears;

		@JsonProperty("intervalKodeAntalInklusivEjere")
		private String intervalCodeForEmployeesIncludingOwners;
		@JsonProperty("intervalKodeAntalAnsatte")
		private String intervalCodeForEmployees;
		@JsonProperty("intervalKodeAntalAarsvaerk")
		private String intervalCodeForManYears;

		@JsonProperty("sidstOpdateret")
		private Date lastUpdated;
	}

	@Data
	public static class QuarterlyEmploymentHistoryEntry {
		@JsonProperty("aar")
		private Short year;
		@JsonProperty("kvartal")
		private Short quarter;
		@JsonProperty("antalAarsvaerk")
		private Double numberOfManYears;
		@JsonProperty("antalAnsatte")
		private Short numberOfEmployees;
		@JsonProperty("intervalKodeAntalAarsvaerk")
		private String intervalCodeForManYears;
		@JsonProperty("intervalKodeAntalAnsatte")
		private String intervalCodeForEmployees;
		@JsonProperty("sidstOpdateret")
		private Date lastUpdated;
	}

	@Data
	public static class MonthlyEmploymentHistoryEntry {
		@JsonProperty("aar")
		private Short year;
		@JsonProperty("maaned")
		private Short month;
		@JsonProperty("antalAarsvaerk")
		private Double numberOfManYears;
		@JsonProperty("antalAnsatte")
		private Short numberOfEmployees;
		@JsonProperty("intervalKodeAntalAarsvaerk")
		private String intervalCodeForManYears;
		@JsonProperty("intervalKodeAntalAnsatte")
		private String intervalCodeForEmployees;
		@JsonProperty("sidstOpdateret")
		private Date lastUpdated;

	}

	@Data
	public static class AttributeInfo {
		@JsonProperty("sekvensnr")
		private Integer sequenceNumber;
		@JsonProperty("type")
		private String type;
		@JsonProperty("vaerditype")
		private String valueType;
		@JsonProperty("vaerdier")
		private List<Value> values;

		@Data
		public static class Value {
			@JsonProperty("vaerdi")
			private String value;
			@JsonProperty("periode")
			private Period period;
			@JsonProperty("sidstOpdateret")
			private Date lastUpdated;
		}
	}

	@Data
	public static class ParticipationInfo {
		@JsonProperty("deltager")
		private ParticipantInfo participant;

		@JsonProperty("kontorsteder")
		private List<OfficeInfo> offices;

		@JsonProperty("organisationer")
		private List<OrganizationInfo> organizations;

		@Data
		public static class ParticipantInfo {
			@JsonProperty("enhedsNummer")
			private Long participantNumber;
			@JsonProperty("enhedstype")
			private String participantType;

			@JsonProperty("forretningsnoegle")
			private Long businessKey;
			@JsonProperty("organisationstype")
			private String organizationType;

			@JsonProperty("sidstIndlaest")
			private Date lastLoaded;
			@JsonProperty("sidstOpdateret")
			private Date lastUpdated;

			@JsonProperty("navne")
			private List<CompanyNameInfo> names;

			@JsonProperty("adresseHemmelig")
			private Boolean isAddressSecret;
			@JsonProperty("adresseOpdateringOphoert")
			private Boolean isAddressUpdateDiscontinued;

			@JsonProperty("postadresse")
			private List<AddressInfo> postalAddresses;

			@JsonProperty("beliggenhedsadresse")
			private List<AddressInfo> locationAddresses;

		}

		@Data
		public static class OfficeInfo {
			@JsonProperty("penhed")
			private ParticipantInfo unit;

			@JsonProperty("attributter")
			private List<AttributeInfo> attributes;

		}

		@Data
		public static class OrganizationInfo {
			@JsonProperty("enhedsNummerOrganisation")
			private Long organizationNumber;
			@JsonProperty("hovedtype")
			private String mainType;
			@JsonProperty("organisationsNavn")
			private List<CompanyNameInfo> organizationNames;
			@JsonProperty("attributter")
			private List<AttributeInfo> attributes;
			@JsonProperty("medlemsData")
			private List<MemberData> membersData;

			@Data
			public static class MemberData {
				@JsonProperty("attributter")
				private List<AttributeInfo> attributes;
			}
		}
	}
}
