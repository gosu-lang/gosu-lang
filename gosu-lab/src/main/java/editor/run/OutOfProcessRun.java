package editor.run;

import gw.lang.Gosu;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class OutOfProcessRun extends AbstractOutOfProcessExecutor<FqnRunConfig>
{
  public OutOfProcessRun()
  {
    super( RunState.Run );
  }

  protected String exec() throws Exception
  {
    String javaHome = System.getProperty( "java.home" );
    String javaBin = javaHome + File.separator + "bin" + File.separator + "java";

    String classpath = makeClasspath( getGosuPanel() );
    List<String> args = new ArrayList<>();
    args.add( javaBin );
    //noinspection unchecked
    args.addAll( getRunConfig().makeVmArgs() );
    args.add( "-classpath" );
    args.add( "\"" + classpath + "\"" );
    args.add( Gosu.class.getName().replace( '.', '/' ) );
    args.add( "-fqn" );
    args.add( getRunConfig().getFqn() );
    //noinspection unchecked
    args.addAll( getRunConfig().makeProgArgs() );

    ProcessBuilder pb = new ProcessBuilder( args );
    printLabMessage( makeRunningMessage( getRunConfig().getName(), pb.command() ) );
    setProcess( pb.start() );
    getGosuPanel().pipeInput();
    waitFor();
    return String.valueOf( "Process finished with exit code " + getProcess().exitValue() );
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
}
