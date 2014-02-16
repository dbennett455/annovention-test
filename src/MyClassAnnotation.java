/**
 * This is a parameterized class annotation used for the the MyAnnotationObjectDiscovererTest
 *
 */

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyClassAnnotation {
	String classValue() default "";
}
