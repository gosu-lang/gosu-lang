/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi

class PsiStatementsTest extends GosuPsiTestCase
{

  function test_StringTemplate_OptionalExpressionSyntax_CollectionInitializer()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestProgram1.gsp",
      "${{1,2}}" )
  }


}