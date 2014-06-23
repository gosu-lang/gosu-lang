/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.refactor

uses com.intellij.codeInsight.TargetElementUtilBase
uses com.intellij.openapi.application.Result
uses com.intellij.openapi.command.WriteCommandAction
uses com.intellij.psi.PsiFile
uses com.intellij.psi.impl.source.PsiFileImpl
uses gw.plugin.ij.framework.GosuTestCase
uses gw.plugin.ij.framework.generator.GosuTestingResource
uses gw.plugin.ij.framework.generator.ResourceFactory
uses gw.plugin.ij.lang.psi.impl.GosuTemplateFileImpl
uses gw.plugin.ij.lang.psi.impl.expressions.GosuIdentifierImpl
uses gw.testharness.Disabled
uses gw.testharness.KnownBreak

uses java.util.ArrayList
uses java.util.List

@Disabled("dpetrusca", "templates are not fully supported")
class RenameTemplatesTest extends GosuTestCase {

  function testRenameTemplateVariable1() {
    test("newFoo",
    {
      "//TEMPLATE, test/MyTemplate \n" ,
//      "some/pkg/Template.gst",
      "\<%@ params(_city : String, _state : String, [[_today]] : java.util.Date) %>\n" +
      "City : \${_city}\n" +
      "State : \${_state}\n" +
      "Generated on \<%= [[_to^^day]] %>"
    })
  }

  function testRenameTemplateVariable2() {
    test("newFoo",
    {
      "//TEMPLATE, test/MyTemplate \n" ,
//      "some/pkg/Template.gst",
      "\<%@ params(_city : String, _state : String, [[_to^^day]] : java.util.Date) %>\n" +
      "City : \${_city}\n" +
      "State : \${_state}\n" +
      "Generated on \<%= [[_today]] %>"
    })
  }

  function testRenameTemplateParamDeclFromEmbedTemplateExpression() {
    test("newFoo",
    {
      "//TEMPLATE, test/MyTemplate \n" +
      "\<%@ params(_city : String, _state : String, _to^^day : java.util.Date) %>\n" +
      "City : \${_city}\n" +
      "State : \${_state}\n" +
      "Generated on \${[[_today]]}\n"
    })
  }

  @KnownBreak("", "", "")
  function testRenameExtendsClassFunctionDeclFromAlternateTemplateExpression() {
    test("newFoo",
    {
      "//TEMPLATE, test/MyTemplate \n" +
      "\<%@ extends some.pkg.SuperClass %>\<%= [[foo]](\"foo\") %>" ,
      "package some.pkg\n" +
      "class SuperClass {\n" +
      "  static function fo^^o(x : String) : String {\n" +
      "    return x\n" +
      "  }\n" +
      "}"
    })
  }

  //add code to check "SomeTemplate" is renamed
  function testGoToTemplateDeclFromTemplateEnhancement() {
    test("FooRenamed",
    {
      "//TEMPLATE, some/pkg/SomeTemplate \n" +
      "Great Enhancement Justice!",
      "package some.pkg\n" +
      "enhancement SomeTemplateEnh : [[Some^^Template]] {\n" +
      "  static function passThroughToRender() : String {\n" +
      "    return [[SomeTemplate]].renderToString()\n" +
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

    //ReFactoring the element
    var gsClassFile = (elementAt.Parent as PsiFileImpl).calcTreeElement().Psi as GosuTemplateFileImpl
    new WriteCommandAction ( psiFiles.last().Project, {} ) {
      override function run( result: Result ) {
        var classNameIdentifier = gsClassFile.getTypeDefinitions()[0].getNameIdentifier() as GosuIdentifierImpl
        classNameIdentifier.setName( newName )

      }
    }.execute()

    //Assert that the refactoring was successful
    assertTrue( gsClassFile.ContainingFile.VirtualFile.Name.toString().equals( newName + ".gs") )
    for(i in 0..|files.size()){
      var expectedText = files.get(i).content.toString().replaceAll("\\^\\^","")
      expectedText=expectedText.replaceAll("\\[\\[\\w+\\]\\]", newName)     //find all [[]] and replace with new name
      var actual = psiFiles.get(i).Text
      assertEquals(expectedText,actual)
    }
  }
}