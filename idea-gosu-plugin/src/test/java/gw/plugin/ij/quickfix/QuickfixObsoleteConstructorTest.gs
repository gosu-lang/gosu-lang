/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.quickfix

uses gw.plugin.ij.framework.GosuTestCase
uses gw.plugin.ij.framework.MarkerType
uses gw.plugin.ij.framework.generator.GosuClassFile
uses gw.plugin.ij.framework.generator.GosuTestingResource

class QuickfixObsoleteConstructorTest extends GosuTestCase {

  function testObsoleteConstructorNoArg() {
    var f = new GosuClassFile (
        "package some.pkg\n" +
        "class One {\n" +
        "  [[func^^!!tion One()]] {}\n" +
        "}")
    test({f}, "construct()")
  }

  function testObsoleteConstructorNoArgFormatting() {
    var f = new GosuClassFile (
        "package some.pkg\n" +
        "class One {\n" +
        "  [[  fu!!nc^^tion One()]] {}\n" +
        "}")
    test({f}, "construct()")
  }

  function testObsoleteConstructorOneArg() {
    var f = new GosuClassFile (
        "package some.pkg\n" +
        "class One {\n" +
        "  [[func^^!!tion One(i: int)]] {}\n" +
        "}")

    test({f}, "construct(i: int)")
  }

  function testObsoleteConstructorOneArgFormatting() {
    var f = new GosuClassFile (
        "package some.pkg\n" +
        "class One {\n" +
        "  [[  fu!!nc^^tion One(i: int)]] {}\n" +
        "}")

    test({f}, "construct(i: int)")
  }


  function test(resources: GosuTestingResource[], replace: String) {
    var markers = getAllMarkers(resources.map(\r -> configureByText(r.fileName, r.content)))
    var initialCaret = markers.getCaret(MarkerType.CARET1)
    assertTrue("No initial caret location defined.", initialCaret != null)
    var postCaret = markers.getCaret(MarkerType.CARET4)
    assertTrue("No post caret location defined.", postCaret != null)
    assertEquals("No range carets location defined.", 1, markers.Ranges.size())

    var content = initialCaret.Editor.Document.Text
    var expected = content.substring(0, markers.Ranges.get(0).StartOffset) + replace + content.substring( markers.Ranges.get(0).EndOffset)

    initialCaret.Editor.CaretModel.moveToOffset(initialCaret.offset);
    findAndInvokeIntentionAction(highlightErrors(), "Fix obsolete constructor syntax", initialCaret.Editor, initialCaret.File)

    var found = initialCaret.Editor.Document.Text
    assertEquals("Texts do not match, auto edit failed", expected, found)

    var actualOffset = initialCaret.Editor.CaretModel.Offset
    var expectedOffset = postCaret.Editor.CaretModel.Offset
    assertEquals("Caret is in the wrong place", expectedOffset, actualOffset)
  }
}