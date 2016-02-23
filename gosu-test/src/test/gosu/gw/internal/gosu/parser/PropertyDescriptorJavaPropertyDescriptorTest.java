package gw.internal.gosu.parser;

import gw.lang.reflect.ReflectUtil;
import gw.lang.reflect.java.IJavaClassMethod;
import gw.test.TestClass;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.ref.SoftReference;

public class PropertyDescriptorJavaPropertyDescriptorTest extends TestClass {

  public static final String GET_PROP1 = "java.lang.String gw.internal.gosu.parser.PropertyDescriptorJavaPropertyDescriptorTest$Example.getProp1()";
  private static final String SET_PROP1 = "gw.internal.gosu.parser.PropertyDescriptorJavaPropertyDescriptorTest$Example.setProp1(java.lang.String)";

  public void testPrivateSetterReresolves() throws IntrospectionException
  {

    // only run on sun/oracle JREs
    if(!PropertyDescriptorJavaPropertyDescriptor.shouldFixJREMethodRefIssue()) {
      return;
    }

    PropertyDescriptor pd = new PropertyDescriptor("Prop1",
                                                   PropertyDescriptorJavaPropertyDescriptor.findMethod0(Example.class, GET_PROP1),
                                                   PropertyDescriptorJavaPropertyDescriptor.findMethod0(Example.class, SET_PROP1)
    );

    PropertyDescriptorJavaPropertyDescriptor pdjpd = new PropertyDescriptorJavaPropertyDescriptor(pd, null);

    IJavaClassMethod writeMethod = pdjpd.getWriteMethod();

    // Should work first time
    assertNotNull(writeMethod);

    // Null out the soft ref, should trigger NPE in MethodRev
    Object writeMethodRef = ReflectUtil.getProperty(pd, "writeMethodRef");
    ReflectUtil.setProperty(writeMethodRef, "methodRef", new SoftReference(null));
    try {
      ReflectUtil.invokeMethod(writeMethodRef, "get");
      fail("Expected an NPE");
    } catch (NullPointerException npe) {
      //pass
    }

    // re-null out the soft-ref (hell of a job controlling state, Soracle...)
    ReflectUtil.setProperty(writeMethodRef, "methodRef", new SoftReference(null));

    // Should re-resolve the second time
    writeMethod = pdjpd.getWriteMethod();
    assertNotNull(writeMethod);
  }

  public void testPrivateGetterReresolves() throws IntrospectionException
  {

    // only run on sun/oracle JREs
    if(!PropertyDescriptorJavaPropertyDescriptor.shouldFixJREMethodRefIssue()) {
      return;
    }

    PropertyDescriptor pd = new PropertyDescriptor("Prop1",
                                                   PropertyDescriptorJavaPropertyDescriptor.findMethod0(Example.class, GET_PROP1),
                                                   PropertyDescriptorJavaPropertyDescriptor.findMethod0(Example.class, SET_PROP1)
    );

    PropertyDescriptorJavaPropertyDescriptor pdjpd = new PropertyDescriptorJavaPropertyDescriptor(pd, null);

    IJavaClassMethod readMethod = pdjpd.getReadMethod();

    // Should work first time
    assertNotNull(readMethod);

    // Null out the soft ref, should trigger NPE in MethodRev
    Object writeMethodRef = ReflectUtil.getProperty(pd, "writeMethodRef");
    ReflectUtil.setProperty(writeMethodRef, "methodRef", new SoftReference(null));
    try {
      ReflectUtil.invokeMethod(writeMethodRef, "get");
      fail("Expected an NPE");
    } catch (NullPointerException npe) {
      //pass
    }

    // re-null out the soft-ref (hell of a job controlling state, Soracle...)
    ReflectUtil.setProperty(writeMethodRef, "methodRef", new SoftReference(null));

    // Should re-resolve the second time
    readMethod = pdjpd.getReadMethod();
    assertNotNull(readMethod);
  }

  public static class Example {
    String _foo;

    private String getProp1() {
      return _foo;
    }

    private void setProp1(String _foo) {
      this._foo = _foo;
    }
  }

}
