package io.github.clivelewis.cvrclient.service;

import io.github.clivelewis.cvrclient.annotations.CvrField;
import io.github.clivelewis.cvrclient.annotations.CvrFieldRoot;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class CvrFieldAnnotationProcessor {

	public static <T> Set<String> getFields(Class<T> providedClass) {
		Set<String> fields = new HashSet<>();
		String prefix = getPrefix(providedClass);

		for (Field field : providedClass.getDeclaredFields()) {
			CvrField cvrField = field.getAnnotation(CvrField.class);
			if (cvrField != null) fields.add(prefix + cvrField.value());
		}

		return fields;
	}

	private static <T> String getPrefix(Class<T> providedClass) {
		CvrFieldRoot rootValue = providedClass.getAnnotation(CvrFieldRoot.class);
		// Check implemented interfaces if interface is not on the main class.
		if (rootValue == null) {
			Class<?>[] interfaces = providedClass.getInterfaces();
			for (Class<?> anInterface : interfaces) {
				CvrFieldRoot annotation = anInterface.getAnnotation(CvrFieldRoot.class);
				if (annotation != null) {
					rootValue = annotation;
					break;
				}
			}
		}

		return rootValue == null ? "" : rootValue.value() + ".";
	}
}
