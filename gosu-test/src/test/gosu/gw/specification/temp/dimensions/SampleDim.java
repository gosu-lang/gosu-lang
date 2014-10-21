package gw.specification.temp.dimensions;

import gw.lang.IDimension;

/**
 */
public class SampleDim implements IDimension<SampleDim, Integer> {
  private final Integer _value;

  public SampleDim( Integer value ) {
    _value = value;
  }
  @Override
  public Integer toNumber() {
    return _value;
  }

  @Override
  public SampleDim fromNumber( Integer value ) {
    return new SampleDim( value );
  }

  @Override
  public Class<Integer> numberType() {
    return Integer.class;
  }

  @Override
  public int compareTo( SampleDim o ) {
    return _value.compareTo( o._value );
  }
}

