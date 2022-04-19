package io.github.clivelewis.cvrclient.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Runtime annotation that you can use on the class/interface
 * to set prefix for all {@link CvrField} annotation values.
 * See {@link CvrField} for detailed explanation.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CvrFieldRoot {
	String value();
}
