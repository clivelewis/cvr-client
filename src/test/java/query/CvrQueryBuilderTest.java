package query;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.github.clivelewis.cvrclient.query.CvrQuery;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CvrQueryBuilderTest {
	private ObjectMapper objectMapper;

	@Before
	public void setupObjectMapper() {
		this.objectMapper = new ObjectMapper();
		this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		this.objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
		this.objectMapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);
	}

	@Test
	public void testBuildQueryToGetCompaniesByBranchCode() throws JsonProcessingException {
		CvrQuery query = CvrQuery.builder()
				.bool()
				.must()
				.addTerm("Vrvirksomhed.hovedbranche.branchekode", "494100")
				.build();


		String s = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(query);
		assertNotNull(s);
		assertTrue(s.contains("query"));
		assertTrue(s.contains("\"Vrvirksomhed.hovedbranche.branchekode\" : \"494100\""));
		System.out.println(s);
	}
}
