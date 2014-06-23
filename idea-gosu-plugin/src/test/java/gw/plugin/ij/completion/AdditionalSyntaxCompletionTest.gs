/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion
/**
 * Created by IntelliJ IDEA.
 * User: tlin
 * Date: 9/22/11
 * Time: 10:26 AM
 * To change this template use File | Settings | File Templates.
 */
class AdditionalSyntaxCompletionTest extends AbstractCodeCompletionTest {

  override function shouldReturnType(): boolean {
    return false;
  }

  function testStringTemplateDollarCompletion() {
    test(
      "package pkg\n" +
      "class Foo {\n" +
      "  function bar() {\n" +
      "    var x = \"foo\"\n" +
      "    print(\"\$^^\")\n" +
      "  }\n" +
      "}\n"
    ,{
      "{}"
    } )
  }

  function testStringTemplateLeftChevronCompletion() {
    test(
      "package pkg\n" +
      "class Foo {\n" +
      "  function bar() {\n" +
      "    var x = \"foo\"\n" +
      "    print(\"\<^^\")\n" +
      "  }\n" +
      "}\n"
    ,{
      "%%>", "%=%>"
    })
  }

  function testStringTemplateLeftChevronCompletionInTemplate() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "<^^\n"
    ,{
      "%%>", "%=%>", "%@%>", "%----%>"
    })
  }

  function testStringTemplateDollarCompletionInTemplate() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "$^^\n"
    ,{
      "{}"
    })
  }

  function testStringTemplateLeftChevronCompletionInTemplateInTemplateScriptletTags() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<% print(\"\<^^\") %>"
    ,{
      "%%>", "%=%>"
    })
  }

  function testStringTemplateDollarCompletionInTemplateInTemplateScriptletTags() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<% print(\"\$^^\") %>"
    ,{
      "{}"
    })
  }

  function testStringTemplateDollarCompletionInProgram() {
    test(
      "//PROGRAM, pkg/MyProgram \n" +
      "  function bar() {\n" +
      "    var x = \"foo\"\n" +
      "    print(\"\$^^\")\n" +
      "  }\n"
    ,{
      "{}"
    } )
  }

  function testStringTemplateLeftChevronCompletionInProgram() {
    test(
      "//PROGRAM, pkg/MyProgram \n" +
      "  function bar() {\n" +
      "    var x = \"foo\"\n" +
      "    print(\"\<^^\")\n" +
      "  }\n"
    ,{
      "%%>", "%=%>"
    })
  }

  function testNoItemsAreShownStringTemplateLeftChevronCompletion() {
    testNoItems({
      "package pkg\n" +
      "class Foo {\n" +
      "  function bar() {\n" +
      "    var x = \"foo\"\n" +
      "    print(\"\<^^\")\n" +
      "  }\n" +
      "}\n"}
    ,{
      "%@%>"
    })
  }

  function testNoItemsAreShownStringTemplateLeftChevronCompletionInProgram() {
    testNoItems({
      "//PROGRAM, pkg/MyProgram \n" +
      "  function bar() {\n" +
      "    var x = \"foo\"\n" +
      "    print(\"\<^^\")\n" +
      "  }\n"}
    ,{
      "%@%>"
    })
  }


}