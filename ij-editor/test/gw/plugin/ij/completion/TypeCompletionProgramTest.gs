/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion

uses gw.testharness.Disabled

//data type the same as class, gosu or java type does not work.
class TypeCompletionProgramTest extends AbstractCodeCompletionTest {

  override function shouldReturnType(): boolean {
    return false;
  }

  function testStringTypeIsShownOnColonInVarStatement() {
    test(
      "//PROGRAM, pkg/MyProgram\n" +
      "var x : S^^\n"
    ,{
      "String (java.lang)"
    } 
    )
  }

  function testShortPrimitiveTypeIsNotShownOnColonInVarStatement() {
    testNoItems({
      "//PROGRAM, pkg/MyProgram\n" +
      "var x : S^^\n"
    }
    ,{
      "short"
    }
    )
  }

  function testShortPrimitiveTypeIsShownOnColonInVarStatement() {
    test(
      "//PROGRAM, pkg/MyProgram\n" +
      "var x : s^^\n"
    ,{
      "short"
    }
    )
  }

  function testStringTypeIsNotShownOnColonInVarStatement() {
    testNoItems({
      "//PROGRAM, pkg/MyProgram\n" +
      "var x : s^^\n"
    }
    ,{
      "String"
    }
    )
  }

  function testStringTypeIsShownInTypeParamInVarStatement() {
    test(
      "//PROGRAM, pkg/MyProgram\n" +
      "var x : List<S^^\n"
    ,{
      "String (java.lang)"
    }
    )
  }

  function testShortPrimitiveTypeIsNotShownInTypeParamInVarStatement() {
    testNoItems({
      "//PROGRAM, pkg/MyProgram\n" +
      "var x : List<S^^\n"
    }
    ,{
      "short"
    }
    )
  }

  function testStringTypeIsShownOnColonInMethodParam() {
    test(
      "//PROGRAM, pkg/MyProgram\n" +
      "function bar(x : S^^) {}"
    ,{
      "String (java.lang)"
    }
    )
  }

  function testShortPrimitiveTypeIsNotShownOnColonInMethodParam() {
    testNoItems({
      "//PROGRAM, pkg/MyProgram\n" +
      "function bar(x : S^^) {)"
    }
    ,{
      "short"
    }
    )
  }

  function testShortPrimitiveTypeIsShownOnColonInMethodParam() {
    test(
      "//PROGRAM, pkg/MyProgram\n" +
      "function bar(x : s^^) {}"
    ,{
      "short"
    }
    )
  }

  function testStringTypeIsNotShownOnColonInMethodParam() {
    testNoItems({
      "//PROGRAM, pkg/MyProgram\n" +
      "function bar(x : S^^) {}"
    }
    ,{
      "String"
    }
    )
  }

  function testStringTypeIsShownInTypeParamInMethodParam() {
    test(
      "//PROGRAM, pkg/MyProgram\n" +
      "function bar(x : List<S^^) {}"
    ,{
      "String (java.lang)"
    }
    )
  }

  function testShortPrimitiveTypeIsNotShownInTypeParamInMethodParam() {
    testNoItems({
      "//PROGRAM, pkg/MyProgram\n" +
      "function bar(x : List<S^^) {}"
    }
    ,{
      "short"
    }
    )
  }

  function testStringTypeIsShownOnColonInReturnType() {
    test(
      "//PROGRAM, pkg/MyProgram\n" +
      "function bar() : S^^ {}"
    ,{
      "String (java.lang)"
    }
    )
  }

  function testShortPrimitiveTypeIsNotShownOnColonInReturnType() {
    testNoItems({
      "//PROGRAM, pkg/MyProgram\n" +
      "function bar() : S^^ {}"
    }
    ,{
      "short"
    }
    )
  }

  function testShortPrimitiveTypeIsShownOnColonInReturnType() {
    test(
      "//PROGRAM, pkg/MyProgram\n" +
      "function bar() : s^^ {}"
    ,{
      "short"
    }
    )
  }

  function testStringTypeIsNotShownOnColonInReturnType() {
    testNoItems({
      "//PROGRAM, pkg/MyProgram\n" +
      "function bar() : s^^{}"
    }
    ,{
      "String"
    }
    )
  }

  function testStringTypeIsShownInTypeParamInReturnType() {
    test(
      "//PROGRAM, pkg/MyProgram\n" +
      "function bar() : List<S^^ {}"
    ,{
      "String (java.lang)"
    }
    )
  }

  function testShortPrimitiveTypeIsNotShownInTypeParamInReturnType() {
    testNoItems({
      "//PROGRAM, pkg/MyProgram\n" +
      "function bar() : List<S^^ {}"
    }
    ,{
      "short"
    }
    )
  }

  function testStringTypeIsShownOnColonInBlockParam() {
    test(
      "//PROGRAM, pkg/MyProgram\n" +
      "var x = \\ n : S^^\n"
    ,{
      "String (java.lang)"
    }
    )
  }

  function testShortPrimitiveTypeIsNotShownOnColonInBlockParam() {
    testNoItems({
      "//PROGRAM, pkg/MyProgram\n" +
      "var x = \\ n : S^^\n"
    }
    ,{
      "short"
    }
    )
  }

  function testShortPrimitiveTypeIsShownOnColonInBlockParam() {
    test(
      "//PROGRAM, pkg/MyProgram\n" +
      "var x = \\ n : s^^\n"
    ,{
      "short"
    }
    )
  }

  function testStringTypeIsNotShownOnColonInBlockParam() {
    testNoItems({
      "//PROGRAM, pkg/MyProgram\n" +
      "var x = \\ n : s^^\n"
    }
    ,{
      "String"
    }
    )
  }

  function testStringTypeIsShownInTypeParamInBlockParam() {
    test(
      "//PROGRAM, pkg/MyProgram\n" +
      "var x = \\ n : List<S^^\n"
    ,{
      "String (java.lang)"
    }
    )
  }

  function testShortPrimitiveTypeIsNotShownInTypeParamInBlockParam() {
    testNoItems({
      "//PROGRAM, pkg/MyProgram\n" +
      "var x = \\ n : List<S^^\n"
    }
    ,{
      "short"
    }
    )
  }

  function testThrowablesTypeIsShownInCatchClause(){
    test(
      "//PROGRAM, pkg/MyProgram\n" +
      "uses java.lang.*\n" +
      "try {\n" +
      "} catch (e : N^^)\n"
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
      "//PROGRAM, pkg/MyProgram\n" +
      "uses apkg.*\n" +
      "try {\n" +
      "} catch (e : A^^)\n"
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
      "//PROGRAM, pkg/MyProgram\n" +
      "uses apkg.*\n" +
      "try {\n" +
      "} catch (e : B^^)\n"
    }
    , {
      "BGosuClass", "BJavaClass"
    }
    )
  }

  function testInnerTypeItselfIsShownInNewExpression() {
    test(
      "//PROGRAM, pkg/MyProgram\n" +
      "class GosuClass {\n" +
      "  function bar() {\n" +
      "    new G^^\n" +
      "  }\n" +
      "}\n"
    ,{
      "GosuClass (pkg.MyProgram)"
    }
    )
  }

  function testTypesInLibAreShownInNewExpression() {
    test(
      "//PROGRAM, pkg/MyProgram\n" +
      "uses java.lang.*\n" +
      "new S^^\n"
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
      "//PROGRAM, pkg/MyProgram\n" +
      "uses apkg.*\n" +
      "new B^^\n"
    }
    , {
      "BGosuClass", "BJavaClass"
    }
    )
  }

  @Disabled( "smckinney", "Requires parsing on proposal types, which we don't do for performance reasons" )
  function testNonThrowablesTypesInLibArentShownInNewExpressionAfterThrow() {
    testNoItems({
      "//PROGRAM, pkg/MyProgram\n" +
      "uses java.lang.*\n" +
      "throw new S^^\n"
    }
    ,{
      "StringBuffer", "String"
    }
    )
  }

  @Disabled( "smckinney", "Requires parsing on proposal types, which we don't do for performance reasons" )
  function testThrowablesTypeInLibIsShownInNewExpressionAfterThrow() {
    testNoItems({
      "//PROGRAM, pkg/MyProgram\n" +
      "uses java.lang.*\n" +
      "throw new N^^\n"
    }
    ,{
      "NullPointerException"
    }
    )
  }

  @Disabled( "smckinney", "Requires parsing on proposal types, which we don't do for performance reasons" )
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
      "//PROGRAM, pkg/MyProgram\n" +
      "uses apkg.*\n" +
      "throw new B^^\n"
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
      "//PROGRAM, pkg/MyProgram\n" +
      "try {\n" +
      "} catch (e : A^^)\n"
    }
    , {
      "AGosuClass (pkg)", "AJavaClass (pkg)"
    }
    )
  }

  @Disabled( "smckinney", "Requires parsing on proposal types, which we don't do for performance reasons" )
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
      "//PROGRAM, pkg/MyProgram\n" +
      "uses pkg.TestClass.*\n" +
      "new S^^\n"
    }
    ,{
      "StaticInnerClass (pkg.TestClass)"
    }
    )
  }

  @Disabled( "smckinney", "Requires parsing on proposal types, which we don't do for performance reasons" )
  function testJavaInnerTypeIsShownInNewExpression() {
    test({
      "//JAVA \n" +
      "package test;\n" +
      "public class OuterClass {\n" +
      "  public static class StaticInnerClass { }\n" +
      "}\n"
    ,
      "//PROGRAM, pkg/MyProgram\n" +
      "uses test.OuterClass.*\n" +
      "new S^^\n"
    }
    , {
      "StaticInnerClass (test.OuterClass)"
    }
    )
  }

  @Disabled( "smckinney", "Requires parsing on proposal types, which we don't do for performance reasons" )
  function testAnnotationTypesIsShownInAnnotationUsage() {
    test({
      "//PROGRAM, pkg/MyProgram\n" +
      "uses java.lang.*\n" +
      "@D^^\n" +
      "function bar() {}"
    }
    ,{
      "Deprecated (java.lang)"
    }
    )
  }

  @Disabled( "smckinney", "Requires parsing on proposal types, which we don't do for performance reasons" )
  function testNonAnnotationTypesArentShownInAnnotationUsage() {
    testNoItems({
      "//PROGRAM, pkg/MyProgram\n" +
      "uses java.lang.*\n" +
      "@^^\n" +
      "function bar() {)"
    }
    ,{
      "StringBuffer"
    }
    )
  }

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
      "//PROGRAM, pkg/MyProgram\n" +
      "uses apkg.*\n" +
      "@A^^\n" +
      "function bar() {}"
    }
    ,{
      "AGosuClass (apkg)", "AJavaClass (apkg)"
    }
    )
  }

  @Disabled( "smckinney", "Requires parsing on proposal types, which we don't do for performance reasons" )
  function testTypeThatContainsStaticInnerAnnotationIsShownInAnnotation() {
    test({
      "package apkg\n" +
      "class OuterClass {\n" +
      "  static class StaticInnerClass implements IAnnotation { }\n" +
      "}\n"
    ,
      "//PROGRAM, pkg/MyProgram\n" +
      "uses apkg.OuterClass.*\n" +
      "@S^^\n" +
      "function bar() {}"
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
      "//PROGRAM, pkg/MyProgram\n" +
      "A^^ \n"
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
      "//PROGRAM, pkg/MyProgram\n" +
      "uses otherpkg.*" +
      "A^^ \n"
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
      "//PROGRAM, pkg/MyProgram\n" +
      "var myBoolean : B^^\n"
    ,{
      "Boolean (java.lang)"
    }
    )
  }

  //====List
  function testListTypeIsShown() {
    test(
      "//PROGRAM, pkg/MyProgram\n" +
      "var myList : L^^\n"
    ,{
      "List<E> (java.util)"
    }
    )
  }

  //====Object
  function testObjectTypeIsShown() {
    test(
      "//PROGRAM, pkg/MyProgram\n" +
      "var myObject : O^^\n"
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
      "//PROGRAM, pkg/MyProgram\n" +
      "var myInt : i^^\n"
    ,{
      "int"
    }
    )
  }

  //====char
  function testPrimitiveCharTypeIsShown() {
    test(
      "//PROGRAM, pkg/MyProgram\n" +
      "var myChar : c^^\n"
    ,{
      "char"
    }
    )
  }

  //====boolean, byte
  function testPrimitiveBooleanTypeIsShown() {
    test(
      "//PROGRAM, pkg/MyProgram\n" +
      "var myBoolean : b^^\n"
    ,{
      "boolean", "byte"
    }
    )
  }

  //====long
  function testPrimitiveLongTypeIsShown() {
    test(
      "//PROGRAM, pkg/MyProgram\n" +
      "var myLong : l^^\n"
    ,{
      "long"
    }
    )
  }

  //====float
  function testPrimitiveFloatTypeIsShown() {
    test(
      "//PROGRAM, pkg/MyProgram\n" +
      "var myFloat : f^^\n"
    ,{
      "float"
    }
    )
  }

  //====double
  function testPrimitiveDoubleTypeIsShown() {
    test(
      "//PROGRAM, pkg/MyProgram\n" +
      "var myDouble : d^^\n"
    ,{
      "double"
    }
    )
  }
}