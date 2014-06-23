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
uses gw.lang.reflect.gs.IGosuClass
uses gw.plugin.ij.framework.GosuTestCase
uses gw.plugin.ij.framework.generator.GosuTestingResource
uses gw.plugin.ij.framework.generator.ResourceFactory
uses gw.plugin.ij.lang.psi.impl.AbstractGosuClassFileImpl
uses gw.plugin.ij.lang.psi.impl.expressions.GosuReferenceExpressionImpl
uses gw.testharness.Disabled

uses java.lang.Runnable
uses java.util.ArrayList
uses java.util.List

class RenameClassesTest extends GosuTestCase {

  function testRenamingAbstractClassRenamesItsUsages() {
    test("IFooRenamed",
    {
      "package some.pkg\n" +
      "class Bar extends [[Foo]] {\n" +
      "  var a = 10\n" +
      "}",
      "package some.pkg\n" +
      "abstract class [[Fo^^o]] {\n" +
      "  var name : String\n" +
      "}"
    })
  }

  //  False negative, works when run on its own
  function testRenamingAbstractClassRenamesItsUsagesInDifferentPackage() {
    test("FooRenamed",
    {
      "package other.pkg\n" +
      "uses some.pkg.[[Foo]]\n"+
      "class Bar extends [[Foo]] {\n" +
      "  var a = 10\n" +
      "}",
      "package some.pkg\n" +
      "abstract class [[Fo^^o]] {\n" +
      "  var name : String\n" +
      "}"
    })
  }

  function testRenamingInterfaceRenamesItsUsages() {
    test("IFooRenamed",
    {
      "package some.pkg\n" +
      "class BarImpl implements [[IFoo]] {\n" +
      "  var a = 10\n" +
      "  var _bar : String\n" +
      "  construct( bar: String ) {\n" +
      "    _bar = bar\n" +
      "  }\n" +
      "  override function foo() : String {\n" +
      "    return _bar\n" +
      "  }\n" +
      "}",
      "package some.pkg\n" +
      "interface [[I^^Foo]] {\n" +
      "  function foo() : String\n" +
      "}"
    })
  }

  function testRenameInterfaceInImplementedClass() {
    test("IFooRenamed2",
    {
      "package some.pkg\n" +
      "interface [[IFoo]] {\n" +
      "  function foo() : String\n" +
      "}",
      "package some.pkg\n" +
      "class BarImpl implements [[I^^Foo]] {\n" +
      "  var a: String\n" +
      "  construct( bar: String ) {\n" +
      "    _bar = bar\n" +
      "  }\n" +
      "  override function foo() : String {\n" +
      "    return _bar\n" +
      "  }\n" +
      "}"
    })
  }

  function testRenamingClassRenamesItsUsages() {
    test("IBarRenamed3",
    {
      "package some.pkg\n" +
      "class FooClass {\n" +
      "  function foo() : String {\n" +
      "    var bar = new [[BarClass]]()\n" +
      "  }"+
      "}",
      "package some.pkg\n" +
      "class [[Bar^^Class]] {\n" +
      "}"
    }
    )
  }

  function testRenameClassInUsage() {
    test("BarRenamed4",
    {
      "package some.pkg\n" +
      "class [[BarClass]] {\n" +
      "}",
      "package some.pkg\n" +
      "class FooClass {\n" +
      "  function foo() {\n" +
      "    var bar = new [[Bar^^Class]]()\n" +
      "  }\n"+
      "}"
    })
  }

  function testRenameSingleClass() {
    test("FooRenamed4",
    {
      "package some.pkg\n" +
      "class [[FooClass]] {\n" +
      "  function foo() : String {\n" +
      "    var bar = new [[Foo^^Class]]()\n" +
      "    return null\n" +
      "  }\n"+
      "}"
    })
  }

  function testRenameClassInMultiplePlaces() {
    test("IFooRenamed5",
    {
      "package some.pkg\n" +
      "class [[IFoo]] {\n" +
      "  \n" +
      "}",
      "package some.pkg\n" +
      "class BarImpl{\n" +
      "  function one() : String {\n" +
      "    var _bar: String\n" +
      "    var c = new [[IFoo]]()\n"+
      "    return null\n"+
      "  }\n"+
      "  function two() : String {\n" +
      "    return _bar\n" +
      "  }\n"+
      "}",
      "package some.pkg\n" +
      "class FuBar {\n" +
      "  function foo() : String {\n" +
      "    var _bar: String\n" +
      "    var c = new [[I^^Foo]]()\n"+
      "    return null\n"+
      "  }\n"+
      "}"
    });
  }

  function testRenameExtendedClassInUsage() {
    test("IFooRenamed6",
    {
      "package some.pkg\n" +
      "class [[IFoo]] {\n" +
      "  \n" +
      "}",
      "package some.pkg\n" +
      "class BarExt extends [[IFoo]]{\n" +
      "  function one() : String {\n" +
      "    var a: String\n" +
      "    var c = new [[IFoo]]()\n" +
      "  }\n" +
      "}",
      "package some.pkg\n" +
      "class FuBar {\n" +
      "  function foo() : String {\n" +
      "    var _bar: String\n" +
      "    var c = new [[I^^Foo]]()\n" +
      "  }\n" +
      "}"
    });
  }

  function testRenamingExtendedClassRenamesItsUsages() {
    test("IFooRenamed",
    {
      "package some.pkg\n" +
      "class Bar extends [[IFoo]]{\n" +
      "  function one() : String {\n" +
      "  }\n" +
      "}",
      "package some.pkg\n" +
      "class FuBar {\n" +
      "  function foo() : String {\n" +
      "    var a = new BarExt()\n" +
      "    var c = new [[IFoo]]()\n" +
      "  }\n"+
      "}",
      "package some.pkg\n" +
      "class [[I^^Foo]] {\n" +
      "  \n" +
      "}"
    })
  }

  function testRenameClassViaUses() {
    test("FooRenamed",
    {
      "package other.pkg\n" +
      "class [[NewFooClass]] { \n"+
      "}",
      "package some.pkg\n" +
      "uses other.pkg.[[NewFoo^^Class]]\n" +
      "class FooClass {\n" +
      "  function myMethod(){\n"+
      "    var myMap = new [[NewFooClass]]()\n" +
      "  }\n"+
      "}"
    })
  }

  function testRenamingClassUpdatesUses() {
    test("FooRenamed",
    {
      "package some.pkg\n" +
      "uses other.pkg.[[NewFooClass]]\n" +
      "class FooClass {\n" +
      "  function myMethod(){\n"+
      "    var myMap = new [[NewFooClass]]()\n" +
      "  }\n"+
      "}",
      "package other.pkg\n" +
      "class [[NewFoo^^Class]] { \n"+
      "}"
    })
  }

  function testRenameClassNameFromOuter() {
    test("FooRenamed8",
    {
      "package some.pkg\n" +
      "class [[FooClass]] {\n" +
      "  function something() : String {\n" +
      "    return \"something\"\n" +
      "  }\n" +
      "  class Inner {\n" +
      "    function refOuter() : String {\n" +
      "      return ou^^ter.something()\n" +
      "    }\n" +
      "  }\n" +
      "}"
    })
  }

  function testGoToClassNameFromNestedBlockThenOuterThenBlockOuterPointer() {
    test("FooClassRenamed",
    {
      "package some.pkg\n" +
      "uses java.util.concurrent.Callable\n" +
      "class [[FooClass]] {\n" +
      "  function bar() {\n" +
      "    var b1 = \\-> new Callable() {\n" +
      "      override function call() : block():Callable<block():block():int> {\n" +
      "        return \\-> new Callable<block():block():int> (){\n" +
      "          override function call() : block():block():int {\n" +
      "            return \\->\\-> outer.ou^^ter.IntProperty\n" +
      "          }\n" +
      "        }\n" +
      "      }\n" +
      "    }\n" +
      "  }\n"  +
      "  property get IntProperty() : int {\n" +
      "    return 42\n" +
      "  }\n" +
      "}"
    })
  }

  function testRenameClassNameFromOuterAsAnonymousPropertyPath() {
    test("FooClassRenamed",
    {
      "package some.pkg\n" +
      "class [[FooClass]] {\n" +
      "  function something() {\n" +
      "    new java.lang.Runnable(){\n" +
      "      override function run() {}\n" +
      "    }.ou^^ter.something()\n" +
      "  }\n" +
      "}"
    })
  }

  function testRenameClassNameFromAnonymousClassFromDecl() {
    test("FooClassRenamed",
    {
      "package some.pkg\n" +
      "class [[Sample^^Class]] {\n" +
      "  function testOuter() : [[SampleClass]] {\n" +
      "    var me : [[SampleClass]] = null\n" +
      "    new java.lang.Runnable() {\n" +
      "      override function run() {\n" +
      "        me = outer\n" +
      "      }\n" +
      "    }.run()\n" +
      "    return me\n" +
      "  }\n" +
      "}"
    })
  }

  function testRenameClassNameFromAnonymousClassFromReturnType() {
    test("FooClassRenamed",
    {
      "package some.pkg\n" +
      "class [[SampleClass]] {\n" +
      "  function testOuter() : [[Sample^^Class]] {\n" +
      "    var me : [[SampleClass]] = null\n" +
      "    new java.lang.Runnable() {\n" +
      "      override function run() {\n" +
      "        me = outer\n" +
      "      }\n" +
      "    }.run()\n" +
      "    return me\n" +
      "  }\n" +
      "}"
    })
  }

  function testRenameClassNameFromAnonymousClassFromUsage() {
    test("FooClassRenamed",
    {
      "package some.pkg\n" +
      "class [[SampleClass]] {\n" +
      "  function testOuter() : [[SampleClass]] {\n" +
      "    var me : [[Sample^^Class]] = null\n" +
      "    new java.lang.Runnable() {\n" +
      "      override function run() {\n" +
      "        me = outer\n" +
      "      }\n" +
      "    }.run()\n" +
      "    return me\n" +
      "  }\n" +
      "}"
    })
  }

  function testRenameClassFromDeclRenamesClassEnhancement() {
    test("FooClassRenamed",
    {
      "package some.pkg1\n" +
      "enhancement GosuClassEnhancement : [[GosuClass]] {\n" +
      "}",
      "package some.pkg1\n" +
      "class [[Gosu^^Class]] {\n" +
      "}"
    })
  }

  function testRenameClassFromDeclRenamesUsesAndClassEnhancement() {
    test("FooClassRenamed",
    {
      "package some.pkg\n" +
      "uses other.pkg.[[GosuClass]]\n"+
      "enhancement GosuClassEnhancement : [[GosuClass]] {\n" +
      "}",
      "package other.pkg\n" +
      "class [[Gosu^^Class]] {\n" +
      "}"
    })
  }

  function testRenameClassDeclFromClassEnhancement() {
    test("FooClassRenamed",
    {
      "package other.pkg\n" +
      "class [[GosuClass]] {\n" +
      "}",
      "package some.pkg\n" +
      "uses other.pkg.[[GosuClass]]\n"+
      "enhancement GosuClassEnhancement : [[Gosu^^Class]] {\n" +
      "}"
    })
  }

  function testRenameClassDeclFromUsesInClassEnhancement() {
    test("FooClassRenamed",
    {
      "package other.pkg\n" +
      "class [[GosuClass]] {\n" +
      "}",
      "package some.pkg\n" +
      "uses other.pkg.[[Gosu^^Class]]\n"+
      "enhancement GosuClassEnhancement : [[GosuClass]] {\n" +
      "}"
    })
  }

  function testRenameInnerInnerClassNameFromInner() {
    test("InnerInnerNew",
    {
      "package some.pkg\n" +
      "class ResolveInnerFromInner {\n" +
      "  class Inner {\n" +
      "    function make() : [[InnerInner]] {\n" +
      "      return new [[Inner^^Inner]]()\n" +
      "    }\n" +
      "    class [[InnerInner]] { }\n" +
      "  }\n" +
      "}"
    })
  }

  function testRenameInnerInnerClassNameFromDecl() {
    test("FooClassRenamed",
    {
      "package some.pkg\n" +
      "class ResolveInnerFromInner {\n" +
      "  class Inner {\n" +
      "    function makeInnerInner() : [[InnerInner]] {\n" +
      "      return new [[InnerInner]]()\n" +
      "    }\n" +
      "    class [[Inner^^Inner]] { }\n" +
      "  }\n" +
      "}"
    })
  }

  function testRenameSiblingInnerClassNameFromInner() {
    test("FooClassRenamed",
    {
      "package some.pkg\n" +
      "class ResolveInnerFromOuter {\n" +
      "  class Inner {\n" +
      "    function makeSecondInner() : [[SecondInner]] {\n" +
      "      return new [[Second^^Inner]]()\n" +
      "    }\n" +
      "  }\n" +
      "  class [[SecondInner]] {}\n" +
      "}"
    })
  }

  function testRenameSiblingInnerClassNameFromDecl() {
    test("FooClassRenamed",
    {
      "package some.pkg\n" +
      "class ResolveInnerFromOuter {\n" +
      "  class Inner {\n" +
      "    function makeSecondInner() : [[SecondInner]] {\n" +
      "      return new [[SecondInner]]()\n" +
      "    }\n" +
      "  }\n" +
      "  class [[Second^^Inner]] {}\n" +
      "}"
    })
  }

  function testRenameClassNameFromOuterInAnonymousClass() {
    test("FooClassRenamed",
    {
      "package some.pkg\n" +
      "class [[GosuClass]] {\n" +
      "  function testOuter() : [[GosuClass]] {\n" +
      "    var me : [[GosuClass]] = null\n" +
      "    new java.lang.Runnable() {\n" +
      "      override function run() {\n" +
      "        me = ou^^ter\n" +
      "      }\n" +
      "    }.run()\n" +
      "    return me\n" +
      "  }\n" +
      "}"
    })
  }

  function testRenameClassNameDeclFromNewExpression() {
    test("FooClassRenamed",
    {
      "package some.pkg\n" +
      "class [[GosuClass]] {\n" +
      "  function makeGosuArray() : [[GosuClass]][] {\n" +
      "    return new [[GosuClass]][] {new [[Gosu^^Class]]()}\n" +
      "  }\n" +
      "}"
    })
  }

  function testRenameClassNameDeclFromNewArrayExpression() {
    test("FooClassRenamed",
    {
      "package some.pkg\n" +
      "class [[GosuClass]] {\n" +
      "  function makeGosuArray() : [[GosuClass]][] {\n" +
      "    return new [[Gosu^^Class]][] {new [[GosuClass]]()}\n" +
      "  }\n" +
      "}"
    })
  }

  function testRenameClassNameDeclFromArrayClassType() {
    test("FooClassRenamed",
    {
      "package some.pkg\n" +
      "class [[GosuClass]] {\n" +
      "  function makeGosuArray() : [[Gosu^^Class]][] {\n" +
      "    return new [[GosuClass]][] {new [[GosuClass]]()}\n" +
      "  }\n" +
      "}"
    })
  }

  function testRenameClassArrayFromClassDecl() {
    test("FooClassRenamed",
    {
      "package some.pkg\n" +
      "class [[Gosu^^Class]] {\n" +
      "  function makeGosuArray() : [[GosuClass]][] {\n" +
      "    return new [[GosuClass]][] {new [[GosuClass]]()}\n" +
      "  }\n" +
      "}"
    })
  }

  function testRenameClassFromGenerics() {
    test("FooClassRenamed",
    {
      "package some.pkg\n" +
      "uses java.lang.CharSequence\n\n" +
      "class [[GosuClass]] <T extends CharSequence> {\n" +
      "  function superIsBounded() : [[Gosu^^Class]]<? super T>\n" +
      "  {\n" +
      "    return null\n" +
      "  }\n" +
      "}"
    })
  }

  function testRenameGenericsFromClassName() {
    test("FooClassRenamed",
    {
      "package some.pkg\n" +
      "uses java.lang.CharSequence\n\n" +
      "class [[Gosu^^Class]] <T extends CharSequence> {\n" +
      "  function superIsBounded() : [[GosuClass]]<? super T>\n" +
      "  {\n" +
      "    return null\n" +
      "  }\n" +
      "}"
    })
  }

  @Disabled("dpetrusca", "templates are not fully supported")
  function testRenameClassFromTemplates() {
    test("FooRenamed",
    {
      "package some.pkg\n" +
      "class [[FooClass]] {\n" +
      "}",
      "//TEMPLATE, some.pkg/MyTemplate \n" +
      "\<%var x = new some.pkg.[[Foo^^Class]]() %>\n"
    })
  }

  @Disabled("dpetrusca", "templates are not fully supported")
  function testRenameClassInTemplateFromDecl() {
    test("FooRenamed",
    {
      "//TEMPLATE, some.pkg/MyTemplate \n" +
      "\<%var x = new some.pkg.[[FooClass]]() %>\n",
      "package some.pkg\n" +
      "class [[Foo^^Class]] {\n" +
      "}"
    })
  }

  function test(newName: String, text: String) {
    test(newName, {text})
  }

  function test(newName: String, texts: List<String>) {
    testImpl(newName, texts.map(\elt -> ResourceFactory.create(elt)))
  }

  static var N: int = 1

  function testImpl(newName: String, files: List<GosuTestingResource>) {
    newName = newName + N
    N++
    var psiFiles = new ArrayList<PsiFile>()
    for (f in files) {
      psiFiles.add(configureByText(f.fileName, f.content))
    }
    var caretOffset = getMarkers(psiFiles.last()).getCaretOffset();
    var flags = TargetElementUtilBase.getInstance().getReferenceSearchFlags()
    var elementAt = TargetElementUtilBase.getInstance().findTargetElement(getCurrentEditor(), flags, caretOffset)
    assertNotNull("Target element not found.", elementAt)

    //ReFactoring the element
    if( elementAt typeis GosuReferenceExpressionImpl ) {
      elementAt = elementAt.resolve()
      print( elementAt.Text )
    }
    var topClassFile = (elementAt.ContainingFile as PsiFileImpl).calcTreeElement().Psi as AbstractGosuClassFileImpl
    var classDefinition = elementAt typeis PsiClass ? elementAt : topClassFile.getTypeDefinitions()[0]
    var isTopLevel = classDefinition == topClassFile.getTypeDefinitions()[0]
    var gsClass : IGosuClass
    new WriteCommandAction ( psiFiles.last().Project, {} ) {
      override function run( result: Result ) {
        var processor = new RenameProcessor(getProject(), classDefinition, newName, false, false )
        processor.setPreviewUsages( false )
        processor.run()
        gsClass = topClassFile.reparseGosuFromPsi()
      }
    }.execute()

    ApplicationManager.getApplication().runReadAction(
      new Runnable() {
        function run() {
          //Assert that the refactoring was successful
          if (isTopLevel) {
            assertEquals(topClassFile.ContainingFile.VirtualFile.NameWithoutExtension.toString(), newName)
            assertEquals( newName, gsClass.RelativeName )
          }
          assertTrue( gsClass.Valid )

          for( i in 0..|psiFiles.size() ) {
            var psiFile = psiFiles.get(i)
            var actual = psiFile.Text

            var expectedText = files.get(i).content.toString().replaceAll("\\^\\^","")
            expectedText=expectedText.replaceAll("\\[\\[\\w+\\]\\]", newName)     //find all [[]] and replace with new name

            // Have to remove spaces b/c refactoring will apply formatting, which can put spaces before/after type names eg., ctor calls
            assertEquals(expectedText.remove(" ").replace("\n\n", "\n"), actual.remove(" ").replace("\n\n", "\n"))
          }
        }
      } )
  }
}