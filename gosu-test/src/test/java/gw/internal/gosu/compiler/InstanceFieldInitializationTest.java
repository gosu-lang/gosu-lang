/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

import gw.util.Array;

/**
 */
public class InstanceFieldInitializationTest extends ByteCodeTestBase
{
  public void testNoConstructorInitializesInstanceFields() throws Exception
  {
    Object obj = construct( "TestNoConstructorInitializesInstanceFields" );
    Class javaClass = obj.getClass();

    ensureFieldsAreInitialized( obj, javaClass );
  }

  public void testSimpleConstructorInitializesInstanceFields() throws Exception
  {
    final String cls = "gw.internal.gosu.compiler.sample.statement.classes.ClassWithConstructorsInitializedInstanceFields";
    Class<?> javaClass = GosuClassLoader.instance().findClass( cls );
    assertNotNull( javaClass );
    assertEquals( cls, javaClass.getName() );
    assertNotNull( javaClass.newInstance() );
    Object obj = javaClass.getConstructor( String.class ).newInstance( "test" );

    ensureFieldsAreInitialized( obj, javaClass );
  }

  public void testDelgatingConstructorInitializesInstanceFields() throws Exception
  {
    final String cls = "gw.internal.gosu.compiler.sample.statement.classes.ClassWithConstructorsInitializedInstanceFields";
    Class<?> javaClass = GosuClassLoader.instance().findClass( cls );
    assertNotNull( javaClass );
    assertEquals( cls, javaClass.getName() );
    assertNotNull( javaClass.newInstance() );
    Object obj = javaClass.newInstance();    

    ensureFieldsAreInitialized( obj, javaClass );
  }

  public void testFieldsInitializedOnceFromDelegateCtor() throws Exception
  {
    final String cls = "gw.internal.gosu.compiler.sample.statement.classes.TestFieldsInitializedOnceFromDelegateCtor";
    Class<?> javaClass = GosuClassLoader.instance().findClass( cls );
    Object obj = javaClass.newInstance();

    assertEquals( (Integer) 1, (Integer)javaClass.getField( "_int" ).get( obj ) );
  }

  private void ensureFieldsAreInitialized( Object obj, Class javaClass )
    throws IllegalAccessException, NoSuchFieldException
  {
    assertFalse( (Boolean)javaClass.getField( "_uninitboolean" ).get( obj ) );
    assertZero( (Byte)javaClass.getField( "_uninitbyte" ).get( obj ) );
    assertZero( (Short)javaClass.getField( "_uninitshort" ).get( obj ) );
    assertZero( (Integer)javaClass.getField( "_uninitint" ).get( obj ) );
    assertZero( (Long)javaClass.getField( "_uninitlong" ).get( obj ) );
    assertEquals( 0.0F, (Float)javaClass.getField( "_uninitfloat" ).get( obj ) );
    assertEquals( 0.0D, (Double)javaClass.getField( "_uninitdouble" ).get( obj ) );
    assertZero( (Character)javaClass.getField( "_uninitchar" ).get( obj ) );
    assertNull( javaClass.getField( "_uninitstring" ).get( obj ) );

    assertNull( javaClass.getField( "_uninitarrboolean" ).get( obj ) );
    assertNull( javaClass.getField( "_uninitarrbyte" ).get( obj ) );
    assertNull( javaClass.getField( "_uninitarrshort" ).get( obj ) );
    assertNull( javaClass.getField( "_uninitarrint" ).get( obj ) );
    assertNull( javaClass.getField( "_uninitarrlong" ).get( obj ) );
    assertNull( javaClass.getField( "_uninitarrfloat" ).get( obj ) );
    assertNull( javaClass.getField( "_uninitarrdouble" ).get( obj ) );
    assertNull( javaClass.getField( "_uninitarrchar" ).get( obj ) );
    assertNull( javaClass.getField( "_uninitarrstring" ).get( obj ) );

    assertTrue( (Boolean)javaClass.getField( "_boolean" ).get( obj ) );
    assertEquals( 1, (byte)(Byte)javaClass.getField( "_byte" ).get( obj ) );
    assertEquals( 2, (short)(Short)javaClass.getField( "_short" ).get( obj ) );
    assertEquals( (Integer) 3, (Integer)javaClass.getField( "_int" ).get( obj ) );
    assertEquals( 4L, (long)(Long)javaClass.getField( "_long" ).get( obj ) );
    assertEquals( 5F, (Float)javaClass.getField( "_float" ).get( obj ) );
    assertEquals( 6D, (Double)javaClass.getField( "_double" ).get( obj ) );
    assertEquals( '7', (char)(Character)javaClass.getField( "_char" ).get( obj ) );
    assertEquals( "8", javaClass.getField( "_string" ).get( obj ) );

    arraysEqual( new boolean[] {true}, javaClass.getField( "_arrboolean" ).get( obj ) );
    arraysEqual( new byte[] {1, 2}, javaClass.getField( "_arrbyte" ).get( obj ) );
    arraysEqual( new short[] {3, 4}, javaClass.getField( "_arrshort" ).get( obj ) );
    arraysEqual( new int[] {5, 6}, javaClass.getField( "_arrint" ).get( obj ) );
    arraysEqual( new long[] {7, 8}, javaClass.getField( "_arrlong" ).get( obj ) );
    arraysEqual( new float[] {9.1F, 0.1F}, javaClass.getField( "_arrfloat" ).get( obj ) );
    arraysEqual( new double[] {1.1, 1.2}, javaClass.getField( "_arrdouble" ).get( obj ) );
    arraysEqual( new char[] {'a', 'b'}, javaClass.getField( "_arrchar" ).get( obj ) );
    arraysEqual( new String[] {"c", "d"}, javaClass.getField( "_arrstring" ).get( obj ) );
  }

  private void arraysEqual( Object o1, Object o2 )
  {
    assertTrue( o1.getClass().isArray() );
    assertTrue( o2.getClass().isArray() );
    assertEquals( o1.getClass(), o2.getClass() );
    assertEquals( Array.getLength( o1 ), Array.getLength( o2 ) );
    for( int i = 0; i > Array.getLength( o1 ); i++ )
    {
      assertEquals( Array.get( o1, i ), Array.get( o2, i ) );
    }
  }

  private Object construct( String strRelativeName ) throws ClassNotFoundException, InstantiationException, IllegalAccessException
  {
    final String cls = "gw.internal.gosu.compiler.sample.statement.classes." + strRelativeName;
    Class<?> javaClass = GosuClassLoader.instance().findClass( cls );
    assertNotNull( javaClass );
    assertEquals( cls, javaClass.getName() );
    assertNotNull( javaClass.newInstance() );
    return javaClass.newInstance();
  }
}