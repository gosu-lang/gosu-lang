/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuProgram;

public class EvalExpressionTest extends ByteCodeTestBase
{
  public void testReifiedTypeVarFromGenericFunction() throws Exception
  {
    Object obj = newEvalClass();
    String val = (String)invokeMethod( obj, "testReifiedTypeVarFromGenericFunction" );
    assertEquals( "hellojava.lang.StringBuilder", val );
  }

  public void testReifiedTypeVarParameterizedWithGosuClassFromGenericFunction() throws Exception
  {
    Object obj = newEvalClass();
    String val = (String)invokeMethod( obj, "testReifiedTypeVarParameterizedWithGosuClassFromGenericFunction" );
    assertEquals( "gw.internal.gosu.compiler.sample.expression.HasEvalExpression", val );
  }

  public void testReifiedTypeVarParameterizedWithNullFromGenericFunctionWithNullCapture() throws Exception
  {
    Object obj = newEvalClass();
    String val = (String)invokeMethod( obj, "testReifiedTypeVarParameterizedWithNullFromGenericFunctionWithNullCapture" );
    assertEquals( "java.lang.Objectnull", val );
  }

  public void testReifiedTypeVarParameterizedWithNullFromGenericFunction() throws Exception
  {
    Object obj = newEvalClass();
    String val = (String)invokeMethod( obj, "testReifiedTypeVarParameterizedWithNullFromGenericFunction" );
    assertEquals( "java.lang.Object", val );
  }

  public void testReifiedTypeVarsParameterizedWithGosuClassFromGenericFunction() throws Exception
  {
    Object obj = newEvalClass();
    String val = (String)invokeMethod( obj, "testReifiedTypeVarsParameterizedWithGosuClassFromGenericFunction" );
    assertEquals( "gw.internal.gosu.compiler.sample.expression.HasEvalExpressionjava.lang.Integer", val );
  }

  public void testReifiedTypeVarsParameterizedWithGosuClassFromGenericFunctionWithCapturedSymbols() throws Exception
  {
    Object obj = newEvalClass();
    String val = (String)invokeMethod( obj, "testReifiedTypeVarsParameterizedWithGosuClassFromGenericFunctionWithCapturedSymbols" );
    assertEquals( "gw.internal.gosu.compiler.sample.expression.HasEvalExpressionjava.lang.Integergw.internal.gosu.compiler.sample.expression.HasEvalExpression3", val );
  }

  public void testReifiedTypeVarFromGenericFunctionInNestedEval() throws Exception
  {
    Object obj = newEvalClass();
    String val = (String)invokeMethod( obj, "testReifiedTypeVarFromGenericFunctionInNestedEval" );
    assertEquals( "hello_nested_java.lang.String", val );
  }

  public void testNestedEvalWithCapture() throws Exception
  {
    Object obj = newEvalClass();
    int val = (Integer)invokeMethod( obj, "testNestedEvalWithCapture" );
    assertEquals( 2, val );
  }

  public void testTripleNestedEvalWithCapture() throws Exception
  {
    Object obj = newEvalClass();
    int val = (Integer)invokeMethod( obj, "testTripleNestedEvalWithCapture" );
    assertEquals( 2, val );
  }

  public void testCanAccessEnclosingField() throws Exception
  {
    Object obj = newEvalClass();
    String val = (String)invokeMethod( obj, "testCanAccessEnclosingField" );
    assertEquals( "Yay", val );
  }

  public void testCanAccessOuter() throws Exception
  {
    Object obj = newEvalClass();
    String val = (String)invokeMethod( obj, "testCanAccessThis" );
    assertEquals( "Hey", val );
  }

  public void testCanAccessEnclosingPrivateMethod() throws Exception
  {
    Object obj = newEvalClass();
    String val = (String)invokeMethod( obj, "testCanAccessEnclosingPrivateMethod" );
    assertEquals( "I'm private", val );
  }

  public void testCanAccessEnclosingPrivateMethodReturnsInt() throws Exception
  {
    Object obj = newEvalClass();
    int val = (Integer)invokeMethod( obj, "testCanAccessEnclosingPrivateMethodReturnsInt" );
    assertEquals( 7, val );
  }

  public void testCanAccessEnclosingPrivateMethodWithArgs() throws Exception
  {
    Object obj = newEvalClass();
    String val = (String)invokeMethod( obj, "testCanAccessEnclosingPrivateMethodWithArgs" );
    assertEquals( "hello", val );
  }

  public void testCanAccessEnclosingPrivateMethodFromInnerClass() throws Exception
  {
    Object obj = newEvalClass();
    String val = (String)invokeMethod( obj, "testCanAccessEnclosingPrivateMethodFromInnerClass" );
    assertEquals( "I'm private", val );
  }

  public void testCanAccessEnclosingPrivateMethodReturnsIntFromInnerClass() throws Exception
  {
    Object obj = newEvalClass();
    int val = (Integer)invokeMethod( obj, "testCanAccessEnclosingPrivateMethodReturnsIntFromInnerClass" );
    assertEquals( 7, val );
  }

  public void testCanAccessEnclosingPrivateMethodWithArgsFromInnerClass() throws Exception
  {
    Object obj = newEvalClass();
    String val = (String)invokeMethod( obj, "testCanAccessEnclosingPrivateMethodWithArgsFromInnerClass" );
    assertEquals( "hello", val );
  }

  public void testReifiedTypeVarFromInnerClass() throws Exception
  {
    Object obj = newEvalClass();
    IType val = (IType)invokeMethod( obj, "testReifiedTypeVarFromInnerClass" );
    assertEquals( "java.lang.Integer", val.getName() );
  }

  public void testReifiedTypeVarFromStaticMethod() throws Exception
  {
    Object obj = newEvalClass();
    IType val = (IType)invokeMethod( obj, "testReifiedTypeVarFromStaticMethod" );
    assertEquals( "java.lang.StringBuilder", val.getName() );
  }

  public void testReifiedTypeVarFromStaticMethodWithCapturedSymbols() throws Exception
  {
    Object obj = newEvalClass();
    String val = (String)invokeMethod( obj, "testReifiedTypeVarFromStaticMethodWithCapturedSymbols" );
    assertEquals( "java.lang.StringBuilderduuude", val );
  }

  public void testReifiedTypeVarsFromStaticMethodWithCapturedSymbols() throws Exception
  {
    Object obj = newEvalClass();
    String val = (String)invokeMethod( obj, "testReifiedTypeVarsFromStaticMethodWithCapturedSymbols" );
    assertEquals( "java.lang.StringBuilderjava.lang.Integerduuude", val );
  }

  public void testCapturedSymbolsFromStaticMethod() throws Exception
  {
    Object obj = newEvalClass();
    String val = (String)invokeMethod( obj, "testCapturedSymbolsFromStaticMethod" );
    assertEquals( "a0b1a0c2a0b1a0", val );
  }

  public void testEvalStatement() throws Exception
  {
    Object obj = newEvalClass();
    String val = (String)invokeMethod( obj, "testEvalStatement" );
    assertEquals( "Yay", val );
  }

  //
  // Programs
  //

  public void testEvalInProgram() throws Exception
  {
    int ret =
      (Integer)IGosuProgram.Runner.runProgram(
        "var x = 2\n" +
        "var y = eval( \"x = 3\" )\n" +
        "return x" );
    assertEquals( 3, ret );
  }

  public void testEvalInClassInProgram() throws Exception
  {
    int ret =
      (Integer)IGosuProgram.Runner.runProgram(
        "return new Foo().bar()\n" +
        "class Foo {\n" +
        "  function bar() : int {\n" +
        "    var x = 2\n" +
        "    var y = eval( \"x = 5\" )\n" +
        "    return x\n" +
        "  }\n" +
        "}\n" );
    assertEquals( 5, ret );
  }

  public void testEvalInClassInProgramAccessPrivateMethodFromOuterFromClassDefinedInEval() throws Exception
  {
    String ret =
      (String)IGosuProgram.Runner.runProgram(
        "return new Foo().bar()\n" +
        "function outerMethod() : String {\n" +
        "  return \"gosu\"" +
        "}\n" +
        "class Foo {\n" +
        "  function bar() : String {\n" +
        "    var x = 2\n" +
        "    return eval( " +
        "     \"return new InEval().blarf() " +
        "       class InEval { " +
        "         function blarf() : String { " +
        "           return outerMethod() + x " +
        "         } " +
        "       }\" ) as String\n" +
        "  }\n" +
        "}\n" );
    assertEquals( "gosu2", ret );
  }

  public void testMultipleInvocations() throws Exception
  {
    String ret =
      (String)IGosuProgram.Runner.runProgram(
        "var x = \"\"\n" +
        "for( c in 0..|10 ) {\n" +
        "  x += eval( \"c\" )\n" +
        "}\n" +
        "return x" );
    assertEquals( "0123456789", ret );
  }

  public void testEvalWithLeadingComment() throws Exception
  {
    String ret = (String)IGosuProgram.Runner.runProgram( "eval( \"// leading comment\\n return 'hi'\" )" );
    assertEquals( "hi", ret );
  }

  private Object newEvalClass() throws ClassNotFoundException, InstantiationException, IllegalAccessException
  {
    final String cls = "gw.internal.gosu.compiler.sample.expression.HasEvalExpression";
    Class<?> javaClass = GosuClassLoader.instance().findClass( cls );
    assertNotNull( javaClass );
    assertEquals( cls, javaClass.getName() );
    assertNotNull( javaClass.newInstance() );
    return javaClass.newInstance();
  }

  public static void printMemoryUsage() {
//    System.gc(); //enable but don't check in for more accurate memory usage estimate
    long freeMem = Runtime.getRuntime().freeMemory();
    long totalMem = Runtime.getRuntime().totalMemory();
    long usedMem = totalMem - freeMem;
    long maxMem = Runtime.getRuntime().maxMemory();

    System.out.println( "Memory usage: " +
            formatMemory(usedMem) + " used, " +
            formatMemory(freeMem) + " free, " +
            formatMemory(totalMem) + " total, " +
            formatMemory(maxMem) + " max");
  }

  /**
   * Formats the given value into a pretty string
   * @param mem Memory statistic value
   * @return a pretty string of the given value
   */
  private static final long KBYTES = 1024;
  private static final long MBYTES = KBYTES * KBYTES;
  public static String formatMemory(long mem) {
    if (mem == 0) {
      return "0";
    }
    long mbytes = (mem / MBYTES);
    long kbytes = ((mem % MBYTES) / KBYTES);
    kbytes = (kbytes * 1000) / KBYTES; // turn it into (0-999) as oppose to (0-1023)
    String desc = mbytes + "." + (kbytes < 100 ? "0" : "") + (kbytes < 10 ? "0" : "") + kbytes + " MB";
    return desc;
  }
}