/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.parser

uses gw.plugin.ij.framework.GosuTestCase
uses gw.plugin.ij.framework.generator.GosuClassFile
uses gw.plugin.ij.framework.generator.GosuEnhancementFile
uses gw.plugin.ij.framework.generator.GosuInterfaceFile
uses gw.plugin.ij.framework.generator.GosuTestingResource

class ClassPaserTest extends GosuTestCase {

  //class
  function testErrorShowsInDuplicatedMethodClassDecl() {
    var f = new GosuClassFile("package pkg1\n" +
      "class Class1 {\n" +
      "  function test1() {}\n" +
      "  function test1([[)]] {}\n" +
      "}\n")
    test({f},
    {"test1() : void, is already defined in pkg1.Class1"}
    )
  }

  //interface
  function testErrorShowsInDuplicatedMethodInterfaceDecl() {
    var f = new GosuInterfaceFile("package pkg1\n" +
      "interface Iface1 {\n" +
      "  function test1()\n" +
      "  function test1([[)]]\n" +
      "}\n")
    test({f},
    {"test1() : void, is already defined in pkg1.Iface1"}
    )
  }

  //enhancement
  function testErrorShowsInOverrideMethodInEnhancement() {
    var f = new GosuClassFile("package pkg1\n" +
      "class Class1 {\n" +
      "  function test1() {}\n" +
      "}\n")
    var fenh = new GosuEnhancementFile("package pkg1\n" +
      "enhancement Class1Enh : Class1 {\n" +
      "  [[function test1( ) {]]}\n" +
      "}\n")
    test({f, fenh},
    {"The function \"test1\" is already defined in the type Class1.  Enhancements cannot override functions."}
    )
  }

  function testNoErrorShowsForOperatorsToHandleDefaultValueOnOptionalParameters() {
     var f = new GosuClassFile("package pkg1\n" +
      "class Class1 {\n" +
      "  function test1(a:List<String>={\"hello\"}) {}\n" +
      "}\n")
     testNoErrors({f})
  }

  //helpers

  private function test(resources: GosuTestingResource[], errorMsgs : String[] ) {
    var markers = getAllMarkers(resources.map(\r -> configureByText(r.fileName, r.content)))
    assertEquals("Errors marked in the editor do not match the error messages provided.", markers.Ranges.Count, errorMsgs.Count)
    var errors = highlightErrors()

    assertEquals("That actually errors show in editer does not match the expected.", markers.Ranges.Count, errors.Count)
    var actuallyErrorMsgs = errors.map( \ elt -> elt.description)
    var actuallyStartOffsets = errors.map( \ elt -> elt.StartOffset)
    var actuallyEndOffsets = errors.map( \ elt -> elt.EndOffset)

    gw.test.AssertUtil.assertArrayEquals("Error messages does not match.", errorMsgs, actuallyErrorMsgs.toArray())
    for(i in 0..|markers.Ranges.Count){
      assertTrue(markers.Ranges[i].containsRange(actuallyStartOffsets[i], actuallyEndOffsets[i] ))
    }
  }

  private function testNoErrors(resources: GosuTestingResource[]) {
    var psiFiles = resources.map(\r -> configureByText(r.fileName, r.content))
    var errors = highlightErrors()
    assertEquals("Should not contain any errors in those resources.", 0, errors.Count)
  }

}