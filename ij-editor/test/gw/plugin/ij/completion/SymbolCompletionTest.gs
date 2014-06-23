/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion

uses gw.testharness.KnownBreak

class SymbolCompletionTest extends AbstractCodeCompletionTest {

  //==============================Locals

  //method -- local vars
  function testLocalVarDeclInMethodIsShown() {
    test(
      "package pkg\n" +
      "class GosuClass {\n" +
      "  function testFunction() {\n" +
      "    var local1 = 10 \n" +
      "    var local2 = \"10\" \n" +
      "    l^^ \n" +
      "  }" +
      "}"
      ,{
      "local1: int", "local2: String"
    }
    )
  }

  //property getter  -- local vars
  function testLocalVarDeclInPropertyGetterIsShown() {
    test(
      "package pkg\n" +
      "class GosuClass {\n" +
      "  property get Prop() : int{\n" +
      "    var local1 = 10 \n" +
      "    var local2 = \"10\" \n" +
      "    return l^^\n" +
      "  }" +
      "}"
      ,{
      "local1: int"
    }
    )
  }

  //property getter  -- local vars
  @KnownBreak("Need to implement context sensitive completion", "cgross", "PL-18923")
  function testLocalVarIsntShownIfTypeDoesntMatch() {
    testNoItems({
      "package pkg\n" +
      "class GosuClass {\n" +
      "  property get Prop() : int{\n" +
      "    var local1 = 10 \n" +
      "    var local2 = \"10\" \n" +
      "    return l^^\n" +
      "  }" +
      "}"
    }
      ,{
      "local2: String"
    }
    )
  }

  //property setter  -- local vars
  function testLocalVarDeclInPropertySetterIsShown() {
    test(
      "package pkg\n" +
      "class GosuClass {\n" +
      "  var _x : int\n" +
      "  property set Prop( i : int ) {\n" +
      "    var local1 = 10 \n" +
      "    var local2 = \"10\" \n" +
      "    l^^ \n" +
      "  }" +
      "}"
      ,{
      "local1: int", "local2: String"
    }
    )
  }

  //block  -- local vars
  function testLocalVarDeclInBlockIsShown() {
    test(
      "package pkg\n" +
      "class GosuClass {\n" +
      "  function method() {\n" +
      "    var x : List<String>\n" +
      "    x.each(\\ b -> {var local1 = 10; var local2 = \"qa\"; print(b + l^^)})\n" +
      "  }" +
      "}"
      ,{
      "local1: int", "local2: String"
    }
    )
  }

  //constructors  -- local vars
  function testLocalVarDeclInConstructIsShown() {
    test(
      "package pkg\n" +
      "class GosuClass {\n" +
      "  construct() {\n" +
      "    var local1 : int\n" +
      "    var local2 : String\n" +
      "    l^^\n" +
      "  }" +
      "}"
      ,{
      "local1: int", "local2: String"
    }
    )
  }

  //var in for -- for loop
  function testForLoopVarsDeclIsShown() {
    test(
      "package pkg\n" +
      "class GosuClass {\n" +
      "  function method() {\n" +
      "    for (lvar in 1..10){\n" +
      "      print(l^^)\n" +
      "    }\n" +
      "  }" +
      "}"
      ,{
      "lvar: int"
    }
    )
  }

  //var in index -- for loop
  function testForLoopIndexVarsDeclIsShown() {
    test(
      "package pkg\n" +
      "class GosuClass {\n" +
      "  function method() {\n" +
      "    for (s in \"a test string\" index ix){\n" +
      "      print(i^^)\n" +
      "    }\n" +
      "  }" +
      "}"
      ,{
      "ix: int"
    }
    )
  }

  //catch clause
  function testCatchVarsDeclIsShown() {
    test(
      "package pkg\n" +
      "class GosuClass {\n" +
      "  function method() {\n" +
      "    try{\n" +
      "    }catch(ex : java.lang.NullPointerException){\n" +
      "      throw e^^\n" +
      "    }\n" +
      "  }" +
      "}"
      ,{
      "ex: NullPointerException"
    }
    )
  }

  //using statement
  function testUsingClauseArgumentListDeclIsShown() {
    test(
      "package some.pkg\n" +
      "uses java.io.FileWriter\n" +
      "uses java.io.FileReader\n" +
      "class GosuClass {\n" +
      "  function method() {\n" +
      "    using( var ##reader = new FileReader ( \"c:\\temp\\usingfun.txt\" ),\n" +
      "           var writer = new FileWriter ( \"c:\\temp\\usingfun2.txt\" ) ){\n" +
      "      var char = r^^\n" +
      "    }\n" +
      "  }\n" +
      "}"
      ,{
      "reader: FileReader"
    }
    )
  }

  //method -- Paramters
  function testParamVarsDeclInMethodIsShown() {
    test(
      "package pkg\n" +
      "class GosuClass {\n" +
      "  function method(arg1 : int, arg2 : String) : int {\n" +
      "    return a^^\n" +
      "  }\n" +
      "}"
      ,{
       "arg1: int", "arg2: String"
    }
    )
  }

  //property setter  -- Paramters
  function testParamVarDeclInPropertySetterIsShown() {
    test(
      "package pkg\n" +
      "class GosuClass {\n" +
      "  var _x : int\n" +
      "  property set Prop( myVar : int ) {\n" +
      "    _x = m^^\n" +
      "  }\n" +
      "}"
      ,{
      "myVar: int"
    }
    )
  }

  //Blocks -- Paramters
  function testParamVarsDeclInBlockIsShown() {
    test(
      "package pkg\n" +
      "class GosuClass {\n" +
      "  var adder = \\ addx : Number, addy : Number -> { return a^^ }" +
      "}"
      ,{
      "addx: Double", "addy: Double"
    }
    )
  }

  function testGenericTypsParamVarsDeclInBlockIsShown() {
    test(
      "package pkg\n" +
      "class GosuClass {\n" +
      "  function method() {\n" +
      "    var pods = {\"Web\", \"Studio\", \"BAT\"}\n" +
      "    pods.map(\\ pod -> p^^)"  +
      "  }\n" +
      "}"
      ,{
      "pod: String"
    }
    )
  }

  function testParamVarsDeclInBlockWithImplicitCoercionReturnTypeIsShown () {
    test(
      "package pkg\n" +
      "class GosuClass {\n" +
      "  function method() {\n" +
      "    var myCache = new gw.util.concurrent.Cache<String, String>( \"My Uppercase Cache\", 100, \\ theStr -> t^^ )"  +
      "  }\n" +
      "}"
      ,{
       "theStr: String"
    }
    )
  }

  //constructors  -- Paramters
  function testParamVarDeclInConstructorIsShown() {
    test(
      "package pkg\n" +
      "class GosuClass {\n" +
      "  var _x : int\n" +
      "  construct(local1 : int, local2 : String) {\n" +
      "    _x = ^^\n" +
      "  }\n" +
      "}"
      ,{
      "local1: int", "local2: String"
    }
    )
  }

  //==============================methods

  //instance method
  function testFunctionDeclIsShown() {
    test(
      "package pkg\n" +
      "class GosuClass {\n" +
      "  function method1() : int {\n" +
      "    return 0\n" +
      "  }\n" +
      "  function method2() : String {\n" +
      "    return \"\"\n" +
      "  }\n" +
      "  function test() : int {\n" +
      "    return m^^\n" +
      "  }\n" +
      "}"
      ,{
      "method1(): int"
    }
    )
  }

  @KnownBreak("Need to implement context sensitive completion", "cgross", "PL-18923")
  function testFunctionDeclIsntShown() {
    testNoItems( {
      "package pkg\n" +
      "class GosuClass {\n" +
      "  function method1() : int {\n" +
      "    return 0\n" +
      "  }\n" +
      "  function method2() : String {\n" +
      "    return \"\"\n" +
      "  }\n" +
      "  function test() : int {\n" +
      "    return m^^\n" +
      "  }\n" +
      "}"}
      ,{
      "method2(): String"
    }
    )
  }

  //Static generic method
  function testFunctionDeclWithGenericDefaultTypeIsShown() {
    test(
      "package pkg\n" +
      "class GosuClass {\n" +
      "  static function  fromArrayToCollection<T>( a : T[],  c : java.util.Collection<T>) {\n" +
      "    for ( o in a) {\n" +
      "        c.add(o)\n" +
      "    }\n" +
      "  }\n" +
      "  function bar() {\n" +
      "    f^^\n" +
      "  }\n" +
      "}"
      ,{
      "fromArrayToCollection(a: T[], c: Collection<T>): void"
    }
    )
  }

  function testDefaultParamsAreShown(){
    test({
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function bar(myInt : int, myString : String, myList : List<String>, myBlock : block() : String) {\n" +
      "  }\n" +
      "  function foo() {\n" +
      "    bar(:^^)\n" +
      "  }\n" +
      "}"}
    ,
    {
      "myInt: int", "myString: String", "myList: List<String>", "myBlock: block():String"
    }
    )
  }

  //constructor with default params
  function testDefaultParamsDeclInCtorAreShown() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var _x : int\n" +
      "  var _y : String\n" +
      "  var _z : String\n" +
      "  construct(myInt: int, myString: String = \"default\", myList : List<String> = {\"123\", \"234\"}) {\n" +
      "    _x = x\n" +
      "    _y = y\n" +
      "    _z = z\n" +
      "  }\n" +
      "  function hi() {\n" +
      "    var o = new GosuClass(:my^^)\n" +
      "  }\n" +
      "}"
      ,
    {
      "myInt: int", "myString: String", "myList: List<String>"
    }
    )
  }

  //==============================fields

  //delegate
  function testDelegateIsShown() {
    test (
      "package some.pkg\n" +
      "class GosuClass<E> implements java.util.List<E> {\n" +
      "  delegate _map represents java.util.List<E>\n" +
      "  function addSpecial( e : E ) {\n" +
      "    _m^^\n" +
      "  }\n" +
      "}", {
      "_map: List<E>"
    }
    )
  }

  //fields with property
  function testPropFieldIsShown() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var _propField : String as PropField\n" +
      "  construct(s : String) {\n" +
      "    P^^\n" +
      "  }\n" +
      "}", {
      "PropField: String"
    }
    )
  }

  //class field
  function testClassFieldIsShown() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var _propField : String\n" +
      "  construct(s : String) {\n" +
      "    _p^^\n" +
      "  }\n" +
      "}",{
        "_propField: String"
    }
    )
  }

  //enum constants
  function testEnumIsShown() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var y: TestEnum = F^^ \n" +
      "  enum TestEnum { \n" +
      "    First, Second \n" +
      "  }\n" +
      "}",{
       "First: " //":" is caused by the test framework since Enum constant does not provide type in the completion list
    }
    )
  }

  //property getter
  function testPropertyGetterIsShown() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  property get Prop1(): int{\n" +
      "    return 10\n" +
      "  }\n" +
      "  property get Prop2(): String{\n" +
      "    return \"\"\n" +
      "  }\n" +
      "  function method(): int{\n" +
      "    return P^^\n" +
      "  }\n" +
      "}",{
      "Prop1: int"
    }
    )
  }

  //property setter
  function testPropertySetterIsShown() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var _x : int\n" +
      "  property set Prop( i : int ) {\n" +
      "    _x = i\n" +
      "  }\n" +
      "  function method() {\n" +
      "    P^^\n" +
      "  }\n" +
      "}",{
      "Prop: int"
    }
    )
  }

  //==============================literals

  //string template
  @KnownBreak("PL-18696", "", "cgross")
  function testPropFieldIsShownInStringTemplate() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var _pod : String as POD\n" +
      "  function foo() {\n" +
      "    print(\"\${P^^}\")\n" +
      "  }\n" +
      "}",{
      "POD : String"
    }
    )
  }

  @KnownBreak("PL-18696", "", "cgross")
  function testClassFieldIsShownInStringTemplate() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var _pod : String as POD\n" +
      "  function foo() {\n" +
      "    print(\"\${_p^^}\")\n" +
      "  }\n" +
      "}",{
      "_pod: String"
    }
    )
  }

  @KnownBreak("PL-18696", "", "cgross")
  function testPropertyGetterIsShownInStringTemplate() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  property get Prop() : String {\n" +
      "    return \"\"\n" +
      "  }\n" +
      "  function foo() {\n" +
      "    print(\"\${P^^}\")\n" +
      "  }\n" +
      "}",{
      "Prop: String"
    }
    )
  }

  @KnownBreak("PL-18696", "", "cgross")
  function testFunctionDeclIsShownInStringTemplate() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method() : String{\n" +
      "    return \"\"\n" +
      "  }\n" +
      "  function foo() {\n" +
      "    print(\"\${m^^}\")\n" +
      "  }\n" +
      "}",{
      "method(): String"
    }
    )
  }

  @KnownBreak("PL-18696", "", "cgross")
  function testPropFieldIsShownInAlternateTemplateExpression () {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var _pod : String as POD\n" +
      "  function foo() {\n" +
      "    print(\"\<%=P^^%>\")\n" +
      "  }\n" +
      "}",{
      "POD: String"
    }
    )
  }


  @KnownBreak("PL-18696", "", "cgross")
  function testClassFieldIsShownInAlternateTemplateExpression(){
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var _pod : String as POD\n" +
      "  function foo() {\n" +
      "    print(\"\<%=_p^^%>\")\n" +
      "  }\n" +
      "}",{
      "_pod: String"
    }
    )
  }

  @KnownBreak("PL-18696", "", "cgross")
  function testPropertyGetterIsShownInAlternateTemplateExpression() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  property get Prop() : String {\n" +
      "    return \"\"\n" +
      "  }\n" +
      "  function foo() {\n" +
      "    print(\"\<%=P^^%>\")\n" +
      "  }\n" +
      "}",{
      "Prop: String"
    }
    )
  }

  @KnownBreak("PL-18696", "", "cgross")
  function testFunctionIsShownInAlternateTemplateExpression() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method() : String{\n" +
      "    return \"\"\n" +
      "  }\n" +
      "  function foo() {\n" +
      "    print(\"\<%=m^^%>\")\n" +
      "  }\n" +
      "}",{
      "method(): String"
    }
    )
  }

  //====program
  //====template

  @KnownBreak("PL-18696", "", "cgross")
  function testTemplateParamIsShownInAlternateTemplateExpression() {
    test(
      "//TEMPLATE, package some/pkg\n" +
      "\<%@ params(_city : String, _state : String, _today : java.util.Date) %>\n" +
      "City : \${_c^^}\n"
      ,{
       "_city: String"
    }
    )
  }

  @KnownBreak("PL-18696", "", "cgross")
  function testTemplateParamIsShownInAlternateTemplateExpression2() {
    test(
      "//TEMPLATE, package some/pkg\n" +
      "\<%@ params(_city : String, _state : String, _today : java.util.Date) %>\n" +
      "Generated on \<%=_t^^%>"
      ,{
       "_today: String"
    }
    )
  }

  @KnownBreak("PL-18696", "", "cgross")
  function testExtendsClassFunctionIsShownInAlternateTemplateExpression() {
    test({
      "package some.pkg\n" +
      "class SuperClass {\n" +
      "  static function staticMethod(x : String) : String {\n" +
      "    return \"static function with arg \" + x\n" +
      "  }\n" +
      "}",
      "//TEMPLATE, package some/pkg\n" +
      "\<%@ extends some.pkg.SuperClass %>\<%=s^^%>" }
    ,{
       "staticMethod(x : String): String"
    }
    )
  }

  //====enhancement

  function testLocalMethodIsShownInEnhancement() {
    test({
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function foo() {}\n" +
      "}"
    ,
      "package some.pkg\n" +
      "enhancement GosuClassEnhencement : GosuClass {\n" +
      "  function bar() : String {\n" +
      "    b^^" +
      "  }\n" +
      "}"}
      ,{
      "bar(): String"
    }
    )
  }

  function testMethodsInEnhancedTypeIsntShownInEnhancement() {
    testNoItems({
      "package some.pkg\n" +
      "class GosuClass {\n" +
      " function foo() {}\n" +
      "}"
    ,
      "package some.pkg\n" +
      "enhancement GosuClassEnhencement : GosuClass {\n" +
      "  function bar() : String {\n" +
      "    f^^" +
      "  }\n" +
      "}"}
      ,{
      "foo(): void"
    }
    )
  }

  //====inner class

  function testMethodIsShownInInnerClass() {
    test({
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function something() : String {\n" +
      "    return \"something\"\n" +
      "  }\n" +
      "  class Inner {\n" +
      "    function getSomething() : String {\n" +
      "      return s^^\n" +
      "    }\n" +
      "  }"+
      "}"}
      ,{
      "something(): String"
    }
    )
  }

  function testBlockIsShown() {
    test(
      "package pkg\n" +
      "class GosuClass { \n" +
      "  function  foo() {\n" +
      "    var adder1 = \\ x : Number, y : Number -> {  return x + y }\n" +
      "    var adder2 = \\ x : Number, y : Number -> {  return x + y }\n" +
      "    adde^^ \n" +
      "  }\n" +
      "}"
    , {
      "adder1: block(x:Double, y:Double):Double",
      "adder2: block(x:Double, y:Double):Double"
    }
    )
  }

  function testMethodsInSuperClassIsShown() {
    test({
      "package pkg\n" +
      "class SupClass {\n" +
      "  function bar(i : int, s : String, l : List<String>) {}\n" +
      "}\n"
    ,
      "package pkg\n" +
      "class GosuClass extends SupClass {\n" +
      "  function bar1(i : int, s : String, l : List<String>) {}\n" +
      "  function bar2() {\n" +
      "    ba^^\n" +
      "  }\n" +
      "}\n"
    }
    , {
      "bar(i: int, s: String, l: List<String>): void"
    }
    )
  }

  function testMethodsInJSuperClassIsShown() {
    test({
      "//JAVA \n" +
      "package pkg;\n" +
      "public class SupClass {\n" +
      "  public void bar(int i, String s, java.util.List<String> l) {}\n" +
      "}\n"
    ,
      "package pkg\n" +
      "class GosuClass extends SupClass{\n" +
      "  function bar1(i : int, s : String, l : List<String>) {}\n" +
      "  function bar2() :  {\n" +
      "    ba^^\n" +
      "  }\n" +
      "}\n"
    }
    , {
      "bar(p0: int, p1: String, p2: List<String>): void"
    }
    )
  }

  function testPrintIsShown() {
    test(
      "package pkg\n" +
      "class GosuClass { \n" +
      "  function test() {\n" +
      "    p^^\n" +
      "  }\n" +
      "}\n"
    , {
      "print(): void"
    }
    )
  }

  //negative
  @KnownBreak("Need to implement context sensitive completion", "cgross", "PL-17806")
  function testItemsThatArentRelatedArentShown() {
    testNoItems({
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var i : int as Prop\n" +
      "  function bar() {\n" +
      "    var str : String\n" +
      "    var displayStr = str ?: ^^\n" +
      "  }" +
      "}\n"}
    ,{
      "Prop: int", "bar(): void", "while", "if", "for"
    }
    )
  }

  function testCanntInvokeOnColonInLineComment() {
    testCanntInvoke(
      "package pkg\n" +
      "class Foo {\n" +
      "  // this is a comment:^^\n" +
      "  function bar() {\n" +
      "  }\n" +
      "}\n")
  }

  function testCanntInvokeOnColonInBlockComment() {
    testCanntInvoke(
      "package pkg\n" +
      "class Foo {\n" +
      "/*this is\n" +
      "a comment for :^^*/\n" +
      "  function bar() {\n" +
      "  }\n" +
      "}\n")
  }

  function testCanntInvokeOnColonInJavaDoc() {
    testCanntInvoke(
      "package pkg\n" +
      "class Foo {\n" +
      "/**\n" +
      " * This is a java doc:^^\n" +
      " */" +
      "  function bar() {\n" +
      "  }\n" +
      "}\n")
  }

  function testCanntInvokeOnColonInStringLiteral() {
    testCanntInvoke(
      "package pkg\n" +
      "class Foo {\n" +
      "  function bar() {\n" +
      "    var x = \"foo:^^\"\n" +
      "  }\n" +
      "}\n")
  }

  function testCanntInvokeOnColonAtBeginningOfLine() {
    testCanntInvoke(
      "package pkg\n" +
      "class Foo {\n" +
      "  function bar() {\n" +
      "    :^^\n" +
      "  }\n" +
      "}\n")
  }

  function testCanntInvokeOnColonInMiddleOfNowhere() {
    testNoItems(
        {"package pkg\n" +
      "class Foo {\n" +
      "    :^^\n" +
      "  function bar() {\n" +
      "  }\n" +
      "}\n"},
            {"bar() : void"})
  }

  function testCanntInvokeOnColonAtBeginningOfStringTemplate() {
    testCanntInvoke(
      "package pkg\n" +
      "class Foo {\n" +
      "  function bar() {\n" +
      "    print(\"\${:^^}\")\n" +
      "  }\n" +
      "}\n")
  }

  function testCanntInvokeOnColonAtBeginningOfAlternateTemplateExpression() {
    testCanntInvoke(
      "package pkg\n" +
      "class Foo {\n" +
      "  function bar() {\n" +
      "    print(\"\<%=:^^%>\")\n" +
      "  }\n" +
      "}\n")
  }

}