package editor;

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
import java.util.StringTokenizer;

/**
 */
public class OutOfProcessRunner implements IProcessRunner
{
  private Process _process;

  public OutOfProcessRunner()
  {
  }

  public void execute( String typeName, GosuPanel gosuPanel )
  {
    try
    {
      TaskQueue queue = TaskQueue.getInstance( "_execute_gosu" );
      gosuPanel.addBusySignal();
      queue.postTask(
        () -> {
          GosuEditor.getParserTaskQueue().waitUntilAllCurrentTasksFinish();
          IGosuClass program = (IGosuClass)TypeSystem.getByFullName( typeName );
          try
          {
            String fqn = program.getName();
            printRunningMessage( fqn );
            gosuPanel.getExperiment().setRecentProgram( fqn );
            String result = null;
            try
            {
              result = exec( fqn, gosuPanel );
            }
            finally
            {
              String programResults = result;
              EventQueue.invokeLater(
                () -> {
                  gosuPanel.removeBusySignal();
                  if( programResults != null )
                  {
                    System.out.print( programResults );
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

  public String exec( String fqn, GosuPanel gosuPanel ) throws Exception
  {
    String javaHome = System.getProperty( "java.home" );
    String javaBin = javaHome + File.separator + "bin" + File.separator + "java";

    String classpath = makeClasspath( gosuPanel );
    ProcessBuilder pb = new ProcessBuilder( javaBin, "-classpath", "\"" + classpath + "\"", Gosu.class.getName().replace( '.', '/' ), "-fqn", fqn );
    _process = pb.start();
    waitFor( _process );
    return String.valueOf( _process.exitValue() );
  }

  private int waitFor( Process proc ) throws IOException, InterruptedException
  {
    InputStream input = proc.getInputStream();

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

    return proc.waitFor();
  }

  private String makeClasspath( GosuPanel gosuPanel ) throws IOException
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
    return classpath.toString();
  }

  private void printRunningMessage( String fqn )
  {
    SimpleAttributeSet attr = new SimpleAttributeSet();
    attr.addAttribute( StyleConstants.Foreground, new Color( 192, 192, 192 ) );
    TextComponentWriter out = (TextComponentWriter)System.out;
    out.setAttributes( attr );
    System.out.println( "Running: " + fqn + "...\n" );
    out.setAttributes( null );
  }

  @Override
  public Process getProcess()
  {
    return _process;
  }
}
