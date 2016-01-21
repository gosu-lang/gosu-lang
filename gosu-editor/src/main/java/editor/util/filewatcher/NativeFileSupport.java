package editor.util.filewatcher;

import editor.util.PlatformUtil;
import gw.config.CommonServices;

import java.io.File;
import java.net.URL;

public class NativeFileSupport
{

  private static final String WATCHER_LIB = "gw-watcher-win32.dll";
  private static final String WATCHER_LIB_64 = "gw-watcher-win64.dll";

  private static boolean _enabled = false;

  static
  {
    try
    {
      // native scanning only enabled on Windows.
      if( PlatformUtil.isWindows() )
      {
        if( !Boolean.getBoolean( "disable.native.scanning" ) )
        {
          // hack borrowed from ClassRedefiner.loadJvmtiAccessLibrary() -- locates watcher.dll in gosu-lib/lib
          URL loc = Class.forName( "gw.NativeLibraryLocationMarker" ).getProtectionDomain().getCodeSource().getLocation();
          System.load( new File( loc.toURI() ).getParent() + File.separator + getLibraryName() );
          _enabled = true;
        }
        else
        {
          CommonServices.getEntityAccess().getLogger().info( "Native file support disabled by user." );
        }
      }
    }
    catch( Throwable t )
    {
      _enabled = false;
      CommonServices.getEntityAccess().getLogger().info( "Failed to load native file support", t );
    }
  }

  private static String getLibraryName()
  {
    return is64BitJvm() ? WATCHER_LIB_64 : WATCHER_LIB;
  }

  private static boolean is64BitJvm()
  {
    String arch = System.getProperty( "sun.arch.data.model" );
    if( "64".equals( arch ) )
    {
      return true;
    }
    else if( "32".equals( arch ) )
    {
      return false;
    }
    else
    {
      return System.getProperty( "java.vm.name" ).contains( "64" );
    }
  }

  public static final NativeFileSupport _instance = new NativeFileSupport();

  private NativeFileSupport()
  {
  }

  public static boolean isEnabled()
  {
    return _enabled;
  }

  public static void disable()
  {
    _enabled = false;
  }

  public static native boolean nativeFindFiles( final String path, final IFileHandler handler );

}
