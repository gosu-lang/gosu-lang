package gw.util;

import sun.misc.Unsafe;

import java.lang.reflect.Constructor;

/**
 */
public class GosuUnsafeUtil
{
  private static Unsafe _unsafe;

  private GosuUnsafeUtil() {}

  public static void monitorEnter( Object monitor )
  {
    getUnsafe().monitorEnter( monitor );
  }

  public static void tryMonitorEnter( Object monitor )
  {
    getUnsafe().tryMonitorEnter( monitor );
  }

  public static void monitorExit( Object monitor )
  {
    getUnsafe().monitorExit( monitor );
  }

  private static Unsafe getUnsafe()
  {
    if( _unsafe == null )
    {
      try
      {
        // Prefer getting Unsafe instance by constructing over accessing a field.
        // The field approach is less likely to be platform independent e.g.,
        // the field may not be present or may have a different name.
        Constructor<Unsafe> ctor = Unsafe.class.getDeclaredConstructor();
        ctor.setAccessible( true );
        _unsafe = ctor.newInstance();

//        Field field = Unsafe.class.getDeclaredField( "theUnsafe" );
//        field.setAccessible( true );
//        _unsafe = (Unsafe)field.get( null );
      }
      catch( Exception e )
      {
        throw new RuntimeException( e );
      }
    }
    return _unsafe;
  }
}
