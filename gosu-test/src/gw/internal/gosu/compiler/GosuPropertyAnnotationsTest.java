/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.test.TestClass;

public class GosuPropertyAnnotationsTest extends TestClass
{
  public void testAnnotationOnBasicGetterShowsUp()
  {
    IPropertyInfo pi = getProp( "StringProp1" );
    assertEquals( 1, pi.getAnnotationsOfType( TypeSystem.get(Deprecated.class) ).size() );
  }

  public void testAnnotationOnBasicSetterShowsUp()
  {
    IPropertyInfo pi = getProp( "StringProp2" );
    assertEquals( 1, pi.getAnnotationsOfType( TypeSystem.get(Deprecated.class) ).size() );
  }

  public void testAnnotationOnVarPropertyShowsUp()
  {
    IPropertyInfo pi = getProp( "StringProp3" );
    assertEquals( 1, pi.getAnnotationsOfType( TypeSystem.get(Deprecated.class) ).size() );
  }

  public void testGetFirstWithAnnotationShowsUp()
  {
    IPropertyInfo pi = getProp( "GetFirstWithAnnotationProp" );
    assertEquals( 1, pi.getAnnotationsOfType( TypeSystem.get(Deprecated.class) ).size() );
  }

  public void testGetFirstWithoutAnnotationShowsUp()
  {
    IPropertyInfo pi = getProp( "GetFirstWithoutAnnotationProp" );
    assertEquals( 1, pi.getAnnotationsOfType( TypeSystem.get(Deprecated.class) ).size() );
  }

  public void testSetFirstWithAnnotationShowsUp()
  {
    IPropertyInfo pi = getProp( "SetFirstWithAnnotationProp" );
    assertEquals( 1, pi.getAnnotationsOfType( TypeSystem.get(Deprecated.class) ).size() );
  }

  public void testSetFirstWithoutAnnotationShowsUp()
  {
    IPropertyInfo pi = getProp( "SetFirstWithoutAnnotationProp" );
    assertEquals( 1, pi.getAnnotationsOfType( TypeSystem.get(Deprecated.class) ).size() );
  }

  public void testFieldFirstWithAnnotationShowsUp()
  {
    IPropertyInfo pi = getProp( "FieldFirstWithAnnotationAndGetter" );
    assertEquals( 1, pi.getAnnotationsOfType( TypeSystem.get(Deprecated.class) ).size() );
  }

  public void testFieldFirstWithoutAnnotationShowsUp()
  {
    IPropertyInfo pi = getProp( "FieldFirstWithoutAnnotationAndGetter" );
    assertEquals( 1, pi.getAnnotationsOfType( TypeSystem.get(Deprecated.class) ).size() );
  }

  public void testFieldFirstWithAnnotationAndSetterShowsUp()
  {
    IPropertyInfo pi = getProp( "FieldFirstWithAnnotationAndSetter" );
    assertEquals( 1, pi.getAnnotationsOfType( TypeSystem.get(Deprecated.class) ).size() );
  }

  public void testFieldFirstWithoutAnnotationAndSetterShowsUp()
  {
    IPropertyInfo pi = getProp( "FieldFirstWithoutAnnotationAndSetter" );
    assertEquals( 1, pi.getAnnotationsOfType( TypeSystem.get(Deprecated.class) ).size() );
  }

  public void testAnnotationOnFieldGetterAndSetterOnlyShowUpTwice()
  {
    IPropertyInfo pi = getProp( "FieldGetterAndSetterAllAnnotated" );
    assertEquals( 2, pi.getAnnotationsOfType( TypeSystem.get(Deprecated.class) ).size() );
  }

  private IPropertyInfo getProp( String name )
  {
    IGosuClass type = (IGosuClass)TypeSystem.getByFullName( "gw.internal.gosu.compiler.sample.annotations.PropertyAnnotationsHelper" );
    boolean valid = type.isValid();
    if( !valid )
    {
      type.getParseResultsException().printStackTrace();
      fail("PropertyAnnotationsHelper is not valid");
    }
    return type.getTypeInfo().getProperty( type, name );
  }
}
