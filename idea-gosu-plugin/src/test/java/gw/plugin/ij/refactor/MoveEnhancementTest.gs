/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.refactor

uses com.intellij.openapi.application.ApplicationManager
uses com.intellij.openapi.application.Result
uses com.intellij.openapi.command.WriteCommandAction
uses com.intellij.psi.JavaPsiFacade
uses com.intellij.psi.PsiClass
uses com.intellij.psi.PsiFile
uses com.intellij.psi.impl.source.PsiFileImpl
uses com.intellij.refactoring.PackageWrapper
uses com.intellij.refactoring.move.moveClassesOrPackages.MoveClassesOrPackagesProcessor
uses com.intellij.refactoring.move.moveClassesOrPackages.SingleSourceRootMoveDestination
uses gw.plugin.ij.framework.GosuTestCase
uses gw.plugin.ij.framework.generator.GosuTestingResource
uses gw.plugin.ij.framework.generator.ResourceFactory
uses gw.plugin.ij.lang.psi.impl.GosuClassFileImpl
uses gw.plugin.ij.lang.psi.impl.GosuEnhancementFileImpl
uses gw.plugin.ij.lang.psi.impl.expressions.GosuIdentifierImpl

uses java.lang.Runnable
uses java.util.ArrayList
uses java.util.HashMap
uses java.util.List

class MoveEnhancementTest extends GosuTestCase
{

  //TestClass1 will be moved to some.pkg2, and TestClass2 will be modified accordingly
  function testMoveEnhancementFromSamePackageToDifferentOneUpdatesUses() {
    test("some.one.pkg2", "\nuses some.one.pkg2.TestClass1\n\n",{
        "package [[some.one.pkg]]\n" +
            "class TestClass1 {\n" +
            "}",
        "package some.one.pkg2\n" +
            "class Placeholder {\n" +
            "}",
        "package some.one.pkg\n" +
            "[[]]" +
            "enhancement TestClass2: TestClass1 {\n" +
            "}"
    })
  }

  function testMoveEnhancementFromDifferentPackageToAThirdPackageUpdatesUses() {
    test("some.one.pkg3", "some.one.pkg3", {
        "package [[some.one.pkg1]]\n" +
            "class TestClass1 {\n" +
            "}",
        "package some.one.pkg3\n" +
            "class Placeholder {\n" +
            "}",
        "package some.one.pkg2\n" +
            "uses [[some.one.pkg1]].TestClass1\n" +
            "enhancement TestClass2: TestClass1 {\n" +
            "}"
    })
  }

  function testMoveEnhancementFromDifferentPackageToAThirdPackageUpdatesFQN() {
    test("some.one.pkg3", "some.one.pkg3", {
        "package [[some.one.pkg1]]\n" +
            "class TestClass1 {\n" +
            "}",
        "package some.one.pkg3\n" +
            "class Placeholder {\n" +
            "}",
        "package some.one.pkg2\n" +
            "enhancement TestClass2: [[some.one.pkg1]].TestClass1 {\n" +
            "}"
    })
  }


  function test(newPackage: String, newUses: String, texts: List <String>) {
    testImpl(newPackage, newUses, texts.map(\elt -> ResourceFactory.create(elt)))
  }

  function testImpl(newPackage: String, newUses: String, files: List <GosuTestingResource>) {
    var psiFiles = new ArrayList <PsiFile>()
    for (f in files) {
      psiFiles.add(configureByText(f.fileName, f.content))
    }

    var fileToClasses = new HashMap <PsiFile, PsiClass[]>()
    var gsClassFile1 = (psiFiles.get(0) as PsiFileImpl).calcTreeElement().Psi as GosuClassFileImpl
    fileToClasses.put(gsClassFile1, {gsClassFile1.getClasses()[0]})

    var gsClassFile2 = (psiFiles.get(1) as PsiFileImpl).calcTreeElement().Psi as GosuClassFileImpl
    fileToClasses.put(gsClassFile2, {gsClassFile2.getClasses()[0]})

    var gsClassFile3 = (psiFiles.get(2) as PsiFileImpl).calcTreeElement().Psi as GosuEnhancementFileImpl

    new WriteCommandAction(psiFiles.get(1).Project, {})
        {
          override function run(result: Result)
          {
            var facade = JavaPsiFacade.getInstance(getProject());
            var newParentPackage = facade.findPackage(newPackage);
            assertNotNull(newParentPackage);
            var dirs = newParentPackage.getDirectories();
            assertEquals(dirs.length, 1);

            var dest = new SingleSourceRootMoveDestination(PackageWrapper.create(newParentPackage), dirs[0])

            var processor = new MoveClassesOrPackagesProcessor(getProject(), gsClassFile1.getTypeDefinitions(), dest, false, false, null)
            processor.setPreviewUsages(false)
            processor.run()
            gsClassFile1.reparseGosuFromPsi()
            gsClassFile2.reparseGosuFromPsi()
            gsClassFile3.reparseGosuFromPsi()
          }
        }.execute()

    ApplicationManager.getApplication().runReadAction(
        new Runnable() {
          function run() {
            //Assert that the move was successful
            var psiFile1 = psiFiles.get(0)
            var actual1 = psiFile1.Text
            var expectedText1 = files.get(0).content.toString().replaceAll("\\^\\^", "")
            expectedText1 = expectedText1.replaceAll("\\[\\[(\\w+\\.)+\\w+\\]\\]", newPackage)
            assertEquals(expectedText1, actual1)

            var psiFile2 = psiFiles.get(2)
            var actual2 = psiFile2.Text
            var expectedText2 = files.get(2).content.toString().replaceAll("\\^\\^", "")
            expectedText2 = expectedText2.replaceAll("\\[\\[.*\\]\\]", newUses)
            assertEquals(expectedText2, actual2)
          }
        })
  }

  function getNameIdentifier(gsClassFile: GosuClassFileImpl): GosuIdentifierImpl {
    return gsClassFile.getTypeDefinitions()[0].getNameIdentifier() as GosuIdentifierImpl
  }
}