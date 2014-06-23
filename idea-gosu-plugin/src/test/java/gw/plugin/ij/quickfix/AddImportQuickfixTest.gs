/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.quickfix

uses gw.plugin.ij.framework.GosuTestCase
uses gw.plugin.ij.framework.generator.ResourceFactory
uses gw.testharness.Disabled

class AddImportQuickfixTest extends GosuTestCase {
  function testImportGosuClass() {
    ResourceFactory.createFile(this,
        "package pkg2\n" +
            "class Class2 {\n" +
            "  function getInt() : int {" +
            "    return 1\n" +
            "  }\n" +
            "}"
    )
    test(
        "package pkg1\n\n" +
            "[[[uses pkg2.Class2\n\n]]]" +
            "class Class1 {\n" +
            "  function test() {\n" +
            "    new Cl^^ass2().getInt()\n" +
            "  }\n" +
            "}"
    );
  }

  function testImportJavaClass() {
    test(
        "package pkg1\n" +
            "\n"+
            "[[[uses javax.swing.JFrame\n]]]" +
            "\n"+
            "class Class1 {\n" +
            "  function test() {\n" +
            "    new JFr^^ame().addNotify()\n" +
            "  }\n" +
            "}"
    );
  }

  function testImportGosuEnh() {
    ResourceFactory.createFile(this,
        "package pkg2\n" +
            "class Class2 {\n" +
            "  function getInt() : int {" +
            "    return 1\n" +
            "  }\n" +
            "}"
    )
    test(
        "package pkg1\n" +
            "\n"+
            "[[[uses pkg2.Class2\n]]]" +
            "\n"+
            "enhancement EnhTest : Class^^2 {\n" +
            "  function getString() : int {\n" +
            "    return \"1\"\n" +
            "  }\n" +
            "}"
    );
  }

  function testImportGosuAnnotations() {
    ResourceFactory.createFile(this,
        "package pkg2\n" +
            "class GosuAnnotationClass implements IAnnotation{ \n" +
            "   private var _value : String\n" +
            "   construct(value : String) {\n" +
            "   _value = value\n" +
            "   } \n" +
            "}"
    )
    test(
        "package pkg1\n\n" +
            "[[[uses pkg2.GosuAnnotationClass\n\n]]]" +
            "@GosuAnnotation^^Class(\"test\")\n" +
            "class MyClass3 {\n" +
            "    private var _field3 : String\n" +
            " } \n"
    );
  }

  function testImportProgram() {
    ResourceFactory.createFile(this,
        "package pkg2\n" +
            "class MyClass {\n" +
            "  function getInt() : int {" +
            "    return 1\n" +
            "  }\n" +
            "}"
    )
    test(
        "//PROGRAM, pkg1/MyProgram \n" +
            "[[[uses pkg2.MyClass\n]]]" +
            "\n"+
            "var x = new My^^Class()"
    );
  }

  //Templates do not work yet.
  @Disabled("dpetrusca", "fix in phase 2")
  function testImportTemplates() {
    ResourceFactory.createFile(this,
        "package pkg2\n" +
            "class MyClass {\n" +
            "  function myMethod() : String {" +
            "    return \"Test\"\n" +
            "  }\n" +
            "}"
    )
    test(
        "//TEMPLATE, pkg1/MyTemplate \n" +
            "[[[\<%\nuses pkg2.MyClass\n%>\n]]]" +
            "\<%var x = new My^^Class()\n" +
            "x.myMethod() %>\n"
    );
  }

  function testImportInGosuInnerClass() {
    ResourceFactory.createFile(this,
        "package pkg2\n" +
            "class MyClass { \n" +
            "    static class InnerClass {\n" +
            "        static public function returnString() : String {\n" +
            "            return \"test1\" \n" +
            "        } \n" +
            "    }\n" +
            "}"
    )
    test(
        "package pkg1\n" +
            "\n"+
            "[[[uses pkg2.MyClass\n]]]" +
            "\n"+
            "class Class1 {\n" +
            "  function test() {\n" +
            "    new My^^Class().returnString()\n" +
            "  }\n" +
            "}"
    );
  }

  function testImportGosuAnonInnerClass() {
    ResourceFactory.createFile(this,
        "package pkg2\n" +
            "class MyClass {\n" +
            "   function actionOne() { \n" +
            "        print(\"test1\")\n" +
            "   }\n" +
            "}\n"
    )
    test(
        "package pkg1\n" +
            "\n"+
            "[[[uses pkg2.MyClass\n]]]" +
            "\n"+
            "class GosuClass {\n" +
            "    public function testInner() {\n" +
            "        var test = new My^^Class () {\n" +
            "            var x = \"test\"\n" +
            "        }\n" +
            "    }\n" +
            "}"
    )
  }

  function testImportJavaAnonInnerClass() {
    test(
        "package pkg\n" +
        "\n"+
            "[[[uses java.lang.Runnable\n]]]" +
            "\n"+
            "class GosuClass {\n" +
            "  function doit() {\n" +
            "    new Runn^^able(){\n" +
            "      var x = 5\n" +
            "      override function run(){\n" +
            "        x = 10\n" +
            "      }\n" +
            "    }.run()\n" +
            "  }\n" +
            "}"
    )
  }

  function testImportUsing() {
    test(
        "package pkg\n" +
            "\n"+
            "[[[uses java.io.FileWriter\n]]]" +
            "\n"+
            "class GosuClass {\n" +
            "function method() {\n" +
            "  using( var reader = new FileReader ( \"c:\\temp\\usingfun.txt\" ),\n" +
            "         var writer = new File^^Writer ( \"c:\\temp\\usingfun2.txt\" ) ){\n" +
            "    var char = reader.read()\n" +
            "    writer.write( char )\n" +
            "  }\n" +
            "}\n"
    )
  }

  @Disabled("dpetrusca", "works in real life")
  function testImportGenericTypeWithSpaceBetween() {
    test(
        "package prg\n\n" +
        "[[[uses java.util.ArrayList\n\n]]]" +
        "class GosuClass {\n" +
        "  var myList = new Array^^List <String>()\n" +
        "}"
    )
  }

  // private inner class in another class
  @Disabled("dpetrusca", "fix in phase 2")
  function testNoPrivateInnerClassIsShownInOtherClass() {
    ResourceFactory.createFile(this,
      "package pkg1\n" +
      "class Class1 {\n" +
      "  private class InnerClass{ }\n" +
      "}"
    )
    testNoImportClass(
      "package pkg2\n" +
      "class Class2 {\n" +
      "  var variable : InnerClass^^\n" +
      "}"
    )
  }

  // package-protected class in another class
  @Disabled("dpetrusca", "fix in phase 2")
  function testNoPackageProtectedClassIsShownInOtherPackage() {
    ResourceFactory.createFile(this,
      "package pkg1\n" +
      "internal class GosuClass {\n" +
      "}"
    )
    testNoImportClass(
      "package pkg2\n" +
      "class TestClass {\n" +
      "  var variable : GosuClass^^\n" +
      "}"
    )
  }

  function test(text: String) {
    var markers = getAllMarkers({ResourceFactory.createFile(this, text)})
    var caret = markers.getCaret(CARET1);

    caret.Editor.CaretModel.moveToOffset(caret.offset);
    findAndInvokeIntentionAction(highlightErrors(), "Import Class", caret.Editor, caret.File)

    //get add import quick fix result and compare with the standard
    var found = normalize(caret.Editor.Document.Text)
    var expected = normalize(caret.Parent.TextWithDeltas)
    assertEquals("Could not execute add import correctly", expected, found)
  }

  function testNoImportClass(text: String) {
    var markers = getAllMarkers({ResourceFactory.createFile(this, text)})
    var caret = markers.getCaret(CARET1);

    caret.Editor.CaretModel.moveToOffset(caret.offset); cannotFindAndInvokeIntentionAction(highlightErrors(), "Import Class", caret.Editor, caret.File)
    cannotFindAndInvokeIntentionAction(highlightErrors(), "Import Class", caret.Editor, caret.File)
  }
}
