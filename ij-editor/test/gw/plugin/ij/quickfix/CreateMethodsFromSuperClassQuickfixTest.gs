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

class CreateMethodsFromSuperClassQuickfixTest extends GosuTestCase {

  var intention = "Implement Methods"

  function testCreateGosuMethodWithoutParams() {
    test({
      "package pkg1\n" +
      "interface SuperClass{\n" +
      "  function test1() \n" +
      "  function test2() \n" +
      "}",
      "package pkg1\n" +
      "class Class1 implements Super^^Class{\n" +
      "  function test1() {\n" +
      "    test2()\n" +
      "  }\n" +
      "[[[\n" +
      "  override function test2!!() {\n" +
      "  }\n]]]" +
      "}"
    })
  }

  function testCreateGosuMethodWithIntegerParam() {
    test({
      "package pkg1\n" +
      "interface SuperClass{\n" +
      "  function test1() \n" +
      "  function test2(x:int) \n" +
      "}",
      "package pkg1\n" +
      "class Class1 implements Super^^Class{\n" +
      "  function test1() {\n" +
      "    test2(42)\n" +
      "  }\n" +
      "[[[\n" +
      "  override function test2!!(x: int) {\n" +
      "  }\n]]]" +
      "}"
    })
  }

  function testCreateGosuMethodWithStringParam() {
    test({
      "package pkg1\n" +
      "interface SuperClass {\n" +
      "  function test1(x:int) \n" +
      "  function test2(y:int) \n" +
      "}",
      "package pkg1\n" +
      "class Class1 implements Super^^Class {\n" +
      "  [[[override function test1(x: int) {\n" +
      "  }\n" +
      "\n" +
      "  override function test2(y: int) {\n" +
      "  }\n]]]!!" +
      "}"
    })
  }

  function testCreateGosuMethodWithMapParam() {
    test({
      "package pkg1\n" +
      "interface SuperClass {\n" +
      "  function myF(y: java.util.HashMap<String, String>()) \n" +
      "}",
      "package pkg1\n" +
      "[[[\n" +
      "uses java.util.HashMap\n" +
      "\n]]]" +
      "class Class1 implements Super^^Class {\n" +
      "[[[  override function myF(y: HashMap<String, String>) {\n" +
      "~~~  }\n]]]" +
      "}"
    })
  }

  function testCreateGosuPropertyGetterMethod() {
    test({
      "package pkg1\n" +
      "interface SuperClass {\n" +
      "  property get t():int \n" +
      "}",
      "package pkg1\n" +
      "class Class1 implements SuperClass^^ {\n" +
      "[[[  override property get t(): int {\n" +
      "~~~    return 0\n" +
      "~~~  }\n]]]" +
      "}"
    })
  }

  function testCreateGosuPropertySetterMethod() {
    test({
      "package pkg1\n" +
      "interface SuperClass {\n" +
      "  property set t(x:int) \n" +
      "}",
      "package pkg1\n" +
      "class Class1 implements SuperClass^^ {\n" +
      "[[[  override property set t(x: int) {\n" +
      "~~~  }\n]]]" +
      "}"
  })
  }

  function testCreateGosuMethodWithDefaultParam() {
    test({
      "package pkg1\n" +
      "interface SuperClass {\n" +
      "  function withDefaultParams(x = 1, y = \"y\") \n" +
      "}",
      "package pkg1\n" +
      "class Class1 implements SuperClass^^ {\n" +
      "[[[  override function withDefaultParams(x: int, y: String) {\n" +
      "~~~  }\n]]]" +
      "}"
    })
  }

  function testCreateGosuMethodWithReturnType() {
    test({
      "package pkg1\n" +
      "interface SuperClass {\n" +
      "  function withReturnType():int \n" +
      "}",
      "package pkg1\n" +
      "class Class1 implements SuperClass^^ {\n" +
      "[[[  override function withReturnType(): int {\n" +
      "~~~    return 0\n" +
      "~~~  }\n]]]" +
      "}"
    })
  }

  function testCreateGosuMethodWithParamAndReturnType() {
    test({
      "package pkg1\n" +
      "interface SuperClass {\n" +
      "  function withReturnType(x:int):int \n" +
      "}",
      "package pkg1\n" +
      "class Class1 implements SuperClass^^ {\n" +
      "[[[  override function withReturnType(x: int): int {\n" +
      "~~~    return 0\n" +
      "~~~  }\n]]]" +
      "}"
    })
  }

  function testCreateGosuMethodWithMultiLevelInterfaces() {
    test({
      "package pkg1\n" +
      "interface SuperClass1{\n" +
      "  function test1() \n" +
      "}",
      "package pkg1\n" +
      "interface SuperClass2 implements SuperClass1{\n" +
      "  function test2() \n" +
      "}",
      "package pkg1\n" +
      "class Class1 implements Super^^Class2 {\n" +
      "[[[  override function test2() {\n" +
      "  }\n" +
      "\n" +
      "  override function test1!!() {\n" +
      "  }\n]]]" +
      "}"
    })
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