/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.function.Function0;
import gw.lang.parser.EvaluationException;
import gw.lang.parser.IParseIssue;
import gw.lang.parser.StandardSymbolTable;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.expressions.IProgram;
import gw.lang.parser.resources.Res;
import gw.lang.reflect.java.JavaTypes;
import gw.test.TestClass;
import gw.util.GosuTestUtil;
import junit.framework.Assert;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author cgross
 */
@SuppressWarnings({"unchecked"})
public class BlockExpressionTest extends TestClass
{
  public void testBasicBlockInvocationWorks() throws ParseResultsException
  {
    Number val = (Number)exec( "var x = \\->10; " +
                               "return x()" );
    Assert.assertEquals( new Integer( "10" ), val);
  }

  public void testBasicBlockArgPassing() throws ParseResultsException
  {
    Number val = (Number)exec( "var x = \\y : java.lang.Double -> y + 10; " +
                               "return x(10)" );
    Assert.assertEquals( new Double( "20" ), val);
  }

  public void testInScopeClosureCapture() throws ParseResultsException
  {
    Number val = (Number)exec( "var z = 10; " +
                               "var x = \\y : java.lang.Double -> y + z; " +
                               "return x(10)" );
    Assert.assertEquals( new Double( "20" ), val);
  }

  //## todo: this script should throw a parse exception about the x in doIt() conflicting with the program's x (which will be a field in the compiled class)
  public void testDownwardScopeClosureCapture() throws ParseResultsException
  {
    Number val = (Number)exec( "function doIt(it : block():Object):Object{" +
                               "  var x = 10;" +
                               "  var y = 20;" +
                               "  return it();" +
                               "}" +
                               "var xx = 1; " +
                               "var myIt = \\-> xx; " +
                               "return doIt(myIt)" );
    Assert.assertEquals( new Integer( "1" ), val );
  }

  public void testUpwardClosureCapture() throws ParseResultsException
  {
    Number val = (Number)exec( "function makeBlock() : block(java.lang.Double):java.lang.Double " +
                               "{" +
                               "  var z = 10; " +
                               "  return \\y : java.lang.Double -> y + z; " +
                               "} " +
                               "var x = makeBlock()" +
                               "return x(10)" );
    Assert.assertEquals( new Double( "20" ), val);
  }

  public void testClosureArgPassingWorksWithMultipleArgs() throws ParseResultsException
  {
    Number val = (Number)exec( "function doIt( x : int , y : block():int ) : int\n" +
                               "{ \n" +
                               "  return  y();\n" +
                               "}\n" +
                               "\n" +
                               "return doIt( 10, \\-> 20 )" );
    Assert.assertEquals( new Integer( "20" ), val);
  }

  public void testClosureArgPassingWorksWithMultipleArgPasses() throws ParseResultsException
  {
    Number val = (Number)exec( "function doIt1( x : int , y : block():int ) : int\n" +
                               "{ \n" +
                               "  return  doIt2( y );\n" +
                               "}\n" +
                               "\n" +
                               "function doIt2( y : block():int ) : int\n" +
                               "{ \n" +
                               "  return  y();\n" +
                               "}\n" +
                               "\n" +
                               "return doIt1( 10, \\-> 20 )" );
    Assert.assertEquals( new Integer( "20" ), val);
  }

  public static class FakeProcessInfo
  {
    String _str = "foo";

    public String getStatus()
    {
      return _str;
    }
  }

  public void testLocalVariablesPreserveCorrectly1() throws ParseResultsException
  {
    Number val = (Number)exec( "function run( name : String ) : java.lang.Double\n" +
                               "{ \n" +
                               "  var id = getId( name );\n" +
                               "  waitFor(\"foo\", \\ -> {\n" +
                               "    var status = getStatus( id )\n" +
                               "    return status.Status == \"foo\"\n" +
                               "  })\n" +
                               "  return 20\n" +
                               "}\n" +
                               "\n" +
                               "function waitFor( str : String, fun():Boolean ) \n" +
                               "{ \n" +
                               "  if( fun() ) {\n" +
                               "   print(str)\n" +
                               "  }\n" +
                               "}\n" +
                               "\n" +
                               "function getId( str : String ) : String\n" +
                               "{ \n" +
                               "  return \"id\" \n" +
                               "}\n" +
                               "\n" +
                               "function getStatus( id : String ) : gw.internal.gosu.parser.BlockExpressionTest.FakeProcessInfo\n" +
                               "{ \n" +
                               "  return new gw.internal.gosu.parser.BlockExpressionTest.FakeProcessInfo()\n" +
                               "}\n" +
                               "\n" +
                               "return run( \"test\" )" );
    Assert.assertEquals( new Double( "20" ), val);
  }

  public void testLocalVariablesPreserveCorrectly2() throws ParseResultsException
  {
    Number val = (Number)exec( "function run( name : String ) : java.lang.Double\n" +
                               "{ \n" +
                               "  waitFor(\"foo\", \\ -> {\n" +
                               "    var status = getStatus( \"id\" )\n" +
                               "    return status.Status == \"foo\"\n" +
                               "  })\n" +
                               "  return 20\n" +
                               "}\n" +
                               "\n" +
                               "function waitFor( str : String, fun():Boolean ) \n" +
                               "{ \n" +
                               "  if( fun() ) {\n" +
                               "   print(str)\n" +
                               "  }\n" +
                               "}\n" +
                               "\n" +
                               "function getStatus( id : String ) : gw.internal.gosu.parser.BlockExpressionTest.FakeProcessInfo\n" +
                               "{ \n" +
                               "  return new gw.internal.gosu.parser.BlockExpressionTest.FakeProcessInfo()\n" +
                               "}\n" +
                               "\n" +
                               "return run( \"test\" )" );
    Assert.assertEquals( new Double( "20" ), val);
  }

  public void testLocalVariablesPreserveCorrectly3() throws ParseResultsException
  {
    Number val = (Number)exec( "function run( name : String ) : java.lang.Double\n" +
                               "{ \n" +
                               "  waitFor(\"foo\", \\ -> {\n" +
                               "    var status = new gw.internal.gosu.parser.BlockExpressionTest.FakeProcessInfo()\n" +
                               "    return status.Status == \"foo\"\n" +
                               "  })\n" +
                               "  return 20\n" +
                               "}\n" +
                               "\n" +
                               "function waitFor( str : String, fun():Boolean ) \n" +
                               "{ \n" +
                               "  if( fun() ) {\n" +
                               "   print(str)\n" +
                               "  }\n" +
                               "}\n" +
                               "\n" +
                               "return run( \"test\" )" );
    Assert.assertEquals( new Double( "20" ), val);
  }

  public void testLocalVariablesPreserveCorrectly4() throws ParseResultsException
  {
    Number val = (Number)exec( "function test() : java.lang.Double\n" +
                               "{ \n" +
                               "  var x = 10;\n" +
                               "  var y = \\-> {\n" +
                               "    var z : java.lang.Double\n" +
                               "    return z\n" +
                               "  }\n" +
                               "  return y();\n" +
                               "}\n" +
                               "return test()" );
    Assert.assertNull( val);
  }

  public void testScopingFollowsIntutiveLexicalConventions() throws ParseResultsException
  {
    Number val = (Number)exec( "var z = 10; " +
                               "var x = \\->{z = 20}; " +
                               "x()" +
                               "return z" );
    Assert.assertEquals( 20, val.intValue());
  }

  public void testScopingFollowsIntutiveLexicalConventions2() throws ParseResultsException
  {
    Number val = (Number)exec( "var z = 10; " +
                               "var x = \\->{return z}; " +
                               "z = 20" +
                               "return x()" );
    Assert.assertEquals( 20, val.intValue());
  }

  public void testWrappedBlocksWorkCorrectlyWithBlockSymbols() throws ParseResultsException
  {
    Number val = (Number)exec( "function fun1( x():Object ) : Object {\n" +
                               "  return fun2( \\-> x() )" +
                               "}\n" +
                               "function fun2( y():Object ) : Object {\n" +
                               "  return y()" +
                               "}\n" +
                               "return fun1(\\->20)" );
    Assert.assertEquals( 20, val.intValue());
  }

  public void testWrappedBlocksWorkCorrectlyWithNormalSymbols() throws ParseResultsException
  {
    Number val = (Number)exec( "function fun1( x : Object ) : Object {\n" +
                               "  return fun2( \\-> x )" +
                               "}\n" +
                               "function fun2( y():Object ) : Object {\n" +
                               "  return y()" +
                               "}\n" +
                               "return fun1(20)" );
    Assert.assertEquals( 20, val.intValue());
  }

  public void testDoubleClosureCaptureWorksCorrectly() throws ParseResultsException
  {
    Object val = exec( "  function test1( blk1(Object, Object):Boolean ) : Object\n" +
                       "  {\n" +
                       "      return blk1( \"b\", \"a\" )\n" +
                       "  }\n" +
                       "\n" +
                       "  function test2( blk2(Object):Object ) : Object \n" +
                       "  {\n" +
                       "    return test1( \\ p : Object, q: Object -> blk2(p) == blk2(q)  ); \n" +
                       "  }\n" +
                       "\n" +
                       "return test2( \\ elt -> elt )" );
    Assert.assertEquals( Boolean.FALSE, val );
  }

  public void testGlobalMethodsNotCaptured() throws ParseResultsException
  {
    exec( "  function test1( blk1():void ) : void\n" +
          "  {\n" +
          "      blk1()\n" +
          "  }\n" +
          "\n" +
          "test1( \\ -> print(\"test\") )" );
  }

  public void testClosureStateIsPreservedBetweenInvocations() throws ParseResultsException
  {
    Double[] val = (Double[])exec( "function seq(start : java.lang.Double) : block():java.lang.Double" +
                                   "{ " +
                                   "  var num = start - 1" +
                                   "  return \\-> " +
                                   "  {" +
                                   "     num = num + 1" +
                                   "     return num" +
                                   "  }" +
                                   "} " +
                                   "var mySeq = seq(10)" +
                                   "return new java.lang.Double[]{mySeq(), mySeq(), mySeq(), mySeq(), mySeq(), mySeq()}" );
    assertArrayEquals( new Double[]{new Double( "10" ), new Double( "11" ), new Double( "12" ),
      new Double( "13" ), new Double( "14" ), new Double( "15" )},
                       val);
  }

  public void testArgumentTypesOfBlockInferredIfPossible() throws ParseResultsException
  {
    String val = (String)exec( "function testFun( x : block(String):String ) : String {\n" +
                               "  return x( \"hello\" )\n" +
                               "}\n" +
                               "\n" +
                               "return testFun(\\ y -> y + \" world\" )");
    Assert.assertEquals( "hello world", val );
  }

  public void testExplicitArgumentTypesOKWhenBlockInferrenceIsPossible() throws ParseResultsException
  {
    String val = (String)exec( "function testFun( x : block(String):String ) : String {\n" +
                               "  return x( \"hello\" )\n" +
                               "}\n" +
                               "\n" +
                               "return testFun(\\ y : String -> y + \" world\" )");
    Assert.assertEquals( "hello world", val );
  }

  public void testBlockArgumentTypesInferredOnArgExpressionOnly() throws ParseResultsException
  {
    try
    {
      exec( "function testFun( x : block(String):String ) : String {\n" +
            "  return x( \"hello\" )\n" +
            "}\n" +
            "\n" +
            "return testFun(\\ y : String -> {  var badBlock = \\ z -> z + \"test\"; return y + \" world\" } )");
      Assert.fail();
    }
    catch( ParseResultsException e )
    {
      //pass
    }
  }

  public void testThisCapturedWithinClasses() throws ParseResultsException
  {
    Object val = exec( "var c = new gw.internal.gosu.parser.blocks.TestClass1(10);\n" +
                       "var blk = c.getXUpdater();\n" +
                       "blk(20);\n" +
                       "return c.getX()");
    Assert.assertEquals( new Integer( 20 ), val );
  }

  public void testThisCapturedWithinClassesUsingThis() throws ParseResultsException
  {
    Object val = exec( "var c = new gw.internal.gosu.parser.blocks.TestClass1(10);\n" +
                       "var blk = c.getXUpdaterUsingThis();\n" +
                       "blk(20);\n" +
                       "return c.getX()");
    Assert.assertEquals( new Integer( 20 ), val );
  }

  public void testThisCapturedWithinClassesFromSubclassUsingSuper() throws ParseResultsException
  {
    Object val = exec( "var c = new gw.internal.gosu.parser.blocks.TestClass1Subclass(10);\n" +
                       "var blk = c.getXUpdaterFromSubclass();\n" +
                       "blk(20);\n" +
                       "return c.getX()");
    Assert.assertEquals( new Integer( 20 ), val );
  }

  public void testThisCapturedWithinClasses2() throws ParseResultsException
  {
    Object val = exec( "var c = new gw.internal.gosu.parser.blocks.TestClass2();\n" +
                       "return c.doIt()");
    Assert.assertEquals( new Integer( 20 ), val );
  }

  public void testThisCapturedWithinClasses3() throws ParseResultsException
  {
    Object val = exec( "var c = new gw.internal.gosu.parser.blocks.TestClass1(10);\n" +
                       "return c.recursivelyCallUpdater()");
    Assert.assertEquals( new Integer( 20 ), val );
  }

  public void testThisCapturedWithinClasses4() throws ParseResultsException
  {
    Object val = exec( "var c = new gw.internal.gosu.parser.blocks.TestClass1(10);\n" +
                       "c.recursivelyCallUpdater();" +
                       "return c.getX()");
    Assert.assertEquals( new Integer( 10 ), val );
  }

  public void testClassCaptureWorksCorrectly() throws ParseResultsException
  {
    Object val = exec( "var c = new gw.internal.gosu.parser.blocks.TestClass3();\n" +
                       "return c.shaneTest();");
    Assert.assertEquals( "asdfasdf", val );
  }

  public void testBlockMethodOverloadingWithCompatibleBlockTypesAllowsInferrence1() throws ParseResultsException
  {
    Object val = exec("function testFun( x : block(String):String ) : String {\n" +
                      "  return x( \"hello\" )\n" +
                      "}\n" +
                      "\n" +
                      "function testFun( x : block(String):String, str : String ) : String {\n" +
                      "  return str;\n" +
                      "}\n" +
                      "\n" +
                      "return testFun(\\ y -> y.length() as String )");
    Assert.assertEquals("5", val);
  }

  public void testBlockMethodOverloadingWithCompatibleBlockTypesAllowsInferrence2() throws ParseResultsException
  {
    Object val = exec("function testFun( x : block(String):String ) : String {\n" +
                      "  return x( \"hello\" )\n" +
                      "}\n" +
                      "\n" +
                      "function testFun( x : block(String):String, str : String ) : String {\n" +
                      "  return str;\n" +
                      "}\n" +
                      "\n" +
                      "return testFun(\\ y -> y.length() as String, \"test\" )");
    Assert.assertEquals("test", val);
  }

  public void testBlockMethodOverloadingWithIncompatibleBlockTypesIsNotAllowed()
  {
    try {
      exec("function testFun( x : block(String):String ) : String {\n" +
           "  return x( \"hello\" )\n" +
           "}\n" +
           "\n" +
           "function testFun( x : block(int):String) : String {\n" +
           "  return str;\n" +
           "}\n" +
           "\n" +
           "return testFun(\\ y -> y.length() as String, \"test\" )");
      Assert.fail("Should not allow overloaded block parameters to provide type inference info");
    } catch (ParseResultsException e) {
      //pass
    }
  }

  public void testLocalVariableIdentifierExpressionsUsesSameReference() throws ParseResultsException
  {
    try
    {
      exec( "tryme();\n" +
            "function tryme()\n" +
            "{\n" +
            "  // This local variable will have its value boxed because it is referenced in the \\->local block\n" +
            "  var local = 99;\n" +
            "  // Here we want to make sure the copied symbol for 'local' uses the same boxed value\n" +
            "  if( local == 99 )\n" +
            "  {\n" +
            "    throw \"foobar\";\n" +
            "  }\n" +
            "  doblock( \\->print(local) );\n" +
            "}\n" +
            "function doblock( lambda() : void ) : void\n" +
            "{\n" +
            "  lambda();\n" +
            "}" );
      Assert.fail();
    }
    catch( EvaluationException e )
    {
      Assert.assertTrue( e.getMessage().startsWith( "foobar" ) );
    }
  }


  public void testPsychoticRecursionActuallyWorks() throws ParseResultsException
  {
    Object val = exec( "var psycho : block(int):java.lang.Double\n" +
                       "psycho = \\ x -> { \n" +
                       "  if(x == 0)\n" +
                       "  {\n" +
                       "    return 0.0\n" +
                       "  }\n" +
                       "  else\n" +
                       "  {\n" +
                       "    return 1.0 + psycho(x - 1)\n" +
                       "  }\n" +
                       "}\n" +
                       "return psycho(10)" );
    Assert.assertEquals( new Double(10), val);
  }

  public void testPsychoticRecursionWorksUpward() throws ParseResultsException
  {
    Object val = exec( "function psychoUp() : block(int):java.lang.Double\n" +
                       "{\n" +
                       "  var psycho : block(int):java.lang.Double\n" +
                       "  psycho = \\ x -> { \n" +
                       "    if(x == 0)\n" +
                       "    {\n" +
                       "      return 0.0\n" +
                       "    }\n" +
                       "    else\n" +
                       "    {\n" +
                       "      return 1.0 + psycho(x - 1)\n" +
                       "    }\n" +
                       "  }\n" +
                       "  return psycho;" +
                       "}\n" +
                       "var psychoRes = psychoUp()\n" +
                       "return psychoRes(10)" );
    Assert.assertEquals( new Double(10), val);
  }

  public void testPsychoticRecursionWorksDownward() throws ParseResultsException
  {
    Object val = exec( "function call(i : int, f(int):java.lang.Double) :int\n" +
                       "{\n" +
                       "  return f(i) as int\n" +
                       "}\n" +
                       "var psycho : block(int):java.lang.Double\n" +
                       "psycho = \\ x -> { \n" +
                       "  if(x == 0)\n" +
                       "  {\n" +
                       "    return 0.0\n" +
                       "  }\n" +
                       "  else\n" +
                       "  {\n" +
                       "    return 1.0 + psycho(x - 1)\n" +
                       "  }\n" +
                       "}\n" +
                       "" +
                       "return call(10, psycho)" );
    Assert.assertEquals( new Integer(10), val);
  }

  public void testBlockMethodOverloadingWithBlocksDispatchesCorrectly1() throws ParseResultsException
  {
    Object val = exec("function testFun( x : String ) : String {\n" +
                      "  return x\n" +
                      "}\n" +
                      "\n" +
                      "function testFun( x : block(String):String ) : String {\n" +
                      "  return x(\"abcde\")\n" +
                      "}\n" +
                      "\n" +
                      "return testFun(\\ y -> y.length() as String )");
    Assert.assertEquals("5", val);
  }

  public void testBlockMethodOverloadingWithBlocksDispatchesCorrectly2() throws ParseResultsException
  {
    Object val = exec("function testFun( x : String ) : String {\n" +
                      "  return x\n" +
                      "}\n" +
                      "\n" +
                      "function testFun( x : block(String):String ) : String {\n" +
                      "  return x(\"abcde\")\n" +
                      "}\n" +
                      "\n" +
                      "return testFun( \"5\" )");
    Assert.assertEquals("5", val);
  }

  public void testMethodInvocationWithSimpleBooleanExpressionWorks1() throws ParseResultsException
  {
    Object val = exec("function testFun( x(String):Boolean ) : String {\n" +
                      "  if( x(\"asdf\") )\n" +
                      "  {\n" +
                      "    return \"asdf\"\n" +
                      "  }\n" +
                      "  else\n" +
                      "  {\n" +
                      "    return null;\n" +
                      "  }\n" +
                      "}\n" +
                      "\n" +
                      "return testFun( \\ x -> x.length() > 2 )");
    Assert.assertEquals("asdf", val);
  }

  public void testMethodInvocationWithSimpleBooleanExpressionWorks2() throws ParseResultsException
  {
    Object val = exec("function testFun( x(String):Boolean ) : String {\n" +
                      "  if( x(\"asdf\") )\n" +
                      "  {\n" +
                      "    return \"asdf\"\n" +
                      "  }\n" +
                      "  else\n" +
                      "  {\n" +
                      "    return null;\n" +
                      "  }\n" +
                      "}\n" +
                      "\n" +
                      "return testFun( \\ x -> x.length() < 2 )");
    Assert.assertEquals(null, val);
  }

  public void testMethodInvocationWithSimpleBooleanExpressionWorksWitProps1() throws ParseResultsException
  {
    Object val = exec("function testFun( x(String):Boolean ) : String {\n" +
                      "  if( x(\"asdf\") )\n" +
                      "  {\n" +
                      "    return \"asdf\"\n" +
                      "  }\n" +
                      "  else\n" +
                      "  {\n" +
                      "    return null;\n" +
                      "  }\n" +
                      "}\n" +
                      "\n" +
                      "return testFun( \\ x -> x.Bytes.length > 2 )");
    Assert.assertEquals("asdf", val);
  }

  public void testMethodInvocationWithSimpleBooleanExpressionWorksWithProps2() throws ParseResultsException
  {
    Object val = exec("function testFun( x(String):Boolean ) : String {\n" +
                      "  if( x(\"asdf\") )\n" +
                      "  {\n" +
                      "    return \"asdf\"\n" +
                      "  }\n" +
                      "  else\n" +
                      "  {\n" +
                      "    return null;\n" +
                      "  }\n" +
                      "}\n" +
                      "\n" +
                      "return testFun( \\ x -> x.Bytes.length < 2 )");
    Assert.assertEquals(null, val);
  }

  public void testBlocksWorkInParameterizedTypes() throws ParseResultsException
  {
    Object val = exec("var x = new java.util.ArrayList<block():String>(){\\->\"Foo\"}\n" +
                      "var y = x.get(0)\n" +
                      "return y()");
    Assert.assertEquals("Foo", val);
  }

  public void testBlocksWorkInArgumentsToParameterizedMethods() throws ParseResultsException
  {
    Object val = exec("function foo<T>( bar():T ) : T {\n" +
                      "  return bar()\n" +
                      "}" +
                      "return foo(\\->\"Foo\")");
    Assert.assertEquals("Foo", val);
  }

  public void testCallingBlockWithIncompatibleTypeCausesError()
  {
    try {
      exec("var bl = \\ x : Boolean -> null;\n" +
           "bl( new Object() ) ");
      Assert.fail("should not have parsed");
    } catch (ParseResultsException e) {
      List<IParseIssue> list = e.getParseExceptions();
      Assert.assertEquals(1, list.size());
      Assert.assertEquals(Res.MSG_TYPE_MISMATCH, list.get(0).getMessageKey());
    }
  }

  public void testDoublyNestedBlockCaptureWorks1() throws ParseResultsException {
      Object val = exec("function foo(start : int) : block():block():int{\n" +
                        "  return \\-> \n{ " +
                        "    return \\-> {\n" +
                        "       start = start + 1\n" +
                        "       return start\n" +
                        "    }" +
                        "  }\n" +
                        "}\n" +
                        "var outerBlock = foo(10);\n" +
                        "var innerBlock1 = outerBlock();\n" +
                        "var innerBlock2 = outerBlock();\n" +
                        "var innerBlock3 = outerBlock();\n" +
                        "return new java.util.ArrayList<int>(){innerBlock1(), innerBlock2(), innerBlock3()}"
      );
    assertCollectionEquals((Collection<Integer>) val, Arrays.asList(11, 12, 13) );
  }

  public void testDoublyNestedBlockCaptureWorks2() throws ParseResultsException {
      Object val = exec("function foo(start : int) : block():block():int{\n" +
                        "  var sharedOuter = 0 " +
                        "  return \\-> \n{ " +
                        "    return \\-> {\n" +
                        "       start = start + 1\n" +
                        "       sharedOuter = sharedOuter + 1\n" +
                        "       return ((start + sharedOuter) as int)\n" +
                        "    }" +
                        "  }\n" +
                        "}\n" +
                        "var outerBlock = foo(10);\n" +
                        "var innerBlock1 = outerBlock();\n" +
                        "var innerBlock2 = outerBlock();\n" +
                        "var innerBlock3 = outerBlock();\n" +
                        "return new java.util.ArrayList<int>(){innerBlock1(), innerBlock2(), innerBlock3()}"
      );
    assertCollectionEquals((Collection<Integer>) val, Arrays.asList(12, 14, 16) );
  }

  public void testDoublyNestedBlockCaptureWorks3() throws ParseResultsException {
      Object val = exec("function foo(start : int) : block():block():int{\n" +
                        "  var sharedOuter = 0 " +
                        "  return \\-> \n{ " +
                        "    var sharedInner = 0 " +
                        "    return \\-> {\n" +
                        "       start = start + 1\n" +
                        "       sharedOuter = sharedOuter + 1\n" +
                        "       sharedInner = sharedInner + 1\n" +
                        "       return ((start + sharedOuter + sharedInner) as int)\n" +
                        "    }" +
                        "  }\n" +
                        "}\n" +
                        "var outerBlock = foo(10);\n" +
                        "var innerBlock1 = outerBlock();\n" +
                        "var innerBlock2 = outerBlock();\n" +
                        "var innerBlock3 = outerBlock();\n" +
                        "return new java.util.ArrayList<int>(){innerBlock1(), innerBlock2(), innerBlock3()," +
                        "                                      innerBlock1(), innerBlock2(), innerBlock3()}"
      );
    assertCollectionEquals((Collection<Integer>) val, Arrays.asList(13, 15, 17,
                                                                20, 22, 24 ) );
  }

  public void testDoublyNestedBlockCaptureWorks4() throws ParseResultsException {
      Object val = exec("function foo(start : int) : block():block():int{\n" +
                        "  var sharedOuter = 0 " +
                        "  return \\-> \n{ " +
                        "    var sharedInner = 0 " +
                        "    return \\-> {\n" +
                        "       start = start + 1\n" +
                        "       sharedOuter = sharedOuter + 1\n" +
                        "       sharedInner = sharedInner + 1\n" +
                        "       var localInner = 2\n" +
                        "       return ((start + sharedOuter + sharedInner + localInner) as int)\n" +
                        "    }" +
                        "  }\n" +
                        "}\n" +
                        "var outerBlock = foo(10);\n" +
                        "var innerBlock1 = outerBlock();\n" +
                        "var innerBlock2 = outerBlock();\n" +
                        "var innerBlock3 = outerBlock();\n" +
                        "return new java.util.ArrayList<int>(){innerBlock1(), innerBlock2(), innerBlock3()," +
                        "                                      innerBlock1(), innerBlock2(), innerBlock3()}"
      );
    assertCollectionEquals((Collection<Integer>) val, Arrays.asList(15, 17, 19,
                                                                22, 24, 26 ) );
  }

  public void testDoublyNestedBlockCaptureOfFunctionSymbolsWorks() throws ParseResultsException {
      Object val = exec("function foo( blk():int ) : block():block():int{\n" +
                        "  var sharedOuter = 0 " +
                        "  return \\-> \n{ " +
                        "    var sharedInner = 0 " +
                        "    return \\-> {\n" +
                        "       sharedOuter = sharedOuter + 1\n" +
                        "       sharedInner = sharedInner + 1\n" +
                        "       var localInner = 2\n" +
                        "       return ((blk() + sharedOuter + sharedInner + localInner) as int)\n" +
                        "    }" +
                        "  }\n" +
                        "}\n" +
                        "var localVal = 10\n" +
                        "var outerBlock = foo( \\->{ localVal = localVal + 1; return localVal })\n" +
                        "var innerBlock1 = outerBlock()\n" +
                        "var innerBlock2 = outerBlock()\n" +
                        "var innerBlock3 = outerBlock()\n" +
                        "return new java.util.ArrayList<int>(){innerBlock1(), innerBlock2(), innerBlock3()," +
                        "                                      innerBlock1(), innerBlock2(), innerBlock3(), localVal}"
      );
    assertCollectionEquals( (Collection<Integer>)val , Arrays.asList( 15, 17, 19,
                                                                  22, 24, 26, 16 ) );
  }

  public void testCapturedSymbolsAreUsableOutsideOfCaptureScope() throws ParseResultsException {
      Object val = exec(
              "function foo( b : boolean ) : boolean{" +
              "  if(b) {\n" +
              "    var x = \\-> b\n" +
              "  }" +
              "  return true" +
              "}\n" +
              "\n" +
              "return foo(true)\n"
      );
    Assert.assertEquals(Boolean.TRUE, val);

  }

  public void testCapturedSymbolsAreUsableOutsideOfCaptureScopeInConstructor() throws ParseResultsException
  {
    Object val = exec(
      "var c = new gw.internal.gosu.parser.blocks.ConstructorTest(new java.util.ArrayList(), \n" +
      "                           \\ x -> x as List\n," +
      "                           1 )\n" +
      "return c\n"
    );
    Assert.assertNotNull( val );
  }

  public void testCC35885CantPassBlockIntoSuperCallInConstructor() throws ParseResultsException
  {
    Object val = exec(
      "var c = new gw.internal.gosu.parser.blocks.ConstructorSubTest(\"foo\", \\ x -> x )\n" +
      "return c.getVal()\n"
    );
    Assert.assertEquals( "foo", val );
  }

  public void testCapturingLoopVariable() throws ParseResultsException {
    /**
     * The problem here is that the str symbol is considered "global" because we don't put it
     * on the stack, therefore we don't capture a different variable per loop.  The right answer
     * is to make all the symbols in a program live on an activation record for the program
     */
    Object val = exec(
            "function foo() : String { var result = \"\"\n" +
                    "var strs = new String[] {\"a\", \"b\", \"c\"}\n" +
                    "var blocks = new ArrayList<block():String>()\n" +
                    "for(str in strs) {\n" +
                    "  blocks.add(\\ -> str)\n" +
                    "}\n" +
                    "for(b in blocks) {\n" +
                    "  result = result + b()\n" +
                    "}\n" +
                    "return result\n" +
                    "}\n" +
                    "return foo()"
    );
    Assert.assertEquals("abc", val);
  }

  public static interface TestInterface {
    public void doIt(String msg);
  }

  public static void doIt(TestInterface i) {
    i.doIt("Test");
  }

  public void testArgInferrenceWorksWithInterfaces() throws ParseResultsException {
    Object o = exec("var s : String;\n" +
                    "gw.internal.gosu.parser.BlockExpressionTest.doIt( \\ arg -> { s = arg } )\n" +
                    "return s");
    Assert.assertEquals("Test", o);
  }

  public void testArgInferrenceWorksWithInterfaces2() throws ParseResultsException {
    Object o = exec("var s : String;\n" +
                    "function testIt( bl : gw.internal.gosu.parser.BlockExpressionTest.TestInterface){\n" +
                    "  bl.doIt(\"Test\")\n" +
                    "}\n" +
                    "testIt( \\ arg -> { s = arg } )\n" +
                    "return s");
    Assert.assertEquals("Test", o);
  }

  public void testArgInferrenceBiasesTowardBlocks() throws ParseResultsException {
    Object o = exec("var t : java.lang.Object;\n" +
                    "function testIt( bl : gw.internal.gosu.parser.BlockExpressionTest.TestInterface){\n" +
                    "  bl.doIt(\"Test\")\n" +
                    "}\n" +
                    "function testIt( bl(java.lang.Integer):void ){\n" +
                    "  bl(10)\n" +
                    "}\n" +
                    "testIt( \\ arg -> { t = statictypeof arg } )\n" +
                    "return t");
    Assert.assertEquals( JavaTypes.INTEGER(), o);
  }

  public void testVariablesAreNotSharedAcrossIterationOfLoop1() throws ParseResultsException
  {
    List<Integer> o = (List<Integer>)exec( "function test() : java.util.ArrayList<int> {" +
                     "  var funcs = new java.util.ArrayList<block():int>()\n" +
                     "  for( x in 0..|10 ) {\n" +
                     "    funcs.add( \\-> x )\n" +
                     "  }\n" +
                     "  var func0 = funcs[0]\n" +
                     "  var func1 = funcs[1]\n" +
                     "  var func2 = funcs[2]\n" +
                     "  var func3 = funcs[3]\n" +
                     "  return new java.util.ArrayList<int>() {func0(), func1(), func2(), func3()}\n" +
                     "}\n" +
                     "\n" +
                     "return test()"
    );
    assertCollectionEquals(o, Arrays.asList(0, 1, 2, 3));
  }

  public void testVariablesAreNotSharedAcrossIterationOfLoop2() throws ParseResultsException
  {
    List<Integer> o = (List<Integer>)exec( "function test() : java.util.ArrayList<int> {" +
                     "  var funcs = new java.util.ArrayList<block():int>()\n" +
                     "  for( x in 0..9 ) {\n" +
                     "    var y = x\n" +
                     "    funcs.add( \\-> y )\n" +
                     "  }\n" +
                     "  var func0 = funcs[0]\n" +
                     "  var func1 = funcs[1]\n" +
                     "  var func2 = funcs[2]\n" +
                     "  var func3 = funcs[3]\n" +
                     "  return new java.util.ArrayList<int>() {func0(), func1(), func2(), func3()}\n" +
                     "}\n" +
                     "\n" +
                     "return test()"
    );
    assertListEquals(o, Arrays.asList(0, 1, 2, 3));
  }

  public void testVariablesAreNotSharedAcrossIterationOfLoop3() throws ParseResultsException
  {
    Object o = exec( "function test() : java.util.ArrayList<int> {" +
                     "  var funcs = new java.util.ArrayList<block():int>()\n" +
                     "  for( x in 0..9 ) {\n" +
                     "    var y : int\n" +
                     "    y = x\n" +
                     "    funcs.add( \\-> y )\n" +
                     "  }\n" +
                     "  var func0 = funcs[0]\n" +
                     "  var func1 = funcs[1]\n" +
                     "  var func2 = funcs[2]\n" +
                     "  var func3 = funcs[3]\n" +
                     "  return new java.util.ArrayList<int>() {func0(), func1(), func2(), func3()}\n" +
                     "}\n" +
                     "\n" +
                     "return test()"
    );
    assertCollectionEquals((Collection) o, Arrays.asList(0, 1, 2, 3));
  }

  public void testVariablesAreNotSharedAcrossIterationOfLoop4() throws ParseResultsException
  {
    Object o = exec( "function test() : java.util.ArrayList<int> {" +
                     "  var funcs = new java.util.ArrayList<block():int>()\n" +
                     "  for( x in 0..9 ) {\n" +
                     "    var y : int\n" +
                     "    if( true ) {" +
                     "      y = x\n" +
                     "      funcs.add( \\-> y )\n" +
                     "    }" +
                     "  }\n" +
                     "  var func0 = funcs[0]\n" +
                     "  var func1 = funcs[1]\n" +
                     "  var func2 = funcs[2]\n" +
                     "  var func3 = funcs[3]\n" +
                     "  return new java.util.ArrayList<int>() {func0(), func1(), func2(), func3()}\n" +
                     "}\n" +
                     "\n" +
                     "return test()"
    );
    assertCollectionEquals((List<Integer>) o, Arrays.asList(0, 1, 2, 3));
  }

  public void testBadPathCausesCompileErrorInBlock() throws ParseResultsException
  {

    try
    {
      IProgram prog = GosuTestUtil.compileProgram( "function test( b():String ) {\n" +
                                                     "  print( b() )\n" +
                                                     "}\n" +
                                                     "test( \\-> \"\".Foo )",
                                                     new StandardSymbolTable(true) );
      Assert.fail( "Should not have compiled" );
    }
    catch( ParseResultsException e )
    {
      //pass
    }

  }

  public void testErroneousVarStatementsHaveGoodErrorMessages() throws ParseResultsException {
    try {
      GosuTestUtil.compileProgram( "var x = \\-> var y = 100", new StandardSymbolTable( true ) );
      Assert.fail( "Should not have compiled" );
    } catch( ParseResultsException e ) {
      for( IParseIssue exception : e.getParseExceptions() )
      {
        if( exception.getMessageKey().equals( Res.MSG_STATEMENTS_MUST_BE_ENCLOSED_IN_CURLIES_IN_BLOCKS ) )
        {
          return;
        }
      }
      Assert.fail( "Should have found an error message about needing crackels" );
    }
  }

  public void testErroneousIfStatementsHaveGoodErrorMessages() throws ParseResultsException {
    try {
      GosuTestUtil.compileProgram( "var x = \\-> if( true ) print(\"Hello\")", new StandardSymbolTable( true ) );
      Assert.fail( "Should not have compiled" );
    } catch( ParseResultsException e ) {
      for( IParseIssue exception : e.getParseExceptions() )
      {
        if( exception.getMessageKey().equals( Res.MSG_STATEMENTS_MUST_BE_ENCLOSED_IN_CURLIES_IN_BLOCKS ) )
        {
          return;
        }
      }
      Assert.fail( "Should have found an error message about needing crackels" );
    }
  }

  public void testErroneousSwitchStatementsHaveGoodErrorMessages() throws ParseResultsException {
    try {
      GosuTestUtil.compileProgram( "var x = \\-> switch( true ) print(\"Hello\")", new StandardSymbolTable( true ) );
      Assert.fail( "Should not have compiled" );
    } catch( ParseResultsException e ) {
      for( IParseIssue exception : e.getParseExceptions() )
      {
        if( exception.getMessageKey().equals( Res.MSG_STATEMENTS_MUST_BE_ENCLOSED_IN_CURLIES_IN_BLOCKS ) )
        {
          return;
        }
      }
      Assert.fail( "Should have found an error message about needing crackels" );
    }
  }

  public void testErroneousAssignmentStatementsHaveGoodErrorMessages() throws ParseResultsException {
    try {
      GosuTestUtil.compileProgram( "int i = 0; var x = \\-> i = 10", new StandardSymbolTable( true ) );
      Assert.fail( "Should not have compiled" );
    } catch( ParseResultsException e ) {
      for( IParseIssue exception : e.getParseExceptions() )
      {
        if( exception.getMessageKey().equals( Res.MSG_ASSIGNMENTS_MUST_BE_ENCLOSED_IN_CURLIES_IN_BLOCKS ) )
        {
          return;
        }
      }
      Assert.fail( "Should have found an error message about needing crackels" );
    }
  }

  public void testCannotDefineFunctionsOrPropertiesInBlocks() throws ParseResultsException {
    String script = "\\-> { function foo(){} }";
    GosuTestUtil.assertCausesPRE( script );
    GosuTestUtil.assertCausesPRE( "\\-> { property get foo() : String { return null } }" );
    GosuTestUtil.assertCausesPRE( "\\-> { property set foo( s : String ) {} }" );
  }

  public void testCanDefineFunctionsOrPropertiesInBlocks() throws ParseResultsException {
    Function0 o = (Function0)GosuTestUtil.eval( "\\-> new java.lang.Runnable() { function run(){} " +
                                                "                                property get Foo() : String {return null} }" );
    assertTrue( o.invoke() instanceof Runnable );
  }

  public void testSuperNotAllowedInBlocks() throws ParseResultsException
  {
    GosuTestUtil.assertCausesPRE( "class ASuperClass { \n" +
                                  "  function foo() { var x = \\-> super.toString() } \n" +
                                  "}", Res.MSG_SUPER_NOT_ACCESSIBLE_FROM_BLOCK );
  }

  public void testSuperAllowedInAnonWithinBlock() {
    Function0 o = (Function0)GosuTestUtil.eval( "\\-> new Object(){ function toString() : String { return 'asdf' + super.toString() } }" );
    assertTrue( o.invoke().toString().startsWith( "asdf" ) );
  }

  private Object exec( String s ) throws ParseResultsException
  {
    return GosuTestUtil.evalGosu( s );
  }

}

