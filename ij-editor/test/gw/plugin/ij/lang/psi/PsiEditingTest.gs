/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi

uses com.intellij.psi.PsiFile
uses com.intellij.psi.impl.PsiDocumentManagerImpl
uses com.intellij.psi.impl.source.PsiFileImpl
uses gw.lang.reflect.TypeSystem
uses gw.plugin.ij.framework.CaretMarker
uses gw.plugin.ij.framework.MarkerType
uses gw.plugin.ij.util.ExecutionUtil

uses java.lang.Runnable
uses gw.plugin.ij.util.UIUtil

class PsiEditingTest extends GosuPsiTestCase {

  override function runInDispatchThread(): boolean {
    return false;
  }

  //==========class
  function testFunctionInsertionBeforeVariable() {
    assertModification_PsiTextEqualsSource(
    clsFileName,
    clsText, "function" )
  }

  function testPropertyInsertionBeforeVariable() {
    assertModification_PsiTextEqualsSource(
    clsFileName,
    clsText, "property" )
  }

  function testUnterminatedTypeParamsDeclInsertionBeforeImplements() {
    var text = "package some.pkg\n" +
      "class TestClass^^ implements java.util.List<E> {\n" +
      "}"
    assertModification_PsiTextEqualsSource(
    clsFileName,
    text, "<" )
  }

  //==========Enhancement

  function testFunctionInsertionBeforeVariableInEnhancement() {
    assertModification_PsiTextEqualsSource(
    enhFileName,
    enhText, "function" )
  }

  function testPropertyInsertionBeforeVariableInEnhancement() {
    assertModification_PsiTextEqualsSource(
    enhFileName,
    enhText, "property" )
  }

  function testUsesInsertionInEnhancement() {
    var text = "package some.pkg\n" +
      "^^\n" +
      "enhancement TestClassEnhancement : String {\n" +
      "  var _field1 : String\n" +
      "}"
    assertModification_PsiTextEqualsSource(
    enhFileName,
    text, "uses" )
  }

  //==========program
  function testFunctionInsertionBeforeVariableInProgram() {
    assertModification_PsiTextEqualsSource(
    prgFileName,
    prgText, "function" )
  }

  function testPropertyInsertionBeforeVariableInProgram() {
    assertModification_PsiTextEqualsSource(
    prgFileName,
    prgText, "property" )
  }

  //==========template
  function testFunctionInsertionBeforeVariableInTemplate() {
    assertModification_PsiTextEqualsSource(
    tmpFileName,
    tmpText, "function" )
  }

  function testPropertyInsertionBeforeVariableInTemplate() {
    assertModification_PsiTextEqualsSource(
    tmpFileName,
    tmpText, "property" )
  }

  function testParamsInsertionInTemplate() {
    assertModification_PsiTextEqualsSource(
    tmpFileName,
      "\<%@^^%>", "params")
  }


  //==========helpers
    static property get clsFileName() : String {
    return "some/pkg/TestClass.gs"
  }

    static property get clsText () : String {
    return "package some.pkg\n" +
      "class TestClass {\n" +
      "  ^^\n" +
      "  var _field1 : String\n" +
      "}"
  }

    static property get enhFileName() : String {
    return "some/pkg/TestClassEnhancement.gsx"
  }

    static property get enhText () : String {
    return "package some.pkg\n" +
      "enhancement TestClassEnhancement : String {\n" +
      "  ^^\n" +
      "  var _field1 : String\n" +
      "}"
  }

    static property get prgFileName() : String {
    return "some/pkg/TestClassProgram.gsp"
  }

    static property get prgText () : String {
    return "classpath \"../\"\n" +
      "  ^^\n" +
      "  var _field1 : String\n"
  }

    static property get tmpFileName() : String {
    return "some/pkg/TestClassTemplate.gst"
  }

    static property get tmpText () : String {
    return "\<%\n" +
      "  ^^\n" +
      "  var _field1 : String\n" +
      "%>"
  }

  function assertModification_PsiTextEqualsSource(strFileName: String, strSource: String, insertText: String) {
    var psiFile = configureByText(strFileName, strSource)
    var markers = getMarkers(psiFile)
    assertpsiTextEqualsSource(psiFile, removeMarkers(strSource))
    insert(markers.getCaret(MarkerType.CARET1), insertText)
    assertpsiTextEqualsSource(psiFile, insertAfterMarkers(strSource, insertText))
  }

  function insert(caret: CaretMarker, text: String) {
    runWriteActionInDispatchThread(new Runnable() {
    function run() {
      TypeSystem.pushModule(GosuModule);
      try {
        caret.Editor.CaretModel.moveToOffset(caret.offset)
        type(text, caret.Editor)
        PsiDocumentManagerImpl.getInstance(Project).commitAllDocuments()
      } finally {
        TypeSystem.popModule(GosuModule);
      }
    }
    }, true);
    UIUtil.settleModalEventQueue();
  }

  function removeMarkers(text: String): String {
    return text.replace(MarkerType.CARET1.markerText, "")
  }

  function insertAfterMarkers(text: String, insertText: String): String {
    var markerS = MarkerType.CARET1.markerText
    var offset = text.indexOf(markerS)
    return offset > 0 ? text.substring(0, offset ) + insertText + text.substring(offset + markerS.length) : text
  }

  function assertpsiTextEqualsSource(psiFile : PsiFile, text: String){
    var strPsiText = (psiFile as PsiFileImpl).calcTreeElement().Psi.Text
    assertEquals( text, strPsiText )
  }

}