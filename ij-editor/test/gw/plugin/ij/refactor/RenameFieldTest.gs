/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.refactor

uses com.intellij.codeInsight.TargetElementUtilBase
uses com.intellij.openapi.application.ApplicationManager
uses com.intellij.openapi.application.Result
uses com.intellij.openapi.command.WriteCommandAction
uses com.intellij.psi.PsiClass
uses com.intellij.psi.PsiFile
uses com.intellij.psi.impl.source.PsiFileImpl
uses com.intellij.refactoring.rename.RenameProcessor
uses gw.plugin.ij.framework.GosuTestCase
uses gw.plugin.ij.framework.generator.GosuTestingResource
uses gw.plugin.ij.framework.generator.ResourceFactory
uses gw.plugin.ij.lang.psi.impl.GosuClassFileImpl
uses gw.testharness.KnownBreak

uses java.lang.Runnable
uses java.util.ArrayList
uses java.util.HashMap
uses java.util.List

class RenameFieldTest extends GosuTestCase{

  function testRenameSimpleVariableFromDecl() {
    test("simpleRenamed",
      "package some.pkg\n" +
      "class BarImpl {\n" +
      "  function testFunc( bar: String ) {\n" +
      "    var [[simple]] = 10\n" +
      "    print ([[sim^^ple]])\n" +
      "  }\n"+
      "}"
    )
  }

  function testRenameFieldInDifferentPackageFromDecl() {
    test("fooRenamed",
    {
      "package some.pkg\n" +
      "uses other.pkg.FooClass\n" +
      "class GosuClass {\n" +
      "  function doit() {\n" +
      "    FooClass.[[test]]()\n"+
      "    }\n" +
      "  }\n" +
      "}",
      "package other.pkg\n" +
      "class FooClass {\n" +
      "  static function [[tes^^t]](){}\n"+
      "}"
    })
  }

  function testRenameFieldInDifferentPackageFromUsage() {
    test("fooRenamed",
    {
      "package other.pkg\n" +
      "class FooClass {\n" +
      "  static function [[test]](){}\n"+
      "}",
      "package some.pkg\n" +
      "uses other.pkg.FooClass\n" +
      "class GosuClass {\n" +
      "  function doit() {\n" +
      "    FooClass.[[te^^st]]()\n"+
      "    }\n" +
      "  }\n" +
      "}"
    })
  }

  function testRenameSimpleVariableFromUsage() {
    test("simpleRenamed",
      "package some.pkg\n" +
      "class BarImpl {\n" +
      "  function testFunc( bar: String ) {\n" +
      "    var [[sim^^ple]] = 10\n" +
      "    print ([[simple]])\n" +
      "  }\n" +
      "}"
    )
  }

  function testRenameForLoopVarsFromDecl() {
    test("simpleRenamed",
      "package some.pkg\n" +
      "class BarImpl {\n" +
      "  function method() {\n" +
      "    for ([[l^^var]] in 1..10){\n" +
      "      print([[lvar]])\n" +
      "    }\n" +
      "  }\n" +
      "}"
    )
  }

  function testRenameForLoopVarsFromUsage() {
    test("simpleRenamed",
      "package some.pkg\n" +
      "class BarImpl {\n" +
      "  function method() {\n" +
      "    for ([[lvar]] in 1..10){\n" +
      "      print([[l^^var]])\n" +
      "    }\n" +
      "  }\n" +
      "}"
    )
  }

  function testRenameCatchVarsFromDecl() {
    test("simpleRenamed",
      "package some.pkg\n" +
      "class BarImpl {\n" +
      "  function method() {\n" +
      "    try{\n" +
      "    }catch([[e^^x]]){\n" +
      "      throw [[ex]]\n" +
      "    }\n" +
      "  }\n" +
      "}"
    )
  }

  function testRenameCatchVarsFromUsage() {
    test("simpleRenamed",
      "package some.pkg\n" +
      "class BarImpl {\n" +
      "  function method() {\n" +
      "    try{\n" +
      "    }catch([[ex]]){\n" +
      "      throw [[e^^x]]\n" +
      "    }\n" +
      "  }\n" +
      "}"
    )
  }

  function testRenameUsingClauseArgumentListFromDecl() {
    test("simpleRenamed",
      "package some.pkg\n" +
      "uses java.io.FileWriter\n" +
      "uses java.io.FileReader\n\n" +
      "class GosuClass {\n" +
      "  function method() {\n" +
      "    using( var [[read^^er]] = new FileReader ( \"c:\\temp\\usingfun.txt\" ),\n" +
      "           var writer = new FileWriter ( \"c:\\temp\\usingfun2.txt\" ) ){\n" +
      "      var char = [[reader]].read()\n" +
      "      writer.write( char )\n" +
      "    }\n" +
      "  }\n" +
      "}"
    )
  }

  function testRenameUsingClauseArgumentListFromUsage() {
    test("simpleRenamed",
      "package some.pkg\n" +
      "uses java.io.FileWriter\n" +
      "uses java.io.FileReader\n\n" +
      "class GosuClass {\n" +
      "  function method() {\n" +
      "    using( var [[reader]] = new FileReader ( \"c:\\temp\\usingfun.txt\" ),\n" +
      "           var writer = new FileWriter ( \"c:\\temp\\usingfun2.txt\" ) ){\n" +
      "      var char = [[read^^er]].read()\n" +
      "      writer.write( char )\n" +
      "    }\n" +
      "  }\n" +
      "}"
    )
  }

  function testRenameParamVarsDeclInMethodFromDecl() {
    test("simpleRenamed",
      "package some.pkg\n" +
      "class BarImpl {\n" +
      "  function method([[^^arg1]] : int, arg2 : int) : int {\n" +
      "    return [[arg1]]\n" +
      "  }\n" +
      "}"
    )
  }

  function testRenameParamVarsDeclInMethodFromUsage() {
    test("simpleRenamed",
      "package some.pkg\n" +
      "class BarImpl {\n" +
      "  function method([[arg1]] : int, arg2 : int) : int {\n" +
      "    return [[^^arg1]]\n" +
      "  }\n" +
      "}"
    )
  }

  function testGotoParamVarDeclInPropertySetterFromDecl() {
    test("simpleRenamed",
      "package some.pkg\n" +
      "class BarImpl {\n" +
      "  var _x : int\n" +
      "  property set Prop( [[^^i]] : int ) {\n" +
      "    _x = [[i]]\n" +
      "  }\n" +
      "}"
    )
  }

  function testGotoParamVarDeclInPropertySetterFromUsage() {
    test("simpleRenamed",
      "package some.pkg\n" +
      "class BarImpl {\n" +
      "  var _x : int\n" +
      "  property set Prop( [[i]] : int ) {\n" +
      "    _x = [[^^i]]\n" +
      "  }\n" +
      "}"
    )
  }

  function testRenameBlockDeclThroughBlockInvocationFromDecl() {
    test("fooRenamed",
      "package some.pkg\n" +
      "class Bar {\n" +
      "  function testFunc( bar: String ) {\n" +
      "    var [[f^^oo]] = \\ x : Number, y : Number -> { return x + y }\n" +
      "    print([[foo]](2,3))\n" +
      "  }\n"+
      "}"
    )
  }

  function testRenameBlockDeclThroughBlockInvocationFromUsage() {
    test("fooRenamed",
      "package some.pkg\n" +
      "class Bar {\n" +
      "  function testFunc( bar: String ) {\n" +
      "    var [[foo]] = \\ x : Number, y : Number -> { return x + y }\n" +
      "    print([[f^^oo]](2,3))\n" +
      "  }\n"+
      "}"
    )
  }

  function testRenameLocalVarDeclInBlockFromDecl() {
    test("fooRenamed",
      "package some.pkg\n" +
      "class Bar {\n" +
      "  function method() {\n" +
      "    var x : List<String>\n" +
      "    x.each(\\ b -> {var [[loc^^al]] = \"qa\"; print(b + [[local]])})\n" +
      "  }\n"+
      "}"
    )
  }

  function testRenameLocalVarDeclInBlockFromUsage() {
    test("fooRenamed",
      "package some.pkg\n" +
      "class Bar {\n" +
      "  function method() {\n" +
      "    var x : List<String>\n" +
      "    x.each(\\ b -> {var [[local]] = \"qa\"; print(b + [[loc^^al]])})\n" +
      "  }\n"+
      "}"
    )
  }

  function testRenameGenericTypsParamVarsDeclInBlockFromDecl() {
    test("simpleRenamed",
      "package some.pkg\n" +
      "class BarImpl {\n" +
      "  function method() {\n" +
      "    var pods = {\"Web\", \"Studio\", \"BAT\"}\n" +
      "    pods.map(\\ [[po^^d]] -> [[pod]].length)" +
      "  }\n" +
      "}"
    )
  }

  function testRenameGenericTypsParamVarsDeclInBlockFromUsage() {
    test("simpleRenamed",
      "package some.pkg\n" +
      "class BarImpl {\n" +
      "  function method() {\n" +
      "    var pods = {\"Web\", \"Studio\", \"BAT\"}\n" +
      "    pods.map(\\ [[pod]] -> [[po^^d]].length)" +
      "  }\n" +
      "}"
    )
  }

  function testRenameParamVarsDeclInBlockWithImplicitCoercionReturnType () {
    test("simpleRenamed",
      "package some.pkg\n" +
      "class BarImpl {\n" +
      "  function method() {\n" +
      "    var myCache = new gw.util.concurrent.Cache<String, String>( \"My Uppercase Cache\", 100, \\ [[^^s]] -> [[s]].toUpperCase() )\n" +
      "  }\n" +
      "}"
    )
  }

  function testRenameParamVarsDeclInBlockWithImplicitCoercionReturnTypeUsage() {
    test("simpleRenamed",
      "package some.pkg\n" +
      "class BarImpl {\n" +
      "  function method() {\n" +
      "    var myCache = new gw.util.concurrent.Cache<String, String>( \"My Uppercase Cache\", 100, \\ [[s]] -> [[^^s]].toUpperCase() )\n" +
      "  }\n" +
      "}"
    )
  }

  function testRenameFieldViaThis() {
    test("fooRenamed",
      "package some.pkg\n" +
      "class Bar {\n" +
      "  var [[foo]] : int = 10\n" +
      "  function getXUsingThis() : block(int) { \n" +
      "    return \\ y : int -> { this.[[f^^oo]] = y }\n" +
      "  }\n"+
      "}"
    )
  }

  function testRenameFieldViaOuter() {
    test("fooRenamed",
      "package some.pkg\n" +
      "class Bar {\n" +
      "  var [[myVar]] : int = 10\n" +
      "  class Inner {\n" +
      "    var myVar = \"inner field\"\n" +
      "    function getSomething() : int {\n" +
      "      return outer.[[my^^Var]]\n" +
      "    }\n" +
      "  }\n" +
      "}"
    )
  }

  function testRenameFieldViaMapFromDecl() {
    test("fooRenamed",
      "package some.pkg\n" +
      "class Bar {\n" +
      "  var myVar : int = 10\n" +
      "  class Inner {\n" +
      "  var [[my^^Map]] = new  java.util.HashMap<String, String>(){ \"1\" -> \"one\", \"2\" -> \"two\" }\n\n" +
      "  function foo(){\n" +
      "    var s = [[myMap]][\"1\"]\n" +
      "  }\n" +
      "}"
    )
  }

  function testRenameFieldViaMapFromUsage() {
    test("fooRenamed",
      "package some.pkg\n" +
      "class Bar {\n" +
      "  var myVar : int = 10\n" +
      "  class Inner {\n" +
      "  var [[myMap]] = new  java.util.HashMap<String, String>(){ \"1\" -> \"one\", \"2\" -> \"two\" }\n\n" +
      "  function foo(){\n" +
      "    var s = [[my^^Map]][\"1\"]\n" +
      "  }\n" +
      "}"
    )
  }

  function testRenameInnerPrivateClassFieldFromUsage() {
    test("fooRenamed",
      "package some.pkg\n" +
      "class CanReferenceInnerPrivateMembersFromOuter {\n" +
      "  var _inner : Inner\n" +
      "  function accessPrivateInnerData() : String {\n" +
      "    return _inner.[[_private^^Data]]\n" +
      "  }\n" +
      "  class Inner {\n" +
      "    var [[_privateData]] : String\n\n" +
      "  }\n" +
      "}"
    )
  }

  function testRenameInnerPrivateClassFieldFromDecl() {
    test("fooRenamed",
      "package some.pkg\n" +
      "class CanReferenceInnerPrivateMembersFromOuter {\n" +
      "  var _inner : Inner\n" +
      "  function accessPrivateInnerData() : String {\n" +
      "    return _inner.[[_privateData]]\n" +
      "  }\n" +
      "  class Inner {\n" +
      "    var [[_private^^Data]] : String\n\n" +
      "  }\n" +
      "}"
    )
  }

  function testRenameFieldDeclInJAnonymousClassFromDecl() {
    test("fooRenamed",
      "package some.pkg\n" +
      "uses java.lang.Runnable\n" +
      "class GosuClass {\n" +
      "  function doit() {\n" +
      "    new Runnable(){\n" +
      "      var [[x]] = 5\n" +
      "      override function run(){\n" +
      "        [[^^x]] = 10\n" +
      "      }\n" +
      "    }.run()\n" +
      "  }\n" +
      "}"
    )
  }

  function testRenameFieldDeclInJAnonymousClassFromUsage() {
    test("fooRenamed",
      "package some.pkg\n" +
      "uses java.lang.Runnable\n" +
      "class GosuClass {\n" +
      "  function doit() {\n" +
      "    new Runnable(){\n" +
      "      var [[^^x]] = 5\n" +
      "      override function run(){\n" +
      "        [[x]] = 10\n" +
      "      }\n" +
      "    }.run()\n" +
      "  }\n" +
      "}"
    )
  }

  function testRenameFieldDeclInGAnonymousClassFromDecl() {
    test("fooRenamed",
    {
      "package some.pkg\n" +
      "class FooClass {\n" +
      "}",
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function doit() {\n" +
      "    var o = new FooClass(){\n" +
      "      var [[x]] = 5\n" +
      "      function bar(){\n" +
      "        [[^^x]] = 10\n" +
      "      }\n" +
      "    }\n" +
      "  }\n" +
      "}"
    })
  }

  function testRenameFieldDeclInGAnonymousClassFromUsage() {
    test("fooRenamed",
    {
      "package some.pkg\n" +
      "class FooClass {\n" +
      "}",
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function doit() {\n" +
      "    var o = new FooClass(){\n" +
      "      var [[^^x]] = 5\n" +
      "      function bar(){\n" +
      "        [[x]] = 10\n" +
      "      }\n" +
      "    }\n" +
      "  }\n" +
      "}"
    })
  }

  function testRenameClassTypeParamDeclFromDecl() {
    test("fooRenamed",
      "package some.pkg\n" +
      "uses java.lang.Integer\n\n" +
      "class GosuClass <[[^^T]] extends GosuClass<[[T]]>> { \n" +
      "  function fromNumber( p0: Integer ) : [[T]] { \n" +
      "    return null \n" +
      "  }\n\n" +
      "}"
    )
  }

  function testRenameClassTypeParamDeclFromUsage() {
    test("fooRenamed",
      "package some.pkg\n" +
      "uses java.lang.Integer\n\n" +
      "class GosuClass <[[T]] extends GosuClass<[[T]]>> { \n" +
      "  function fromNumber( p0: Integer ) : [[^^T]] { \n" +
      "    return null \n" +
      "  }\n\n" +
      "}"
    )
  }

  @KnownBreak("smckinney", "", "")
  function testRenameLocalVarDeclFromAnonymousClassReference() {
    test("fooRenamed",
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function testOuter() : GosuClass {\n" +
      "    var [[foo]] : GosuClass = null\n" +
      "    new java.lang.Runnable() {\n" +
      "      override function run() {\n" +
      "        [[f^^oo]] = outer\n" +
      "      }\n" +
      "    }.run()\n" +
      "    return [[foo]]\n" +
      "  }\n" +
      "}"
    )
  }

  @KnownBreak("smckinney", "", "")
  function testRenameLocalVarDeclWithItsValueAssignedInAnonymousClass() {
    test("fooRenamed",
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function testOuter() : GosuClass {\n" +
      "    var [[f^^oo]] : GosuClass = null\n" +
      "    new java.lang.Runnable() {\n" +
      "      override function run() {\n" +
      "        [[foo]] = outer\n" +
      "      }\n" +
      "    }.run()\n" +
      "    return [[foo]]\n" +
      "  }\n" +
      "}"
    )
  }

  @KnownBreak("smckinney", "", "")
  function testRenameLocalVarDeclAndAnonymousClassReferenceFromReturn() {
    test("fooRenamed",
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function testOuter() : GosuClass {\n" +
      "    var [[foo]] : GosuClass = null\n" +
      "    new java.lang.Runnable() {\n" +
      "      override function run() {\n" +
      "        [[foo]] = outer\n" +
      "      }\n" +
      "    }.run()\n" +
      "    return [[f^^oo]]\n" +
      "  }\n" +
      "}"
    )
  }

  @KnownBreak("smckinney", "", "")
  function testRenameArrayDeclFromDecl() {
    test("fooRenamed",
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var [[my^^Strings]] = new String[][] {{\"Mr. \", \"Mrs. \", \"Ms. \"}, {\"Smith\", \"Jones\"}}\n" +
      "  function foo(){\n" +
      "    var s = [[myStrings]][0][0]\n" +
      "  }"
    )
  }

  @KnownBreak("smckinney", "", "")
  function testRenameArrayDeclFromUsage() {
    test("fooRenamed",
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var [[myStrings]] = new String[][] {{\"Mr. \", \"Mrs. \", \"Ms. \"}, {\"Smith\", \"Jones\"}}\n" +
      "  function foo(){\n" +
      "    var s = [[my^^Strings]][0][0]\n" +
      "  }"
    )
  }

  @KnownBreak("smckinney", "", "")
  function testRenameArrayListFromDecl() {
    test("fooRenamed",
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var [[my^^Strings]] = {{\"Mr. \", \"Mrs. \", \"Ms. \"},\n" +
      "                            {\"Smith\", \"Jones\"}}\n" +
      "  function foo(){\n" +
      "    var s = [[myStrings]][0][0]\n" +
      "  }"
    )
  }

  @KnownBreak("smckinney", "", "")
  function testRenameArrayListFromUsage() {
    test("fooRenamed",
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var [[myStrings]] = {{\"Mr. \", \"Mrs. \", \"Ms. \"},\n" +
      "                            {\"Smith\", \"Jones\"}}\n" +
      "  function foo(){\n" +
      "    var s = [[my^^Strings]][0][0]\n" +
      "  }"
    )
  }

  @KnownBreak("smckinney", "", "")
  function testRenameJavaArrayListFromDecl() {
    test("fooRenamed",
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var [[f^^oo]] = new java.util.ArrayList<String> ({\"Buenos Aires\", \"Córdoba\", \"La Plata\"})\n" +
      "  function foo(){\n" +
      "    var s = [[foo]][0]\n" +
      "  }\n" +
      "}"
    )
  }

  @KnownBreak("smckinney", "", "")
  function testRenameArrayJavaListFromUsage() {
    test("fooRenamed",
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var [[foo]] = new java.util.ArrayList<String> ({\"Buenos Aires\", \"Córdoba\", \"La Plata\"})\n" +
      "  function foo(){\n" +
      "    var s = [[f^^oo]][0]\n" +
      "  }" +
      "}"
    )
  }

  @KnownBreak("smckinney", "", "")
  function testRenameMapFromDecl() {
    test("fooRenamed",
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var [[my^^Map]] = new  java.util.HashMap<String, String>(){ \"1\" -> \"one\", \"2\" -> \"two\" }\n\n" +
      "  function foo(){\n" +
      "    var s = [[myMap]].get(\"1\")\n" +
      "  }" +
      "}"
    )
  }

  @KnownBreak("smckinney", "", "")
  function testRenameMapFromUsage() {
    test("fooRenamed",
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var [[myMap]] = new  java.util.HashMap<String, String>(){ \"1\" -> \"one\", \"2\" -> \"two\" }\n\n" +
      "  function foo(){\n" +
      "    var s = [[my^^Map]].get(\"1\")\n" +
      "  }" +
      "}"
    )
  }

  @KnownBreak("smckinney", "", "")
  function testRenamePropFieldInConstrFromDecl() {
    test("fooRenamed",
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var _propField : String as [[Prop^^Field]]\n" +
      "  construct(s : String) {\n" +
      "    [[PropField]] = s\n" +
      "  }" +
      "}"
    )
  }

  @KnownBreak("smckinney", "", "")
  function testRenamePropFieldInConstrFromUsage() {
    test("fooRenamed",
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var _propField : String as [[PropField]]\n" +
      "  construct(s : String) {\n" +
      "    [[Prop^^Field]] = s\n" +
      "  }\n" +
      "}"
    )
  }

  function testRenameLocalVarDeclInConstructFromDecl() {
    test("fooRenamed",
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  construct() {\n" +
      "    var [[loc^^al]] : int\n" +
      "    [[local]] = 10\n" +
      "  }\n" +
      "}"
    )
  }

  function testRenameLocalVarDeclInConstructFromUsage() {
    test("fooRenamed",
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  construct() {\n" +
      "    var [[local]] : int\n" +
      "    [[loc^^al]] = 10\n" +
      "  }\n" +
      "}"
    )
  }

  @KnownBreak("smckinney", "", "")
  function testRenameParamVarDeclInConstructorFromDecl() {
    test("fooRenamed",
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var _x : int\n" +
      "  construct([[lo^^cal]] : int) {\n" +
      "    _x = [[local]]\n" +
      "  }\n" +
      "}"
    )
  }

  @KnownBreak("smckinney", "", "")
  function testRenameParamVarDeclInConstructorFromUsage() {
    test("fooRenamed",
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var _x : int\n" +
      "  construct([[local]] : int) {\n" +
      "    _x = [[loc^^al]]\n" +
      "  }\n" +
      "}"
    )
  }

  @KnownBreak("smckinney", "", "")
  function testRenamePropertyInMethodFromDecl() {
    test("fooRenamed",
      "package some.pkg\n" +
      "class Bar {\n" +
      "  var _local : String as [[Prop]]\n" +
      "  function getXUsingThis() { \n" +
      "    var s = [[Pro^^p]]\n" +
      "  }\n"+
      "}"
    )
  }

  @KnownBreak("smckinney", "", "")
  function testRenamePropertyInMethodFromUsage() {
    test("fooRenamed",
      "package some.pkg\n" +
      "class Bar {\n" +
      "  var _local : String as [[Pro^^p]]\n" +
      "  function getXUsingThis() { \n" +
      "    var s = [[Prop]]\n" +
      "  }\n"+
      "}"
    )
  }

  @KnownBreak("smckinney", "", "")
  function testRenamePropertyViaSuperDecl() {
    test("fooRenamed",
    {
      "package some.pkg\n" +
      "class GosuClass extends SupGosuClass {\n" +
      "  function foo() {\n" +
      "    var s = super.[[Prop]]\n" +
      "    super.[[Prop]] = \"\"\n" +
      "  }\n"  +
      "}",
      "package some.pkg\n" +
      "class SupGosuClass {\n" +
      "  var _local : String as [[Pr^^op]]\n" +
      "}"
    })
  }

  @KnownBreak("smckinney", "", "")
  function testRenamePropertyViaUsage() {
    test("fooRenamed",
    {
      "package some.pkg\n" +
      "class SupGosuClass {\n" +
      "  var _local : String as [[Prop]]\n" +
      "}",
      "package some.pkg\n" +
      "class GosuClass extends SupGosuClass {\n" +
      "  function foo() {\n" +
      "    var s = super.[[Pro^^p]]\n" +
      "    super.[[Prop]] = \"\"\n" +
      "  }\n"  +
      "}"
    })
  }

  @KnownBreak("smckinney", "", "")
  function testRenamePropertyInDifferentPackageViaUsage() {
    test("fooRenamed",
    {
      "package some.pkg\n" +
      "class SupGosuClass {\n" +
      "  var _local : String as [[Prop]]\n" +
      "}",
      "package other.pkg\n" +
      "uses some.pkg.SupGosuClass\n" +
      "class GosuClass extends SupGosuClass {\n" +
      "  function foo() {\n" +
      "    var s = super.[[Pro^^p]]\n" +
      "    super.[[Prop]] = \"\"\n" +
      "  }\n"  +
      "}"
    })
  }

  function testRenameLocalVarDeclInPropertyGetterFromDecl() {
    test("fooRenamed",
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  property get Prop() : int{\n" +
      "    var [[lo^^cal]] = 10 \n" +
      "    return [[local]]\n" +
      "  }\n"  +
      "}"
    )
  }

  function testRenameLocalVarDeclInPropertyGetterFromUsage() {
    test("fooRenamed",
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  property get Prop() : int{\n" +
      "    var [[local]] = 10 \n" +
      "    return [[lo^^cal]]\n" +
      "  }\n"  +
      "}"
    )
  }

  @KnownBreak("smckinney", "", "")
  function testRenameLocalVarDeclInPropertySetterFromDecl() {
    test("fooRenamed",
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var _x : int\n" +
      "  property set Prop( i : int ) {\n" +
      "    var [[lo^^cal]] = 10 \n" +
      "    [[local]] = 11 \n" +
      "    _x = i\n" +
      "  }\n"  +
      "}"
    )
  }

  @KnownBreak("smckinney", "", "")
  function testRenameLocalVarDeclInPropertySetterFromUsage() {
    test("fooRenamed",
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var _x : int\n" +
      "  property set Prop( i : int ) {\n" +
      "    var [[local]] = 10 \n" +
      "    [[lo^^cal]] = 11 \n" +
      "    _x = i\n" +
      "  }\n"  +
      "}"
    )
  }

  @KnownBreak("smckinney", "", "")
  function testRenameEnumFromDecl() {
    test("fooRenamed",
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var y: TestEnum = [[Fir^^st]] \n" +
      "  enum TestEnum { \n" +
      "    [[First]], Second \n" +
      "  }" +
      "}"
    )
  }

  @KnownBreak("smckinney", "", "")
  function testRenameEnumFromUsage() {
    test("fooRenamed",
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var y: TestEnum = [[First]] \n" +
      "  enum TestEnum { \n" +
      "    [[Fir^^st]], Second \n" +
      "  }" +
      "}"
    )
  }

  // Re-enable after templates are fully supported by plugin again
  @KnownBreak("smckinney", "", "")
  function testRenameVariablesInTemplateFromDecl() {
    test("yFoo",
    {
      "//TEMPLATE, other.pkg/MyTemplate \n" +
      "\<%@ extends some.pkg.FooClass%>\n" +
      "\<%var x = new some.pkg.FooClass() \n" +
      "print([[xFoo]])%>\n",
      "package some.pkg\n" +
      "class FooClass {\n" +
      "  static var [[^^xFoo]] = 20\n" +
      "  function test(){\n" +
      "    print(\"test\")\n" +
      "  }\n" +
      "}"
    })
  }

  // Re-enable after templates are fully supported by plugin again
  @KnownBreak("smckinney", "", "")
  function testRenameVariablesInTemplateFromDecl1() {
    test("yFoo",
      "//TEMPLATE, other.pkg/MyTemplate \n" +
      "\<%@ extends some.pkg.FooClass%>\n" +
      "\<%var x = new some.pkg.FooClass() \n" +
      "var [[myVar]] = 2 \n" +
      "class Inner {\n" +
      "  var myVar = 2\n" +
      "  function getSomething() : int {\n" +
      "    return outer.[[my^^Var]]\n" +
      "  }\n" +
      "}%>\n"
    )
  }

  // Re-enable after templates are fully supported by plugin again
  @KnownBreak("smckinney", "", "")
  function testRenamePropInTemplateFromDecl() {
    test("yFoo",
    {
      "//TEMPLATE, other.pkg/MyTemplate \n" +
      "\<%@ extends some.pkg.FooClass%>\n" +
      "\<%var x = new some.pkg.FooClass() \n" +
      "  function foo() {\n" +
      "    var s = super.[[Prop]]\n" +
      "    super.[[Prop]] = \"\"\n" +
      "  }\n" +
      "}%>\n",
      "package some.pkg\n" +
      "class FooClass {\n" +
      "  var _local : String as [[Pro^^p]]\n" +
      "}"
    })
  }

  // Re-enable after templates are fully supported by plugin again
  @KnownBreak("smckinney", "", "")
  function testRenamePropInTemplateFromUsage() {
    test("yFoo",
    {
      "package some.pkg\n" +
      "class FooClass {\n" +
      "  var _local : String as [[Prop]]\n" +
      "}",
      "//TEMPLATE, other.pkg/MyTemplate \n" +
      "\<%@ extends some.pkg.FooClass%>\n" +
      "\<%var x = new some.pkg.FooClass() \n" +
      "  function foo() {\n" +
      "    var s = super.[[Pro^^p]]\n" +
      "    super.[[Prop]] = \"\"\n" +
      "  }\n" +
      "}%>\n"
    })
  }

  function test(newName: String, text: String) {
    test(newName, {text})
  }

  function test(newName: String, texts: List<String>) {
    testImpl(newName, texts.map(\elt -> ResourceFactory.create(elt)))
  }

  function testImpl(newName: String, files: List<GosuTestingResource>) {

    var psiFiles = new ArrayList<PsiFile>()
    for (f in files) {
      psiFiles.add(configureByText(f.fileName, f.content))
    }

    var caretOffset = getMarkers(psiFiles.last()).getCaretOffset();
    var flags = TargetElementUtilBase.getInstance().getReferenceSearchFlags()
    var elementAt = TargetElementUtilBase.getInstance().findTargetElement(getCurrentEditor(), flags, caretOffset)
    assertNotNull("Target element not found.", elementAt)

    var fileToClasses = new HashMap<PsiFile, PsiClass[]>()
    var gsClassFile = (psiFiles.last() as PsiFileImpl).calcTreeElement().Psi as GosuClassFileImpl

    fileToClasses.put( gsClassFile, {gsClassFile.getClasses()[0]} )

    new WriteCommandAction( psiFiles.last().Project, {} )
    {
    override function run( result: Result )
    {
      var processor = new RenameProcessor(getProject(), elementAt, newName, false, false )
      processor.setPreviewUsages( false )
      processor.run()
      gsClassFile.reparseGosuFromPsi()
    }
    }.execute()

    ApplicationManager.getApplication().runReadAction(
      new Runnable() {
        function run() {

          for( i in 0..|psiFiles.size() ) {
            var psiFile = psiFiles.get(i)
            var actual = psiFile.Text

            var expectedText = files.get(i).content.toString().replaceAll("\\^\\^","")
            expectedText=expectedText.replaceAll("\\[\\[\\w+\\]\\]", newName)     //find all [[]] and replace with new name

            // Have to remove spaces b/c refactoring will apply formatting, which can put spaces before/after type names eg., ctor calls
            assertEquals(expectedText.remove(" "), actual.remove(" "))
          }
        }
      }
    )
  }

}