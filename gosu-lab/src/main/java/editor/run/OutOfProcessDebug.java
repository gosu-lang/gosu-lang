package editor.run;

import com.sun.jdi.Bootstrap;
import com.sun.jdi.VirtualMachineManager;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.LaunchingConnector;
import gw.lang.Gosu;

import java.io.File;
import java.util.Map;

/**
 */
public class OutOfProcessDebug extends AbstractOutOfProcessExecutor<FqnRunConfig>
{
  public OutOfProcessDebug()
  {
    super( RunState.Debug );
  }

  protected String exec() throws Exception
  {
    VirtualMachineManager vmm = Bootstrap.virtualMachineManager();
    LaunchingConnector conn = vmm.defaultConnector();
    Map<String, Connector.Argument> defaultArguments = conn.defaultArguments();
    String jreHome = getRunConfig().getJreForProcessOrDefault( defaultArguments.get( "home" ).value() );
    defaultArguments.get( "home" ).setValue( jreHome );
    String progArgs = getRunConfig().getProgArgs() == null ? "" : getRunConfig().getProgArgs();
    defaultArguments.get( "main" ).setValue( Gosu.class.getName().replace( '.', '/' ) + " -fqn " + getRunConfig().getFqn() + " " + progArgs );
    String vmArgs = getRunConfig().getVmArgs() == null ? "" : getRunConfig().getVmArgs();
    defaultArguments.get( "options" ).setValue( vmArgs + " -cp \"" + makeClasspath( getGosuPanel(), true ) + "\"" );
    printLabMessage( makeDebuggingMessage( defaultArguments ) );
    setVm( conn.launch( defaultArguments ) );
    setProcess( getVm().process() );
    getGosuPanel().pipeInput();
    getGosuPanel().makeDebugger( getVm() );
    waitFor();
    return String.valueOf( "Process finished with exit code " + getProcess().exitValue() );
  }

  private String makeDebuggingMessage( Map<String, Connector.Argument> vmArgs )
  {
    StringBuilder sb = new StringBuilder();
    String java = vmArgs.get( "home" ).value() + File.separator + "bin" + File.separator + "java.exe";
    String javaArgs = vmArgs.get( "options" ).value();
    String target = vmArgs.get( "main" ).value();
    sb.append( java ).append( ' ' )
      .append( javaArgs ).append( ' ' )
      .append( target )
      .append( '\n' );
    sb.append( "Debugging: " ).append( getRunConfig().getName() ).append( "...\n" );
    return sb.toString();
  }
}
