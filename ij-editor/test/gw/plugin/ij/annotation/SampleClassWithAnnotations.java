/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.annotation;

import static gw.plugin.ij.annotation.JavaClassWithStaticsToImport.CONST1;
import static gw.plugin.ij.annotation.JavaEnum.ONE;
import static gw.plugin.ij.annotation.JavaEnum.TWO;

/**
 */
@SuppressWarnings({"UnusedDeclaration"})
public class SampleClassWithAnnotations
{
  private static final int IntConst = 42;
  private static final int IndirectIntConst = IntConst;
  private static final int QIndirectIntConst = SampleClassWithAnnotations.IntConst;

  //
  // Int tests
  //
  @JavaIntAnnotation(value=42)
  public void testIntLiteral() {}

  @JavaIntAnnotation(value=IntConst)
  public void testIntConstField() {}

  @JavaIntAnnotation(IntConst)
  public void testIntConstFieldDefault() {}

  @JavaIntAnnotation(value=IndirectIntConst)
  public void testIndirectIntConstField() {}

  @JavaIntAnnotation(value=SampleClassWithAnnotations.IntConst)
  public void testQIntConstField() {}

  @JavaIntAnnotation(value=QIndirectIntConst)
  public void testQIndirectIntConstField() {}

  @JavaIntAnnotation(value=QIndirectIntConst + IntConst)
  public void testAddition() {}

  @JavaIntAnnotation(value=QIndirectIntConst - IntConst)
  public void testSubtraction() {}

  @JavaIntAnnotation(value=QIndirectIntConst * IntConst)
  public void testMultiplication() {}

  @JavaIntAnnotation(value=15 / 5)
  public void testDivision() {}

  @JavaIntAnnotation(value=12 % 4)
  public void testModulo() {}

  @JavaIntAnnotation( value=(12 + (2 * (2))) )
  public void testParenthesized() {}

  @JavaIntAnnotation(value=-80 >> 2)
  public void testSignedRightShift() {}

  @JavaIntAnnotation(value=8 << 2)
  public void testLeftShift() {}

  @JavaIntAnnotation(value=1001 >>> 1)
  public void testUnsignedRightShift() {}

  @JavaIntAnnotation(value=2&4)
  public void testBitwiseAnd() {}

  @JavaIntAnnotation(value=2|4)
  public void testBitwiseOr() {}

  @JavaIntAnnotation(value=2^4)
  public void testBitwiseXOr() {}

  @JavaIntAnnotation(value=-88)
  public void testUnaryMinus() {}

  @JavaIntAnnotation(value=+88)
  public void testUnaryPlus() {}

  @JavaIntAnnotation(value=~88)
  public void testUnaryNot() {}

  @JavaBooleanAnnotation(value=2>4)
  public void testGreater() {}

  @JavaBooleanAnnotation(value=2<4)
  public void testLess() {}

  @JavaBooleanAnnotation(value=2<=4)
  public void testLessEqual() {}

  @JavaBooleanAnnotation(value=2>=4)
  public void testGreaterEqual() {}

  @JavaBooleanAnnotation(value=2==4)
  public void testEquals() {}

  @JavaBooleanAnnotation(value=2!=4)
  public void testNotEquals() {}

  @JavaBooleanAnnotation(value=true && false)
  public void testConditionalAnd() {}

  @JavaBooleanAnnotation(value=true || false)
  public void testConditionalOr() {}

  @JavaIntAnnotation(value= (2>4) ? 3 : 5)
  public void testConditionalTernary() {}

  @JavaIntAnnotation(value=(int)4.2)
  public void testIntCast() {}

  //
  // String tests
  //
  @JavaStringAnnotation(foo="hello")
  public void testStringLiteral() {}

  @JavaStringArrayAnnotation(foo={"hello", "bye"})
  public void testStringArrayLiteral() {}

  @JavaStringAndStringArrayAnnotation(foo={"hello", "bye"}, bar="fred")
  public void testStringAndStringArrayLiteral() {}

  @JavaComplexAnnotation(foo={"hello", "bye"}, bar="fred", moo=123.00, noo=123)
  public void testComplexLiteral() {}

  @JavaStringAnnotation(foo=JavaClassWithStaticsToImport.CONST1)
  public void testQStringConstField() {}

  @JavaStringAnnotation(foo=gw.plugin.ij.annotation.JavaClassWithStaticsToImport.CONST1)
  public void testFQStringConstField() {}

  @JavaStringAnnotation(foo=CONST1)
  public void testStringConstField() {}

  @JavaStringAnnotation(foo=CONST1 + CONST1)
  public void testStringConcatenation() {}

  //
  // Enum tests
  //
  @JavaEnumAnnotation(value=JavaEnum.ONE)
  public void testQEnumConst() {}

  @JavaEnumAnnotation(value=gw.plugin.ij.annotation.JavaEnum.ONE)
  public void testFQEnumConst() {}

  @JavaEnumAnnotation(value=ONE)
  public void testEnumConst() {}

  @JavaEnumArrayAnnotation({ONE, TWO})
  public void testEnumArray() {}

  //
  // Class tests
  //
  @JavaClassAnnotation(SampleClassWithAnnotations.class)
  public void testClassConst() {}

  //
  // Annotations tests
  //
  @JavaAnnotationAnnotation(@JavaIntAnnotation(value = 8))
  public void testAnnotation() {}

  @JavaAnnotationArrayAnnotation({@JavaIntAnnotation(value = 8),@JavaIntAnnotation(value = 9)})
  public void testAnnotationArray() {}
}
