/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.autoformatting

uses com.intellij.openapi.actionSystem.IdeActions
uses gw.plugin.ij.framework.GosuTestCase
uses gw.plugin.ij.framework.generator.ResourceFactory
uses gw.testharness.Disabled
uses gw.testharness.KnownBreak

uses java.lang.StringBuilder
uses java.util.HashMap

/**
*  ^^ denotes the cursor location where ENTER should be pressed
*  !! denotes the cursor location (on the next line) after ENTER is pressed
*  [[[]]] denotes additional text that has been auto inserted
*  ~~ does not do anything, it is used as a visual cue so that the elements line up. It is optional
*/
class AutoEditIndentTest extends GosuTestCase {
  final var keyMap = new HashMap (){"[" -> "]", "{" -> "}", "(" -> ")", "<" -> ">", "\"" -> "\"", "\'" -> "\'"}

//  function testIndentBeforeParen() {
//    test1(
//      "package pkg1\n" +
//      "class MyClass {\n" +
//      "  fu!!nction test(^^) {}\n" +
//      "}\n"
//    )
//  }

  function testIndentBeforeBrace() {
    test1(
      "package pkg1\n" +
      "class MyClass {\n" +
      "  !!public function test() ^^{\n" +
      "  }\n" +
      "}\n"
    )
  }

  function testIndentAfterElse() {
    test1(
      "package pkg1\n" +
      "class MyClass {\n" +
      "  public function test() {\n" +
      "    if (true)\n" +
      "      print(0)\n" +
      "    el!!se ^^print(1)\n" +
      "  }\n" +
      "}\n"
    )
  }

  function testIndentAfterPublicModifier() {
    test1(
      "package pkg1\n" +
      "class MyClass {\n" +
      "  !!public ^^function test() {" +
      "  }" +
      "}\n"
    )
  }

  function testIndentPublicAndStaticModifiers() {
    test1(
      "package pkg1\n" +
      "class MyClass {\n" +
      "  !!public ^^static function test() {" +
      "  }" +
      "}\n"
    )
  }

  function testIndentAfterImportStatement() {
    test1(
      "package pkg1\n" +
      "!!uses java.util.*^^\n" +
      "class MyClass {\n" +
      "}\n"
    )
  }

  function testIndentAfterIfStatement() {
    test1(
      "package pkg1\n" +
      "class MyClass {\n" +
      "  function test() {\n" +
      "    if!! (true)^^\n" +
      "  }\n" +
      "}\n"
    )
  }

  function testIndentAfterWhileStatement() {
    test1(
      "package pkg1\n" +
      "class MyClass {\n" +
      "  function test() {\n" +
      "    wh!!ile (true)^^\n" +
      "  }\n" +
      "}\n"
    )
  }

  function testIndentAfterGosuClassOpeningBrace() {
    test1(
      "package pkg1\n" +
      "cl!!ass MyClass {^^\n" +
      "}\n"
    )
  }

  function testIndentAfterGosuInterfaceOpeningBrace() {
    test1(
      "package pkg1\n" +
      "in!!terface MyInterface {^^\n" +
      "}\n"
    )
  }

  function testIndentAfterGosuEnumOpeningBrace() {
    test1(
      "package pkg1\n" +
      "en!!um MyEnum {^^\n" +
      "}\n"
    )
  }

  function testIndentAfterOpeningBraceInGosuClassExtends() {
    test1(
      "package pkg1\n" +
      "cl!!ass MyClass extends testClass{^^\n" +
      "}\n"
    )
  }

  function testIndentAfterOpeningBraceInGosuInnerClass() {
    test1(
      "package test\n" +
      "class MyClass {\n" +
      "  var _inner : Inner\n\n" +
      "  construct() {\n" +
      "      _inner = new Inner()\n" +
      "  }\n\n" +
      "  cl!!ass Inner {^^\n" +
      "    var _privateData : String\n\n" +
      "  }\n" +
      "}"
    )
  }

  function testIndentAfterOpeningBraceInFunction() {
    test1(
      "package pkg1\n" +
      "class MyClass {\n" +
      "  fu!!nction test() {^^\n"+
      "  }\n"+
      "}\n"
    )
  }

  function testIndentAfterOpeningBraceInFunctionWithParams() {
    test1(
      "package pkg1\n" +
      "class MyClass {\n" +
      "  fu!!nction foo(x: int, y: String = \"default\"){^^\n" +
      "    var myMap = y\n" +
      "  }" +
      "}\n"
    )
  }

  function testIndentOfFunctionParams() {
    test1(
      "package pkg1\n" +
      "class MyClass {\n" +
      "  function foo(!!x: int, ^^y: String = \"default\"){\n" +
      "    var myMap = y\n" +
      "  }" +
      "}\n"
    )
  }

  function testIndentOfParametersWithinParameters() {
    test1(
      "package pkg1\n" +
      "class MyClass {\n" +
      "  function foo(x: int, y: String){\n" +
      "    requ!!ests.put(POSTAL_CODE, new AutofillRequest(country, ^^\"PostalCode\",{\"PostalCode\"}))\n" +
      "  }" +
      "}\n"
    )
  }

  function testFunctionCallWithMultipleParams() {
    test1(
      "package pkg1\n" +
      "class MyClass {\n" +
      "  function foo(x:int, y:String){\n" +
      "    var myMap = int\n" +
      "    foo(!!23, ^^\"rr\")" +
      "  }\n" +
      "}\n"
    )
  }

  function testFunctionOnSameLineAsGosuClassIndentsCorrectly() {
    test1(
      "package pkg1\n" +
      "cl!!ass MyClass {^^function test() {\n"+
      "  }\n"+
      "}\n"
    )
  }

  function testIndentAfterOpeningBraceInConstr() {
    test1(
      "package pkg1\n" +
      "class MyClass {\n" +
      "  co!!nstruct() {^^\n"+
      "  }\n"+
      "}\n"
    )
  }

  function testIndentAfterOpeningBraceInConstrWithTwoParams() {
    test1(
      "package pkg1\n" +
      "class MyClass {\n" +
      "  co!!nstruct(x: int, y: String = \"default\") {^^\n"+
      "  }\n"+
      "}\n"
    )
  }

  function testIndentOfConstrWithTwoParams() {
    test1(
      "package pkg1\n" +
      "class MyClass {\n" +
      "  construct(!!x: int, ^^y: String = \"default\") {\n"+
      "  }\n"+
      "}\n"
    )
  }

  function testIndentOfConstrSuper() {
    test1(
      "package pkg1\n" +
      "class MyClass {\n" +
      "  private construct(){ \n" +
      "    supe!!r(true, true, ^^true, false ) \n" +
      "  }\n" +
      "}"
    )
  }

  function testVariableInGosuClassIndentsCorrectly() {
    test1(
      "package pkg1\n" +
      "class MyClass {\n" +
      "  !!var x = 10^^\n"+
      "  }\n"+
      "}\n"
    )
  }

  function testMethodInvocationIndentsCorrectly() {
    test1(
      "package pkg1\n" +
      "class MyClass {\n" +
      "  var !!x = gw.test^^.PermutationTester.Type.toString()\n" +
      "}"
    )
  }

  function testStringListOnOneLineInGosuClassIndentsCorrectly() {
    test1(
      "package pkg1\n" +
      "class MyClass {\n" +
      "  var !!myStrings = {\"Mr. \", ^^\"Mrs. \", \"Ms. \"}\n" +
      "}"
    )
  }

  function testIndentAfterEnumOpeningBrace() {
    test1(
      "package pkg1\n" +
      "class MyClass {\n" +
      "  en!!um MyEnum {^^\n" +
      "    First, Second \n" +
      "  }\n" +
      "}\n"
    )
  }

  function testEnumDeclOnOneLineInGosuClassIndentsCorrectly() {
    test1(
      "package pkg1\n" +
      "class MyClass {\n" +
      "  en!!um TestEnum { ^^First, Second \n" +
      "  }"+
      "}\n"
    )
  }

  function testVariableInMethodIndentsCorrectly() {
    test1(
      "package pkg1\n" +
      "class MyClass {\n" +
      "  function myMethod(){\n"+
      "    !!var x = 10^^\n"+
      "  }\n"+
      "}\n"
    )
  }

  function testIfStatementInFunctionIndentsCorrectly() {
    test1(
      "package pkg1\n" +
      "class MyClass {\n" +
      "  function test() {\n"+
      "    if!!(true){^^\n"+
      "    }\n"+
      "  }\n"+
      "}\n"
    )
  }

  function testIfStatementWithoutBracesIndentsCorrectly() {
    test1(
      "package pkg1\n" +
      "class MyClass {\n" +
      "  function test() {\n"+
      "    if!!(true)^^\n"+
      "  }\n"+
      "}\n"
    )
  }

  function testIfStatementInFunctionWithParametersIndentsCorrectly() {
    test1(
      "package pkg1\n" +
      "class MyClass {\n" +
      "  function test() {\n" +
      "  var x = true\n" +
      "  var y = false\n" +
      "    if(x!! and ^^y){\n" +
      "    }\n" +
      "  }\n" +
      "}\n"
    )
  }

  function testIfElseStatementInFunctionIndentsCorrectly() {
    test1(
      "package pkg1\n" +
      "class MyClass {\n" +
      "  function test() {\n"+
      "    if(test){^^\n"+
      "    !!}^^else {\n"+
      "    }\n"+
      "  }\n"+
      "}\n"
    )
  }

  function testIndentAfterSingleStatementWhile() {
    test1(
      "package pkg1\n" +
      "class MyClass {\n" +
      "  function test() {\n"+
      "    while(true)\n" +
      "    !!  System.out.println(\"Test\")^^\n" +
      "  }\n"+
      "}\n"
    )
  }

  function testForLoopInFunctionIndentsCorrectly() {
    test1(
      "package pkg1\n" +
      "class MyClass {\n" +
      "  function test() {\n"+
      "    fo!!r(i in 1..10){^^\n"+
      "    }\n"+
      "  }\n"+
      "}\n"
    )
  }

  function testForLoopWithoutBracesIndentsCorrectly() {
    test1(
      "package pkg1\n" +
      "class MyClass {\n" +
      "  function test() {\n"+
      "    fo!!r(i in 1..10)^^\n"+
      "  }\n"+
      "}\n"
    )
  }

  function testWhileInFunctionIndentsCorrectly() {
    test1(
      "package pkg1\n" +
      "class MyClass {\n" +
      "  function test() {\n"+
      "    wh!!ile(true){^^\n"+
      "    }\n"+
      "  }\n"+
      "}\n"
    )
  }

  function testWhileWithoutBracesIndentsCorrectly() {
    test1(
      "package pkg1\n" +
      "class MyClass {\n" +
      "  function test() {\n"+
      "    wh!!ile(true)^^\n"+
      "  }\n"+
      "}\n"
    )
  }

  function testTryInTryCatchBlockIndentsCorrectly() {
    test1(
      "package pkg1\n" +
      "class MyClass {\n" +
      "  function method() {\n" +
      "    tr!!y{^^\n" +
      "    }catch(ex){\n" +
      "      throw ex\n" +
      "    }\n" +
      "  }\n"+
      "}"
    )
  }

  function testCatchInTryCatchBlockIndentsCorrectly() {
    test1(
      "package pkg1\n" +
      "class MyClass {\n" +
      "  function method() {\n" +
      "    try{\n" +
      "    }c!!atch(ex){^^\n" +
      "      throw ex\n" +
      "    }\n" +
      "  }\n"+
      "}"
    )
  }

  function testDeepIndentWorksCorrrectly() {
    test1(
      "package pkg1\n" +
      "class MyClass {\n" +
      "  class InnerClass {\n" +
      "    function method() {\n" +
      "      try{\n" +
      "      }c!!atch(ex){^^\n" +
      "        throw ex\n" +
      "      }\n" +
      "    }\n"+
      "  }" +
      "}"
    )
  }

  function testIndentAfterAnnotationStatement() {
    test1(
      "package pkg1\n" +
      "class MyClass {\n" +
      "  !!@Deprecated(\"\")^^\n" +
      "  function method() {\n\n" +
      "  }"  +
      "}\n"
    )
  }

  function testIndentInStaticFunctionStatement() {
    test1(
      "package pkg1\n" +
      "class MyClass {\n" +
      "    st!!atic function method() {^^\n" +
      "    }\n"  +
      "}\n"
    )
  }

  function testIndentInBlock1() {
    test1(
      "package pkg1\n" +
      "\n" +
      "uses java.util.ArrayList\n" +
      "\n" +
      "class MyClass {\n" +
      "  function test() {\n" +
      "    var list = new ArrayList<String>()\n" +
      "    li!!st.each(\\x -> { ^^if(true) print(x) });\n" +
      "  }\n" +
      "}"
    )
  }

  function testIndentInBlock2() {
    test1(
      "package pkg1\n" +
      "class MyClass {\n" +
      "  function test() {\n" +
      "    x.each( \\ elt -> {\n" +
      "      if!!(true)^^print(y)})\n" +
      "  }\n"  +
      "}\n"
    )
  }

  function testIndentInBlock3() {
    test1(
      "package pkg1\n" +
      "\n" +
      "uses java.util.ArrayList\n" +
      "\n" +
      "class MyClass {\n" +
      "  function test() {\n" +
      "    var list = new ArrayList<String>()\n" +
      "    list.each(\\x -> { \n" +
      "    !!  if(true) print(x) ^^});\n" +
      "  }\n" +
      "}"
    )
  }

  function testIndentInBlock4() {
    test1(
      "package pkg1\n" +
      "class MyClass {\n" +
      "  function test() {\n" +
      "    x.each( \\ elt -> {\n" +
      "    !!  if(true) print(y)}^^)\n" +
      "  }\n"  +
      "}\n"
    )
  }

  function testAddCloseBracketForEmptyClass() {
    test1(
      "package pkg1\n" +
      "class MyClass {^^\n" +
      "  !!" +
      "}\n"
    )
  }

  function testAddCloseBracketsForClassWithVariable() {
    test1(
      "package pkg1\n" +
      "class MyClass {\n" +
      "  var !!x = 12+^^34 + 56\n" +
      "}\n"
    )
  }

  function testTextIsFormattedCorrectlyWhenEnterIsHitInStringDefinition() {
    test1(
      "package pkg1\n" +
      "class MyClass {\n" +
      "  var !!x = \"This is test string which ends here\"+^^\"and the new line begins here.\"\n" +
      "}\n"
    )
  }

  @KnownBreak("dkovalev", "", "")
  function testIndentAfterOverride() {
    test1(
      "package pkg1\n" +
      "uses com.intellij.openapi.command.WriteCommandAction\n" +
      "uses com.intellij.openapi.application.Result\n" +
      "uses com.intellij.psi.PsiFile\n" +
      "uses java.util.ArrayList\n" +
      "class MyClass {\n" +
      "  function test(){\n" +
      "    var psiFiles = new ArrayList<PsiFile>()\n" +
      "    new WriteCommandAction ( psiFiles.last().Project, {} ) {\n" +
      "      ov!!erride function run( result: Result ) {^^\n" +
      "      }\n" +
      "    }.execute()\n" +
      "  }\n" +
      "}"
    )
  }

  @KnownBreak("dkovalev", "", "")
  function testIndentInRunnable1() {
    test1(
      "package pkg1\n" +
      "uses gw.plugin.ij.quickfix.akshay.abs2\n" +
      "uses gw.plugin.ij.framework.GosuTestCase\n" +
      "uses com.intellij.openapi.application.ApplicationManager\n" +
      "uses java.lang.Runnable\n" +
      "class SampleClass extends GosuTestCase {\n" +
      "  function test(){\n" +
      "    Ap!!plicationManager.getApplication().runReadAction(^^\n" +
      "      new Runnable() {function run() {}})\n" +
      "  }\n" +
      "}"
    )
  }

  function testIndentInRunnable2() {
    test1(
      "package pkg1\n" +
      "uses gw.plugin.ij.quickfix.akshay.abs2\n" +
      "uses gw.plugin.ij.framework.GosuTestCase\n" +
      "uses com.intellij.openapi.application.ApplicationManager\n" +
      "uses java.lang.Runnable\n" +
      "class SampleClass extends GosuTestCase {\n" +
      "  function test(){\n" +
      "    Appl!!icationManager.getApplication().runReadAction(^^new Runnable() {function run() {}})\n" +
      "      \n" +
      "  }\n" +
      "}"
    )
  }

  function testIndentInRunnable3() {
    test1(
      "package pkg1\n" +
      "uses gw.plugin.ij.quickfix.akshay.abs2\n" +
      "uses gw.plugin.ij.framework.GosuTestCase\n" +
      "uses com.intellij.openapi.application.ApplicationManager\n" +
      "uses java.lang.Runnable\n" +
      "class SampleClass extends GosuTestCase {\n" +
      "  function test(){\n" +
      "    ApplicationManager.getApplication().runReadAction(\n" +
      "      ne!!w Runnable() {^^function run() {}})\n" +
      "      \n" +
      "  }\n" +
      "}"
    )
  }

  //Making an assumption here, feel free to change
  function testIndentInRunnable4() {
    test1(
      "package pkg1\n" +
      "uses gw.plugin.ij.quickfix.akshay.abs2\n" +
      "uses gw.plugin.ij.framework.GosuTestCase\n" +
      "uses com.intellij.openapi.application.ApplicationManager\n" +
      "uses java.lang.Runnable\n" +
      "class SampleClass extends GosuTestCase {\n" +
      "  function test(){\n" +
      "    ApplicationManager.getApplication().runReadAction(\n" +
      "      new Runnable() { \n" +
      "        function run() {}\n "+
      "   !!  }^^)\n" +
      "  }\n" +
      "}"
    )
  }

  @KnownBreak("dkovalev", "", "")
  function testIndentAfterAbstractClass() {
    test1(
      "package pkg1\n" +
      "uses com.intellij.openapi.command.WriteCommandAction\n" +
      "uses com.intellij.openapi.application.Result\n" +
      "uses com.intellij.psi.PsiFile\n" +
      "uses java.util.ArrayList\n" +
      "class MyClass {\n" +
      "  function test(){\n" +
      "    var psiFiles = new ArrayList<PsiFile>()\n" +
      "    ne!!w WriteCommandAction ( psiFiles.last().Project, {} ) {^^override function run( result: Result ) {}\n" +
      "    }.execute()\n" +
      "  }\n" +
      "}"
    )
  }

  function testAddCloseBracketsForMethod() {
    test2(
      "package pkg1\n" +
      "class MyClass {\n" +
      "~~~  function myMethod(){^^\n" +
      "[[[    !!\n" +
      "~~~  }\n]]]" +
      "  var x = 10 \n"+
      "}\n"
    )
  }

  function testAddCloseBracketsForForLoop() {
    test2(
      "package pkg1\n" +
      "class MyClass {\n" +
      "~~~  function myMethod(){\n" +
      "~~~    for (i in 0..10){^^\n" +
      "[[[      !!\n" +
      "~~~    }\n]]]" +
      "~~~  }\n" +
      "  var x = 10 \n"+
      "}\n"
    )
  }

  function testAddCloseBracketsForWhileLoop() {
    test2(
      "package pkg1\n" +
      "class MyClass {\n" +
      "~~~  function myMethod(){\n" +
      "~~~    while(true){^^\n" +
      "[[[      !!\n" +
      "~~~    }\n]]]" +
      "~~~  }\n" +
      "  var x = 10 \n"+
      "}\n"
    )
  }

  function testAddCloseBracketsForIfCondition() {
    test2(
      "package pkg1\n" +
      "class MyClass {\n" +
      "~~~  function myMethod(){\n" +
      "~~~    if(true){^^\n" +
      "[[[      !!\n" +
      "~~~    }\n]]]" +
      "~~~  }\n" +
      "  var x = 10 \n"+
      "}\n"
    )
  }

  function testAddCloseBracketsForConstruct() {
    test2(
      "package pkg1\n" +
      "class MyClass {\n" +
      "~~~  construct() {^^\n" +
      "[[[    !!\n" +
      "~~~  }\n]]]" +
      "  var x = 10 \n"+
      "}\n"
    )
  }

  function testAddCloseBracketsForTryCatchBlock() {
    test2(
      "package pkg1\n" +
      "class MyClass {\n" +
      "~~~  function myMethod(){\n" +
      "~~~    try{\n" +
      "~~~    } catch(ex){^^\n" +
      "[[[      !!\n" +
      "~~~    }\n]]]" +
      "~~~  }\n" +
      "  var x = 10 \n"+
      "}\n"
    )
  }

  function testOpenSquareBracketIsAutoClosed() {
    test3( "[",
      "package pkg1\n" +
      "class MyClass {\n" +
      "  function myMethod(){\n" +
      "    var x : String^^\n"+
      "  }\n"+
      "  var x = 10 \n"+
      "}\n"
    )
  }

  function testOpenCurlyBracketsIsAutoClosed() {
    test3( "{",
      "package pkg1\n" +
      "class MyClass {\n" +
      "  construct()^^\n"+
      "  var x = 10 \n"+
      "}\n"
    )
  }

  function testClosedCurlyBracketsIsNotClosedASecondTime() {
    test3( "{",
      "package pkg1\n" +
      "class MyClass {\n" +
      "  construct()^^}\n"+
      "  var x = 10 \n"+
      "}\n"
    )
  }

  function testOpenRoundBracketsIsAutoClosed() {
    test3( "(",
      "package pkg1\n" +
      "class MyClass {\n" +
      "  function myMethod(){\n" +
      "    var x = new Object^^\n" +
      "  }\n"+
      "  var x = 10 \n"+
      "}\n"
    )
  }

  function testOpenSingleQuotesIsAutoClosed() {
    test3( "\'",
      "package pkg1\n" +
      "class MyClass {\n" +
      "  function myMethod(){\n" +
      "    var x = ^^\n" +
      "  }\n"+
      "  var x = 10 \n"+
      "}\n"
    )
  }

  function testOpenDoubleQuotesIsAutoClosed() {
    test3( "\"",
      "package pkg1\n" +
      "class MyClass {\n" +
      "  function myMethod(){\n" +
      "    var x = ^^\n" +
      "  }\n"+
      "  var x = 10 \n"+
      "}\n"
    )
  }

  function testClosedSquareBracketIsNotClosedASecondTime() {
    test3( "[",
      "package pkg1\n" +
      "class MyClass {\n" +
      "  function myMethod(){\n" +
      "    var x : String^^]\n"+
      "  }\n"+
      "  var x = 10 \n"+
      "}\n"
    )
  }

  function testClosedRoundBracketsIsNotClosedASecondTime() {
    test3( "(",
      "package pkg1\n" +
      "class MyClass {\n" +
      "  function myMethod(){\n" +
      "    var x = new Object^^)\n" +
      "  }\n"+
      "  var x = 10 \n"+
      "}\n"
    )
  }

  //========JavaDoc

  @Disabled("dkovalev", "hard to implement")
  function testAddSimpleJavaDocForClass() {
    test2(
      "package pkg1\n\n" +
      "~~~/**^^\n" +
      "[[[!! * \n" +
      "~~~ */\n]]]" +
      "class MyClass {\n" +
      "}\n"
    )
  }

  function testAddMoreJavaDocLinesForClass() {
    test2(
      "package pkg1\n\n" +
      "~~~/**^^\n" +
      "[[[ * !!\n]]]" +
      "~~~ */\n" +
      "class MyClass {\n" +
      "}\n"
    )
  }

  @Disabled("dkovalev", "hard to implement")
  function testAddSimpleJavaDocForMethod() {
    test2(
      "package pkg1\n\n" +

      "class MyClass {\n" +
      "~~~  /**^^\n" +
      "[[[  !! * \n" +
      "~~~   */\n]]]" +
      "  function bar() {}\n" +
      "}\n"
    )
  }

  function testAddMoreJavaDocLinesForMethod() {
    test2(
      "package pkg1\n\n" +

      "class MyClass {\n" +
      "~~~  /**^^\n" +
      "[[[   * !!\n]]]" +
      "~~~   */\n" +
      "  function bar() {}\n" +
      "}\n"
    )
  }

  @Disabled("dkovalev", "hard to implement")
  function testAddJavaDocForMethodWithParamTag() {
    test2(
      "package pkg1\n\n" +

      "class MyClass {\n" +
      "~~~/**^^\n" +
      "[[[ * !!\n" +
      "~~~ * @param i\n" +
      "~~~ */\n]]]" +
      "  function bar(i: int) {}\n" +
      "}\n"
    )
  }

  @Disabled("dkovalev", "hard to implement")
  function testAddJavaDocForMethodWithReturnTag() {
    test2(
      "package pkg1\n\n" +

      "class MyClass {\n" +
      "~~~/**^^\n" +
      "[[[ * !!\n" +
      "~~~ * @return\n" +
      "~~~ */\n]]]" +
      "  function bar() : String {return \"\"}\n" +
      "}\n"
    )
  }

  @Disabled("dkovalev", "hard to implement")
  function testAddJavaDocForMethodWithParamAndReturnTag() {
    test2(
      "package pkg1\n\n" +

      "class MyClass {\n" +
      "~~~/**^^\n" +
      "[[[ * !!\n" +
      "~~~ * @param i\n" +
      "~~~ * @return\n" +
      "~~~ */\n]]]" +
      "  function bar(i: int) : String {return \"\"}\n" +
      "}\n"
    )
  }

  //========================heplers================

  //For Auto Indent
  function test1(text: String ) {
    var markers = getMarkers(ResourceFactory.createFile(this, text))
    var caret1 = markers.getCaret(CARET1)
    var caret2 = markers.getPostCaret(CARET4)

    var expectedText = new StringBuilder(caret1.Parent.TextWithDeltas)
    var eolIndex = expectedText.indexOf('\n', caret1.offset)
    var bolIndex = expectedText.lastIndexOf('\n', caret2.offset) + 1
    var textToMove = expectedText.substring(caret1.offset, eolIndex)
    expectedText.replace(caret1.offset, eolIndex, "\n" + spaces(caret2.offset - bolIndex) + textToMove)

    caret1.Editor.CaretModel.moveToOffset(caret1.offset);
    runAction(IdeActions.ACTION_EDITOR_ENTER, caret1.Editor)

    var found = caret1.Editor.Document.Text
    assertEquals("Texts do not match, auto indent failed", expectedText.toString(), found)

    var actualOffset = caret1.Editor.CaretModel.Offset
    var expectedOffset = caret1.offset +  1 + caret2.offset - bolIndex
    assertEquals("Caret is in the wrong place", expectedOffset, actualOffset)
  }

  //For Auto Edit
  function test2(text: String ) {
    var markers = getMarkers(ResourceFactory.createFile(this, text))
    var caret1 = markers.getCaret(CARET1)
    var caret2 = markers.getPostCaret(CARET4)

    caret1.Editor.CaretModel.moveToOffset(caret1.offset);
    runAction( IdeActions.ACTION_EDITOR_ENTER, caret1.Editor)

    var found = caret1.Editor.Document.Text
    assertEquals ("Texts do not match, auto edit failed", caret2.Parent.TextWithDeltas, found)

    var actualOffset = caret1.Editor.CaretModel.Offset
    var expectedOffset = caret2.offset
    assertEquals("Caret is in the wrong place", expectedOffset, actualOffset)
  }

  //For Quotes and Parenthesis
  function test3(key: String, text: String ) {
    var markers = getMarkers(ResourceFactory.createFile(this, text))
    var caret = markers.getCaret(CARET1)

    var expectedText = new StringBuilder(caret.Parent.TextWithDeltas)
    var eolIndex = expectedText.indexOf('\n', caret.offset)
    if(!(expectedText[caret.offset+1].equals(keyMap.get(key))))
      expectedText.replace(caret.offset, eolIndex, key + keyMap.get(key) )

    caret.Editor.CaretModel.moveToOffset(caret.offset)
    type(key, caret.Editor)

    var foundText = caret.Editor.Document.Text
    assertEquals ("Texts do not match, auto edit failed", expectedText.toString(), foundText)

    var actualOffset = caret.Editor.CaretModel.Offset
    var expectedOffset = caret.offset + 1
    assertEquals("Caret is in the wrong place", expectedOffset, actualOffset)
  }

  function spaces(n: int): String {
    var s = ""
    for (i in 0..|n) {
      s += " "
    }
    return s
  }
}