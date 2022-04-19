package io.github.clivelewis.cvrclient.model;

import lombok.Data;

import java.util.List;

/**
 * Scroll Query response. <br>
 * Contains:<br>
 * 1. scrollId that needs to be passed to the next ScrollSearch request. <br>
 * 2. Cvr Data Models from current response.
 * @param <T> Cvr Data Model class
 */
@Data
public class CvrScrollResponse<T extends CvrCompanyDataModel> {
	/**
	 * Scroll ID from the response. Should be passed to the next ScrollSearch query.
	 */
	private String scrollId;

	/**
	 * List of CVR companies data from current response.
	 */
	private List<T> data;
}
