/*
 * Copyright 2026 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.generics;

import gw.lang.parser.IParseIssue;
import gw.lang.parser.coercers.TypeVariableCoercer;
import gw.lang.parser.expressions.ITypeAsExpression;
import gw.lang.parser.resources.Res;
import gw.lang.parser.statements.IFunctionStatement;
import gw.lang.reflect.ReflectUtil;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.test.TestClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Inside {@code if( item typeis Date )} a value of type T is cast to the compound type {@code T & java.util.Date}.
 * Narrowing a value does not read T at runtime, so it must not change whether a function has to be reified: what
 * compiles without the {@code typeis} compiles with it. The parser used to report "must be declared with the
 * reified modifier" for the cast, and for the narrowed value wherever a T was expected.
 */
public class TypeVariableNarrowedByTypeisTest extends TestClass
{
  private static final String PACKAGE = "gw.internal.gosu.parser.generics.gwtest.";

  public void testFunctionThatIsNotReifiedNarrowsItsOwnTypeVariable()
  {
    assertValid( "NarrowedTypeVarOneFunction" );

    Object instance = ReflectUtil.construct( PACKAGE + "NarrowedTypeVarOneFunction" );
    assertEquals( 5L, ReflectUtil.invokeMethod( instance, "total", Arrays.asList( "abc", new Date( 5 ), 7 ) ) );
  }

  /**
   * A reified function and one that is not, in one class, narrow their own T to the same types. This compiled
   * only while the compound type cache handed the T of the reified function to the other one.
   */
  public void testReifiedFunctionAndOneThatIsNotNarrowTheirOwnTypeVariable()
  {
    assertValid( "NarrowedTypeVarTwoFunctions" );

    Object instance = ReflectUtil.construct( PACKAGE + "NarrowedTypeVarTwoFunctions" );
    assertEquals( 8L, ReflectUtil.invokeMethod( instance, "total", Arrays.asList( "abc", new Date( 5 ), 7 ) ) );
  }

  /** The type variable of a generic enhancement has the same reified check. */
  public void testEnhancementFunctionThatIsNotReifiedNarrowsTheTypeVariableOfTheEnhancement()
  {
    assertValid( "NarrowedTypeVarEnhancement" );
  }

  /**
   * {@code List<T> & Runnable} is made of bytecode types only. The cast to such a type used to come with a
   * runtime coercion that never ran but did read T.
   */
  public void testFunctionThatIsNotReifiedNarrowsAParameterizedType()
  {
    assertValid( "NarrowedTypeVarParameterized" );

    Object instance = ReflectUtil.construct( PACKAGE + "NarrowedTypeVarParameterized" );

    RunnableList list = new RunnableList();
    assertSame( list, ReflectUtil.invokeMethod( instance, "runList", list ) );
    assertEquals( 1, list.runs );

    assertEquals( "first", ReflectUtil.invokeMethod( instance, "runArrayList", list ) );
    assertEquals( 2, list.runs );
  }

  /** The narrowed value is still a T: returned, assigned and passed on as one without a coercion. */
  public void testNarrowedValueIsUsedAsItsTypeVariable()
  {
    assertValid( "NarrowedTypeVarUsedAsTypeVar" );

    Object instance = ReflectUtil.construct( PACKAGE + "NarrowedTypeVarUsedAsTypeVar" );
    Date date = new Date( 5 );
    for( String function : new String[]{"returned", "assigned", "inferred", "passed"} )
    {
      assertSame( function, date, ReflectUtil.invokeMethod( instance, function, date ) );
      assertNull( function, ReflectUtil.invokeMethod( instance, function, "abc" ) );
      assertEquals( function, 0, coercionsToTypeVariable( "NarrowedTypeVarUsedAsTypeVar", function ) );
    }
  }

  /**
   * Where T is reified the narrowed value is coerced to T with TypeVariableCoercer as before, whatever
   * TypeVariableType.equals() makes of the two types, so that the class file does not depend on it.
   */
  public void testReifiedFunctionCoercesTheNarrowedValueToItsTypeVariable()
  {
    assertValid( "NarrowedTypeVarUsedAsTypeVar" );
    assertEquals( 1, coercionsToTypeVariable( "NarrowedTypeVarUsedAsTypeVar", "reifiedReturned" ) );
  }

  /** Reading T at runtime through the narrowed value is no different from reading it through any other. */
  public void testReadingTheTypeVariableThroughTheNarrowedValueNeedsReified()
  {
    IGosuClass errant = (IGosuClass)TypeSystem.getByFullName( PACKAGE + "Errant_NarrowedTypeVarReadAtRuntime" );
    assertFalse( "T is read at runtime", errant.isValid() );
    List<IParseIssue> errors = errant.getParseResultsException().getParseExceptions();
    assertEquals( feedback( errant ), 2, errors.size() );
    for( IParseIssue error : errors )
    {
      assertEquals( feedback( errant ), Res.MSG_TYPE_NOT_REIFIED, error.getMessageKey() );
    }
  }

  private int coercionsToTypeVariable( String relativeName, String function )
  {
    IGosuClass gsClass = (IGosuClass)TypeSystem.getByFullName( PACKAGE + relativeName );
    List<IFunctionStatement> functions = new ArrayList<>();
    gsClass.getClassStatement().getContainedParsedElementsByType( IFunctionStatement.class, functions );
    int count = 0;
    for( IFunctionStatement functionStmt : functions )
    {
      if( function.equals( functionStmt.getFunctionName() ) )
      {
        List<ITypeAsExpression> coercions = new ArrayList<>();
        functionStmt.getContainedParsedElementsByType( ITypeAsExpression.class, coercions );
        for( ITypeAsExpression coercion : coercions )
        {
          if( coercion.getCoercer() == TypeVariableCoercer.instance() )
          {
            count++;
          }
        }
      }
    }
    return count;
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

  public static class RunnableList extends ArrayList<String> implements Runnable
  {
    public int runs;

    public RunnableList()
    {
      add( "first" );
    }

    @Override
    public void run()
    {
      runs++;
    }
  }
}
