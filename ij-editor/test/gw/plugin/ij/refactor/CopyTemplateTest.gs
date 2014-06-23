/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.refactor

uses com.intellij.openapi.application.Result
uses com.intellij.openapi.command.WriteCommandAction
uses com.intellij.refactoring.copy.CopyFilesOrDirectoriesHandler
uses gw.plugin.ij.framework.GosuTestCase
uses gw.plugin.ij.framework.generator.ResourceFactory
uses gw.testharness.Disabled

@Disabled("dpetrusca", "templates are not fully supported")
class CopyTemplateTest extends GosuTestCase {

  function testCopySimpleTemplate() {
    testImpl("some.pkg.TestTemplate_Copy",
             "TestTemplate_Copy.gst",
             "//TEMPLATE, some/pkg/TestTemplate.gst\n" +
             "my <% print( 'hello' ) %> template")
  }

  function testCopy() {
    testImpl("some.pkg.TestTemplate_Copy",
             "TestTemplate_Copy.gst",
             "//TEMPLATE, some/pkg/TestTemplate.gst\n" +
             "my <% print( 'hello' ) %> template")
  }

  function testImpl(newFQNName: String, newName: String, text: String) {
    var file = ResourceFactory.create(text)
    var psiFile = configureByText(file.fileName, file.content)

    new WriteCommandAction(psiFile.Project, {}){
      override function run(result: Result){
        var copiedFile = CopyFilesOrDirectoriesHandler.copyToDirectory(psiFile, newName, psiFile.ContainingDirectory)
        assertTrue(copiedFile.Name == newName)
        var strNewSource = text.substring(text.indexOf("\n")+1, text.length())

        assertEquals(copiedFile.Text, strNewSource)

        var type1 = gw.lang.reflect.TypeSystem.getByFullNameIfValid(
            text.substring(text.indexOf(",")+2, text.indexOf("\n")).replace("/","."))
        var type2 = gw.lang.reflect.TypeSystem.getByFullNameIfValid(newFQNName)

        assertNotNull(type1)
        assertTrue(type1.Valid)
        assertNotNull(type2)
        assertTrue(type2.Valid)
      }
    }.execute()
  }
}