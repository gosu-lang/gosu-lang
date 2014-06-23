/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.refactor

uses com.intellij.openapi.application.ApplicationManager
uses com.intellij.openapi.application.Result
uses com.intellij.openapi.command.WriteCommandAction
uses com.intellij.psi.PsiClass
uses com.intellij.psi.PsiFile
uses com.intellij.psi.impl.source.PsiFileImpl
uses com.intellij.refactoring.rename.RenameProcessor
uses gw.plugin.ij.framework.GosuTestCase
uses gw.plugin.ij.lang.psi.impl.GosuClassFileImpl
uses gw.plugin.ij.lang.psi.impl.expressions.GosuIdentifierImpl

uses java.lang.Runnable
uses java.util.HashMap

class RenameClassTest extends GosuTestCase
{
  function testRenameClassFromClassDeclIdentifier()
  {
    var psiFile = configureByText(
      "some/pkg/TestClass1.gs",
      "package some.pkg\n" +
      "class TestClass1 {\n" +
      "}" )
    var fileToClasses = new HashMap<PsiFile, PsiClass[]>()
    var gsClassFile = (psiFile as PsiFileImpl).calcTreeElement().Psi as GosuClassFileImpl

    fileToClasses.put( gsClassFile, {gsClassFile.getClasses()[0]} )

    new WriteCommandAction( psiFile.Project, {} )
    {
      override function run( result: Result )
      {
        var processor = new RenameProcessor(getProject(), gsClassFile.getTypeDefinitions()[0], "TestClass1_Rename", false, false )
        processor.setPreviewUsages( false )
        processor.run()
//        var classNameIdentifier = getNameIdentifier( gsClassFile )
//        classNameIdentifier.setName( "TestClass1_Rename" )
        gsClassFile.reparseGosuFromPsi()
      }
    }.execute()

    ApplicationManager.getApplication().runReadAction(
      new Runnable() {
        function run() {
          var strNewSource =
            "package some.pkg\n" +
            "class TestClass1_Rename {\n" +
            "}"

          assertEquals( gsClassFile.Text, strNewSource )

          var type = gw.lang.reflect.TypeSystem.getByFullNameIfValid("some.pkg.TestClass1_Rename")
          assertNotNull( type )
          assertTrue( type.Valid )
          assertTrue( gsClassFile.ContainingFile.VirtualFile.Name.contains( "TestClass1_Rename" ) )
        }
      } )
  }

  function getNameIdentifier( gsClassFile: GosuClassFileImpl ) : GosuIdentifierImpl
  {
    return gsClassFile.getTypeDefinitions()[0].getNameIdentifier() as GosuIdentifierImpl
  }
}