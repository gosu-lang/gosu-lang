/*
 * Copyright 2026 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.generics;

import gw.internal.gosu.parser.CompoundType;
import gw.internal.gosu.parser.TypeVariableType;
import gw.lang.parser.IParseIssue;
import gw.lang.parser.resources.Res;
import gw.lang.reflect.ICompoundType;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeVariableType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.java.JavaTypes;
import gw.test.TestClass;

import java.util.Date;
import java.util.List;

/**
 * Inside {@code if( item typeis Date )} a value of type T has the compound type {@code T & java.util.Date}.
 * Compound types are cached by name, and every type variable named T has the name "T", whichever class or function
 * declares it. The compound type of one T must not be handed out for another T: the value is then no longer
 * assignable to its own type variable and has the members of the other one's bound, in whichever declaration is
 * compiled second.
 */
public class CompoundTypeOfTypeVariableTest extends TestClass
{
  private static final String PACKAGE = "gw.internal.gosu.parser.generics.gwtest.";

  public void testCompoundTypeIsMadeOfTheTypeVariableAskedFor()
  {
    IType listE = typeVariableOf( JavaTypes.LIST() );
    IType setE = typeVariableOf( JavaTypes.SET() );
    IType date = TypeSystem.get( Date.class );

    // Preconditions. Two type variables that are not the same but have the same name, so that their compound
    // types have the same name too.
    assertEquals( listE.getName(), setE.getName() );
    assertFalse( listE.equals( setE ) );

    IType listEAndDate = CompoundType.get( listE, date );
    IType setEAndDate = CompoundType.get( setE, date );
    assertEquals( listEAndDate.getName(), setEAndDate.getName() );

    assertSame( "the compound type made from the E of List", listE, typeVariableIn( listEAndDate ) );
    assertSame( "the compound type made from the E of Set", setE, typeVariableIn( setEAndDate ) );
    assertTrue( "E & Date is assignable to its own E", setE.isAssignableFrom( setEAndDate ) );
    assertFalse( "E & Date is not assignable to the E of another type", listE.isAssignableFrom( setEAndDate ) );
  }

  public void testCompoundTypeOfTheSameTypesIsCached()
  {
    IGenericTypeVariable e = JavaTypes.LIST().getGenericTypeVariables()[0];
    IType date = TypeSystem.get( Date.class );

    IType listE = typeVariableOf( JavaTypes.LIST() );
    assertSame( CompoundType.get( listE, date ), CompoundType.get( listE, date ) );

    // Two instances of one type variable are the same type, see TypeVariableType.equals()
    IType first = new TypeVariableType( JavaTypes.LIST(), e );
    IType second = new TypeVariableType( JavaTypes.LIST(), e );
    assertNotSame( first, second );
    assertSame( CompoundType.get( first, date ), CompoundType.get( second, date ) );
  }

  /** Two classes that each narrow a value of their own T. Each is valid, whichever is compiled first. */
  public void testNarrowedValueIsAssignableToTheTypeVariableOfItsOwnClass()
  {
    assertValid( "CompoundTypeVarFirst" );
    assertValid( "CompoundTypeVarSecond" );
  }

  /** The same in one class: two generic functions, each with a T of its own. */
  public void testNarrowedValueIsAssignableToTheTypeVariableOfItsOwnFunction()
  {
    assertValid( "CompoundTypeVarTwoFunctions" );
  }

  /**
   * The narrowed value has the members of the bound of its own T. Here the first class calls run() on a
   * {@code T extends Runnable}, which is valid. The second calls it on a {@code T extends CharSequence}, which is
   * an error that must still be reported after the first class was compiled.
   */
  public void testNarrowedValueHasTheBoundOfItsOwnTypeVariable()
  {
    assertValid( "CompoundTypeVarBoundedByRunnable" );

    IGosuClass errant = (IGosuClass)TypeSystem.getByFullName( PACKAGE + "Errant_CompoundTypeVarBoundedByCharSequence" );
    assertFalse( "run() is not a method of CharSequence & Closeable", errant.isValid() );
    List<IParseIssue> errors = errant.getParseResultsException().getParseExceptions();
    assertEquals( feedback( errant ), 1, errors.size() );
    assertEquals( Res.MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD, errors.get( 0 ).getMessageKey() );
  }

  private static IType typeVariableOf( IType genericType )
  {
    return genericType.getGenericTypeVariables()[0].getTypeVariableDefinition().getType();
  }

  private static IType typeVariableIn( IType compoundType )
  {
    for( IType type : ((ICompoundType)compoundType).getTypes() )
    {
      if( type instanceof ITypeVariableType )
      {
        return type;
      }
    }
    return null;
  }

  private void assertValid( String relativeName )
  {
    IGosuClass gsClass = (IGosuClass)TypeSystem.getByFullName( PACKAGE + relativeName );
    // isValid() first: it is what compiles the class and produces the feedback
    boolean valid = gsClass.isValid();
    assertTrue( feedback( gsClass ), valid );
  }

  private String feedback( IGosuClass gsClass )
  {
    return gsClass.getParseResultsException() == null ? "" : gsClass.getParseResultsException().getFeedback();
  }
}
