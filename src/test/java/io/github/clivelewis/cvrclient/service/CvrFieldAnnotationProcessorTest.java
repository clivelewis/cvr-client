package io.github.clivelewis.cvrclient.service;

import io.github.clivelewis.cvrclient.annotations.CvrField;
import io.github.clivelewis.cvrclient.annotations.CvrFieldRoot;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CvrFieldAnnotationProcessorTest {

	@CvrFieldRoot("root")
	interface InterfaceWithAnnotation {

	}

	static class DummyClassWithInterfaceWithAnnotation implements InterfaceWithAnnotation {
		@CvrField("field1")
		private String field1;
		@CvrField("whatever")
		public Integer field2;
		@CvrField("anyName")
		private Object field3;
	}

	@Test
	public void testWhenClassImplementsInterfaceWithAnnotation() {
		Set<String> fieldsAsSet = CvrFieldAnnotationProcessor.getFields(DummyClassWithInterfaceWithAnnotation.class);
		List<String> fields = new ArrayList<>(fieldsAsSet);
		assertEquals(3, fields.size());
		fields.forEach(f -> assertTrue(f.startsWith("root.")));
		assertTrue(fields.contains("root.field1"));
		assertTrue(fields.contains("root.whatever"));
		assertTrue(fields.contains("root.anyName"));
		assertFalse(fields.contains("root.valueThatDoesntExist"));
	}

	class DummyClassWithoutInterfaceAndRootAnnotation {
		@CvrField("fieldName")
		private Void field1;
		@CvrField("parent.child.nextChild")
		private String field2;
		@CvrField("")
		private String field3;
	}

	@Test
	public void testClassWithoutRootAnnotationAndInterfaces() {
		Set<String> fieldsAsSet = CvrFieldAnnotationProcessor.getFields(DummyClassWithoutInterfaceAndRootAnnotation.class);
		List<String> fields = new ArrayList<>(fieldsAsSet);
		assertEquals(2, fields.size());
		assertTrue(fields.contains("fieldName"));
		assertTrue(fields.contains("parent.child.nextChild"));
	}


	@CvrFieldRoot("lorem")
	class DummyClassWithAnnotation {
		@CvrField("ipsum")
		private Void field1;
		@CvrField("...strangeField.*")
		private String field2;
		@CvrField("")
		private String field3;
	}

	@Test
	public void testClassWithRootAnnotation() {
		Set<String> fieldsAsSet = CvrFieldAnnotationProcessor.getFields(DummyClassWithAnnotation.class);
		List<String> fields = new ArrayList<>(fieldsAsSet);
		assertEquals(2, fields.size());
		assertTrue(fields.contains("lorem.ipsum"));
		assertTrue(fields.contains("lorem....strangeField.*"));
	}




}