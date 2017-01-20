/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

import gw.internal.gosu.parser.GosuProgramParser;
import gw.internal.gosu.parser.Symbol;
import gw.lang.function.IBlock;
import gw.lang.parser.GosuParserFactory;
import gw.lang.parser.IGosuProgramParser;
import gw.lang.parser.ILanguageLevel;
import gw.lang.parser.StandardSymbolTable;
import gw.lang.parser.ParserOptions;
import gw.lang.parser.ExternalSymbolMapSymbolTableWrapper;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.resources.Res;
import gw.lang.reflect.IMetaType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.FunctionType;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.gs.IProgramInstance;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import junit.framework.AssertionFailedError;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import java.math.BigInteger;

public class ProgramTest extends ByteCodeTestBase
{
  //
  // Field testing
  //

  public void testFieldNoReturnHasNullDefaultReturnValue() throws Exception
  {
    String ret =
      (String)IGosuProgram.Runner.runProgram( "var _field0 = \"hello\"\n" );
    assertEquals( null, ret );
  }

  public void testFieldNotInitialized() throws Exception
  {
    String ret =
      (String)IGosuProgram.Runner.runProgram( "var _field0 : String\n" +
                                              "return _field0\n" );
    assertEquals( null, ret );
  }

  public void testFieldInitializedWithType() throws Exception
  {
    String ret =
      (String)IGosuProgram.Runner.runProgram( "var _field0 : String = \"hello\"\n" +
                                              "return _field0\n" );
    assertEquals( "hello", ret );
  }

  public void testFieldInitializedWithoutType() throws Exception
  {
    String ret =
      (String)IGosuProgram.Runner.runProgram( "var _field0 = \"hello\"\n" +
                                              "return _field0\n" );
    assertEquals( "hello", ret );
  }

  public void testFieldInitializedWithGlobal() throws Exception
  {
    StandardSymbolTable symTable = makeSymbolTableWithGlobalSymbol();
    IProgramInstance instance =
      IGosuProgram.Runner.getProgramInstance( "var _field0 = globalSym\n" +
                                              "return _field0\n", symTable );
    symTable.getSymbol( "globalSym" ).setValue( "value1" );
    assertEquals( "value1", instance.evaluate(new ExternalSymbolMapSymbolTableWrapper(symTable)) );
  }

  public void testFieldsInterspersed() throws Exception
  {
    String ret =
      (String)IGosuProgram.Runner.runProgram( "var _field0 = \"hello\"\n" +
                                              "_field0 += \"bye\"\n" +
                                              "var _field1 = _field0\n" +
                                              "return _field1\n" );
    assertEquals( "hellobye", ret );
  }

  public void testFieldWithShorthandProperty() throws Exception
  {
    String ret =
      (String)IGosuProgram.Runner.runProgram( "var _field0 : String as Field0 = \"hello\"\n" +
                                              "return Field0\n" );
    assertEquals( "hello", ret );
  }

  public void testFieldWithShorthandReadonlyProperty() throws Exception
  {
    String ret =
      (String)IGosuProgram.Runner.runProgram( "var _field0 : String as readonly Field0 = \"hello\"\n" +
                                              "return Field0\n" );
    assertEquals( "hello", ret );
  }

  public void testFieldWithShorthandPropertySetter() throws Exception
  {
    String ret =
      (String)IGosuProgram.Runner.runProgram( "var _field0 : String as Field0 = \"hello\"\n" +
                                              "Field0 = \"bye\"\n" +
                                              "return _field0" );
    assertEquals( "bye", ret );
  }

  //
  // Function testing
  //

  public void testFunction() throws Exception
  {
    String ret =
      (String)IGosuProgram.Runner.runProgram( "return foo( \"testFunction\" )\n" +
                                              "function foo( s : String ) : String {\n" +
                                              "  return s\n" +
                                              "}\n" );
    assertEquals( "testFunction", ret );
  }

  public static class Goober
  {
    public void gooberMethod()
    {
      System.out.println( "gooberMethod()" );
    }
  }
  public static class SubGoober extends Goober implements Runnable, Iterable
  {
    @Override
    public void run()
    {
      System.out.println( "run()" );
    }

    @Override
    public Iterator iterator()
    {
      System.out.println( "iterator()" );
      return null;
    }
  }

  public void testCompoundTypeImplicitTypeAsWithContextInference() throws Exception
  {
    IGosuProgram.Runner.runProgram( "gw.internal.gosu.compiler.sample.statement.HasCompoundType.doit()" );
  }
  
  public void testMultipleFunction() throws Exception
  {
    String ret =
      (String)IGosuProgram.Runner.runProgram( "return foo( \"testFunction\" )\n" +
                                              "function foo( s : String ) : String {\n" +
                                              "  return \"foo\" + bar( s )\n" +
                                              "}" +
                                              "function bar( s : String ) : String {\n" +
                                              "  return \"bar\" + s\n" +
                                              "}\n" );
    assertEquals( "foobartestFunction", ret );
  }

  public void testMultipleFunctionInterspersedWithGlobalFields() throws Exception
  {
    String ret =
      (String)IGosuProgram.Runner.runProgram( "function foo( s : String ) : String {\n" +
                                              "  return \"foo\" + bar( s )\n" +
                                              "}\n" +
                                              "var _field0 = \"tween\"\n" +
                                              "function bar( s : String ) : String {\n" +
                                              "  return _field0 + s\n" +
                                              "}\n" +
                                              "return foo( \"testFunction\" )\n" );
    assertEquals( "footweentestFunction", ret );
  }

  public void testErrant_FunctionDeclaresLocalVarConflictingWithField() throws Exception
  {
    ParseResultsException res =
      parseErrantProgram(
        "var _field1 : String\n" +
        "function foo() {\n" +
        "  var _field1 : String\n" +
        "}" );
    assertEquals( 1, res.getParseExceptions().size() );
    assertEquals( Res.MSG_VARIABLE_ALREADY_DEFINED, res.getParseExceptions().get( 0 ).getMessageKey() );
  }

  private ParseResultsException parseErrantProgram( String strProgram ) {
    IGosuProgramParser pcp = GosuParserFactory.createProgramParser();
    try {
      pcp.parseProgramOnly( strProgram, new StandardSymbolTable( true ), new ParserOptions().withExpectedType( JavaTypes.pVOID() ) );
      throw new AssertionFailedError( "Expected to find parse errors" );
    }
    catch( ParseResultsException e ) {
      return e;
    }
  }

  //
  // Property testing
  //

  public void testProperty() throws Exception
  {
    String ret =
      (String)IGosuProgram.Runner.runProgram( "return foo\n" +
                                              "property get foo() : String {\n" +
                                              "  return \"testProperty\"\n" +
                                              "}\n" );
    assertEquals( "testProperty", ret );
  }

  public void testMultipleProperty() throws Exception
  {
    String ret =
      (String)IGosuProgram.Runner.runProgram( "return foo\n" +
                                              "property get foo() : String {\n" +
                                              "  return \"foo\" + bar\n" +
                                              "}" +
                                              "property get bar() : String {\n" +
                                              "  return \"bar\"\n" +
                                              "}\n" );
    assertEquals( "foobar", ret );
  }

  public void testMultiplePropertyInterspersedWithGlobalFields() throws Exception
  {
    String ret =
      (String)IGosuProgram.Runner.runProgram( "property get foo() : String {\n" +
                                              "  return \"foo\" + bar\n" +
                                              "}\n" +
                                              "var _field0 = \"tween\"\n" +
                                              "property get bar() : String {\n" +
                                              "  return _field0 + \"testProperty\"\n" +
                                              "}\n" +
                                              "return foo\n" );
    assertEquals( "footweentestProperty", ret );
  }


  //
  // Block testing
  //

  public void testGlobalBlock() throws Exception
  {
    int ret =
      (Integer)IGosuProgram.Runner.runProgram( "var _field0 = \\x:int -> x+x\n" +
                                               "return _field0( 5 )\n" );
    assertEquals( 10, ret );
  }

  public void testBlockPassedToFunction() throws Exception
  {
    int ret =
      (Integer)IGosuProgram.Runner.runProgram( "return foo( \\p -> p*p )\n" +
                                               "function foo( cb( n:int ) : int ) : int {\n" +
                                               "  return cb( 9 )\n" +
                                               "}\n" );
    assertEquals( 81, ret );
  }

  public void testBlockInFunction() throws Exception
  {
    int ret =
      (Integer)IGosuProgram.Runner.runProgram( "var b = foo()\n" +
                                               "return b( 8 )" +
                                               "function foo() : block(p:int):int {\n" +
                                               "  return \\p:int -> p*p\n" +
                                               "}\n" );
    assertEquals( 64, ret );
  }

  public void testBlockInFunctionCapturesParam() throws Exception
  {
    int ret =
      (Integer)IGosuProgram.Runner.runProgram( "var b = foo( 10 )\n" +
                                               "return b( 8 )" +
                                               "function foo( factor:int ) : block(p:int):int {\n" +
                                               "  return \\p:int -> p*factor\n" +
                                               "}\n" );
    assertEquals( 80, ret );
  }

  public void testBlockInFunctionCapturesGlobal() throws Exception
  {
    int ret =
      (Integer)IGosuProgram.Runner.runProgram( "var _field0 = 11" +
                                               "var b = foo()\n" +
                                               "function foo() : block(p:int):int {\n" +
                                               "  return \\p:int -> p*_field0\n" +
                                               "}\n" +
                                               "var _field1 = b( 8 )\n" +
                                               "_field0 = 12\n" +
                                               "return b( 8 ) + _field1\n" +
                                               "" );
    assertEquals( 184, ret );
  }


  //
  // Inner Class testing
  //

  public void testInnerClass() throws Exception
  {
    String ret =
      (String)IGosuProgram.Runner.runProgram( "var _field0 = \"shrapnel\"\n" +
                                              "return new MyInnerClass().blah()\n" +
                                              "class MyInnerClass {\n" +
                                              "  function blah() : String {\n" +
                                              "    return _field0\n" +
                                              "  }\n" +
                                              "}\n" );
    assertEquals( "shrapnel", ret );
  }

  public void testMultipleClasses() throws Exception
  {
    int ret = (Integer)IGosuProgram.Runner.runProgram( "var tangle = new Triangle()\n" +
                                                       "return tangle.Sides\n" +
                                                       "class Shape {\n" +
                                                       "  var _iSides : int as Sides\n" +
                                                       "  construct( iSides: int ) {\n" +
                                                       "    _iSides = iSides\n" +
                                                       "  }\n" +
                                                       "}\n" +
                                                       "\n" +
                                                       "class Triangle extends Shape {\n" +
                                                       "  construct() {\n" +
                                                       "    super( 3 )\n" +
                                                       "  }\n" +
                                                       "}\n" );
    assertEquals( 3, ret );
  }

  public void testMultipleClasses2() throws Exception
  {
    int ret = (Integer)IGosuProgram.Runner.runProgram( "var tangle = new Triangle()\n" +
                                                       "return tangle.Sides\n" +
                                                       "class Shape {\n" +
                                                       "  var _iSides : int as Sides\n" +
                                                       "  construct() {\n" +
                                                       "    _iSides = 1\n" +
                                                       "  }\n" +
                                                       "}\n" +
                                                       "\n" +
                                                       "class Triangle extends Shape {\n" +
                                                       "  construct() {\n" +
                                                       "  }\n" +
                                                       "}\n" );
    assertEquals( 1, ret );
  }

  public void testMultipleClasses3() throws Exception
  {
    int ret = (Integer)IGosuProgram.Runner.runProgram( "return new Bar().boo()\n" +
                                                       "\n" +
                                                       "class Foo\n" +
                                                       "{\n" +
                                                       "}\n" +
                                                       "\n" +
                                                       "class Bar extends Foo\n" +
                                                       "{\n" +
                                                       "  function boo() : int {return 5}\n" +
                                                       "}\n" );
    assertEquals( 5, ret );
  }

  public void testAnonymousInnerClass() throws Exception
  {
    String ret =
      (String)IGosuProgram.Runner.runProgram( "var _field0 = \"shrapnel\"\n" +
                                              "var aic = \n" +
                                              "  new MyInnerClass() {\n" +
                                              "    override function blah() : String {\n" +
                                              "      return super.blah() + \"anon\"\n" +
                                              "    }\n" +
                                              "  }\n" +
                                              "return aic.blah()\n" +
                                              "class MyInnerClass {\n" +
                                              "  function blah() : String {\n" +
                                              "    return _field0\n" +
                                              "  }\n" +
                                              "}\n" );
    assertEquals( "shrapnelanon", ret );
  }

  public void testAnonymousInnerClassInFunction() throws Exception
  {
    String ret =
      (String)IGosuProgram.Runner.runProgram( "var _field0 = \"shrapnel\"\n" +
                                              "function foo() : MyInnerClass {\n" +
                                              "  var aic = \n" +
                                              "    new MyInnerClass() {\n" +
                                              "      override function blah() : String {\n" +
                                              "        return super.blah() + _field0\n" +
                                              "      }\n" +
                                              "    }\n" +
                                              "  return aic\n" +
                                              "}\n" +
                                              "return foo().blah()\n" +
                                              "class MyInnerClass {\n" +
                                              "  function blah() : String {\n" +
                                              "    return _field0\n" +
                                              "  }\n" +
                                              "}\n" );
    assertEquals( "shrapnelshrapnel", ret );
  }

  public void testAnonymousInnerClassInFunctionReferencesGlobalSymbol() throws Exception
  {
    if( ILanguageLevel.Util.STANDARD_GOSU() )
    {
      // global (external) symbols are deprecated in standard Gosu
      return;
    }

    StandardSymbolTable symTable = makeSymbolTableWithGlobalSymbol();

    IProgramInstance instance =
      IGosuProgram.Runner.getProgramInstance( "var _field0 = \"shrapnel\"\n" +
                                              "function foo() : MyInnerClass {\n" +
                                              "  var aic = \n" +
                                              "    new MyInnerClass() {\n" +
                                              "      override function blah() : String {\n" +
                                              "        return super.blah() + _field0 + globalSym\n" +
                                              "      }\n" +
                                              "    }\n" +
                                              "  return aic\n" +
                                              "}\n" +
                                              "return foo().blah()\n" +
                                              "class MyInnerClass {\n" +
                                              "  function blah() : String {\n" +
                                              "    return _field0\n" +
                                              "  }\n" +
                                              "}\n", symTable );

    symTable.getSymbol( "globalSym" ).setValue( "value1" );
    assertEquals( "shrapnelshrapnelvalue1", instance.evaluate(new ExternalSymbolMapSymbolTableWrapper(symTable)) );
  }

  public void testAnonymousInnerClassInFunctionReferencesGlobalSymbolFromGlobalBlock() throws Exception
  {
    if( ILanguageLevel.Util.STANDARD_GOSU() )
    {
      // global (external) symbols are deprecated in standard Gosu
      return;
    }

    StandardSymbolTable symTable = makeSymbolTableWithGlobalSymbol();

    IProgramInstance instance =
      IGosuProgram.Runner.getProgramInstance( "var _field0 = \"shrapnel\"\n" +
                                              "function foo() : MyInnerClass {\n" +
                                              "  var aic = \n" +
                                              "    new MyInnerClass() {\n" +
                                              "      override function blah( bl( st:String ) : String ) : String {\n" +
                                              "        return super.blah( bl ) + _field0 + globalSym\n" +
                                              "      }\n" +
                                              "    }\n" +
                                              "  return aic\n" +
                                              "}\n" +
                                              "return foo().blah( \\st:String -> st + \"_fromBlock\")\n" +
                                              "class MyInnerClass {\n" +
                                              "  function blah( bl( st:String ) : String ) : String {\n" +
                                              "    return bl( _field0 )\n" +
                                              "  }\n" +
                                              "}\n", symTable );

    symTable.getSymbol( "globalSym" ).setValue( "value1" );
    assertEquals( "shrapnel_fromBlockshrapnelvalue1", instance.evaluate(new ExternalSymbolMapSymbolTableWrapper(symTable)) );
  }

// @KnownBreak
//todo: PL-8284
//  public void testAnonymousInnerClassInFunctionReferencesGlobalSymbolFromLocalBlock() throws Exception
//  {
//    StandardSymbolTable symTable = makeSymbolTableWithGlobalSymbol();
//
//    IProgramInstance instance =
//      IGosuProgram.Runner.getProgramInstance( "var _field0 = \"shrapnel\"\n" +
//                                              "function foo() : MyInnerClass {\n" +
//                                              "  var aic = \n" +
//                                              "    new MyInnerClass() {\n" +
//                                              "      override function blah( st : String ) : block(st:String):String {\n" +
//                                              "        return \\p:String -> {var blk = super.blah( st ); return blk( p ) + _field0 + globalSym}\n" +
//                                              "      }\n" +
//                                              "    }\n" +
//                                              "  return aic\n" +
//                                              "}\n" +
//                                              "var cb = foo().blah( \"whatever\" )\n" +
//                                              "return cb( \"blockParam\" )\n" +
//                                              "class MyInnerClass {\n" +
//                                              "  function blah( st : String ) : block(st:String):String {\n" +
//                                              "    return \\p:String -> p + st + _field0\n" +
//                                              "  }\n" +
//                                              "}\n", symTable );
//
//    symTable.getSymbol( "globalSym" ).setValue( "value1" );
//    assertEquals( "blockParamwhatevershrapnelshrapnelvalue1", instance.call() );
//  }


  //
  // Globlal Symbol testing
  //

  public void testSimpleWithSymTable() throws Exception
  {
    StandardSymbolTable symTable = makeSymbolTableWithGlobalSymbol();

    IGosuProgramParser pcp = new GosuProgramParser();
    IGosuProgram gp = pcp.parseProgramOnly(
      "var _field0 = globalSym\n" +
      "foo( _field0 )\n" +
      "function foo( s : String ) {\n" +
      "  print( s )\n" +
      "}\n" +
      "return new MyInnerClass().blah()\n" +
      "class MyInnerClass {\n" +
      "  function blah() : String {\n" +
      "    return _field0\n" +
      "  }\n" +
      "}\n", symTable, new ParserOptions() ).getProgram();

    assertTrue( gp.isValid() );

    Class<?> javaClass = gp.getBackingClass();
    assertNotNull( javaClass );
    IProgramInstance instance = (IProgramInstance)javaClass.newInstance();

    symTable.getSymbol( "globalSym" ).setValue( "value1" );
    assertEquals( "value1", instance.evaluate(new ExternalSymbolMapSymbolTableWrapper(symTable)) );
    // Test again to demonstrate true manhood
    symTable.getSymbol( "globalSym" ).setValue( "value2" );
    assertEquals( "value2", instance.evaluate(new ExternalSymbolMapSymbolTableWrapper(symTable)) );
  }

  public void testFunctionSymbolWithSymTable() throws Exception
  {
    if( ILanguageLevel.Util.STANDARD_GOSU() )
    {
      // global (external) symbols are deprecated in standard Gosu
      return;
    }

    StandardSymbolTable symTable = makeSymbolTableWithGlobalSymbol();
    Symbol sym = new Symbol( "globalFunc",
                             new FunctionType( "globalFunc", JavaTypes.STRING(), new IType[] {JavaTypes.STRING()} ),
                             getClass().getMethod( "globalFunc", String.class ) );
    symTable.putSymbol( sym );


    IGosuProgramParser pcp = new GosuProgramParser();
    IGosuProgram gp = pcp.parseProgramOnly(
      "var _field0 = globalSym\n" +
      "foo( _field0 )\n" +
      "function foo( s : String ) {\n" +
      "  print( s )\n" +
      "}\n" +
      "return new MyInnerClass().blah()\n" +
      "class MyInnerClass {\n" +
      "  function blah() : String {\n" +
      "    return globalFunc( _field0 )\n" +
      "  }\n" +
      "}\n", symTable, new ParserOptions() ).getProgram();

    assertTrue( gp.isValid() );

    Class<?> javaClass = gp.getBackingClass();
    assertNotNull( javaClass );
    IProgramInstance instance = (IProgramInstance)javaClass.newInstance();

    symTable.getSymbol( "globalSym" ).setValue( "value1" );
    assertEquals( "value1_you_rang?", instance.evaluate(new ExternalSymbolMapSymbolTableWrapper(symTable)) );
    // Test again to demonstrate true manhood
    symTable.getSymbol( "globalSym" ).setValue( "value2" );
    assertEquals( "value2_you_rang?", instance.evaluate(new ExternalSymbolMapSymbolTableWrapper(symTable)) );
  }

  //
  // Expression testing
  //

  public void testBooleanLiteralExpr() throws Exception
  {
    boolean ret =
      (Boolean)IGosuProgram.Runner.runProgram( "true" );
    assertEquals( true, ret );
  }

  public void testNullExpr() throws Exception
  {
    Object ret = IGosuProgram.Runner.runProgram( "null" );
    assertNull( ret );
  }

  public void testStringLiteralExpr() throws Exception
  {
    String ret =
      (String)IGosuProgram.Runner.runProgram( "\"hello\"" );
    assertEquals( "hello", ret );
  }

  public void testTypeLiteralExpr() throws Exception
  {
    IJavaType ret =
      (IJavaType)IGosuProgram.Runner.runProgram( "java.math.BigDecimal" );
    assertEquals( JavaTypes.BIG_DECIMAL(), ret );
  }

  public void testRelativeTypeLiteralExpr() throws Exception
  {
    IJavaType ret =
      (IJavaType)IGosuProgram.Runner.runProgram( "String" );
    assertEquals( JavaTypes.STRING(), ret );
  }

  public void testIntArrayExpr() throws Exception
  {
    int[] ret =
      (int[])IGosuProgram.Runner.runProgram( "new int[] {1,2,3}" );
    assertEquals( 3, ret.length );
    assertEquals( 1, ret[0] );
    assertEquals( 2, ret[1] );
    assertEquals( 3, ret[2] );
  }

  public void testIntegerArrayExpr() throws Exception
  {
    Integer[] ret =
      (Integer[])IGosuProgram.Runner.runProgram( "new java.lang.Integer[] {1,2,3}" );
    assertArrayEquals( new Integer[] {1,2,3}, ret );
  }

  public void testListExpr() throws Exception
  {
    @SuppressWarnings({"unchecked"})
    List<Integer> ret =
      (List<Integer>)IGosuProgram.Runner.runProgram( "{1,2,3}" );

    List<Integer> list = new ArrayList<Integer>();
    list.add( 1 );
    list.add( 2 );
    list.add( 3 );
    assertListEquals( list, ret );
  }

  public void testMapExpr() throws Exception
  {
    @SuppressWarnings({"unchecked"})
    Map<String,Integer> ret =
      (Map<String,Integer>)IGosuProgram.Runner.runProgram( "{\"a\" -> 1, \"b\" -> 2, \"c\" -> 3}" );
    assertEquals( 3, ret.size() );
    assertEquals( (Integer)1, ret.get( "a" ) );
    assertEquals( (Integer)2, ret.get( "b" ) );
    assertEquals( (Integer)3, ret.get( "c" ) );
  }

  public void testIntegerLiteralExpr() throws Exception
  {
    int ret =
      (Integer)IGosuProgram.Runner.runProgram( "12\n" );
    assertEquals( 12, ret );
  }

  public void testNegIntegerLiteralExpr() throws Exception
  {
    int ret =
      (Integer)IGosuProgram.Runner.runProgram( "-12" );
    assertEquals( -12, ret );
  }

  public void testFloatLiteralExpr() throws Exception
  {
    double ret =
      (Double)IGosuProgram.Runner.runProgram( ".12" );
    assertEquals( .12, ret );
  }

  public void testNegFloatLiteralExpr() throws Exception
  {
    double ret =
      (Double)IGosuProgram.Runner.runProgram( "-.12" );
    assertEquals( -.12, ret );
  }

  public void testAdditionExpr() throws Exception
  {
    int ret =
      (Integer)IGosuProgram.Runner.runProgram( "7 + 8" );
    assertEquals( 15, ret );
  }

  public void testAdditionExprWithUnary() throws Exception
  {
    int ret =
      (Integer)IGosuProgram.Runner.runProgram( "-7 + 8" );
    assertEquals( 1, ret );
  }

  public void testSubtractionExpr() throws Exception
  {
    int ret =
      (Integer)IGosuProgram.Runner.runProgram( "7 - 8" );
    assertEquals( -1, ret );
  }

  public void testSubtractionExprWithUnary() throws Exception
  {
    int ret =
      (Integer)IGosuProgram.Runner.runProgram( "-7 - 8" );
    assertEquals( -15, ret );
  }
  
  public void testMultiplicationExpr() throws Exception
  {
    int ret =
      (Integer)IGosuProgram.Runner.runProgram( "7 * 8" );
    assertEquals( 56, ret );
  }

  public void testMultiplicationExprWithUnary() throws Exception
  {
    int ret =
      (Integer)IGosuProgram.Runner.runProgram( "-7 * 8" );
    assertEquals( -56, ret );
  }

  public void testDivisionExpr() throws Exception
  {
    int ret =
      (Integer)IGosuProgram.Runner.runProgram( "20 / 10" );
    assertEquals( 2, ret );
  }

  public void testDivisionExprWithUnary() throws Exception
  {
    int ret =
      (Integer)IGosuProgram.Runner.runProgram( "-20 / 10" );
    assertEquals( -2, ret );
  }

  public void testStringConcat() throws Exception
  {
    String ret =
      (String)IGosuProgram.Runner.runProgram( "\"a\" + \"b\"" );
    assertEquals( "ab", ret );
  }

  public void testUnaryNotExpr() throws Exception
  {
    boolean ret =
      (Boolean)IGosuProgram.Runner.runProgram( "!true" );
    assertEquals( false, ret );
  }

  public void testUnaryBitNotExpr() throws Exception
  {
    int ret =
      (Integer)IGosuProgram.Runner.runProgram( "~8" );
    assertEquals( ~8, ret );
  }

  public void testTypeAsExpr() throws Exception
  {
    double ret =
      (Double)IGosuProgram.Runner.runProgram( "8 as double" );
    assertEquals( 8.0, ret );
  }

  public void testTypeOfExpr() throws Exception
  {
    IJavaType ret =
      (IJavaType)IGosuProgram.Runner.runProgram( "typeof 8" );
    assertEquals( JavaTypes.pINT(), ret );
  }

  public void testMetaTypeOfExpr() throws Exception
  {
    IMetaType ret =
      (IMetaType)IGosuProgram.Runner.runProgram( "typeof typeof 8" );
    assertEquals( TypeSystem.getFromObject( JavaTypes.pINT() ), ret );
  }

  public void testIdentifierExpr() throws Exception
  {
    StandardSymbolTable symTable = makeSymbolTableWithGlobalSymbol();

    IProgramInstance instance =
      IGosuProgram.Runner.getProgramInstance( "globalSym", symTable );

    symTable.getSymbol( "globalSym" ).setValue( "value1" );
    assertEquals( "value1", instance.evaluate(new ExternalSymbolMapSymbolTableWrapper(symTable)) );
  }

  public void testMemberAccessExpr() throws Exception
  {
    StandardSymbolTable symTable = makeSymbolTableWithGlobalSymbol();

    IProgramInstance instance =
      IGosuProgram.Runner.getProgramInstance( "globalSym.Bytes", symTable );

    symTable.getSymbol( "globalSym" ).setValue( "value1" );
    byte[] bytes = (byte[])instance.evaluate(new ExternalSymbolMapSymbolTableWrapper(symTable));
    assertEquals( 6, bytes.length );
  }

  public void testBeanMethodCallExpr() throws Exception
  {
    StandardSymbolTable symTable = makeSymbolTableWithGlobalSymbol();

    IProgramInstance instance =
      IGosuProgram.Runner.getProgramInstance( "globalSym.substring( 1 )", symTable );

    symTable.getSymbol( "globalSym" ).setValue( "value1" );
    String ret = (String)instance.evaluate(new ExternalSymbolMapSymbolTableWrapper(symTable));
    assertEquals( "alue1", ret );
  }

  public void testPrintExpr() throws Exception
  {
    StandardSymbolTable symTable = makeSymbolTableWithGlobalSymbol();

    IProgramInstance instance =
      IGosuProgram.Runner.getProgramInstance( "print( 2 )", symTable );

    Object ret = instance.evaluate(new ExternalSymbolMapSymbolTableWrapper(symTable));
    assertEquals( null, ret );
  }

  public void testBlockExpr() throws Exception
  {
    IBlock ret = (IBlock)IGosuProgram.Runner.runProgram( "\\->8" );
    assertEquals( 8, ret.invokeWithArgs() );
  }

  public void testBlockExprWithStatement() throws Exception
  {
    IBlock ret = (IBlock)IGosuProgram.Runner.runProgram( "\\->{var x = 5 return x}" );
    assertEquals( 5, ret.invokeWithArgs() );
  }

  public void testAnonymousInstanceExpr() throws Exception
  {
    if( ILanguageLevel.Util.STANDARD_GOSU() )
    {
      // global (external) symbols are deprecated in standard Gosu
      return;
    }

    StandardSymbolTable symTable = makeSymbolTableWithGlobalSymbol();

    IProgramInstance instance =
      IGosuProgram.Runner.getProgramInstance( "new java.lang.Runnable() {" +
                                              " function run() {" +
                                              "   globalSym += \"ran\"" +
                                              " }" +
                                              "}.run()", symTable );


    symTable.getSymbol( "globalSym" ).setValue( "value1" );
    assertNull( instance.evaluate(new ExternalSymbolMapSymbolTableWrapper(symTable)) );
    assertEquals( "value1ran", symTable.getSymbol( "globalSym" ).getValue() );
  }

  public void testMemberExpansionExpr() throws Exception
  {
    char[] chars = (char[])IGosuProgram.Runner.runProgram( "{\"abc\",\"def\"}*.charAt(0)" );
    assertEquals( 2, chars.length );
    assertEquals( 'a', chars[0] );
    assertEquals( 'd', chars[1] );
  }

  public void testExpectedReturnType() throws Exception
  {
    int ret =
      (Integer)IGosuProgram.Runner.runProgram( "return 5 + 2" );
    assertEquals( 7, ret );

    BigInteger biRet =
      (BigInteger)IGosuProgram.Runner.runProgram( "return 5 + 2", null, JavaTypes.BIG_INTEGER() );
    assertEquals( BigInteger.valueOf( 7 ), biRet );
  }

  // Statements
  
  public void testAssignmentStmt() throws Exception
  {
    StandardSymbolTable symTable = makeSymbolTableWithGlobalSymbol();

    IProgramInstance instance =
      IGosuProgram.Runner.getProgramInstance( "globalSym = \"whatever\"", symTable );

    symTable.getSymbol( "globalSym" ).setValue( "value1" );
    assertNull( instance.evaluate(new ExternalSymbolMapSymbolTableWrapper(symTable)) );
    assertEquals( "whatever", symTable.getSymbol( "globalSym" ).getValue() );
  }
  
  private StandardSymbolTable makeSymbolTableWithGlobalSymbol()
  {
    StandardSymbolTable symTable = new StandardSymbolTable( true );
    Symbol sym = new Symbol( "globalSym", JavaTypes.STRING(), null );
    symTable.putSymbol( sym );
    return symTable;
  }

  public static String globalFunc( String s )
  {
    return s + "_you_rang?";
  }
}