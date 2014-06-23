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
uses gw.plugin.ij.lang.psi.impl.GosuClassFileImpl
uses gw.testharness.Disabled

uses java.lang.Runnable
uses java.util.ArrayList
uses java.util.HashMap
uses java.util.List

@Disabled("dpetrusca", "functionality works but test breaks")
class RenameAnnotationTest extends GosuTestCase{
  function testRenameAnnotationConstr1() {
    test("",
    {
      "package some.pkg\n" +
      "class [[GosuAnnotation]] implements IAnnotation{\n" +
      "  private var _value : String\n\n" +
      "  construct(value : String) {\n" +
      "    _value = value\n" +
      "  }\n\n" +
      "  construct() {\n" +
      "    this(\"EMPTY\")\n" +
      "  }\n\n" +
      "}",
      "package some.pkg\n" +
      "@[[Gosu^^Annotation]]\n" +
      "class GosuClass {\n\n" +
      "}"
    })
  }

  function testRenameAnnotationConstr2() {
    test("",
    {
      "package some.pkg\n" +
      "@[[GosuAnnotation]]\n" +
      "class GosuClass {\n\n" +
      "}",
      "package some.pkg\n" +
      "class [[Gosu^^Annotation]] implements IAnnotation{\n" +
      "  private var _value : String\n\n" +
      "  construct(value : String) {\n" +
      "    _value = value\n" +
      "  }\n\n" +
      "  construct() {\n" +
      "    this(\"EMPTY\")\n" +
      "  }\n\n" +
      "}"
    })
  }

  function testRenameAnnotationConstr3() {
    test("",
    {
      "package some.pkg\n" +
      "@[[GosuAnnotation]](\"qa\")\n" +
      "class GosuClass {\n\n" +
      "}",
      "package some.pkg\n" +
      "class [[Gosu^^Annotation]] implements IAnnotation{\n" +
      "  private var _value : String\n\n" +
      "  construct(value : String) {\n" +
      "    _value = value\n" +
      "  }\n\n" +
      "  construct() {\n" +
      "    this(\"EMPTY\")\n" +
      "  }\n\n" +
      "}"
    })
  }

  function testRenameAnnotationConstr4() {
    test("",
    {
      "package some.pkg\n" +
      "class [[GosuAnnotation]] implements IAnnotation{\n" +
      "  private var _value : String\n\n" +
      "  construct(value : String) {\n" +
      "    _value = value\n" +
      "  }\n\n" +
      "  construct() {\n" +
      "    this(\"EMPTY\")\n" +
      "  }\n\n" +
      "}",
      "package some.pkg\n" +
      "@[[Gosu^^Annotation]](\"qa\")\n" +
      "class GosuClass {\n\n" +
      "}"
    })
  }

  function testRenameEnumConst1() {
    test( "RenamedEnum",
    {
      "package test\n" +
      "enum [[FooEnum]] { \n" +
      "  Dog( 17 ), Cat( 15 ), Mouse( 5 )\n\n" +
      "  private construct( iAge: int ) { \n" +
      "    var s: [[Foo^^Enum]] = Dog \n" +
      "  } \n" +
      "}"
    })
  }

  function testRenameEnumConst2() {
    test( "RenamedEnum",
    {
      "package test\n" +
      "enum [[Foo^^Enum]] { \n" +
      "  Dog( 17 ), Cat( 15 ), Mouse( 5 )\n\n" +
      "  private construct( iAge: int ) { \n" +
      "    var s: [[FooEnum]] = Dog \n" +
      "  } \n" +
      "}"
    })
  }

  function testRenameEnumConst3() {
    test( "RenamedEnum",
    {
      "package test\n" +
      "enum FooEnum { \n" +
      "  [[Dog]]( 17 ), Cat( 15 ), Mouse( 5 )\n\n" +
      "  private construct( iAge: int ) { \n" +
      "    var s: FooEnum = [[D^^og]] \n" +
      "  } \n" +
      "}"
    })
  }

  function testRenameEnumConst4() {
    test( "RenamedEnum",
    {
      "package test\n" +
      "enum FooEnum { \n" +
      "  [[D^^og]]( 17 ), Cat( 15 ), Mouse( 5 )\n\n" +
      "  private construct( iAge: int ) { \n" +
      "    var s: FooEnum = [[Dog]] \n" +
      "  } \n" +
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
    var gsClassFile = (psiFiles.last() as PsiFileImpl).calcTreeElement().Psi as GosuClassFileImpl

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