package editor.util;

import java.awt.event.KeyEvent;

/**
 */
public class PlatformUtil
{
  /**
   * Platform dependent keystroke info
   */
  public static final String CONTROL_KEY_NAME;
  public static final int CONTROL_KEY_MASK;

  static
  {
    if( isMac() )
    {
      CONTROL_KEY_MASK = KeyEvent.META_DOWN_MASK;
      CONTROL_KEY_NAME = "meta";
    }
    else
    {
      CONTROL_KEY_MASK = KeyEvent.CTRL_DOWN_MASK;
      CONTROL_KEY_NAME = "control";
    }
  }

  //
  // Platforms
  //
  static final int PLAT_UNINIT = -2;
  static final int PLAT_UNDEFINED = -1;
  static final int PLAT_WINDOWS = 0;
  static final int PLAT_MAC = 1;
  static final int PLAT_SOLARIS = 2;
  static final int PLAT_LINUX = 3;
  static final int PLAT_OS2 = 4;
  static final int PLAT_HPUX = 5;
  static final int PLAT_AIX = 6;
  static final int PLAT_NETWARE = 7;

  static protected int g_iPlatform = PLAT_UNINIT;

  public static int getPlatform()
  {
    try
    {
      if( g_iPlatform == PLAT_UNINIT )
      {
        String strOSName = System.getProperty( "os.name" );
        if( strOSName != null )
        {
          strOSName = strOSName.toLowerCase();

          if( strOSName.startsWith( "windows" ) )
          {
            g_iPlatform = PLAT_WINDOWS;
          }
          else if( strOSName.startsWith( "mac" ) )
          {
            g_iPlatform = PLAT_MAC;
          }
          else if( strOSName.startsWith( "solaris" ) )
          {
            g_iPlatform = PLAT_SOLARIS;
          }
          else if( strOSName.startsWith( "linux" ) )
          {
            g_iPlatform = PLAT_LINUX;
          }
          else if( strOSName.startsWith( "os/2" ) )
          {
            g_iPlatform = PLAT_OS2;
          }
          else if( strOSName.startsWith( "aix" ) )
          {
            g_iPlatform = PLAT_AIX;
          }
          else if( strOSName.startsWith( "hp" ) )
          {
            g_iPlatform = PLAT_HPUX;
          }
          else if( strOSName.startsWith( "netware" ) )
          {
            g_iPlatform = PLAT_NETWARE;
          }
          else
          {
            g_iPlatform = PLAT_UNDEFINED;
          }
        }
      }
    }
    catch( Throwable t )
    {
      t.printStackTrace();
    }

    return g_iPlatform;
  }

  public static boolean isWindows()
  {
    return getPlatform() == PLAT_WINDOWS;
  }

  public static boolean isMac()
  {
    return getPlatform() == PLAT_MAC;
  }

  public static boolean isSolaris()
  {
    return getPlatform() == PLAT_SOLARIS;
  }

  public static boolean isLinux()
  {
    return getPlatform() == PLAT_LINUX;
  }
}