/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.refactor

uses com.intellij.openapi.application.Result
uses com.intellij.openapi.command.WriteCommandAction
uses com.intellij.psi.PsiClass
uses com.intellij.psi.PsiFile
uses com.intellij.psi.impl.source.PsiFileImpl
uses com.intellij.refactoring.copy.CopyClassesHandler
uses gw.plugin.ij.framework.GosuTestCase
uses gw.plugin.ij.framework.generator.GosuTestingResource
uses gw.plugin.ij.framework.generator.ResourceFactory
uses gw.plugin.ij.lang.psi.impl.GosuClassFileImpl
uses gw.plugin.ij.lang.psi.impl.statements.typedef.GosuClassDefinitionImpl
uses gw.testharness.Disabled

uses java.util.ArrayList
uses java.util.HashMap
uses java.util.List

class CopyClassTest extends GosuTestCase
{

  function testCopyInSamePackage() {
    test("some.pkg",
         "GosuClass_Copy",{
         "package [[some.pkg]]\n" +
         "class [[GosuClass]] {\n" +
         "}"
         })
  }

  @Disabled("dpetrusca", "The infrastructure in this test does not support copy to a different package")
  function testCopyIntoAnotherPackage() {
      test("another.pkg",
           "GosuClass_Copy",{
           "package [[some.pkg]]\n" +
           "class [[GosuClass]] {\n" +
           "}"
           })
  }

  function test(newPackageName: String, newClassName: String, texts: List <String>) {
      testImpl(newPackageName, newClassName,  texts.map(\elt -> ResourceFactory.create(elt)))
  }

  function testImpl(newPackageName: String, newClassName: String, files: List <GosuTestingResource>) {
    var psiFiles = new ArrayList <PsiFile>()
    for (f in files) {
      psiFiles.add(configureByText(f.fileName, f.content))
    }
    var fileToClasses = new HashMap <PsiFile, PsiClass[]>()
    var gsClassFile1 = (psiFiles.get(0) as PsiFileImpl).calcTreeElement().Psi as GosuClassFileImpl
    fileToClasses.put(gsClassFile1, {gsClassFile1.getClasses()[0]})

    new WriteCommandAction( psiFiles.get(0).Project, {} ) {
      override function run( result: Result ) {
        var copiedPsiClassDef = CopyClassesHandler.doCopyClasses(
            fileToClasses, newClassName, psiFiles.get(0).ContainingDirectory, Project ) as GosuClassDefinitionImpl

        var strNewSource = files.get(0).content.toString()
        strNewSource = strNewSource.replaceFirst("\\[\\[(\\w+\\.)+\\w+\\]\\]", newPackageName)
        strNewSource = strNewSource.replaceAll("\\[\\[\\w+\\]\\]", newClassName)

        assertEquals( strNewSource, copiedPsiClassDef.Parent.Text )

        var type = gw.lang.reflect.TypeSystem.getByFullNameIfValid(newPackageName+"."+newClassName)
        assertNotNull(type)
        assertTrue( type.Valid )
      }
    }.execute()
  }
}