/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.quickfix

uses com.intellij.psi.PsiFile
uses gw.plugin.ij.framework.GosuTestCase
uses gw.plugin.ij.framework.generator.GosuTestingResource
uses gw.plugin.ij.framework.generator.ResourceFactory

uses java.util.ArrayList
uses java.util.List

class QuickfixOverrideKeywordTest extends GosuTestCase {
  var intention = "Add 'override' keyword"

  function testAddOverrideInMethodInheritedFromInterface() {
    test({
        "package some.pkg\n" +
        "interface IBar {\n" +
        "  function bar()\n" +
        "}",
        "package some.pkg\n" +
        "class Bar1 implements IBar {\n" +
        "  [[[override ]]]function bar()^^ {\n" +
        "  }\n" +
        "}"})
  }

  function testAddOverrideInMethodWithParamInheritedFromInterface() {
    test({
        "package some.pkg\n" +
        "interface IBar {\n" +
        "  function bar(p1 : int)\n" +
        "}",
        "package some.pkg\n" +
        "class Bar1 implements IBar {\n" +
        "  [[[override ]]]!!function bar(x:int)^^ {\n" +
        "  }\n" +
        "}"})
  }

  function testAddInMethodWithReturnTypeInheritedFromInterface() {
    test({
        "package some.pkg\n" +
        "interface IBar {\n" +
        "  function bar():boolean\n" +
        "}",
        "package some.pkg\n" +
        "class Bar1 implements IBar {\n" +
        "  [[[override ]]]!!function bar() : boo^^lean {\n" +
        "    return false\n" +
        "  }\n" +
        "}"})
  }

  function testAddInMethodWithReturnTypeInheritedFromAbstractClass() {
    test({
        "package some.pkg\n" +
        "abstract class Foo {\n" +
        "  function bar():boolean{ return true}\n" +
        "}",
        "package some.pkg\n" +
        "class Bar1 extends Foo {\n" +
        "  [[[override ]]]!!function bar() : bool^^ean{\n" +
        "    return false\n" +
        "  }\n" +
        "}"})
  }

  //Bug - PL-19194 although override key word added
  function testAddOverrideInAbstractClassMethodInheritedFromAbstractClass() {
    test({
        "package some.pkg\n" +
        "abstract class Class1 {\n" +
        "  abstract function foo()\n" +
        "}",
        "package some.pkg\n" +
        "abstract class Class11 extends Class1 {\n" +
        "  [[[override ]]]!!abstract function foo()^^\n" +
        "}"})
  }

  function testAddOverrideInInterfaceMethodInheritedFromInterface() {
    test({
        "package some.pkg\n" +
        "public interface IBar {\n" +
        "  function bar(p1 : int) : int \n" +
        "}",
        "package some.pkg\n" +
        "interface IDerivedBar extends IBar {\n" +
        "  [[[override ]]]!!function bar(p1 : int) : in^^t \n" +
        "}"})
  }

  function testAddInPropertyInheritedFromInterface() {
    test({
        "package some.pkg\n" +
        "interface IBar {\n" +
        "  property get x():int\n" +
        "}",
        "package some.pkg\n" +
        "class Bar1 implements IBar {\n" +
        "  [[[override ]]]!!property get x() : in^^t{\n" +
        "    return 123\n" +
        "  }\n" +
        "}\n"})
  }

  function testAddInMethodInheritedFromJavaClass() {
    test({
        "package some.pkg\n" +
        "class Bar1 extends java.awt.Rectangle {\n" +
        "  [[[override ]]]!!function setBounds(a:int, b:int, c: int, d:int)^^ {\n" +
        "  }\n" +
        "}"})
  }

  function testAddInMethodInheritedFromJavaClass1() {
    test({
        "package some.pkg\n" +
        "class Foo implements java.lang.Runnable {\n" +
        "  [[[override ]]]!!function run()^^ {\n" +
        "  }\n" +
        "}\n"})
  }

  function testAddInInnerClassFromOuterClass() {
    test({
        "package some.pkg\n" +
        "class Bar {\n" +
        "function bar() {}\n" +
        "  class Foo extends Bar {\n" +
        "    [[[override ]]]!!function bar()^^ {\n" +
        "      return false\n" +
        "    }\n" +
        "  }\n" +
        "}"})
  }

  function testAddForMethodInInnerClassFromOuterClassNegativeCase() {
    test({
        "package some.pkg\n" +
        "class Bar {\n" +
        "function bar() {}\n" +
        "  class Foo extends Bar {\n" +
        "    [[[override ]]]!!function bar()^^ {\n" +
        "      return false\n" +
        "    }\n" +
        "    function bar(x:int){}\n" +
        "  }\n" +
        "}"
    })
  }

  function testAddForPropertyFieldInInnerClassFromOuterClassNegativeCase() {
    test({
        "package some.pkg\n" +
        "class Bar {\n" +
        "  property get Bar() : boolean { return true }\n" +
        "  class Foo extends Bar {\n" +
        "    [[[override ]]]!!property get Bar() : boolean^^ {\n" +
        "      return false\n" +
        "    }\n" +
        "    function bar(x:int){}\n" +
        "  }\n" +
        "}"})
  }

  function testAddInMethodWithReturnTypeFromAbstractClasss() {
    test({
        "package some.pkg\n" +
        "abstract class Foo {\n" +
        "  function bar():boolean{ return true}\n" +
        "}",
        "package some.pkg\n" +
        "class Bar1 extends Foo {\n" +
        "  [[[override ]]]!!function bar() : bool^^ean{\n" +
        "    return false\n" +
        "  }\n" +
        "}"})
  }

  function testAddtoMethodInTopLevelJAnonymousClass() {
    test({
        "package some.pkg\n" +
        "uses java.lang.Runnable\n" +
        "class Foo {\n" +
        "  function bar() {\n" +
        "    var x = new Runnable() {\n" +
        "      [[[override ]]]function run()^^ {\n" +
        "        \n" +
        "      }\n" +
        "    } " +
        "  }\n" +
        "}\n"})
  }

  function testAddInMethodInSecondLevelJAnonymousClass() {
    test({
         "package some.pkg\n" +
         "uses java.lang.Runnable\n" +
         "class Foo {\n" +
         "  function bar() {\n" +
         "    var x = new Runnable() {\n" +
         "      override function run() {\n" +
         "         var x = new Runnable() {\n" +
         "           [[[override ]]]function run()^^ {}\n" +
         "        }\n" +
         "      }\n" +
         "    }\n" +
         "  }\n" +
         "}"})
  }

  function testAddtoMethodInTopLevelGAnonymousClass() {
    test({
        "package some.pkg\n" +
        "class Bar {\n" +
        "  function bar() {\n" +
        "  }" +
        "}\n",
        "package some.pkg\n" +
        "class Foo {\n" +
        "  function doit() {\n" +
        "    var o = new Bar(){\n" +
        "      var x = 5\n" +
        "      [[[override ]]]function bar()^^{\n" +
        "        x = 10\n" +
        "      }\n" +
        "    }\n" +
        "  }\n" +
        "}\n"})
  }

  function testAddInMethodInSecondLevelGAnonymousClass() {
    test({
        "package some.pkg\n" +
        "class Bar {\n" +
        "  function bar() {\n" +
        "  }" +
        "}\n",
        "package some.pkg\n" +
        "class Foo {\n" +
        "  function doit() {\n" +
        "    var a = new Bar(){\n" +
        "      function bar(){\n" +
        "        var b = new Bar(){\n" +
        "        [[[override ]]]function bar()^^{\n" +
        "          x = 10\n" +
        "        }\n" +
        "      }\n" +
        "    }\n" +
        "  }\n" +
        "}\n"})
  }

  function testAddInMethodInExtendedSecondLevelGAnonymousClass() {
    test({
        "package some.pkg\n" +
        "class Bar extends Foo{\n" +
        "  override function bar() {\n" +
        "  }" +
        "}\n",
        "package some.pkg\n" +
        "class Foo {\n" +
        "  function doit() {\n" +
        "    var a = new Bar(){\n" +
        "      function bar(){\n" +
        "        var b = new Bar(){\n" +
        "        [[[override ]]]function bar()^^{\n" +
        "          x = 10\n" +
        "        }\n" +
        "      }\n" +
        "    }\n" +
        "  }\n" +
        "}\n"})
  }

  function testAddInMethodWithReturnTypeFromAbstractClass1() {
    test({
        "package some.pkg\n" +
        "uses java.util.concurrent.Callable\n" +
        "class Bar1 {\n" +
        "  function bar() {\n" +
        "    var b1 = \\-> new Callable() {\n" +
        "      [[[override ]]]function call() : block():Callable<block():block():int>^^ {\n" +
        "        return \\-> new Callable<block():block():int> (){\n" +
        "          override function call() : block():block():int {\n" +
        "            return \\->\\-> outer.outer.IntProperty\n" +
        "          }\n" +
        "        }\n" +
        "      }\n" +
        "    }\n" +
        "  }\n" +
        "  property get IntProperty() : int {\n" +
        "    return 42\n" +
        "  }\n" +
        "}"})
  }

  function testAddInMethodWithReturnTypeFromAbstractClass2() {
    test({
        "package some.pkg\n" +
        "uses java.util.concurrent.Callable\n" +
        "class Bar1 {\n" +
        "  function bar() {\n" +
        "    var b1 = \\-> new Callable() {\n" +
        "      override function call() : block():Callable<block():block():int> {\n" +
        "        return \\-> new Callable<block():block():int> (){\n" +
        "          [[[override ]]]function call() : block():block():i^^nt {\n" +
        "            return \\->\\-> outer.outer.IntProperty\n" +
        "          }\n" +
        "        }\n" +
        "      }\n" +
        "    }\n" +
        "  }\n" +
        "  property get IntProperty() : int {\n" +
        "    return 42\n" +
        "  }\n" +
        "}"
    })
  }

  function testAddInMethodWithReturnTypeFromAbstractClass112() {
     test({
         "package some.pkg\n" +
         "abstract class Foo {\n" +
         "  function bar(): boolean { return true }\n" +
         "}",
         "package some.pkg\n" +
         "class Bar1 extends Foo {\n" +
         "  [[[override ]]]!!function bar(): b^^oolean {\n" +
         "    return false\n" +
         "  }\n" +
         "}"})
    }

  function test(text: String) {
    test({text})
  }

  function test(texts: List <String>) {
    testImpl(texts.map(\elt -> ResourceFactory.create(elt)))
  }

  function testImpl(files: List <GosuTestingResource>) {
    var psiFiles = new ArrayList <PsiFile>()
    for (f in files) {
      psiFiles.add(configureByText(f.fileName, f.content))
    }

    var markers = getAllMarkers(psiFiles.toArray(new PsiFile[psiFiles.size()]))
    var caret1 = markers.getCaret(CARET1);

    caret1.Editor.CaretModel.moveToOffset(caret1.offset)
    findAndInvokeIntentionAction(highlightErrors(), intention, caret1.Editor, caret1.File)

    var expected = caret1.Parent.TextWithDeltas
    var actual = caret1.Editor.Document.Text
    assertEquals("Texts do not match, auto edit failed", expected, actual)
  }
}