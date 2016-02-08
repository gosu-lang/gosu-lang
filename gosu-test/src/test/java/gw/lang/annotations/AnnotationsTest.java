/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.annotations;

import gw.lang.annotation.Annotations;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.test.TestClass;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

import junit.framework.Assert;

/**
 * Class description...
 *
 * @author pdalbora
 */
@TestAnnotation(s = "what", i = 34, arr = { "is", "this" }, primArr = { 100, 200, 300 })
public class AnnotationsTest extends TestClass {

  public void testBuildsSimpleAnnotation() {
    NoParamAnnotation annotation = Annotations.builder(NoParamAnnotation.class).create();
    Assert.assertNotNull(annotation);
  }

  public void testBuildsAnnotationWithElements() {
    TestAnnotation annotation = Annotations.builder(TestAnnotation.class)
            .withElement("s", "value1")
            .create();
    Assert.assertEquals("value1", annotation.s());
    Assert.assertEquals(42, annotation.i());
    Assert.assertTrue(Arrays.equals(new String[] {"hello", "world"}, annotation.arr()));
    Assert.assertTrue(Arrays.equals(new int[] {42}, annotation.primArr()));
  }

  public void testAnnotationType() {
    Assert.assertEquals(NoParamAnnotation.class, Annotations.builder(NoParamAnnotation.class).create().annotationType());
  }

  public void testEqualsHashCode() {
    TestAnnotation builtAnnotation = Annotations.builder(TestAnnotation.class)
            .withElement("s", "what")
            .withElement("i", 34)
            .withElement("arr", new String[] { "is", "this" })
            .withElement("primArr", new int[] { 100, 200, 300 })
            .create();
    TestAnnotation declaredAnnotation = getClass().getAnnotation(TestAnnotation.class);
    Assert.assertEquals(declaredAnnotation, builtAnnotation);
    Assert.assertEquals(declaredAnnotation.hashCode(), builtAnnotation.hashCode());
    TestAnnotation annotationDifferentInSimpleValue = Annotations.builder(TestAnnotation.class)
            .withElement("s", "diff")
            .withElement("i", 34)
            .withElement("arr", new String[] { "is", "this" })
            .withElement("primArr", new int[] { 100, 200, 300 })
            .create();
    Assert.assertFalse(annotationDifferentInSimpleValue.equals(declaredAnnotation));
    TestAnnotation annotationDifferentInArray = Annotations.builder(TestAnnotation.class)
            .withElement("s", "what")
            .withElement("i", 34)
            .withElement("arr", new String[] { "is", "this" })
            .withElement("primArr", new int[] { 100, -200, 300 })
            .create();
    Assert.assertFalse(annotationDifferentInArray.equals(declaredAnnotation));
  }

  public void testToString() {
    TestAnnotation builtAnnotation = Annotations.builder(TestAnnotation.class)
            .withElement("s", "what")
            .withElement("i", 34)
            .withElement("arr", new String[] { "is", "this" })
            .withElement("primArr", new int[] { 100, 200, 300 })
            .create();
    Assert.assertEquals(
            "@gw.lang.annotations.TestAnnotation(arr=[is, this], i=34, primArr=[100, 200, 300], s=what)",
            builtAnnotation.toString());
  }

  public void testArraysSafeFromMutation() {
    TestAnnotation annotation = Annotations.builder(TestAnnotation.class)
            .withElement("s", "what")
            .withElement("primArr", new int[] { 100, 200, 300 })
            .create();
    Assert.assertTrue(Arrays.equals(new int[] { 100, 200, 300 }, annotation.primArr()));
    annotation.primArr()[0] = -2;
    Assert.assertTrue("Array changed by caller!", Arrays.equals(new int[] { 100, 200, 300 }, annotation.primArr()));
  }

  public void testThrowsWhenAnnotationElementsNotPresent() {
    try {
      Annotations.create(TestAnnotation.class);
      Assert.fail();
    } catch (IllegalStateException e) {
      assertTrue(e.getMessage().contains("\"s\""));
    }
  }

  public void testThrowsWhenElementDoesNotExist() {
    try {
      Annotations.builder(TestAnnotation.class).withElement("bogus", 3);
      Assert.fail();
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("\"bogus\""));
    }
  }

  public void testThrowsWhenElementValueIncorrectType() {
    try {
      Annotations.builder(TestAnnotation.class).withElement("s", 3);
      Assert.fail();
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("\"s\""));
    }
  }

  public void testThrowsWhenElementValueIsObjectArrayWithNullElements() {
    try {
      Annotations.builder(TestAnnotation.class).withElement("arr", new String[] { "hello", null });
      Assert.fail();
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("\"arr\""));
    }
  }

  public void testRepeatAnnotations() {
    IGosuClass gsClass = (IGosuClass)TypeSystem.getByFullNameIfValid( "gw.lang.annotations.HasRepeatableAnnos" );
    gsClass.isValid();
    Method foo;
    try {
       foo = gsClass.getBackingClass().getMethod( "foo" );
    }
    catch( Exception e ) {
      throw new RuntimeException( e );
    }

    Annotation[] declaredAnnotations = foo.getDeclaredAnnotations();
    assertEquals( 1, declaredAnnotations.length );
    assertTrue( declaredAnnotations[0] instanceof MyAnnos );
    MyAnnos myAnnos = (MyAnnos)declaredAnnotations[0];

    MyAnno[] value = myAnnos.value();

    assertEquals( "hi", value[0].value() );
    assertEquals( String.class, value[0].type() );

    assertEquals( "bye", value[1].value() );
    assertEquals( String.class, value[1].type() );
  }
}
