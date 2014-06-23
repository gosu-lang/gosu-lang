/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.quickfix

uses gw.plugin.ij.framework.generator.GosuClassFile
uses gw.plugin.ij.actions.TypeSystemAwareAction
uses gw.lang.reflect.TypeSystem

class ChangeReturnTypeFixTest extends AbstractQuickFixTest {

  function testStringLiteral() {
    var f = new GosuClassFile (
        "package some.pkg\n" +
            "class One {\n" +
            "  function bar()[[[: String]]] {\n" +
            "    return ^^!!\"LITERAL\"\n" +
            "  }\n" +
            "}")

    test({f}, "Change method return type to 'java.lang.String'")
  }

  function testBooleanLiteral() {
    var f = new GosuClassFile (
        "package some.pkg\n" +
            "class One {\n" +
            "  function bar()[[[: boolean]]] {\n" +
            "    return ^^!!true" +
            "  }\n" +
            "}")

    test({f}, "Change method return type to 'boolean'")
  }

  function disabled_testIntExpression() {
    var f = new GosuClassFile (
        "package some.pkg\n" +
            "class One {\n" +
            "  function bar()[[[: int]]] {\n" +
            "    return ^^!!1+1" +
            "  }\n" +
            "}")

    TypeSystem.pushGlobalModule();
    try {
     test({f}, "Change method return type to 'java.lang.String'")
    } finally {
      TypeSystem.popGlobalModule();
    }
  }

  function testStringExpression() {
    var f = new GosuClassFile (
        "package some.pkg\n" +
            "class One {\n" +
            "  function bar()[[[: String]]] {\n" +
            "    return ^^!!\"cat\"+1" +
            "  }\n" +
            "}")

    test({f}, "Change method return type to 'java.lang.String'")
  }

  function disabled_testTransitive() {
    var f = new GosuClassFile (
        "package some.pkg\n" +
            "[[[uses java.util.Collections]]] \n" +
            "[[[uses java.util.List]]] \n" +
            "class One {\n" +
            "  function bar()[[[: List]]] {\n" +
            "    return ^^!!Collections.emptyList()\\n" +
            "  }\n" +
            "}")

    test({f}, "Change method return type to 'java.util.List'")
  }

  function testPreferFQN() {
    var f = new GosuClassFile (
        "package some.pkg\n" +
            "class One {\n" +
            "  function bar()[[[: java.util.List<String>]]] {\n" +
            "    var x : java.util.List<String>\n" +
            "    return ^^!!x" +
            "  }\n" +
            "}")

    test({f}, "Change method return type to 'java.util.List'")
}

  function disabled_TestConflicts() {

    var f1 = new GosuClassFile (
        "package some.pkg1\n" +
        "class Set {\n" +
        "}")

    var f2 = new GosuClassFile (
        "package some.pkg2\n" +
        "class Set {\n" +
        "}")

    var f3 = new GosuClassFile (
        "package some.pkg2\n" +
        "class SetProv {\n" +
         "  function get() : Set{\n" +
         "    return null\n" +
         "  }\n" +
        "}")

    var f = new GosuClassFile (
        "package some.pkg\n" +
        "\n" +
        "class One {\n" +
        "  function bar()[[[: some.pkg2.Set]]]{\n" +
        "    return ^^!!some.pkg2.SetProv.get()\n" +
        "  }\n" +
        "}")

    test({f1, f2, f3, f}, "Change method return type to 'some.pkg2.Set'")
  }
}