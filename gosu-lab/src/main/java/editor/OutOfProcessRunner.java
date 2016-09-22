package editor;

import com.sun.jdi.Bootstrap;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.VirtualMachineManager;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.LaunchingConnector;
import editor.util.TaskQueue;
import gw.lang.Gosu;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.util.GosuExceptionUtil;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 */
public class OutOfProcessRunner implements IProcessRunner
{
  private String _typeName;
  private RunState _runState;
  private Process _process;
  private VirtualMachine _vm;

  public OutOfProcessRunner( RunState runState )
  {
    _runState = runState;
  }

  @Override
  public String getTypeName()
  {
    return _typeName;
  }

  @Override
  public RunState getRunState()
  {
    return _runState;
  }

  @Override
  public Process getProcess()
  {
    return _process;
  }

  public VirtualMachine getVm()
  {
    return _vm;
  }

  public void execute( String typeName, GosuPanel gosuPanel )
  {
    try
    {
      _typeName = typeName;
      TaskQueue queue = TaskQueue.getInstance( "_execute_gosu" );
      gosuPanel.addBusySignal( _runState );
      queue.postTask(
        () -> {
          GosuEditor.getParserTaskQueue().waitUntilAllCurrentTasksFinish();
          IGosuClass program = (IGosuClass)TypeSystem.getByFullName( typeName );
          try
          {
            String fqn = program.getName();
            gosuPanel.getExperiment().setRecentProgram( fqn );
            String result = null;
            try
            {
              switch( _runState )
              {
                case Run:
                  result = exec( fqn, gosuPanel );
                  break;
                case Debug:
                  result = debug( fqn, gosuPanel );
                  break;
                default:
                  throw new IllegalStateException( "Unexpected RunState: " + _runState );
              }
            }
            finally
            {
              String programResults = result;
              EventQueue.invokeLater(
                () -> {
                  gosuPanel.removeBusySignal();
                  if( programResults != null )
                  {
                    printLabMessage( programResults );
                  }
                } );
            }
          }
          catch( Exception e )
          {
            Throwable cause = GosuExceptionUtil.findExceptionCause( e );
            throw GosuExceptionUtil.forceThrow( cause );
          }
        } );
    }
    catch( Throwable t )
    {
      editor.util.EditorUtilities.handleUncaughtException( t );
    }
  }

  private String exec( String fqn, GosuPanel gosuPanel ) throws Exception
  {
    String javaHome = System.getProperty( "java.home" );
    String javaBin = javaHome + File.separator + "bin" + File.separator + "java";

    String debugArgs = _runState == RunState.Debug ? "-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005" : "";

    String classpath = makeClasspath( gosuPanel );
    ProcessBuilder pb = new ProcessBuilder( javaBin, debugArgs, "-classpath", "\"" + classpath + "\"", Gosu.class.getName().replace( '.', '/' ), "-fqn", fqn );
    printLabMessage( makeRunningMessage( fqn, pb.command() ) );
    _process = pb.start();
    gosuPanel.pipeInput();
    waitFor();
    return String.valueOf( "Process finished with exit code " + _process.exitValue() );
  }

  private String debug( String fqn, GosuPanel gosuPanel ) throws Exception
  {
    VirtualMachineManager vmm = Bootstrap.virtualMachineManager();
    LaunchingConnector conn = vmm.defaultConnector();
    Map<String, Connector.Argument> defaultArguments = conn.defaultArguments();
    defaultArguments.get( "main" ).setValue( Gosu.class.getName().replace( '.', '/' ) + " -fqn " + fqn );
    defaultArguments.get( "options" ).setValue( "-cp \"" + makeClasspath( gosuPanel, true ) + "\"" );
    printLabMessage( makeDebuggingMessage( fqn, defaultArguments ) );
    _vm = conn.launch( defaultArguments );
    _process = _vm.process();
    gosuPanel.pipeInput();
    gosuPanel.makeDebugger( _vm );
    waitFor();
    return String.valueOf( "Process finished with exit code " + _process.exitValue() );
  }

  private int waitFor() throws IOException, InterruptedException
  {
    InputStream input = _process.getInputStream();

    byte[] b = new byte[512];
    int read = 1;
    while( read > -1 )
    {
      read = input.read( b, 0, b.length );
      if( read > -1 )
      {
        System.out.write( b, 0, read );
      }
    }

    return _process.waitFor();
  }

  private String makeClasspath( GosuPanel gosuPanel ) throws IOException
  {
    return makeClasspath( gosuPanel, false );
  }

  private String makeClasspath( GosuPanel gosuPanel, boolean bToolsJar ) throws IOException
  {
    StringBuilder classpath = new StringBuilder();

    String javaHomePath = System.getProperty( "java.home" );

    List<String> srcPaths = gosuPanel.getExperiment().getSourcePath();

    for( String path : srcPaths )
    {
      if( path.startsWith( javaHomePath ) )
      {
        // don't pack jre jars
        continue;
      }

      classpath.append( path ).append( File.pathSeparator );
    }

    String cp = System.getProperty( "java.class.path" );
    StringTokenizer tok = new StringTokenizer( cp, File.pathSeparator );
    while( tok.hasMoreTokens() )
    {
      String path = tok.nextToken();
      if( path.contains( "gw-asm-all" ) ||
          path.contains( "gosu-core" ) )
      {
        classpath.append( path ).append( File.pathSeparator );
      }
    }

    if( bToolsJar )
    {
      String javaHome = System.getProperty( "java.home" );
      String toolsJar = javaHome + File.separator + "lib" + File.separator + "tools.jar";
      classpath.append( toolsJar ).append( File.pathSeparator );
    }

    return classpath.toString();
  }

  private String makeRunningMessage( String fqn, List<String> command )
  {
    StringBuilder sb = new StringBuilder();
    for( String part : command )
    {
      sb.append( part ).append( ' ' );
    }
    sb.append( '\n' );
    sb.append( "Running: " ).append( fqn ).append( "...\n" );
    return sb.toString();
  }

  private String makeDebuggingMessage( String fqn, Map<String, Connector.Argument> vmArgs )
  {
    StringBuilder sb = new StringBuilder();
    String java = vmArgs.get( "home" ).value() + File.separator + "bin" + File.separator + "java.exe";
    String javaArgs = vmArgs.get( "options" ).value();
    String target = vmArgs.get( "main" ).value();
    sb.append( java ).append( ' ' )
      .append( javaArgs ).append( ' ' )
      .append( target )
      .append( '\n' );
    sb.append( "Debugging: " ).append( fqn ).append( "...\n" );
    return sb.toString();
  }

  private void printLabMessage( String message )
  {
    SimpleAttributeSet attr = new SimpleAttributeSet();
    attr.addAttribute( StyleConstants.Foreground, new Color( 192, 192, 192 ) );
    TextComponentWriter out = (TextComponentWriter)System.out;
    out.setAttributes( attr );
    System.out.println( message );
    out.setAttributes( null );
  }
}
