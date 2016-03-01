/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.annotation;

import gw.internal.gosu.parser.IGosuClassInternal;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.reflect.ReflectUtil;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.TypeSystem;
import gw.test.TestClass;

import java.util.List;

/**
 * User: dbrewster
 * Date: May 9, 2007
 * Time: 9:53:34 PM
 */
public class JavaAnnotationsInGosuTest extends TestClass {

  public void testSimpleClassHasCorrectJavaAnnotation() throws ParseResultsException {
    IGosuClassInternal gosuClass = (IGosuClassInternal) ReflectUtil.getClass("gw.internal.gosu.parser.annotation.gwtest.annotation.java.ClassWithLegalJavaReference");
    gosuClass.compileDeclarationsIfNeeded();
    if (gosuClass.hasError()) {
      throw gosuClass.getParseResultsException();
    }
    List<? extends IAnnotationInfo> annotation = gosuClass.getTypeInfo().getAnnotationsOfType(TypeSystem.get(TestClassAnnotation.class));
    assertNotNull(annotation);
    assertEquals(1, annotation.size());
    TestClassAnnotation testAnnotation = (TestClassAnnotation) annotation.get(0).getInstance();
    assertNotNull(testAnnotation);
  }

  public void testJavaAnnotationWithDataHasCorrectData() throws ParseResultsException {
    IGosuClassInternal gosuClass = (IGosuClassInternal) ReflectUtil.getClass("gw.internal.gosu.parser.annotation.gwtest.annotation.java.ClassWithJavaAnnotationWithData");
    gosuClass.compileDeclarationsIfNeeded();
    if (gosuClass.hasError()) {
      throw gosuClass.getParseResultsException();
    }
    List<? extends IAnnotationInfo> annotation = gosuClass.getTypeInfo().getAnnotationsOfType(TypeSystem.get(TestClassAnnotationWithArgs.class));
    assertNotNull(annotation);
    assertEquals(1, annotation.size());
    IAnnotationInfo testAnnotation = annotation.get(0);
    assertNotNull( testAnnotation );
    assertEquals( TestClassAnnotationWithArgs.class.getName(), testAnnotation.getType().getName() );
    IAnnotationInfo[] value = (IAnnotationInfo[])testAnnotation.getFieldValue( "value" );
    assertEquals(2, value.length);
    assertEquals(TestClassAnnotationWithArgsEndpoint.AuthenticationTypes.None.name(), value[0].getFieldValue( "authType" ) );
    assertEquals(TestClassAnnotationWithArgsEndpoint.AuthenticationTypes.Http.name(), value[1].getFieldValue( "authType" ) );
  }

  public void testEqualsReturnsCorrectValue() throws ParseResultsException {
    IGosuClassInternal gosuClass = (IGosuClassInternal) ReflectUtil.getClass("gw.internal.gosu.parser.annotation.gwtest.annotation.java.ClassWithJavaAnnotationWithData");
    gosuClass.compileDeclarationsIfNeeded();
    if (gosuClass.hasError()) {
      throw gosuClass.getParseResultsException();
    }
    List<? extends IAnnotationInfo> annotation = gosuClass.getTypeInfo().getAnnotationsOfType(TypeSystem.get(TestClassAnnotationWithArgs.class));
    assertNotNull(annotation);
    assertEquals(1, annotation.size());
    TestClassAnnotationWithArgs testAnnotation = (TestClassAnnotationWithArgs) annotation.get(0).getInstance();
    TestClassAnnotationWithArgsEndpoint argsEndpoint1 = testAnnotation.value()[0];
    TestClassAnnotationWithArgsEndpoint argsEndpoint2 = testAnnotation.value()[1];
    assertTrue(argsEndpoint1.equals(argsEndpoint1));
    assertFalse(argsEndpoint1.equals(argsEndpoint2));
  }

  public void testHashCodeReturnsCorrectValue() throws ParseResultsException {
    IGosuClassInternal gosuClass = (IGosuClassInternal) ReflectUtil.getClass("gw.internal.gosu.parser.annotation.gwtest.annotation.java.ClassWithJavaAnnotationWithData");
    gosuClass.compileDeclarationsIfNeeded();
    if (gosuClass.hasError()) {
      throw gosuClass.getParseResultsException();
    }
    List<? extends IAnnotationInfo> annotation = gosuClass.getTypeInfo().getAnnotationsOfType(TypeSystem.get(TestClassAnnotationWithArgs.class));
    assertNotNull(annotation);
    assertEquals(1, annotation.size());
    TestClassAnnotationWithArgs testAnnotation = (TestClassAnnotationWithArgs) annotation.get(0).getInstance();
    TestClassAnnotationWithArgsEndpoint argsEndpoint1 = testAnnotation.value()[0];
    TestClassAnnotationWithArgsEndpoint argsEndpoint2 = testAnnotation.value()[1];
    assertEquals(argsEndpoint1.hashCode(), argsEndpoint1.hashCode());
    assertFalse(argsEndpoint1.hashCode() == argsEndpoint2.hashCode());
  }

  public void testToStringDoesntBlowUp() throws ParseResultsException {
    IGosuClassInternal gosuClass = (IGosuClassInternal) ReflectUtil.getClass("gw.internal.gosu.parser.annotation.gwtest.annotation.java.ClassWithJavaAnnotationWithData");
    gosuClass.compileDeclarationsIfNeeded();
    if (gosuClass.hasError()) {
      throw gosuClass.getParseResultsException();
    }
    List<? extends IAnnotationInfo> annotation = gosuClass.getTypeInfo().getAnnotationsOfType(TypeSystem.get(TestClassAnnotationWithArgs.class));
    assertNotNull(annotation);
    assertEquals(1, annotation.size());
    TestClassAnnotationWithArgs testAnnotation = (TestClassAnnotationWithArgs) annotation.get(0).getInstance();
    assertNotNull(testAnnotation.toString());
  }

  public void testAnnotationsCanBeReloadedOverARefresh() throws ParseResultsException
  {
    IGosuClassInternal gosuClass = (IGosuClassInternal) ReflectUtil.getClass("gw.internal.gosu.parser.annotation.gwtest.annotation.java.ClassWithLegalJavaReference");
    gosuClass.compileDeclarationsIfNeeded();
    if (gosuClass.hasError()) {
      throw gosuClass.getParseResultsException();
    }
    List<? extends IAnnotationInfo> annotation = gosuClass.getTypeInfo().getAnnotationsOfType(TypeSystem.get(TestClassAnnotation.class));
    assertNotNull(annotation);
    assertEquals(1, annotation.size());
    TestClassAnnotation testAnnotation = (TestClassAnnotation) annotation.get(0).getInstance();
    assertNotNull(testAnnotation);

    TypeSystem.refresh(false);

    // Call a method and make sure it doesn't go boom
    testAnnotation.annotationType();
  }

  public void testAnnotationsCanBeCreatedAfterARefresh() throws ParseResultsException {
    IGosuClassInternal gosuClass = (IGosuClassInternal) ReflectUtil.getClass("gw.internal.gosu.parser.annotation.gwtest.annotation.java.ClassWithLegalJavaReference");
    gosuClass.compileDeclarationsIfNeeded();
    if (gosuClass.hasError()) {
      throw gosuClass.getParseResultsException();
    }
    List<? extends IAnnotationInfo> annotation = gosuClass.getTypeInfo().getAnnotationsOfType(TypeSystem.get(TestClassAnnotation.class));
    assertNotNull(annotation);
    assertEquals(1, annotation.size());
    TestClassAnnotation testAnnotation = (TestClassAnnotation) annotation.get(0).getInstance();
    assertNotNull(testAnnotation);

    TypeSystem.refresh(false);

    gosuClass = (IGosuClassInternal) ReflectUtil.getClass("gw.internal.gosu.parser.annotation.gwtest.annotation.java.ClassWithLegalJavaReference");
    gosuClass.compileDeclarationsIfNeeded();
    if (gosuClass.hasError()) {
      throw gosuClass.getParseResultsException();
    }
    annotation = gosuClass.getTypeInfo().getAnnotationsOfType(TypeSystem.get(TestClassAnnotation.class));
    assertNotNull(annotation);
    assertEquals(1, annotation.size());
    testAnnotation = (TestClassAnnotation) annotation.get(0).getInstance();
    assertNotNull(testAnnotation);
  }

//  private void assertEndpointEquals(String name, String wsdl, int timeout,
//                                    EndPointAuthentication.AuthenticationTypes authenticationTypes,
//                                    String userName, String password, TestClassAnnotationWithArgsEndpoint endpoint) {
//    assertEquals(name, endpoint.name());
//    assertEquals(wsdl, endpoint.wsdlLocation());
//    assertEquals(timeout, endpoint.callTimeout());
//    assertEquals(authenticationTypes, endpoint.authType());
//    assertEquals(userName, endpoint.userName());
//    assertEquals(password, endpoint.password());
//  }
}
