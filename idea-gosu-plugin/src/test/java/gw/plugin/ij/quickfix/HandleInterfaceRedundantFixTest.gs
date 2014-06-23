/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.quickfix

uses gw.plugin.ij.framework.GosuTestCase
uses gw.plugin.ij.framework.MarkerType
uses gw.plugin.ij.framework.generator.GosuClassFile
uses gw.plugin.ij.framework.generator.GosuTestingResource

class HandleInterfaceRedundantFixTest extends GosuTestCase {

  function testRedundantInterface() {
    var f = new GosuClassFile (
                  "package some.pkg\n" +
                  "uses java.lang.*\n" +
                  "class A { \n" +
                  "  function foo<A extends [[String & ^^Comparable<String>]]>(a : A) : void { }\n" +
                  "}")
    test({f}, "String ")
  }

  function test(resources: GosuTestingResource[], replace: String) {
    var markers = getAllMarkers(resources.map(\r -> configureByText(r.fileName, r.content)))
    var initialCaret = markers.getCaret(MarkerType.CARET1)
    assertTrue("No initial caret location defined.", initialCaret != null)
    assertEquals("No range carets location defined.", 1, markers.Ranges.size())

    var content = initialCaret.Editor.Document.Text
    var expected = content.substring(0, markers.Ranges.get(0).StartOffset) + replace + content.substring(markers.Ranges.get(0).EndOffset)

    initialCaret.Editor.CaretModel.moveToOffset(initialCaret.offset);
    findAndInvokeIntentionAction(highlightErrors(), "Remove redundant interface", initialCaret.Editor, initialCaret.File)

    var found = initialCaret.Editor.Document.Text
    assertEquals("Texts do not match, auto edit failed", expected, found)
  }
}
