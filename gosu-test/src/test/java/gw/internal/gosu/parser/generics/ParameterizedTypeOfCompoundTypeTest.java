/*
 * Copyright 2026 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.generics;

import gw.internal.gosu.parser.CompoundType;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.java.JavaTypes;
import gw.test.TestClass;

import java.util.Date;

/**
 * Inside {@code if( item typeis Date )} a value of type T has the compound type {@code T & java.util.Date}, and
 * {@code {item}} is an {@code ArrayList<T & java.util.Date>}. Parameterized types are cached by the names of
 * their type arguments. TypeLord.appendTypeName() qualifies a type variable with the type that declares it. It
 * did not do so for a type variable inside a compound type, so "T & java.util.Date" was the key for every T. The
 * parameterized type of one T must not be handed out for another T: its elements are then not assignable to
 * their own type variable, in whichever declaration is compiled second.
 * <p>
 * The same problem as in CompoundTypeOfTypeVariableTest, one level up.
 */
public class ParameterizedTypeOfCompoundTypeTest extends TestClass
{
  private static final String PACKAGE = "gw.internal.gosu.parser.generics.gwtest.";

  public void testParameterizedTypeIsMadeOfTheCompoundTypeAskedFor()
  {
    IType date = TypeSystem.get( Date.class );
    IType listEAndDate = CompoundType.get( typeVariableOf( JavaTypes.LIST() ), date );
    IType setEAndDate = CompoundType.get( typeVariableOf( JavaTypes.SET() ), date );

    // Preconditions. Two compound types that are not the same but have the same name.
    assertNotSame( listEAndDate, setEAndDate );
    assertEquals( listEAndDate.getName(), setEAndDate.getName() );

    IType arrayListOfListE = JavaTypes.ARRAY_LIST().getParameterizedType( listEAndDate );
    IType arrayListOfSetE = JavaTypes.ARRAY_LIST().getParameterizedType( setEAndDate );

    assertSame( "the ArrayList of the E of List", listEAndDate, arrayListOfListE.getTypeParameters()[0] );
    assertSame( "the ArrayList of the E of Set", setEAndDate, arrayListOfSetE.getTypeParameters()[0] );
  }

  /** Two classes that each put a narrowed value of their own T in a list. Each is valid, whichever is compiled first. */
  public void testElementOfListOfNarrowedValuesIsAssignableToTheTypeVariableOfItsOwnClass()
  {
    assertValid( "ParameterizedCompoundTypeVarFirst" );
    assertValid( "ParameterizedCompoundTypeVarSecond" );
  }

  private static IType typeVariableOf( IType genericType )
  {
    return genericType.getGenericTypeVariables()[0].getTypeVariableDefinition().getType();
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
