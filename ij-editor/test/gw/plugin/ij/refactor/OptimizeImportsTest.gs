/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.refactor

uses com.intellij.codeInsight.actions.OptimizeImportsProcessor
uses com.intellij.openapi.application.ApplicationManager
uses com.intellij.openapi.application.Result
uses com.intellij.openapi.command.WriteCommandAction
uses com.intellij.openapi.util.Computable
uses com.intellij.psi.impl.source.PsiFileImpl
uses com.intellij.psi.util.ClassUtil
uses com.intellij.psi.util.PsiMatcherImpl
uses com.intellij.psi.util.PsiMatchers
uses com.intellij.psi.util.PsiTreeUtil
uses gw.plugin.ij.framework.GosuTestCase
uses gw.plugin.ij.lang.psi.api.statements.IGosuUsesStatementList
uses gw.plugin.ij.lang.psi.impl.GosuClassFileImpl
uses gw.plugin.ij.lang.psi.impl.expressions.GosuTypeLiteralImpl

uses java.util.Arrays
uses java.util.List

class OptimizeImportsTest extends GosuTestCase {

  function testRemovesUnused() {

    var gsFile = createClass(
      "some.pkg.OrgImp",
      {"java.lang.StringBuilder", "java.lang.StringBuffer"},
      "var _x : StringBuilder"
    )

    organizeImports(gsFile)
    assertEquals({"java.lang.StringBuilder"}, extractImports(gsFile))
  }

  function testUseWildcard() {

    var gsFile = createClass(
      "some.pkg.OrgImp",
        {
          "java.util.List",
          "java.util.Set",
          "java.util.Map",
          "java.util.EnumSet",
          "java.util.Vector",
          "java.util.HashSet",
          "java.util.HashMap"

        },
        "var _a : List \n" +
        "var _b : Set \n" +
        "var _c : Map \n" +
        "var _d : EnumSet \n" +
        "var _e : Vector \n" +
        "var _f : HashSet \n" +
        "var _g : HashMap \n"
    )

    organizeImports(gsFile)
    assertEquals({"java.util.*"}, extractImports(gsFile))
  }

  function testWildcardConflicts() {

    var gsFile = createClass(
      "some.pkg.OrgImp",
      {
        "java.util.List",
        "java.util.Set",
        "java.util.Map",
        "java.util.EnumSet",
        "java.util.Vector",
        "java.util.HashSet",
        "java.util.HashMap",
        "java.sql.Date",
        "java.sql.Blob",
        "java.sql.Clob",
        "java.sql.Driver",
        "java.sql.Ref"
      },
      "var _a : List \n" +
      "var _b : Set \n" +
      "var _c : Map \n" +
      "var _d : EnumSet \n" +
      "var _e : Vector \n" +

      "var _f : Date \n" +
      "var _g : Blob \n" +
      "var _h : Clob \n" +
      "var _j : Driver \n" +
      "var _k : Ref \n" +
      "var _l : HashMap \n" +
      "var _m : HashSet \n"

    )


    organizeImports(gsFile)
    assertEquals({
        "java.sql.*",
        "java.sql.Date",
        "java.util.*"
    }, extractImports(gsFile))
  }

  function testWildcardSettings() {

    var gsFile = createClass(
      "some.pkg.OrgImp",
      {"java.awt.Button"},
      "var _a : Button \n"
    )

    organizeImports(gsFile)
    assertEquals({
      "java.awt.*"
    }, extractImports(gsFile))
  }

  function testWildcardConflicts2() {

    var gsFile = createClass(
      "some.pkg.OrgImp",
        {
          "java.util.List",
          "java.util.Set",
          "java.util.Map",
          "java.util.EnumSet",
          "java.util.Vector",
          "java.util.HashSet",
          "java.awt.Button"
        },
        "var _a : List \n" +
        "var _b : Set \n" +
        "var _c : Map \n" +
        "var _d : EnumSet \n" +
        "var _e : Vector \n" +
        "var _f : Button \n" +
        "var _g : HashSet \n"

    )

    organizeImports(gsFile)
    assertEquals({
      "java.awt.Button",
      "java.util.*"
    }, extractImports(gsFile))
  }

  function testWildcardJavaLang() {

    var gsFile = createClass(
      "some.pkg.OrgImp",
      {
        "java.lang.Process",
        "java.lang.Float",
        "java.lang.Enum",
        "java.lang.Class",
        "java.lang.Byte"
      },

      "var _a : Process\n" +
      "var _b : Byte \n" +
      "var _c : Class \n" +
      "var _d : Enum \n" +
      "var _e : Float \n"
    )

    organizeImports(gsFile)
    assertEquals({
      "java.lang.*"
    }, extractImports(gsFile))
  }


  function testWildcardInterferencesWithImplicit() {

    var gsFile = createClass(
      "some.pkg.OrgImp",
      {
        "java.lang.Process",
        "java.lang.Float",
        "java.lang.Enum",
        "java.lang.Class",
        "java.lang.Byte"
      },

      "@Deprecated('') var _z : String \n" + // <- interferences with java.lang.*
      "var _a : Process\n" +
      "var _b : Byte \n" +
      "var _c : Class \n" +
      "var _d : Enum \n" +
      "var _e : Float \n"
    )

    organizeImports(gsFile)
    assertEquals({
      "java.lang.Byte",
      "java.lang.Class",
      "java.lang.Enum",
      "java.lang.Float",
      "java.lang.Process"
    }, extractImports(gsFile))
  }

  function testFQNTolerance() {

    var gsFile = createClass(
      "some.pkg.OrgImp", {},
      "var _a : java.util.List \n" +
      "var _b : java.util.List<String> \n" +
      "var _c : java.util.List[] \n"
    )

    organizeImports(gsFile)
    assertEquals({}, extractImports(gsFile))
  }

  function testLayout() {

    createClass("some.pkg2.Test1", {}, "")
    createClass("some.pkg2.Test2", {}, "")

    var gsFile = createClass(
      "some.pkg.OrgImp",
        {
          "java.lang.Process",
          "java.util.List",
          "some.pkg2.Test2",
          "some.pkg2.Test1"
        },
        "var _a : Process\n" +
        "var _b : List \n" +
        "var _c : Test2 \n" +
        "var _d : Test1 \n"
    )

    organizeImports(gsFile)
    assertEquals({
      "some.pkg2.Test1",
      "some.pkg2.Test2",
      "java.lang.Process"
    }, extractImports(gsFile))
  }

  function extractImports(gsFile : GosuClassFileImpl) : List<String> {
    return ApplicationManager.getApplication().runReadAction( \ -> {
      var imports = new PsiMatcherImpl(gsFile)
          .descendant(PsiMatchers.hasClass(IGosuUsesStatementList)).Element as IGosuUsesStatementList
      if (imports == null) {
        return (List<String>) {}
      }
      var importedFQNs = imports.UsesStatements.map(\e -> PsiTreeUtil.findChildOfType(e, GosuTypeLiteralImpl).Text)
      return Arrays.asList(importedFQNs)
    } as Computable<List<String>>).map( \ e -> e.endsWith(".") ? e + "*" : e  )
  }

  function organizeImports(gsFile : GosuClassFileImpl) {
    new WriteCommandAction( gsFile.Project, {} ) {
      override function run( result: Result ) {
        new OptimizeImportsProcessor(myProject, gsFile).runWithoutProgress();
        gsFile.reparseGosuFromPsi()
      }
    }.execute()
  }

  private function createClass(fqn:String, imports:String[], body:String ) : GosuClassFileImpl {
    var name = ClassUtil.extractClassName(fqn)
    var pckg = ClassUtil.extractPackageName(fqn)
    var psiFile = configureByText(
        pckg.replace('.', '/')+ "/${name}.gs",
        "package ${pckg}\n" +
        imports.map(\ e -> "uses " + e).join("\n") + "\n" +
        "\n" +
        "class ${name} {\n" +
          body + "\n" +
        "}" )
    return (psiFile as PsiFileImpl).calcTreeElement().Psi as GosuClassFileImpl
  }
}