/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.core.annotations.instances;

import gw.test.TestClass;
import gw.lang.reflect.*;

import java.util.*;

public class GeneratedCoreAnnotationInstancesOnTopLevelClassTest extends TestClass {

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
  public void test_gosu_deprecated_relative_(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_deprecated_relative_" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_public(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_deprecated_relative_public" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_internal(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_deprecated_relative_internal" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_deprecated_relative_comment_before_" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_public(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_deprecated_relative_comment_before_public" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_internal(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_deprecated_relative_comment_before_internal" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after_(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_deprecated_relative_comment_after_" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after_public(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_deprecated_relative_comment_after_public" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_after_internal(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_deprecated_relative_comment_after_internal" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after_(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_deprecated_relative_comment_before_and_after_" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after_public(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_deprecated_relative_comment_before_and_after_public" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_relative_comment_before_and_after_internal(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_deprecated_relative_comment_before_and_after_internal" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified_(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_deprecated_fully_qualified_" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified_public(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_deprecated_fully_qualified_public" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_fully_qualified_internal(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_deprecated_fully_qualified_internal" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_deprecated_in_comment_" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_public(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_deprecated_in_comment_public" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_internal(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_deprecated_in_comment_internal" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before_(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_deprecated_in_comment_w_text_before_" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before_public(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_deprecated_in_comment_w_text_before_public" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_w_text_before_internal(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_deprecated_in_comment_w_text_before_internal" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line_(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_deprecated_in_comment_multi_line_" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line_public(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_deprecated_in_comment_multi_line_public" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_deprecated_in_comment_multi_line_internal(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_deprecated_in_comment_multi_line_internal" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo\nbar", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_deprecated_fully_qualified_no_parens_(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_java_deprecated_fully_qualified_no_parens_" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens_public(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_java_deprecated_fully_qualified_no_parens_public" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_no_parens_internal(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_java_deprecated_fully_qualified_no_parens_internal" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens_(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_java_deprecated_fully_qualified_w_parens_" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens_public(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_java_deprecated_fully_qualified_w_parens_public" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_deprecated_fully_qualified_w_parens_internal(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_java_deprecated_fully_qualified_w_parens_internal" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );
  }

  public void test_java_local_relative_no_parens_no_args_(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_java_local_relative_no_parens_no_args_" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args_public(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_java_local_relative_no_parens_no_args_public" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_no_parens_no_args_internal(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_java_local_relative_no_parens_no_args_internal" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args_(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_java_local_relative_parens_no_args_" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args_public(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_java_local_relative_parens_no_args_public" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_no_args_internal(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_java_local_relative_parens_no_args_internal" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "default", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg_(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_java_local_relative_parens_with_arg_" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg_public(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_java_local_relative_parens_with_arg_public" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_java_local_relative_parens_with_arg_internal(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_java_local_relative_parens_with_arg_internal" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalJavaAnnotation" ) ).size() );
    assertEquals( "foo", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getInstance()).value() );
  }

  public void test_gosu_local_relative_no_parens_no_args_(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_local_relative_no_parens_no_args_" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args_public(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_local_relative_no_parens_no_args_public" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_no_parens_no_args_internal(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_local_relative_no_parens_no_args_internal" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args_(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_local_relative_parens_no_args_" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args_public(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_local_relative_parens_no_args_public" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_no_args_internal(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_local_relative_parens_no_args_internal" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "default", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg_(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_local_relative_parens_with_arg_" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg_public(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_local_relative_parens_with_arg_public" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_local_relative_parens_with_arg_internal(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_local_relative_parens_with_arg_internal" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment_(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_multi_no_comment_" ).getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment_public(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_multi_no_comment_public" ).getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_no_comment_internal(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_multi_no_comment_internal" ).getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_multi_comment_before_" ).getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_public(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_multi_comment_before_public" ).getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_internal(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_multi_comment_before_internal" ).getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_multi_comment_between_" ).getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_public(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_multi_comment_between_public" ).getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_internal(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_multi_comment_between_internal" ).getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_multi_comment_after_" ).getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_public(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_multi_comment_after_public" ).getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_internal(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_multi_comment_after_internal" ).getTypeInfo().getAnnotations());
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation_(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_multi_comment_before_w_annotation_" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation_public(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_multi_comment_before_w_annotation_public" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_before_w_annotation_internal(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_multi_comment_before_w_annotation_internal" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation_(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_multi_comment_between_w_annotation_" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation_public(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_multi_comment_between_w_annotation_public" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_between_w_annotation_internal(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_multi_comment_between_w_annotation_internal" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation_(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_multi_comment_after_w_annotation_" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation_public(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_multi_comment_after_w_annotation_public" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

  public void test_gosu_multi_comment_after_w_annotation_internal(){
    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( "gw.spec.core.annotations.instances._GeneratedTopLevelHolderClass_gosu_multi_comment_after_w_annotation_internal" ).getTypeInfo().getAnnotations());
    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );
    assertEquals( "foo", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getInstance()).value() );
    assertEquals( 2, map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).size() );
    assertEquals( "foo", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 0 ).getInstance(), "Value" ) );
    assertEquals( "bar", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( "gw.spec.core.annotations.instances.LocalGosuAnnotation" ) ).get( 1 ).getInstance(), "Value" ) );
  }

}