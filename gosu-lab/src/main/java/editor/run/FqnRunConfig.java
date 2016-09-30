package editor.run;

import gw.lang.reflect.TypeSystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

/**
 */
public abstract class FqnRunConfig<T extends FqnRunConfigParameters<T>> extends AbstractRunConfig<T>
{
  public FqnRunConfig( T params )
  {
    super( params );
  }

  // this if for reading from json
  @SuppressWarnings("UnusedDeclaration")
  protected FqnRunConfig()
  {
  }

  public String getFqn()
  {
    return getParams().getFqn();
  }

  public String getVmArgs()
  {
    return getParams().getVmArgs();
  }

  public String getProgArgs()
  {
    return getParams().getProgArgs();
  }

  @Override
  public boolean isValid()
  {
    //## todo: verify the entire run config parameters
    return TypeSystem.getByFullNameIfValid( getFqn() ) != null;
  }

  @Override
  public boolean isRunnable()
  {
    return true;
  }

  @Override
  public boolean isDebuggable()
  {
    return true;
  }

  @Override
  public IProcessRunner run()
  {
    OutOfProcessRun processRunner = new OutOfProcessRun();
    processRunner.execute( this );
    return processRunner;
  }

  @Override
  public IProcessRunner debug()
  {
    OutOfProcessDebug processRunner = new OutOfProcessDebug();
    processRunner.execute( this );
    return processRunner;
  }

  public List<String> makeVmArgs()
  {
    String vmArgs = getVmArgs();
    if( vmArgs == null || vmArgs.isEmpty() )
    {
      return Collections.emptyList();
    }
    List<String> list = new ArrayList<>();
    StringTokenizer tok = new StringTokenizer( vmArgs, " \t\n" );
    while( tok.hasMoreTokens() )
    {
      String token = tok.nextToken();
      list.add( token );
    }
    return list;
  }
  
  public List<String> makeProgArgs()
  {
    String ProgArgs = getProgArgs();
    if( ProgArgs == null || ProgArgs.isEmpty() )
    {
      return Collections.emptyList();
    }
    List<String> list = new ArrayList<>();
    StringTokenizer tok = new StringTokenizer( ProgArgs, " \t\n" );
    while( tok.hasMoreTokens() )
    {
      String token = tok.nextToken();
      list.add( token );
    }
    return list;
  }
}
