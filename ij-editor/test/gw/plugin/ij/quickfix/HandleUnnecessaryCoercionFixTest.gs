/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.quickfix

uses gw.plugin.ij.framework.GosuTestCase
uses gw.plugin.ij.framework.MarkerType
uses gw.plugin.ij.framework.generator.GosuClassFile
uses gw.plugin.ij.framework.generator.GosuTestingResource

class HandleUnnecessaryCoercionFixTest extends GosuTestCase {

  function testUnnecessaryCoercion00() {
    var f = new GosuClassFile (
        "package some.pkg\n" +
            "uses java.lang.*\n" +
            "uses java.util.*\n" +
            "class Test {\n" +
            "\n" +
            "  function foo() {\n" +
            "    var x : Object\n" +
            "    x = \"Hello\"\n" +
            "    if( x typeis String ) {\n" +
            "      var len = [[(^^x as String)]].length\n" +
            "      var len2 = (x as String + 1).length\n" +
            "      var str = x as String\n" +
            "    }\n" +
            "  }\n" +
            "}")
    test({f}, "x")
  }

  function testUnnecessaryCoercion01() {
    var f = new GosuClassFile (
        "package some.pkg\n" +
            "uses java.lang.*\n" +
            "uses java.util.*\n" +
            "class Test {\n" +
            "\n" +
            "  function foo() {\n" +
            "    var x : Object\n" +
            "    x = \"Hello\"\n" +
            "    if( x typeis String ) {\n" +
            "      var len = (x as String).length\n" +
            "      var len2 = [[(^^x as String + 1)]].length\n" +
            "      var str = x as String\n" +
            "    }\n" +
            "  }\n" +
            "}")
    test({f}, "( x + 1)")
  }


  function testUnnecessaryCoercion02() {
    var f = new GosuClassFile (
        "package some.pkg\n" +
            "uses java.lang.*\n" +
            "uses java.util.*\n" +
            "class Test {\n" +
            "\n" +
            "  function foo() {\n" +
            "    var x : Object\n" +
            "    x = \"Hello\"\n" +
            "    if( x typeis String ) {\n" +
            "      var len = (x as String).length\n" +
            "      var len2 = (x as String + 1).length\n" +
            "      var str = [[^^x as String]]\n" +
            "    }\n" +
            "  }\n" +
            "}")
    test({f}, "x")
  }

  function testUnnecessaryCoercion03() {
    var f = new GosuClassFile (
        "package some.pkg\n" +
            "uses java.lang.*\n" +
            "uses java.util.*\n" +
            "class Test {\n" +
            "\n" +
            "  function foo() {\n" +
            "    var i : Integer\n" +
            "    i = 6\n" +
            "    var j = [[^^i as Integer]]\n" +
            "    }\n" +
            "  }\n" +
            "}")
    test({f}, "i")
  }

  function testUnnecessaryCoercion04() {
    var f = new GosuClassFile (
        "package some.pkg\n" +
            "uses java.lang.*\n" +
            "uses java.util.*\n" +
            "class Test {\n" +
            "\n" +
            "  function foo() {\n" +
            "    var i = 'aaa'\n" +
            "    var j = [[^^i.capitalize() as String]]\n" +
            "    }\n" +
            "  }\n" +
            "}")
    test({f}, "i.capitalize()")
  }


  function testUnnecessaryCoercion05() {
    var f = new GosuClassFile (
        "package some.pkg\n" +
            "uses java.lang.*\n" +
            "uses java.util.*\n" +
            "class Test {\n" +
            "\n" +
            "  function foo() {\n" +
            "    var i = 'aaa'\n" +
            "    var j = [[^^i.Alpha as boolean]]\n" +
            "    }\n" +
            "  }\n" +
            "}")
    test({f}, "i.Alpha")
  }

  function testUnnecessaryCoercion06() {
    var f = new GosuClassFile (
        "package some.pkg\n" +
            "uses java.lang.*\n" +
            "uses java.util.*\n" +
            "class Test {\n" +
            "\n" +
            "  function foo() {\n" +
            "    var j = [[^^'foo'.substring(1).length() as int]]\n" +
            "    }\n" +
            "  }\n" +
            "}")
    test({f}, "'foo'.substring(1).length()")
  }

  function test(resources: GosuTestingResource[], replace: String) {
    var markers = getAllMarkers(resources.map(\r -> configureByText(r.fileName, r.content)))
    var initialCaret = markers.getCaret(MarkerType.CARET1)
    assertTrue("No initial caret location defined.", initialCaret != null)
    assertEquals("No range carets location defined.", 1, markers.Ranges.size())

    var content = initialCaret.Editor.Document.Text
    var expected = content.substring(0, markers.Ranges.get(0).StartOffset) + replace + content.substring(markers.Ranges.get(0).EndOffset)

    initialCaret.Editor.CaretModel.moveToOffset(initialCaret.offset);
    findAndInvokeIntentionAction(highlightErrors(), "Remove unnecessary cast", initialCaret.Editor, initialCaret.File)

    var found = initialCaret.Editor.Document.Text
    assertEquals("Texts do not match, auto edit failed", expected, found)
  }
}
