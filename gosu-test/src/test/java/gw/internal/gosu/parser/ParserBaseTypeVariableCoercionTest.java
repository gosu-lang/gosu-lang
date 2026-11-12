/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.internal.gosu.parser.expressions.TypeVariableDefinitionImpl;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.expressions.Variance;
import gw.lang.reflect.FunctionType;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.reflect.java.JavaTypes;
import gw.test.TestClass;
import gw.util.GosuTestUtil;

/**
 * A generic function that returns the result of a generic call, for example
 * <pre>
 *   function getPlugin&lt;T&gt;( type : Class&lt;T&gt; ) : T { return config.getPlugin( type ) }
 * </pre>
 * makes an implicit coercion from the type variable T of the call to the type variable T of the function. Type
 * variable types are not canonical, so the two can be different instances of the same variable (see the javadoc of
 * {@code TypeVariableType.equals}). A coercion to a type variable is a runtime conversion that needs T at runtime,
 * so for a function that is not {@code reified} it reports "must be declared with the reified modifier" for valid
 * code. ParserBase must not add that coercion when both sides are one function's type variable.
 */
public class ParserBaseTypeVariableCoercionTest extends TestClass
{
  /** A generic function with one type variable, built without compiling any Gosu. */
  private static class GenericFunction
  {
    final FunctionType _function;
    final TypeVariableType _typeVar;

    GenericFunction( String functionName, String typeVarName, IType enclosingType )
    {
      TypeVariableDefinitionImpl typeVarDef = new TypeVariableDefinitionImpl( null, typeVarName, null, null, null, Variance.DEFAULT );
      _typeVar = new TypeVariableType( typeVarDef, true );
      GenericTypeVariable genericTypeVar = new GenericTypeVariable( typeVarDef, JavaTypes.OBJECT() );
      // This constructor makes the function the enclosing type of the type variable.
      _function = new FunctionType( functionName, _typeVar, new IType[]{JavaTypes.CLASS()}, new IGenericTypeVariable[]{genericTypeVar} );
      _function.setEnclosingType( enclosingType );
    }
  }

  public void testTwoInstancesOfTheSameFunctionTypeVariableAreTheSameVariable()
  {
    GenericFunction first = new GenericFunction( "getPlugin", "T", JavaTypes.STRING() );
    GenericFunction second = new GenericFunction( "getPlugin", "T", JavaTypes.STRING() );

    // Preconditions. They must be distinct instances, as found after a refresh (ISSTUDIO-1908), and the
    // type system must already call them interchangeable. Otherwise the assertion below proves nothing.
    assertNotSame( first._typeVar, second._typeVar );
    assertNotSame( first._function, second._function );
    assertTrue( first._typeVar.equals( second._typeVar ) );

    assertTrue( ParserBase.isSameFunctionTypeVariable( first._typeVar, second._typeVar ) );
    assertTrue( ParserBase.isSameFunctionTypeVariable( second._typeVar, first._typeVar ) );
  }

  public void testAnInstanceIsTheSameVariableAsItself()
  {
    GenericFunction function = new GenericFunction( "getPlugin", "T", JavaTypes.STRING() );

    assertTrue( ParserBase.isSameFunctionTypeVariable( function._typeVar, function._typeVar ) );
  }

  public void testDifferentTypeVariableNamesAreDifferentVariables()
  {
    GenericFunction first = new GenericFunction( "getPlugin", "T", JavaTypes.STRING() );
    GenericFunction second = new GenericFunction( "getPlugin", "U", JavaTypes.STRING() );

    assertFalse( ParserBase.isSameFunctionTypeVariable( first._typeVar, second._typeVar ) );
  }

  public void testDifferentFunctionsAreDifferentVariables()
  {
    GenericFunction first = new GenericFunction( "getPlugin", "T", JavaTypes.STRING() );
    GenericFunction second = new GenericFunction( "getCurrentPlugin", "T", JavaTypes.STRING() );

    assertFalse( ParserBase.isSameFunctionTypeVariable( first._typeVar, second._typeVar ) );
  }

  public void testSameFunctionNameInDifferentTypesAreDifferentVariables()
  {
    GenericFunction first = new GenericFunction( "getPlugin", "T", JavaTypes.STRING() );
    GenericFunction second = new GenericFunction( "getPlugin", "T", JavaTypes.OBJECT() );

    assertFalse( ParserBase.isSameFunctionTypeVariable( first._typeVar, second._typeVar ) );
  }

  public void testATypeThatIsNotATypeVariableIsNeverTheSameVariable()
  {
    GenericFunction function = new GenericFunction( "getPlugin", "T", JavaTypes.STRING() );

    assertFalse( ParserBase.isSameFunctionTypeVariable( function._typeVar, JavaTypes.OBJECT() ) );
    assertFalse( ParserBase.isSameFunctionTypeVariable( JavaTypes.OBJECT(), function._typeVar ) );
    assertFalse( ParserBase.isSameFunctionTypeVariable( function._typeVar, null ) );
    assertFalse( ParserBase.isSameFunctionTypeVariable( null, function._typeVar ) );
  }

  /** Only function type variables are covered: the reified check does not fail for the type variable of a class. */
  public void testTypeVariablesOfAClassAreNotMatched()
  {
    IType first = JavaTypes.LIST().getGenericTypeVariables()[0].getTypeVariableDefinition().getType();
    IType second = new TypeVariableType( JavaTypes.LIST(), JavaTypes.LIST().getGenericTypeVariables()[0] );
    assertNotSame( first, second );

    assertFalse( ParserBase.isSameFunctionTypeVariable( first, second ) );
  }

  /**
   * The check must stay for a coercion that really needs T at runtime: a cast from another type to T, in a
   * function that is not reified.
   */
  public void testCastToTheTypeVariableOfANonReifiedFunctionStillReportsAnError()
  {
    try
    {
      GosuTestUtil.evalGosu( "function toT<T>( o : Object ) : T {\n  return o as T\n}\nreturn null" );
      fail( "A cast to T in a function that is not reified must not compile" );
    }
    catch( ParseResultsException e )
    {
      assertTrue( e.getFeedback(), e.getFeedback().contains( "reified" ) );
    }
  }

  /** A generic function that returns the result of another generic call compiles without the reified modifier. */
  public void testGenericFunctionReturningAGenericCallCompiles() throws ParseResultsException
  {
    Object result = GosuTestUtil.evalGosu(
        "function inner<T>( type : Class<T> ) : T {\n  return null\n}\n" +
        "function outer<T>( type : Class<T> ) : T {\n  return inner( type )\n}\n" +
        "return outer( String ) == null" );
    assertEquals( Boolean.TRUE, result );
  }
}
