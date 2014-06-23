/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.lang.reflect.IType;

public interface IResolvingCoercer extends ICoercer
{
  /**
   * <p>This method should produce a more appropriately parameterized type for the given target type for this
   * coercion.  This allows a coercion to communicate type information through the coercion process.  An example
   * is the MetaType-to-Class coercion.  MetaType<Foo> should be interpreted as Class<Foo> from a type inference
   * perspective, so this give the coercer a chance to let the compiler know so.</p>
   *
   * <p>If no more appropriate inference type exists, this method should return the <b>source</b> type.</p>
   * 
   * @param target type
   * @param source
   * @return a possibly more appropriately parameterized type or the source type if not
   */
  IType resolveType( IType target, IType source );
}
