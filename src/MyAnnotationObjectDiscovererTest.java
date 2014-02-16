/**
 * This tests a few of the newer features of annovention
 *
 * @author David Bennett - dbennett455@gmail.com
 */


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javassist.bytecode.ClassFile;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.MemberValue;

import com.impetus.annovention.ClasspathDiscoverer;
import com.impetus.annovention.Discoverer;
import com.impetus.annovention.listener.ClassAnnotationObjectDiscoveryListener;
import com.impetus.annovention.listener.FieldAnnotationObjectDiscoveryListener;
import com.impetus.annovention.listener.MethodAnnotationObjectDiscoveryListener;
import com.impetus.annovention.listener.MethodParameterAnnotationDiscoveryListener;
import com.impetus.annovention.listener.MethodParameterAnnotationObjectDiscoveryListener;
import com.impetus.annovention.util.MethodParameter;



@MyClassAnnotation(classValue="maodt")
public class MyAnnotationObjectDiscovererTest {

	@MyFieldAnnotation(fieldValue="aField annotation")
	String aField;

	public static void main (String args []) {
		Discoverer discoverer = new ClasspathDiscoverer();
		discoverer.addAnnotationListener(new MyMethodAnnotationObjectDiscoveryListener());
		discoverer.addAnnotationListener(new MyClassAnnotationObjectDiscoveryListener());
		discoverer.addAnnotationListener(new MyFieldAnnotationObjectDiscoveryListener());
		discoverer.addAnnotationListener(new MyMethodParameterAnnotationDiscoveryListener());  // plain jane
		discoverer.addAnnotationListener(new MyMethodParameterAnnotationObjectDiscoveryListener());
		discoverer.discover(true, true, true, true, true, true);
	}

	/**
	 * Report on class annotation with parameter
	 *
	 */
	static class MyClassAnnotationObjectDiscoveryListener implements ClassAnnotationObjectDiscoveryListener {
		public void discovered(ClassFile clazz, Annotation annotation) {

			// get annotation value
			MemberValue myParamVal=annotation.getMemberValue("classValue");
			String myValue=null;
			if (myParamVal != null)
				myValue=myParamVal.toString();

			System.out.println("Discovered Class[" + clazz.getName() +
					"] with Annotation (" + annotation.getTypeName() +
					(myValue == null ? "" : (" value="+myValue))  + ")");
		}

		public String[] supportedAnnotations() {
			return new String[] { MyClassAnnotation.class.getName() };
		}
	}


	/**
	 * Report on method annotation with and without parameters
	 *
	 */
	static class MyMethodAnnotationObjectDiscoveryListener implements MethodAnnotationObjectDiscoveryListener {
		public void discovered(ClassFile clazz, MethodInfo method, Annotation annotation) {

			// get annotation value
			MemberValue myParamVal=annotation.getMemberValue("value");
			String myValue=null;
			if (myParamVal != null)
				myValue=myParamVal.toString();

			System.out.println("Discovered Method[" + clazz.getName() + "." + method.getName() + "(" + method.getDescriptor() + ")" + "] with Annotation("
					+ annotation.getTypeName() + (myValue == null ? "" : (" value="+myValue)) + ")" );

		}

		public String[] supportedAnnotations() {
			return new String[] { MyMethodAnnotation.class.getName() };
		}
	}

	/**
	 *
	 * Report on method parameter annotations using the classic String listener style.
	 *
	 */
	static class MyMethodParameterAnnotationDiscoveryListener implements MethodParameterAnnotationDiscoveryListener {
		public void discovered(String clazz, String method, int parameterIndex, String parameterType, String annotation) {

			System.out.println("Discovered Parameter (String listener) on Method[" + clazz + "." + method
					+ "] index:" + parameterIndex + " type: " + parameterType
					+  " with Annotation: " + annotation);
		}

		public String[] supportedAnnotations() {
			return new String[] { MyParameterAnnotation.class.getName() };
		}
	}

	/**
	 *
	 * Report on method parameter annotations with and without parameters using the new Javassist object listener style
	 *
	 */
	static class MyMethodParameterAnnotationObjectDiscoveryListener implements MethodParameterAnnotationObjectDiscoveryListener {
		public void discovered(ClassFile clazz, MethodInfo method, MethodParameter methodParameter, Annotation annotation) {

			// get annotation value
			MemberValue myParamVal=annotation.getMemberValue("paramValue");
			String myValue=null;
			if (myParamVal != null)
				myValue=myParamVal.toString();

			System.out.println("Discovered Parameter on Method[" + clazz.getName() + "." + method.getName() + "(" + method.getDescriptor() + ")"
					+ "] index:" + methodParameter.getIndex() + " type: " + methodParameter.getType()
					+  " with Annotation(" + annotation.getTypeName() + (myValue == null ? "" : (" paramValue="+myValue)) + ")" );
		}

		public String[] supportedAnnotations() {
			return new String[] { MyParameterAnnotation.class.getName() };
		}
	}


	/**
	 * Report on a field annotation with parameter using new object style listener
	 *
	 */

	static class MyFieldAnnotationObjectDiscoveryListener implements FieldAnnotationObjectDiscoveryListener {
		public void discovered(ClassFile clazz, FieldInfo fld, Annotation annotation) {

			// get annotation value
			MemberValue myFieldVal=annotation.getMemberValue("fieldValue");
			String myValue=null;
			if (myFieldVal != null)
				myValue=myFieldVal.toString();

			System.out.println("Discovered Field[" + clazz.getName() + "." + fld.getName() + "] with Annotation("
					+ annotation.getTypeName() + (myValue == null ? "" : (" fieldValue="+myValue)) + ")" );

		}

		public String[] supportedAnnotations() {
			return new String[] { MyFieldAnnotation.class.getName() };
		}
	}

	// below are some defintions to scan for

	@Target({ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	private @interface MyMethodAnnotation {
		String value() default "";
	}

	@Target({ElementType.FIELD})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	private @interface MyFieldAnnotation {
		String fieldValue() default "";
	}


	@Target({ElementType.PARAMETER})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	private @interface MyParameterAnnotation {
		String paramValue() default "";
	}

	@MyMethodAnnotation("object param")
	private void testMethod(Object o) {
	}

	@MyMethodAnnotation("MyParam")
	private void testMethod() {
	}

	@MyMethodAnnotation
	private Boolean testMethod(@MyParameterAnnotation(paramValue="Integer[0]") Integer i) {
		String s="hello";
		if (s.indexOf("z") > -1)
			return false;
		return true;
	}

	@MyMethodAnnotation
	private String testMethod(@MyParameterAnnotation(paramValue="String[0] (2D Array)") String[][] ary, @MyParameterAnnotation(paramValue="Integer[1]") Integer i) {
		return "true";
	}

	@MyMethodAnnotation
	private int testMethod(@MyParameterAnnotation(paramValue="int[0]") int i,
					@MyParameterAnnotation(paramValue="long[1]") long l,
					@MyParameterAnnotation(paramValue="Object[2]") Object o) {
		return 0;
	}

}
