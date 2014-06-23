/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion

uses com.intellij.codeInsight.completion.CompletionType
uses com.intellij.codeInsight.lookup.LookupElement
uses gw.plugin.ij.framework.GosuTestCase
uses gw.plugin.ij.framework.generator.ResourceFactory

abstract class AbstractCodeCompletionTest extends GosuTestCase {

  function testCanInvoke(text: String) {
    testCanInvoke({text})
  }

  function testCanntInvoke(text: String) {
    testCanntInvoke({text})
  }

  function testCanInvoke(texts: String[]) {
    assertTrue("No completion results found.", test(texts).length > 0)
  }

  function testCanntInvoke(texts: String[]) {
    var val = test(texts)
    assertTrue("Completion results found: \n  " + val.map( \ elt -> elt.LookupString ).join("\n  "), val.length <= 0)
  }

  function testInsertModeCompletion(texts: String[], selectedItem : String, completedText : String ) {
    var markers = getAllMarkers(texts.map(\text -> ResourceFactory.createFile(this, text)))
    var caret = markers.getCaret(CARET1)
    caret.Editor.CaretModel.moveToOffset(caret.offset)
    var completionItems = complete( CompletionType.BASIC, 1, caret.Editor, null)
    //selectItem(completionItems[0], '\n')
    //fail("Please add Enter completion method here!!!")
    //manually test works, we do support automation for this according to Dumitru
  }

  function testReplaceModeCompletion(texts: String[], selectedItem : String, completedText : String ) {
    var markers = getAllMarkers(texts.map(\text -> ResourceFactory.createFile(this, text)))
    var caret = markers.getCaret(CARET1)
    caret.Editor.CaretModel.moveToOffset(caret.offset)
    var completionItems = complete( CompletionType.BASIC, 1, caret.Editor, null)
    //selectItem(completionItems[0], '\t')
    //fail("Please add Tab completion method here!!!")
    //manually test works, we do support automation for this according to Dumitru
  }

  function test(text: String, items: String[]) {
    test({text}, items)
  }

  function test(texts: String[], expectedItems: String[]) {
    test(texts, expectedItems, null)
  }

  function test(texts : String[], expectedItems : String[], stuffToType : String) {
    var completionItems = test(texts, stuffToType)
    assertTrue("No completion results found.", completionItems.length > 0)
    if (expectedItems != null) {
      var completionTexts = getCompletionTexts(completionItems)
      for (expected in expectedItems) {
        assertTrue("Completion results do not contain the item: " + expected + "\n" + completionTexts.join("\n"),
        completionTexts.contains(expected))
      }
    }
  }

  function test(texts: String[]) : LookupElement[] {
    return test(texts, null as String)
  }

  function test(texts: String[], stuffToType : String) : LookupElement[] {
    var markers = getAllMarkers(texts.map(\text -> ResourceFactory.createFile(this, text)))
    var caret = markers.getCaret(CARET1)
    var caret2 = markers.maybeGetPostCaret(CARET4)

    caret.Editor.CaretModel.moveToOffset(caret.offset)
    var completionItems = complete( CompletionType.BASIC, 1, caret.Editor, stuffToType)
    if (caret2 != null) {
      assertEquals("Document texts do not match", caret2.Parent.TextWithDeltas, caret.Editor.Document.Text)
      var actualOffset = caret.Editor.CaretModel.Offset
      var expectedOffset = caret2.offset
      assertEquals("Caret is in the wrong place", expectedOffset, actualOffset)
    }
    return completionItems
  }

  function shouldReturnType(): boolean {
    return true;
  }

  function getCompletionTexts(completionItems : LookupElement[]) : String[]{
    return  completionItems.map(\i -> {
        var item : String
        var p = new ElementPresentation()
        i.renderElement(p)
        return p.toString()  + (shouldReturnType() ? ": " + p.TypeText : "")
      })
  }

  function testNoItems(texts: String[], nonExtectedItems: String[]) {
    var completionItems = test(texts)
    if (completionItems.length > 0) {
      if (nonExtectedItems != null) {
        var completionTexts = getCompletionTexts(completionItems)
        for (nonExpected in nonExtectedItems) {
          assertTrue("Completion results should not contain the item: " + nonExpected + "\n" + completionTexts.join("\n"),
              !completionTexts.contains(nonExpected))
        }
      }
    }
  }
}