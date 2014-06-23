/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.annotation

uses gw.internal.gosu.parser.ClassAnnotationInfo
uses gw.plugin.ij.framework.GosuTestCase

@gw.testharness.Disabled("tlin", "Needs to be finished")
class SourceAnnotationFromJavaTest extends GosuTestCase {

  //@JavaIntAnnotation(value=42)
  function testIntLiteral() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testIntLiteral", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(42, annotations2[0].getFieldValue("value") )
  }

  //@JavaIntAnnotation(value=IntConst)
  function testIntConstField() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testIntConstField", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(42, annotations2[0].getFieldValue("value") )
  }

  //@JavaIntAnnotation(IntConst)
  function testIntConstFieldDefault() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testIntConstFieldDefault", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(42, annotations2[0].getFieldValue("value") )
  }

  //@JavaIntAnnotation(value=IndirectIntConst)
  function testIndirectIntConstField() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testIndirectIntConstField", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(42, annotations2[0].getFieldValue("value") )
  }

  //@JavaIntAnnotation(value=SourceAnnotationFromJavaTest.IntConst)
  function testQIntConstField() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testQIntConstField", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(42, annotations2[0].getFieldValue("value") )
  }

  //@JavaIntAnnotation(value=QIndirectIntConst)
  function testQIndirectIntConstField() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testQIndirectIntConstField", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(42, annotations2[0].getFieldValue("value") )
  }

  //@JavaIntAnnotation(value=QIndirectIntConst + IntConst)
  function testAddition() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testAddition", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(84, annotations2[0].getFieldValue("value") )
  }

  //@JavaIntAnnotation(value=QIndirectIntConst - IntConst)
  function testSubtraction() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testSubtraction", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(0, annotations2[0].getFieldValue("value") )
  }

  //@JavaIntAnnotation(value=QIndirectIntConst * IntConst)
  function testMultiplication() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testMultiplication", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(1764, annotations2[0].getFieldValue("value") )
  }

  //@JavaIntAnnotation(value=15 / 5)
  function testDivision() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testDivision", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(3, annotations2[0].getFieldValue("value") )
  }

  //@JavaIntAnnotation(value=12 % 4)
  function testModulo() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testModulo", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(0, annotations2[0].getFieldValue("value") )
  }

  //@JavaIntAnnotation( value=(12 + (2 * (2))) )
  function testParenthesized() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testParenthesized", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(16, annotations2[0].getFieldValue("value") )
  }

  //@JavaIntAnnotation(value=-80 >> 2)
  function testSignedRightShift() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testSignedRightShift", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(-20, annotations2[0].getFieldValue("value") )
  }

  //@JavaIntAnnotation(value=8 << 2)
  function testLeftShift() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testLeftShift", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(32, annotations2[0].getFieldValue("value") )
  }

  //@JavaIntAnnotation(value=1001 >>> 1)
  function testUnsignedRightShift() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testUnsignedRightShift", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(500, annotations2[0].getFieldValue("value") )
  }

  //@JavaIntAnnotation(value=2&4)
  function testBitwiseAnd() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testBitwiseAnd", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(0, annotations2[0].getFieldValue("value") )
  }

  //@JavaIntAnnotation(value=2|4)
  function testBitwiseOr() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testBitwiseOr", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(6, annotations2[0].getFieldValue("value") )
  }

  //@JavaIntAnnotation(value=2^4)
  function testBitwiseXOr() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testBitwiseXOr", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(6, annotations2[0].getFieldValue("value") )
  }

  //@JavaIntAnnotation(value=-88)
  function testUnaryMinus() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testUnaryMinus", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(-88, annotations2[0].getFieldValue("value") )
  }

  //@JavaIntAnnotation(value=+88)
  function testUnaryPlus() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testUnaryPlus", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(88, annotations2[0].getFieldValue("value") )
  }

  //@JavaIntAnnotation(value=~88)
  function testUnaryNot() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testUnaryNot", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(-89, annotations2[0].getFieldValue("value") )
  }

  //@JavaBooleanAnnotation(value=2>4)
  function testGreater() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testGreater", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(false, annotations2[0].getFieldValue("value") )
  }

  //@JavaBooleanAnnotation(value=2<4)
  function testLess() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testLess", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(true, annotations2[0].getFieldValue("value") )
  }

  //@JavaBooleanAnnotation(value=2<=4)
  function testLessEqual() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testLessEqual", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(true, annotations2[0].getFieldValue("value") )
  }

  //@JavaBooleanAnnotation(value=2>=4)
  function testGreaterEqual() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testGreaterEqual", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(false, annotations2[0].getFieldValue("value") )
  }

  //@JavaBooleanAnnotation(value=2==4)
  function testEquals() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testEquals", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(false, annotations2[0].getFieldValue("value") )
  }

  //@JavaBooleanAnnotation(value=2!=4)
  function testNotEquals() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testNotEquals", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(true, annotations2[0].getFieldValue("value") )
  }

  //@JavaBooleanAnnotation(value=true && false)
  function testConditionalAnd() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testConditionalAnd", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(false, annotations2[0].getFieldValue("value") )
  }

  //@JavaBooleanAnnotation(value=true || false)
  function testConditionalOr() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testConditionalOr", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(true, annotations2[0].getFieldValue("value") )
  }

  //@JavaIntAnnotation(value= (2>4) ? 3 : 5)
  function testConditionalTernary() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testConditionalTernary", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(5, annotations2[0].getFieldValue("value") )
  }

  //@JavaIntAnnotation(value=(int)4.2)
  function testIntCast() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testIntCast", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(4, annotations2[0].getFieldValue("value") )
  }

  //
  // String tests
  //

  //@JavaStringAnnotation(foo="hello")
  function testStringLiteral() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testStringLiteral", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals("hello", annotations2[0].getFieldValue("foo") )
  }

  //@JavaStringArrayAnnotation(foo={"hello", "bye"})
  function testStringArrayLiteral() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testStringArrayLiteral", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    var stringArray = annotations2[0].getFieldValue("foo") as String[]
    assertEquals("hello", stringArray[0] )
    assertEquals("bye", stringArray[1] )
  }

  //@JavaStringAndStringArrayAnnotation(foo={"hello", "bye"}, bar="fred")
  function testStringAndStringArrayLiteral() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testStringAndStringArrayLiteral", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    var stringArray = annotations2[0].getFieldValue("foo") as String[]
    assertEquals("hello", stringArray[0] )
    assertEquals("bye", stringArray[1] )
    assertEquals("fred", annotations2[0].getFieldValue("bar") )
  }

  //@JavaComplexAnnotation(foo={"hello", "bye"}, bar="fred", moo=123.00, noo=123)
  function testComplexLiteral() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testComplexLiteral", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    var stringArray = annotations2[0].getFieldValue("foo") as String[]
    assertEquals("hello", stringArray[0] )
    assertEquals("bye", stringArray[1] )
    assertEquals("fred", annotations2[0].getFieldValue("bar") )
    assertEquals(123.00, annotations2[0].getFieldValue("moo").toString().toDouble(), 0)
    assertEquals(123, annotations2[0].getFieldValue("noo").toString().toInt() )
  }

  //@JavaStringAnnotation(foo=JavaClassWithStaticsToImport.CONST1)
  function testQStringConstField() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testQStringConstField", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals("uno", annotations2[0].getFieldValue("foo") )
  }

  //@JavaStringAnnotation(foo=gw.plugin.ij.annotation.JavaClassWithStaticsToImport.CONST1)
  function testFQStringConstField() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testFQStringConstField", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals("uno", annotations2[0].getFieldValue("foo") )
  }

  //@JavaStringAnnotation(foo=CONST1)
  function testStringConstField() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testStringConstField", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals("uno", annotations2[0].getFieldValue("foo") )
  }

  //@JavaStringAnnotation(foo=CONST1 + CONST1)
  function testStringConcatenation() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testStringConcatenation", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals("unouno", annotations2[0].getFieldValue("foo") )
  }

  //
  // Enum tests
  //
  //@JavaEnumAnnotation(value=JavaEnum.ONE)
  function testQEnumConst() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testQEnumConst", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(JavaEnum.ONE.name(), annotations2[0].getFieldValue("value") )
  }

  //@JavaEnumAnnotation(value=gw.plugin.ij.annotation.JavaEnum.ONE)
  function testFQEnumConst() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testFQEnumConst", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(JavaEnum.ONE.name(), annotations2[0].getFieldValue("value") )
  }

  //@JavaEnumAnnotation(value=ONE)
  function testEnumConst() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testEnumConst", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(JavaEnum.ONE.name(), annotations2[0].getFieldValue("value") )
  }

  //@JavaEnumArrayAnnotation({ONE, TWO})
  function testEnumArray() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testEnumArray", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    var enumArray = annotations2[0].getFieldValue("value") as String[]
    assertEquals(JavaEnum.ONE.name(), enumArray[0] )
    assertEquals(JavaEnum.TWO.name(), enumArray[1] )
  }

  //
  // Class tests
  //
  //@JavaClassAnnotation(SampleClassWithAnnotations.class)
  function testClassConst() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testClassConst", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(SampleClassWithAnnotations as java.lang.Class, annotations2[0].getFieldValue("value") )
  }

  //
  // Annotations tests
  //
  //@JavaAnnotationAnnotation(@JavaIntAnnotation(value = 8))
  function testAnnotation() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testAnnotation", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(8, (annotations2[0].getFieldValue("value") as ClassAnnotationInfo).getFieldValue("value") )
  }

  //@JavaAnnotationArrayAnnotation({@JavaIntAnnotation(value = 8),@JavaIntAnnotation(value = 9)})
  function testAnnotationArray() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testAnnotationArray", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    var annotationArray = annotations2[0].getFieldValue("value") as ClassAnnotationInfo[]
    assertEquals(8, annotationArray[0].getFieldValue("value"))
    assertEquals(9, annotationArray[1].getFieldValue("value"))
  }

  //Gosu
  @JavaIntAnnotation(42)
  function testIntLiteralInGosu() {
    var annotations2 = SourceAnnotationFromJavaTest.Type.TypeInfo.getMethod("testIntLiteralInGosu", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(42, annotations2[0].getFieldValue("value") )
  }

  //TODO : Internal error (Plugin: Gosu): [I cannot be cast to [Ljava.lang.Object;
  //@JavaIntArrayAnnotation({42})
  function testIntArrayInGosu() {
    var annotations2 = SourceAnnotationFromJavaTest.Type.TypeInfo.getMethod("testIntArrayInGosu", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(42, (annotations2[0].getFieldValue("value") as int[])[0])
  }

  @JavaIntArrayAnnotation(42)
  function testIntAsArrayInGosu() {
    var annotations2 = SourceAnnotationFromJavaTest.Type.TypeInfo.getMethod("testIntAsArrayInGosu", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(42, (annotations2[0].getFieldValue("value") as int[])[0])
  }

  @JavaStringAnnotation("hello")
  function testStringLiteralInGosu() {
    var annotations2 = SourceAnnotationFromJavaTest.Type.TypeInfo.getMethod("testStringLiteralInGosu", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals("hello", annotations2[0].getFieldValue("foo") )
  }

  @JavaStringArrayAnnotation("hello")
  function testStringLiteralAsArrayInGosu() {
    var annotations2 = SourceAnnotationFromJavaTest.Type.TypeInfo.getMethod("testStringLiteralAsArrayInGosu", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals("hello", annotations2[0].getFieldValue("foo") )
  }

  @JavaStringArrayAnnotation({"hello", "bye"})
  function testStringArrayLiteralInGosu() {
    var annotations2 = SourceAnnotationFromJavaTest.Type.TypeInfo.getMethod("testStringArrayLiteralInGosu", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    var stringArray = annotations2[0].getFieldValue("foo") as String[]
    assertEquals("hello", stringArray[0] )
    assertEquals("bye", stringArray[1] )
  }

  @JavaStringAndStringArrayAnnotation("fred", {"hello", "bye"})
  function testStringAndStringArrayLiteralInGosu() {
    var annotations2 = SourceAnnotationFromJavaTest.Type.TypeInfo.getMethod("testStringAndStringArrayLiteralInGosu", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    var stringArray = annotations2[0].getFieldValue("foo") as String[]
    assertEquals("hello", stringArray[0] )
    assertEquals("bye", stringArray[1] )
    assertEquals("fred", annotations2[0].getFieldValue("bar") )
    fail("It should not pass according to Scott!!!")
  }

  @JavaComplexAnnotation("fred", {"hello", "bye"}, 123.00, 123)
  function testComplexLiteralInGosu() {
    var annotations2 = SourceAnnotationFromJavaTest.TypeInfo.getMethod("testStringAndStringArrayLiteralInGosu", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    var stringArray = annotations2[0].getFieldValue("foo") as String[]
    assertEquals("hello", stringArray[0] )
    assertEquals("bye", stringArray[1] )
    assertEquals(123.00, annotations2[0].getFieldValue("moo").toString().toDouble(), 0)
    assertEquals(123, annotations2[0].getFieldValue("noo").toString().toInt() )
  }

  //cast
  @JavaIntAnnotation((int)4.2)
  function testIntJavaStyleCastInGosu() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("SampleClassWithAnnotations", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(4, annotations2[0].getFieldValue("value") )
  }

  @JavaIntAnnotation(4.2 as int)
  function testIntCastInGosu() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testIntCastInGosu", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(4, annotations2[0].getFieldValue("value") )
  }

  //enum
  @JavaEnumAnnotation(JavaEnum.ONE)
  function testQEnumConstInGosu() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testQEnumConstInGosu", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(JavaEnum.ONE.name(), annotations2[0].getFieldValue("value") )
  }

  @JavaEnumAnnotation(gw.plugin.ij.annotation.JavaEnum.ONE)
  function testFQEnumConstInGosu() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testFQEnumConstInGosu", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(JavaEnum.ONE.name(), annotations2[0].getFieldValue("value") )
  }

  @JavaEnumAnnotation(ONE)
  function testEnumConstInGosu() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testEnumConstInGosu", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(JavaEnum.ONE.name(), annotations2[0].getFieldValue("value") )
  }

  @JavaEnumArrayAnnotation({ONE, TWO})
  function testEnumArrayInGosu() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testEnumArrayInGosu", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    var enumArray = annotations2[0].getFieldValue("value") as String[]
    assertEquals(JavaEnum.ONE.name(), enumArray[0] )
    assertEquals(JavaEnum.TWO.name(), enumArray[1] )
  }

  @JavaEnumArrayAnnotation(ONE)
  function testEnumAsEnumArrayInGosu() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testEnumAsEnumArrayInGosu", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    var enumArray = annotations2[0].getFieldValue("value") as String[]
    assertEquals(JavaEnum.ONE.name(), enumArray[0] )
  }

  //class
  @JavaClassAnnotation(SampleClassWithAnnotations)
  function testClassConstInGosu() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testClassConstInGosu", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(SampleClassWithAnnotations as java.lang.Class, annotations2[0].getFieldValue("value") )
  }

  //TODO : Internal error (Plugin: Gosu): null
  //@JavaClassArrayAnnotation(SampleClassWithAnnotations)
  function testClassConstAsArrayInGosu() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testClassConstAsArrayInGosu", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(SampleClassWithAnnotations as java.lang.Class, (annotations2[0].getFieldValue("value") as java.lang.Class[])[0] )
  }

  //TODO : Internal error (Plugin: Gosu): A compile-time constant expression must be either primitive, String, or Enum
  //@JavaClassArrayAnnotation({SampleClassWithAnnotations})
  function testClassArrayInGosu() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testClassArrayInGosu", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(SampleClassWithAnnotations as java.lang.Class, (annotations2[0].getFieldValue("value") as java.lang.Class[])[0])
  }

  //@JavaAnnotationArrayAnnotation({@JavaIntAnnotation(8),@JavaIntAnnotation(9)})
  function testAnnotationArrayInGosu() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testAnnotationArray", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    var annotationArray = annotations2[0].getFieldValue("value") as ClassAnnotationInfo[]
    assertEquals(8, annotationArray[0].getFieldValue("value"))
    assertEquals(9, annotationArray[1].getFieldValue("value"))
  }

  //Support named annotation arguments
  @JavaIntAnnotation(:value = 42)
  function testIntLiteralInGosuNamedAnnotationArguments() {
    var annotations2 = SourceAnnotationFromJavaTest.Type.TypeInfo.getMethod("testIntLiteralInGosu", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(42, annotations2[0].getFieldValue("value") )
  }

  //TODO : Internal error (Plugin: Gosu): [I cannot be cast to [Ljava.lang.Object;
  //@JavaIntArrayAnnotation(:value = {42})
  function testIntArrayInGosuNamedAnnotationArguments() {
    var annotations2 = SourceAnnotationFromJavaTest.Type.TypeInfo.getMethod("testIntArrayInGosu", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(42, (annotations2[0].getFieldValue("value") as int[])[0])
  }

  //@JavaIntArrayAnnotation(:value = 42)
  function testIntAsArrayInGosuNamedAnnotationArguments() {
    var annotations2 = SourceAnnotationFromJavaTest.Type.TypeInfo.getMethod("testIntAsArrayInGosu", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(42, (annotations2[0].getFieldValue("value") as int[])[0])
  }

  @JavaStringAnnotation(:foo = "hello")
  function testStringLiteralInGosuNamedAnnotationArguments() {
    var annotations2 = SourceAnnotationFromJavaTest.Type.TypeInfo.getMethod("testStringLiteralInGosu", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals("hello", annotations2[0].getFieldValue("foo") )
  }

  //@JavaStringArrayAnnotation(:foo = "hello")
  function testStringLiteralAsArrayInGosuNamedAnnotationArguments() {
    var annotations2 = SourceAnnotationFromJavaTest.Type.TypeInfo.getMethod("testStringLiteralAsArrayInGosu", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals("hello", annotations2[0].getFieldValue("foo") )
  }

  @JavaStringArrayAnnotation(:foo = {"hello", "bye"})
  function testStringArrayLiteralInGosuNamedAnnotationArguments() {
    var annotations2 = SourceAnnotationFromJavaTest.Type.TypeInfo.getMethod("testStringArrayLiteralInGosu", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    var stringArray = annotations2[0].getFieldValue("foo") as String[]
    assertEquals("hello", stringArray[0] )
    assertEquals("bye", stringArray[1] )
  }

  @JavaStringAndStringArrayAnnotation(:bar = "fred", :foo = {"hello", "bye"})
  function testStringAndStringArrayLiteralInGosuNamedAnnotationArguments() {
    var annotations2 = SourceAnnotationFromJavaTest.Type.TypeInfo.getMethod("testStringAndStringArrayLiteralInGosu", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    var stringArray = annotations2[0].getFieldValue("foo") as String[]
    assertEquals("hello", stringArray[0] )
    assertEquals("bye", stringArray[1] )
    assertEquals("fred", annotations2[0].getFieldValue("bar") )
    fail("It should not pass according to Scott!!!")
  }

  @JavaComplexAnnotation(:bar = "fred", :foo = {"hello", "bye"}, :moo = 123.00, :noo = 123)
  function testComplexLiteralInGosuNamedAnnotationArguments() {
    var annotations2 = SourceAnnotationFromJavaTest.TypeInfo.getMethod("testStringAndStringArrayLiteralInGosu", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    var stringArray = annotations2[0].getFieldValue("foo") as String[]
    assertEquals("hello", stringArray[0] )
    assertEquals("bye", stringArray[1] )
    assertEquals(123.00, annotations2[0].getFieldValue("moo").toString().toDouble(), 0)
    assertEquals(123, annotations2[0].getFieldValue("noo").toString().toInt() )
  }

  //cast
  @JavaIntAnnotation(:value = 4.2 as int)
  function testIntJavaStyleCastInGosuNamedAnnotationArguments() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("SampleClassWithAnnotations", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(4, annotations2[0].getFieldValue("value") )
  }

  @JavaIntAnnotation(:value = (int) 4.2)
  function testIntCastInGosuNamedAnnotationArguments() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testIntCastInGosu", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(4, annotations2[0].getFieldValue("value") )
  }

  //enum
  @JavaEnumAnnotation(:value = JavaEnum.ONE)
  function testQEnumConstInGosuNamedAnnotationArguments() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testQEnumConstInGosu", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(JavaEnum.ONE.name(), annotations2[0].getFieldValue("value") )
  }

  @JavaEnumAnnotation(:value = gw.plugin.ij.annotation.JavaEnum.ONE)
  function testFQEnumConstInGosuNamedAnnotationArguments() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testFQEnumConstInGosu", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(JavaEnum.ONE.name(), annotations2[0].getFieldValue("value") )
  }

  @JavaEnumAnnotation(:value = ONE)
  function testEnumConstInGosuNamedAnnotationArguments() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testEnumConstInGosu", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(JavaEnum.ONE.name(), annotations2[0].getFieldValue("value") )
  }

  @JavaEnumArrayAnnotation(:value = {ONE, TWO})
  function testEnumArrayInGosuNamedAnnotationArguments() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testEnumArrayInGosu", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    var enumArray = annotations2[0].getFieldValue("value") as String[]
    assertEquals(JavaEnum.ONE.name(), enumArray[0] )
    assertEquals(JavaEnum.TWO.name(), enumArray[1] )
  }

  //@JavaEnumArrayAnnotation(:value = ONE)
  function testEnumAsEnumArrayInGosuNamedAnnotationArguments() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testEnumAsEnumArrayInGosu", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    var enumArray = annotations2[0].getFieldValue("value") as String[]
    assertEquals(JavaEnum.ONE.name(), enumArray[0] )
  }

  //class
  @JavaClassAnnotation(:value = SampleClassWithAnnotations)
  function testClassConstInGosuNamedAnnotationArguments() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testClassConstInGosu", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(SampleClassWithAnnotations as java.lang.Class, annotations2[0].getFieldValue("value") )
  }

  //TODO : Internal error (Plugin: Gosu): null
  //@JavaClassArrayAnnotation(:value = SampleClassWithAnnotations)
  function testClassConstAsArrayInGosuNamedAnnotationArguments() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testClassConstAsArrayInGosu", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(SampleClassWithAnnotations as java.lang.Class, (annotations2[0].getFieldValue("value") as java.lang.Class[])[0] )
  }

  //TODO : Internal error (Plugin: Gosu): A compile-time constant expression must be either primitive, String, or Enum
  //@JavaClassArrayAnnotation(:value = {SampleClassWithAnnotations})
  function testClassArrayInGosuNamedAnnotationArguments() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testClassArrayInGosu", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    assertEquals(SampleClassWithAnnotations as java.lang.Class, (annotations2[0].getFieldValue("value") as java.lang.Class[])[0])
  }

  //@JavaAnnotationArrayAnnotation(:value = {@JavaIntAnnotation(8),@JavaIntAnnotation(9)})
  function testAnnotationArrayInGosuNamedAnnotationArguments() {
    var annotations2 = SampleClassWithAnnotations.Type.TypeInfo.getMethod("testAnnotationArray", {}).DeclaredAnnotations
    assertEquals(1, annotations2.Count)
    var annotationArray = annotations2[0].getFieldValue("value") as ClassAnnotationInfo[]
    assertEquals(8, annotationArray[0].getFieldValue("value"))
    assertEquals(9, annotationArray[1].getFieldValue("value"))
  }

}