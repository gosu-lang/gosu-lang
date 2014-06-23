/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion

uses gw.testharness.Disabled
uses gw.testharness.KnownBreak

class DotCompletionTest extends AbstractCodeCompletionTest {

  //method local var
  function testPropertyAndMethodAreShownOnLocalVar() {
    test(
      "package pkg\n" +
      "class GosuClass { \n" +
      "  function foo() { \n" +
      "    var x: String \n" +
      "    x.^^ \n" +
      "  } \n" +
      "}"
    , {
      "Bytes: byte[]",
      "charAt(int): char"
    }
    )
  }

  function testPropertyAndMethodAreShownOnDotAfterLocalVar() {
    test(
      "package pkg\n" +
      "class GosuClass { \n" +
      "  function foo() { \n" +
      "    var x: String \n" +
      "    x.^^substring(0, 5) \n" +
      "  } \n" +
      "}"
    , {
      "Bytes: byte[]",
      "charAt(int): char"
    }
    )
  }

  //block param
  function testPropertyAndMethodAreShownOnDotAfterBlockParam() {
    test(
      "package pkg\n" +
      "class GosuClass { \n" +
      "  function foo() { \n" +
      "    var adder = \\ x : Number, y : Number -> {x.^^ ;return x + y }" +
      "  } \n" +
      "}"
    , {
      "byteValue(): byte",
      "Infinite: boolean"
    }
    )
  }

  //String literal
  function testCanInvokeOnStringLiteral() {
    testCanInvoke(
      "package pkg\n" +
      "class GosuClass { \n" +
      "  function foo() { \n" +
      "    \"\".^^ \n" +
      "  } \n" +
      "}"
    )
  }

  //classes
  function testPropertyAndMethodAreShownOnDotAfterInstanceClass() {
    test(
      "package pkg\n" +
      "class GosuClass { \n" +
      "  property get Prop() : String {return \"\"}\n" +
      "  function foo() { \n" +
      "    new GosuClass().^^ \n" +
      "  } \n" +
      "}"
    , {
      "Prop: String",
      "foo(): void"
    } 
    )
  }

  function testCanInvokeOnDotAfterTypeName() {
    test(
      "package pkg\n" +
      "class GosuClass { \n" +
      "  static function  fromArrayToCollection<T>( a : T[], c : java.util.Collection<T>) {\n" +
      "    GosuClass.f^^\n" +
      "  }\n" +
      "}"
    , {
      "fromArrayToCollection<T>(a: T[], c: Collection<T>): void"
    } 
    )
  }

  //=== Object property path
  function testCanInvokeOnDotAfterPropertyPath() {
    test(
      "package pkg\n" +
      "class Foo {\n" +
      "  function bar() {\n" +
      "    var x = \"foo\"\n" +
      "    x.Class.^^\n" +
      "  }\n" +
      "}"
    , {
      "hashCode(): int"
    } 
    )
  }

  function testCanInvokeOnDotAfterAnonymousClass() {
    test(
      "package pkg\n" +
      "class Foo {\n" +
      "  function bar(){\n" +
      "    new java.lang.Runnable(){\n" +
      "      override function run() {}\n" +
      "    }.^^\n" +
      "  }\n" +
      "}"
    , {
      "run(): void", "outer: Foo"
    } 
    )
  }

  //object method path
  function testCanInvokeOnDotAfterMethodCall() {
    testCanInvoke(
      "package pkg\n" +
      "class Foo {\n" +
      "  function bar() {\n" +
      "    var x = \"foo\"\n" +
      "    x.substring(0).^^\n" +
      "  }\n" +
      "}\n")
  }

  //string template
  @KnownBreak("PL-18696", "", "cgross")
  function testCanInvokeOnDotAfterPropertyPathInStringTemplate() {
    testCanInvoke(
      "package pkg\n" +
      "class Foo {\n" +
      "  function bar() {\n" +
      "    var x = \"foo\"\n" +
      "    print(\"\${x.Class.^^}\")\n" +
      "  }\n" +
      "}\n")
  }

  @KnownBreak("PL-18696", "", "cgross")
  function testCanInvokeOnDotAfterMethodCallInStringTemplate() {
    testCanInvoke(
      "package pkg\n" +
      "class Foo {\n" +
      "  function bar() {\n" +
      "    var x = \"foo\"\n" +
      "    print(\"\${x.substring(0).^^}\")\n" +
      "  }\n" +
      "}\n")
  }

  function testCanntInvokeOnDotAtBeginningOfStringTemplateExpression() {
    testCanntInvoke(
      "package pkg\n" +
      "class Foo {\n" +
      "  function bar() {\n" +
      "    print(\"\${.^^}\")\n" +
      "  }\n" +
      "}\n")
  }

  @KnownBreak("PL-18696", "", "cgross")
  function testCanInvokeOnDotAfterPropertyPathInAlternateTemplateExpression() {
    testCanInvoke(
      "package pkg\n" +
      "class Foo {\n" +
      "  function bar() {\n" +
      "    var x = \"foo\"\n" +
      "    print(\"\<%=x.Class.^^%>\")\n" +
      "  }\n" +
      "}\n")
  }

  @KnownBreak("PL-18696", "", "cgross")
  function testCanInvokeOnDotAfterMethodCallInAlternateTemplateExpression() {
    testCanInvoke(
      "package pkg\n" +
      "class Foo {\n" +
      "  function bar() {\n" +
      "    var x = \"foo\"\n" +
      "    print(\"\<%=x.substring(0).^^%>\")\n" +
      "  }\n" +
      "}\n")
  }

  function testCanntInvokeOnDotAtBeginningOfAlternateTemplateExpression() {
    testCanntInvoke(
      "package pkg\n" +
      "class Foo {\n" +
      "  function bar() {\n" +
      "    print(\"\<%=.^^%>\")\n" +
      "  }\n" +
      "}\n")
  }

  //comment, javadoc

  function testCanntInvokeOnDotInLineComment() {
    testCanntInvoke(
      "package pkg\n" +
      "class Foo {\n" +
      "  // this is a comment.^^\n" +
      "  function bar() {\n" +
      "  }\n" +
      "}\n")
  }

  function testCanntInvokeOnDotInBlockComment() {
    testCanntInvoke(
      "package pkg\n" +
      "class Foo {\n" +
      "  /* this is \n" +
      "     a block comment.^^*/\n" +
      "  function bar() {\n" +
      "  }\n" +
      "}\n")
  }

  function testCanntInvokeOnJavadoc() {
    testCanntInvoke(
      "package pkg\n" +
      "class Foo {\n" +
      "  /**\n" +
      "   * this is a java doc.^^\n" +
      "   */\n" +
      "  function bar() {\n" +
      "  }\n" +
      "}\n")
  }

  // Expansion operator
  function testCanInvokeOnExpansionDot() {
    testCanInvoke(
      "package pkg\n" +
      "class GosuClass {\n" +
      "  var _pods : String[] as Pods\n" +
      "  function foo() {\n" +
      "    var obj1 = new GosuClass() { :Pods = {\"Developer IDE\", \"Analyst Tools\"} }\n" +
      "    var obj2 = new GosuClass() { :Pods = {\"Code Utilities\", \"Customer Tools\"} }\n" +
      "    var podList : List<GosuClass> = {obj1, obj2}\n" +
      "    var allMembers = podList*.^^\n" +
      "  }\n" +
      "}"
    )
  }

  function testCanInvokeOnExpansionDotInProgram() {
    testCanInvoke({
      "package some.pkg\n" +
      "class GosuClass { \n" +
      "  var _pods : String[] as Pods\n" +
      "}"
    ,
      "//PROGRAM, some/pkg/MyProgram\n" +
      "var obj1 = new some.pkg.GosuClass() { :Pods = {\"Developer IDE\", \"Analyst Tools\"} }\n" +
      "var obj2 = new some.pkg.GosuClass() { :Pods = {\"Code Utilities\", \"Customer Tools\"} }\n" +
      "var podList : List<some.pkg.GosuClass> = {obj1, obj2}\n" +
      "var allMembers = podList*.^^\n"
    }
    )
  }

  //var in for -- for loop
  function testGoToForLoopVarsDecl() {
    testCanntInvoke(
      "package pkg\n" +
      "class Foo {\n" +
      "  function method() {\n" +
      "    for (lvar in 1.^^\n" +
      "  }\n" +
      "}\n")
  }

  function testGoToForLoopVarsDecl2() {
    testCanInvoke(
      "package pkg\n" +
      "class Foo {\n" +
      "  function method() {\n" +
      "    for (lvar in 0..|1..^^\n" +
      "  }\n" +
      "}\n")
  }

  function testMethodsInEnhancedTypeAfterThisIsShownInEnhancement() {
    test({
      "package some.pkg\n" +
      "class GosuClass {\n" +
      " function foo( i : int, s : String, l : List<String> ) : String {return \"\"}\n" +
      "}"
    ,
      "package some.pkg\n" +
      "enhancement GosuClassEnhencement : GosuClass {\n" +
      "    function bar() : String {\n" +
      "      this.f^^" +
      "    }\n" +
      "}"}
      ,{
      "foo(i: int, s: String, l: List<String>): String"
    } 
    )
  }

  function testMethodsInEnhancedTypeSuperClassAfterThisIsShownInEnhancement() {
    test({
      "package some.pkg\n" +
      "class SuperGosuClass {\n" +
      "    function foo( i : int, s : String, l : List<String> ) {\n" +
      "    }\n" +
      "}"
    ,
      "package some.pkg\n" +
      "class GosuClass extends SuperGosuClass {\n" +
      "}"
    ,
      "package some.pkg\n" +
      "enhancement GosuClassEnhencement : GosuClass {\n" +
      "    function bar() {\n" +
      "        this.f^^\n" +
      "    }\n" +
      "}"}
      ,{
      "foo(i: int, s: String, l: List<String>): void"
    }
    )
  }

  function testMethodsInEnhancedTypeSuperJClassAfterThisIsShownInEnhancement() {
    test({
      "//JAVA\n" +
      "package some.pkg;\n" +
      "import java.util.List;"  +
      "public class SuperJavaClass {\n" +
      "    public String foo(int i, String s, List<String> l) {return \"\";}\n" +
      "}"
    ,
      "package some.pkg\n" +
      "class GosuClass extends SuperJavaClass {\n" +
      "}"
    ,
      "package some.pkg\n" +
      "enhancement GosuClassEnhencement : GosuClass {\n" +
      "    function bar() {\n" +
      "        this.f^^\n" +
      "    }\n" +
      "}"}
      ,{
       "foo(p0: int, p1: String, p2: List<String>): String"
    } 
    )
  }

  function testOuterMethodIsShownFromOuter() {
    test({
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function something(  s : String, l : List<String> ) : String {\n" +
      "    return \"something\"\n" +
      "  }\n" +
      "  class Inner {\n" +
      "    function getSomething() : String {\n" +
      "      return outer.s^^\n" +
      "    }\n" +
      "    function something( i : int, s : String, l : List<String> ) : String {\n" +
      "      return \"inner something\"\n" +
      "    }\n" +
      "  }"+
      "}"}
      ,{
      "something(s: String, l: List<String>): String"
    } 
    )
  }

  function testInnerPrivateFunctionIsShownFromOuter() {
    test({
      "package some.pkg\n" +
      "class CanReferenceInnerPrivateMembersFromOuter {\n" +
      "    var _inner : Inner\n" +
      "    function accessPrivateInnerFunction() : String {\n" +
      "        return _inner.p^^\n" +
      "    }\n" +
      "    class Inner {\n" +
      "        private function privateFunction( i : int, s : String, l : List<String> ) : String {\n" +
      "            return \"privateFunction\"\n" +
      "        }\n" +
      "    }\n" +
      "}"}
      ,{
      "privateFunction(i: int, s: String, l: List<String>): String"
    } 
    )
  }

  function testInnerPrivateClassFieldIsShownFromOuter() {
    test({
      "package some.pkg\n" +
      "class CanReferenceInnerPrivateMembersFromOuter {\n" +
      "    var _inner : Inner\n" +
      "    function accessPrivateInnerData() : String {\n" +
      "        return _inner._p^^\n" +
      "    }\n" +
      "    class Inner {\n" +
      "        var _privateData : String\n\n" +
      "    }\n" +
      "}"}
      ,{
      "_privateData: String"
    } 
    )
  }

  function testClassTypeInfoPropertiesAreShown() {
    test({
      "package some.pkg\n" +
          "class GosuClass {\n" +
          "  function bar() {\n" +
          "    var typeinfo = GosuClass.Type.TypeInfo.^^\n" +
          "  }\n" +
          "}"
      }
      ,{
      "Annotations: List<IAnnotationInfo>","Constructors: List<IConstructorInfo>", "DeclaredMethods: List<IMethodInfo>"
    }
    )
  }

  function testClassTypeInfoMethodsAreShown() {
    test({
      "package some.pkg\n" +
          "class GosuClass {\n" +
          "  function bar() {\n" +
          "    var typeinfo = GosuClass.Type.TypeInfo.^^\n" +
          "  }\n" +
          "}"
      }
      ,{
      "getConstructors(IType): List<IConstructorInfo>", "getMethods(IType): MethodList"
    }
    )
  }

  function testCanntInvokeFromOutofScopeInstanceVar() {
    testCanntInvoke({
      "package some.pkg\n" +
          "class GosuClass {\n" +
          "  function bar() {\n" +
          "    if (false)\n" +
          "       var outofscopeVar = new GosuClass()\n" +
          "    outofscopeVar.^^\n" +
          "  }\n" +
          "}"
    }
    )
  }

  @Disabled("dpetrusca", "fix in phase 2")
  function testCanntInvokeFromOutofScopeInstanceVarInProgram() {
    testCanntInvoke({
      "package some.pkg\n" +
      "class GosuClass { \n" +
      "  function foo() {} \n" +
      "}"
    ,
      "//PROGRAM, some/pkg/MyProgram\n" +
      "  if (false) \n" +
      "    var outofscopeVar = new GosuClass() \n"  +
      "  outofscopeVar.^^\n"
      }
    )
  }

  function testCanntInvokeFromOutofScopeInstanceVar1() {
    testCanntInvoke({
      "package some.pkg\n" +
          "class GosuClass {\n" +
          "  function bar() {\n" +
          "    for(i in 0..10)\n" +
          "       var outofscopeVar = new GosuClass()\n" +
          "    outofscopeVar.^^\n" +
          "  }\n" +
          "}"
    }
    )
  }

}