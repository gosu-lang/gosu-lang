/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion

uses gw.testharness.Disabled

class KeywordCompletionTest extends AbstractCodeCompletionTest {

  override function shouldReturnType(): boolean {
    return false;
  }

  //==================out of class

  function testAbstractIsShown() {
    test(
      "package some.pkg\n" +
      "a^^ class GosuClass{}\n"
    , {
      "abstract"
    }
    )
  }

  function testClassIsShown() {
    test(
      "package some.pkg\n" +
      "c^^lass GosuClass{}\n"
    , {
      "class"
    }
    )
  }

  @Disabled("cgross", "not worth fixing in 1.0")
  function testConstructIsntShown() {
    testNoItems({
      "package some.pkg\n" +
      "c^^ class GosuClass{}\n"
    }
    , {
      "construct"
    }
    )
  }

  @Disabled("cgross", "not worth fixing in 1.0")
  function testDelegateIsntShown() {
    testCanntInvoke(
      "package some.pkg\n" +
      "d^^ class GosuClass{}\n"
    )
  }

  function testEnumIsShown() {
    test(
      "package some.pkg\n" +
      "e^^ class GosuClass{}\n"
    , {
      "enum"
    }
    )
  }

  function testEnumIsShown2() {
    test(
      "package some.pkg\n" +
      "e^^num GosuEnum{}\n"
    , {
      "enum"
    }
    )
  }

  function testEnhancementIsntShown() {
    testNoItems({
      "package some.pkg\n" +
      "e^^ class GosuClass{}\n"
    }
    , {
      "enhancement"
    }
    )
  }

  @Disabled("cgross", "not worth fixing in 1.0")
  function testEnhancementIsShown() {
    test(
      "package some.pkg\n" +
      "e^^nhancement GosuEnhancement : java.lang.String {}\n"
    , {
      "enhancement"
    }
    )
  }

  @Disabled("cgross", "not worth fixing in 1.0")
  function testEnumIsntShown() {
    testNoItems({
      "package some.pkg\n" +
      "e^^nhancement EdcEnh : java.lang.String {}\n"
    }
    , {
      "enum"
    }
    )
  }

  function testFinalIsShown() {
    test(
      "package some.pkg\n" +
      "f^^ class GosuClass{}\n"
    , {
      "final"
    }
    )
  }

  @Disabled("cgross", "not worth fixing in 1.0")
  function testFunctionIsntShown() {
    testNoItems({
      "package some.pkg\n" +
      "f^^ class GosuClass{}\n"}
    , {
      "function"
    }
    )
  }

  function testInterfaceAndInternalIsShown() {
    test(
      "package some.pkg\n" +
      "i^^ class GosuClass{}\n"
    , {
      "interface", "internal"
    }
    )
  }

  @Disabled("cgross", "not worth fixing in 1.0")
  function testClassAccessModifiersAreShown() {
    test(
      "package some.pkg" +
      "p^^\n" +
      "class GosuClass {}\n"
    , {
      "protected", "public"
    }
    )
  }

  function testPropertyIsntShown() {
    testNoItems({
      "package some.pkg" +
      "p^^\n" +
      "class GosuClass {}\n"
    }
    , {
      "property"
    }
    )
  }

  @Disabled("cgross", "not worth fixing in 1.0")
  function testUsesIsShown() {
    test(
      "package some.pkg\n" +
      "u^^\n" +
      "class GosuClass {}\n"
    , {
      "uses"
    }
    )
  }

  function testStaticIsShown() {
    test(
      "package some.pkg\n" +
      "s^^\n" +
      "class GosuClass {}\n"
    , {
      "static"
    }
    )
  }

  //==================in class declaration

  function testClassExtendsOrImplementsAreShownInClassDecl() {
    test(
      "package some.pkg\n" +
      "class GosuClass ^^\n"
    , {
      "extends", "implements"
    }
    )
  }

  @Disabled("cgross", "not worth fixing in 1.0")
  function testKWsArentShownInClassDeclAfterClass() {
    testCanntInvoke(
      "package some.pkg\n" +
      "class ^^ GosuClass{}\n"
    )
  }

  function testOtherKWsArentShownInClassDecl() {
    testNoItems({
      "package some.pkg\n" +
      "class GosuClass ^^{}\n"
    }
    , {
      "function", "public", "static"
    }
    )
  }

  @Disabled("cgross", "not worth fixing in 1.0")
  function testInterfaceExtendsIsShownInInterfaceDecl() {
    test(
      "package some.pkg\n" +
      "interface GosuIface ^^\n"
    , {
      "extends"
    }
    )
  }

  @Disabled("cgross", "not worth fixing in 1.0")
  function testOtherKWsArentShownInInterfaceDecl() {
    testNoItems({
      "package some.pkg\n" +
      "interface GosuIface ^^\n"
    }
    , {
      "implements", "function", "public", "static"
    }
    )
  }

  @Disabled("cgross", "not worth fixing in 1.0")
  function testImplementsIsShownInEnumDecl() {
    test(
      "package some.pkg\n" +
      "enum GosuEnum ^^\n"
    , {
      "implements"
    }
    )
  }

  @Disabled("cgross", "not worth fixing in 1.0")
  function testOtherKWsArentShownInEnumDecl() {
    testNoItems({
      "package some.pkg\n" +
      "enum GosuEnum ^^\n"
    }
    , {
      "extends", "function", "public", "static"
    }
    )
  }

  //==================in class body

  function testAbstractIsShownInClassBody() {
    test(
      "package some.pkg\n" +
      "abstract class GosuClass {\n" +
      "  a^^\n" +
      "}\n"
    , {
      "abstract"
    }
    )
  }

  function testConstructIsShownInClassBody() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  c^^\n" +
      "}\n"
    , {
      "construct"
    }
    )
  }

  function testClassIsShownInClassBody() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  c^^\n" +
      "}\n"
    , {
      "class"
    }
    )
  }

  function testDelegateIsShownInClassBody() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  d^^\n" +
      "}\n"
    , {
      "delegate"
    }
    )
  }

  function testDelegateIsShownInEnhancementBody() {
    testCanntInvoke(
      "package some.pkg\n" +
      "enhancement GosuEnhancement {\n" +
      "  d^^\n" +
      "}\n"
    )
  }

  function testEnumIsShownInClassBody() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  e^^\n" +
      "}\n"
    , {
      "enum"
    }
    )
  }

  function testEnhancementIsntShownInClassBody() {
    testNoItems({
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  e^^\n" +
      "}\n"}
    , {
      "enhancement"
    }
    )
  }

  function testFinalAndFunctionAreShownInClassBody() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  f^^\n" +
      "}\n"
    , {
      "final", "function"
    }
    )
  }

  function testInternalIsShownInClassBody() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  i^^\n" +
      "}\n"
    , {
      "internal"
    }
    )
  }

  function testInterfaceIsShownInClassBody() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  i^^\n" +
      "}\n"
    , {
      "interface"
    }
    )
  }

  function testOverrideIsShownInClassBody() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  o^^\n" +
      "}\n"
    , {
      "override"
    }
    )
  }

  function testPropertyAndAccessModifiersAreShownInClassBody() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  p^^\n" +
      "}\n"
    , {
      "private", "property", "protected", "public"
    }
    )
  }

  function testStaticIsShownInClassBody() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  s^^\n" +
      "}\n"
    , {
      "static"
    }
    )
  }

  @Disabled("cgross", "not worth fixing in 1.0")
  function testTransientIsShownInClassBody() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  t^^\n" +
      "}\n"
    , {
      "transient"
    }
    )
  }

  function testVarIsShownInClassBody() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  v^^\n" +
      "}\n"
    , {
      "var"
    }
    )
  }

  function testVarIsntShownInEnhancementBody() {
    testCanntInvoke(
      "package some.pkg\n" +
      "enhancement GosuEnhancement {\n" +
      "  v^^\n" +
      "}\n"
    )
  }

  function testPackageIsntShownInClassBody() {
    testNoItems({
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  p^^\n" +
      "}\n"
    }
    , {
      "package"
    }
    )
  }

  function testUsesIsntShownInClassBody() {
    testCanntInvoke(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  u^^\n" +
      "}\n"
    )
  }

  //==================in constructor decl

  function testCanntInvokeAfterConstructKW() {
    testCanntInvoke({
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  construct ^^\n" +
      "}\n"
    }
    )
  }

  //==================in var declaration

  @Disabled("cgross", "not worth fixing in 1.0")
  function testAsIsShownInVar() {
    test({
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var _pod a^^\n" +
      "}\n"
    }
    , {
      "as"
    }
    )
  }

  @Disabled("cgross", "not worth fixing in 1.0")
  function testReadonlyIsShownInVar() {
    test({
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "static var _applicationBaseURLCount : Integer as r^^\n" +
      "}\n"
    }
    , {
      "readonly"
    }
    )
  }

  @Disabled("cgross", "not worth fixing in 1.0")
  function testApplicationIsShownInVar() {      //deprecated
    test({
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  public var _appVar a^^\n" +
      "}\n"
    }
    , {
      "application"
    }
    )
  }

  @Disabled("cgross", "not worth fixing in 1.0")
  function testSessionIsShownInVar() {        //deprecated
    test({
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  public var _sessionVar s^^\n" +
      "}\n"
    }
    , {
      "session"
    }
    )
  }

  @Disabled("cgross", "not worth fixing in 1.0")
  function testRequestIsShownInVar() {       //deprecated
    test({
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  public var _requestVar r^^\n" +
      "}\n"
    }
    , {
      "request"
    }
    )
  }

  @Disabled("cgross", "not worth fixing in 1.0")
  function testExecutionIsShownInVar() {      //deprecated
    test({
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  public var _executionVar e^^\n" +
      "}\n"
    }
    , {
      "execution"
    }
    )
  }

  //==================in degelate declaration

  @Disabled("cgross", "not worth fixing in 1.0")
  function testRepresentsIsShownInClassBody() {
    test({
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  degelate _map r^^\n" +
      "}\n"
    }
    , {
      "represents"
    }
    )
  }

  //==================in function declaration
  function testCanntInvokeAfterFuncitonKW() {
    testCanntInvoke(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function p^^\n" +
      "}\n"
    )
  }

  function testBlockIsShownInFunctionReturn() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method() :^^\n" +
      "}\n"
    ,{
      "block"
    }
    )
  }

  function testVoidIsShownInFunctionReturn() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method() :v^^\n" +
      "}\n"
    ,{
      "void"
    }
    )
  }

  //==================in property declaration
  function testCanntInvokeAfterPropertyKW() {
    testCanntInvoke(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  property p^^\n" +
      "}\n"
    )
  }

  @Disabled("dpetrusca", "fix in phase 2")
  function testGetAndSetAreShownAfterPropertyKWInClassBody() {
    test({
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  property ^^\n" +
      "}\n"
    }
    , {
      "get", "set"
    }
    )
  }

  //==================in function/constructor/property setter/getter body

  function testAKWsArentShownInFunctionBody() {
    testNoItems({
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    a^^\n" +
      "  }\n" +
      "}\n"
    }
    , {
     "abstract"
    }
    )
  }

  @Disabled("cgross", "not worth fixing in 1.0")
  function testAKWsArentShownInFunctionBody1() {
    testNoItems({
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    a^^\n" +
      "  }\n" +
      "}\n"
    }
    , {
     "and"
    }
    )
  }

  @Disabled("cgross", "not worth fixing in 1.0")
  function testBKWsArentShownInFunctionBody() {
    testNoItems({
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    b^^\n" +
      "  }\n" +
      "}\n"
    }
    , {
     "block", "break"
    }
    )
  }

  @Disabled("cgross", "not worth fixing in 1.0")
  function testCKWsArentShownInFunctionBody() {
    testNoItems({
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    c^^\n" +
      "  }\n" +
      "}\n"
    }
    , {
     "catch"
    }
    )
  }

  function testCKWsArentShownInFunctionBody1() {
    testNoItems({
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    c^^\n" +
      "  }\n" +
      "}\n"
    }
    , {
     "construct"
    }
    )
  }

  function testDKWsArentShownInFunctionBody() {
    testNoItems({
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    d^^\n" +
      "  }\n" +
      "}\n"
    }
    , {
     "delegate", "default"
    }
    )
  }

  function testDoIsShownInFunctionBody() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    d^^\n" +
      "  }\n" +
      "}\n"
    , {
     "do"
    }
    )
  }

  function testEKWsArentShownInFunctionBody() {
    testNoItems({
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    e^^\n" +
      "  }\n" +
      "}\n"
    }
    , {
     "extends", "enum"
    }
    )
  }

  function testEvalIsShownInFunctionBody() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    e^^\n" +
      "  }\n" +
      "}\n"
    , {
     "eval"
    }
    )
  }

  @Disabled("cgross", "not worth fixing in 1.0")
  function testFKWsArentShownInFunctionBody() {
    testNoItems({
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    f^^\n" +
      "  }\n" +
      "}\n"
    }
    , {
     "function", "finally", "false"
    }
    )
  }

  function testFKWsArentShownInFunctionBody1() {
    testNoItems({
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    f^^\n" +
      "  }\n" +
      "}\n"
    }
    , {
     "function"
    }
    )
  }

  function testFKWsAreShownInFunctionBody() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    f^^\n" +
      "  }\n" +
      "}\n"
    , {
     "for", "final"
    }
    )
  }

  function testInIsShownInFor() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    var myArray = new String[10]\n" +
      "    for(i in^^)\n" +
      "  }\n" +
      "}\n"
    , {
     "in"
    }
    )
  }

  function testInIsntShownInFor() {
    testCanntInvoke(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    var myArray = new String[10]\n" +
      "    for(i^^)\n" +
      "  }\n" +
      "}\n"
    )
  }

  @Disabled("cgross", "not worth fixing in 1.0")
  function testIndexIsShownInFor() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    var myArray = new String[10]\n" +
      "    for(i in myArray i^^)\n" +
      "  }\n" +
      "}\n"
    , {
     "index"
    }
    )
  }

  function testIteratorIsntShownInForWithNonIterableTyps() {
    testNoItems({
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    var myArray = new String[10]\n" +
      "    for(i in myArray i^^)\n" +
      "  }\n" +
      "}\n"
    }
    , {
     "iterator"
    }
    )
  }

  @Disabled("cgross", "not worth fixing in 1.0")
  function testIndexAndIteratorAreShownInFor() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    var myArray = {123, 234, 345}\n" +
      "    for(i in myArray i^^)\n" +
      "  }\n" +
      "}\n"
    , {
     "index", "iterator"
    }
    )
  }

  function testGKWsArentShownInFunctionBody() {
      testNoItems({
        "package some.pkg\n" +
        "class GosuClass {\n" +
        "  function method {\n" +
        "    g^^\n" +
        "  }\n" +
        "}\n"
      }
      , {
       "get"
      }
      )
    }

  function testHKWsArentShownInFunctionBody() {
      testNoItems({
        "package some.pkg\n" +
        "class GosuClass {\n" +
        "  function method {\n" +
        "    h^^\n" +
        "  }\n" +
        "}\n"
      }
      , {
       "hide"
      }
      )
    }

  function testIKWsArentShownInFunctionBody() {
    testNoItems({
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    i^^\n" +
      "  }\n" +
      "}\n"
    }
    , {
     "Infinity", "in", "index", "internal", "iterator", "implement", "interface"
    }
    )
  }

  function testIKWsAreShownInFunctionBody() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    i^^\n" +
      "  }\n" +
      "}\n"
    , {
     "if"
    }
    )
  }

  function testElseIsShownAfterIfClause() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    if (true) {} e^^\n" +
      "  }\n" +
      "}\n"
    , {
     "else"
    }
    )
  }

  function testLKWsArentShownInFunctionBody() {
    testNoItems({
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    l^^\n" +
      "  }\n" +
      "}\n"
    }
    , {
     "length"
    }
    )
  }

  @Disabled("dpetrusca", "fix in phase 2")
  function testNKWsArentShownInFunctionBody() {
    testNoItems({
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    n^^\n" +
      "  }\n" +
      "}\n"
    }
    , {
     "NaN", "not", "null"
    }
    )
  }

  function testNewKWsAreShownInFunctionBody() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    n^^\n" +
      "  }\n" +
      "}\n"
    , {
     "new"
    }
    )
  }

  @Disabled("cgross", "not worth fixing in 1.0")
  function testOKWsArentShownInFunctionBody() {
    testNoItems({
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    o^^\n" +
      "  }\n" +
      "}\n"
    }
    , {
     "or", "outer"
    }
    )
  }

  function testOKWsArentShownInFunctionBody1() {
    testNoItems({
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    o^^\n" +
      "  }\n" +
      "}\n"
    }
    , {
     "override"
    }
    )
  }

  function testOuterIsShownInInnerFunctionBody() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  class InnerGosuClass {\n" +
      "    function method {\n" +
      "      o^^\n" +
      "    }\n" +
      "  }\n" +
      "}\n"
    , {
     "outer"
    }
    )
  }

  @Disabled("cgross", "not worth fixing in 1.0")
  function testOuterIsntShownInStaticInnerFunctionBody() {
    testNoItems({
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  static class InnerGosuClass {\n" +
      "    function method {\n" +
      "      o^^\n" +
      "    }\n" +
      "  }\n" +
      "}\n"
    }
    , {
     "outer"
    }
    )
  }

  function testOuterIsShownInAnonymousFunctionBody() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method(){\n" +
      "    var o = new java.lang.Runnable(){\n" +
      "      override function run() {\n" +
      "        o^^\n" +
      "      }\n" +
      "    }\n" +
      "  }\n" +
      "}\n"
    , {
     "outer"
    }
    )
  }

  function testPKWsArentShownInFunctionBody() {
    testNoItems({
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    p^^\n" +
      "  }\n" +
      "}\n"
    }
    , {
     "package", "private", "property"
    }
    )
  }

  function testRKWsArentShownInFunctionBody() {
    testNoItems({
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    r^^\n" +
      "  }\n" +
      "}\n"
    }
    , {
     "represent", "readonly"
    }
    )
  }

  function testReturnKWsAreShownInFunctionBody() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    r^^\n" +
      "  }\n" +
      "}\n"
    , {
     "return"
    }
    )
  }

  @Disabled("cgross", "not worth fixing in 1.0")
  function testSKWsArentShownInFunctionBody() {
    testNoItems({
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    s^^\n" +
      "  }\n" +
      "}\n"
    }
    , {
     "startswith", "set", "statictypeof"
    }
    )
  }

  function testSKWsArentShownInFunctionBody1() {
    testNoItems({
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    s^^\n" +
      "  }\n" +
      "}\n"
    }
    , {
     "static"
    }
    )
  }

  function testSKWsAreShownInFunctionBody() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "   s^^\n" +
      "  }\n" +
      "}\n"
    , {
     "switch", "super"
    }
    )
  }

  function testCaseIsShownInSwitch() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    switch (myVar) {\n" +
      "      c^^\n" +
      "    }\n" +
      "  }\n" +
      "}\n"
    , {
     "case"
    }
    )
  }

  @Disabled("cgross", "not worth fixing in 1.0")
  function testCaseIsntShownAfterDefaultInSwitch() {
    testNoItems({
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    switch (myVar) {\n" +
      "      default:  print(\"ok\")\n" +
      "      c^^\n" +
      "    }\n" +
      "  }\n" +
      "}\n"
    }
    , {
     "case"
    }
    )
  }

  @Disabled("cgross", "not worth fixing in 1.0")
  function testDefaultIsShownInSwitch() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    switch (myVar) {\n" +
      "      d^^\n" +
      "    }\n" +
      "  }\n" +
      "}\n"
    , {
     "default"
    }
    )
  }

  @Disabled("cgross", "not worth fixing in 1.0")
  function testTKWsArentShownInFunctionBody() {
    testNoItems({
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    t^^\n" +
      "  }\n" +
      "}\n"
    }
    , {
     "true", "typeof", "typeis", "typeas"
    }
    )
  }

  function testTKWsArentShownInFunctionBody1() {
    testNoItems({
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    t^^\n" +
      "  }\n" +
      "}\n"
    }
    , {
     "transient"
    }
    )
  }

  function testTKWsAreShownInFunctionBody() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    t^^\n" +
      "  }\n" +
      "}\n"
    , {
     "try", "this", "throw"
    }
    )
  }

  function testCatchIsShownAfterTry() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    try {\n" +
      "    } c^^" +
      "  }\n" +
      "}\n"
    , {
     "catch"
    }
    )
  }

  function testCatchIsntShownWithoutTry() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    c^^" +
      "  }\n" +
      "}\n"
    , {
     "catch"
    }
    )
  }

  function testFinallyIsShownAfterTry() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    try {\n" +
      "    } f^^" +
      "  }\n" +
      "}\n"
    , {
     "finally"
    }
    )
  }

  function testFinallyIsntShownWithoutTry() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    f^^" +
      "  }\n" +
      "}\n"
    , {
     "finally"
    }
    )
  }

  function testUKWsArentShownInFunctionBody() {
    testNoItems({
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    u^^\n" +
      "  }\n" +
      "}\n"
    }
    , {
     "uses", "unless"
    }
    )
  }

  function testUKWsAreShownInFunctionBody() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    u^^\n" +
      "  }\n" +
      "}\n"
    , {
     "using"
    }
    )
  }

  @Disabled("cgross", "not worth fixing in 1.0")
  function testVKWsArentShownInFunctionBody() {
    testNoItems({
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    v^^\n" +
      "  }\n" +
      "}\n"
    }
    , {
     "void"
    }
    )
  }

  function testVKWsAreShownInFunctionBody() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    v^^\n" +
      "  }\n" +
      "}\n"
    , {
     "var"
    }
    )
  }

  @Disabled("cgross", "not worth fixing in 1.0")
  function testWKWsArentShownInFunctionBody() {
    testNoItems({
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    w^^\n" +
      "  }\n" +
      "}\n"
    }
    , {
     "where"
    }
    )
  }

  function testWKWsAreShownInFunctionBody() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    w^^\n" +
      "  }\n" +
      "}\n"
    , {
     "while"
    }
    )
  }

  function testBreakIsShownInForLoop() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    for(i in myArray index ix) {\n" +
      "      if (true) b^^\n" +
      "    }\n" +
      "  }\n" +
      "}\n"
    , {
     "break"
    }
    )
  }

  @Disabled("cgross", "not worth fixing in 1.0")
  function testBreakIsntShownInWithoutLoopsOrSwitch() {
    testNoItems({
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "      if (true) b^^\n" +
      "  }\n" +
      "}\n"
    }
    , {
     "break"
    }
    )
  }

  function testBreakIsShownInWhileLoop() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    while(true) {\n" +
      "      b^^\n" +
      "    }\n" +
      "  }\n" +
      "}\n"
    , {
     "break"
    }
    )
  }

  function testBreakIsShownInDoWhileLoop() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    do {\n" +
      "      b^^\n" +
      "    }\n" +
      "  }\n" +
      "}\n"
    , {
     "break"
    }
    )
  }

  @Disabled("cgross", "not worth fixing in 1.0")
  function testBreakIsShownInSwitchAfterCase() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method(myVar : String) {\n" +
      "    switch (myVar) {\n" +
      "      case \"Monday\" :\n" +
      "      b^^\n" +
      "    }\n" +
      "  }\n" +
      "}\n"
    , {
     "break"
    }
    )
  }

  function testBreakIsShownInSwitchAfterDefault() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method(myVar : String) {\n" +
      "    switch (myVar) {\n" +
      "      default:  print(\"ok\")\n" +
      "      b^^\n" +
      "    }\n" +
      "  }\n" +
      "}\n"
    , {
     "break"
    }
    )
  }

  @Disabled("cgross", "not worth fixing in 1.0")
  function testBreakIsntShownInSwitchWithoutCaseOrDefault() {
    testNoItems({
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method(myVar : String) {\n" +
      "    switch (myVar) {\n" +
      "      b^^\n" +
      "    }\n" +
      "  }\n" +
      "}\n"
    }
    , {
     "break"
    }
    )
  }

  function testContinueIsShownInForLoop() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    for(i in 0..10) {\n" +
      "      c^^\n" +
      "    }\n" +
      "  }\n" +
      "}\n"
    , {
     "continue"
    }
    )
  }

  @Disabled("cgross", "not worth fixing in 1.0")
  function testContinueIsntShownInWithoutLoops() {
    testNoItems({
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "      if (true) c^^\n" +
      "  }\n" +
      "}\n"
    }
    , {
     "continue"
    }
    )
  }

  function testContinueIsShownInWhileLoop() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    while(true) {\n" +
      "      c^^\n" +
      "    }\n" +
      "  }\n" +
      "}\n"
    , {
     "continue"
    }
    )
  }

  function testContinueIsShownInDoWhileLoop() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    do {\n" +
      "      c^^\n" +
      "    }\n" +
      "  }\n" +
      "}\n"
    , {
     "continue"
    }
    )
  }

  function testTrueIsShownInIfClause(){
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    if (t^^)\n" +
      "  }\n" +
      "}\n"
    , {
     "true"
    }
    )
  }

  function testFalseIsShownInIfClause(){
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    if (f^^)\n" +
      "  }\n" +
      "}\n"
    , {
     "false"
    }
    )
  }

  function testTrueIsShownInWhileClause(){
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    while (t^^)\n" +
      "  }\n" +
      "}\n"
    , {
     "true"
    }
    )
  }

  function testFalseIsShownInWhileClause(){
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method {\n" +
      "    while (f^^)\n" +
      "  }\n" +
      "}\n"
    , {
     "false"
    }
    )
  }

  //====template
  @Disabled("cgross", "not worth fixing in 1.0")
  function testParamsIsShownInTemplateScriptletTag() {
    test(
      "//TEMPLATE, package some/pkg\n" +
      "\<%@ p^^ %>\n"
      ,{
       "params"
    }
    )
  }

  @Disabled("cgross", "not worth fixing in 1.0")
  function testExtendsIsShownInTemplateScriptletTag() {
    test(
      "//TEMPLATE, package some/pkg\n" +
      "\<%@ e^^ %>\n"
    ,{
       "extends"
    }
    )
  }

  //====program
  @Disabled("dpetrusca", "fix in phase 2")
  function testClassIsShownInTopLevel() {
    test(
      "//PROGRAM, pkg/MyProgram\n" +
      "  c^^\n"
    , {
      "class"
    }
    )
  }

  @Disabled("dpetrusca", "fix in phase 2")
  function testEnumIsShownInTopLevel() {
    test(
      "//PROGRAM, pkg/MyProgram\n" +
      "  e^^\n"
    , {
      "enum"
    }
    )
  }

  @Disabled("dpetrusca", "fix in phase 2")
  function testFunctionIsShownInTopLevel() {
    test(
      "//PROGRAM, pkg/MyProgram\n" +
      "  f^^\n"
    , {
      "function"
    }
    )
  }

  @Disabled("dpetrusca", "fix in phase 2")
  function testInterfaceIsShownInToplevel() {
    test(
      "//PROGRAM, pkg/MyProgram\n" +
      "  i^^\n"
    , {
      "interface"
    }
    )
  }

  function testPackageIsntShownInProgram() {
    testNoItems({
     "//PROGRAM, pkg/MyProgram\n" +
      "  p^^\n"
    }
    , {
      "package"
    }
    )
  }

  @Disabled("dpetrusca", "fix in phase 2")
  function testPropertyIsShownInToplevel() {
    test(
      "//PROGRAM, pkg/MyProgram\n" +
      "  p^^\n"
    , {
      "property"
    }
    )
  }

  @Disabled("dpetrusca", "fix in phase 2")
  function testUsesIsShownInToplevel() {
    test(
      "//PROGRAM, pkg/MyProgram\n" +
      "  u^^\n"
    , {
      "uses"
    }
    )
  }

  @Disabled("dpetrusca", "fix in phase 2")
  function testClassExtendsOrImplementsAreShownInClassDeclInProgram() {
    test(
      "//PROGRAM, pkg/MyProgram\n" +
      "class GosuClass ^^\n"
    , {
      "extends", "implements"
    }
    )
  }

  @Disabled("dpetrusca", "fix in phase 2")
  function testOtherKWsArentShownInClassDeclInProgram() {
    testNoItems({
     "//PROGRAM, pkg/MyProgram\n" +
      "class GosuClass ^^{}\n"
    }
    , {
      "function", "public", "static"
    }
    )
  }

}