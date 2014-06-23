/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.quickfix

uses com.intellij.openapi.application.ApplicationManager
uses com.intellij.psi.PsiFile
uses com.intellij.psi.search.GlobalSearchScope
uses gw.plugin.ij.framework.GosuTestCase
uses gw.plugin.ij.framework.generator.GosuTestingResource
uses gw.plugin.ij.framework.generator.ResourceFactory
uses gw.plugin.ij.intentions.ITestableCreateClassFix
uses gw.plugin.ij.util.JavaPsiFacadeUtil
uses gw.testharness.Disabled

uses java.lang.Runnable
uses java.util.ArrayList
uses java.util.List

class CreateClassQuickfixTest extends GosuTestCase {
  //bug
  function testNewGosuClassInDifferentPackageCreatedByNewStatement() {
    test(
        "package pkg\n" +
            "class GosuClass {\n" +
            "  function foo(x: List) {\n" +
            "    foo(new MyGosuL^^ist()) \n" +
            "  } \n" +
            "}",
            "Create Gosu class",
            "pkg2",
            true,
            {"class MyGosuList implements List"}
    )
  }

   function testNewJavaClassInDifferentPackageByNewStatement() {
    test(
        "package pkg\n" +
            "class GosuClass {\n" +
            "  function foo(x: List) {\n" +
            "    foo(new MyGosuL^^ist()) \n" +
            "  } \n" +
            "}",
            "Create Java class",
            "pkg2",
            true,
            {"public class MyGosuList implements List"}
    )
  }

  //expect to pass, but fail
  function testNewGosuClassInSamePackageCreatedByNewStatement() {
    test(
        "package pkg\n" +
            "class GosuClass {\n" +
            "  function foo(x: List) {\n" +
            "    foo(new MyGosuL^^ist()) \n" +
            "  } \n" +
            "}",
            "Create Gosu class",
            "pkg",
            false,
            {"class MyGosuList implements List"}
    )
  }

  // passed manually
  // currently, it creates uses SuperGosuClass statement --> delete SuperGosuClass --> Alt+Enter --> recreate SuperGosuClass --> add another uses statement
  @Disabled("dpetrusca", "Works in real life.")
  function testNewGosuClassInDifferentPackageCreatedByVarStatement() {
    test(
        "package pkg\n" +
            "class GosuClass {\n" +
            "  var x: F^^oo \n" +
            "}",
            "Create Gosu class",
            "pkg2",
            true,
            {"class Foo"}
    )
  }

  //bug, we generate uses statement currently
  //same behavior for Gosu Class/Enum/Interface and Java Class/Enum/Interface
  function testNewJavaClassInSamePackageCreatedByVarStatement() {
    test(
        "package pkg\n" +
            "class GosuClass {\n" +
            "  var x: F^^oo \n" +
            "}",
            "Create Java class",
            "pkg",
            false,
            {"class Foo"}
    )
  }

  //bug, we generate uses statement currently
  //same behavior for Gosu Class/Enum/Interface and Java Class/Enum/Interface
  function testNewGosuClassInSamePackageCreatedByListParameter() {
    test(
        "package pkg\n" +
            "class GosuClass {\n" +
            "  var x: List<F^^oo> \n" +
            "}",
            "Create Gosu class",
            "pkg",
            false,
            {"class Foo"}
    )
  }


  // bug  - uses statement was added
  function testNewGoSuClassIsCreatedFromFunctionInvalidParameter() {
    test(
        "package pkg\n" +
            "class GosuClass {\n" +
            "  function foo(x: MyGosu^^Class) {\n" +
            "  } \n" +
            "}",
            "Create Gosu class",
            "pkg",
            false,
            {"class MyGosuClass"}
    )
  }
   // bug --  IllegalArgumentException
  function testNewGoSuClassIsCreatedFromFunctionIncludesInvalidParameter() {
    test(
        "package pkg\n" +
            "class GosuClass {\n" +
            "  function foo(x: MyGoSuClass) {\n" +
            "    foo(new MyGosuL^^ist()) \n" +
            "  } \n" +
            "}",
            "Create Gosu class",
            "pkg",
            false,
            {"class MyGosuList"}
    )
  }
  // bug - missing 'extends Throwable' currently
  // bug - no uses statement added
  @Disabled("dpetrusca", "unimportant")
  function testThrowableIsAddedForNewGosuClassCreatedFromThrowStatement() {
    test(
        "package pkg\n" +
            "class GosuClass {\n" +
            "  function foo() {\n" +
            "    if (true) {\n" +
            "         throw new MyGosu^^Throwable() \n" +
            "  } \n" +
            "}",
            "Create Gosu class",
            "pkg2",
            true,
            {"class MyGosuThrowable extends Throwable"}
    )
  }

  // bug -- create Uses statement currently
  function testNewGosuClassCreatedFromExtendsStatement() {
    test(
        "package pkg\n" +
            "class GosuClass extends Super^^GosuClass {\n" +
            "  } \n" +
            "}",
            "Create Gosu class",
            "pkg",
            false,
            {"class SuperGosuClass"}
    )
  }

  // bug -- quick fix options are unavailable currently (Gosu Class/Enum/Interface, Java Class/Enum/Interface)
   @Disabled("dpetrusca", "Gotta figure out what to do here. Foo is not a type literal.")
   function testNewGosuClassInSamePackageCreatedByVarStatement() {
    test(
        "package pkg\n" +
            "class GosuClass {\n" +
            "  var m = F^^oo\n" +
            "}",
            "Create Gosu class",
            "pkg",
            false,
            {"class Foo"}
    )
  }

  // private

  function test(text: String, quickFix: String, newPackage: String, usageNeeded: boolean, newClassContents: List <String>) {
    test({text}, quickFix, newPackage, usageNeeded, newClassContents)
  }

  function test(texts: List <String>, quickFix: String, newPackage: String, usageNeeded: boolean, newClassContents: List <String>) {
    testImpl(texts.map(\elt -> ResourceFactory.create(elt)), quickFix, newPackage, usageNeeded, newClassContents)
  }

  function testImpl(files: List <GosuTestingResource>, quickFix: String, newPackage: String, usesNeeded: boolean, newClassContents: List <String>) {
    if (JavaPsiFacadeUtil.findPackage(Project, newPackage) == null) {
      ResourceFactory.createFile(this, "package " + newPackage + " class _place_holder_ {}")
    }

    var psiFiles = new ArrayList <PsiFile>()
    for (f in files) {
      psiFiles.add(configureByText(f.fileName, f.content))
    }

    var markers = getAllMarkers(psiFiles.toArray(new PsiFile[psiFiles.size()]))
    var caret1 = markers.getCaret(CARET1);
    var textBefore = caret1.Editor.Document.Text

    var className = caret1.File.findElementAt(caret1.offset).Text

    caret1.Editor.CaretModel.moveToOffset(caret1.offset)
    var quickFixName = quickFix + " '" + className + "'"
    var fix = findIntentionAction(highlightErrors(), quickFixName, caret1.Editor, caret1.File);
    assertNotNull("Invalid quickfix " + quickFixName, fix);
    if (fix typeis ITestableCreateClassFix) {
      ApplicationManager.getApplication().runWriteAction(new Runnable() {
        override function run() {
          fix.invokeForTest(newPackage)
        }
      })
    }
    var textAfter = caret1.Editor.Document.Text

    var newPsiClass = JavaPsiFacadeUtil.findClass(Project, newPackage + "." + className, GlobalSearchScope.allScope(Project))
    assertNotNull("Quick fix did not create the class.", newPsiClass)

    if (usesNeeded) {
      assertTrue("Uses statement was not added", textBefore.length < textAfter.length)
      var usesText = "uses " + newPackage + "." + className
      assertTrue("Class must contain: " + usesText, textAfter.contains(usesText))
    } else {
      assertEquals("Class text was affected", textBefore.length, textAfter.length)
    }

    newClassContents.each(\s -> assertTrue("New class does not contain: " + s, newPsiClass.Text.contains(s)))
  }
}