/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.core.expressions.arithmetic;

import gw.lang.ir.builder.IRArgConverter;
import gw.util.StreamUtil;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.math.MathContext;
import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.Writer;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: Oct 7, 2009
 * Time: 11:23:29 AM
 * To change this template use File | Settings | File Templates.
 */
public class ArithmeticTestGenerator {
  // For a given LHS, loop over every type on the RHS
  // For each combination, combine every "interesting" type on the LHS with every "interesting" type on the LHS

  public static void main(String[] args) throws IOException {
    regenTest(byte.class, new Addition());
    regenTest(Byte.class, new Addition());
    regenTest(char.class, new Addition());
    regenTest(Character.class, new Addition());
    regenTest(short.class, new Addition());
    regenTest(Short.class, new Addition());
    regenTest(int.class, new Addition());
    regenTest(Integer.class, new Addition());
    regenTest(long.class, new Addition());
    regenTest(Long.class, new Addition());
    regenTest(float.class, new Addition());
    regenTest(Float.class, new Addition());
    regenTest(double.class, new Addition());
    regenTest(Double.class, new Addition());
    regenTest(BigInteger.class, new Addition());
    regenTest(BigDecimal.class, new Addition());

    regenTest(byte.class, new Subtraction());
    regenTest(Byte.class, new Subtraction());
    regenTest(char.class, new Subtraction());
    regenTest(Character.class, new Subtraction());
    regenTest(short.class, new Subtraction());
    regenTest(Short.class, new Subtraction());
    regenTest(int.class, new Subtraction());
    regenTest(Integer.class, new Subtraction());
    regenTest(long.class, new Subtraction());
    regenTest(Long.class, new Subtraction());
    regenTest(float.class, new Subtraction());
    regenTest(Float.class, new Subtraction());
    regenTest(double.class, new Subtraction());
    regenTest(Double.class, new Subtraction());
    regenTest(BigInteger.class, new Subtraction());
    regenTest(BigDecimal.class, new Subtraction());

    regenTest(byte.class, new Multiplication());
    regenTest(Byte.class, new Multiplication());
    regenTest(short.class, new Multiplication());
    regenTest(Short.class, new Multiplication());
    regenTest(int.class, new Multiplication());
    regenTest(Integer.class, new Multiplication());
    regenTest(long.class, new Multiplication());
    regenTest(Long.class, new Multiplication());
    regenTest(float.class, new Multiplication());
    regenTest(Float.class, new Multiplication());
    regenTest(double.class, new Multiplication());
    regenTest(Double.class, new Multiplication());
    regenTest(BigInteger.class, new Multiplication());
    regenTest(BigDecimal.class, new Multiplication());

    regenTest(byte.class, new Division());
    regenTest(Byte.class, new Division());
    regenTest(short.class, new Division());
    regenTest(Short.class, new Division());
    regenTest(int.class, new Division());
    regenTest(Integer.class, new Division());
    regenTest(long.class, new Division());
    regenTest(Long.class, new Division());
    regenTest(float.class, new Division());
    regenTest(Float.class, new Division());
    regenTest(double.class, new Division());
    regenTest(Double.class, new Division());
    regenTest(BigInteger.class, new Division());
    regenTest(BigDecimal.class, new Division());
    
    regenTest(byte.class, new Remainder());
    regenTest(Byte.class, new Remainder());
    regenTest(short.class, new Remainder());
    regenTest(Short.class, new Remainder());
    regenTest(int.class, new Remainder());
    regenTest(Integer.class, new Remainder());
    regenTest(long.class, new Remainder());
    regenTest(Long.class, new Remainder());
    regenTest(float.class, new Remainder());
    regenTest(Float.class, new Remainder());
    regenTest(double.class, new Remainder());
    regenTest(Double.class, new Remainder());
    regenTest(BigInteger.class, new Remainder());
    regenTest(BigDecimal.class, new Remainder());
  }

  public static void regenTest(Class lhsType, Operator operator) throws IOException {
    File rootDir = new File("C:\\eng\\diamond\\pl2\\active\\core\\platform\\gosu-test\\src\\gw\\spec\\core\\expressions\\arithmetic\\" + operator.getName().toLowerCase());
    if (!rootDir.exists()) {
      throw new IllegalStateException("No directory found " + rootDir);
    }
    String gosuTestName = determineClassName(lhsType, operator);
    File testFile = new File(rootDir, gosuTestName + ".gs");
    Writer writer = StreamUtil.getOutputStreamWriter(new FileOutputStream(testFile));
    writer.write(genCode(lhsType, operator).toString());
    writer.flush();
    writer.close();
  }

  private static String determineClassName(Class lhsType, Operator operator) {
    return operator.getName() + "_" + functionNameForClass(lhsType) + "Test";
  }

  private static String determineByteCodeTestClassName(Class lhsType, Operator operator) {
    return operator.getName() + "_" + functionNameForClass(lhsType) + "ByteCodeTest";
  }

  public static StringBuilder genCode(Class lhsType, Operator operator) {
    StringBuilder sb = new StringBuilder();
    sb.append("package gw.spec.core.expressions.arithmetic.").append(operator.getName().toLowerCase()).append("\n");
    sb.append("uses java.lang.Byte\n");
    sb.append("uses java.lang.Character\n");
    sb.append("uses java.lang.Integer\n");
    sb.append("uses java.lang.Short\n");
    sb.append("uses java.lang.Long\n");
    sb.append("uses java.lang.Float\n");
    sb.append("uses java.lang.Double\n");
    sb.append("uses java.math.BigInteger\n");
    sb.append("uses java.math.BigDecimal\n");
    sb.append("uses gw.spec.core.expressions.arithmetic.ArithmeticTestBase\n");
    sb.append("\n");
    sb.append("class ").append(determineClassName(lhsType, operator)).append(" extends ArithmeticTestBase {\n\n");
    List<? extends InterestingValue> interestingLHSValues = operator.getInterestingValuesByTypeMap().get(lhsType);
    for (Class rhsType : _allTypes) {
      sb.append("  function test").append(functionNameForClass(lhsType)).append(functionNameForClass(rhsType)).append(operator.getName()).append("() {\n");
      List<? extends InterestingValue> interestingRHSValues = operator.getInterestingValuesByTypeMap().get(rhsType);
      for (InterestingValue lhsValue : interestingLHSValues) {
        for (InterestingValue rhsValue : interestingRHSValues) {
          try {
            String resultString = computeResultString(lhsValue, rhsValue, operator);
            sb.append("    assertEquals(")
                  .append(resultString)
                  .append(", ")
                  .append(lhsValue.getArgumentString())
                  .append(" ").append(operator.getToken()).append(" ")
                  .append(rhsValue.getArgumentString())
                  .append(")\n");
          } catch (NumberFormatException e) {
            sb.append("    // Skipped test assertEquals(something, ").append(lhsValue.getArgumentString())
                  .append(" - ")
                  .append(rhsValue.getArgumentString()) 
                  .append(")\n");
          } catch (ArithmeticException e) {
            sb.append("    assertThrowsArithmeticException(")
                .append("\"").append(e.getMessage()).append("\"")
                .append(", ")
                .append("\\ -> ")
                .append(lhsValue.getArgumentString())
                .append(" ").append(operator.getToken()).append(" ")
                .append(rhsValue.getArgumentString())
                .append(")\n");
          } catch (Exception e) {
            System.out.println("Failed on " + lhsValue.getArgumentString() + " " + operator.getToken() + " " + rhsValue.getArgumentString());
          }
        }
        sb.append("\n");
      }
      sb.append("    assertEquals(")
              .append(determineResultClass(interestingLHSValues.get(0), interestingRHSValues.get(0)).getSimpleName())
              .append(", statictypeof(")
              .append(interestingLHSValues.get(0).getArgumentString())
              .append(" ").append(operator.getToken()).append(" ")
              .append(interestingRHSValues.get(0).getArgumentString())
              .append("))\n");
      sb.append("  }\n\n");
    }
    sb.append("}\n\n");
    return sb;
  }

  private static String functionNameForClass(Class cls) {
    if (cls.isPrimitive()) {
      return "P" + Character.toUpperCase(cls.getSimpleName().charAt(0)) + cls.getSimpleName().substring(1);
    } else {
      return cls.getSimpleName();
    }
  }

  private static String computeResultString(InterestingValue lhs, InterestingValue rhs, Operator operator) {
    Class resultingClass = determineResultClass(lhs, rhs);
    InterestingValue lhsValue = widen(lhs, resultingClass);
    InterestingValue rhsValue = widen(rhs, resultingClass);

    return operator.apply(lhsValue, rhsValue).getArgumentString();
  }

  private static InterestingValue widen(InterestingValue v, Class resultingClass) {
    if (resultingClass == BigDecimal.class) {
      return new BigDecimalInterestingValue(v.getValue().toString());
    } else if (resultingClass == BigInteger.class) {
      return new BigIntegerInterestingValue(v.getValue().toString());
    } else if (resultingClass == Double.class) {
      return new DoubleInterestingValue(v.getValue().doubleValue(), Double.class);
    } else if (resultingClass == double.class) {
      return new DoubleInterestingValue(v.getValue().doubleValue(), double.class);
    } else if (resultingClass == Float.class) {
      return new FloatInterestingValue(v.getValue().floatValue(), Float.class);
    } else if (resultingClass == float.class) {
      return new FloatInterestingValue(v.getValue().floatValue(), float.class);
    } else if (resultingClass == Long.class) {
      return new LongInterestingValue(v.getValue().longValue(), Long.class);
    } else if (resultingClass == long.class) {
      return new LongInterestingValue(v.getValue().longValue(), long.class);
    } else if (resultingClass == Integer.class) {
      return new IntInterestingValue(v.getValue().intValue(), Integer.class);
    } else if (resultingClass == int.class) {
      return new IntInterestingValue(v.getValue().intValue(), int.class);
    } else {
      throw new IllegalArgumentException(resultingClass.getName());
    }
  }

  private static Class determineResultClass(InterestingValue lhs, InterestingValue rhs) {
    Class lhsType = lhs.getType();
    Class rhsType = rhs.getType();
    if( lhsType == BigDecimal.class || rhsType == BigDecimal.class ) {
      return BigDecimal.class;
    }

    if( lhsType == BigInteger.class)
    {
      if (rhsType == Double.class || rhsType == double.class || rhsType == Float.class || rhsType == float.class) {
        return BigDecimal.class;
      } else {
        return BigInteger.class;
      }
    }
    if( rhsType == BigInteger.class )
    {
      if (lhsType == Double.class || lhsType == double.class || lhsType == Float.class || lhsType == float.class) {
        return BigDecimal.class;
      } else {
        return BigInteger.class;
      }
    }

    Class retType;
    if( lhsType == Double.class || lhsType == double.class )
    {
      retType = lhsType;
    }
    else if( rhsType == Double.class || rhsType == double.class )
    {
      retType = rhsType;
    }

    else if( lhsType == Float.class || lhsType == float.class )
    {
      retType = lhsType;
    }
    else if( rhsType == Float.class || rhsType == float.class )
    {
      retType = rhsType;
    }

    else if( lhsType == Long.class || lhsType == long.class )
    {
      retType = lhsType;
    }
    else if( rhsType == Long.class || rhsType == long.class )
    {
      retType = rhsType;
    }

    else if( lhsType == Integer.class || lhsType == int.class )
    {
      retType = lhsType;
    }
    else if( rhsType == Integer.class || rhsType == int.class )
    {
      retType = rhsType;
    }
    else
    {
      retType = int.class;
    }

    if( retType.isPrimitive() && (!lhsType.isPrimitive() || !rhsType.isPrimitive()) )
    {
      retType = IRArgConverter.getBoxedType( retType ).getJavaClass();
    }

    return retType;
  }

  private static final List<Class> _allTypes = ArithmeticTestGenerator.<Class>list(byte.class, Byte.class, short.class, Short.class, char.class, Character.class, int.class, Integer.class, long.class, Long.class, float.class, Float.class, double.class, Double.class, BigInteger.class, BigDecimal.class);

  private static <T> List<T> list(T... elements) {
    return Arrays.asList(elements);
  }

  private static interface InterestingValue {
    Number getValue();
    String getArgumentString();
    Class getType();
    InterestingValue subtract(InterestingValue rhs);
    InterestingValue add(InterestingValue rhs);
    InterestingValue multiply(InterestingValue rhs);
    InterestingValue divideBy(InterestingValue rhs);
    InterestingValue remainder(InterestingValue rhs);
  }

  private static class ByteInterestingValue implements InterestingValue {
    private Byte _v;
    private Class _type;

    private ByteInterestingValue(byte v, Class type) {
      _v = v;
      _type = type;
    }

    @Override
    public Number getValue() {
      return _v;
    }

    protected String getValueString() {
      return _v.toString();
    }

    @Override
    public String getArgumentString() {
      if (_type == byte.class) {
        return "p_byte(" + getValueString() + ")";
      } else {
        return "b_byte(" + getValueString() + ")";
      }
    }

    @Override
    public Class getType() {
      return _type;
    }

    @Override
    public InterestingValue subtract(InterestingValue rhs) {
      throw new UnsupportedOperationException();
    }

    @Override
    public InterestingValue add(InterestingValue rhs) {
      throw new UnsupportedOperationException();
    }

    @Override
    public InterestingValue multiply(InterestingValue rhs) {
      throw new UnsupportedOperationException();
    }

    @Override
    public InterestingValue divideBy(InterestingValue rhs) {
      throw new UnsupportedOperationException();
    }

    @Override
    public InterestingValue remainder(InterestingValue rhs) {
      throw new UnsupportedOperationException();
    }
  }

  private static class ByteMaxValue extends ByteInterestingValue {
    private ByteMaxValue(Class type) {
      super(Byte.MAX_VALUE, type);
    }

    @Override
    public String getValueString() {
      return "Byte.MAX_VALUE";
    }
  }

  private static class ByteMinValue extends ByteInterestingValue {
    private ByteMinValue(Class type) {
      super(Byte.MIN_VALUE, type);
    }

    @Override
    public String getValueString() {
      return "Byte.MIN_VALUE";
    }
  }

  private static class CharInterestingValue implements InterestingValue {
    private Integer _v;
    private Class _type;

    private CharInterestingValue(char v, Class type) {
      _v = (int) v;
      _type = type;
    }

    @Override
    public Number getValue() {
      return _v;
    }

    protected String getValueString() {
      return _v.toString();
    }

    @Override
    public String getArgumentString() {
      if (_type == char.class) {
        return "p_char(" + getValueString() + ")";
      } else {
        return "b_char(" + getValueString() + ")";
      }
    }

    @Override
    public Class getType() {
      return _type;
    }

    @Override
    public InterestingValue subtract(InterestingValue rhs) {
      throw new UnsupportedOperationException();
    }

    @Override
    public InterestingValue add(InterestingValue rhs) {
      throw new UnsupportedOperationException();
    }

    @Override
    public InterestingValue multiply(InterestingValue rhs) {
      throw new UnsupportedOperationException();
    }

    @Override
    public InterestingValue divideBy(InterestingValue rhs) {
      throw new UnsupportedOperationException();
    }

    @Override
    public InterestingValue remainder(InterestingValue rhs) {
      throw new UnsupportedOperationException();
    }
  }

  private static class CharMaxValue extends CharInterestingValue {
    private CharMaxValue(Class type) {
      super(Character.MAX_VALUE, type);
    }

    @Override
    public String getValueString() {
      return "Character.MAX_VALUE";
    }
  }

  private static class ShortInterestingValue implements InterestingValue {
    private Short _v;
    private Class _type;

    private ShortInterestingValue(short v, Class type) {
      _v = v;
      _type = type;
    }

    @Override
    public Number getValue() {
      return _v;
    }

    protected String getValueString() {
      return _v.toString();
    }

    @Override
    public String getArgumentString() {
      if (_type == short.class) {
        return "p_short(" + getValueString() + ")";
      } else {
        return "b_short(" + getValueString() + ")";
      }
    }

    @Override
    public Class getType() {
      return _type;
    }

    @Override
    public InterestingValue subtract(InterestingValue rhs) {
      int value = _v.shortValue() - ((Short) rhs.getValue()).shortValue();
      return new IntInterestingValue(value, int.class);
    }

    @Override
    public InterestingValue add(InterestingValue rhs) {
      int value = _v.shortValue() + ((Short) rhs.getValue()).shortValue();
      return new IntInterestingValue(value, int.class);
    }

    @Override
    public InterestingValue multiply(InterestingValue rhs) {
      int value = _v.shortValue() * ((Short) rhs.getValue()).shortValue();
      return new IntInterestingValue(value, int.class);
    }

    @Override
    public InterestingValue divideBy(InterestingValue rhs) {
      int value = _v.shortValue() / ((Short) rhs.getValue()).shortValue();
      return new IntInterestingValue(value, int.class);
    }

    @Override
    public InterestingValue remainder(InterestingValue rhs) {
      int value = _v.shortValue() % ((Short) rhs.getValue()).shortValue();
      return new IntInterestingValue(value, int.class);
    }
  }

  private static class ShortMaxValue extends ShortInterestingValue {
    private ShortMaxValue(Class type) {
      super(Short.MAX_VALUE, type);
    }

    @Override
    public String getValueString() {
      return "Short.MAX_VALUE";
    }
  }

  private static class ShortMinValue extends ShortInterestingValue {
    private ShortMinValue(Class type) {
      super(Short.MIN_VALUE, type);
    }

    @Override
    public String getValueString() {
      return "Short.MIN_VALUE";
    }
  }

  private static class IntInterestingValue implements InterestingValue {
    private Integer _v;
    private Class _type;

    private IntInterestingValue(int v, Class type) {
      _v = v;
      _type = type;
    }

    @Override
    public Number getValue() {
      return _v;
    }

    protected String getValueString() {
      return _v.toString();
    }

    @Override
    public String getArgumentString() {
      if (_type == int.class) {
        return "p_int(" + getValueString() + ")";
      } else {
        return "b_int(" + getValueString() + ")";
      }
    }

    @Override
    public Class getType() {
      return _type;
    }

    @Override
    public InterestingValue subtract(InterestingValue rhs) {
      int value = _v.intValue() - ((Integer) rhs.getValue()).intValue();
      return new IntInterestingValue(value, _type);
    }

    @Override
    public InterestingValue add(InterestingValue rhs) {
      int value = _v.intValue() + ((Integer) rhs.getValue()).intValue();
      return new IntInterestingValue(value, _type);
    }

    @Override
    public InterestingValue multiply(InterestingValue rhs) {
      int value = _v.intValue() * ((Integer) rhs.getValue()).intValue();
      return new IntInterestingValue(value, _type);
    }

    @Override
    public InterestingValue divideBy(InterestingValue rhs) {
      int value = _v.intValue() / ((Integer) rhs.getValue()).intValue();
      return new IntInterestingValue(value, _type);
    }

    @Override
    public InterestingValue remainder(InterestingValue rhs) {
      int value = _v.intValue() % ((Integer) rhs.getValue()).intValue();
      return new IntInterestingValue(value, _type);
    }
  }

  private static class IntMaxValue extends IntInterestingValue {
    private IntMaxValue(Class type) {
      super(Integer.MAX_VALUE, type);
    }

    @Override
    public String getValueString() {
      return "Integer.MAX_VALUE";
    }
  }

  private static class IntMinValue extends IntInterestingValue {
    private IntMinValue(Class type) {
      super(Integer.MIN_VALUE, type);
    }

    @Override
    public String getValueString() {
      return "Integer.MIN_VALUE";
    }
  }

  private static class LongInterestingValue implements InterestingValue {
    private Long _v;
    private Class _type;

    private LongInterestingValue(long v, Class type) {
      _v = v;
      _type = type;
    }

    @Override
    public Number getValue() {
      return _v;
    }

    protected String getValueString() {
      return _v.toString();
    }

    @Override
    public String getArgumentString() {
      if (_type == long.class) {
        return "p_long(" + getValueString() + ")";
      } else {
        return "b_long(" + getValueString() + ")";
      }
    }

    @Override
    public Class getType() {
      return _type;
    }

    @Override
    public InterestingValue subtract(InterestingValue rhs) {
      long value = _v.longValue() - ((Long) rhs.getValue()).longValue();
      return new LongInterestingValue(value, _type);
    }

    @Override
    public InterestingValue add(InterestingValue rhs) {
      long value = _v.longValue() + ((Long) rhs.getValue()).longValue();
      return new LongInterestingValue(value, _type);
    }

    @Override
    public InterestingValue multiply(InterestingValue rhs) {
      long value = _v.longValue() * ((Long) rhs.getValue()).longValue();
      return new LongInterestingValue(value, _type);
    }

    @Override
    public InterestingValue divideBy(InterestingValue rhs) {
      long value = _v.longValue() / ((Long) rhs.getValue()).longValue();
      return new LongInterestingValue(value, _type);
    }

    @Override
    public InterestingValue remainder(InterestingValue rhs) {
      long value = _v.longValue() % ((Long) rhs.getValue()).longValue();
      return new LongInterestingValue(value, _type);
    }
  }

  private static class LongMaxValue extends LongInterestingValue {
    private LongMaxValue(Class type) {
      super(Long.MAX_VALUE, type);
    }

    @Override
    public String getValueString() {
      return "Long.MAX_VALUE";
    }
  }

  private static class LongMinValue extends LongInterestingValue {
    private LongMinValue(Class type) {
      super(Long.MIN_VALUE, type);
    }

    @Override
    public String getValueString() {
      return "Long.MIN_VALUE";
    }
  }

  private static class FloatInterestingValue implements InterestingValue {
    private Float _v;
    private Class _type;

    private FloatInterestingValue(float v, Class type) {
      _v = v;
      _type = type;
    }

    @Override
    public Number getValue() {
      return _v;
    }

    protected String getValueString() {
      String value = _v.toString();
      if (value.contains("E")) {
        return "Float.parseFloat(\"" + value + "\")";
      } else {
        return value;
      }
    }

    @Override
    public String getArgumentString() {
      if (_type == float.class) {
        return "p_float(" + getValueString() + ")";
      } else {
        return "b_float(" + getValueString() + ")";
      }
    }

    @Override
    public Class getType() {
      return _type;
    }

    @Override
    public InterestingValue subtract(InterestingValue rhs) {
      float value = _v.floatValue() - ((Float) rhs.getValue()).floatValue();
      return createWrapperForValue(value);
    }

    @Override
    public InterestingValue add(InterestingValue rhs) {
      float value = _v.floatValue() + ((Float) rhs.getValue()).floatValue();
      return createWrapperForValue(value);
    }

    @Override
    public InterestingValue multiply(InterestingValue rhs) {
      float value = _v.floatValue() * ((Float) rhs.getValue()).floatValue();
      return createWrapperForValue(value);
    }

    @Override
    public InterestingValue divideBy(InterestingValue rhs) {
      float value = _v.floatValue() / ((Float) rhs.getValue()).floatValue();
      return createWrapperForValue(value);
    }

    @Override
    public InterestingValue remainder(InterestingValue rhs) {
      float value = _v.floatValue() % ((Float) rhs.getValue()).floatValue();
      return createWrapperForValue(value);
    }

    private InterestingValue createWrapperForValue(float value) {
      if (value == Float.POSITIVE_INFINITY) {
        return new FloatPositiveInfinity(_type);
      } else if (value == Float.NEGATIVE_INFINITY) {
        return new FloatNegativeInfinity(_type);
      } else if (Float.isNaN(value)) {
        return new FloatNaN(_type);
      } else {
        return new FloatInterestingValue(value, _type);
      }
    }
  }

  private static class FloatMaxValue extends FloatInterestingValue {
    private FloatMaxValue(Class type) {
      super(Float.MAX_VALUE, type);
    }

    @Override
    public String getValueString() {
      return "Float.MAX_VALUE";
    }
  }

  private static class FloatMinValue extends FloatInterestingValue {
    private FloatMinValue(Class type) {
      super(Float.MIN_VALUE, type);
    }

    @Override
    public String getValueString() {
      return "Float.MIN_VALUE";
    }
  }

  private static class FloatPositiveInfinity extends FloatInterestingValue {
    private FloatPositiveInfinity(Class type) {
      super(Float.POSITIVE_INFINITY, type);
    }

    @Override
    public String getValueString() {
      return "Float.POSITIVE_INFINITY";
    }
  }

  private static class FloatNegativeInfinity extends FloatInterestingValue {
    private FloatNegativeInfinity(Class type) {
      super(Float.NEGATIVE_INFINITY, type);
    }

    @Override
    public String getValueString() {
      return "Float.NEGATIVE_INFINITY";
    }
  }

  private static class FloatNaN extends FloatInterestingValue {
    private FloatNaN(Class type) {
      super(Float.NaN, type);
    }

    @Override
    public String getValueString() {
      return "Float.NaN_";
    }
  }

  private static class DoubleInterestingValue implements InterestingValue {
    private Double _v;
    private Class _type;

    private DoubleInterestingValue(double v, Class type) {
      _v = v;
      _type = type;
    }

    @Override
    public Number getValue() {
      return _v;
    }

    protected String getValueString() {
      String value = _v.toString();
      if (value.contains("E")) {
        return "Double.parseDouble(\"" + value + "\")";
      } else {
        return value;
      }
    }

    @Override
    public String getArgumentString() {
      if (_type == double.class) {
        return "p_double(" + getValueString() + ")";
      } else {
        return "b_double(" + getValueString() + ")";
      }
    }

    @Override
    public Class getType() {
      return _type;
    }

    @Override
    public InterestingValue subtract(InterestingValue rhs) {
      double value = _v.doubleValue() - ((Double) rhs.getValue()).doubleValue();
      return createValueWrapper(value);
    }

    @Override
    public InterestingValue add(InterestingValue rhs) {
      double value = _v.doubleValue() + ((Double) rhs.getValue()).doubleValue();
      return createValueWrapper(value);
    }

    @Override
    public InterestingValue multiply(InterestingValue rhs) {
      double value = _v.doubleValue() * ((Double) rhs.getValue()).doubleValue();
      return createValueWrapper(value);
    }

    @Override
    public InterestingValue divideBy(InterestingValue rhs) {
      double value = _v.doubleValue() / ((Double) rhs.getValue()).doubleValue();
      return createValueWrapper(value);
    }

    @Override
    public InterestingValue remainder(InterestingValue rhs) {
      double value = _v.doubleValue() % ((Double) rhs.getValue()).doubleValue();
      return createValueWrapper(value);
    }

    private InterestingValue createValueWrapper(double value) {
      if (value == Double.POSITIVE_INFINITY) {
        return new DoublePositiveInfinity(_type);
      } else if (value == Double.NEGATIVE_INFINITY) {
        return new DoubleNegativeInfinity(_type);
      } else if (Double.isNaN(value)) {
        return new DoubleNaN(_type);
      } else if (value == Double.MAX_VALUE) {
        return new DoubleMaxValue(_type);
      } else if (value == Double.MIN_VALUE) {
        return new DoubleMinValue(_type);
      } else {
        return new DoubleInterestingValue(value, _type);
      }
    }
  }

  private static class DoubleMaxValue extends DoubleInterestingValue {
    private DoubleMaxValue(Class type) {
      super(Double.MAX_VALUE, type);
    }

    @Override
    public String getValueString() {
      return "Double.MAX_VALUE";
    }
  }

  private static class DoubleMinValue extends DoubleInterestingValue {
    private DoubleMinValue(Class type) {
      super(Double.MIN_VALUE, type);
    }

    @Override
    public String getValueString() {
      return "Double.MIN_VALUE";
    }
  }

  private static class DoublePositiveInfinity extends DoubleInterestingValue {
    private DoublePositiveInfinity(Class type) {
      super(Double.POSITIVE_INFINITY, type);
    }

    @Override
    public String getValueString() {
      return "Double.POSITIVE_INFINITY";
    }
  }

  private static class DoubleNegativeInfinity extends DoubleInterestingValue {
    private DoubleNegativeInfinity(Class type) {
      super(Double.NEGATIVE_INFINITY, type);
    }

    @Override
    public String getValueString() {
      return "Double.NEGATIVE_INFINITY";
    }
  }

  private static class DoubleNaN extends DoubleInterestingValue {
    private DoubleNaN(Class type) {
      super(Double.NaN, type);
    }

    @Override
    public String getValueString() {
      return "Double.NaN_";
    }
  }

  private static class BigIntegerInterestingValue implements InterestingValue {
    private BigInteger _v;

    private BigIntegerInterestingValue(String v) {
      _v = new BigInteger(v);
    }

    @Override
    public Number getValue() {
      return _v;
    }

    protected String getValueString() {
      return _v.toString();
    }

    @Override
    public String getArgumentString() {
      return "big_int(\"" + getValueString() + "\")";
    }

    @Override
    public Class getType() {
      return BigInteger.class;
    }

    @Override
    public InterestingValue subtract(InterestingValue rhs) {
      BigInteger value = _v.subtract(((BigInteger) rhs.getValue()));
      return new BigIntegerInterestingValue(value.toString());
    }

    @Override
    public InterestingValue add(InterestingValue rhs) {
      BigInteger value = _v.add(((BigInteger) rhs.getValue()));
      return new BigIntegerInterestingValue(value.toString());
    }

    @Override
    public InterestingValue multiply(InterestingValue rhs) {
      BigInteger value = _v.multiply(((BigInteger) rhs.getValue()));
      return new BigIntegerInterestingValue(value.toString());
    }

    @Override
    public InterestingValue divideBy(InterestingValue rhs) {
      BigInteger value = _v.divide(((BigInteger) rhs.getValue()));
      return new BigIntegerInterestingValue(value.toString());
    }

    @Override
    public InterestingValue remainder(InterestingValue rhs) {
      BigInteger value = _v.mod(((BigInteger) rhs.getValue()));
      return new BigIntegerInterestingValue(value.toString());
    }
  }

  private static class BigDecimalInterestingValue implements InterestingValue {
    private BigDecimal _v;

    private BigDecimalInterestingValue(String v) {
      _v = new BigDecimal(v);
    }

    @Override
    public Number getValue() {
      return _v;
    }

    protected String getValueString() {
      return _v.toString();
    }

    @Override
    public String getArgumentString() {
      return "big_decimal(\"" + getValueString() + "\")";
    }

    @Override
    public Class getType() {
      return BigDecimal.class;
    }

    @Override
    public InterestingValue subtract(InterestingValue rhs) {
      BigDecimal value = _v.subtract(((BigDecimal) rhs.getValue()));
      return new BigDecimalInterestingValue(value.toString());
    }

    @Override
    public InterestingValue add(InterestingValue rhs) {
      BigDecimal value = _v.add(((BigDecimal) rhs.getValue()));
      return new BigDecimalInterestingValue(value.toString());
    }

    @Override
    public InterestingValue multiply(InterestingValue rhs) {
      BigDecimal value = _v.multiply(((BigDecimal) rhs.getValue()));
      return new BigDecimalInterestingValue(value.toString());
    }

    @Override
    public InterestingValue divideBy(InterestingValue rhs) {
      BigDecimal value = _v.divide(((BigDecimal) rhs.getValue()), MathContext.DECIMAL128);
      return new BigDecimalInterestingValue(value.toString());
    }

    @Override
    public InterestingValue remainder(InterestingValue rhs) {
      BigDecimal value = _v.remainder(((BigDecimal) rhs.getValue()), MathContext.DECIMAL128).abs();
      return new BigDecimalInterestingValue(value.toString());
    }
  }

  private static interface Operator {
    String getToken();
    String getName();
    InterestingValue apply(InterestingValue lhs, InterestingValue rhs);
    Map<Class, List<? extends InterestingValue>> getInterestingValuesByTypeMap();
  }

  public static class Subtraction implements Operator {
    @Override
    public String getToken() {
      return "-";
    }

    @Override
    public String getName() {
      return "Subtraction";
    }

    @Override
    public InterestingValue apply(InterestingValue lhs, InterestingValue rhs) {
      return lhs.subtract(rhs);
    }

    @Override
    public Map<Class, List<? extends InterestingValue>> getInterestingValuesByTypeMap() {
      return _interestingValuesByType;
    }

    private static final Map<Class, List<? extends InterestingValue>> _interestingValuesByType;
    static {
      _interestingValuesByType = new HashMap<Class, List<? extends InterestingValue>>();
      _interestingValuesByType.put(byte.class, list(new ByteInterestingValue((byte) 0, byte.class),
                                                    new ByteInterestingValue((byte) 23, byte.class),
                                                    new ByteInterestingValue((byte) -32, byte.class),
                                                    new ByteMaxValue(byte.class),
                                                    new ByteMinValue(byte.class)));
      _interestingValuesByType.put(Byte.class, list(new ByteInterestingValue((byte) 0, Byte.class),
                                                    new ByteInterestingValue((byte) 23, Byte.class),
                                                    new ByteInterestingValue((byte) -32, Byte.class),
                                                    new ByteMaxValue(Byte.class),
                                                    new ByteMinValue(Byte.class)));
      _interestingValuesByType.put(short.class, list(new ShortInterestingValue((short) 0, short.class),
              new ShortInterestingValue((short) 23, short.class),
              new ShortInterestingValue((short) -32, short.class),
              new ShortMaxValue(short.class),
              new ShortMinValue(short.class)));
      _interestingValuesByType.put(Short.class, list(new ShortInterestingValue((short) 0, Short.class),
              new ShortInterestingValue((short) 23, Short.class),
              new ShortInterestingValue((short) -32, Short.class),
              new ShortMaxValue(Short.class),
              new ShortMinValue(Short.class)));
      _interestingValuesByType.put(char.class, list(new CharInterestingValue((char) 0, char.class),
              new CharInterestingValue((char) 23, char.class),
              new CharMaxValue(char.class)));
      _interestingValuesByType.put(Character.class, list(new CharInterestingValue((char) 0, Character.class),
              new CharInterestingValue((char) 23, Character.class),
              new CharMaxValue(Character.class)));
      _interestingValuesByType.put(int.class, list(new IntInterestingValue(0, int.class),
              new IntInterestingValue(23, int.class),
              new IntInterestingValue(-32, int.class),
              new IntMaxValue(int.class),
              new IntMinValue(int.class)));
      _interestingValuesByType.put(Integer.class, list(new IntInterestingValue(0, Integer.class),
              new IntInterestingValue(23, Integer.class),
              new IntInterestingValue(-32, Integer.class),
              new IntMaxValue(Integer.class),
              new IntMinValue(Integer.class)));
      _interestingValuesByType.put(long.class, list(new LongInterestingValue((long) 0, long.class),
              new LongInterestingValue((long) 23, long.class),
              new LongInterestingValue((long) -32, long.class),
              new LongMaxValue(long.class),
              new LongMinValue(long.class)));
      _interestingValuesByType.put(Long.class, list(new LongInterestingValue((long) 0, Long.class),
              new LongInterestingValue((long) 23, Long.class),
              new LongInterestingValue((long) -32, Long.class),
              new LongMaxValue(Long.class),
              new LongMinValue(Long.class)));
      _interestingValuesByType.put(float.class, list(
              new FloatInterestingValue((float) 0.0, float.class),
              new FloatInterestingValue((float) 23.0, float.class),
              new FloatInterestingValue((float) 23.123, float.class),
              new FloatInterestingValue((float) -32.0, float.class),
              new FloatInterestingValue((float) -32.456, float.class),
              new FloatPositiveInfinity(float.class),
              new FloatNegativeInfinity(float.class),
              new FloatNaN(float.class),
              new FloatMaxValue(float.class),
              new FloatMinValue(float.class)));
      _interestingValuesByType.put(Float.class, list(
              new FloatInterestingValue((float) 0.0, Float.class),
              new FloatInterestingValue((float) 23.0, Float.class),
              new FloatInterestingValue((float) 23.123, Float.class),
              new FloatInterestingValue((float) -32.0, Float.class),
              new FloatInterestingValue((float) -32.456, Float.class),
              new FloatPositiveInfinity(Float.class),
              new FloatNegativeInfinity(Float.class),
              new FloatNaN(Float.class),
              new FloatMaxValue(Float.class),
              new FloatMinValue(Float.class)));
      _interestingValuesByType.put(double.class, list(
              new DoubleInterestingValue(0.0, double.class),
              new DoubleInterestingValue(23.0, double.class),
              new DoubleInterestingValue(23.123, double.class),
              new DoubleInterestingValue(-32.0, double.class),
              new DoubleInterestingValue(-32.456, double.class),
              new DoublePositiveInfinity(double.class),
              new DoubleNegativeInfinity(double.class),
              new DoubleNaN(double.class),
              new DoubleMaxValue(double.class),
              new DoubleMinValue(double.class)));
      _interestingValuesByType.put(Double.class, list(
              new DoubleInterestingValue(0.0, Double.class),
              new DoubleInterestingValue(23.0, Double.class),
              new DoubleInterestingValue(23.123, Double.class),
              new DoubleInterestingValue(-32.0, Double.class),
              new DoubleInterestingValue(-32.456, Double.class),
              new DoublePositiveInfinity(Double.class),
              new DoubleNegativeInfinity(Double.class),
              new DoubleNaN(Double.class),
              new DoubleMaxValue(Double.class),
              new DoubleMinValue(Double.class)));
      _interestingValuesByType.put(BigInteger.class, list(
              new BigIntegerInterestingValue("0"),
              new BigIntegerInterestingValue("23"),
              new BigIntegerInterestingValue("-32"),
              new BigIntegerInterestingValue("123456789012345678901234567890"),
              new BigIntegerInterestingValue("-123456789012345678901234567890")));
      _interestingValuesByType.put(BigDecimal.class, list(
              new BigDecimalInterestingValue("0"),
              new BigDecimalInterestingValue("23"),
              new BigDecimalInterestingValue("23.123"),
              new BigDecimalInterestingValue("-32"),
              new BigDecimalInterestingValue("-32.456"),
              new BigDecimalInterestingValue("123456789012345678901234567890.123456789"),
              new BigDecimalInterestingValue("-123456789012345678901234567890.123456789")));
    }
  }

  public static class Addition implements Operator {
    @Override
    public String getToken() {
      return "+";
    }

    @Override
    public String getName() {
      return "Addition";
    }

    @Override
    public InterestingValue apply(InterestingValue lhs, InterestingValue rhs) {
      return lhs.add(rhs);
    }

    @Override
    public Map<Class, List<? extends InterestingValue>> getInterestingValuesByTypeMap() {
      return _interestingValuesByType;
    }

    private static final Map<Class, List<? extends InterestingValue>> _interestingValuesByType;
    static {
      _interestingValuesByType = new HashMap<Class, List<? extends InterestingValue>>();
      _interestingValuesByType.put(byte.class, list(new ByteInterestingValue((byte) 0, byte.class),
                                                    new ByteInterestingValue((byte) 23, byte.class),
                                                    new ByteInterestingValue((byte) -32, byte.class),
                                                    new ByteMaxValue(byte.class),
                                                    new ByteMinValue(byte.class)));
      _interestingValuesByType.put(Byte.class, list(new ByteInterestingValue((byte) 0, Byte.class),
                                                    new ByteInterestingValue((byte) 23, Byte.class),
                                                    new ByteInterestingValue((byte) -32, Byte.class),
                                                    new ByteMaxValue(Byte.class),
                                                    new ByteMinValue(Byte.class)));
      _interestingValuesByType.put(short.class, list(new ShortInterestingValue((short) 0, short.class),
              new ShortInterestingValue((short) 23, short.class),
              new ShortInterestingValue((short) -32, short.class),
              new ShortMaxValue(short.class),
              new ShortMinValue(short.class)));
      _interestingValuesByType.put(Short.class, list(new ShortInterestingValue((short) 0, Short.class),
              new ShortInterestingValue((short) 23, Short.class),
              new ShortInterestingValue((short) -32, Short.class),
              new ShortMaxValue(Short.class),
              new ShortMinValue(Short.class)));
      _interestingValuesByType.put(char.class, list(new CharInterestingValue((char) 0, char.class),
              new CharInterestingValue((char) 23, char.class),
              new CharMaxValue(char.class)));
      _interestingValuesByType.put(Character.class, list(new CharInterestingValue((char) 0, Character.class),
              new CharInterestingValue((char) 23, Character.class),
              new CharMaxValue(Character.class)));
      _interestingValuesByType.put(int.class, list(new IntInterestingValue(0, int.class),
              new IntInterestingValue(23, int.class),
              new IntInterestingValue(-32, int.class),
              new IntMaxValue(int.class),
              new IntMinValue(int.class)));
      _interestingValuesByType.put(Integer.class, list(new IntInterestingValue(0, Integer.class),
              new IntInterestingValue(23, Integer.class),
              new IntInterestingValue(-32, Integer.class),
              new IntMaxValue(Integer.class),
              new IntMinValue(Integer.class)));
      _interestingValuesByType.put(long.class, list(new LongInterestingValue((long) 0, long.class),
              new LongInterestingValue((long) 23, long.class),
              new LongInterestingValue((long) -32, long.class),
              new LongMaxValue(long.class),
              new LongMinValue(long.class)));
      _interestingValuesByType.put(Long.class, list(new LongInterestingValue((long) 0, Long.class),
              new LongInterestingValue((long) 23, Long.class),
              new LongInterestingValue((long) -32, Long.class),
              new LongMaxValue(Long.class),
              new LongMinValue(Long.class)));
      _interestingValuesByType.put(float.class, list(
              new FloatInterestingValue((float) 0.0, float.class),
              new FloatInterestingValue((float) 23.0, float.class),
              new FloatInterestingValue((float) 23.123, float.class),
              new FloatInterestingValue((float) -32.0, float.class),
              new FloatInterestingValue((float) -32.456, float.class),
              new FloatPositiveInfinity(float.class),
              new FloatNegativeInfinity(float.class),
              new FloatNaN(float.class),
              new FloatMaxValue(float.class),
              new FloatMinValue(float.class)));
      _interestingValuesByType.put(Float.class, list(
              new FloatInterestingValue((float) 0.0, Float.class),
              new FloatInterestingValue((float) 23.0, Float.class),
              new FloatInterestingValue((float) 23.123, Float.class),
              new FloatInterestingValue((float) -32.0, Float.class),
              new FloatInterestingValue((float) -32.456, Float.class),
              new FloatPositiveInfinity(Float.class),
              new FloatNegativeInfinity(Float.class),
              new FloatNaN(Float.class),
              new FloatMaxValue(Float.class),
              new FloatMinValue(Float.class)));
      _interestingValuesByType.put(double.class, list(
              new DoubleInterestingValue(0.0, double.class),
              new DoubleInterestingValue(23.0, double.class),
              new DoubleInterestingValue(23.123, double.class),
              new DoubleInterestingValue(-32.0, double.class),
              new DoubleInterestingValue(-32.456, double.class),
              new DoublePositiveInfinity(double.class),
              new DoubleNegativeInfinity(double.class),
              new DoubleNaN(double.class),
              new DoubleMaxValue(double.class),
              new DoubleMinValue(double.class)));
      _interestingValuesByType.put(Double.class, list(
              new DoubleInterestingValue(0.0, Double.class),
              new DoubleInterestingValue(23.0, Double.class),
              new DoubleInterestingValue(23.123, Double.class),
              new DoubleInterestingValue(-32.0, Double.class),
              new DoubleInterestingValue(-32.456, Double.class),
              new DoublePositiveInfinity(Double.class),
              new DoubleNegativeInfinity(Double.class),
              new DoubleNaN(Double.class),
              new DoubleMaxValue(Double.class),
              new DoubleMinValue(Double.class)));
      _interestingValuesByType.put(BigInteger.class, list(
              new BigIntegerInterestingValue("0"),
              new BigIntegerInterestingValue("23"),
              new BigIntegerInterestingValue("-32"),
              new BigIntegerInterestingValue("123456789012345678901234567890"),
              new BigIntegerInterestingValue("-123456789012345678901234567890")));
      _interestingValuesByType.put(BigDecimal.class, list(
              new BigDecimalInterestingValue("0"),
              new BigDecimalInterestingValue("23"),
              new BigDecimalInterestingValue("23.123"),
              new BigDecimalInterestingValue("-32"),
              new BigDecimalInterestingValue("-32.456"),
              new BigDecimalInterestingValue("123456789012345678901234567890.123456789"),
              new BigDecimalInterestingValue("-123456789012345678901234567890.123456789")));
    }
  }

  public static class Multiplication implements Operator {
    @Override
    public String getToken() {
      return "*";
    }

    @Override
    public String getName() {
      return "Multiplication";
    }

    @Override
    public InterestingValue apply(InterestingValue lhs, InterestingValue rhs) {
      return lhs.multiply(rhs);
    }

    @Override
    public Map<Class, List<? extends InterestingValue>> getInterestingValuesByTypeMap() {
      return _interestingValuesByType;
    }

    private static final Map<Class, List<? extends InterestingValue>> _interestingValuesByType;
    static {
      _interestingValuesByType = new HashMap<Class, List<? extends InterestingValue>>();
      _interestingValuesByType.put(byte.class, list(
              new ByteInterestingValue((byte) 0, byte.class),
              new ByteInterestingValue((byte) 1, byte.class),
              new ByteInterestingValue((byte) -1, byte.class),
              new ByteInterestingValue((byte) 23, byte.class),
              new ByteInterestingValue((byte) -32, byte.class),
              new ByteMaxValue(byte.class),
              new ByteMinValue(byte.class)));
      _interestingValuesByType.put(Byte.class, list(
              new ByteInterestingValue((byte) 0, Byte.class),
              new ByteInterestingValue((byte) 1, byte.class),
              new ByteInterestingValue((byte) -1, byte.class),
              new ByteInterestingValue((byte) 23, Byte.class),
              new ByteInterestingValue((byte) -32, Byte.class),
              new ByteMaxValue(Byte.class),
              new ByteMinValue(Byte.class)));
      _interestingValuesByType.put(short.class, list(
              new ShortInterestingValue((short) 0, short.class),
              new ShortInterestingValue((short) 1, short.class),
              new ShortInterestingValue((short) -1, short.class),
              new ShortInterestingValue((short) 23, short.class),
              new ShortInterestingValue((short) -32, short.class),
              new ShortMaxValue(short.class),
              new ShortMinValue(short.class)));
      _interestingValuesByType.put(Short.class, list(
              new ShortInterestingValue((short) 0, Short.class),
              new ShortInterestingValue((short) 1, Short.class),
              new ShortInterestingValue((short) -1, Short.class),
              new ShortInterestingValue((short) 23, Short.class),
              new ShortInterestingValue((short) -32, Short.class),
              new ShortMaxValue(Short.class),
              new ShortMinValue(Short.class)));
      _interestingValuesByType.put(char.class, list(
              new CharInterestingValue((char) 0, char.class),
              new CharInterestingValue((char) 1, char.class),
              new CharInterestingValue((char) 23, char.class),
              new CharMaxValue(char.class)));
      _interestingValuesByType.put(Character.class, list(
              new CharInterestingValue((char) 0, Character.class),
              new CharInterestingValue((char) 1, Character.class),
              new CharInterestingValue((char) 23, Character.class),
              new CharMaxValue(Character.class)));
      _interestingValuesByType.put(int.class, list(
              new IntInterestingValue(0, int.class),
              new IntInterestingValue(1, int.class),
              new IntInterestingValue(-1, int.class),
              new IntInterestingValue(23, int.class),
              new IntInterestingValue(-32, int.class),
              new IntMaxValue(int.class),
              new IntMinValue(int.class)));
      _interestingValuesByType.put(Integer.class, list(
              new IntInterestingValue(0, Integer.class),
              new IntInterestingValue(1, Integer.class),
              new IntInterestingValue(-1, Integer.class),
              new IntInterestingValue(23, Integer.class),
              new IntInterestingValue(-32, Integer.class),
              new IntMaxValue(Integer.class),
              new IntMinValue(Integer.class)));
      _interestingValuesByType.put(long.class, list(
              new LongInterestingValue((long) 0, long.class),
              new LongInterestingValue((long) 1, long.class),
              new LongInterestingValue((long) -1, long.class),
              new LongInterestingValue((long) 23, long.class),
              new LongInterestingValue((long) -32, long.class),
              new LongMaxValue(long.class),
              new LongMinValue(long.class)));
      _interestingValuesByType.put(Long.class, list(
              new LongInterestingValue((long) 0, Long.class),
              new LongInterestingValue((long) 1, Long.class),
              new LongInterestingValue((long) -1, Long.class),
              new LongInterestingValue((long) 23, Long.class),
              new LongInterestingValue((long) -32, Long.class),
              new LongMaxValue(Long.class),
              new LongMinValue(Long.class)));
      _interestingValuesByType.put(float.class, list(
              new FloatInterestingValue((float) 0.0, float.class),
              new FloatInterestingValue((float) 1.0, float.class),
              new FloatInterestingValue((float) -1.0, float.class),
              new FloatInterestingValue((float) 23.0, float.class),
              new FloatInterestingValue((float) 23.123, float.class),
              new FloatInterestingValue((float) -32.0, float.class),
              new FloatInterestingValue((float) -32.456, float.class),
              new FloatPositiveInfinity(float.class),
              new FloatNegativeInfinity(float.class),
              new FloatNaN(float.class),
              new FloatMaxValue(float.class),
              new FloatMinValue(float.class)));
      _interestingValuesByType.put(Float.class, list(
              new FloatInterestingValue((float) 0.0, Float.class),
              new FloatInterestingValue((float) 1.0, Float.class),
              new FloatInterestingValue((float) -1.0, Float.class),
              new FloatInterestingValue((float) 23.0, Float.class),
              new FloatInterestingValue((float) 23.123, Float.class),
              new FloatInterestingValue((float) -32.0, Float.class),
              new FloatInterestingValue((float) -32.456, Float.class),
              new FloatPositiveInfinity(Float.class),
              new FloatNegativeInfinity(Float.class),
              new FloatNaN(Float.class),
              new FloatMaxValue(Float.class),
              new FloatMinValue(Float.class)));
      _interestingValuesByType.put(double.class, list(
              new DoubleInterestingValue(0.0, double.class),
              new DoubleInterestingValue(1.0, double.class),
              new DoubleInterestingValue(-1.0, double.class),
              new DoubleInterestingValue(23.0, double.class),
              new DoubleInterestingValue(23.123, double.class),
              new DoubleInterestingValue(-32.0, double.class),
              new DoubleInterestingValue(-32.456, double.class),
              new DoublePositiveInfinity(double.class),
              new DoubleNegativeInfinity(double.class),
              new DoubleNaN(double.class),
              new DoubleMaxValue(double.class),
              new DoubleMinValue(double.class)));
      _interestingValuesByType.put(Double.class, list(
              new DoubleInterestingValue(0.0, Double.class),
              new DoubleInterestingValue(1.0, Double.class),
              new DoubleInterestingValue(-1.0, Double.class),
              new DoubleInterestingValue(23.0, Double.class),
              new DoubleInterestingValue(23.123, Double.class),
              new DoubleInterestingValue(-32.0, Double.class),
              new DoubleInterestingValue(-32.456, Double.class),
              new DoublePositiveInfinity(Double.class),
              new DoubleNegativeInfinity(Double.class),
              new DoubleNaN(Double.class),
              new DoubleMaxValue(Double.class),
              new DoubleMinValue(Double.class)));
      _interestingValuesByType.put(BigInteger.class, list(
              new BigIntegerInterestingValue("0"),
              new BigIntegerInterestingValue("1"),
              new BigIntegerInterestingValue("-1"),
              new BigIntegerInterestingValue("23"),
              new BigIntegerInterestingValue("-32"),
              new BigIntegerInterestingValue("123456789012345678901234567890"),
              new BigIntegerInterestingValue("-123456789012345678901234567890")));
      _interestingValuesByType.put(BigDecimal.class, list(
              new BigDecimalInterestingValue("0"),
              new BigDecimalInterestingValue("1"),
              new BigDecimalInterestingValue("-1"),
              new BigDecimalInterestingValue("23"),
              new BigDecimalInterestingValue("23.123"),
              new BigDecimalInterestingValue("-32"),
              new BigDecimalInterestingValue("-32.456"),
              new BigDecimalInterestingValue("123456789012345678901234567890.123456789"),
              new BigDecimalInterestingValue("-123456789012345678901234567890.123456789")));
    }
  }

  public static class Division implements Operator {
    @Override
    public String getToken() {
      return "/";
    }

    @Override
    public String getName() {
      return "Division";
    }

    @Override
    public InterestingValue apply(InterestingValue lhs, InterestingValue rhs) {
      return lhs.divideBy(rhs);
    }

    @Override
    public Map<Class, List<? extends InterestingValue>> getInterestingValuesByTypeMap() {
      return _interestingValuesByType;
    }

    private static final Map<Class, List<? extends InterestingValue>> _interestingValuesByType;
    static {
      _interestingValuesByType = new HashMap<Class, List<? extends InterestingValue>>();
      _interestingValuesByType.put(byte.class, list(
              new ByteInterestingValue((byte) 0, byte.class),
              new ByteInterestingValue((byte) 1, byte.class),
              new ByteInterestingValue((byte) -1, byte.class),
              new ByteInterestingValue((byte) 23, byte.class),
              new ByteInterestingValue((byte) -32, byte.class),
              new ByteMaxValue(byte.class),
              new ByteMinValue(byte.class)));
      _interestingValuesByType.put(Byte.class, list(
              new ByteInterestingValue((byte) 0, Byte.class),
              new ByteInterestingValue((byte) 1, byte.class),
              new ByteInterestingValue((byte) -1, byte.class),
              new ByteInterestingValue((byte) 23, Byte.class),
              new ByteInterestingValue((byte) -32, Byte.class),
              new ByteMaxValue(Byte.class),
              new ByteMinValue(Byte.class)));
      _interestingValuesByType.put(short.class, list(
              new ShortInterestingValue((short) 0, short.class),
              new ShortInterestingValue((short) 1, short.class),
              new ShortInterestingValue((short) -1, short.class),
              new ShortInterestingValue((short) 23, short.class),
              new ShortInterestingValue((short) -32, short.class),
              new ShortMaxValue(short.class),
              new ShortMinValue(short.class)));
      _interestingValuesByType.put(Short.class, list(
              new ShortInterestingValue((short) 0, Short.class),
              new ShortInterestingValue((short) 1, Short.class),
              new ShortInterestingValue((short) -1, Short.class),
              new ShortInterestingValue((short) 23, Short.class),
              new ShortInterestingValue((short) -32, Short.class),
              new ShortMaxValue(Short.class),
              new ShortMinValue(Short.class)));
      _interestingValuesByType.put(char.class, list(
              new CharInterestingValue((char) 0, char.class),
              new CharInterestingValue((char) 1, char.class),
              new CharInterestingValue((char) 23, char.class),
              new CharMaxValue(char.class)));
      _interestingValuesByType.put(Character.class, list(
              new CharInterestingValue((char) 0, Character.class),
              new CharInterestingValue((char) 1, Character.class),
              new CharInterestingValue((char) 23, Character.class),
              new CharMaxValue(Character.class)));
      _interestingValuesByType.put(int.class, list(
              new IntInterestingValue(0, int.class),
              new IntInterestingValue(1, int.class),
              new IntInterestingValue(-1, int.class),
              new IntInterestingValue(23, int.class),
              new IntInterestingValue(-32, int.class),
              new IntMaxValue(int.class),
              new IntMinValue(int.class)));
      _interestingValuesByType.put(Integer.class, list(
              new IntInterestingValue(0, Integer.class),
              new IntInterestingValue(1, Integer.class),
              new IntInterestingValue(-1, Integer.class),
              new IntInterestingValue(23, Integer.class),
              new IntInterestingValue(-32, Integer.class),
              new IntMaxValue(Integer.class),
              new IntMinValue(Integer.class)));
      _interestingValuesByType.put(long.class, list(
              new LongInterestingValue((long) 0, long.class),
              new LongInterestingValue((long) 1, long.class),
              new LongInterestingValue((long) -1, long.class),
              new LongInterestingValue((long) 23, long.class),
              new LongInterestingValue((long) -32, long.class),
              new LongMaxValue(long.class),
              new LongMinValue(long.class)));
      _interestingValuesByType.put(Long.class, list(
              new LongInterestingValue((long) 0, Long.class),
              new LongInterestingValue((long) 1, Long.class),
              new LongInterestingValue((long) -1, Long.class),
              new LongInterestingValue((long) 23, Long.class),
              new LongInterestingValue((long) -32, Long.class),
              new LongMaxValue(Long.class),
              new LongMinValue(Long.class)));
      _interestingValuesByType.put(float.class, list(
              new FloatInterestingValue((float) 0.0, float.class),
              new FloatInterestingValue((float) 1.0, float.class),
              new FloatInterestingValue((float) -1.0, float.class),
              new FloatInterestingValue((float) 23.0, float.class),
              new FloatInterestingValue((float) 23.123, float.class),
              new FloatInterestingValue((float) -32.0, float.class),
              new FloatInterestingValue((float) -32.456, float.class),
              new FloatPositiveInfinity(float.class),
              new FloatNegativeInfinity(float.class),
              new FloatNaN(float.class),
              new FloatMaxValue(float.class),
              new FloatMinValue(float.class)));
      _interestingValuesByType.put(Float.class, list(
              new FloatInterestingValue((float) 0.0, Float.class),
              new FloatInterestingValue((float) 1.0, Float.class),
              new FloatInterestingValue((float) -1.0, Float.class),
              new FloatInterestingValue((float) 23.0, Float.class),
              new FloatInterestingValue((float) 23.123, Float.class),
              new FloatInterestingValue((float) -32.0, Float.class),
              new FloatInterestingValue((float) -32.456, Float.class),
              new FloatPositiveInfinity(Float.class),
              new FloatNegativeInfinity(Float.class),
              new FloatNaN(Float.class),
              new FloatMaxValue(Float.class),
              new FloatMinValue(Float.class)));
      _interestingValuesByType.put(double.class, list(
              new DoubleInterestingValue(0.0, double.class),
              new DoubleInterestingValue(1.0, double.class),
              new DoubleInterestingValue(-1.0, double.class),
              new DoubleInterestingValue(23.0, double.class),
              new DoubleInterestingValue(23.123, double.class),
              new DoubleInterestingValue(-32.0, double.class),
              new DoubleInterestingValue(-32.456, double.class),
              new DoublePositiveInfinity(double.class),
              new DoubleNegativeInfinity(double.class),
              new DoubleNaN(double.class),
              new DoubleMaxValue(double.class),
              new DoubleMinValue(double.class)));
      _interestingValuesByType.put(Double.class, list(
              new DoubleInterestingValue(0.0, Double.class),
              new DoubleInterestingValue(1.0, Double.class),
              new DoubleInterestingValue(-1.0, Double.class),
              new DoubleInterestingValue(23.0, Double.class),
              new DoubleInterestingValue(23.123, Double.class),
              new DoubleInterestingValue(-32.0, Double.class),
              new DoubleInterestingValue(-32.456, Double.class),
              new DoublePositiveInfinity(Double.class),
              new DoubleNegativeInfinity(Double.class),
              new DoubleNaN(Double.class),
              new DoubleMaxValue(Double.class),
              new DoubleMinValue(Double.class)));
      _interestingValuesByType.put(BigInteger.class, list(
              new BigIntegerInterestingValue("0"),
              new BigIntegerInterestingValue("1"),
              new BigIntegerInterestingValue("-1"),
              new BigIntegerInterestingValue("23"),
              new BigIntegerInterestingValue("-32"),
              new BigIntegerInterestingValue("123456789012345678901234567890"),
              new BigIntegerInterestingValue("-123456789012345678901234567890")));
      _interestingValuesByType.put(BigDecimal.class, list(
              new BigDecimalInterestingValue("0"),
              new BigDecimalInterestingValue("1"),
              new BigDecimalInterestingValue("-1"),
              new BigDecimalInterestingValue("23"),
              new BigDecimalInterestingValue("23.123"),
              new BigDecimalInterestingValue("-32"),
              new BigDecimalInterestingValue("-32.456"),
              new BigDecimalInterestingValue("123456789012345678901234567890.123456789"),
              new BigDecimalInterestingValue("-123456789012345678901234567890.123456789")));
    }
  }

  public static class Remainder implements Operator {
    @Override
    public String getToken() {
      return "%";
    }

    @Override
    public String getName() {
      return "Remainder";
    }

    @Override
    public InterestingValue apply(InterestingValue lhs, InterestingValue rhs) {
      return lhs.remainder(rhs);
    }

    @Override
    public Map<Class, List<? extends InterestingValue>> getInterestingValuesByTypeMap() {
      return _interestingValuesByType;
    }

    private static final Map<Class, List<? extends InterestingValue>> _interestingValuesByType;
    static {
      _interestingValuesByType = new HashMap<Class, List<? extends InterestingValue>>();
      _interestingValuesByType.put(byte.class, list(
              new ByteInterestingValue((byte) 0, byte.class),
              new ByteInterestingValue((byte) 1, byte.class),
              new ByteInterestingValue((byte) -1, byte.class),
              new ByteInterestingValue((byte) 23, byte.class),
              new ByteInterestingValue((byte) -32, byte.class),
              new ByteMaxValue(byte.class),
              new ByteMinValue(byte.class)));
      _interestingValuesByType.put(Byte.class, list(
              new ByteInterestingValue((byte) 0, Byte.class),
              new ByteInterestingValue((byte) 1, byte.class),
              new ByteInterestingValue((byte) -1, byte.class),
              new ByteInterestingValue((byte) 23, Byte.class),
              new ByteInterestingValue((byte) -32, Byte.class),
              new ByteMaxValue(Byte.class),
              new ByteMinValue(Byte.class)));
      _interestingValuesByType.put(short.class, list(
              new ShortInterestingValue((short) 0, short.class),
              new ShortInterestingValue((short) 1, short.class),
              new ShortInterestingValue((short) -1, short.class),
              new ShortInterestingValue((short) 23, short.class),
              new ShortInterestingValue((short) -32, short.class),
              new ShortMaxValue(short.class),
              new ShortMinValue(short.class)));
      _interestingValuesByType.put(Short.class, list(
              new ShortInterestingValue((short) 0, Short.class),
              new ShortInterestingValue((short) 1, Short.class),
              new ShortInterestingValue((short) -1, Short.class),
              new ShortInterestingValue((short) 23, Short.class),
              new ShortInterestingValue((short) -32, Short.class),
              new ShortMaxValue(Short.class),
              new ShortMinValue(Short.class)));
      _interestingValuesByType.put(char.class, list(
              new CharInterestingValue((char) 0, char.class),
              new CharInterestingValue((char) 1, char.class),
              new CharInterestingValue((char) 23, char.class),
              new CharMaxValue(char.class)));
      _interestingValuesByType.put(Character.class, list(
              new CharInterestingValue((char) 0, Character.class),
              new CharInterestingValue((char) 1, Character.class),
              new CharInterestingValue((char) 23, Character.class),
              new CharMaxValue(Character.class)));
      _interestingValuesByType.put(int.class, list(
              new IntInterestingValue(0, int.class),
              new IntInterestingValue(1, int.class),
              new IntInterestingValue(-1, int.class),
              new IntInterestingValue(23, int.class),
              new IntInterestingValue(-32, int.class),
              new IntMaxValue(int.class),
              new IntMinValue(int.class)));
      _interestingValuesByType.put(Integer.class, list(
              new IntInterestingValue(0, Integer.class),
              new IntInterestingValue(1, Integer.class),
              new IntInterestingValue(-1, Integer.class),
              new IntInterestingValue(23, Integer.class),
              new IntInterestingValue(-32, Integer.class),
              new IntMaxValue(Integer.class),
              new IntMinValue(Integer.class)));
      _interestingValuesByType.put(long.class, list(
              new LongInterestingValue((long) 0, long.class),
              new LongInterestingValue((long) 1, long.class),
              new LongInterestingValue((long) -1, long.class),
              new LongInterestingValue((long) 23, long.class),
              new LongInterestingValue((long) -32, long.class),
              new LongMaxValue(long.class),
              new LongMinValue(long.class)));
      _interestingValuesByType.put(Long.class, list(
              new LongInterestingValue((long) 0, Long.class),
              new LongInterestingValue((long) 1, Long.class),
              new LongInterestingValue((long) -1, Long.class),
              new LongInterestingValue((long) 23, Long.class),
              new LongInterestingValue((long) -32, Long.class),
              new LongMaxValue(Long.class),
              new LongMinValue(Long.class)));
      _interestingValuesByType.put(float.class, list(
              new FloatInterestingValue((float) 0.0, float.class),
              new FloatInterestingValue((float) 1.0, float.class),
              new FloatInterestingValue((float) -1.0, float.class),
              new FloatInterestingValue((float) 23.0, float.class),
              new FloatInterestingValue((float) 23.123, float.class),
              new FloatInterestingValue((float) -32.0, float.class),
              new FloatInterestingValue((float) -32.456, float.class),
              new FloatPositiveInfinity(float.class),
              new FloatNegativeInfinity(float.class),
              new FloatNaN(float.class),
              new FloatMaxValue(float.class),
              new FloatMinValue(float.class)));
      _interestingValuesByType.put(Float.class, list(
              new FloatInterestingValue((float) 0.0, Float.class),
              new FloatInterestingValue((float) 1.0, Float.class),
              new FloatInterestingValue((float) -1.0, Float.class),
              new FloatInterestingValue((float) 23.0, Float.class),
              new FloatInterestingValue((float) 23.123, Float.class),
              new FloatInterestingValue((float) -32.0, Float.class),
              new FloatInterestingValue((float) -32.456, Float.class),
              new FloatPositiveInfinity(Float.class),
              new FloatNegativeInfinity(Float.class),
              new FloatNaN(Float.class),
              new FloatMaxValue(Float.class),
              new FloatMinValue(Float.class)));
      _interestingValuesByType.put(double.class, list(
              new DoubleInterestingValue(0.0, double.class),
              new DoubleInterestingValue(1.0, double.class),
              new DoubleInterestingValue(-1.0, double.class),
              new DoubleInterestingValue(23.0, double.class),
              new DoubleInterestingValue(23.123, double.class),
              new DoubleInterestingValue(-32.0, double.class),
              new DoubleInterestingValue(-32.456, double.class),
              new DoublePositiveInfinity(double.class),
              new DoubleNegativeInfinity(double.class),
              new DoubleNaN(double.class),
              new DoubleMaxValue(double.class),
              new DoubleMinValue(double.class)));
      _interestingValuesByType.put(Double.class, list(
              new DoubleInterestingValue(0.0, Double.class),
              new DoubleInterestingValue(1.0, Double.class),
              new DoubleInterestingValue(-1.0, Double.class),
              new DoubleInterestingValue(23.0, Double.class),
              new DoubleInterestingValue(23.123, Double.class),
              new DoubleInterestingValue(-32.0, Double.class),
              new DoubleInterestingValue(-32.456, Double.class),
              new DoublePositiveInfinity(Double.class),
              new DoubleNegativeInfinity(Double.class),
              new DoubleNaN(Double.class),
              new DoubleMaxValue(Double.class),
              new DoubleMinValue(Double.class)));
      _interestingValuesByType.put(BigInteger.class, list(
              new BigIntegerInterestingValue("0"),
              new BigIntegerInterestingValue("1"),
              new BigIntegerInterestingValue("-1"),
              new BigIntegerInterestingValue("23"),
              new BigIntegerInterestingValue("-32"),
              new BigIntegerInterestingValue("123456789012345678901234567890"),
              new BigIntegerInterestingValue("-123456789012345678901234567890")));
      _interestingValuesByType.put(BigDecimal.class, list(
              new BigDecimalInterestingValue("0"),
              new BigDecimalInterestingValue("1"),
              new BigDecimalInterestingValue("-1"),
              new BigDecimalInterestingValue("23"),
              new BigDecimalInterestingValue("23.123"),
              new BigDecimalInterestingValue("-32"),
              new BigDecimalInterestingValue("-32.456"),
              new BigDecimalInterestingValue("123456789012345678901234567890.123456789"),
              new BigDecimalInterestingValue("-123456789012345678901234567890.123456789")));
    }
  }
}
