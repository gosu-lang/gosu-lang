/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.util;

import gw.lang.parser.GosuParserFactory;
import gw.lang.parser.IExpression;
import gw.lang.parser.IGosuParser;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.ScriptabilityModifiers;
import gw.lang.parser.StandardSymbolTable;
import gw.lang.parser.IParseIssue;
import gw.lang.parser.ParserOptions;
import gw.lang.parser.ExternalSymbolMapSymbolTableWrapper;
import gw.lang.parser.resources.ResourceKey;
import gw.lang.GosuShop;
import gw.lang.parser.expressions.IProgram;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.gs.BytecodeOptions;
import gw.test.TestClass;

import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Assert;

/**
 */
public class GosuTestUtil
{
  private static final Object[] EMPTY_OBJECT_ARR = new Object[0];

  public static Object eval(String script) {
    try {
      return evalGosu(script);
    } catch ( ParseResultsException e) {
      throw new RuntimeException(e);
    }
  }

  public static Object eval(String script, String name1, Object val1) {
    try {
      return evalGosu(script, name1, val1);
    } catch (ParseResultsException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Evaluates the given Gosu.
   *
   * @param script - Must be non-null
   */
  public static Object evalGosu(String script) throws ParseResultsException {
    return evalGosu(script, EMPTY_OBJECT_ARR );
  }

  /**
   * Evaluates the given Gosu, with variables of the given names that will be the
   * dynamic (runtime) type of the values passed in.
   *
   * @param val1 - Must be non-null
   */
  public static Object evalGosu(String script, String name1, Object val1) throws ParseResultsException {
    return evalGosu(script, new Object[]{name1, val1});
  }

  /**
   * Evaluates the given Gosu, with variables of the given names that will be the
   * dynamic (runtime) type of the values passed in.
   *
   * @param val1 cannot be null.
   * @param val2 cannot be null.
   */
  public static Object evalGosu(String script, String name1, Object val1, String name2, Object val2) throws ParseResultsException {
    return evalGosu(script, new Object[]{name1, val1, name2, val2});
  }

  /**
   * Evaluates the given Gosu, with variables of the given names that will be the
   * dynamic (runtime) type of the values passed in.
   *
   * @param val1 cannot be null.
   * @param val2 cannot be null.
   * @param val3 cannot be null.
   */
  public static Object evalGosu(String script, String name1, Object val1, String name2, Object val2, String name3, Object val3) throws ParseResultsException {
    return evalGosu(script, new Object[]{name1, val1, name2, val2, name3, val3});
  }

  /**
   * Evaluates the given Gosu, with variables of the given names that will be the
   * dynamic (runtime) type of the values passed in.
   *
   * @param val1 cannot be null.
   * @param val2 cannot be null.
   * @param val3 cannot be null.
   * @param val4 cannot be null.
   */
  public static Object evalGosu(String script, String name1, Object val1, String name2, Object val2,
                                      String name3, Object val3, String name4, Object val4) throws ParseResultsException {
    return evalGosu(script, new Object[]{name1, val1, name2, val2, name3, val3, name4, val4});
  }

  /**
   * Evaluates the given Gosu, with variables of the given names that will be the
   * dynamic (runtime) type of the values passed in.
   *
   * @param val1 cannot be null.
   * @param val2 cannot be null.
   * @param val3 cannot be null.
   * @param val4 cannot be null.
   * @param val5 cannot be null.
   */
  public static Object evalGosu(String script, String name1, Object val1, String name2, Object val2,
                                      String name3, Object val3, String name4, Object val4,
                                      String name5, Object val5) throws ParseResultsException {
    return evalGosu(script, new Object[]{name1, val1, name2, val2, name3, val3, name4, val4, name5, val5});
  }

  /**
   * Evaluates the given Gosu, with variables of the given names that will be the
   * dynamic (runtime) type of the values passed in.
   *
   * @param args must be an array of alternating non-null name/value pairs
   */
  public static Object evalGosu(String script, Object[] args) throws ParseResultsException
  {
    ISymbolTable table = new StandardSymbolTable(true);
    assert(args.length % 2 == 0) : "You must pass in an array of matchign name/value pairs";
    for (int i = 0; i < args.length; i = i + 2) {
      String name = (String) args[i];
      Object arg = args[i + 1];
      assert arg != null : "Argument " + name + " is null, so we cannot determine the type for it.  " +
              "If you wish to actually pass a null value in, construct an ISybmolTable with the null value " +
              "properly typed and pass it in directly";
      table.putSymbol( GosuShop.createSymbol(name, TypeSystem.getFromObject(arg), arg));
    }
    return evalGosu(script, table);
  }

  public static Object evalGosu(String script, ISymbolTable table) throws ParseResultsException {
    IGosuProgram gosuProgram = GosuParserFactory.createProgramParser().parseExpressionOrProgram( script, table, new ParserOptions() ).getProgram();
    return gosuProgram.evaluate(new ExternalSymbolMapSymbolTableWrapper(table));
  }

  public static IExpression compileExpression(String script) throws ParseResultsException {
    return compileExpression(script, new StandardSymbolTable(true));
  }

  public static IExpression compileExpression(String script, String varName, IType varType) throws ParseResultsException {
    StandardSymbolTable symbolTable = new StandardSymbolTable( true );
    symbolTable.putSymbol( GosuShop.createSymbol( varName, varType, null ) );
    return compileExpression(script, symbolTable );
  }

  public static IExpression compileExpression(String script, String varName, IType varType, String varName2, IType varType2) throws ParseResultsException {
    StandardSymbolTable symbolTable = new StandardSymbolTable( true );
    symbolTable.putSymbol( GosuShop.createSymbol( varName, varType, null ) );
    symbolTable.putSymbol( GosuShop.createSymbol( varName2, varType2, null ) );
    return compileExpression(script, symbolTable );
  }

  public static IExpression compileExpression( String script, ISymbolTable table ) throws ParseResultsException {
    IGosuParser parser = GosuParserFactory.createParser(script, table, ScriptabilityModifiers.SCRIPTABLE);
    parser.setThrowParseExceptionForWarnings( false );
    return parser.parseExpOrProgram( null );
  }

  public static IExpression compileExpression( String script, ISymbolTable table, boolean bThrowOnWarning ) throws ParseResultsException {
    IGosuParser parser = GosuParserFactory.createParser(script, table, ScriptabilityModifiers.SCRIPTABLE);
    parser.setThrowParseExceptionForWarnings( bThrowOnWarning );
    return parser.parseExpOrProgram( null );
  }

  public static IExpression compileExpression( String script, boolean bThrowOnWarning ) throws ParseResultsException {
    IGosuParser parser = GosuParserFactory.createParser(script, new StandardSymbolTable(true), ScriptabilityModifiers.SCRIPTABLE);
    parser.setThrowParseExceptionForWarnings( bThrowOnWarning );
    return parser.parseExpOrProgram( null );
  }

  public static IProgram compileProgram(String script, ISymbolTable table) throws ParseResultsException {
    IGosuParser parser = GosuParserFactory.createParser(script, table, ScriptabilityModifiers.SCRIPTABLE);
    parser.setEditorParser(true);
    return parser.parseProgram(null);
  }

  public static void assertHasErrors( String strClass, ResourceKey... expected ) throws ClassNotFoundException
  {
    assertHasErrors( loadClass( strClass ), expected );
  }

  private static IGosuClass loadClass(String strClass) {
    return (IGosuClass) TypeSystem.getByFullName(strClass);
  }

  public static void assertHasErrors( IGosuClass gsClass, ResourceKey... expected ) throws ClassNotFoundException
  {
    Comparator<ResourceKey> resourceKeyComparator = new Comparator<ResourceKey>() {
      @Override
      public int compare(ResourceKey o1, ResourceKey o2) {
        return o1.getKey().compareTo( o2.getKey() );
      }
    };
    Assert.assertFalse( gsClass.isValid() );
    List<ResourceKey> actual = new ArrayList<ResourceKey>( expected.length );
    for ( IParseIssue parseException : gsClass.getParseResultsException().getParseExceptions() ) {
      actual.add( parseException.getMessageKey() );
    }
    Arrays.sort( expected, resourceKeyComparator );
    Collections.sort( actual, resourceKeyComparator );
    TestClass.assertCollectionEquals(Arrays.asList(expected), actual);
  }

  public static void assertOneError( String strClass, ResourceKey errorKey ) throws ClassNotFoundException
  {
    assertOneError( loadClass( strClass ), errorKey );
  }

  public static void assertOneError( IGosuClass gsClass, ResourceKey errorKey )
  {
    Assert.assertFalse( gsClass.isValid() );
    Assert.assertEquals( 1, gsClass.getParseResultsException().getParseExceptions().size() );
    Assert.assertEquals( errorKey, gsClass.getParseResultsException().getParseExceptions().get( 0 ).getMessageKey() );
  }

  public static void assertOneWarning( String strClass, ResourceKey warningKey ) throws ClassNotFoundException
  {
    assertOneWarning( loadClass( strClass ), warningKey );
  }

  public static void assertOneWarning( IGosuClass gsClass, ResourceKey warningKey )
  {
    Assert.assertTrue( gsClass.isValid() );
    Assert.assertEquals( 1, gsClass.getParseResultsException().getParseWarnings().size() );
    Assert.assertEquals( warningKey, gsClass.getParseResultsException().getParseWarnings().get( 0 ).getMessageKey() );
  }

  /**
   * Gets the parse results exception caused by the given program.  Throws an IllegalArgumentException
   * if no parse exceptions are found.
   */
  public static ParseResultsException getParseResultsException( String script )
  {
    try
    {
      GosuTestUtil.compileExpression( script, new StandardSymbolTable( true ), true );
      throw new IllegalArgumentException( "The script \n\n\"" + script + "\"\n\ndid not cause a parse exception." );
    }
    catch( ParseResultsException e )
    {
      return e;
    }
  }

  public static void assertCausesPRE( String script, ResourceKey... keys )
  {
    try
    {
      eval( script );
      junit.framework.Assert.fail( "Should not have compiled" );
    }
    catch( RuntimeException e )
    {
      junit.framework.Assert.assertTrue( e.getCause() instanceof ParseResultsException );
      a:
      for( ResourceKey k : keys )
      {
        for( IParseIssue pi : ((ParseResultsException)e.getCause()).getParseIssues() )
        {
          if( pi.getMessageKey().equals( k ) )
          {
            continue a;
          }
        }
        junit.framework.Assert.fail( "Should have found a parse issue with key " + k );
      }
    }
  }
}
