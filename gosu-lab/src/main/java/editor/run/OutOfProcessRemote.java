package editor.run;

import com.sun.jdi.Bootstrap;
import com.sun.jdi.VirtualMachineManager;
import com.sun.jdi.connect.AttachingConnector;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.ListeningConnector;

import java.util.List;
import java.util.Map;

/**
 */
public class OutOfProcessRemote extends AbstractOutOfProcessExecutor<RemoteRunConfig>
{
  public OutOfProcessRemote()
  {
    super( RunState.Debug );
  }

  protected String exec() throws Exception
  {
    Connector conn = getConnector();
    Map<String, Connector.Argument> args = applyArgs( conn, getRunConfig().getTransport() );
    printLabMessage( makeMessage( args ) );
    if( conn instanceof AttachingConnector )
    {
      AttachingConnector attachingConn = (AttachingConnector)conn;
      setVm( attachingConn.attach( args ) );
      getGosuPanel().makeDebugger( getVm() );
      return String.valueOf( "Attached to: " + getVm().name() );
    }
    else
    {
      ListeningConnector listeningConn = (ListeningConnector)conn;
      setVm( listeningConn.accept( args ) );
      getGosuPanel().makeDebugger( getVm() );
      return String.valueOf( "Accepted connection to: " + getVm().name() );
    }
  }

  private Connector getConnector()
  {
    VirtualMachineManager vmm = Bootstrap.virtualMachineManager();
    List<AttachingConnector> attachingConnectors = vmm.attachingConnectors();
    for( AttachingConnector conn : attachingConnectors )
    {
      DebugTransport transport = getRunConfig().getTransport();
      if( conn.name().equals( transport.getFqn() ) )
      {
        return conn;
      }
    }
    List<ListeningConnector> listeningConnectors = vmm.listeningConnectors();
    for( ListeningConnector conn : listeningConnectors )
    {
      DebugTransport transport = getRunConfig().getTransport();
      if( conn.name().equals( transport.getFqn() ) )
      {
        return conn;
      }
    }
    throw new IllegalStateException();
  }

  private Map<String, Connector.Argument> applyArgs( Connector conn, DebugTransport transport )
  {
    Map<String, Connector.Argument> args = conn.defaultArguments();
    switch( transport )
    {
      case AttachingSocket:
      case ListeningSocket:
        applySocketArgs( args );
        break;
      case AttachingMemory:
      case ListeningMemory:
        applySharedMemoryArgs( args );
        break;
    }
    return args;
  }

  private void applySharedMemoryArgs( Map<String, Connector.Argument> args )
  {
    RemoteRunConfig runConfig = getRunConfig();
    Connector.Argument name = args.get( "name" );
    name.setValue( runConfig.getAddress() );
  }

  private void applySocketArgs( Map<String, Connector.Argument> args )
  {
    RemoteRunConfig runConfig = getRunConfig();
    String host = runConfig.getHost();
    if( host != null && !host.isEmpty() )
    {
      Connector.Argument hostname = args.get( "hostname" );
      if( hostname != null )
      {
        hostname.setValue( host );
      }
    }
    Connector.Argument port = args.get( "port" );
    port.setValue( runConfig.getPort() );
  }

  private String makeMessage( Map<String, Connector.Argument> args )
  {
    StringBuilder sb = new StringBuilder();
    for( Map.Entry<String, Connector.Argument> entry: args.entrySet() )
    {
      sb.append( entry.getKey() ).append( ": " ).append( entry.getValue() ).append( "\n" );
    }
    sb.append( '\n' );
    sb.append( "Attaching: " ).append( getRunConfig().getName() ).append( "...\n" );
    return sb.toString();
  }
}
