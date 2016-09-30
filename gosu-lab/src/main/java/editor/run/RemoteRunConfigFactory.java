package editor.run;

import editor.util.EditorUtilities;

import javax.swing.*;

/**
 */
public class RemoteRunConfigFactory implements IRunConfigFactory<RemoteRunConfig, RemoteRunConfigParameters>
{
  private static RemoteRunConfigFactory INSTANCE = new RemoteRunConfigFactory();

  public static RemoteRunConfigFactory instance()
  {
    return INSTANCE;
  }

  @Override
  public String getName()
  {
    return "Remote";
  }

  @Override
  public RemoteRunConfigParameters makeParameters()
  {
    RemoteRunConfigParameters params = new RemoteRunConfigParameters();
    // setup reasonable default values
    params.setTransport( DebugTransport.AttachingSocket );
    params.setHost( "localhost" );
    params.setPort( "5005" );
    params.setAddress( "gosudebug" );
    return params;
  }

  @Override
  public RemoteRunConfig newRunConfig( RemoteRunConfigParameters params )
  {
    return new RemoteRunConfig( params );
  }

  @Override
  public Icon getIcon()
  {
    return EditorUtilities.loadIcon( "images/remote.png" );
  }
}
