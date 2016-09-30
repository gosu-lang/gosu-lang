package editor.run;

/**
 */
public class FqnRunConfigParameters<T extends FqnRunConfigParameters<T>> extends AbstractRunConfigParameters<T>
{
  private String _fqn;
  private String _vmArgs;
  private String _progArgs;
  private String _workingDir;
  private String _jre;
  private boolean _bJre;

  public FqnRunConfigParameters()
  {
  }

  public FqnRunConfigParameters( String name, String fqn )
  {
    super( name );
    _fqn = fqn;
  }

  public String getFqn()
  {
    return _fqn;
  }
  public void setFqn( String fqn )
  {
    _fqn = fqn;
  }

  public String getVmArgs()
  {
    return _vmArgs;
  }
  public void setVmArgs( String vmArgs )
  {
    _vmArgs = vmArgs;
  }

  public String getProgArgs()
  {
    return _progArgs;
  }
  public void setProgArgs( String progArgs )
  {
    _progArgs = progArgs;
  }

  public String getWorkingDir()
  {
    return _workingDir;
  }
  public void setWorkingDir( String workingDir )
  {
    _workingDir = workingDir;
  }

  public String getJre()
  {
    return _jre;
  }
  public void setJre( String jre )
  {
    _jre = jre;
  }

  public boolean isJreEnabled()
  {
    return _bJre;
  }
  public void setJreEnabled( boolean bJre )
  {
    _bJre = bJre;
  }

  @Override
  public void copy( T to )
  {
    super.copy( to );
    to.setFqn( getFqn() );
    to.setVmArgs( getVmArgs() );
    to.setProgArgs( getProgArgs() );
    to.setWorkingDir( getWorkingDir() );
    to.setJreEnabled( isJreEnabled() );
    to.setJre( getJre() );
  }
}
