/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.quickfix

uses com.intellij.psi.PsiReference
uses gw.plugin.ij.framework.GosuTestCase
uses gw.plugin.ij.lang.psi.impl.GosuClassFileImpl


class AddImportQuickfixMultipleModuleTest extends GosuTestCase {

  var INTENTION_ACTION_NAME_IMPORT_CLASS = "Import Class";

  override function beforeClass() {
    initModules (new String[] {"test_module1<test_module2", "test_module2"})
    super.beforeClass();
  }

  function testImportGosuClassFromModule () {
    test ("test_module2", "pkg2/Class2.gs",
      "package pkg2\n" +
      "class Class2 {\n" +
      "  function getInt() : int {" +
      "    return 1\n" +
      "  }\n" +
      "}",
      "test_module1", "pkg1/Class1.gs",
      "package pkg1\n" +
      "class Class1 {\n" +
      "  function test() {\n" +
      "    new Cl^^ass2().getInt()\n" +
      "  }\n" +
      "}",
      "package pkg1\n" +
      "\n" +
      "uses pkg2.Class2\n" +
      "\n" +
      "class Class1 {\n" +
      "  function test() {\n" +
      "    new Class2().getInt()\n" +
      "  }\n" +
      "}")
  }

  function test (module2 : String, filenameClass2: String, textClass2: String,
                 module1 : String, filenameClass1: String, textClass1: String,
                 textClass1After: String) {

    // create Class2 be referenced Class1
    var psifile2 = configureByText (module2, filenameClass2, textClass2)
    assertNotNull("Could not get PsiFile for Class2.gs", psifile2)

    // create Class1 reference Class2
    var psiFile1 = configureByText(module1, filenameClass1, textClass1)
    assertNotNull("Could not get PsiFile for Class1.gs", psiFile1)

    var markers = getMarkers(psiFile1)
    var caretOffset = markers.getCaretOffset();

    var psiClass = (psiFile1 as  GosuClassFileImpl ).getPsiClass()
    var ref : PsiReference = psiClass.findReferenceAt(caretOffset)
    assertNotNull("Could not find reference for invalid type at offset " + caretOffset + " in Class1.gs", ref)

    // do import on Class2 in Class1
    getCurrentEditor().getCaretModel().moveToOffset(caretOffset);
    findAndInvokeIntentionAction(highlightErrors(), INTENTION_ACTION_NAME_IMPORT_CLASS, getCurrentEditor(), psiFile1)

    //get add import quick fix result and compare with the standard
    assertEquals ("Could not execute add import correctly", textClass1After, getCurrentEditor().getDocument().getText())
  }
}