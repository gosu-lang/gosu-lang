/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.core.expressions.arithmetic;

import gw.lang.IDimension;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 */
public abstract class TestDimensions {

  public static final class TestDimensionWithAllAddOverrides implements IDimension<TestDimensionWithAllAddOverrides, BigDecimal> {
    private BigDecimal _number;

    public TestDimensionWithAllAddOverrides(BigDecimal number) {
      _number = number;
    }

    @Override
    public BigDecimal toNumber() {
      return _number;
    }

    @Override
    public TestDimensionWithAllAddOverrides fromNumber(BigDecimal units) {
      return new TestDimensionWithAllAddOverrides(units);
    }

    @Override
    public Class<BigDecimal> numberType() {
      return BigDecimal.class;
    }

    public Integer add(BigDecimal number) {
      return 1;
    }

    public Integer add(BigInteger number) {
      return 2;
    }

    public Integer add(Double number) {
      return 3;
    }

    public Integer add(double number) {
      return 4;
    }

    public Integer add(Float number) {
      return 5;
    }

    public Integer add(float number) {
      return 6;
    }

    public int add(Long number) {
      return 7;
    }

    public int add(long number) {
      return 8;
    }

    public Double add(Integer number) {
      return 9.0;
    }

    public Double add(int number) {
      return 10.0;
    }

    public double add(Short number) {
      return 11.0;
    }

    public double add(short number) {
      return 12.0;
    }

    public Double add(Character number) {
      return 13.0;
    }

    public Double add(char number) {
      return 14.0;
    }

    public Double add(Byte number) {
      return 15.0;
    }

    public Double add(byte number) {
      return 16.0;
    }

    public float add(TestStrictBigDecimalDimension number) {
      return 17.0f;
    }

    @Override
    public int compareTo( TestDimensionWithAllAddOverrides o ) {
      return _number.compareTo( o._number );
    }
  }

  public static final class TestDimensionWithThreeAddOverrides implements IDimension<TestDimensionWithThreeAddOverrides, BigDecimal> {
    private BigDecimal _number;
    private boolean _addInvoked;

    public TestDimensionWithThreeAddOverrides(BigDecimal number) {
      _number = number;
    }

    @Override
    public BigDecimal toNumber() {
      return _number;
    }

    @Override
    public TestDimensionWithThreeAddOverrides fromNumber(BigDecimal units) {
      return new TestDimensionWithThreeAddOverrides(units);
    }

    @Override
    public Class<BigDecimal> numberType() {
      return BigDecimal.class;
    }

    public Integer add(Double number) {
      return 3;
    }

    public Double add(int number) {
      return 10.0;
    }

    public TestDimensionWithThreeAddOverrides add(TestDimensionWithThreeAddOverrides number) {
      _addInvoked = true;
      return new TestDimensionWithThreeAddOverrides(_number.add(number._number));
    }

    public boolean isAddInvoked() {
      return _addInvoked;
    }

    @Override
    public int compareTo( TestDimensionWithThreeAddOverrides o ) {
      return _number.compareTo( o._number );
    }
  }

  // Strict dimensions

  public static final class TestStrictBigDecimalDimension implements IDimension<TestStrictBigDecimalDimension, BigDecimal> {
    private BigDecimal _number;

    public TestStrictBigDecimalDimension(BigDecimal number) {
      _number = number;
    }

    @Override
    public BigDecimal toNumber() {
      return _number;
    }

    @Override
    public TestStrictBigDecimalDimension fromNumber(BigDecimal units) {
      return new TestStrictBigDecimalDimension(units);
    }

    @Override
    public Class<BigDecimal> numberType() {
      return BigDecimal.class;
    }

    @Override
    public int compareTo( TestStrictBigDecimalDimension o ) {
      return _number.compareTo( o._number );
    }
  }

  public static final class TestStrictBigIntegerDimension implements IDimension<TestStrictBigIntegerDimension, BigInteger> {
    private BigInteger _number;

    public TestStrictBigIntegerDimension(BigInteger number) {
      _number = number;
    }

    @Override
    public BigInteger toNumber() {
      return _number;
    }

    @Override
    public TestStrictBigIntegerDimension fromNumber(BigInteger units) {
      return new TestStrictBigIntegerDimension(units);
    }

    @Override
    public Class<BigInteger> numberType() {
      return BigInteger.class;
    }

    @Override
    public int compareTo( TestStrictBigIntegerDimension o ) {
      return _number.compareTo( o._number );
    }
  }

  public static final class TestStrictDoubleDimension implements IDimension<TestStrictDoubleDimension, Double> {
    private Double _number;

    public TestStrictDoubleDimension(Double number) {
      _number = number;
    }

    @Override
    public Double toNumber() {
      return _number;
    }

    @Override
    public TestStrictDoubleDimension fromNumber(Double units) {
      return new TestStrictDoubleDimension(units);
    }

    @Override
    public Class<Double> numberType() {
      return Double.class;
    }

    @Override
    public int compareTo( TestStrictDoubleDimension o ) {
      return _number.compareTo( o._number );
    }
  }

  public static final class TestStrictFloatDimension implements IDimension<TestStrictFloatDimension, Float> {
    private Float _number;

    public TestStrictFloatDimension(Float number) {
      _number = number;
    }

    @Override
    public Float toNumber() {
      return _number;
    }

    @Override
    public TestStrictFloatDimension fromNumber(Float units) {
      return new TestStrictFloatDimension(units);
    }

    @Override
    public Class<Float> numberType() {
      return Float.class;
    }

    @Override
    public int compareTo( TestStrictFloatDimension o ) {
      return _number.compareTo( o._number );
    }
  }

  public static final class TestStrictLongDimension implements IDimension<TestStrictLongDimension, Long> {
    private Long _number;

    public TestStrictLongDimension(Long number) {
      _number = number;
    }

    @Override
    public Long toNumber() {
      return _number;
    }

    @Override
    public TestStrictLongDimension fromNumber(Long units) {
      return new TestStrictLongDimension(units);
    }

    @Override
    public Class<Long> numberType() {
      return Long.class;
    }

    @Override
    public int compareTo( TestStrictLongDimension o ) {
      return _number.compareTo( o._number );
    }
  }

  public static final class TestStrictIntegerDimension implements IDimension<TestStrictIntegerDimension, Integer> {
    private Integer _number;

    public TestStrictIntegerDimension(Integer number) {
      _number = number;
    }

    @Override
    public Integer toNumber() {
      return _number;
    }

    @Override
    public TestStrictIntegerDimension fromNumber(Integer units) {
      return new TestStrictIntegerDimension(units);
    }

    @Override
    public Class<Integer> numberType() {
      return Integer.class;
    }

    @Override
    public int compareTo( TestStrictIntegerDimension o ) {
      return _number.compareTo( o._number );
    }
  }

  public static final class TestStrictShortDimension implements IDimension<TestStrictShortDimension, Short> {
    private Short _number;

    public TestStrictShortDimension(Short number) {
      _number = number;
    }

    @Override
    public Short toNumber() {
      return _number;
    }

    @Override
    public TestStrictShortDimension fromNumber(Short units) {
      return new TestStrictShortDimension(units);
    }

    @Override
    public Class<Short> numberType() {
      return Short.class;
    }

    @Override
    public int compareTo( TestStrictShortDimension o ) {
      return _number.compareTo( o._number );
    }
  }

  public static final class TestStrictByteDimension implements IDimension<TestStrictByteDimension, Byte> {
    private Byte _number;

    public TestStrictByteDimension(Byte number) {
      _number = number;
    }

    @Override
    public Byte toNumber() {
      return _number;
    }

    @Override
    public TestStrictByteDimension fromNumber(Byte units) {
      return new TestStrictByteDimension(units);
    }

    @Override
    public Class<Byte> numberType() {
      return Byte.class;
    }

    @Override
    public int compareTo( TestStrictByteDimension o ) {
      return _number.compareTo( o._number );
    }
  }

  // ----------- One-off cases

  public static final class TestStrictDimensionWithMethodSpy implements IDimension<TestStrictDimensionWithMethodSpy, Integer> {
    private int _number;
    private boolean _fromNumberInvoked = false;

    public TestStrictDimensionWithMethodSpy(Integer number) {
      _number = number;
    }

    @Override
    public Integer toNumber() {
      return _number;
    }

    @Override
    public TestStrictDimensionWithMethodSpy fromNumber(Integer units) {
      _fromNumberInvoked = true;
      return new TestStrictDimensionWithMethodSpy(units);
    }

    @Override
    public Class<Integer> numberType() {
      return Integer.class;
    }

    public boolean wasFromNumberInvoked() {
      return _fromNumberInvoked;
    }

    @Override
    public int compareTo( TestStrictDimensionWithMethodSpy o ) {
      return Integer.compare( _number, o._number );
    }
  }

  // Error cases

  public static class TestNonFinalDimension implements IDimension<TestNonFinalDimension, BigDecimal> {
    private BigDecimal _number;

    public TestNonFinalDimension(BigDecimal number) {
      _number = number;
    }

    @Override
    public BigDecimal toNumber() {
      return _number;
    }

    @Override
    public TestNonFinalDimension fromNumber(BigDecimal units) {
      return new TestNonFinalDimension(units);
    }

    @Override
    public Class<BigDecimal> numberType() {
      return BigDecimal.class;
    }

    @Override
    public int compareTo( TestNonFinalDimension o ) {
      return _number.compareTo( o._number );
    }
  }
}
