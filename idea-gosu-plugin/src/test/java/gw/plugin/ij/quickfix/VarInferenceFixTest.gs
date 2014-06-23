/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.quickfix

uses gw.plugin.ij.codeInspection.declaration.GosuInferTypeInDeclarationInspection
uses gw.plugin.ij.framework.GosuTestCase
uses gw.plugin.ij.framework.MarkerType
uses gw.plugin.ij.framework.generator.GosuClassFile
uses gw.plugin.ij.framework.generator.GosuTestingResource

class VarInferenceFixTest extends GosuTestCase {

  function testVarInf00() {
    var f = new GosuClassFile (
        "package some.pkg\n" +
            "uses java.util.*\n" +
            "uses java.lang.*\n" +
            "\n" +
            "class A {\n" +
            "  function foo() {\n" +
            "    [[var d : ^^Map<String, Integer> = new HashMap<String, Integer>()]]\n" +
            "    var f : Map<String, Integer> = d\n" +
            "    var e2 : double = 1.0d\n" +
            "    var e3 : float = 1.0f\n" +
            "  }\n" +
            "}")
    test({f}, "var d = new HashMap<String, Integer>()")
  }

  function testVarInf01() {
      var f = new GosuClassFile (
          "package some.pkg\n" +
              "uses java.util.*\n" +
              "uses java.lang.*\n" +
              "\n" +
              "class A {\n" +
              "  function foo() {\n" +
              "    var d : Map<String, Integer> = new HashMap<String, Integer>()\n" +
              "    [[var f : ^^Map<String, Integer> = d]]\n" +
              "    var e2 : double = 1.0d\n" +
              "    var e3 : float = 1.0f\n" +
              "  }\n" +
              "}")
      test({f}, "var f = d")
    }

    function testVarInf02() {
      var f = new GosuClassFile (
          "package some.pkg\n" +
              "uses java.util.*\n" +
              "uses java.lang.*\n" +
              "\n" +
              "class A {\n" +
              "  function foo() {\n" +
              "    var d : Map<String, Integer> = new HashMap<String, Integer>()\n" +
              "    var f : Map<String, Integer> = d\n" +
              "    [[var e2 : ^^double = 1.0d]]\n" +
              "    var e3 : float = 1.0f\n" +
              "  }\n" +
              "}")
      test({f}, "var e2 = 1.0d")
    }

    function testVarInf03() {
      var f = new GosuClassFile (
          "package some.pkg\n" +
              "uses java.util.*\n" +
              "uses java.lang.*\n" +
              "\n" +
              "class A {\n" +
              "  function foo() {\n" +
              "    var d : Map<String, Integer> = new HashMap<String, Integer>()\n" +
              "    var f : Map<String, Integer> = d\n" +
              "    var e2 : double = 1.0d\n" +
              "    [[var e3 : ^^float = 1.0f]]\n" +
              "  }\n" +
              "}")
      test({f}, "var e3 = 1.0f")
    }

    function testVarInf04() {
      var f = new GosuClassFile (
          "package some.pkg\n" +
              "uses java.util.*\n" +
              "uses java.lang.*\n" +
              "\n" +
              "class A {\n" +
            "    [[var d : ^^Map<String, Integer> = new HashMap<String, Integer>()]]\n" +
              "}")
      test({f}, "var d = new HashMap<String, Integer>()")
    }


  function test(resources: GosuTestingResource[], replace: String) {
    var markers = getAllMarkers(resources.map(\r -> configureByText(r.fileName, r.content)))
    var initialCaret = markers.getCaret(MarkerType.CARET1)
    assertTrue("No initial caret location defined.", initialCaret != null)
    assertEquals("No range carets location defined.", 1, markers.Ranges.size())
    var content = initialCaret.Editor.Document.Text
    var expected = content.substring(0, markers.Ranges.get(0).StartOffset) + replace + content.substring(markers.Ranges.get(0).EndOffset)
    initialCaret.Editor.CaretModel.moveToOffset(initialCaret.offset);
    enableInspectionTool(new GosuInferTypeInDeclarationInspection())
    findAndInvokeIntentionAction(doHighlighting(), "The variable type is inferred and can be omitted", initialCaret.Editor, initialCaret.File)
    var found = initialCaret.Editor.Document.Text
    assertEquals("Texts do not match, auto edit failed", expected, found)
  }
}
