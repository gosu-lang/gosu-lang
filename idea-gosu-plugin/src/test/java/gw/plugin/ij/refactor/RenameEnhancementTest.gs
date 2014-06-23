/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.refactor

uses com.intellij.codeInsight.TargetElementUtilBase
uses com.intellij.openapi.application.ApplicationManager
uses com.intellij.openapi.application.Result
uses com.intellij.openapi.command.WriteCommandAction
uses com.intellij.psi.PsiClass
uses com.intellij.psi.PsiFile
uses com.intellij.psi.impl.source.PsiFileImpl
uses com.intellij.refactoring.rename.RenameProcessor
uses gw.plugin.ij.framework.GosuTestCase
uses gw.plugin.ij.framework.generator.GosuTestingResource
uses gw.plugin.ij.framework.generator.ResourceFactory
uses gw.plugin.ij.lang.psi.impl.GosuEnhancementFileImpl
uses gw.testharness.Disabled

uses java.lang.Runnable
uses java.util.ArrayList
uses java.util.HashMap
uses java.util.List

class RenameEnhancementTest extends GosuTestCase {
  
  function testRenameTemplateDeclFromTemplateEnhancement() {
    test("newName",
    {
      "//TEMPLATE, some/pkg/SomeTemplate \n" +
      "\<%var x : [[SomeTemplateEnh]] %>\n",
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "}",
      "package some.pkg\n" +
      "enhancement [[Some^^TemplateEnh]]: GosuClass{\n" +
      "}\n"
    })
  }

  @Disabled("dpetrusca", "corner case")
  function testRenameProgramDeclFromProgramEnhancement() {
    test("newName",
    {
      "//TEMPLATE, some/pkg/SomeTemplate \n" +
      "\<%var x : [[SomeTemplateEnh]] %>\n"
      ,
      "//PROGRAM, some/pkg/GosuProgram \n"+
      "var x : String\n"
      ,
      "package some.pkg\n" +
      "enhancement EnhancesGosuProgram : some.pkg.[[Gosu^^Program]] {\n" +
      "}"
    })
  }

  function testRenameMethodDeclInSuperClassFromEnhancement() {
    test("newName",
    {
      "package some.pkg\n" +
      "class SuperGosuClass {\n" +
      "  function [[bar]]() {\n" +
      "  }\n" +
      "}",
      "package some.pkg\n" +
      "class GosuClass extends SuperGosuClass {\n" +
      "}",
      "package some.pkg\n" +
      "enhancement GosuClassEnhencement : some.pkg.GosuClass {\n" +
      "  function foo() {\n" +
      "    this.[[b^^ar]]()\n" +
      "  }\n" +
      "}"
    })
  }

  function test(newName: String, text: String) {
    test(newName, {text})
  }

  function test(newName: String, texts: List<String>) {
    testImpl(newName, texts.map(\elt -> ResourceFactory.create(elt)))
  }

  function testImpl(newName: String, files: List<GosuTestingResource>) {

    var psiFiles = new ArrayList<PsiFile>()
    for (f in files) {
      psiFiles.add(configureByText(f.fileName, f.content))
    }

    var caretOffset = getMarkers(psiFiles.last()).getCaretOffset();
    var flags = TargetElementUtilBase.getInstance().getReferenceSearchFlags()
    var elementAt = TargetElementUtilBase.getInstance().findTargetElement(getCurrentEditor(), flags, caretOffset)
    assertNotNull("Target element not found.", elementAt)

    var fileToClasses = new HashMap<PsiFile, PsiClass[]>()
    var gsClassFile = (psiFiles.last() as PsiFileImpl).calcTreeElement().Psi as GosuEnhancementFileImpl

    fileToClasses.put( gsClassFile, {gsClassFile.getClasses()[0]} )

    new WriteCommandAction( psiFiles.last().Project, {} )
    {
    override function run( result: Result )
    {
      var processor = new RenameProcessor(getProject(), elementAt, newName, false, false )
      processor.setPreviewUsages( false )
      processor.run()
      gsClassFile.reparseGosuFromPsi()
    }
    }.execute()

    ApplicationManager.getApplication().runReadAction(
      new Runnable() {
        function run() {

          for( i in 0..|psiFiles.size() ) {
            var psiFile = psiFiles.get(i)
            var actual = psiFile.Text

            var expectedText = files.get(i).content.toString().replaceAll("\\^\\^","")
            expectedText=expectedText.replaceAll("\\[\\[\\w+\\]\\]", newName)     //find all [[]] and replace with new name

            // Have to remove spaces b/c refactoring will apply formatting, which can put spaces before/after type names eg., ctor calls
            assertEquals(expectedText.remove(" "), actual.remove(" "))
          }
        }
      }
    )
  }

}