/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi

class PsiDuplicateMembersTest extends GosuPsiTestCase
{
  function testDuplicateMembers()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/DuplicateMembers.gs",
      "package some.pkg\n" +
      "class DuplicateMembers {\n" +
      "\n" +
      "  var _x : int\n" +
      "  var _x : int\n" +
      "  var _x : String  \n" +
      "\n" +
      "  function mass() {}\n" +
      "\n" +
      "  construct() {\n" +
      "\n" +
      "  }\n" +
      "  construct() {\n" +
      "\n" +
      "  }\n" +
      "  construct() {\n" +
      "  \n" +
      "  }\n" +
      "\n" +
      "  function foo( s: String ) {\n" +
      "    var w = s\n" +
      "  }\n" +
      "\n" +
      "  function foo( s: String ) {\n" +
      "\n" +
      "  }\n" +
      "\n" +
      "  function foo( s: String ) {\n" +
      "    var x = s\n" +
      "  }\n" +
      "\n" +
      "  property get Prop1() : String {\n" +
      "    return \"\"\n" +
      "  }\n" +
      "\n" +
      "  property get Prop1() : String {\n" +
      "      return \"\"\n" +
      "  }\n" +
      "\n" +
      "  property get Prop1() : String {\n" +
      "      return \"\"\n" +
      "  }\n" +
      "}" )
  }

}