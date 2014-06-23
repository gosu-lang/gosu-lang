/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion

uses gw.testharness.Disabled
uses gw.testharness.KnownBreak

@Disabled("dpetrusca", "Templates are not fully supported because they are a separate language in plugin.xml")
class SymbolCompletionTemplateTest extends AbstractCodeCompletionTest {

  //==============================Locals

  //method -- local vars
  function testLocalVarDeclInMethodIsShown() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "  function testFunction() {\n" +
      "    var local1 = 10 \n" +
      "    var local2 = \"10\" \n" +
      "    l^^ \n" +
      "  }" +
      "%>"
      ,{
      "local1: int", "local2: String"
    }
    )
  }

  //property getter  -- local vars
  function testLocalVarDeclInPropertyGetterIsShown() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "  property get Prop() : int{\n" +
      "    var local1 = 10 \n" +
      "    var local2 = \"10\" \n" +
      "    return l^^\n" +
      "  }" +
      "%>"
      ,{
      "local1: int"
    }
    )
  }

  //property getter  -- local vars
  @KnownBreak("Need to implement context sensitive completion", "cgross", "PL-18923")
  function testLocalVarIsntShownIfTypeDoesntMatch() {
    testNoItems({
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "  property get Prop() : int{\n" +
      "    var local1 = 10 \n" +
      "    var local2 = \"10\" \n" +
      "    return l^^\n" +
      "  }" +
      "%>"
    }
      ,{
      "local2: String"
    }
    )
  }

  //property setter  -- local vars
  function testLocalVarDeclInPropertySetterIsShown() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "  var _x : int\n" +
      "  property set Prop( i : int ) {\n" +
      "    var local1 = 10 \n" +
      "    var local2 = \"10\" \n" +
      "    l^^ \n" +
      "  }" +
      "%>"
      ,{
      "local1: int", "local2: String"
    }
    )
  }

  //block  -- local vars
  function testLocalVarDeclInBlockIsShown() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "  function method() {\n" +
      "    var x : List<String>\n" +
      "    x.each(\\ b -> {var local1 = 10; var local2 = \"qa\"; print(b + l^^)})\n" +
      "  }" +
      "%>"
      ,{
      "local1: int", "local2: String"
    }
    )
  }

  //var in for -- for loop
  function testForLoopVarsDeclIsShown() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "  function method() {\n" +
      "    for (lvar in 1..10){\n" +
      "      print(l^^)\n" +
      "    }\n" +
      "  }" +
      "%>"
      ,{
      "lvar: int"
    }
    )
  }

  //var in index -- for loop
  function testForLoopIndexVarsDeclIsShown() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "  function method() {\n" +
      "    for (s in \"a test string\" index ix){\n" +
      "      print(i^^)\n" +
      "    }\n" +
      "  }" +
      "%>"
      ,{
      "ix: int"
    }
    )
  }

  //catch clause
  function testCatchVarsDeclIsShown() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "  function method() {\n" +
      "    try{\n" +
      "    }catch(ex : java.lang.NullPointerException){\n" +
      "      throw e^^\n" +
      "    }\n" +
      "  }" +
      "%>"
      ,{
      "ex: NullPointerException"
    }
    )
  }

  //using statement
  function testUsingClauseArgumentListDeclIsShown() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "uses java.io.FileWriter\n" +
      "uses java.io.FileReader\n" +
      "function method() {\n" +
      "  using( var ##reader = new FileReader ( \"c:\\temp\\usingfun.txt\" ),\n" +
      "         var writer = new FileWriter ( \"c:\\temp\\usingfun2.txt\" ) ){\n" +
      "    var char = r^^\n" +
      "  }\n" +
      "}\n" +
      "%>"
      ,{
      "reader: FileReader"
    }
    )
  }

  //method -- Paramters
  function testParamVarsDeclInMethodIsShown() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "function method(arg1 : int, arg2 : String) : int {\n" +
      "  return a^^\n" +
      "}\n" +
      "%>"
      ,{
       "arg1: int", "arg2: String"
    }
    )
  }

  //property setter  -- Paramters
  function testParamVarDeclInPropertySetterIsShown() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "var _x : int\n" +
      "property set Prop( myVar : int ) {\n" +
      "  _x = m^^\n" +
      "}\n" +
      "%>"
      ,{
      "myVar: int"
    }
    )
  }

  //Blocks -- Paramters
  function testParamVarsDeclInBlockIsShown() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "var adder = \\ addx : Number, addy : Number -> { return a^^ }" +
      "%>"
      ,{
      "addx: Double", "addy: Double"
    }
    )
  }

  function testGenericTypsParamVarsDeclInBlockIsShown() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "function method() {\n" +
      "  var pods = {\"Web\", \"Studio\", \"BAT\"}\n" +
      "  pods.map(\\ pod -> p^^)"  +
      "}\n" +
      "%>"
      ,{
      "pod: String"
    }
    )
  }

  function testParamVarsDeclInBlockWithImplicitCoercionReturnTypeIsShown () {
    test(
     "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "function method() {\n" +
      "  var myCache = new gw.util.concurrent.Cache<String, String>( \"My Uppercase Cache\", 100, \\ theStr -> t^^ )"  +
      "}\n" +
      "%>"
      ,{
       "theStr: String"
    }
    )
  }

  //==============================methods

  //instance method
  function testFunctionDeclIsShown() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "function method1() : int {\n" +
      "  return 0\n" +
      "}\n" +
      "function method2() : String {\n" +
      "  return \"\"\n" +
      "}\n" +
      "function test() : int {\n" +
      "  return m^^\n" +
      "}\n" +
      "%>"
      ,{
      "method1(): int"
    }
    )
  }

  @KnownBreak("Need to implement context sensitive completion", "cgross", "PL-18923")
  function testFunctionDeclIsntShown() {
    testNoItems( {
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "function method1() : int {\n" +
      "  return 0\n" +
      "}\n" +
      "function method2() : String {\n" +
      "  return \"\"\n" +
      "}\n" +
      "function test() : int {\n" +
      "  return m^^\n" +
      "}\n" +
      "%>"
    }
      ,{
      "method2(): String"
    }
    )
  }

  //Static generic method
  @Disabled("dpetrusca", "fix in phase 2")
  function testFunctionDeclWithGenericDefaultTypeIsShown() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "static function  fromArrayToCollection<T>( a : T[],  c : java.util.Collection<T>) {\n" +
      "  for ( o in a) {\n" +
      "    c.add(o)\n" +
      "  }\n" +
      "}\n" +
      "function bar() {\n" +
      "  f^^\n" +
      "}\n" +
      "%>"
      ,{
      "fromArrayToCollection<T>(a: T[], c: java.util.Collection<T>)"
    }
    )
  }

  function testDefaultParamsAreShown(){
    test({
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "function bar(myInt : int, myString : String, myList : List<String>, myBlock : block() : String) {}\n" +
      "function foo() {\n" +
      "  bar(:^^)\n" +
      "}\n" +
      "%>"
    }
    ,
    {
      "myInt: int", "myString: String", "myList: List<String>", "myBlock: block():String"
    }
    )
  }

  //==============================fields

  //fields with property
  function testPropFieldIsShown() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "var _propField : String as PropField\n" +
      "function method(s : String) {\n" +
      "  P^^\n" +
      "}\n" +
      "%>"
    , {
      "PropField: String"
    }
    )
  }

  //class field
  function testClassFieldIsShown() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "var _propField : String\n" +
      "function method(s : String) {\n" +
      "  _p^^\n" +
      "}\n" +
      "%>"
    ,{
      "_propField: String"
    }
    )
  }

  //enum constants
  function testEnumIsShown() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "var y: TestEnum = F^^ \n" +
      "enum TestEnum { \n" +
      "  First, Second \n" +
      "}\n" +
      "%>"
    ,{
       "First: "   //":" is caused by the test framework since Enum constant does not provide type in the completion list
    }
    )
  }

  //property getter
  function testPropertyGetterIsShown() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "property get Prop1(): int{\n" +
      "  return 10\n" +
      "}\n" +
      "property get Prop2(): String{\n" +
      "  return \"\"\n" +
      "}\n" +
      "function method(): int{\n" +
      "  return P^^\n" +
      "}\n" +
      "%>"
    ,{
      "Prop1: int"
    }
    )
  }

  //property setter
  function testPropertySetterIsShown() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "var _x : int\n" +
      "property set Prop( i : int ) {\n" +
      "  _x = i\n" +
      "}\n" +
      "function method() {\n" +
      "  P^^\n" +
      "}\n" +
      "%>"
    ,{
      "Prop: int"
    }
    )
  }

  //==============================literals

  //string template
  @KnownBreak("PL-18696", "", "cgross")
  function testPropFieldIsShownInStringTemplate() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "var _pod : String as POD\n" +
      "function foo() {\n" +
      "  print(\"\${P^^}\")\n" +
      "}\n" +
      "%>"
    ,{
      "POD : String"
    }
    )
  }

  @KnownBreak("PL-18696", "", "cgross")
  function testClassFieldIsShownInStringTemplate() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "var _pod : String as POD\n" +
      "function foo() {\n" +
      "  print(\"\${_p^^}\")\n" +
      "}\n" +
      "%>"
    ,{
      "_pod: String"
    }
    )
  }

  @KnownBreak("PL-18696", "", "cgross")
  function testPropertyGetterIsShownInStringTemplate() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "property get Prop() : String {\n" +
      "  return \"\"\n" +
      "}\n" +
      "function foo() {\n" +
      "  print(\"\${P^^}\")\n" +
      "}\n" +
      "%>"
    ,{
      "Prop: String"
    }
    )
  }

  @KnownBreak("PL-18696", "", "cgross")
  function testFunctionDeclIsShownInStringTemplate() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "function method() : String{\n" +
      "  return \"\"\n" +
      "}\n" +
      "function foo() {\n" +
      "  print(\"\${m^^}\")\n" +
      "}\n" +
      "%>"
    ,{
      "method(): String"
    }
    )
  }

  @KnownBreak("PL-18696", "", "cgross")
  function testPropFieldIsShownInAlternateTemplateExpression () {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "var _pod : String as POD\n" +
      "function foo() {\n" +
      "  print(\"\<%=P^^%>\")\n" +
      "}\n" +
      "%>"
    ,{
      "POD: String"
    }
    )
  }


  @KnownBreak("PL-18696", "", "cgross")
  function testClassFieldIsShownInAlternateTemplateExpression(){
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "var _pod : String as POD\n" +
      "function foo() {\n" +
      "  print(\"\<%=_p^^%>\")\n" +
      "}\n" +
      "%>"
    ,{
      "_pod: String"
    }
    )
  }

  @KnownBreak("PL-18696", "", "cgross")
  function testPropertyGetterIsShownInAlternateTemplateExpression() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "property get Prop() : String {\n" +
      "  return \"\"\n" +
      "}\n" +
      "function foo() {\n" +
      "  print(\"\<%=P^^%>\")\n" +
      "}\n" +
      "%>"
    ,{
      "Prop: String"
    }
    )
  }

  @KnownBreak("PL-18696", "", "cgross")
  function testFunctionIsShownInAlternateTemplateExpression() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "function method() : String{\n" +
      "  return \"\"\n" +
      "}\n" +
      "function foo() {\n" +
      "  print(\"\<%=m^^%>\")\n" +
      "}\n" +
      "%>"
    ,{
      "method(): String"
    }
    )
  }

  //====inner class

  function testMethodIsShownInInnerClass() {
    test({
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "function something() : String {\n" +
      "  return \"something\"\n" +
      "}\n" +
      "class Inner {\n" +
      "  function getSomething() : String {\n" +
      "    return s^^\n" +
      "  }\n" +
      "}" +
      "%>"
    }
      ,{
      "something(): String"
    }
    )
  }

  function testBlockIsShown() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "function  foo() {\n" +
      "  var adder1 = \\ x : Number, y : Number -> {  return x + y }\n" +
      "  var adder2 = \\ x : Number, y : Number -> {  return x + y }\n" +
      "  adde^^ \n" +
      "}" +
      "%>"
    , {
      "adder1: block(x:Double, y:Double):Double", "adder2: block(x:Double, y:Double):Double"
    }
    )
  }



  function testPrintIsShown() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "p^^\n" +
      "%>"
    , {
      "print(): void"
    }
    )
  }

}