/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.classTests;

import gw.internal.gosu.parser.IGosuClassInternal;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClassTypeInfo;
import gw.lang.reflect.ReflectUtil;
import gw.lang.parser.resources.Res;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.test.TestClass;

/**
 * @author cgross
 */
public class GosuClassVisibilityTest extends TestClass
{
  public void testCanCallInstanceMethodsCorrectly()
  {
    Object instance = getVisibilityTestClassInstance();
    assertExistsAndInvoke( instance, "privateFun" );
    assertExistsAndInvoke( instance, "protectedFun" );
    assertExistsAndInvoke( instance, "publicFun" );
  }

  public void testCanCallStaticMethodsCorrectly()
  {
    IGosuClassInternal gsClass = (IGosuClassInternal) ReflectUtil.getClass( "gw.internal.gosu.parser.classTests.gwtest.visibility.Visibility" );
    assertExistsAndInvokeStatic( gsClass, "privateStaticFun" );
    assertExistsAndInvokeStatic( gsClass, "protectedStaticFun" );
    assertExistsAndInvokeStatic( gsClass, "publicStaticFun" );
  }

  public void testCanCallInstancePropertiesCorrectly()
  {
    Object instance = getVisibilityTestClassInstance();
    assertEquals( "", assertExistsAndGet( instance, "PrivateProperty" ) );
    assertEquals( "", assertExistsAndGet( instance, "ProtectedProperty" ) );
    assertEquals( "", assertExistsAndGet( instance, "PublicProperty" ) );
  }

  public void testCanCallStaticPropertiesCorrectly()
  {
    IGosuClassInternal gsClass = (IGosuClassInternal) ReflectUtil.getClass( "gw.internal.gosu.parser.classTests.gwtest.visibility.Visibility" );
    assertEquals( "", assertExistsAndGetStatic( gsClass, "PrivateStaticProperty" ) );
    assertEquals( "", assertExistsAndGetStatic( gsClass, "ProtectedStaticProperty" ) );
    assertEquals( "", assertExistsAndGetStatic( gsClass, "PublicStaticProperty" ) );
  }

  public void testCanNotAccessPrivateFunctionFromPackageLocalClass()
  {
    IGosuClassInternal gsClass = (IGosuClassInternal) ReflectUtil.getClass( "gw.internal.gosu.parser.classTests.gwtest.visibility.OtherVisibility" );
    assertFalse( gsClass.isValid() );
    assertTrue( gsClass.getParseResultsException().getParseExceptions().size() > 0 );
    assertEquals( Res.MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD, gsClass.getParseResultsException().getParseExceptions().get( 0 ).getMessageKey() );
  }

  public void testInnerClassVisibility()
  {
    assertTrue( ((IGosuClassTypeInfo)TypeSystem.getByFullName( "gw.internal.gosu.parser.classTests.gwtest.visibility.Visibility" ).getTypeInfo()).isPublic() );
    assertTrue( ((IGosuClassTypeInfo)TypeSystem.getByFullName( "gw.internal.gosu.parser.classTests.gwtest.visibility.Visibility.PrivateClass" ).getTypeInfo()).isPrivate() );
    assertTrue( ((IGosuClassTypeInfo)TypeSystem.getByFullName( "gw.internal.gosu.parser.classTests.gwtest.visibility.Visibility.InternalClass" ).getTypeInfo()).isInternal() );
    assertTrue( ((IGosuClassTypeInfo)TypeSystem.getByFullName( "gw.internal.gosu.parser.classTests.gwtest.visibility.Visibility.ProtectedClass" ).getTypeInfo()).isProtected() );
    assertTrue( ((IGosuClassTypeInfo)TypeSystem.getByFullName( "gw.internal.gosu.parser.classTests.gwtest.visibility.Visibility.PublicClass" ).getTypeInfo()).isPublic() );
  }

  private void assertExistsAndInvokeStatic( IGosuClassInternal gsClass, String methodName )
  {
    gsClass.compileDeclarationsIfNeeded();
    IMethodInfo methodInfo = gsClass.getTypeInfo().getMethod( gsClass, methodName );
    methodInfo.getCallHandler().handleCall( null );
  }

  private void assertExistsAndInvoke( Object instance, String name )
  {
    ReflectUtil.invokeMethod( instance, name );
  }

  private Object assertExistsAndGet( Object instance, String name )
  {
    return ReflectUtil.getProperty( instance, name );
  }

  private Object assertExistsAndGetStatic( IGosuClassInternal gsClass, String name )
  {
    IPropertyInfo methodInfo = gsClass.getTypeInfo().getProperty( gsClass, name );
    assertNotNull( methodInfo );
    return methodInfo.getAccessor().getValue( null );
  }

  private Object getVisibilityTestClassInstance()
  {
    Object instance = ReflectUtil.constructGosuClassInstance( "gw.internal.gosu.parser.classTests.gwtest.visibility.Visibility" );
    return instance;
  }
}
