// This is the original test - a few modifications were made to be compatible with
// the github.com/ngocdaothanh/annovention version that is in the maven repo

// - boolean parameters added to Discoverer.discover()
// - signature removed from listener declaration (see object listeners in other test)

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.impetus.annovention.ClasspathDiscoverer;
import com.impetus.annovention.Discoverer;
import com.impetus.annovention.listener.MethodAnnotationDiscoveryListener;

public class MyAnnotationDiscovererTest {

	public static void main (String args []) {
		Discoverer discoverer = new ClasspathDiscoverer();
		discoverer.addAnnotationListener(new MyAnnotationDiscoveryListener());
		discoverer.discover(true, true, true, true, true);
	}

	static class MyAnnotationDiscoveryListener implements MethodAnnotationDiscoveryListener {
		public void discovered(String clazz, String method, String annotation) {
			System.out.println("Discovered Method[" + clazz + "." + method + "] with Annotation("
					+ annotation + ")");

		}

		public String[] supportedAnnotations() {
			return new String[] { MyAnnotation.class.getName() };
		}
	}

	@Target(value = ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	private @interface MyAnnotation {
	}

	@MyAnnotation
	private void testMethod() {
	}

	@MyAnnotation
	private Boolean testMethod(Integer i) {
		return true;
	}

	@MyAnnotation
	private String testMethod(String i, Integer o) {
		return "true";
	}
}
