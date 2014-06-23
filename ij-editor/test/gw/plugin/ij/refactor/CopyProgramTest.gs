/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.refactor

uses com.intellij.openapi.application.Result
uses com.intellij.openapi.command.WriteCommandAction
uses com.intellij.refactoring.copy.CopyFilesOrDirectoriesHandler
uses gw.plugin.ij.framework.GosuTestCase
uses gw.plugin.ij.framework.generator.ResourceFactory

class CopyProgramTest extends GosuTestCase
{

  function testCopyInTheSamePackage() {
    testImpl("some.pkg.TestProgram1_Copy",
             "TestProgram1_Copy.gsp",
             "//PROGRAM, some/pkg/TestProgram\n" +
             "print( 'hello' )")
  }

  function testImpl(newFQNName: String, newName: String, text: String) {
    var file = ResourceFactory.create(text)
    var psiFile = configureByText(file.fileName, file.content)

    new WriteCommandAction(psiFile.Project, {})
        {
          override function run(result: Result)
          {
            var copiedFile = CopyFilesOrDirectoriesHandler.copyToDirectory(psiFile, newName, psiFile.ContainingDirectory)
            assertTrue(copiedFile.Name == newName)
            var strNewSource = text.substring(text.indexOf("\n") + 1, text.length())

            assertEquals(copiedFile.Text, strNewSource)

            var type1 = gw.lang.reflect.TypeSystem.getByFullNameIfValid(
                text.substring(text.indexOf(",") + 2, text.indexOf("\n")).replace("/", "."))
            var type2 = gw.lang.reflect.TypeSystem.getByFullNameIfValid(newFQNName)

            assertNotNull(type1)
            assertTrue(type1.Valid)
            assertNotNull(type2)
            assertTrue(type2.Valid)
          }
        }.execute()
  }
}