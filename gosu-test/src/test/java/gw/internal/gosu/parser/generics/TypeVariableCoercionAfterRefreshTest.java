/*
 * Copyright 2026 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.generics;

import gw.lang.reflect.ITypeRef;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.test.TestClass;

/**
 * Type variable types are not canonical (see the javadoc of {@code TypeVariableType.equals}). After an in-place
 * refresh of a class, as Studio does on an edit, types cached before the refresh still hold the old instance of a
 * type variable, so a value of type T can meet a new instance of the same T. That is not a coercion. If the parser
 * adds one, it needs T at runtime and reports "must be declared with the reified modifier" for valid code.
 */
public class TypeVariableCoercionAfterRefreshTest extends TestClass
{
  /** Covers T and T[] of a generic function. */
  public void testFunctionTypeVariable()
  {
    assertValidAfterRefresh( "gw.internal.gosu.parser.generics.gwtest.TypeVarAfterRefresh" );
  }

  /** The type variable of a generic enhancement has the same reified check. */
  public void testEnhancementTypeVariable()
  {
    assertValidAfterRefresh( "gw.internal.gosu.parser.generics.gwtest.TypeVarAfterRefreshEnhancement" );
  }

  private void assertValidAfterRefresh( String typeName )
  {
    IGosuClass gsClass = (IGosuClass)TypeSystem.getByFullName( typeName );
    assertValid( gsClass );

    TypeSystem.refresh( (ITypeRef)gsClass );

    assertValid( gsClass );
  }

  private void assertValid( IGosuClass gsClass )
  {
    // isValid() first: it is what compiles the class and produces the feedback
    boolean valid = gsClass.isValid();
    assertTrue( feedback( gsClass ), valid );
  }

  private String feedback( IGosuClass gsClass )
  {
    return gsClass.getParseResultsException() == null ? "" : gsClass.getParseResultsException().getFeedback();
  }
}
