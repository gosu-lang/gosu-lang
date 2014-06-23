/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion

uses gw.testharness.Disabled

class TypeCompletionTest extends AbstractCodeCompletionTest {

  override function shouldReturnType(): boolean {
    return false;
  }

  function testStringTypeIsShownOnColonInVarStatement() {

    test(
      "package pkg\n" +
      "class Foo {\n" +
      "  function bar() {\n" +
      "    var x : S^^\n" +
      "  }\n" +
      "}\n"
    ,{
      "String (java.lang)"
    } 
    )
  }

  function testShortPrimitiveTypeIsNotShownOnColonInVarStatement() {
    testNoItems({
      "package pkg\n" +
      "class Foo {\n" +
      "  function bar() {\n" +
      "    var x : S^^\n" +
      "  }\n" +
      "}\n" }
    ,{
      "short"
    }
    )
  }

  function testShortPrimitiveTypeIsShownOnColonInVarStatement() {
    test(
      "package pkg\n" +
      "class Foo {\n" +
      "  function bar() {\n" +
      "    var x : s^^\n" +
      "  }\n" +
      "}\n"
    ,{
      "short"
    }
    )
  }


  function testStringTypeIsShownInTypeParamInVarStatement() {
    test(
      "package pkg\n" +
      "class Foo {\n" +
      "  function bar() {\n" +
      "    var x : List<S^^\n" +
      "  }\n" +
      "}\n"
    ,{
      "String (java.lang)"
    }
    )
  }

  function testShortPrimitiveTypeIsNotShownInTypeParamInVarStatement() {

    testNoItems({
      "package pkg\n" +
      "class Foo {\n" +
      "  function bar() {\n" +
      "    var x : List<S^^\n" +
      "  }\n" +
      "}\n" }
    ,{
      "short"
    }
    )
  }

  function testStringTypeIsShownOnColonInMethodParam() {
    test(
      "package pkg\n" +
      "class Foo {\n" +
      "  function bar(x : S^^) {}\n" +
      "}\n"
    ,{
        "String (java.lang)"
    }
    )
  }

  function testShortPrimitiveTypeIsNotShownOnColonInMethodParam() {
    testNoItems({
      "package pkg\n" +
      "class Foo {\n" +
      "  function bar(x : S^^) {}\n" +
      "}\n" }
    ,{
      "short"
    }
    )
  }

  function testShortPrimitiveTypeIsShownOnColonInMethodParam() {
    test(
      "package pkg\n" +
      "class Foo {\n" +
      "  function bar(x : s^^) {}\n" +
      "}\n"
    ,{
      "short"
    }
    )
  }

  function testStringTypeIsNotShownOnColonInMethodParam() {
    test({
      "package pkg\n" +
      "class Foo {\n" +
      "  function bar(x : S^^) {}\n" +
      "}\n" }
    ,{
      "String (java.lang)"
    }
    )
  }

  function testStringTypeIsShownInTypeParamInMethodParam() {
    test(
      "package pkg\n" +
      "class Foo {\n" +
      "  function bar(x : List<S^^) {}\n" +
      "}\n"
    ,{
      "String (java.lang)"
    }
    )
  }

  function testShortPrimitiveTypeIsNotShownInTypeParamInMethodParam() {
    testNoItems({
      "package pkg\n" +
      "class Foo {\n" +
      "  function bar(x : List<S^^) {}\n" +
      "}\n" }
    ,{
      "short"
    }
    )
  }

  function testStringTypeIsShownOnColonInReturnType() {
    test(
      "package pkg\n" +
      "class Foo {\n" +
      "  function bar() : S^^ {}\n" +
      "}\n"
    ,{
      "String (java.lang)"
    }
    )
  }

  function testShortPrimitiveTypeIsNotShownOnColonInReturnType() {
    testNoItems({
      "package pkg\n" +
      "class Foo {\n" +
      "  function bar() : S^^ {}\n" +
      "}\n" }
    ,{
      "short"
    }
    )
  }

  function testShortPrimitiveTypeIsShownOnColonInReturnType() {
    test(
      "package pkg\n" +
      "class Foo {\n" +
      "  function bar() : s^^ {}\n" +
      "}\n"
    ,{
      "short"
    }
    )
  }

  function testStringTypeIsShownOnColonInReturnType1() {
    test({
      "package pkg\n" +
      "class Foo {\n" +
      "  function bar() : S^^{}\n" +
      "}\n" }
    ,{
      "String (java.lang)"
    }
    )
  }

  function testStringTypeIsShownInTypeParamInReturnType() {
    test(
      "package pkg\n" +
      "class Foo {\n" +
      "  function bar() : List<S^^ {}\n" +
      "}\n"
    ,{
      "String (java.lang)"
    }
    )
  }

  function testShortPrimitiveTypeIsNotShownInTypeParamInReturnType() {
    testNoItems({
      "package pkg\n" +
      "class Foo {\n" +
      "  function bar() : List<S^^ {}\n" +
      "}\n" }
    ,{
      "short"
    }
    )
  }

  function testStringTypeIsShownOnColonInBlockParam() {
    test(
      "package pkg\n" +
      "class Foo {\n" +
      "  function bar() {\n" +
      "    var x = \\ n : S^^\n" +
      "  }\n" +
      "}\n"
    ,{
      "String (java.lang)"
    }
    )
  }

  function testShortPrimitiveTypeIsNotShownOnColonInBlockParam() {
    testNoItems({
      "package pkg\n" +
      "class Foo {\n" +
      "  function bar() {\n" +
      "    var x = \\ n : S^^\n" +
      "  }\n" +
      "}\n" }
    ,{
      "short"
    }
    )
  }

  function testShortPrimitiveTypeIsShownOnColonInBlockParam() {
    test(
      "package pkg\n" +
      "class Foo {\n" +
      "  function bar() {\n" +
      "    var x = \\ n : s^^\n" +
      "  }\n" +
      "}\n"
    ,{
      "short"
    }
    )
  }

  function testStringTypeIsShownOnColonInBlockParam1() {
    test({
      "package pkg\n" +
      "class Foo {\n" +
      "  function bar() {\n" +
      "    var x = \\ n : S^^\n" +
      "  }\n" +
      "}\n" }
    ,{
      "String (java.lang)"
    }
    )
  }

  function testStringTypeIsShownInTypeParamInBlockParam() {
    test(
      "package pkg\n" +
      "class Foo {\n" +
      "  function bar() {\n" +
      "    var x = \\ n : List<S^^\n" +
      "  }\n" +
      "}\n"
    ,{
      "String (java.lang)"
    }
    )
  }

  function testShortPrimitiveTypeIsNotShownInTypeParamInBlockParam() {
    testNoItems({
      "package pkg\n" +
      "class Foo {\n" +
      "  function bar() {\n" +
      "    var x = \\ n : List<S^^\n" +
      "  }\n" +
      "}\n" }
    ,{
      "short"
    }
    )
  }

  function testThrowablesTypeIsShownInCatchClause(){
    test(
      "package pkg\n" +
      "uses java.lang.*\n" +
      "class Foo  {\n" +
      "  function bar() {\n" +
      "    try {\n" +
      "    } catch (e : N^^)\n" +
      "  }\n" +
      "}"
    , {
      "NullPointerException (java.lang)"
    }
    )
  }

  function testTypesDefinedInOtherPkgAreShownInCatchClause(){
    test({
      "package apkg\n" +
      "class AGosuClass extends Exception {\n" +
      "  construct(){}\n" +
      "  construct(msg : String){\n" +
      "    super(msg)\n" +
      "  }\n" +
      "}"
    ,
      "//JAVA \n" +
      "package apkg;\n" +
      "public class AJavaClass extends Exception {\n" +
      "  public AJavaClass(){}\n" +
      "  public AJavaClass(String msg) {\n" +
      "    super(msg);\n" +
      "  }\n" +
      "}"
    ,
      "package pkg\n" +
      "uses apkg.*\n" +
      "class Foo  {\n" +
      "  function bar() {\n" +
      "    try {\n" +
      "    } catch (e : A^^)\n" +
      "  }\n" +
      "}"
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
      "  construct(){}\n" +
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
      "  public AJavaClass() {}\n" +
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
      "package pkg\n" +
      "uses apkg.*\n" +
      "class Foo  {\n" +
      "  function bar() {\n" +
      "    try {\n" +
      "    } catch (e : B^^)\n" +
      "  }\n" +
      "}"
    }
    , {
      "BGosuClass", "BJavaClass"
    }
    )
  }

  function testTypeItselfIsShownInNewExpression() {
    test(
      "package pkg\n" +
      "class Foo {\n" +
      "  function bar() {\n" +
      "    new F^^\n" +
      "  }\n" +
      "}\n"
    ,{
      "Foo (pkg)"
    }
    )
  }

  function testTypesInLibAreShownInNewExpression() {
    test(
      "package pkg\n" +
      "uses java.lang.*\n" +
      "class Foo {\n" +
      "  function bar() {\n" +
      "    new S^^\n" +
      "  }\n" +
      "}\n"
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
      "package pkg\n" +
      "uses apkg.*\n" +
      "class Foo  {\n" +
      "  function bar() {\n" +
      "    new B^^\n" +
      "  }\n" +
      "}"
    }
    , {
      "BGosuClass", "BJavaClass"
    }
    )
  }

  @Disabled("dpetrusca", "corner case")
  function testNonThrowablesTypesInLibArentShownInNewExpressionAfterThrow() {
    testNoItems({
      "package pkg\n" +
      "uses java.lang.*\n" +
      "class Foo {\n" +
      "  function bar() {\n" +
      "    throw new Str^^\n" +
      "  }\n" +
      "}\n"
    }
    ,{
      "StringBuffer (java.lang)", "String (java.lang)"
    }
    )
  }

  @Disabled( "smckinney", "Requires parsing on proposal types, which we don't do for performance reasons" )
  function testThrowablesTypeInLibIsShownInNewExpressionAfterThrow() {
    testNoItems({
      "package pkg\n" +
      "uses java.lang.*\n" +
      "class Foo {\n" +
      "  function bar() {\n" +
      "    throw new N^^\n" +
      "  }\n" +
      "}\n"
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
      "package pkg\n" +
      "uses apkg.*\n" +
      "class Foo  {\n" +
      "  function bar() {\n" +
      "    throw new B^^\n" +
      "  }\n" +
      "}"
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
      "  construct(){}\n" +
      "  construct(msg : String){\n" +
      "    super(msg)\n" +
      "  }\n" +
      "}"
    ,
      "//JAVA \n" +
      "package pkg;\n" +
      "public class AJavaClass extends Exception {\n" +
      "  public AJavaClass() {}\n" +
      "  public AJavaClass(String msg) {\n" +
      "    super(msg);\n" +
      "  }\n" +
      "}"
    ,
      "package pkg\n" +
      "class Foo  {\n" +
      "  function bar() {\n" +
      "    try {\n" +
      "    } catch (e : A^^)\n" +
      "  }\n" +
      "}"
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
      "    function bar() {}\n" +
      "  }\n" +
      "}\n"
    ,
      "package pkg\n" +
      "uses pkg.TestClass.*\n" +
      "class Foo {\n" +
      "  function bar() {\n" +
      "    new S^^\n" +
      "  }\n" +
      "}\n"
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
      "package pkg\n" +
      "uses test.OuterClass.*\n" +
      "class Foo {\n" +
      "  function bar() {\n" +
      "    new S^^\n" +
      "  }\n" +
      "}\n"}
    , {
      "StaticInnerClass (test.OuterClass)"
    }
    )
  }

  function testAnnotationTypesIsShownInAnnotationUsage() {
    test({
      "package pkg\n" +
      "uses java.lang.*\n" +
      "class Foo {\n" +
      "  @D^^\n" +
      "  function bar() {}\n" +
      "}\n"}
    ,{
      "Deprecated (java.lang)"
    }
    )
  }

  @Disabled( "smckinney", "Requires parsing on proposal types, which we don't do for performance reasons" )
  function testNonAnnotationTypesArentShownInAnnotationUsage() {
    testNoItems({
      "package pkg\n" +
      "uses java.lang.*\n" +
      "class Foo {\n" +
      "  @^^\n" +
      "  function bar() {}\n" +
      "}\n"}
    ,{
      "StringBuffer (java.lang)"
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
      "package pkg\n" +
      "uses apkg.*\n" +
      "class Foo {\n" +
      "  @A^^\n" +
      "  function bar() {}\n" +
      "}\n"}
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
      "    static class StaticInnerClass implements IAnnotation { }\n" +
      "}\n"
    ,
      "package pkg\n" +
      "uses apkg.OuterClass.*\n" +
      "class Foo {\n" +
      "  @S^^\n" +
      "  function bar() {}\n" +
      "}\n"}
    ,{
      "StaticInnerClass (apkg.OuterClass)"
    })
  }

  @Disabled("dpetrusca", "fix in phase 2")
  function testTypesUnderSamePackageAreShown() {
    test({
      "//JAVA \n" +
      "package pkg;\n" +
      "public class AJavaClass { \n" +
      "}",
      "package pkg\n" +
      "class AGosuClass { \n" +
      "}",
      "package pkg\n" +
      "class Foo { \n" +
      "  function  foo() {\n" +
      "    A^^ \n" +
      "  }\n" +
      "}"
    }
    , {
      "AGosuClass (pkg)", "AJavaClass (pkg)"
    }
    )
  }

  @Disabled("dpetrusca", "fix in phase 2")
  function testTypesInUsesAreShown() {
    test({
      "//JAVA \n" +
      "package otherpkg;\n" +
      "public class AJavaClass { \n" +
      "}",
      "package otherpkg\n" +
      "class AGosuClass { \n" +
      "}",
      "package pkg\n" +
      "uses otherpkg.*" +
      "class Foo { \n" +
      "  function  foo() {\n" +
      "    A^^ \n" +
      "  }\n" +
      "}"
    }
    , {
      "AGosuClass (otherpkg)", "AJavaClass (otherpkg)"
    }
    )
  }

  function testTypesAreShownAsEnhancedTypeInEnhancement() {
    test ( {
      "package some.pkg1\n" +
      "class GosuClass1 {\n" +
      "}" ,
      "package some.pkg1\n" +
      "class GosuClass2 {\n" +
      "}" ,
      "package some.pkg\n" +
      "uses some.pkg1.*" +
      "enhancement GosuClassEnhencement : Gosu^^ {\n" +
      "}"}
      ,{
      "GosuClass1 (some.pkg1)", "GosuClass2 (some.pkg1)"
    }
    )
  }

  function testNoWildcardTypeIsShownAsEnhancedTypeInEnhancement() {
    testNoItems ( {
      "package some.pkg1\n" +
      "class GosuClass1 {\n" +
      "}" ,
      "package some.pkg1\n" +
      "class GosuClass2 {\n" +
      "}" ,
      "package some.pkg\n" +
      "uses some.pkg1.*" +
      "enhancement GosuClassEnhencement : ^^ {\n" +
      "}"}
      ,{
      "*"
    }
    )
  }

  //default types completion

  //====String has been covered
  //====Boolean
  function testBooleanTypeIsShown() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var myBoolean : B^^\n" +
      "}"
    ,{
      "Boolean (java.lang)"
    }
    )
  }

  //====List
  function testListTypeIsShown() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var myList : L^^\n" +
      "}"
    ,{
      "List<E> (java.util)"
    }
    )
  }

  //====Object
  function testObjectTypeIsShown() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var myObject : O^^\n" +
      "}"
    ,{
      "Object (java.lang)"
    }
    )
  }

  //====Type
  @Disabled("dpetrusca", "fix in phase 2")
  function testTypeTypeIsShown() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var myType : T^^\n" +
      "}"
    ,{
      "Type"
    }
    )
  }

  //primitive data types
  //====short has been covered
  //====int
  function testPrimitiveIntTypeIsShown() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var myInt : i^^\n" +
      "}"
    ,{
      "int"
    }
    )
  }

  //====char
  function testPrimitiveCharTypeIsShown() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var myChar : c^^\n" +
      "}"
    ,{
      "char"
    }
    )
  }

  //====boolean, byte
  function testPrimitiveBooleanTypeIsShown() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var myBoolean : b^^\n" +
      "}"
    ,{
      "boolean", "byte"
    }
    )
  }

  //====long
  function testPrimitiveLongTypeIsShown() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var myLong : l^^\n" +
      "}"
    ,{
      "long"
    }
    )
  }

  //====float
  function testPrimitiveFloatTypeIsShown() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var myFloat : f^^\n" +
      "}"
    ,{
      "float"
    }
    )
  }

  //====double
  function testPrimitiveDoubleTypeIsShown() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var myDouble : d^^\n" +
      "}"
    ,{
      "double"
    }
    )
  }

  //java.lang.Number
  //====AtomicInteger, AtomicLong
  function testAtomicIntegerTypeIsShown() {
    test(
      "package some.pkg\n" +
      "uses java.util.concurrent.atomic.*\n" +
      "class GosuClass {\n" +
      "  var variable : A^^\n" +
      "}"
    ,{
      "AtomicInteger (java.util.concurrent.atomic)", "AtomicLong (java.util.concurrent.atomic)"
    }
    )
  }

  //====BigDecimal, BigInteger
  function testBigDecimalTypeIsShown() {
    test(
      "package some.pkg\n" +
      "uses java.math.*\n" +
      "class GosuClass {\n" +
      "  var variable : B^^\n" +
      "}"
    ,{
      "BigDecimal (java.math)", "BigInteger (java.math)"
    }
    )
  }

  //====Double
  function testDoubleTypeIsShown() {
    test(
      "package some.pkg\n" +
      "uses java.lang.*\n" +
      "class GosuClass {\n" +
      "  var variable : D^^\n" +
      "}"
    ,{
      "Double (java.lang)"
    }
    )
  }

  //====Float
  function testFloatTypeIsShown() {
    test(
      "package some.pkg\n" +
      "uses java.lang.*\n" +
      "class GosuClass {\n" +
      "  var variable : F^^\n" +
      "}"
    ,{
      "Float (java.lang)"
    }
    )
  }

  //====Integer
  function testIntegerTypeIsShown() {
    test(
      "package some.pkg\n" +
      "uses java.lang.*\n" +
      "class GosuClass {\n" +
      "  var variable : I^^\n" +
      "}"
    ,{
      "Integer (java.lang)"
    }
    )
  }

  //====Long
  function testLongTypeIsShown() {
    test(
      "package some.pkg\n" +
      "uses java.lang.*\n" +
      "class GosuClass {\n" +
      "  var variable : L^^\n" +
      "}"
    ,{
      "Long (java.lang)"
    }
    )
  }

  //====Short
  function testShortTypeIsShown() {
    test(
      "package some.pkg\n" +
      "uses java.lang.*\n" +
      "class GosuClass {\n" +
      "  var variable : S^^\n" +
      "}"
    ,{
      "Short (java.lang)"
    })
  }

  //compound type
  function testCompletionForCompoundType() {
    test(
      "package some.pkg\n" +
      "uses java.lang.Runnable\n" +
      "uses java.io.Closeable\n" +
      "class GosuClass {\n" +
      "  var variable : Closeable & Run^^\n" +
      "}"
    ,{
      "Runnable (java.lang)"
    })
  }

  function testCompletionForCompoundType1() {
    test(
      "package some.pkg\n" +
      "uses java.lang.Runnable\n" +
      "uses java.io.Closeable\n" +
      "class GosuClass {\n" +
      "  var variable : Close^^able & Runnable\n" +
      "}"
    ,{
      "Closeable (java.io)"
    })
  }

  function testCompletionForCompoundTypeInTypeParam() {
    test(
      "package some.pkg\n" +
      "uses java.io.Closeable\n" +
      "class GosuClass<T extends java.lang.Number & Closea^^ {\n" +
      "}"
    ,{
      "Closeable (java.io)"
    })
  }


  // inner class/interface in a program
  @Disabled("dpetrusca", "fix in phase 2")
  function testNoInnerClassInProgramIsShown() {
    testNoItems({
      "//PROGRAM, pkg/MyProgram\n" +
      "class InnerClass{}\n",
      "package pkg\n" +
      "class GosuClass {\n" +
      "  var variable : InnerClass^^\n" +
      "}"
    }
    ,{
      "InnerClass (pkg.MyProgram)"
    }
    )
  }

  @Disabled("dpetrusca", "fix in phase 2")
  function testNoInnerInterfaceInProgramIsShown() {
    testNoItems({
      "//PROGRAM, pkg/MyProgram\n" +
      "interface InnerInterface{}\n",
      "package pkg\n" +
      "class GosuClass {\n" +
      "  var variable : InnerInterface^^\n" +
      "}"
    }
    ,{
      "InnerInterface (pkg.MyProgram)"
    }
    )
  }

  // private inner class in another class
  function testNoPrivateInnerClassInOtherClassIsShown() {
    testNoItems({
      "package pkg1\n" +
      "class GosuClass {\n" +
      "  private class InnerClass{ }\n" +
      "}",
      "package pkg2\n" +
      "class TestClass {\n" +
      "  var variable : Inner^^\n" +
      "}"}
    ,{
      "InnerClass (pkg1.GosuClass)"
    })
  }

  @Disabled( "smckinney", "Requires parsing on proposal types, which we don't do for performance reasons" )
  function testNoPackageProtectedClassInOtherClassIsShown() {
    testNoItems({
      "package pkg1\n" +
      "internal class GosuClass {\n" +
      "}",
      "package pkg2\n" +
      "class TestClass {\n" +
      "  var variable : GosuC^^\n" +
      "}"}
    ,{
      "GosuClass (pkg1)"
    })
  }



}