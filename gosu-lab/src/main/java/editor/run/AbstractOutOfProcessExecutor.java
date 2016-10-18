package editor.run;

import com.sun.jdi.VirtualMachine;
import editor.GosuEditor;
import editor.GosuPanel;
import editor.RunMe;
import editor.TextComponentWriter;
import editor.util.TaskQueue;
import gw.util.GosuExceptionUtil;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.StringTokenizer;

/**
 */
public abstract class AbstractOutOfProcessExecutor<T extends IRunConfig> implements IProcessRunner<T>
{
  private T _runConfig;
  private RunState _runState;
  private Process _process;
  private VirtualMachine _vm;

  public AbstractOutOfProcessExecutor( RunState runState )
  {
    _runState = runState;
  }

  protected abstract String exec() throws Exception;

  @Override
  public T getRunConfig()
  {
    return _runConfig;
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

  protected void setProcess( Process process )
  {
    _process = process;
  }

  public VirtualMachine getVm()
  {
    return _vm;
  }

  public void setVm( VirtualMachine vm )
  {
    _vm = vm;
  }

  public void execute( T runConfig )
  {
    try
    {
      _runConfig = runConfig;
      TaskQueue queue = TaskQueue.getInstance( "_execute_gosu" );
      getGosuPanel().addBusySignal( _runState );
      queue.postTask(
        () -> {
          GosuEditor.getParserTaskQueue().waitUntilAllCurrentTasksFinish();
          try
          {
            String result = null;
            try
            {
              result = exec();
            }
            finally
            {
              String programResults = result;
              EventQueue.invokeLater(
                () -> {
                  getGosuPanel().removeBusySignal();
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

  int waitFor() throws IOException, InterruptedException
  {
    captureErrorStream();

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

  private void captureErrorStream() throws IOException
  {
    new Thread( () -> {
      InputStream input = _process.getErrorStream();

      byte[] b = new byte[512];
      int read = 1;
      while( read > -1 )
      {
        try
        {
          read = input.read( b, 0, b.length );
        }
        catch( IOException e )
        {
          // eat
        }
        if( read > -1 )
        {
          System.out.write( b, 0, read );
        }
        if( !_process.isAlive() )
        {
          break;
        }
      }
    }, "Capture Error Stream" ).start();
  }

  String makeClasspath( GosuPanel gosuPanel ) throws IOException
  {
    return makeClasspath( gosuPanel, false );
  }

  String makeClasspath( GosuPanel gosuPanel, boolean bToolsJar ) throws IOException
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

  void printLabMessage( String message )
  {
    if( getGosuPanel().getConsolePanel() == null )
    {
      return;
    }
    SimpleAttributeSet attr = new SimpleAttributeSet();
    attr.addAttribute( StyleConstants.Foreground, new Color( 192, 192, 192 ) );
    TextComponentWriter out = (TextComponentWriter)System.out;
    out.setAttributes( attr );
    System.out.println( message );
    out.setAttributes( null );
  }

  protected GosuPanel getGosuPanel()
  {
    return RunMe.getEditorFrame().getGosuPanel();
  }
}
