package io.github.clivelewis.cvrclient.utils;

/**
 * Utility class with the list of terms that can be used for queries.
 */
public class TERMS {


	/**
	 * CVR Number (Unique for every company).
	 */
	public static final String CVR = "Vrvirksomhed.cvrNummer";

	/**
	 * Main branch (industry) code.
	 */
	public static final String BRANCH_NUMBER = "Vrvirksomhed.hovedbranche.branchekode";


	/**
	 * Company's lifecycle end date.
	 * Company is not active (basically closed, terminated, etc.) if this field is present in CVR record.
	 * Please note that active status doesn't mean that company is operational.
	 * For example, it can be bankrupt, but not yet officially closed.
	 */
	public static final String LIFECYCLE_END_DATE = "Vrvirksomhed.livsforloeb.periode.gyldigTil";

	private TERMS() {
	}

}
