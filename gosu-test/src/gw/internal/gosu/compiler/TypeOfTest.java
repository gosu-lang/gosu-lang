/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

import gw.internal.gosu.parser.MetaType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.java.JavaTypes;

/**
 */
public class TypeOfTest extends ByteCodeTestBase
{
  public void testTypeOfNull() throws Exception
  {
    Object obj = newTypeOfTestClass();

    Object val = invokeMethod( obj,  "typeOfNull" );
    Object ret = val;
    assertSame( JavaTypes.pVOID(), ret );
  }

  public void testTypeOfNullValue() throws Exception
  {
    Object obj = newTypeOfTestClass();

    Object val = invokeMethod( obj,  "typeOfNullValue" );
    Object ret = val;
    assertSame( JavaTypes.pVOID(), ret );
  }

  public void testTypeOfPrimitiveByte() throws Exception
  {
    Object obj = newTypeOfTestClass();

    Object val = invokeMethod( obj,  "typeOfPrimitiveByte" );
    Object ret = val;
    assertSame( JavaTypes.pBYTE(), ret );
  }
        
  public void testTypeOfPrimitiveShort() throws Exception
  {
    Object obj = newTypeOfTestClass();

    Object val = invokeMethod( obj,  "typeOfPrimitiveShort" );
    Object ret = val;
    assertSame( JavaTypes.pSHORT(), ret );
  }
        
  public void testTypeOfPrimitiveInt() throws Exception
  {
    Object obj = newTypeOfTestClass();

    Object val = invokeMethod( obj,  "typeOfPrimitiveInt" );
    Object ret = val;
    assertSame( JavaTypes.pINT(), ret );
  }
        
  public void testTypeOfPrimitiveLong() throws Exception
  {
    Object obj = newTypeOfTestClass();

    Object val = invokeMethod( obj,  "typeOfPrimitiveLong" );
    Object ret = val;
    assertSame( JavaTypes.pLONG(), ret );
  }

  public void testTypeOfPrimitiveFloat() throws Exception
  {
    Object obj = newTypeOfTestClass();

    Object val = invokeMethod( obj,  "typeOfPrimitiveFloat" );
    Object ret = val;
    assertSame( JavaTypes.pFLOAT(), ret );
  }
          
  public void testTypeOfPrimitiveDouble() throws Exception
  {
    Object obj = newTypeOfTestClass();

    Object val = invokeMethod( obj,  "typeOfPrimitiveDouble" );
    Object ret = val;
    assertSame( JavaTypes.pDOUBLE(), ret );
  }
          
  public void testTypeOfPrimitiveChar() throws Exception
  {
    Object obj = newTypeOfTestClass();

    Object val = invokeMethod( obj,  "typeOfPrimitiveChar" );
    Object ret = val;
    assertSame( JavaTypes.pCHAR(), ret );
  }

  public void testTypeOfPrimitiveBoolean() throws Exception
  {
    Object obj = newTypeOfTestClass();

    Object val = invokeMethod( obj,  "typeOfPrimitiveBoolean" );
    Object ret = val;
    assertSame( JavaTypes.pBOOLEAN(), ret );
  }
  
  //
  
  public void testTypeOfBoxedByte() throws Exception
  {
    Object obj = newTypeOfTestClass();

    Object val = invokeMethod( obj,  "typeOfBoxedByte" );
    Object ret = val;
    assertSame( JavaTypes.BYTE(), ret );
  }
          
  public void testTypeOfBoxedShort() throws Exception
  {
    Object obj = newTypeOfTestClass();

    Object val = invokeMethod( obj,  "typeOfBoxedShort" );
    Object ret = val;
    assertSame( JavaTypes.SHORT(), ret );
  }
          
  public void testTypeOfBoxedInt() throws Exception
  {
    Object obj = newTypeOfTestClass();

    Object val = invokeMethod( obj,  "typeOfBoxedInt" );
    Object ret = val;
    assertSame( JavaTypes.INTEGER(), ret );
  }
          
  public void testTypeOfBoxedLong() throws Exception
  {
    Object obj = newTypeOfTestClass();

    Object val = invokeMethod( obj,  "typeOfBoxedLong" );
    Object ret = val;
    assertSame( JavaTypes.LONG(), ret );
  }
  
  public void testTypeOfBoxedFloat() throws Exception
  {
    Object obj = newTypeOfTestClass();

    Object val = invokeMethod( obj,  "typeOfBoxedFloat" );
    Object ret = val;
    assertSame( JavaTypes.FLOAT(), ret );
  }
            
  public void testTypeOfBoxedDouble() throws Exception
  {
    Object obj = newTypeOfTestClass();

    Object val = invokeMethod( obj,  "typeOfBoxedDouble" );
    Object ret = val;
    assertSame( JavaTypes.DOUBLE(), ret );
  }
            
  public void testTypeOfBoxedChar() throws Exception
  {
    Object obj = newTypeOfTestClass();

    Object val = invokeMethod( obj,  "typeOfBoxedChar" );
    Object ret = val;
    assertSame( JavaTypes.CHARACTER(), ret );
  }
  
  public void testTypeOfBoxedBoolean() throws Exception
  {
    Object obj = newTypeOfTestClass();

    Object val = invokeMethod( obj,  "typeOfBoxedBoolean" );
    Object ret = val;
    assertSame( JavaTypes.BOOLEAN(), ret );
  }

  public void testTypeOfString() throws Exception
  {
    Object obj = newTypeOfTestClass();

    Object val = invokeMethod( obj,  "typeOfString" );
    Object ret = val;
    assertSame( JavaTypes.STRING(), ret );
  }

  public void testTypeOfThis() throws Exception
  {
    Object obj = newTypeOfTestClass();

    Object val = invokeMethod( obj,  "typeOfThis" );
    Object ret = val;
    assertSame( TypeSystem.getByFullName( "gw.internal.gosu.compiler.sample.expression.TestTypeOf" ), ret );
  }

  public void testTypeOfGosuParameterizedType() throws Exception
  {
    Object obj = newTypeOfTestClass();

    Object val = invokeMethod( obj,  "typeOfGosuParameterizedType" );
    Object ret = val;
    assertSame( TypeSystem.getByFullName( "gw.internal.gosu.compiler.sample.expression.TestTypeOf.ParameterizedThing" ).getParameterizedType( JavaTypes.STRING() ), ret );
  }

  public void testTypeOfMeta() throws Exception
  {
    Object obj = newTypeOfTestClass();

    Object val = invokeMethod( obj,  "typeOfMeta" );
    Object ret = val;
    assertSame( MetaType.getLiteral( JavaTypes.STRING() ), ret );
  }
  
  private Object newTypeOfTestClass() throws ClassNotFoundException, InstantiationException, IllegalAccessException
  {
    final String classPropertyIdentifier = "gw.internal.gosu.compiler.sample.expression.TestTypeOf";
    Class<?> javaClass = GosuClassLoader.instance().findClass( classPropertyIdentifier );
    assertNotNull( javaClass );
    assertEquals( classPropertyIdentifier, javaClass.getName() );
    assertNotNull( javaClass.newInstance() );
    return javaClass.newInstance();
  }
}