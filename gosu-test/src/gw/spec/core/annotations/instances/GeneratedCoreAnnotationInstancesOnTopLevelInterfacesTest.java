/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.core.annotations.instances;

import gw.test.TestClass;
import gw.lang.reflect.*;

import java.util.*;

public class GeneratedCoreAnnotationInstancesOnTopLevelInterfacesTest extends TestClass {

  private Map<IType, List<IAnnotationInfo>> map(List<IAnnotationInfo> annotations) {
    HashMap<IType, List<IAnnotationInfo>> map = new HashMap<IType, List<IAnnotationInfo>>();
    for (IAnnotationInfo annotation : annotations) {
      List<IAnnotationInfo> infoList = map.get(annotation.getType());
      if (infoList == null) {
        infoList = new ArrayList<IAnnotationInfo>();
        map.put(annotation.getType(), infoList);
      }
      infoList.add(annotation);
    }
    return map;
  }
  public void test_gosu_deprecated_relative(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderInterface_gosu_deprecated_relative" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderInterface_gosu_deprecated_relative_comment_before" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderInterface_gosu_deprecated_relative_comment_after" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderInterface_gosu_deprecated_relative_comment_before_and_after" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderInterface_gosu_deprecated_fully_qualified" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderInterface_gosu_deprecated_in_comment" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderInterface_gosu_deprecated_in_comment_w_text_before" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderInterface_gosu_deprecated_in_comment_multi_line" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_deprecated_fully_qualified_no_parens(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderInterface_java_deprecated_fully_qualified_no_parens" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderInterface_java_deprecated_fully_qualified_w_parens" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_local_relative_no_parens_no_args(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderInterface_java_local_relative_no_parens_no_args" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderInterface_java_local_relative_parens_no_args" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderInterface_java_local_relative_parens_with_arg" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_local_relative_no_parens_no_args(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderInterface_gosu_local_relative_no_parens_no_args" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderInterface_gosu_local_relative_parens_no_args" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderInterface_gosu_local_relative_parens_with_arg" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderInterface_gosu_multi_no_comment" ).getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderInterface_gosu_multi_comment_before" ).getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderInterface_gosu_multi_comment_between" ).getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderInterface_gosu_multi_comment_after" ).getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderInterface_gosu_multi_comment_before_w_annotation" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderInterface_gosu_multi_comment_between_w_annotation" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderInterface_gosu_multi_comment_after_w_annotation" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

}