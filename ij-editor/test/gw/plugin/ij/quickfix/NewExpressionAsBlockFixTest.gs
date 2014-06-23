/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.quickfix

uses gw.plugin.ij.codeInspection.expression.NewExpressionAsBlockInspection
uses gw.plugin.ij.framework.GosuTestCase
uses gw.plugin.ij.framework.MarkerType
uses gw.plugin.ij.framework.generator.GosuClassFile
uses gw.plugin.ij.framework.generator.GosuTestingResource

class NewExpressionAsBlockFixTest extends GosuTestCase {

  function testNewExprAsBlock00() {
    var f = new GosuClassFile (
        "package some.pkg\n" +
            "uses java.util.*\n" +
            "uses java.lang.*\n" +
            "\n" +
            "class A {\n" +
            "  function foo() {\n" +
            "    [[var a = new ^^Comparator<Integer>() {\n" +
            "      function compare( v1 : Integer, v2 : Integer) : int {\n" +
            "        return v1 - v2\n" +
            "      }\n" +
            "    }]]\n" +
            "  }\n" +
            "}")
    test({f}, "var a = \\ v1 : Integer, v2 : Integer ->  v1 - v2")
  }


  function testNewExprAsBlock01() {
    var f = new GosuClassFile (
        "package some.pkg\n" +
            "uses java.util.*\n" +
            "uses java.lang.*\n" +
            "\n" +
            "class A {\n" +
            "  function foo() {\n" +
            "    [[var b =  new ^^Iterable<String>() {\n" +
            "\n" +
            "      public function iterator() : Iterator<String> {\n" +
            "        return new Iterator<String>() {\n" +
            "\n" +
            "          internal var i : int = 0\n" +
            "          public function hasNext() : boolean {\n" +
            "            return i < 0\n" +
            "          }\n" +
            "\n" +
            "          public function next() : String {\n" +
            "            var tmp = i\n" +
            "            i++\n" +
            "            return \"string\" + tmp\n" +
            "          }\n" +
            "\n" +
            "          public function remove() : void {\n" +
            "          }\n" +
            "\n" +
            "        }\n" +
            "\n" +
            "      }\n" +
            "    }]]\n" +
            "  }\n" +
            "}")
    test({f}, "var b = \\  ->  new Iterator<String>() {\n" +
        "    \n" +
        "              internal var i : int = 0\n" +
        "              public function hasNext() : boolean {\n" +
        "                return i < 0\n" +
        "              }\n" +
        "    \n" +
        "              public function next() : String {\n" +
        "                var tmp = i\n" +
        "                i++\n" +
        "                return \"string\" + tmp\n" +
        "              }\n" +
        "    \n" +
        "              public function remove() : void {\n" +
        "              }\n" +
        "    \n" +
        "            }")
  }

  function test(resources: GosuTestingResource[], replace: String) {
    var markers = getAllMarkers(resources.map(\r -> configureByText(r.fileName, r.content)))
    var initialCaret = markers.getCaret(MarkerType.CARET1)
    assertTrue("No initial caret location defined.", initialCaret != null)
    assertEquals("No range carets location defined.", 1, markers.Ranges.size())
    var content = initialCaret.Editor.Document.Text
    var expected = content.substring(0, markers.Ranges.get(0).StartOffset) + replace + content.substring(markers.Ranges.get(0).EndOffset)
    initialCaret.Editor.CaretModel.moveToOffset(initialCaret.offset);
    enableInspectionTool(new NewExpressionAsBlockInspection())
    findAndInvokeIntentionAction(doHighlighting(), "The \"new expression\" can be replaced by a block", initialCaret.Editor, initialCaret.File)
    var found = initialCaret.Editor.Document.Text
    assertEquals("Texts do not match, auto edit failed", expected, found)
  }

}