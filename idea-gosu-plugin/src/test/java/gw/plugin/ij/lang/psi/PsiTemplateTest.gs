/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi

uses gw.testharness.Disabled

@Disabled("dpetrusca", "templates are not fully supported")
class PsiTemplateTest extends GosuPsiTestCase
{
  function testIncremental_EmptyTemplate()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestTemplate1.gst",
      "" )
  }

  function testEmptyTemplateWithTrailingWhitespace()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestTemplate1.gst",
      "\n" )
  }
  function testEmptyTemplateWithTrailingWhitespace2()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestTemplate1.gst",
      "\n\n" )
  }
  function testEmptyTemplateWithTrailingWhitespace3()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestTemplate1.gst",
      "\n \n" )
  }
  function testEmptyTemplateWithTrailingWhitespace4()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestTemplate1.gst",
      " \n \n \n" )
  }



  function testIncremental_EmptyExpressionTemplate()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestTemplate1.gst",
      "\<%=%>" )
  }

  function testEmptyExpressionTemplateWithTrailingWhitespace()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestTemplate1.gst",
      "\<%=\n%>" )
  }
  function testEmptyExpressionTemplateWithTrailingWhitespace2()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestTemplate1.gst",
      "\<%=\n\n%>" )
  }
  function testEmptyExpressionTemplateWithTrailingWhitespace3()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestTemplate1.gst",
      "\<%=\n \n%>" )
  }
  function testEmptyExpressionTemplateWithTrailingWhitespace4()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestTemplate1.gst",
      "\<%= \n \n \n%>" )
  }


  function testErrant_EmptyTemplate_BadIdentifier()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestTemplate1.gst",
      "\<%=bad%>" )
  }
  function testErrant_EmptyTemplate_BadIdentifiers()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestTemplate1.gst",
      "\<%=junk bad%>" )
  }

  function testTemplateWithUsesStatement()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestTemplate1.gst",
      "\<%uses java.lang.StringBuilder\n%>" )
  }
  function testTemplateWithUsesStatements()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestTemplate1.gst",
      "\<%uses java.lang.StringBuilder\n" +
      "uses java.util.ArrayList\n" +
      "uses java.io.*%>" )
  }
  function testErrant_TemplateWithBadUsesStatements()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestTemplate1.gst",
      "\<%uses java.lang.StringBuilder.;\n" +
      "uses java.util.ArrayList<dumb>\n" +
      "uses java.io%>" )
  }

  function testEmptyInnerClass()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestTemplate1.gst",
      "\<%class Inner1 {\n" +
      "}%>" )
  }

  function testEmptyInnerClass2()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestTemplate1.gst",
      "\<%class Inner1 {\n" +
      "  class Inner2 {\n" +
      "  }\n" +
      "}%>" )
  }

  function testTemplateParametersSyntax()
  {  assertIncremental_PsiTextEqualsSource(
    "some/pkg/TestTemplate1.gst",
    "\<%@params()%>")
  }

  function testTemplateExtendsSyntax()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestTemplate1.gst",
      "\<%@extends%>")
  }

  function testTemplate_OptionalSyntax_CollectionInitializer()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestTemplate1.gst",
      "\${{8}}")
  }

  function testErrant_JavaStyleVarDeclRecovery()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/Boom.gst",
      "\<%uses java.util.ArrayList\n" +
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
      "}%>" );
  }


  function testErrant_UnterminatedMultilineComment()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestTemplate1.gst",
      "\<%/* %>" )
  }

}