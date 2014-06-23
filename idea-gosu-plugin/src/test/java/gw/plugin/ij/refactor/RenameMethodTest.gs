/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.refactor

uses com.intellij.codeInsight.TargetElementUtilBase
uses com.intellij.openapi.application.ApplicationManager
uses com.intellij.openapi.application.Result
uses com.intellij.openapi.command.WriteCommandAction
uses com.intellij.psi.PsiFile
uses com.intellij.psi.impl.source.PsiFileImpl
uses com.intellij.refactoring.rename.RenameProcessor
uses gw.lang.reflect.gs.IGosuClass
uses gw.plugin.ij.framework.GosuTestCase
uses gw.plugin.ij.framework.generator.GosuTestingResource
uses gw.plugin.ij.framework.generator.ResourceFactory
uses gw.plugin.ij.lang.psi.impl.GosuClassFileImpl
uses gw.plugin.ij.lang.psi.impl.expressions.GosuReferenceExpressionImpl
uses gw.testharness.Disabled

uses java.lang.Runnable
uses java.util.ArrayList
uses java.util.List

class RenameMethodTest extends GosuTestCase {

  function testSimpleMethodRenameFromDecl() {
    test("fooRenamed",
      "package some.pkg\n" +
      "class BarImpl {\n" +
      "  var a: String\n" +
      "  function [[f^^oo]]( bar: String ) {\n" +
      "    a = bar\n" +
      "  }\n" +
      "  function two() {\n" +
      "    [[foo]](\"test\")\n" +
      "  }\n" +
      "}"
    )
  }

  function testRenameFunctionUsage() {
    test("fooRenamed",
      "package some.pkg\n" +
      "class BarImpl {\n" +
      "  function [[meth^^od]]() : int {\n" +
      "    return 0\n" +
      "  }\n" +
      "  function foo() : int {\n" +
      "    return [[method]]()\n" +
      "  }"+
      "}"
    )
  }

  //instance method
  function testRenameFunctionFromDecl() {
    test("renamedMethod1",
      "package some.pkg\n" +
      "class BarClass {\n" +
      "  function [[meth^^od]]() : int {\n" +
      "    return 0\n" +
      "  }\n" +
      "  function foo() : int {\n" +
      "    return [[method]]()\n" +
      "  }"+
      "}"
    )
  }

  function testRenameFunctionFromUsage() {
    test("renamedMethod2",
      "package some.pkg\n" +
      "class BarClass {\n" +
      "  function [[method]]() : int {\n" +
      "    return 0\n" +
      "  }\n" +
      "  function foo() : int {\n" +
      "    return [[meth^^od]]()\n" +
      "  }"+
      "}"
    )
  }

  //Static method
  function testRenameStaticFunctionFromDecl() {
    test("renamedMethod3",
      "package some.pkg\n" +
      "class BarClass {\n" +
      "  static function [[meth^^od]]() : int {\n" +
      "    return 0\n" +
      "  }\n" +
      "  function foo() : int {\n" +
      "    return [[method]]()\n" +
      "  }"+
      "}"
    )
  }

  function testRenameStaticFunctionFromUsage() {
    test("renamedMethod",
      "package some.pkg\n" +
      "class BarClass {\n" +
      "  static function [[method]]() : int {\n" +
      "    return 0\n" +
      "  }\n" +
      "  function foo() : int {\n" +
      "    return [[meth^^od]]()\n" +
      "  }"+
      "}"
    )
  }

  function testRenameStaticFunctionInDifferentPackageFromDecl() {
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

  function testRenameStaticFunctionDifferentPackageFromUsage() {
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

  function testSimpleMethodRenameFromUsage() {
    test("fooRenamed",
    {
      "package some.pkg\n" +
      "class BarImpl {\n" +
      "  var a: String\n" +
      "  function [[foo]]( bar: String ) {\n" +
      "    a = bar\n" +
      "  }\n" +
      "  function two() {\n" +
      "    [[^^foo]](\"test\")\n" +
      "  }\n" +
      "}"
    })
  }

  @Disabled("dpetrusca", "functionality works but the test breaks")
  function testRenameMethodInInterface() {
    test("fooRenamed",
    {
      "package some.pkg\n" +
      "class BarImpl implements IFoo {\n" +
      "  var _bar: String\n" +
      "  override function [[foo]]() : String {\n" +
      "    return _bar\n" +
      "  }\n" +
      "}",
      "package some.pkg\n" +
      "interface IFoo {\n" +
      "  function [[f^^oo]]() : String\n" +
      "}"
    });
  }

  @Disabled("dpetrusca", "functionality works but the test breaks")
  function testRenameMethodInDeclRenamesInInterface() {
    test("fooRenamed4",
    {
      "package some.pkg\n" +
      "interface IFoo {\n" +
      "  function [[foo]]() : String\n" +
      "}",
      "package some.pkg\n" +
      "class BarImpl implements IFoo {\n" +
      "  var _bar: String\n" +
      "  override function [[fo^^o]]() : String {\n" +
      "    return _bar\n" +
      "  }\n" +
      "}"
    });
  }

  function testRenameMethodFromUsageInInnerClass() {
    test("testRenamed5",
    {
      "package some.pkg\n" +
      "class Bar {\n" +
      "  public function testInner(){\n" +
      "    var test = new Foo()\n" +
      "    test.[[testInner]]()\n" +
      "  }\n" +
      "  class Foo {\n" +
      "    public function [[test^^Inner]](){\n" +
      "    }\n" +
      "  }\n" +
      "}\n"
    });
  }

  function testRenameMethodFromDeclRenamesItInInnerClass() {
    test("testRenamed1",
    {
      "package some.pkg\n" +
      "class Foo {\n" +
      "  public function [[testInner]](){\n" +
      "  }\n" +
      "}\n",
      "package some.pkg\n" +
      "class Bar {\n" +
      "  public function testInner(){\n" +
      "    var test = new Foo()\n" +
      "    test.[[test^^Inner]]()\n" +
      "  }\n"+
      "}"
    });
  }

  function testRenameMethodRenamesItInAnonClass() {
    test("testRenamed2",
    {
      "package some.pkg\n" +
      "class Bar {\n" +
      "  function [[test^^Inner]](){\n" +
      "  }\n"+
      "}\n",
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function doit() {\n" +
      "    var o = new Bar(){\n" +
      "      var x = new Bar()\n" +
      "      function bar(){\n" +
      "        x.[[test^^Inner]]()\n" +
      "      }\n" +
      "    }\n" +
      "  }\n" +
      "}"
    })
  }

  function testRenameMethodFromAnonClass() {
    test("testRenamed3",
    {
      "package some.pkg\n" +
      "class Bar {\n" +
      "  function [[testInner]](){\n" +
      "  }\n"+
      "}\n",
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function doit() {\n" +
      "    var o = new Bar(){\n" +
      "      var x = new Bar()\n" +
      "      function bar(){\n" +
      "        x.[[test^^Inner]]()\n" +
      "      }\n" +
      "    }\n" +
      "  }\n" +
      "}"
    })
  }

  function testRenameOverLoadedMethod() {
    test("overLoadRenamed",
    {
      "package some.pkg\n" +
      "class BarExt {\n" +
      "  public function [[overLoad]](one:String){\n" +
      "    print(one)\n" +
      "  }\n" +
      "  public function overLoad(one:String, two:String){\n" +
      "    print(one + two)\n" +
      "  }\n" +
      "}",
      "package some.pkg\n" +
      "class Bar {\n" +
      "  public function testInner(){\n" +
      "    var test = new BarExt()\n"+
      "    test.[[over^^Load]](\"one\")\n"+
      "  }\n"+
      "}\n"
    });
  }

  function testRenameOverLoadedMethodFromImpl() {
    test("overLoadRenamed2",
    {
      "package some.pkg\n" +
      "class Bar {\n" +
      "  public function testInner(){\n" +
      "    var test = new BarExt()\n"+
      "    test.[[over^^Load]](\"one\")\n"+
      "  }\n"+
      "}\n",
      "package some.pkg\n" +
      "class BarExt {\n" +
      "  public function [[over^^Load]](one:String){\n" +
      "    print(one)\n" +
      "  }\n" +
      "  public function overLoad(one:String, two:String){\n" +
      "    print(one + two)\n" +
      "  }\n" +
      "}"
    });
  }

  function testRenameClassNameFromOuter() {
    test("FooRenamed5",
    {
      "package some.pkg\n" +
      "class FooClass {\n" +
      "  function [[some^^thing]]() : String {\n" +
      "    return \"something\"\n" +
      "  }\n" +
      "  class Inner {\n" +
      "    function refOuter() : String {\n" +
      "      return outer.[[something]]()\n" +
      "    }\n" +
      "  }\n" +
      "}"
    })
  }

  function testRenameClassNameFromUsage() {
    test("FooRenamed6",
    {
      "package some.pkg\n" +
      "class FooClass {\n" +
      "  function [[something]]() : String {\n" +
      "    return \"something\"\n" +
      "  }\n" +
      "  class Inner {\n" +
      "    function refOuter() : String {\n" +
      "      return outer.[[some^^thing]]()\n" +
      "    }\n" +
      "  }\n" +
      "}"
    })
  }

  function testRenameMethodDeclInSuperClassFromEnhancement() {
    test("FooRenamed7",
    {
      "package some.pkg\n" +
      "class GosuClass extends SuperGosuClass {\n" +
      "}",
      "package some.pkg\n" +
      "uses some.pkg.GosuClass\n"+
      "enhancement GosuClassEnhancement : GosuClass {\n" +
      "  function foo() {\n" +
      "    this.[[bar]]()\n" +
      "  }\n" +
      "}",
      "package some.pkg\n" +
      "class SuperGosuClass {\n" +
      "  function [[b^^ar]]() {\n" +
      "  }\n" +
      "}"
    })
  }

  function testRenameSuperClassMethodInEnhancement() {
    test("FooRenamed8",
    {
      "package some.pkg\n" +
      "class GosuClass extends SuperGosuClass {\n" +
      "}",
      "package some.pkg\n" +
      "uses some.pkg.GosuClass\n"+
      "enhancement GosuClassEnhancement : GosuClass {\n" +
      "  function foo() {\n" +
      "    this.[[bar]]()\n" +
      "  }\n" +
      "}",
      "package some.pkg\n" +
      "class SuperGosuClass {\n" +
      "  function [[ba^^r]]() {\n" +
      "  }\n" +
      "}"
    })
  }

//  Cannot create Java files using current test fixtures
//  function testRenameMethodDeclInJSuperClassFromEnhancement() {
//    test("FooRenamed9",
//    {
//      "package some.pkg\n" +
//      "class GosuClass extends SuperJavaClass {\n" +
//      "}",
//      "package some.pkg\n" +
//      "some.pkg.GosuClass\n"+
//      "enhancement GosuClassEnhancement : GosuClass {\n" +
//      "  function foo() {\n" +
//      "    this.[[bar]]()\n" +
//      "  }\n" +
//      "}",
//      "//JAVA\n" +
//      "package some.pkg;\n" +
//      "class SuperJavaClass {\n" +
//      "  public void [[^^bar]]() {\n\n" +
//      "  }\n" +
//      "}"
//    })
//  }
//
//  function testRenameJSuperClassMethodInInEnhancement() {
//    test("FooRenamed10",
//    {
//      "package some.pkg\n" +
//      "class GosuClass extends SuperJavaClass {\n" +
//      "}",
//      "//JAVA\n" +
//      "package some.pkg;\n" +
//      "class SuperJavaClass {\n" +
//      "  public void [[bar]]() {\n\n" +
//      "  }\n" +
//      "}",
//      "package some.pkg\n" +
//      "some.pkg.GosuClass\n"+
//      "enhancement GosuClassEnhancement : GosuClass {\n" +
//      "  function foo() {\n" +
//      "    this.[[b^^ar]]()\n" +
//      "  }\n" +
//      "}"
//    })
//  }

  function testRenameOuterMethodFromMethodCall() {
    test("FooRenamed11",
    {
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function [[fooFunc]]() : String {\n" +
      "    return \"something\"\n" +
      "  }\n"+
      "  class Inner {\n" +
      "    function getSomething() : String {\n" +
      "      return [[foo^^Func]]()\n" +
      "    }\n" +
      "  }\n"+
      "}"
    })
  }

  function testRenameOuterMethodFromDecl() {
    test("FooRenamed12",
    {
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function [[foo^^Func]]() : String {\n" +
      "    return \"something\"\n" +
      "  }\n"+
      "  class Inner {\n" +
      "    function getSomething() : String {\n" +
      "      return [[fooFunc]]()\n" +
      "    }\n" +
      "  }\n"+
      "}"
    })
  }

  function testRenameInnerMethodFromDecl() {
    test("FooRenamed13",
    {
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function something() : String {\n" +
      "    return \"something\"\n" +
      "  }\n" +
      "  class Inner {\n" +
      "    function getSomething() : String {\n" +
      "      return [[something]]()\n" +
      "    }\n" +
      "    function [[some^^thing]]() : String {\n" +
      "      return \"inner something\"\n" +
      "    }\n" +
      "  }\n" +
      "}"
    })
  }

  function testRenameInnerMethodFromMethodCall() {
    test("FooRenamed14",
    {
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function something() : String {\n" +
      "    return \"something\"\n" +
      "  }\n" +
      "  class Inner {\n" +
      "    function getSomething() : String {\n" +
      "      return [[some^^thing]]()\n" +
      "    }\n" +
      "    function [[something]]() : String {\n" +
      "      return \"inner something\"\n" +
      "    }\n" +
      "  }\n"+
      "}"
    })
  }

  function testRenameInnerPrivateFunctionFromOuter() {
    test("innerRenamed",
    {
      "package some.pkg\n" +
      "class CanReferenceInnerPrivateMembersFromOuter {\n" +
      "  var _inner : Inner\n" +
      "  function accessPrivateInnerFunction() : String {\n" +
      "    return _inner.[[private^^Function]]()\n" +
      "  }\n" +
      "  class Inner {\n" +
      "    private function [[privateFunction]]() : String {\n" +
      "      return \"privateFunction\"\n" +
      "    }\n" +
      "  }\n" +
      "}"
    }
    )
  }

  function testRenameInnerPrivateFunctionFromDecl() {
    test("FooRenamed",
    {
      "package some.pkg\n" +
      "class CanReferenceInnerPrivateMembersFromOuter {\n" +
      "  var _inner : Inner\n" +
      "  function accessPrivateInnerFunction() : String {\n" +
      "    return _inner.[[privateFunction]]()\n" +
      "  }\n" +
      "  class Inner {\n" +
      "    private function [[private^^Function]]() : String {\n" +
      "      return \"privateFunction\"\n" +
      "    }\n" +
      "  }\n" +
      "}"

    })
  }

  function testRenameOuterMethodFromMethodCallUsingOuterKeyword() {
    test("FooRenamed15",
    {
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function [[something]]() : String {\n" +
      "    return \"something\"\n" +
      "  }\n" +
      "  class Inner {\n" +
      "    function getSomething() : String {\n" +
      "      return outer.[[some^^thing]]()\n" +
      "    }\n" +
      "    function something() : String {\n" +
      "      return \"inner something\"\n" +
      "    }\n" +
      "  }\n" +
      "}"
    })
  }

  function testRenameOuterMethodFromDeclUsingOuterKeyword() {
    test("FooRenamed16",
    {
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function [[some^^thing]]() : String {\n" +
      "    return \"something\"\n" +
      "  }\n" +
      "  class Inner {\n" +
      "    function getSomething() : String {\n" +
      "      return outer.[[something]]()\n" +
      "    }\n" +
      "    function something() : String {\n" +
      "      return \"inner something\"\n" +
      "    }\n" +
      "  }\n" +
      "}"
    })
  }

  function testRenameDefaultParamFunctionFromUsage() {
    test("RenamedDefParams",
    {
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function [[withDefaultParams]](x: int, y: String = \"default\", z: String = \"default\") {\n" +
      "  }\n" +
      "  function test(){\n" +
      "    [[withDefault^^Params]](:z = \"z\", :x = 1)\n" +
      "  }\n" +
      "}"
    })
  }

  function testRenameDefaultParamFunctionFromDecl() {
    test("RenamedDefParams2",
    {
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function [[withDefault^^Params]](x: int, y: String = \"default\", z: String = \"default\") {\n" +
      "  }\n" +
      "  function test(){\n" +
      "    [[withDefaultParams]](:z = \"z\", :x = 1)\n" +
      "  }\n" +
      "}"
    })
  }

//  Cannot create Java files using current test fixtures
//  function testRenameJConstInSuper() {
//    test("RenameJConstr",
//    {
//      "package some.pkg;\n" +
//      "class SupJavaClass {\n" +
//      "    public [[SupJavaClass]](String s) {}" +
//      "}",
//      "package some.pkg\n" +
//      "class GosuClass extends SupJavaClass {\n" +
//      "  construct(s:String) {sup^^er(s)}\n" +
//      "}"
//    })
//  }

  //method with param's type as Type
  @Disabled("dpetrusca", "functionality works but the test breaks")
  function testRenameMethodDeclWithTypeAsParamTypeFromDecl() {
    test("renameParamMethod",
      "package some.pkg;\n" +
      "class BarClass {\n" +
      "  function bar() {\n" +
      "    [[meth^^od]](typeof \"123\")\n" +
      "  }\n" +
      "  function [[method]](type : Type) {\n" +
      "  }\n"+
      "}"
    )
  }

  @Disabled("dpetrusca", "functionality works but the test breaks")
  function testRenameMethodDeclWithTypeAsParamTypeFromUsage() {
    test("renameParamMethod2",
      "package some.pkg;\n" +
      "class BarClass {\n" +
      "  function bar() {\n" +
      "    [[method]](typeof \"123\")\n" +
      "  }\n" +
      "  function [[meth^^od]](type : Type) {\n" +
      "  }\n"+
      "}"
    )
  }

  function testRenameMethodDeclWithGenericDefaultTypeFromDecl() {
    test("renameGenericMethod1",
      "package some.pkg;\n" +
      "class BarClass {\n" +
      "  function bar() {\n" +
      "    [[method]](null)\n" +
      "  }\n" +
      "  function [[meth^^od]]<T>(type : Type<T>) {\n" +
      "  }\n"+
      "}"
    )
  }

  function testRenameMethodDeclWithGenericDefaultTypeFromUsage() {
    test("renameGenericMethod2",
      "package some.pkg;\n" +
      "class BarClass {\n" +
      "  function bar() {\n" +
      "    [[meth^^od]](null)\n" +
      "  }\n" +
      "  function [[method]]<T>(type : Type<T>) {\n" +
      "  }\n"+
      "}"
    )
  }

  //method with param's type as List<T>
  function testRenameMethodDeclWithListAsParamTypeFromDecl() {
    test("renameListParam1",
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function [[my^^F]](texts : List<String>) {}\n" +
      "  function test(){\n" +
      "    [[myF]]({\"123\", \"456\"})\n" +
      "  }\n" +
      "}"
    )
  }

  function testRenameMethodDeclWithListAsParamTypeFromUsage() {
    test("renameListParam2",
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function [[myF]](texts : List<String>) {}\n" +
      "  function test(){\n" +
      "    [[my^^F]]({\"123\", \"456\"})\n" +
      "  }\n" +
      "}"
    )
  }

  //method with param's type as Map<K, V>
  function testRenameMethodDeclWithMapAsParamTypeFromDecl() {
    test("renameMapParam1",
      "package some.pkg\n" +
      "uses java.util.Map\n" +
      "uses java.util.HashMap\n" +
      "class GosuClass {\n" +
      "  function [[my^^F]](myMap : Map<String, String>) {}\n" +
      "  function test(){\n" +
      "    [[myF]](new  java.util.HashMap<String, String>(){ \"1\" -> \"Monday\" })\n" +
      "  }\n" +
      "}"
    )
  }

  function testRenameMethodDeclWithMapAsParamTypeFromUsage() {
    test("renameMapParam2",
      "package some.pkg\n" +
      "uses java.util.Map\n" +
      "uses java.util.HashMap\n" +
      "class GosuClass {\n" +
      "  function [[myF]](myMap : Map<String, String>) {}\n" +
      "  function test(){\n" +
      "    [[my^^F]](new  java.util.HashMap<String, String>(){ \"1\" -> \"Monday\" })\n" +
      "  }\n" +
      "}"
    )
  }

  //method with block argument
  function testRenameMethodDeclWithBlockAsParamTypeFromDecl() {
      test("renameBlockParam1",
        "package some.pkg\n" +
        "class GosuClass {\n" +
        "  function test() {\n" +
        "    var lst = [[getStringList]]( \\-> null )\n" +
        "  }\n\n" +
        "  function [[get^^StringList]]( foo():List<String> ) : List<String> {\n" +
        "    return null \n" +
        "  }"+
        "}"
      )
    }

  function testRenameMethodDeclWithBlockAsParamTypeFromUsage() {
      test("renameBlockParam2",
        "package some.pkg\n" +
        "class GosuClass {\n" +
        "  function test() {\n" +
        "    var lst = [[get^^StringList]]( \\-> null )\n" +
        "  }\n\n" +
        "  function [[getStringList]]( foo():List<String> ) : List<String> {\n" +
        "    return null \n" +
        "  }"+
        "}"
      )
    }

  //method with default params
  function testRenameDefaultParamDeclInMethodFromDecl() {
    test("renameDefParam1",
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function [[withDefaultParams]](x: int, y: String = \"default\", z: String = \"default\") {\n" +
      "  }\n" +
      "  function test(){\n" +
      "    [[withDefault^^Params]](:x = 1, :y = \"y\")\n" +
      "  }"+
      "}"
    )
  }

  function testRenameDefaultParamDeclInMethodFromUsage() {
    test("renameDefParam2",
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function [[withDefault^^Params]](x: int, y: String = \"default\", z: String = \"default\") {\n" +
      "  }\n" +
      "  function test(){\n" +
      "    [[withDefaultParams]](:x = 1, :y = \"y\")\n" +
      "  }"+
      "}"
    )
  }

  //feature literals
  function testRenamePropFieldDeclThroughFeatureAccessFromDecl() {
    test("renameLiteral1",
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var _pod : String as [[PO^^D]]\n" +
      "  function foo() {\n" +
      "    var myProperty = this.[[POD]]\n" +
      "  }"+
      "}"
    )
  }

  function testRenamePropFieldDeclThroughFeatureAccessFromUsage() {
    test("renameLiteral2",
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var _pod : String as [[POD]]\n" +
      "  function foo() {\n" +
      "    var myProperty = this.[[PO^^D]]\n" +
      "  }"+
      "}"
    )
  }

  //Prop Getter Through Feature Access
  function testRenamePropGetterDeclThroughFeatureAccessFromDecl() {
    test("renamePod1",
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  property get [[Po^^d]]() : String {\n" +
      "    return \"\"\n" +
      "  }\n" +
      "  function foo1() {\n" +
      "    var myProperty = this.[[Pod]]\n" +
      "  }" +
      "}"
    )
  }

  function testRenamePropGetterDeclThroughFeatureAccessFromUsage() {
    test("renamePod2",
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  property get [[Pod]]() : String {\n" +
      "    return \"\"\n" +
      "  }\n" +
      "  function foo1() {\n" +
      "    var myProperty = this.[[Po^^d]]\n" +
      "  }" +
      "}"
    )
  }

  //Function Decl Through Feature Access
  function testRenameFunctionDeclThroughFeatureAccessFromDecl() {
    test("renameFunc1",
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var _pod : String as POD\n" +
      "  function [[fo^^o]]() : String{\n" +
      "    var anObj = new GosuClass(){ : POD = \"Studio\"}\n" +
      "    var fooFuncForClass = anObj.[[foo]]()\n" +
      "    return anObj.toString()\n" +
      "  }\n" +
      "}"
    )
  }

  function testRenameFunctionDeclThroughFeatureAccessFromUsage() {
    test("renameFunc2",
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var _pod : String as POD\n" +
      "  function [[foo]]() : String{\n" +
      "    var anObj = new GosuClass(){ : POD = \"Studio\"}\n" +
      "    var fooFuncForClass = anObj.[[f^^oo]]()\n" +
      "    return anObj.toString()\n" +
      "  }\n" +
      "}"
    )
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

    //ReFactoring the element
    if( elementAt typeis GosuReferenceExpressionImpl ) {
      elementAt = elementAt.resolve()
      print( elementAt.Text )
    }

    var gsClassFile = (elementAt.ContainingFile as PsiFileImpl).calcTreeElement().Psi as GosuClassFileImpl
    var gsClass : IGosuClass
    new WriteCommandAction ( psiFiles.last().Project, {} ) {
      override function run( result: Result ) {
        var processor = new RenameProcessor(getProject(), elementAt, newName, false, false )
        processor.setPreviewUsages( false )
        processor.run()
        gsClass = gsClassFile.reparseGosuFromPsi()
      }
    }.execute()

    ApplicationManager.getApplication().runReadAction(
      new Runnable() {
        function run() {
          //Assert that the refactoring was successful
          assertTrue( gsClass.Valid )

          for( i in 0..|psiFiles.size() ) {
            var psiFile = psiFiles.get(i)
            var actual = psiFile.Text

            var expectedText = files.get(i).content.toString().replaceAll("\\^\\^","")
            expectedText=expectedText.replaceAll("\\[\\[\\w+\\]\\]", newName)     //find all [[]] and replace with new name

            // Have to remove spaces b/c refactoring will apply formatting, which can put spaces before/after type names eg., ctor calls
            assertEquals(expectedText.remove(" "), actual.remove(" "))
          }
        }
      } )
  }
}