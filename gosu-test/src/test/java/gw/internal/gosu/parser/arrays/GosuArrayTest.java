/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.arrays;

import gw.internal.gosu.parser.arrays.gwtest.IJavaInterface;
import gw.internal.gosu.parser.arrays.gwtest.JavaClass;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.ReflectUtil;
import gw.lang.reflect.gs.IGosuObject;
import gw.test.TestClass;
import gw.util.GosuTestUtil;

public class GosuArrayTest extends TestClass
{
  public void testCanAssignGosuArrayToJavaArrayWhereGosuArrayComponentIsSubTypeOfJavaArrayComponent() throws ClassNotFoundException
  {
    IGosuObject gsInstance = ReflectUtil.constructGosuClassInstance( "gw.internal.gosu.parser.arrays.gwtest.ExtendsJavaClass" );
    Object[] gsArrayInstance = (Object[])gsInstance.getIntrinsicType().makeArrayInstance( 1 );
    gsArrayInstance[0] = gsInstance;
    assertTrue( JavaClass[].class.isAssignableFrom( gsArrayInstance.getClass() ) );

    IMethodInfo mi = gsInstance.getIntrinsicType().getTypeInfo().getMethod( "makeJavaArray" );
    JavaClass[] javaArray = (JavaClass[])mi.getCallHandler().handleCall( gsInstance );
    assertNotNull( javaArray );
    assertEquals( 1, javaArray.length );
    assertNotNull( javaArray[0] );

    mi = gsInstance.getIntrinsicType().getTypeInfo().getMethod( "callJavaMethodWithArray" );
    javaArray = (JavaClass[])mi.getCallHandler().handleCall( gsInstance );
    assertNotNull( javaArray );
    assertEquals( 1, javaArray.length );
    assertNull( javaArray[0] );
  }

  public void testCanAssignGosuArrayToJavaArrayWhereGosuArrayComponentImplementsJavaArrayComponent() throws ClassNotFoundException
  {
    IGosuObject gsInstance = ReflectUtil.constructGosuClassInstance( "gw.internal.gosu.parser.arrays.gwtest.ImplsJavaInterface" );
    Object[] gsArrayInstance = (Object[])gsInstance.getIntrinsicType().makeArrayInstance( 1 );
    gsArrayInstance[0] = gsInstance;
    assertTrue( IJavaInterface[].class.isAssignableFrom( gsArrayInstance.getClass() ) );
  }

  public void testObjectMethodsCanBeInvokedOnArrays()
  {
    assertNotNull( GosuTestUtil.eval( "new int[0].toString()" ) );
    assertNotNull( GosuTestUtil.eval( "new String[0].toString()" ) );
  }
}
