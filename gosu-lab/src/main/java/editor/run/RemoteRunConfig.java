package editor.run;

import editor.util.EditorUtilities;

import javax.swing.*;

/**
 */
public class RemoteRunConfig extends AbstractRunConfig<RemoteRunConfigParameters>
{
  public RemoteRunConfig( RemoteRunConfigParameters params )
  {
    super( params );
  }

  @SuppressWarnings("UnusedDeclaration")
  // required for IJsonIO
  public RemoteRunConfig()
  {
  }

  public DebugTransport getTransport()
  {
    return getParams().getTransport();
  }

  public String getHost()
  {
    return getParams().getHost();
  }

  public String getPort()
  {
    return getParams().getPort();
  }

  public String getAddress()
  {
    return getParams().getAddress();
  }

  @Override
  public boolean isValid()
  {
    //## todo: validate parameters
    return true;
  }

  @Override
  public Icon getIcon()
  {
    return EditorUtilities.loadIcon( "images/remote.png" );
  }

  @Override
  public JComponent makePanel( RemoteRunConfigParameters params )
  {
    return new RemoteConfigPanel( params );
  }

  @Override
  public boolean isRunnable()
  {
    return false;
  }

  @Override
  public boolean isDebuggable()
  {
    return true;
  }

  @Override
  public IProcessRunner run()
  {
    throw new UnsupportedOperationException( "Attempted to run a remote config, only debugging supported" );
  }

  @Override
  public IProcessRunner debug()
  {
    OutOfProcessRemote processRunner = new OutOfProcessRemote();
    processRunner.execute( this );
    return processRunner;
  }
}
