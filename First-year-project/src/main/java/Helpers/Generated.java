package Helpers;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Ignores the annotated element when testing.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Generated {
}

