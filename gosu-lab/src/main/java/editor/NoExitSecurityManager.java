package editor;

/**
 */
public class NoExitSecurityManager extends SecurityManager
{
  public static boolean CLOSING = false;

  public NoExitSecurityManager()
  {
  }

  public void checkPermission( java.security.Permission perm, Object context )
  {
    // allow everything
  }

  public void checkPermission( java.security.Permission perm )
  {
    // allow everything
  }

  public void checkExit( int status )
  {
    if( !CLOSING )
    {
      throw new SecurityException( "kek" );
    }
  }
}
