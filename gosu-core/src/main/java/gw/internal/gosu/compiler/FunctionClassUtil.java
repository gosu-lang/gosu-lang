/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.IJavaType;
import gw.lang.function.IBlock;

public class FunctionClassUtil extends ClassLoader
{
  public static final String FUNCTION_PACKAGE = "gw.lang.function";
  public static final String FUNCTION_CLASS_PREFIX = FUNCTION_PACKAGE + ".Function";
  public static final String PROCEDURE_CLASS_PREFIX = FUNCTION_PACKAGE + ".Procedure";
  public static final String FUNCTION_INTERFACE_PREFIX = FUNCTION_PACKAGE + ".IFunction";
  public static final String PROCEDURE_INTERFACE_PREFIX = FUNCTION_PACKAGE + ".IProcedure";
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

  public static IJavaType getFunctionClassForArity( boolean hasReturn, int arity )
  {
    return getFunctionType( arity, hasReturn ? FUNCTION_CLASS_PREFIX : PROCEDURE_CLASS_PREFIX );
  }

  public static IJavaType getFunctionInterfaceForArity( boolean hasReturn, int arity )
  {
    return getFunctionType( arity, hasReturn ? FUNCTION_INTERFACE_PREFIX : PROCEDURE_INTERFACE_PREFIX );
  }

  private static IJavaType getFunctionType( int arity, String s )
  {
    if( arity >= 0 && arity <= IBlock.MAX_ARGS ) {
      String functionTypeName = s + arity;
      return (IJavaType)TypeSystem.getByFullNameIfValid( functionTypeName );
    }
    return null;
  }

  public static Class[] getArgArrayForArity( int i )
  {
    return ARGS[i];
  }
}
