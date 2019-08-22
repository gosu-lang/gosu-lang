package editor.run;

import com.sun.jdi.VirtualMachine;
import editor.GosuEditor;
import editor.GosuPanel;
import editor.LabFrame;
import editor.TextComponentWriter;
import editor.settings.CompilerSettings;
import editor.util.Experiment;
import gw.util.PathUtil;
import editor.util.TaskQueue;
import gw.util.GosuExceptionUtil;
import manifold.util.JreUtil;

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
    return makeClasspath( gosuPanel, true );
  }

  String makeClasspath( GosuPanel gosuPanel, boolean bToolsJar ) throws IOException
  {
    StringBuilder classpath = new StringBuilder();

    String javaHomePath = System.getProperty( "java.home" );

    addExperimentPaths( gosuPanel, classpath, javaHomePath );

    String cp = System.getProperty( "java.class.path" );
    StringTokenizer tok = new StringTokenizer( cp, File.pathSeparator );
    while( tok.hasMoreTokens() )
    {
      String path = tok.nextToken();
      if( path.contains( "gw-asm-all" ) ||
          path.contains( "gosu-core" ) ||
          path.contains( "manifold" ) )
      {
        classpath.append( path ).append( File.pathSeparator );
      }
    }

    if( JreUtil.isJava8() && bToolsJar )
    {
      classpath.append( PathUtil.findToolsJar() ).append( File.pathSeparator );
    }

    cp = classpath.toString();
    if( cp.endsWith( File.pathSeparator ) )
    {
      cp = cp.substring( 0, cp.length()-1 );
    }
    return cp;
  }

  private void addExperimentPaths( GosuPanel gosuPanel, StringBuilder classpath, String javaHomePath )
  {
    if( CompilerSettings.isStaticCompile() )
    {
      classpath.append( PathUtil.getAbsolutePathName( CompilerSettings.getCompilerOutputDir() ) ).append( File.pathSeparator );
    }
    Experiment experiment = gosuPanel.getExperiment();
    List<String> srcPaths = experiment.getSourcePath();
    for( String path : srcPaths )
    {
      if( path.startsWith( javaHomePath ) )
      {
        // don't pack jre jars
        continue;
      }

      if( !CompilerSettings.isStaticCompile() || PathUtil.isFile( PathUtil.getAbsolutePath( PathUtil.create( path ) ) ) )
      {
        // Include jars in classpath, include source directories only if running with static compilation OFF
        classpath.append( path ).append( File.pathSeparator );
      }
    }
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
    return LabFrame.instance().getGosuPanel();
  }
}
