/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion

uses gw.testharness.Disabled

//data type the same as class, gosu or java type does not work.
@Disabled("dpetrusca", "templates are not fully supported")
class TypeCompletionTemplateTest extends AbstractCodeCompletionTest {

  override function shouldReturnType(): boolean {
    return false;
  }

  function testStringTypeIsShownOnColonInVarStatement() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "var x : S^^\n" +
      "%>"
    ,{
      "String (java.lang)"
    }
    )
  }

  function testShortPrimitiveTypeIsNotShownOnColonInVarStatement() {
    testNoItems({
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "var x : S^^\n" +
      "%>"
    }
    ,{
      "short"
    }
    )
  }

  function testShortPrimitiveTypeIsShownOnColonInVarStatement() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "var x : s^^\n" +
      "%>"
    ,{
      "short"
    }
    )
  }

  function testStringTypeIsNotShownOnColonInVarStatement() {
    test({
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "var x : s^^\n" +
      "%>"
    }
    ,{
      "String (java.lang)"
    }
    )
  }

  function testStringTypeIsShownInTypeParamInVarStatement() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "var x : List<S^^\n" +
      "%>"
    ,{
      "String (java.lang)"
    }
    )
  }

  function testShortPrimitiveTypeIsNotShownInTypeParamInVarStatement() {
    testNoItems({
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "var x : List<S^^\n" +
      "%>"
    }
    ,{
      "short"
    }
    )
  }

  function testStringTypeIsShownOnColonInMethodParam() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "function bar(x : S^^) {}" +
      "%>"
    ,{
      "String (java.lang)"
    }
    )
  }

  function testShortPrimitiveTypeIsNotShownOnColonInMethodParam() {
    testNoItems({
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "function bar(x : S^^) {)" +
      "%>"
    }
    ,{
      "short"
    }
    )
  }

  function testShortPrimitiveTypeIsShownOnColonInMethodParam() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "function bar(x : s^^) {}" +
      "%>"
    ,{
      "short"
    }
    )
  }

  function testStringTypeIsNotShownOnColonInMethodParam() {
    test({
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "function bar(x : S^^) {}" +
      "%>"
    }
    ,{
      "String (java.lang)"
    }
    )
  }

  function testStringTypeIsShownInTypeParamInMethodParam() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "function bar(x : List<S^^) {}" +
      "%>"
    ,{
      "String (java.lang)"
    }
    )
  }

  function testShortPrimitiveTypeIsNotShownInTypeParamInMethodParam() {
    testNoItems({
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "function bar(x : List<S^^) {}" +
      "%>"
    }
    ,{
      "short"
    }
    )
  }

  function testStringTypeIsShownOnColonInReturnType() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "function bar() : S^^ {}" +
      "%>"
    ,{
      "String (java.lang)"
    }
    )
  }

  function testShortPrimitiveTypeIsNotShownOnColonInReturnType() {
    testNoItems({
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "function bar() : S^^ {}" +
      "%>"
    }
    ,{
      "short"
    }
    )
  }

  function testShortPrimitiveTypeIsShownOnColonInReturnType() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "function bar() : s^^ {}" +
      "%>"
    ,{
      "short"
    }
    )
  }

  function testStringTypeIsNotShownOnColonInReturnType() {
    test({
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "function bar() : s^^{}" +
      "%>"
    }
    ,{
      "String (java.lang)"
    }
    )
  }

  function testStringTypeIsShownInTypeParamInReturnType() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "function bar() : List<S^^ {}" +
      "%>"
    ,{
      "String (java.lang)"
    }
    )
  }

  function testShortPrimitiveTypeIsNotShownInTypeParamInReturnType() {
    testNoItems({
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "function bar() : List<S^^ {}" +
      "%>"
    }
    ,{
      "short"
    }
    )
  }

  function testStringTypeIsShownOnColonInBlockParam() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "var x = \\ n : S^^\n" +
      "%>"
    ,{
      "String (java.lang)"
    }
    )
  }

  function testShortPrimitiveTypeIsNotShownOnColonInBlockParam() {
    testNoItems({
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "var x = \\ n : S^^\n" +
      "%>"
    }
    ,{
      "short"
    }
    )
  }

  function testShortPrimitiveTypeIsShownOnColonInBlockParam() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "var x = \\ n : s^^\n" +
      "%>"
    ,{
      "short"
    }
    )
  }

  function testStringTypeIsNotShownOnColonInBlockParam() {
    test({
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "var x = \\ n : s^^\n" +
      "%>"
    }
    ,{
      "String (java.lang)"
    }
    )
  }

  function testStringTypeIsShownInTypeParamInBlockParam() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "var x = \\ n : List<S^^\n" +
      "%>"
    ,{
      "String (java.lang)"
    }
    )
  }

  function testShortPrimitiveTypeIsNotShownInTypeParamInBlockParam() {
    testNoItems({
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "var x = \\ n : List<S^^\n" +
      "%>"
    }
    ,{
      "short"
    }
    )
  }

  function testThrowablesTypeIsShownInCatchClause(){
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "uses java.lang.*\n" +
      "function bar() {\n" +
      "  try {\n" +
      "  } catch (e : N^^)\n" +
      "}\n" +
      "%>"
    , {
      "NullPointerException (java.lang)"
    }
    )
  }

  function testTypesDefinedInOtherPkgAreShownInCatchClause(){
    test({
      "package apkg\n" +
      "class AGosuClass extends Exception {\n" +
      "  construct(){\n" +
      "  }\n" +
      "  construct(msg : String){\n" +
      "    super(msg)\n" +
      "  }\n" +
      "}"
    ,
      "//JAVA \n" +
      "package apkg;\n" +
      "public class AJavaClass extends Exception {\n" +
      "  public AJavaClass() {\n" +
      "  }\n" +
      "  public AJavaClass(String msg) {\n" +
      "    super(msg);\n" +
      "  }\n" +
      "}"
    ,
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "uses apkg.*\n" +
      "%>" +
      "\<%" +
      "function bar() {\n" +
      "  try {\n" +
      "  } catch (e : A^^)\n" +
      "}" +
      "%>"
    }
    , {
      "AGosuClass (apkg)", "AJavaClass (apkg)"
    }
    )
  }

  function testNoTypesDefinedInOtherPkgAreShownInCatchClause(){
    testNoItems({
      "package apkg\n" +
      "class AGosuClass extends Exception {\n" +
      "  construct(){\n" +
      "  }\n" +
      "  construct(msg : String){\n" +
      "    super(msg)\n" +
      "  }\n" +
      "}"
    ,
      "package apkg\n" +
      "class BGosuClass {\n" +
      "}"
    ,
      "//JAVA \n" +
      "package apkg;\n" +
      "public class AJavaClass extends Exception {\n" +
      "  public AJavaClass() {\n" +
      "  }\n" +
      "  public AJavaClass(String msg) {\n" +
      "    super(msg);\n" +
      "  }\n" +
      "}"
    ,
      "//JAVA \n" +
      "package apkg;\n" +
      "public class BJavaClass {\n" +
      "}"
    ,
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "uses apkg.*\n" +
      "%>" +
      "\<%" +
      "function bar() {\n" +
      "  try {\n" +
      "  } catch (e : B^^)\n" +
      "}\n" +
      "%>"
    }
    , {
      "BGosuClass", "BJavaClass"
    }
    )
  }

  function testTypesInLibAreShownInNewExpression() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "new S^^\n" +
      "%>"
    ,{
      "StringBuffer (java.lang)", "String (java.lang)"
    }
    )
  }

  function testTypesInSamePackageAreShownInNewExpression(){
    testNoItems({
      "package apkg\n" +
      "class BGosuClass {\n" +
      "}"
    ,
      "//JAVA \n" +
      "package apkg;\n" +
      "public class BJavaClass {\n" +
      "}"
    ,
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "uses apkg.*\n" +
      "%>" +
      "\<%" +
      "new B^^\n" +
      "%>"
    }
    , {
      "BGosuClass", "BJavaClass"
    }
    )
  }

  function testNonThrowablesTypesInLibArentShownInNewExpressionAfterThrow() {
    testNoItems({
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "uses java.lang.*\n" +
      "%>" +
      "\<%" +
      "throw new S^^\n" +
      "%>"
    }
    ,{
      "StringBuffer", "String (java.lang)"
    }
    )
  }

  function testThrowablesTypeInLibIsShownInNewExpressionAfterThrow() {
    testNoItems({
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "uses java.lang.*\n" +
      "%>" +
      "\<%" +
      "throw new N^^\n" +
      "%>"
    }
    ,{
      "NullPointerException"
    }
    )
  }

  function testNonThrowablesTypesInSamePackageArentShownInNewExpressionAfterThrow(){
    testNoItems({
      "package apkg\n" +
      "class BGosuClass {\n" +
      "}"
    ,
      "//JAVA \n" +
      "package apkg;\n" +
      "public class BJavaClass {\n" +
      "}"
    ,
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "uses apkg.*\n" +
      "%>" +
      "\<%" +
      "function bar() {\n" +
      "throw new B^^\n" +
      "}\n" +
      "%>"
    }
    , {
      "BGosuClass", "BJavaClass"
    }
    )
  }

  function testTypesInSamePackageArentShownInNewExpressionAfterThrow(){
    test({
      "package pkg\n" +
      "class AGosuClass extends Exception {\n" +
      "  construct(){\n" +
      "  }\n" +
      "  construct(msg : String){\n" +
      "    super(msg)\n" +
      "  }\n" +
      "}"
    ,
      "//JAVA \n" +
      "package pkg;\n" +
      "public class AJavaClass extends Exception {\n" +
      "  public AJavaClass() {\n" +
      "  }\n" +
      "  public AJavaClass(String msg) {\n" +
      "    super(msg);\n" +
      "  }\n" +
      "}"
    ,
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "function bar() {\n" +
      "  try {\n" +
      "  } catch (e : A^^)\n" +
      "}\n" +
      "%>"
    }
    , {
      "AGosuClass (pkg)", "AJavaClass (pkg)"
    }
    )
  }

  function testInnerTypeIsShownInNewExpression() {
    test({
      "package pkg\n" +
      "uses java.lang.*\n" +
      "class TestClass {\n" +
      "  class StaticInnerClass {\n" +
      "    function bar() {\n" +
      "    }\n" +
      "  }\n" +
      "}\n"
    ,
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "uses pkg.TestClass.*\n" +
      "%>" +
      "\<%" +
      "new S^^\n" +
      "%>"
    }
    ,{
      "StaticInnerClass (pkg.TestClass)"
    }
    )
  }

  function testJavaInnerTypeIsShownInNewExpression() {
    test({
      "//JAVA \n" +
      "package test;\n" +
      "public class OuterClass {\n" +
      "  public static class StaticInnerClass { }\n" +
      "}\n"
    ,
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "uses test.OuterClass.*\n" +
      "%>" +
      "\<%" +
      "new S^^\n" +
      "%>"
    }
    , {
      "StaticInnerClass (test.OuterClass)"
    }
    )
  }

  @Disabled("cgross", "Need to investigate")
  function testAnnotationTypesIsShownInAnnotationUsage() {
    test({
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "uses java.lang.*\n" +
      "%>" +
      "\<%" +
      "@D^^\n" +
      "function bar() {}" +
      "%>"
    }
    ,{
      "Deprecated (java.lang)"
    }
    )
  }

  @Disabled("cgross", "Need to investigate")
  function testNonAnnotationTypesArentShownInAnnotationUsage() {
    testNoItems({
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "uses java.lang.*\n" +
      "%>" +
      "\<%" +
      "@^^\n" +
      "function bar() {)" +
      "%>"
    }
    ,{
      "StringBuffer (java.lang)"
    }
    )
  }

  @Disabled("cgross", "Need to investigate")
  function testTypsAreShownInAnnotationDefinedInOtherPackage() {
    test({
      "package apkg\n" +
      "class AGosuClass implements IAnnotation {\n" +
      "}"
    ,
      "//JAVA \n" +
      "package apkg;\n" +
      "public @interface AJavaClass {\n" +
      "}"
    ,
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "uses apkg.*\n" +
      "%>" +
      "\<%" +
      "@A^^\n" +
      "function bar() {}" +
      "%>"
    }
    ,{
      "AGosuClass (apkg)", "AJavaClass (apkg)"
    }
    )
  }

  @Disabled("cgross", "Need to investigate")
  function testTypeThatContainsStaticInnerAnnotationIsShownInAnnotation() {
    test({
      "package apkg\n" +
      "class OuterClass {\n" +
      "  static class StaticInnerClass implements IAnnotation { }\n" +
      "}\n"
    ,
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "uses apkg.OuterClass.*\n" +
      "%>" +
      "\<%" +
      "@S^^\n" +
      "function bar() {}" +
      "%>"
    }
    ,{
      "StaticInnerClass (apkg.OuterClass)"
    })
  }

  @Disabled("cgross", "Need to investigate")
  function testTypesUnderSamePackageAreShown() {
    test({
      "//JAVA \n" +
      "package pkg;\n" +
      "public class AJavaClass { \n" +
      "}",
      "package pkg\n" +
      "class AGosuClass { \n" +
      "}",
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "A^^ \n" +
      "%>"
    }
    , {
      "AGosuClass (pkg)", "AJavaClass (pkg)"
    }
    )
  }

  @Disabled("cgross", "Need to investigate")
  function testTypesInUsesAreShown() {
    test({
      "//JAVA \n" +
      "package otherpkg;\n" +
      "public class AJavaClass { \n" +
      "}",
      "package otherpkg\n" +
      "class AGosuClass { \n" +
      "}",
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "uses otherpkg.*" +
      "%>" +
      "\<%" +
      "A^^ \n" +
      "%>"
    }
    , {
      "AGosuClass (otherpkg)", "AJavaClass (otherpkg)"
    }
    )
  }

  //default types completion

  //====String has been covered
  //====Boolean
  function testBooleanTypeIsShown() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "var myBoolean : B^^\n" +
      "%>"
    ,{
      "Boolean (java.lang)"
    }
    )
  }

  //====List
  function testListTypeIsShown() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "var myList : L^^\n" +
      "%>"
    ,{
      "List<E> (java.util)"
    }
    )
  }

  //====Object
  function testObjectTypeIsShown() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "var myObject : O^^\n" +
      "%>"
    ,{
      "Object (java.lang)"
    }
    )
  }

  //primitive data types
  //====short has been covered
  //====int
  function testPrimitiveIntTypeIsShown() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "var myInt : i^^\n" +
      "%>"
    ,{
      "int"
    }
    )
  }

  //====char
  function testPrimitiveCharTypeIsShown() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "var myChar : c^^\n" +
      "%>"
    ,{
      "char"
    }
    )
  }

  //====boolean, byte
  function testPrimitiveBooleanTypeIsShown() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "var myBoolean : b^^\n" +
      "%>"
    ,{
      "boolean", "byte"
    }
    )
  }

  //====long
  function testPrimitiveLongTypeIsShown() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "var myLong : l^^\n" +
      "%>"
    ,{
      "long"
    }
    )
  }

  //====float
  function testPrimitiveFloatTypeIsShown() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "var myFloat : f^^\n" +
      "%>"
    ,{
      "float"
    }
    )
  }

  //====double
  function testPrimitiveDoubleTypeIsShown() {
    test(
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "var myDouble : d^^\n" +
      "%>"
    ,{
      "double"
    }
    )
  }
}