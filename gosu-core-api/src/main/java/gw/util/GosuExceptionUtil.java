/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.util;

import gw.util.concurrent.LockingLazyVar;

import java.io.StringWriter;
import java.io.PrintWriter;

import gw.config.CommonServices;

public class GosuExceptionUtil
{
  private static final LockingLazyVar<IForceThrower> FORCE_THROWER = new LockingLazyVar<IForceThrower>() {
    @Override
    protected IForceThrower init() {
      return CommonServices.getGosuIndustrialPark().getForceThrower();
    }
  };

  /**
   * Given an Exception, finds the root cause and returns it. This may end up
   * returning the originally passed exception if that was the root cause.
   *
   * @param e the Throwable whose root cause should be located.
   *
   * @return the root Throwable, or e if it is the root.
   */
  public static Throwable findExceptionCause( Throwable e )
  {
    Throwable error = e;
    Throwable cause = e;
    while( (error.getCause() != null) && (error.getCause() != error) )
    {
      cause = error.getCause();
      error = cause;
    }
    return cause;
  }

  /**
   * This method can be used to provide a more informative type-mismatch exception message than the standard java reflection
   * does with its IllegalArgumentException.
   *
   * @param exceptionToWrap  - exception to wrap
   * @param featureName      - feature being executed when the (e.g. "method" or "constructor")
   * @param actualParameters - the actual parameter types expected
   * @param args             - the args passed to the feature
   */
  public static void throwArgMismatchException( IllegalArgumentException exceptionToWrap, String featureName, Class[] actualParameters, Object[] args )
  {
    // Build expected parameter types string efficiently using streams
    String paramTypesList = java.util.Arrays.stream(actualParameters)
        .map(Class::getName)
        .collect(java.util.stream.Collectors.joining(" ,"));
    String argTypes = "(" + paramTypesList + ")";

    // Build actual runtime types string efficiently using streams
    String runtimeTypesList = java.util.Arrays.stream(args)
        .map(arg -> arg != null ? arg.getClass().getName() : "null")
        .collect(java.util.stream.Collectors.joining(" ,"));
    String rttTypes = "(" + runtimeTypesList + ")";

    throw new RuntimeException( "Tried to pass values of types: " + rttTypes + " into " + featureName + " that takes types " + argTypes, exceptionToWrap );
  }

  /**
   * Given an Exception and an Exception type to look for, finds the exception of that type
   * or returns null if none of that type exist.
   */
  public static <T extends Throwable> T findException(Class<T> exceptionTypeToFind, Throwable t) {

    Throwable cause = t;

    while (cause != null)
    {
      if( exceptionTypeToFind.isAssignableFrom( cause.getClass() ) )
      {
        //noinspection unchecked
        return (T)cause;
      }
      if( cause == cause.getCause() )
      {
        return null;
      }
      cause = cause.getCause();
    }

    return null;
  }

  // Convert throwable to RuntimeException, avoiding unnecessary wrapping
  public static RuntimeException convertToRuntimeException( Throwable t )
  {
    if (t instanceof RuntimeException e) {
      return e;
    } else {
      return new RuntimeException(t);
    }
  }

  public static String getStackTraceAsString(Throwable t) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    t.printStackTrace(pw);
    return sw.toString();
  }

  /**
   * <p>This method will force the Throwable object that is passed in to be rethrown, regardless if it is a checked exception
   * or not, allowing a developer to side step dealing with or wrapping checked exceptions in java.
   *
   * <p>This method "returns" a RuntimeException, so that you don't have to put spurious return statements after
   * it is called, but the method never exits normally, so the return value is never used.
   *
   * <p>An example usage would be:
   * <pre>
   *   <b>throw</b> GosuExceptionUtil.forceThrow( myCheckedException );
   * </pre>
   *
   * @param t the throwable object to be rethrown.
   */
  public static RuntimeException forceThrow( Throwable t )
  {
    return forceThrow( t, null );
  }

  public static RuntimeException forceThrow( Throwable t, String ctxInfo )
  {
    if( ctxInfo != null )
    {
      addContextInfoImpl( t, ctxInfo );
    }
    FORCE_THROWER.get().throwException( t );
    return null;
  }

  public  static <T extends Throwable> T addContextInfo( T t, String ctxInfo )
  {
    return addContextInfoImpl( t, ctxInfo );
  }

  public static <T extends Throwable> T addContextInfoImpl( T t, String ctxInfo )
  {
    StackTraceElement[] currentStack = new RuntimeException().getStackTrace();
    StackTraceElement[] throwableStack = t.getStackTrace();
    for( int i = 0; i < throwableStack.length && i < currentStack.length; i++ )
    {
      StackTraceElement currentElt = currentStack[currentStack.length - i - 1];
      StackTraceElement throwableElt = throwableStack[throwableStack.length - i - 1];
      if( !GosuObjectUtil.equals( throwableElt.getClassName(), currentElt.getClassName() ) &&
          !areMethodsEquals( throwableElt, currentElt ) &&
          !areFilesEquals( throwableElt, currentElt ) &&
          throwableElt.getLineNumber() != currentElt.getLineNumber() )
      {
        break;
      }

      if( currentStack.length - i - 1 == 2 )
      {
        throwableElt = new StackTraceElement( throwableElt.getClassName(),
                                              throwableElt.getMethodName(),
                                              throwableElt.getFileName() + ":" + throwableElt.getLineNumber() + ") - (" + ctxInfo,
                                              -1 );
        throwableStack[throwableStack.length - i - 1] = throwableElt;
        t.setStackTrace( throwableStack );
        break;
      }
    }
    return t;
  }

  private static boolean areMethodsEquals( StackTraceElement throwableElt, StackTraceElement currentElt )
  {
    if( throwableElt.getMethodName() == null )
    {
      return currentElt.getMethodName() == null;
    }
    String s = throwableElt.getMethodName();
    int index = s.indexOf( ' ' );
    if( index >= 0 )
    {
      s = s.substring( 0, index );
    }
    return s.equals( currentElt.getMethodName() );
  }

  private static boolean areFilesEquals( StackTraceElement throwableElt, StackTraceElement currentElt )
  {
    if( throwableElt.getFileName() == null )
    {
      return currentElt.getFileName() == null;
    }
    String s = throwableElt.getFileName();
    int index = s.indexOf( ':' );
    if( index >= 0 )
    {
      s = s.substring( 0, index );
    }
    return s.equals( currentElt.getFileName() );
  }

  public interface IForceThrower
  {
    public void throwException( Throwable t );
  }
}
