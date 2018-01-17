/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

import gw.util.Array;

/**
 */
public class StaticFieldInitializationTest extends ByteCodeTestBase
{
  public void testStaticInit() throws Exception
  {
    final String cls = "gw.internal.gosu.compiler.sample.statement.classes.ClassInitializesStaticFields";
    Class<?> javaClass = GosuClassLoader.instance().findClass( cls );
    
    assertFalse( (Boolean)javaClass.getField( "_uninitboolean" ).get( null ) );
    assertZero( (Byte)javaClass.getField( "_uninitbyte" ).get( null ) );
    assertZero( (Short)javaClass.getField( "_uninitshort" ).get( null ) );
    assertZero( (Integer)javaClass.getField( "_uninitint" ).get( null ) );
    assertZero( (Long)javaClass.getField( "_uninitlong" ).get( null ) );
    assertEquals( 0.0F, (Float)javaClass.getField( "_uninitfloat" ).get( null ) );
    assertEquals( 0.0D, (Double)javaClass.getField( "_uninitdouble" ).get( null ) );
    assertZero( (Character)javaClass.getField( "_uninitchar" ).get( null ) );
    assertNull( javaClass.getField( "_uninitstring" ).get( null ) );

    assertNull( javaClass.getField( "_uninitarrboolean" ).get( null ) );
    assertNull( javaClass.getField( "_uninitarrbyte" ).get( null ) );
    assertNull( javaClass.getField( "_uninitarrshort" ).get( null ) );
    assertNull( javaClass.getField( "_uninitarrint" ).get( null ) );
    assertNull( javaClass.getField( "_uninitarrlong" ).get( null ) );
    assertNull( javaClass.getField( "_uninitarrfloat" ).get( null ) );
    assertNull( javaClass.getField( "_uninitarrdouble" ).get( null ) );
    assertNull( javaClass.getField( "_uninitarrchar" ).get( null ) );
    assertNull( javaClass.getField( "_uninitarrstring" ).get( null ) );

    assertTrue( (Boolean)javaClass.getField( "_boolean" ).get( null ) );
    assertEquals( 1, (byte)(Byte)javaClass.getField( "_byte" ).get( null ) );
    assertEquals( 2, (short)(Short)javaClass.getField( "_short" ).get( null ) );
    assertEquals( 3, (int) (Integer)javaClass.getField( "_int" ).get( null ) );
    assertEquals( 4L, (long)(Long)javaClass.getField( "_long" ).get( null ) );
    assertEquals( 5F, (Float)javaClass.getField( "_float" ).get( null ) );
    assertEquals( 6D, (Double)javaClass.getField( "_double" ).get( null ) );
    assertEquals( '7', (char)(Character)javaClass.getField( "_char" ).get( null ) );
    assertEquals( "8", javaClass.getField( "_string" ).get( null ) );

    arraysEqual( new boolean[] {true}, javaClass.getField( "_arrboolean" ).get( null ) );
    arraysEqual( new byte[] {1, 2}, javaClass.getField( "_arrbyte" ).get( null ) );
    arraysEqual( new short[] {3, 4}, javaClass.getField( "_arrshort" ).get( null ) );
    arraysEqual( new int[] {5, 6}, javaClass.getField( "_arrint" ).get( null ) );
    arraysEqual( new long[] {7, 8}, javaClass.getField( "_arrlong" ).get( null ) );
    arraysEqual( new float[] {9.1F, 0.1F}, javaClass.getField( "_arrfloat" ).get( null ) );
    arraysEqual( new double[] {1.1, 1.2}, javaClass.getField( "_arrdouble" ).get( null ) );
    arraysEqual( new char[] {'a', 'b'}, javaClass.getField( "_arrchar" ).get( null ) );
    arraysEqual( new String[] {"c", "d"}, javaClass.getField( "_arrstring" ).get( null ) );
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