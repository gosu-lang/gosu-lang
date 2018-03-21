package gw.util;

/**
 * Replaces java.lang.reflect.Array for better performance.
 * <p/>
 * Basically the java.lang.reflect version uses native methods which are quite slow.
 */
public final class Array
{
  private Array()
  {
  }

  public static Object newInstance( Class<?> componentType, int length )
  {
    if( !componentType.isPrimitive() )
    {
      return java.lang.reflect.Array.newInstance( componentType, length );
    }
    if( componentType == boolean.class )
    {
      return new boolean[length];
    }
    if( componentType == byte.class )
    {
      return new byte[length];
    }
    if( componentType == char.class )
    {
      return new char[length];
    }
    if( componentType == short.class )
    {
      return new short[length];
    }
    if( componentType == int.class )
    {
      return new int[length];
    }
    if( componentType == long.class )
    {
      return new long[length];
    }
    if( componentType == float.class )
    {
      return new float[length];
    }
    if( componentType == double.class )
    {
      return new double[length];
    }
    // assert componentType == void.class
    throw new IllegalArgumentException();
  }

  public static Object newInstance( Class<?> componentType, int[] dimensions )
  {
    if( dimensions.length <= 0 )
    {
      throw new IllegalArgumentException( "Empty dimensions array." );
    }
    return java.lang.reflect.Array.newInstance( componentType, dimensions );
  }

  public static int getLength( Object array )
  {
    if( array instanceof Object[] )
    {
      return ((Object[])array).length;
    }
    if( array instanceof boolean[] )
    {
      return ((boolean[])array).length;
    }
    if( array instanceof byte[] )
    {
      return ((byte[])array).length;
    }
    if( array instanceof char[] )
    {
      return ((char[])array).length;
    }
    if( array instanceof short[] )
    {
      return ((short[])array).length;
    }
    if( array instanceof int[] )
    {
      return ((int[])array).length;
    }
    if( array instanceof long[] )
    {
      return ((long[])array).length;
    }
    if( array instanceof float[] )
    {
      return ((float[])array).length;
    }
    if( array instanceof double[] )
    {
      return ((double[])array).length;
    }
    if( array == null )
    {
      throw new NullPointerException();
    }
    throw new IllegalArgumentException();
  }

  public static Object get( Object array, int index )
  {
    if( array instanceof Object[] )
    {
      return ((Object[])array)[index];
    }
    if( array instanceof boolean[] )
    {
      return ((boolean[])array)[index] ? Boolean.TRUE : Boolean.FALSE;
    }
    if( array instanceof byte[] )
    {
      return new Byte( ((byte[])array)[index] );
    }
    if( array instanceof char[] )
    {
      return new Character( ((char[])array)[index] );
    }
    if( array instanceof short[] )
    {
      return new Short( ((short[])array)[index] );
    }
    if( array instanceof int[] )
    {
      return new Integer( ((int[])array)[index] );
    }
    if( array instanceof long[] )
    {
      return new Long( ((long[])array)[index] );
    }
    if( array instanceof float[] )
    {
      return new Float( ((float[])array)[index] );
    }
    if( array instanceof double[] )
    {
      return new Double( ((double[])array)[index] );
    }
    if( array == null )
    {
      throw new NullPointerException();
    }
    throw new IllegalArgumentException();
  }

  public static boolean getBoolean( Object array, int index )
  {
    if( array instanceof boolean[] )
    {
      return ((boolean[])array)[index];
    }
    if( array == null )
    {
      throw new NullPointerException();
    }
    throw new IllegalArgumentException();
  }

  public static byte getByte( Object array, int index )
  {
    if( array instanceof byte[] )
    {
      return ((byte[])array)[index];
    }
    if( array == null )
    {
      throw new NullPointerException();
    }
    throw new IllegalArgumentException();
  }

  public static char getChar( Object array, int index )
  {
    if( array instanceof char[] )
    {
      return ((char[])array)[index];
    }
    if( array == null )
    {
      throw new NullPointerException();
    }
    throw new IllegalArgumentException();
  }

  public static short getShort( Object array, int index )
  {
    if( array instanceof short[] )
    {
      return ((short[])array)[index];
    }
    return getByte( array, index );
  }

  public static int getInt( Object array, int index )
  {
    if( array instanceof int[] )
    {
      return ((int[])array)[index];
    }
    if( array instanceof char[] )
    {
      return ((char[])array)[index];
    }
    return getShort( array, index );
  }


  public static long getLong( Object array, int index )
  {
    if( array instanceof long[] )
    {
      return ((long[])array)[index];
    }
    return getInt( array, index );
  }

  public static float getFloat( Object array, int index )
  {
    if( array instanceof float[] )
    {
      return ((float[])array)[index];
    }
    return getLong( array, index );
  }

  public static double getDouble( Object array, int index )
  {
    if( array instanceof double[] )
    {
      return ((double[])array)[index];
    }
    return getFloat( array, index );
  }

  public static void set( Object array, int index, Object value )
  {
    if( array instanceof Object[] )
    {
      // Too bad the API won't let us throw the easier ArrayStoreException!
      if( value != null
          && !array.getClass().getComponentType().isInstance( value ) )
      {
        throw new IllegalArgumentException();
      }
      ((Object[])array)[index] = value;
    }
    else if( value instanceof Byte )
    {
      setByte( array, index, ((Byte)value).byteValue() );
    }
    else if( value instanceof Short )
    {
      setShort( array, index, ((Short)value).shortValue() );
    }
    else if( value instanceof Integer )
    {
      setInt( array, index, ((Integer)value).intValue() );
    }
    else if( value instanceof Long )
    {
      setLong( array, index, ((Long)value).longValue() );
    }
    else if( value instanceof Float )
    {
      setFloat( array, index, ((Float)value).floatValue() );
    }
    else if( value instanceof Double )
    {
      setDouble( array, index, ((Double)value).doubleValue() );
    }
    else if( value instanceof Character )
    {
      setChar( array, index, ((Character)value).charValue() );
    }
    else if( value instanceof Boolean )
    {
      setBoolean( array, index, ((Boolean)value).booleanValue() );
    }
    else if( array == null )
    {
      throw new NullPointerException();
    }
    else
    {
      throw new IllegalArgumentException();
    }
  }

  public static void setBoolean( Object array, int index, boolean value )
  {
    if( array instanceof boolean[] )
    {
      ((boolean[])array)[index] = value;
    }
    else if( array == null )
    {
      throw new NullPointerException();
    }
    else
    {
      throw new IllegalArgumentException();
    }
  }

  public static void setByte( Object array, int index, byte value )
  {
    if( array instanceof byte[] )
    {
      ((byte[])array)[index] = value;
    }
    else
    {
      setShort( array, index, value );
    }
  }

  public static void setChar( Object array, int index, char value )
  {
    if( array instanceof char[] )
    {
      ((char[])array)[index] = value;
    }
    else
    {
      setInt( array, index, value );
    }
  }

  public static void setShort( Object array, int index, short value )
  {
    if( array instanceof short[] )
    {
      ((short[])array)[index] = value;
    }
    else
    {
      setInt( array, index, value );
    }
  }

  public static void setInt( Object array, int index, int value )
  {
    if( array instanceof int[] )
    {
      ((int[])array)[index] = value;
    }
    else
    {
      setLong( array, index, value );
    }
  }

  public static void setLong( Object array, int index, long value )
  {
    if( array instanceof long[] )
    {
      ((long[])array)[index] = value;
    }
    else
    {
      setFloat( array, index, value );
    }
  }

  public static void setFloat( Object array, int index, float value )
  {
    if( array instanceof float[] )
    {
      ((float[])array)[index] = value;
    }
    else
    {
      setDouble( array, index, value );
    }
  }

  public static void setDouble( Object array, int index, double value )
  {
    if( array instanceof double[] )
    {
      ((double[])array)[index] = value;
    }
    else if( array == null )
    {
      throw new NullPointerException();
    }
    else
    {
      throw new IllegalArgumentException();
    }
  }
}