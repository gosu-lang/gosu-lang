/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.annotation;

import gw.internal.gosu.parser.IGosuClassInternal;
import gw.test.AnnotationTestJavaAnnotation;
import gw.lang.annotation.AnnotationUsage;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.test.TestClass;
import gw.util.AnnotationUtil;

import java.util.List;
import java.util.Map;

/**
 * User: dbrewster
 * Date: Mar 17, 2007
 * Time: 12:24:49 PM
 */
public class AnnotationTest extends TestClass {

  public void testAnnotationsCanLiveOnCorrectAnnotationClasses() {
    // Tests that meta-annotations can live on annotation classes and
    //  non-metat-annotations can live on regular classes
    IGosuClassInternal annotationClass = (IGosuClassInternal) TypeSystem.getByFullName("gw.internal.gosu.parser.annotation.gwtest.annotation.AnnotationTestAnnotation");
    IType type = TypeSystem.getByFullName("gw.internal.gosu.parser.annotation.gwtest.annotation.AnnotationTestMetaAnnotation");
    IAnnotationInfo annotation = annotationClass.getTypeInfo().getAnnotationsOfType(type).get(0);
    assertEquals(type, annotation.getType());

    IGosuClassInternal simpleClass = (IGosuClassInternal) TypeSystem.getByFullName("gw.internal.gosu.parser.annotation.gwtest.annotation.AnnotationTestClass");
    IType annoType = TypeSystem.getByFullName("gw.internal.gosu.parser.annotation.gwtest.annotation.AnnotationTestAnnotation");
    annotation = simpleClass.getTypeInfo().getAnnotationsOfType(annoType).get(0);
    assertEquals(annoType, annotation.getType());
  }

  public void testAnnotationIsInherited() {
    getAnnotationsOnType("gw.internal.gosu.parser.annotation.gwtest.annotation.AnnotationTestAnnotationIsInherited", 1, TypeSystem.get(AnnotationUsage.class));
  }

  public void testGetAnnotationsReturnsInheritedAnnotations() {
    getAnnotationsOnType("gw.internal.gosu.parser.annotation.gwtest.annotation.AnnotationTestAnnotationIsInherited", 1, TypeSystem.get(AnnotationUsage.class));
  }

  public void testGosuAnnotationPointsToCorrectContainer() {
    Map<IType, List<IAnnotationInfo>> annotations = getAnnotationsOnType("gw.internal.gosu.parser.annotation.gwtest.annotation.AnnotationTestAnnotationIsInherited", 1, TypeSystem.get(AnnotationUsage.class));
    List<IAnnotationInfo> annotation = annotations.get(TypeSystem.get(AnnotationUsage.class));
    assertNotNull(annotation);
    assertEquals(1, annotation.size());
    assertEquals(TypeSystem.getByFullName("gw.internal.gosu.parser.annotation.gwtest.annotation.AnnotationUsageTestOneOnClass"), annotation.get(0).getOwnersType());
  }

  public void testGosuAnnotationOfChildIsNotOnParent() {
    Map<IType, List<IAnnotationInfo>> annotations = getAnnotationsOnType("gw.internal.gosu.parser.annotation.gwtest.annotation.AnnotationUsageTestOneOnClass", 1, TypeSystem.get(AnnotationUsage.class));
    assertFalse(annotations.keySet().iterator().next() == TypeSystem.getByFullName("gw.lang.Deprecated"));
  }

  public void testGosuAnnotationOfSameAnnotationCanAppearAtMultipleLevelsInHierarchy() {
    Map<IType, List<IAnnotationInfo>> annotations = getAnnotationsOnType("gw.internal.gosu.parser.annotation.gwtest.annotation.AnnotationTestMultipleAnnotationsLevel2", 2, TypeSystem.getByFullName("gw.internal.gosu.parser.annotation.gwtest.annotation.AnnotationTestAnnotation"));
    List<IAnnotationInfo> annotationInfos = annotations.get(TypeSystem.getByFullName("gw.internal.gosu.parser.annotation.gwtest.annotation.AnnotationTestAnnotation"));
    assertNotNull(annotationInfos);
    assertEquals(2, annotationInfos.size());
    boolean found1 = false, found2 = false;
    IType metaAnnotationType = TypeSystem.getByFullName("gw.internal.gosu.parser.annotation.gwtest.annotation.AnnotationTestAnnotation");
    for (IAnnotationInfo annotationInfo : annotationInfos) {
      assertEquals(metaAnnotationType, annotationInfo.getType());
      if (annotationInfo.getInstance().toString().equals("Level1")) {
        found1 = true;
      } else if (annotationInfo.getInstance().toString().equals("Level2")) {
        found2 = true;
      } else {
        throw new IllegalStateException("Unexpected value");
      }
    }
    assertTrue(found1);
    assertTrue(found2);
  }

  public void testGosuClassInheritsAnnotationFromJavaClass() {
    Map<IType, List<IAnnotationInfo>> annotations = getAnnotationsOnType("gw.internal.gosu.parser.annotation.gwtest.annotation.AnnotationTestGosuClassInheritsAnnotationFromJavaClass", 1, TypeSystem.get(AnnotationTestJavaAnnotation.class));
    List<IAnnotationInfo> annotationInfos = annotations.get(TypeSystem.get(AnnotationTestJavaAnnotation.class));
    assertNotNull(annotationInfos);
    assertEquals(1, annotationInfos.size());
  }

  public void testJavaClassInheritsAnnotationFromJavaClassViaJavaIntrinsicType() {
    Map<IType, List<IAnnotationInfo>> annotations = getAnnotationsOnType(AnnotationTestJavaClassTwo.class.getName(), 1, TypeSystem.get(AnnotationTestJavaAnnotation.class));
    List<IAnnotationInfo> annotationInfos = annotations.get(TypeSystem.get(AnnotationTestJavaAnnotation.class));
    assertNotNull(annotationInfos);
    assertEquals(1, annotationInfos.size());
  }

  public void testGosuClassWithGosuParentInheritsAnnotationFromJavaClassTwoLevelsUp() {
    Map<IType, List<IAnnotationInfo>> annotations = getAnnotationsOnType("gw.internal.gosu.parser.annotation.gwtest.annotation.AnnotationTestGosuClassInheritsAnnotationFromJavaClassLevel2", 1, TypeSystem.get(AnnotationTestJavaAnnotation.class));
    List<IAnnotationInfo> annotationInfos = annotations.get(TypeSystem.get(AnnotationTestJavaAnnotation.class));
    assertNotNull(annotationInfos);
    assertEquals(1, annotationInfos.size());
  }

  public void testGosuClassWithJavaParentInheritsAnnotationFromJavaClassTwoLevelsUp() {
    Map<IType, List<IAnnotationInfo>> annotations = getAnnotationsOnType("gw.internal.gosu.parser.annotation.gwtest.annotation.AnnotationTestGosuClassInheritsAnnotationFromJavaClassTwoDeep", 1, TypeSystem.get(AnnotationTestJavaAnnotation.class));
    List<IAnnotationInfo> annotationInfos = annotations.get(TypeSystem.get(AnnotationTestJavaAnnotation.class));
    assertNotNull(annotationInfos);
    assertEquals(1, annotationInfos.size());
  }


  private Map<IType, List<IAnnotationInfo>> getAnnotationsOnType(String fullyQualifiedName, int expectedNumberOfAnnotationTypes, IType annotationName) {
    IType type = TypeSystem.getByFullName(fullyQualifiedName);
    if (type instanceof IGosuClassInternal ) {
      ((IGosuClassInternal) type).compileDeclarationsIfNeeded();
    }
    List<IAnnotationInfo> annotations = type.getTypeInfo().getAnnotationsOfType(annotationName);
    assertNotNull(annotations);
    assertEquals(expectedNumberOfAnnotationTypes, annotations.size());
    if (annotationName != null) {
      assertTrue(type.getTypeInfo().hasAnnotation(annotationName));
    }
    return AnnotationUtil.map(annotations);
  }
}
