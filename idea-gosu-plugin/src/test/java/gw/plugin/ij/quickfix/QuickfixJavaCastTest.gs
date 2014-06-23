/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.quickfix

uses gw.plugin.ij.framework.GosuTestCase
uses gw.plugin.ij.framework.MarkerType
uses gw.plugin.ij.framework.generator.GosuClassFile
uses gw.plugin.ij.framework.generator.GosuProgramFile
uses gw.plugin.ij.framework.generator.GosuTestingResource

class QuickfixJavaCastTest extends GosuTestCase {

  //broken
  function testChangeJavaCastShouldNotaffectOperatorPrecedence() {
    var f = new GosuClassFile (
        "package some.pkg\n" +
        "class One {\n" +
        "  function bar() {\n" +
        "    var t = typeof [[(String)^^ 1]]\n"  +
        "  }\n" +
        "}")
    test({f}, "(1 as String)")
  }

  function testLiteralExpressionInVarCast() {
    var f = new GosuClassFile (
        "package some.pkg\n" +
        "class One {\n" +
        "  function bar() {\n" +
        "    var s = [[(String)^^ 1]]\n" +
        "  }\n" +
        "}")
    test({f}, "1 as String")
  }

  function testArrayElementExpressionInVarCastWithAutoFormat() {
    var f = new GosuClassFile (
        "package some.pkg\n" +
        "class One {\n" +
        "  function bar() {\n" +
        "    var intArray = new int[] {1, 2}\n" +
        "    var i = [[(java.lang.Integer)^^ intArray[1]" +
        "  }]]\n" +
        "}")
    test({f}, "intArray[1] as java.lang.Integer\n  }")
  }

  function testArrayElementExpressionInVarCast() {
    var f = new GosuClassFile (
        "package some.pkg\n" +
        "class One {\n" +
        "  function bar() {\n" +
        "    var intArray = new int[] {1, 2}\n" +
        "    var i = [[(java.lang.Integer)^^ intArray[1] ]]\n" +
        "  }\n" +
        "}")
    test({f}, "intArray[1] as java.lang.Integer ")
  }

  function testArrayElementExpressionInVarCastInProgram() {
    var f = new GosuProgramFile("some/pkg/OnePrg.gsp",
        "function bar() {\n" +
        "  var intArray = new int[] {1, 2}\n" +
        "  var i = [[(java.lang.Integer)^^ intArray[1] ]]\n" +
        "}\n")
    test({f}, "intArray[1] as java.lang.Integer ")
  }

  function testArrayElementExpressionInVarCastInProgramTopLevel() {
    var f = new GosuProgramFile("some/pkg/OnePrg.gsp",
        "var intArray = new int[] {1, 2}\n" +
        "var i = [[(java.lang.Integer)^^ intArray[1] ]]")
    test({f}, "intArray[1] as java.lang.Integer ")
  }

  function testArrayElementExpressionInSameNameVarsCastInProgram() {
    var f = new GosuProgramFile("some/pkg/OnePrg.gsp",
        "var _intArray = new int[] {1, 2}\n" +
        "var _i = [[(java.lang.Integer)^^ _intArray[1] ]]\n" +
        "function bar() {\n" +
        "  var intArray = new int[] {1, 2}\n" +
        "  var i = (java.lang.Integer) intArray[1]\n" +
        "}\n")
    test({f}, "_intArray[1] as java.lang.Integer ")
  }

  //fixed in PL-19048, Fix local var conflict with field in program
  function testArrayElementExpressionInSameNameVarsCastInProgram1() {
    var f = new GosuProgramFile("some/pkg/OnePrg.gsp",
        "var _intArray = new int[] {1, 2}\n" +
        "var _i = (java.lang.Integer) _intArray[1]\n" +
        "function bar() {\n" +
        "  var intArray = new int[] {1, 2}\n" +
        "  var i = [[(java.lang.Integer)^^ intArray[1] ]]\n" +
        "}\n")
    test({f}, "intArray[1] as java.lang.Integer ")
  }

  function test(resources: GosuTestingResource[], replace: String) {
    var markers = getAllMarkers(resources.map(\r -> configureByText(r.fileName, r.content)))
    var initialCaret = markers.getCaret(MarkerType.CARET1)
    assertTrue("No initial caret location defined.", initialCaret != null)
    assertEquals("No range carets location defined.", 1, markers.Ranges.size())

    var content = initialCaret.Editor.Document.Text
    var expected = content.substring(0, markers.Ranges.get(0).StartOffset) + replace + content.substring(markers.Ranges.get(0).EndOffset)

    initialCaret.Editor.CaretModel.moveToOffset(initialCaret.offset);
    findAndInvokeIntentionAction(highlightErrors(), "Change Java-style cast to Gosu-style cast", initialCaret.Editor, initialCaret.File)

    var found = initialCaret.Editor.Document.Text
    assertEquals("Texts do not match, auto edit failed", expected, found)
  }
}