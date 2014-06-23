/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.quickfix

uses gw.plugin.ij.framework.GosuTestCase
uses gw.plugin.ij.framework.MarkerType
uses gw.plugin.ij.framework.generator.GosuClassFile
uses gw.plugin.ij.framework.generator.GosuTestingResource

class HandleVarArgFixTest extends GosuTestCase {

  function testVarArg00() {
    var f = new GosuClassFile (
        "package some.pkg\n" +
            "uses java.lang.*\n" +
            "uses java.util.*\n" +
            "class Test {\n" +
            "\n" +
            "  function bar1(arr : int[]) : void {\n" +
            "    bar1([[^^0, 1]])\n" +
            "  }" +
            "}")
    test({f}, "{0, 1}")
  }

  function testVarArg01() {
    var f = new GosuClassFile (
        "package some.pkg\n" +
            "uses java.lang.*\n" +
            "uses java.util.*\n" +
            "class Test {\n" +
            "\n" +
            "  function bar2(a : int, arr : int[]) : void {\n" +
            "    bar2(0, [[^^1]])\n" +
            "  }" +
            "}")
    test({f}, "{1}")
  }

  function testVarArg02() {
    var f = new GosuClassFile (
        "package some.pkg\n" +
            "uses java.lang.*\n" +
            "uses java.util.*\n" +
            "class Test {\n" +
            "\n" +
            "  function bar2(a : int, arr : int[]) : void {\n" +
            "    bar2([[^^0, 1, 2, 3]])\n" +
            "  }" +
            "}")
    test({f}, "0, {1, 2, 3}")
  }

  function testVarArg03() {
    var f = new GosuClassFile (
        "package some.pkg\n" +
            "uses java.lang.*\n" +
            "uses java.util.*\n" +
            "class Test {\n" +
            "\n" +
            "  function bar3(a : int, b : int, arr : int[]) : void {\n" +
            "    [[bar3(^^0, 1)]]\n" +
            "  }" +
            "}")
    test({f},"bar3(0, 1, {})")
  }

  function testVarArg04() {
    var f = new GosuClassFile (
        "package some.pkg\n" +
            "uses java.lang.*\n" +
            "uses java.util.*\n" +
            "class Test {\n" +
            "\n" +
            "  function bar3(a : int, b : int, arr : int[]) : void {\n" +
            "    bar3(0, 1, [[^^2]])\n" +
            "  }" +
            "}")
    test({f}, "{2}")
  }

  function testVarArg05() {
    var f = new GosuClassFile (
        "package some.pkg\n" +
            "uses java.lang.*\n" +
            "uses java.util.*\n" +
            "class Test {\n" +
            "\n" +
            "  function bar4(a : int, b : int[], arr : int[]) : void {\n" +
            "    [[bar4(^^0, b)]]\n" +
            "  }" +
            "}")
    test({f}, "bar4(0, b, {})")
  }

  function testVarArg06() {
    var f = new GosuClassFile (
        "package some.pkg\n" +
            "uses java.lang.*\n" +
            "uses java.util.*\n" +
            "class Test {\n" +
            "\n" +
            "  function bar4(a : int, b : int[], arr : int[]) : void {\n" +
            "    [[bar4(0, b, ^^2)]]\n" +
            "  }" +
            "}")
    test({f}, "bar4(0, b, {2})")
  }

  function testVarArg07() {
    var f = new GosuClassFile (
        "package some.pkg\n" +
            "uses java.lang.*\n" +
            "uses java.util.*\n" +
            "class Test {\n" +
            "\n" +
            "  function bar5() : void {\n" +
            "    var t = new Date()\n" +
            "    [[System.out.format(\"%tc%d\", t, ^^1)]]\n" +
            "  }" +
            "}")
    test({f}, "System.out.format(\"%tc%d\", {t, 1})")
  }

  function testVarArg08() {
    var f = new GosuClassFile (
        "package some.pkg\n" +
            "uses java.lang.*\n" +
            "uses java.util.*\n" +
            "class Test {\n" +
            "\n" +
            "  function bar5() : void {\n" +
            "    var t = new Date()\n" +
            "    [[System.out.format(Locale.US, \"%tc%s\", ^^t, \"string\")]]\n" +
            "  }" +
            "}")
    test({f}, "System.out.format(Locale.US, \"%tc%s\", {t, \"string\"})")
  }

    function testVarArg09() {
      var f = new GosuClassFile (
          "package some.pkg\n" +
              "uses java.lang.*\n" +
              "uses java.util.*\n" +
              "class Test {\n" +
              "\n" +
              "  function bar1(arr : int[]) : void {\n" +
              "    bar1([[^^]])\n" +
              "  }" +
              "}")
      test({f}, "{}")
    }

  function test(resources: GosuTestingResource[], replace: String) {
    var markers = getAllMarkers(resources.map(\r -> configureByText(r.fileName, r.content)))
    var initialCaret = markers.getCaret(MarkerType.CARET1)
    assertTrue("No initial caret location defined.", initialCaret != null)
    assertEquals("No range carets location defined.", 1, markers.Ranges.size())

    var content = initialCaret.Editor.Document.Text
    var expected = content.substring(0, markers.Ranges.get(0).StartOffset) + replace + content.substring(markers.Ranges.get(0).EndOffset)

    initialCaret.Editor.CaretModel.moveToOffset(initialCaret.offset);
    findAndInvokeIntentionAction(highlightErrors(), "Add array literal", initialCaret.Editor, initialCaret.File)

    var found = initialCaret.Editor.Document.Text
    assertEquals("Texts do not match, auto edit failed", expected, found)
  }
}