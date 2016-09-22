package editor;

import com.sun.jdi.VirtualMachine;
import editor.util.TaskQueue;
import gw.config.CommonServices;
import gw.lang.Gosu;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.Modifier;
import gw.lang.reflect.ReflectUtil;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.GosuClassPathThing;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.java.JavaTypes;
import gw.util.GosuExceptionUtil;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
*/
public class InProcessRunner implements IProcessRunner
{
  private String _typeName;

  public InProcessRunner()
  {
  }

  @Override
  public String getTypeName()
  {
    return _typeName;
  }

  @Override
  public Process getProcess()
  {
    return null;
  }

  @Override
  public RunState getRunState()
  {
    return null;
  }

  @Override
  public VirtualMachine getVm()
  {
    return null;
  }

  public void execute( String typeName, GosuPanel gosuPanel )
  {
    try
    {
      _typeName = typeName;
      ClassLoader loader = InProcessRunner.class.getClassLoader();
      URLClassLoader runLoader = new URLClassLoader( getAllUrlsAboveGosuclassProtocol( (URLClassLoader)loader ), loader.getParent() );

      TaskQueue queue = TaskQueue.getInstance( "_execute_gosu" );
      gosuPanel.addBusySignal( RunState.Run );
      queue.postTask(
        () -> {
          GosuEditor.getParserTaskQueue().waitUntilAllCurrentTasksFinish();
          IGosuClass program = (IGosuClass)TypeSystem.getByFullName( typeName );
          try
          {
            Class<?> runnerClass = Class.forName( "editor.InProcessRunner", true, runLoader );
            String fqn = program.getName();
            printRunningMessage( fqn );
            gosuPanel.getExperiment().setRecentProgram( fqn );
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

              GosuClassPathThing.addOurProtocolHandler();
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

  private void printRunningMessage( String fqn )
  {
    SimpleAttributeSet attr = new SimpleAttributeSet();
    attr.addAttribute( StyleConstants.Foreground, new Color( 192, 192, 192 ) );
    TextComponentWriter out = (TextComponentWriter)System.out;
    out.setAttributes( attr );
    System.out.println( "Running: " + fqn + "...\n" );
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
    GosuClassPathThing.addOurProtocolHandler();
    GosuClassPathThing.init();
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
      runTest( gsType );
      return null;
    }
  }

  private void runTest( IGosuClass gsType ) throws Exception
  {
    Class cls = gsType.getBackingClass();
    Object instance = cls.newInstance();
    runNamedOrAnnotatedMethod( instance, "beforeClass", "org.junit.BeforeClass" );
    for( Method m : cls.getMethods() )
    {
      if( isTestMethod( m ) )
      {
        runNamedOrAnnotatedMethod( instance, "beforeMethod", "org.junit.Before" );
        try
        {
          System.out.println( " - " + m.getName() );
          m.invoke( instance );
          System.out.println( GosuPanel.SUCCESS );
        }
        catch( InvocationTargetException e )
        {
          //noinspection ThrowableResultOfMethodCallIgnored
          Throwable cause = GosuExceptionUtil.findExceptionCause( e );
          if( cause instanceof AssertionError )
          {
            System.out.println( GosuPanel.FAILED + cause.getClass().getSimpleName() + " : " + cause.getMessage() );
            String lines = findPertinentLines( gsType, cause );
            System.out.println( lines );
          }
          else
          {
            throw GosuExceptionUtil.forceThrow( cause );
          }
        }
        finally
        {
          runNamedOrAnnotatedMethod( instance, "afterMethod", "org.junit.After" );
        }
      }
    }
    runNamedOrAnnotatedMethod( instance, "afterClass", "org.junit.AfterClass" );
  }

  private String findPertinentLines( IGosuClass gsType, Throwable cause )
  {
    StringBuilder sb = new StringBuilder();
    StackTraceElement[] trace = cause.getStackTrace();
    for( int i = 0; i < trace.length; i++ )
    {
      StackTraceElement elem = trace[i];
      if( elem.getClassName().equals( gsType.getName() ) )
      {
        sb.append( "     at " ).append( elem.toString() ).append( "\n" );
      }
    }
    return sb.toString();
  }

  private boolean isTestMethod( Method m ) throws Exception
  {
    int modifiers = m.getModifiers();
    return Modifier.isPublic( modifiers ) &&
           (m.getName().startsWith( "test" ) || hasAnnotation( m, "org.junit.Test" )) &&
           m.getParameters().length == 0;
  }

  private void runNamedOrAnnotatedMethod( Object instance, String methodName, String annoName ) throws Exception
  {
    for( Method m : instance.getClass().getMethods() )
    {
      if( m.getName().equals( methodName ) )
      {
        m.invoke( instance );
        return;
      }
      for( Annotation anno : m.getAnnotations() )
      {
        if( anno.annotationType().getName().equals( annoName ) )
        {
          m.invoke( instance );
          return;
        }
      }
    }
  }

  private boolean hasAnnotation( Method m, String name ) throws Exception
  {
    for( Annotation anno : m.getAnnotations() )
    {
      if( anno.annotationType().getName().equals( name ) )
      {
        return true;
      }
    }
    return false;
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
