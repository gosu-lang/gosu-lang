/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi

class PsiClassTest extends GosuPsiTestCase
{
  function testIncremental_EmptyClass()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestClass1.gs",
      "package some.pkg\n" +
      "class TestClass1 {\n" +
      "}" )
  }

  function testIncremental_EmptyClassWithModifier()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestClass1.gs",
      "package some.pkg\n" +
      "internal class TestClass1 {\n" +
      "}" )
  }

  function testEmptyClassWithTrailingWhitespace()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestClass1.gs",
      "package some.pkg\n" +
      "class TestClass1 {\n" +
      "}\n" )
  }
  function testEmptyClassWithTrailingWhitespace2()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestClass1.gs",
      "package some.pkg\n" +
      "class TestClass1 {\n" +
      "}\n\n" )
  }
  function testEmptyClassWithTrailingWhitespace3()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestClass1.gs",
      "package some.pkg\n" +
      "class TestClass1 {\n" +
      "}\n \n" )
  }
  function testEmptyClassWithTrailingWhitespace4()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestClass1.gs",
      "package some.pkg\n" +
      "class TestClass1 {\n" +
      " \n }\n \n" )
  }

  function testErrant_EmptyClass_NameNotMatch()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestClass1.gs",
      "package some.pkg\n" +
      "class TestClass1_bad {\n" +
      "" )
  }
  function testErrant_EmptyClass_MissingClosingBrace()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestClass1.gs",
      "package some.pkg\n" +
      "class TestClass1 {\n" +
      "" )
  }
  function testErrant_EmptyClass_MissingOpenBrace()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestClass1.gs",
      "package some.pkg\n" +
      "class TestClass1 \n" +
      "}" )
  }
  function testErrant_EmptyClass_MissingBraces()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestClass1.gs",
      "package some.pkg\n" +
      "class TestClass1 \n" +
      "" )
  }
  function testErrant_EmptyClass_MissingClassKeyword()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestClass1.gs",
      "package some.pkg\n" +
      "TestClass1 \n" +
      "" )
  }
  function testErrant_EmptyClass_BadModifier()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestClass1.gs",
      "package some.pkg\n" +
      "pub class TestClass1 {\n" +
      "}" )
  }
  function testErrant_EmptyClass_BadModifiers()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestClass1.gs",
      "package some.pkg\n" +
      "junk pub class TestClass1 {\n" +
      "}" )
  }

  function testClassWithUsesStatement()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestClass1.gs",
      "package some.pkg\n" +
      "uses java.lang.StringBuilder\n" +
      "class TestClass1 {\n" +
      "}" )
  }
  function testClassWithUsesStatements()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestClass1.gs",
      "package some.pkg\n\n" +
      "uses java.lang.StringBuilder\n" +
      "uses java.util.ArrayList\n" +
      "uses java.io.*\n\n" +
      "class TestClass1 {\n" +
      "}" )
  }
  function testErrant_ClassWithBadUsesStatements()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestClass1.gs",
      "package some.pkg\n\n" +
      "uses java.lang.StringBuilder.;\n" +
      "uses java.util.ArrayList<dumb>\n" +
      "uses java.io\n\n" +
      "class TestClass1 {\n" +
      "}" )
  }

  function testEmptyInnerClass()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestClass1.gs",
      "package some.pkg\n" +
      "class TestClass1 {\n" +
      "  class InnerClass1 {\n" +
      "  }\n" +
      "}" )
  }

  function testErrant_JavaStyleVarDeclRecovery()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/Boom.gs",
      "package some.pkg\n" +
      "uses java.util.ArrayList\n" +
      "\n" +
      "class Boom extends ArrayList {\n" +
      "  var _x : String as readonly PProp\n" +
      "\n" +
      "  construct()\n" +
      "  {\n" +
      "    super()\n" +
      "    //this()\n" +
      "  }\n" +
      "  function foo()\n" +
      "  {\n" +
      "    print( PProp )\n" +
      "  // } missing close bract\n" +
      "\n" +
      "  static class Foo extends Boom\n" +
      "  { //asd\n" +
      "    construct()\n" +
      "    {\n" +
      "      super()\n" +
      "    }\n" +
      "  }\n" +
      "}" );
  }


  function testErrant_UnterminatedMultilineComment()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestClass1.gs",
      "package some.pkg\n" +
      "class TestClass1 {\n" +
      "  /*\n" +
      "  class InnerClass1 {\n" +
      "  }\n" +
      "}" )
  }

  function testErrant_DuplicateFields()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestClass1.gs",
      "package some.pkg\n" +
      "class Uhoh {\n" +
        "var w : Fred\n" +
        "var w : Fred\n" +
        "var w : Fred\n" +
     "}" )
  }

  function testAnonymousInnerClassWithInnerClass()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestClass1.gs",
      "package some.pkg\n" +
      "class Blue {\n" +
      "  function foo() {\n" +
      "    new Runnable() {\n" +
      "      override function run() {\n" +
      "      }\n" +
      "      class Glue implements Runnable{\n" +
      "        override function run(){\n" +
      "        }\n" +
      "      }\n" +
      "    }.run()\n" +
      "  }\n" +
      "}" )
  }
}