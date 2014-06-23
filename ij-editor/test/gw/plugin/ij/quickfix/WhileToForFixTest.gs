/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.quickfix

uses gw.plugin.ij.codeInspection.statement.GosuWhileToForInspection
uses gw.plugin.ij.framework.GosuTestCase
uses gw.plugin.ij.framework.MarkerType
uses gw.plugin.ij.framework.generator.GosuClassFile
uses gw.plugin.ij.framework.generator.GosuTestingResource

class WhileToForFixTest extends GosuTestCase {

  function testWhileToFor00() {
    var f = new GosuClassFile (
        "package some.pkg\n" +
            "uses java.util.*\n" +
            "uses java.lang.*\n" +
            "\n" +
            "class A {\n" +
            "function foo() {\n" +
            "  [[var a = 0\n" +
            "  var b = 1\n" +
            "  ^^while (a < 3) {\n" +
            "    print(a)\n" +
            "    a++\n" +
            "    b++\n" +
            "  }]]\n"+
            "}")
    test({f}, "var b = 1\n  for (a in 0..2) {\n     print(a)\n     b++\n  }")
  }

  function testWhileToFor01() {
    var f = new GosuClassFile (
        "package some.pkg\n" +
            "uses java.util.*\n" +
            "uses java.lang.*\n" +
            "\n" +
            "class A {\n" +
            "function foo() {\n" +
            "  var str = \"foo\"\n" +
            "  [[var a1 = 0\n" +
            "  ^^while (a1 < str.length()) {\n" +
            "    print(str[a1])\n" +
            "    a1++\n" +
            "  }]]\n" +
            "}")
    test({f}, "for (a1 in 0..|str.length()) {\n     print(str[a1])\n  }")
  }

  function testWhileToFor02() {
    var f = new GosuClassFile (
        "package some.pkg\n" +
            "uses java.util.*\n" +
            "uses java.lang.*\n" +
            "\n" +
            "class A {\n" +
            "function foo() {\n" +
            "  [[var a2 = 0\n" +
            "  ^^while (a2 < arr.length) {\n" +
            "    print(str[a2])\n" +
            "    a2++\n" +
            "  }]]\n" +
            "}")
    test({f}, "for (a2 in 0..|arr.length) {\n                 print(str[a2\n                 ]\n                 )\n  }")
  }

  function testWhileToFor03() {
    var f = new GosuClassFile (
        "package some.pkg\n" +
            "uses java.util.*\n" +
            "uses java.lang.*\n" +
            "\n" +
            "class A {\n" +
            "function foo() {\n" +
            "  var l  = {'f', 'o', 'o'};\n" +
            "  [[var a3 = 0\n" +
            "  ^^while(a3 < l.size()) {\n" +
            "    print(l[a3])\n" +
            "    a3++\n" +
            "  }]]\n" +
            "}")
    test({f}, "for (a3 in 0..|l.size()) {\n     print(l[a3])\n  }")
  }
  function test(resources: GosuTestingResource[], replace: String) {
    var markers = getAllMarkers(resources.map(\r -> configureByText(r.fileName, r.content)))
    var initialCaret = markers.getCaret(MarkerType.CARET1)
    assertTrue("No initial caret location defined.", initialCaret != null)
    assertEquals("No range carets location defined.", 1, markers.Ranges.size())
    var content = initialCaret.Editor.Document.Text
    var expected = content.substring(0, markers.Ranges.get(0).StartOffset) + replace + content.substring(markers.Ranges.get(0).EndOffset)
    initialCaret.Editor.CaretModel.moveToOffset(initialCaret.offset);
    enableInspectionTool(new GosuWhileToForInspection())
    findAndInvokeIntentionAction(doHighlighting(), "The while loop can be replaced by a for loop", initialCaret.Editor, initialCaret.File)
    var found = initialCaret.Editor.Document.Text
    assertEquals("Texts do not match, auto edit failed", expected, found)
  }
}
