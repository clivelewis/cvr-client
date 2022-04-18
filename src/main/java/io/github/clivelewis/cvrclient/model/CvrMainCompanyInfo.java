package io.github.clivelewis.cvrclient.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.clivelewis.cvrclient.annotations.CvrField;
import io.github.clivelewis.cvrclient.model.internal.CompanyLifespanInfo;
import io.github.clivelewis.cvrclient.model.internal.CompanyNameInfo;
import io.github.clivelewis.cvrclient.model.internal.IndustryInfo;
import io.github.clivelewis.cvrclient.model.internal.Period;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CvrMainCompanyInfo implements CvrCompanyDataModel {

	@JsonProperty("cvrNummer")
	@CvrField("cvrNummer")
	private Long cvrNumber;

	@JsonProperty("reklamebeskyttet")
	@CvrField("reklamebeskyttet")
	private Boolean advertisementProtected;

	@CvrField("navne")
	private String name;

	@JsonProperty("postadresse")
	@CvrField("postadresse")
	private List<AddressInfo> postalAddresses;

	@JsonProperty("beliggenhedsadresse")
	@CvrField("beliggenhedsadresse")
	private List<AddressInfo> locationAddresses;

	@JsonProperty("telefonNummer")
	@CvrField("telefonNummer")
	private List<ContactInfo> phoneNumbers;

	@JsonProperty("sekundaertTelefonNummer")
	@CvrField("sekundaertTelefonNummer")
	private List<ContactInfo> additionalPhoneNumbers;

	@JsonProperty("elektroniskPost")
	@CvrField("elektroniskPost")
	private List<ContactInfo> emails;

	@JsonProperty("hjemmeside")
	@CvrField("hjemmeside")
	private List<ContactInfo> websites;

	@JsonProperty("obligatoriskEmail")
	@CvrField("obligatoriskEmail")
	private List<ContactInfo> obligatoryEmails;

	@CvrField("livsforloeb")
	private boolean isActive;

	@CvrField("hovedbranche")
	private String mainBranchCode;
	private String mainBranchName;

	@JsonProperty("sidstOpdateret")
	@CvrField("sidstOpdateret")
	private Date latestUpdateDate;

	@JsonProperty("livsforloeb")
	public void setCompanyAlive(List<CompanyLifespanInfo> lifespans) {
		if (lifespans == null || lifespans.isEmpty()) this.isActive = false;
		else {
			CompanyLifespanInfo lastLifespan = lifespans.get(lifespans.size() - 1);
			this.isActive = lastLifespan.getPeriod().getValidTo() == null;
		}
	}

	@JsonProperty("navne")
	public void setNames(List<CompanyNameInfo> companyNameHistory) {
		String name = null;
		if (companyNameHistory != null && !companyNameHistory.isEmpty()) {
			CompanyNameInfo lastKnownName = companyNameHistory.get(companyNameHistory.size() - 1);
			this.name = lastKnownName.getName();
		}
	}

	@JsonProperty("hovedbranche")
	public void setIndustryCodeAndName(List<IndustryInfo> industryHistory) {
		if (industryHistory == null || industryHistory.isEmpty()) {
			this.mainBranchCode = null;
			this.mainBranchName = null;
		} else {
			IndustryInfo latestIndustryInfo = industryHistory.get(industryHistory.size() - 1);
			this.mainBranchCode = latestIndustryInfo.getCode();
			this.mainBranchName = latestIndustryInfo.getName();
		}
	}

	@Data
	private static class AddressInfo {
		@JsonProperty("landekode")
		private String countryCode;
		@JsonProperty("fritekst")
		private String comment;
		@JsonProperty("vejkode")
		private String streetCode;
		@JsonProperty("vejnavn")
		private String streetName;
		@JsonProperty("kommune")
		private AddressInfo.MunicipalityInfo municipality;
		@JsonProperty("husnummerFra")
		private Integer houseNumberFrom;
		@JsonProperty("husnummerTil")
		private Integer houseNumberTo;
		@JsonProperty("etage")
		private Integer floor;
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
		private static class MunicipalityInfo {
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
	private static class ContactInfo {
		@JsonProperty("kontaktoplysning")
		private String value;
		@JsonProperty("hemmelig")
		private Boolean isSecret;
		@JsonProperty("periode")
		private Period period;
		@JsonProperty("sidstOpdateret")
		private Date lastUpdated;
	}
}
