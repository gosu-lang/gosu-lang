/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.annotation;

import gw.internal.gosu.parser.IGosuClassInternal;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.TypeSystem;
import gw.test.TestClass;

import java.util.List;

/**
 * User: dbrewster
 * Date: Mar 17, 2007
 * Time: 8:53:47 PM
 */
public class AnnotationUsageTest extends TestClass {

  public void testAnnotationWithNoUsageDefaultsToAllowManyEverywhere() {
    IGosuClassInternal gosuClass = (IGosuClassInternal) TypeSystem.getByFullName("gw.internal.gosu.parser.annotation.gwtest.annotation.AnnotationUsageTestNoUsage");
    // test that the annotations are there at decl time
    List<IAnnotationInfo> annotation = gosuClass.getTypeInfo().getProperty("varFeature").getAnnotationsOfType((IGosuClassInternal) TypeSystem.getByFullName("gw.internal.gosu.parser.annotation.gwtest.annotation.AnnotationUsageTestAnnotationNoUsage"));
    assertEquals(2, annotation.size());
    annotation = gosuClass.getTypeInfo().getProperty("varProperty").getAnnotationsOfType((IGosuClassInternal) TypeSystem.getByFullName("gw.internal.gosu.parser.annotation.gwtest.annotation.AnnotationUsageTestAnnotationNoUsage"));
    assertEquals(2, annotation.size());
    annotation = gosuClass.getTypeInfo().getMethod("varFunction").getAnnotationsOfType((IGosuClassInternal) TypeSystem.getByFullName("gw.internal.gosu.parser.annotation.gwtest.annotation.AnnotationUsageTestAnnotationNoUsage"));
    assertEquals(2, annotation.size());
    annotation = gosuClass.getTypeInfo().getConstructor().getAnnotationsOfType((IGosuClassInternal) TypeSystem.getByFullName("gw.internal.gosu.parser.annotation.gwtest.annotation.AnnotationUsageTestAnnotationNoUsage"));
    assertEquals(2, annotation.size());

    // Now tests they are there at defn time
    gosuClass.isValid();
    annotation = gosuClass.getTypeInfo().getProperty("varFeature").getAnnotationsOfType((IGosuClassInternal) TypeSystem.getByFullName("gw.internal.gosu.parser.annotation.gwtest.annotation.AnnotationUsageTestAnnotationNoUsage"));
    assertEquals(2, annotation.size());
    annotation = gosuClass.getTypeInfo().getProperty("varProperty").getAnnotationsOfType((IGosuClassInternal) TypeSystem.getByFullName("gw.internal.gosu.parser.annotation.gwtest.annotation.AnnotationUsageTestAnnotationNoUsage"));
    assertEquals(2, annotation.size());
    annotation = gosuClass.getTypeInfo().getMethod("varFunction").getAnnotationsOfType((IGosuClassInternal) TypeSystem.getByFullName("gw.internal.gosu.parser.annotation.gwtest.annotation.AnnotationUsageTestAnnotationNoUsage"));
    assertEquals(2, annotation.size());
    annotation = gosuClass.getTypeInfo().getConstructor().getAnnotationsOfType((IGosuClassInternal) TypeSystem.getByFullName("gw.internal.gosu.parser.annotation.gwtest.annotation.AnnotationUsageTestAnnotationNoUsage"));
    assertEquals(2, annotation.size());
  }

  public void testAnnotationInheritanceWorksOnFeatures() {
    IGosuClassInternal gosuClass = (IGosuClassInternal) TypeSystem.getByFullName("gw.internal.gosu.parser.annotation.gwtest.annotation.ExtendsAnnotationUsageTestOneUsagePerPart");
    List<IAnnotationInfo> annotation = gosuClass.getTypeInfo().getProperty( "varProperty" ).getAnnotationsOfType( TypeSystem.getByFullName( "gw.internal.gosu.parser.annotation.gwtest.annotation.AnnotationUsageTestOneOnProperty" ) );
    assertEquals(1, annotation.size());
    annotation = gosuClass.getTypeInfo().getMethod("varFunction").getAnnotationsOfType( TypeSystem.getByFullName("gw.internal.gosu.parser.annotation.gwtest.annotation.AnnotationUsageTestOneOnMethod") );
    assertEquals(1, annotation.size());
  }

  public void testAnnotationsWithOnlyUsageOnCorrectPartOfClassWorks() {
    // This test class contains annotations that are only allowed once on the corresponding part of the source file.
    TypeSystem.getByFullName("gw.internal.gosu.parser.annotation.gwtest.annotation.AnnotationUsageTestOneUsagePerPart");
    // If the class parsed then there aren't any errors
  }

  public void testAnnotationWithBadUsageReportsCorrectErrors() {
    // The test class has bad attributes on every type of part.  It should produce 5 errors
    IGosuClassInternal clazz = (IGosuClassInternal) TypeSystem.getByFullName("gw.internal.gosu.parser.annotation.gwtest.annotation.AnnotationUsageTestBadOneUsagePerPart");
    assertFalse(clazz.isValid());
    ParseResultsException resultsException = clazz.getParseResultsException();
    assertNotNull("Expected error", resultsException);
    assertEquals(5, resultsException.getParseExceptions().size());

  }

  public void testAnnotationWithBadCardinalityUsageReportsCorrectErrors() {
    // The test class has bad attributes on every type of part.  It should produce 5 errors
    IGosuClassInternal clazz = (IGosuClassInternal) TypeSystem.getByFullName("gw.internal.gosu.parser.annotation.gwtest.annotation.AnnotationUsageTestBadCardinality");
    assertFalse(clazz.isValid());
    ParseResultsException resultsException = clazz.getParseResultsException();
    assertNotNull("Expected error", resultsException);
    assertEquals(5, resultsException.getParseExceptions().size());
  }

}
