/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.IJavaType;
import gw.util.GosuExceptionUtil;
import gw.lang.function.IBlock;

public class FunctionClassUtil extends ClassLoader
{
  public static final String FUNCTION_PACKAGE = "gw.lang.function";
  public static final String FUNCTION_CLASS_PREFIX = FUNCTION_PACKAGE + ".Function";
  public static final String FUNCTION_INTERFACE_PREFIX = FUNCTION_PACKAGE + ".IFunction";
  private static final Class[][] ARGS = new Class[IBlock.MAX_ARGS + 1][];
  static
  {
    for( int i = 0; i <= IBlock.MAX_ARGS; i++ )
    {
      Class[] classes1 = new Class[i];
      for( int j = 0; j < i; j++ )
      {
        classes1[j] = Object.class;
      }
      ARGS[i] = classes1;
    }
  }

  private FunctionClassUtil() {}

  public static IJavaType getFunctionClassForArity(int functionArity)
  {
    return getFunctionType( functionArity, FUNCTION_CLASS_PREFIX );
  }

  public static IJavaType getFunctionInterfaceForArity(int functionArity)
  {
    return getFunctionType( functionArity, FUNCTION_INTERFACE_PREFIX );
  }

  private static IJavaType getFunctionType(int functionArity, String s)
  {
    if( functionArity >= 0 && functionArity <= IBlock.MAX_ARGS ) {
      String functionTypeName = s + functionArity;
      return (IJavaType) TypeSystem.getByFullNameIfValid(functionTypeName, TypeSystem.getGlobalModule());
    }
    return null;
  }

  public static String getFunctionInterfaceSlashNameForArity( int arity )
  {
    return "gw/lang/function/IFunction" + arity;
  }

  private Class getFunctionClassForArityImpl( int functionArity )
  {
    String functionTypeName = FUNCTION_CLASS_PREFIX + functionArity;
    if( functionArity >= 0 && functionArity <= IBlock.MAX_ARGS ) {
      try
      {
        return Class.forName( functionTypeName, false, getClass().getClassLoader() );
      } catch (ClassNotFoundException e) {
        throw GosuExceptionUtil.forceThrow(e);
      }
    }
    return null;
  }

  public static Class[] getArgArrayForArity( int i )
  {
    return ARGS[i];
  }
}
