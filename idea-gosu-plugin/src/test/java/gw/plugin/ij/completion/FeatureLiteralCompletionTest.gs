/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion

class FeatureLiteralCompletionTest extends AbstractCodeCompletionTest {

  override function shouldReturnType() : boolean {
    return false
  }

  function testBasicPropertyCompletion() {
    test(
      "package pkg\n" +
      "class GosuClass { \n" +
      "var xyz : String as Foo\n" +
      "function foo() { \n" +
      "  var x = this#F^^\n" +
      "}",
    {
      "Foo"
    } )
  }

  function testRelativePropertyCompletion() {
    test(
      "package pkg\n" +
      "class GosuClass { \n" +
      "var xyz : String as Foo\n" +
      "function foo() { \n" +
      "  var x = #F^^\n" +
      "}",
    {
      "Foo"
    } )
  }

  function testBasicFunctionCompletion() {
    test(
      "package pkg\n" +
      "class GosuClass { \n" +
      "var xyz : String as Foo\n" +
      "function foo() { \n" +
      "  var x = this#fo^^\n" +
      "}",
    {
      "foo()"
    } )
  }

  function testBasicFunctionWOneArgCompletion() {
    test(
      "package pkg\n" +
      "class GosuClass { \n" +
      "var xyz : String as Foo\n" +
      "function foo(m:java.util.Map) { \n" +
      "  var x = this#fo^^\n" +
      "}",
    {
      "foo(java.util.Map<java.lang.Object, java.lang.Object>)"
    } )
  }

  function testBasicFunctionWTwoArgsCompletion() {
    test(
      "package pkg\n" +
      "class GosuClass { \n" +
      "var xyz : String as Foo\n" +
      "function foo(m:java.util.Map, d:java.sql.Date) { \n" +
      "  var x = this#fo^^\n" +
      "}",
    {
      "foo(java.util.Map<java.lang.Object, java.lang.Object>, java.sql.Date)"
    } )
  }

  function testBasicFunctionWOneArgShortNameIsPickedCompletion() {
    test(
      "package pkg\n" +
      "class GosuClass { \n" +
      "var xyz : String as Foo\n" +
      "function foo(m:String) { \n" +
      "  var x = this#fo^^\n" +
      "}",
    {
      "foo(String)"
    } )
  }


  function testRelativeFunctionCompletion() {
    test(
      "package pkg\n" +
      "class GosuClass { \n" +
      "var xyz : String as Foo\n" +
      "function foo() { \n" +
      "  var x = #fo^^\n" +
      "}",
    {
      "foo()"
    } )

  }

  function testBasicConstructorCompletion() {
    test(
      "package pkg\n" +
      "class GosuClass { \n" +
      "var xyz : String as Foo\n" +
      "function foo() { \n" +
      "  var x = this#con^^\n" +
      "}",
    {
      "construct()"
    } )
  }

  function testRelativeConstructorCompletion() {
    test(
      "package pkg\n" +
      "class GosuClass { \n" +
      "var xyz : String as Foo\n" +
      "function foo() { \n" +
      "  var x = #con^^\n" +
      "}",
    {
      "construct()"
    } )
  }

  function testBasicConstructorWithParamsCompletion() {
    test(
      "package pkg\n" +
      "class GosuClass { \n" +
      "var xyz : String as Foo\n" +
      "construct(m:String) {}" +
      "function foo() { \n" +
      "  var x = this#con^^\n" +
      "}",
    {
      "construct(String)"
    } )
  }

  function testRelativeConstructorWithParamsCompletion() {
    test(
      "package pkg\n" +
      "class GosuClass { \n" +
      "var xyz : String as Foo\n" +
      "construct(m:String) {}" +
      "function foo() { \n" +
      "  var x = #con^^\n" +
      "}",
    {
      "construct(String)"
    } )
  }

  function testBasicPropertyDeclInJavaClassCompletion() {
    test({
      "//JAVA\n" +
      "package pkg;\n" +
      "public class JavaClass {\n" +
      "  public void setFoo(String s) {\n" +
      "  }\n" +
      "  public String getFoo() {\n" +
      "    return \"\";\n" +
      "  }\n" +
      "  public void foo(){\n" +
      "  }\n" +
      "}",
      "package pkg\n" +
      "class GosuClass { \n" +
      "function foo() { \n" +
      "  var x = pkg.JavaClass#F^^\n" +
      "}"},
    {
      "Foo"
    })
  }

   function testBasicFunctionDeclInJavaClassCompletion() {
    test( {
      "//JAVA\n" +
      "package pkg;\n" +
      "public class JavaClass {\n" +
      "  public void foo(){\n" +
      "  }\n" +
      "}",
      "package pkg\n" +
      "class GosuClass { \n" +
      "function foo() { \n" +
      "  var x = JavaClass#f^^\n" +
      "}"},
    {
      "foo()"
    } )
  }

  function testBasicFunctionWOneArgDeclInJavaClassCompletion() {
    test( {
      "//JAVA\n" +
      "package pkg;\n" +
      "public class JavaClass {\n" +
      "  public void foo(java.util.Map m) {\n" +
      "  }\n" +
      "}",
      "package pkg\n" +
      "class GosuClass { \n" +
      "function foo) { \n" +
      "  var x = JavaClass#f^^\n" +
      "}"},
    {
      "foo(java.util.Map)"
    } )
  }

  function testBasicFunctionWTwoArgsDeclInJavaClassCompletion() {
    test({
      "//JAVA\n" +
      "package pkg;\n" +
      "public class JavaClass {\n" +
      "  public void foo(java.util.Map m, java.sql.Date d) {\n" +
      "  }\n" +
      "}",
      "package pkg\n" +
      "class GosuClass { \n" +
      "function foo() { \n" +
      "  var x = JavaClass#f^^\n" +
      "}"},
    {
      "foo(java.util.Map, java.sql.Date)"
    } )
  }

  function testBasicFunctionWOneArgShortNameDeclInJavaClassIsPickedCompletion() {
    test({
      "//JAVA\n" +
      "package pkg;\n" +
      "public class JavaClass {\n" +
      "  public void foo(String m) {\n" +
      "  }\n" +
      "}",
      "package pkg\n" +
      "class GosuClass { \n" +
      "function foo() { \n" +
      "  var x = JavaClass#f^^\n" +
      "}"},
    {
      "foo(String)"
    } )
  }

  function testBasicConstructorDeclInJavaClassCompletion() {
    test({
      "//JAVA\n" +
      "package pkg;\n" +
      "public class JavaClass {\n" +
      "}",
      "package pkg\n" +
      "class GosuClass { \n" +
      "function foo() { \n" +
      "  var x = JavaClass#con^^\n" +
      "}"},
    {
      "construct()"
    } )
  }

  function testBasicConstructorWithParamsDeclInJavaClassCompletion() {
    test({
      "//JAVA\n" +
      "package pkg;\n" +
      "public class JavaClass {\n" +
      "  public JavaClass(String m) { }\n"  +
      "}",
      "package pkg\n" +
      "class GosuClass { \n" +
      "function foo() { \n" +
      "  var x = JavaClass#con^^\n" +
      "}"},
    {
      "construct(String)"
    } )
  }

}