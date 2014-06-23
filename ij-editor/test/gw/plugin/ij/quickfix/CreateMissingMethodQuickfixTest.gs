/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.quickfix

uses gw.plugin.ij.framework.GosuTestCase
uses gw.plugin.ij.framework.generator.ResourceFactory

class CreateMissingMethodQuickfixTest extends GosuTestCase {

  function test() {}

/*

This feature has not been implemented yet. It may be implemented in the future.

  function testCreateGosuMethodWithoutParams() {
    test(
      "package pkg1\n" +
      "class Class1 {\n" +
      "  function test1() {\n" +
      "    test^^2()\n" +
      "  }\n" +
      "[[[\n" +
      "  function test2!!(){\n" +
      "  }\n]]]" +
      "}"
    )
  }

  function testCreateGosuMethodWithIntegerParam() {
    test(
      "package pkg1\n" +
      "class Class1 {\n" +
      "  function test1() {\n" +
      "    test^^2(42)\n" +
      "  }\n" +
      "[[[\n" +
      "  function test2!!(i: int){\n" +
      "  }\n]]]" +
      "}"
    )
  }

  function testCreateGosuMethodWithStringParam() {
    test(
      "package pkg1\n" +
      "class Class1 {\n" +
      "  function test1() {\n" +
      "    test^^2(\"test\")\n" +
      "  }\n" +
      "[[[\n" +
      "  function test2!!(s: String){\n" +
      "  }\n]]]" +
      "}"
    )
  }

  function testCreateGosuMethodWithMapParam() {
    test(
      "package pkg1\n" +
      "class Class1 {\n" +
      "  function test1() {\n" +
      "    my^^F(new java.util.HashMap<String, String>(){ \"1\" -> \"Monday\" })\n" +
      "  }\n" +
      "[[[\n" +
      "  function myF!!(myMap : Map<String, String>) {\n" +
      "  }\n]]]" +
      "}"
    )
  }

  function testCreateGosuMethodWithBlockParam() {
    test(
      "package pkg1\n" +
      "class Class1 {\n" +
      "  function test1() {\n" +
      "    var lst = getString^^List( \\-> null )\n" +
      "  }\n" +
      "[[[\n" +
      "  function getStringList( foo():List<String> ) : List<String> {\n" +
      "  }\n]]]" +
      "}"
    )
  }

  function testCreateGosuMethodWithDefaultParam() {
    test(
      "package pkg1\n" +
      "class Class1 {\n" +
      "  function test1() {\n" +
      "    withDefault^^Params(:x = 1, :y = \"y\")\n" +
      "  }\n" +
      "[[[\n" +
      "  function withDefaultParams(x: int, y: String) {\n" +
      "  }\n]]]" +
      "}"
    )
  }

  function testCreateGosuMethodWithReturnType() {
    test(
      "package pkg1\n" +
      "class Class1 {\n" +
      "  function test1() {\n" +
      "    var x : String = test^^2()\n" +
      "  }\n" +
      "[[[\n" +
      "  function test2() : !!String{\n" +
      "  }\n]]]" +
      "}"
    )
  }

  function testCreateGosuMethodWithParamAndReturnType() {
    test(
      "package pkg1\n" +
      "class Class1 {\n" +
      "  function test1() {\n" +
      "    var x : String = test^^2(123)\n" +
      "  }\n" +
      "[[[\n" +
      "  function test2(i: int) : !!String{\n" +
      "  }\n]]]" +
      "}"
    )
  }




  function testCreateGosuConstrWithIntegerParam() {
    test(
      "package pkg1\n" +
      "class Class1 {\n" +
      "[[[\n" +
      "  construct(i: !!int){\n" +
      "  }\n]]]" +
      "  function test1() {\n" +
      "    var x : Class1 = new Class^^1(123)\n" +
      "  }\n" +
      "}"
    )
  }

  function testCreateGosuConstrWithMapParam() {
    test(
      "package pkg1\n" +
      "class Class1 {\n" +
      "[[[\n" +
      "  construct(myMap : Map<String, String>){\n" +
      "  }\n]]]" +
      "  function test1() {\n" +
      "    var x : Class1 = new Class^^1(new java.util.HashMap<String, String>(){ \"1\" -> \"Monday\" })\n" +
      "  }\n" +
      "}"
    )
  }

  function testCreateGosuConstrWithBlockParam() {
    test(
      "package pkg1\n" +
      "class Class1 {\n" +
      "[[[\n" +
      "  construct(foo():List<String>){\n" +
      "  }\n]]]" +
      "  function test1() {\n" +
      "    var x : Class1 = new Class^^1( \\-> null )\n" +
      "  }\n" +
      "}"
    )
  }

  function testCreateGosuConstrWithDefaultParam() {
    test(
      "package pkg1\n" +
      "class Class1 {\n" +
      "[[[\n" +
      "  construct(x: int, y: String){\n" +
      "  }\n]]]" +
      "  function test1() {\n" +
      "    var x : Class1 = new Class^^1(:x = 1, :y = \"y\")\n" +
      "  }\n" +
      "}"
    )
  }

  //method in different class
  function testCreateGosuMethodInAnotherClassParamAndReturnType() {
    test(
      "package pkg1\n" +
      "class Class1 {\n" +
      "  class testClass1 {\n" +
      "    var x : testClass2 = new testClass2()\n" +
      "    \n" +
      "  }\n" +
      "  class testClass2 {\n" +

      "  }\n" +
      "}"
    )
  }
*/
  function test(text: String ) {
    var markers = getMarkers(ResourceFactory.createFile(this, text))
    var caret1 = markers.getCaret(CARET1)
    var caret2 = markers.getPostCaret(CARET4)

    caret1.Editor.CaretModel.moveToOffset(caret1.offset);
    //Need to find
    findAndInvokeIntentionAction(highlightErrors(), "Create Method", caret1.Editor, caret1.File)

    var found = caret1.Editor.Document.Text
    assertEquals ("Texts do not match, auto edit failed", caret2.Parent.TextWithDeltas, found)

    var actualOffset = caret1.Editor.CaretModel.Offset
    var expectedOffset = caret2.offset
    assertEquals("Caret is in the wrong place", expectedOffset, actualOffset)
  }
}