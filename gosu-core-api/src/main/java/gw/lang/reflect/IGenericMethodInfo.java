/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.reflect.gs.IGosuClass;

public interface IGenericMethodInfo
{
  /**
   * @return An array of generic type variables if this feature corresponds with
   *         a generic type.
   */
  IGenericTypeVariable[] getTypeVariables();

  IType getParameterizedReturnType( IType... typeParams );

  IType[] getParameterizedParameterTypes( IType... typeParams );
  IType[] getParameterizedParameterTypes2( IType owningParameterizedType, IType[] typeParams );

  /**
   * @param argTypes The argument types from a generic method call.
   *
   * @return A map of inferred type parameters based on the argTypes. The map
   *         contains only the types that could be inferred -- the map may be empty.
   *         <p/>
   *         E.g.,
   *         given generic method: <T> T[] toArray( T[] )
   *         and call: list.toArray( new String[list.size()] );
   *         => the toArray() method call should be automatically parameterized with <String>
   *         based on the new String[0].
   */
  TypeVarToTypeMap inferTypeParametersFromArgumentTypes( IType... argTypes );
  TypeVarToTypeMap inferTypeParametersFromArgumentTypes2( IType owningParameterizedType, IType... argTypes );
}
