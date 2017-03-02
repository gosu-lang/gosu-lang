package editor.run;

/**
 */
public class RemoteRunConfigParameters extends AbstractRunConfigParameters<RemoteRunConfigParameters>
{
  private DebugTransport _transport;
  private String _host;
  private String _port;
  private String _address;

  public RemoteRunConfigParameters( String name )
  {
    super( name );
  }

  public RemoteRunConfigParameters()
  {
  }

  public DebugTransport getTransport()
  {
    return _transport;
  }
  public void setTransport( DebugTransport transport )
  {
    _transport = transport;
  }

  public String getHost()
  {
    return _host;
  }
  public void setHost( String host )
  {
    _host = host;
  }

  public String getPort()
  {
    return _port;
  }
  public void setPort( String port )
  {
    _port = port;
  }

  public String getAddress()
  {
    return _address;
  }
  public void setAddress( String address )
  {
    _address = address;
  }

  @Override
  public void copy( RemoteRunConfigParameters to )
  {
    super.copy( to );
    to.setTransport( getTransport() );
    to.setHost( getHost() );
    to.setPort( getPort() );
    to.setAddress( getAddress() );
  }
}
