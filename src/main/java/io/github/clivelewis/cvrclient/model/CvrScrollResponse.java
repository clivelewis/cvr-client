package io.github.clivelewis.cvrclient.model;

import lombok.Data;

import java.util.List;

@Data
public class CvrScrollResponse<T> {
	private String scrollId;

	private List<T> data;
}
