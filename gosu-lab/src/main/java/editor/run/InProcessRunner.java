package editor.run;

import com.sun.jdi.VirtualMachine;
import editor.GosuEditor;
import editor.GosuPanel;
import editor.LabFrame;
import editor.TextComponentWriter;
import editor.util.TaskQueue;
import gw.config.CommonServices;
import gw.lang.Gosu;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.ReflectUtil;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.GosuBootstrap;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.java.JavaTypes;
import gw.util.GosuExceptionUtil;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
*/
public class InProcessRunner implements IProcessRunner<FqnRunConfig>
{
  private FqnRunConfig _runConfig;

  public InProcessRunner()
  {
  }

  @Override
  public FqnRunConfig getRunConfig()
  {
    return _runConfig;
  }

  @Override
  public Process getProcess()
  {
    return null;
  }

  @Override
  public RunState getRunState()
  {
    return RunState.Run;
  }

  @Override
  public VirtualMachine getVm()
  {
    return null;
  }

  public void execute( FqnRunConfig runConfig )
  {
    try
    {
      _runConfig = runConfig;
      ClassLoader loader = InProcessRunner.class.getClassLoader();
      URLClassLoader runLoader = new URLClassLoader( getAllUrlsAboveGosuclassProtocol( (URLClassLoader)loader ), loader.getParent() );

      TaskQueue queue = TaskQueue.getInstance( "_execute_gosu" );
      GosuPanel gosuPanel = LabFrame.instance().getGosuPanel();
      gosuPanel.addBusySignal( RunState.Run );
      queue.postTask(
        () -> {
          GosuEditor.getParserTaskQueue().waitUntilAllCurrentTasksFinish();
          IGosuClass program = (IGosuClass)TypeSystem.getByFullName( runConfig.getFqn() );
          try
          {
            Class<?> runnerClass = Class.forName( "editor.InProcessRunner", true, runLoader );
            String fqn = program.getName();
            printRunningMessage( runConfig.getName() );
            String result = null;
            try
            {
              result = (String)runnerClass.getMethod( "run", String.class, List.class ).
                invoke( null, fqn, gosuPanel.getExperiment().getSourcePath().stream().map( File::new ).collect( Collectors.toList() ) );
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

              GosuBootstrap.addOurProtocolHandler();
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

  private void printRunningMessage( String runConfigName )
  {
    SimpleAttributeSet attr = new SimpleAttributeSet();
    attr.addAttribute( StyleConstants.Foreground, new Color( 192, 192, 192 ) );
    TextComponentWriter out = (TextComponentWriter)System.out;
    out.setAttributes( attr );
    System.out.println( "Running: " + runConfigName + "...\n" );
    out.setAttributes( null );
  }

  private URL[] getAllUrlsAboveGosuclassProtocol( URLClassLoader loader )
  {
    List<URL> urls = new ArrayList<>();
    boolean bAdd = true;
    for( URL url : loader.getURLs() )
    {
      if( bAdd && !url.getProtocol().contains( "gosu" ) )
      {
        urls.add( url );
      }
      else
      {
        bAdd = false;
      }
    }
    return urls.toArray( new URL[urls.size()] );
  }

  public String run( String typeName, List<File> classpath ) throws Exception
  {
    Gosu.init( classpath );
    GosuBootstrap.addOurProtocolHandler();
    GosuBootstrap.init();
    IGosuClass gsType = (IGosuClass)TypeSystem.getByFullNameIfValid( typeName );
    if( gsType instanceof IGosuProgram )
    {
      Object result = ((IGosuProgram)gsType).evaluate( null );
      return (String)CommonServices.getCoercionManager().convertValue( result, JavaTypes.STRING() );
    }
    else
    {
      IMethodInfo mainMethod = hasStaticMain( gsType );
      if( mainMethod != null )
      {
        ReflectUtil.invokeStaticMethod( gsType.getName(), "main", new Object[]{new String[]{}} );
        return null;
      }
      Gosu.runTest( gsType );
      return null;
    }
  }

  private IMethodInfo hasStaticMain( IGosuClass gsType )
  {
    IMethodInfo main = gsType.getTypeInfo().getMethod( "main", JavaTypes.STRING().getArrayType() );
    if( main != null && main.isStatic() && main.getReturnType() == JavaTypes.pVOID() )
    {
      return main;
    }
    return null;
  }
}
