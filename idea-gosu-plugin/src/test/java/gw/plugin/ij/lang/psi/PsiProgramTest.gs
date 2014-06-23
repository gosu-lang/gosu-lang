/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi

class PsiProgramTest extends GosuPsiTestCase
{
  function testIncremental_EmptyProgram()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestProgram1.gsp",
      "" )
  }

  function testEmptyProgramWithTrailingWhitespace()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestProgram1.gsp",
      "\n" )
  }
  function testEmptyProgramWithTrailingWhitespace2()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestProgram1.gsp",
      "\n\n" )
  }
  function testEmptyProgramWithTrailingWhitespace3()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestProgram1.gsp",
      "\n \n" )
  }
  function testEmptyProgramWithTrailingWhitespace4()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestProgram1.gsp",
      " \n \n \n" )
  }

  function testErrant_EmptyProgram_MissingClosingBrace()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestProgram1.gsp",
      "{" )
  }
  function testErrant_EmptyProgram_MissingOpenBrace()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestProgram1.gsp",
      "}" )
  }

  function testErrant_EmptyProgram_BadIdentifier()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestProgram1.gsp",
      "bad" )
  }
  function testErrant_EmptyProgram_BadIdentifiers()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestProgram1.gsp",
      "junk bad" )
  }

  function testProgramWithUsesStatement()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestProgram1.gsp",
      "uses java.lang.StringBuilder\n" )
  }
  function testProgramWithUsesStatements()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestProgram1.gsp",
      "uses java.lang.StringBuilder\n" +
      "uses java.util.ArrayList\n" +
      "uses java.io.*" )
  }
  function testErrant_ProgramWithBadUsesStatements()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestProgram1.gsp",
      "uses java.lang.StringBuilder.;\n" +
      "uses java.util.ArrayList<dumb>\n" +
      "uses java.io" )
  }

  function testEmptyInnerClass()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestProgram1.gsp",
      "class Inner1 {\n" +
      "}" )
  }

  function testEmptyInnerClass2()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestProgram1.gsp",
      "class Inner1 {\n" +
      "  class Inner2 {\n" +
      "  }\n" +
      "}" )
  }

  function testErrant_JavaStyleVarDeclRecovery()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/Boom.gsp",
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
      "some/pkg/TestProgram1.gsp",
      "/* " )
  }

  function testErrant_IncompleteFunctionAnnotationThatConsumesFunctionKeyword()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestProgram1.gsp",
      "@bad.\n" +
      "function foo() {\n" +
      "}")
  }

  function testErrant_BadStatementFollowedByFunctionWithNoBracesFollowedByJunk()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestProgram1.gsp",
      "for z ( n )\n" +
      "function foo() \n" +
      "  asdf\n" +
      "\n" +
      "fjx" );
  }

  function testErrant_BadStatementFollowedByFunctionFollowedByJunk()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestProgram1.gsp",
      "for z ( n )\n" +
      "function foo() {\n" +
      "  asdf\n" +
      "}\n" +
      "\n" +
      "fjx" );
  }

  function testErrant_BadStatementFollowedByIncompleteFunctionWithNoOpenBraceFollowedByJunk()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestProgram1.gsp",
      "for z ( n )\n" +
      "function foo() \n" +
      "  asdf\n" +
      "}\n" +
      "fjx" );
  }

  function testErrant_BadStatementFollowedByIncompleteFunctionWithNoCloseBraceFollowedByJunk()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestProgram1.gsp",
      "for z ( n ) {\n" +
      "function foo() \n" +
      "  asdf\n" +
      "\n" +
      "fjx" );
  }
}