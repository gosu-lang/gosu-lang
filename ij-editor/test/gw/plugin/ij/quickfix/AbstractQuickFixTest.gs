/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.quickfix

uses com.intellij.openapi.util.TextRange
uses gw.plugin.ij.framework.GosuTestCase
uses gw.plugin.ij.framework.MarkerType
uses gw.plugin.ij.framework.generator.GosuTestingResource

abstract class AbstractQuickFixTest extends GosuTestCase {
  function test(resources: GosuTestingResource[], intention: String) {
    var markers = getAllMarkers(resources.map(\r -> configureByText(r.fileName, r.content)))
    var initialCaret = markers.getCaret(MarkerType.CARET1)
    assertTrue("No initial caret location defined.", initialCaret != null)
    var deltas = markers.getDeltas()
    assertTrue("No Delta range defined.", deltas.size() > 0)

    var postCaret = markers.getPostCaret(CARET4)
    assertTrue("No post action caret location defined.", postCaret != null)

    initialCaret.Editor.CaretModel.moveToOffset(initialCaret.offset);
    findAndInvokeIntentionAction(highlightErrors(), intention, initialCaret.Editor, initialCaret.File)

    var found = postCaret.Editor.Document.Text
    assertEquals("Texts do not match, auto edit failed", postCaret.Parent.TextWithDeltas, found)

    var actualOffset = initialCaret.Editor.CaretModel.Offset
    var expectedOffset = postCaret.offset
    assertEquals("Caret is in the wrong place", expectedOffset, actualOffset)
  }

  function testNoIntention(resources: GosuTestingResource[]) {
    testNoIntention(resources, "")
  }

  function testNoIntention(resources: GosuTestingResource[], message: String) {
    var markers = getAllMarkers(resources.map(\r -> configureByText(r.fileName, r.content)))
    var initialCaret = markers.getCaret(MarkerType.CARET1)
    assertTrue("No initial caret location defined.", initialCaret != null)
    var deltas = markers.getDeltas()
    assertTrue("No Delta range defined.", deltas.size() >= 0)

    var postCaret = markers.getPostCaret(CARET4)
    assertTrue("No post action caret location defined.", postCaret != null)
    if (highlightErrors().Count as boolean){
      var textRang = new TextRange(highlightErrors().first().quickFixActionMarkers.first().Second.StartOffset,
          highlightErrors().first().quickFixActionMarkers.first().Second.EndOffset)
      var lineNumber = highlightErrors().first().quickFixActionMarkers.first().Second.Document.getLineNumber(textRang.StartOffset)
      var beginOfLine = highlightErrors().first().quickFixActionMarkers.first().Second.Document.getLineStartOffset(lineNumber)
      var endOfLine = highlightErrors().first().quickFixActionMarkers.first().Second.Document.getLineEndOffset(lineNumber)
      var lineRange = new TextRange(beginOfLine, endOfLine)
      var lineRangeText = highlightErrors().first().quickFixActionMarkers.first().Second.Document.getText(lineRange)
      fail("unexpedted error: " + highlightErrors().first().description +
          "\n RangeMarker : " + highlightErrors().first().quickFixActionMarkers.first().Second.Document.getText(textRang) +
          "\n The line number : " + lineNumber +
          " which is :" + lineRangeText +
          " message " + message
      )
    }
  }
}