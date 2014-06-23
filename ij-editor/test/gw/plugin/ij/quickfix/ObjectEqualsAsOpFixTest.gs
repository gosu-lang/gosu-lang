/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.quickfix

uses gw.plugin.ij.codeInspection.expression.GosuObjectEqualsInspection
uses gw.plugin.ij.framework.GosuTestCase
uses gw.plugin.ij.framework.MarkerType
uses gw.plugin.ij.framework.generator.GosuClassFile
uses gw.plugin.ij.framework.generator.GosuTestingResource

class ObjectEqualsAsOpFixTest extends GosuTestCase {

  function testObjectEqualsToEqual00() {
    var f = new GosuClassFile (
        "package some.pkg\n" +
            "uses java.util.*\n" +
            "uses java.lang.*\n" +
            "\n" +
            "class A {\n" +
            "  function foo() {\n" +
            "  var i = true\n" +
            "  var c = \"a\"\n" +
            "  var a = \"a\" \n" +
            "  if(i || [[a != null && ^^a.equals(c)]]) {\n" +
            "      var b = a.equals(c)\n" +
            "  }\n" +
            "}")
    test({f}, "a == c")
  }

  function testObjectEqualsToEqual01() {
    var f = new GosuClassFile (
        "package some.pkg\n" +
            "uses java.util.*\n" +
            "uses java.lang.*\n" +
            "\n" +
            "class A {\n" +
            "  function foo() {\n" +
            "  var i = true\n" +
            "  var c = \"a\"\n" +
            "  var a = \"a\" \n" +
            "  if(i || a != null && a.equals(c)) {\n" +
            "      var b = [[^^a.equals(c)]]\n" +
            "  }\n" +
            "}")
    test({f}, "a == c")
  }

  function testObjectEqualsToEqual02() {
    var f = new GosuClassFile (
        "package some.pkg\n" +
            "uses java.util.*\n" +
            "uses java.lang.*\n" +
            "\n" +
            "class A {\n" +
            "  function foo() {\n" +
            "  if([[!^^\"1\".equals(\"2\")]]) {\n" +
            "  }\n" +
            "}")
    test({f}, "\"1\" != \"2\"")
  }

  function test(resources: GosuTestingResource[], replace: String) {
    var markers = getAllMarkers(resources.map(\r -> configureByText(r.fileName, r.content)))
    var initialCaret = markers.getCaret(MarkerType.CARET1)
    assertTrue("No initial caret location defined.", initialCaret != null)
    assertEquals("No range carets location defined.", 1, markers.Ranges.size())
    var content = initialCaret.Editor.Document.Text
    var expected = content.substring(0, markers.Ranges.get(0).StartOffset) + replace + content.substring(markers.Ranges.get(0).EndOffset)
    initialCaret.Editor.CaretModel.moveToOffset(initialCaret.offset);
    enableInspectionTool(new GosuObjectEqualsInspection())
    findAndInvokeIntentionAction(doHighlighting(), "The \"Object.equals()\" can be replaced by the equality operator", initialCaret.Editor, initialCaret.File)
    var found = initialCaret.Editor.Document.Text
    assertEquals("Texts do not match, auto edit failed", expected, found)
  }

}