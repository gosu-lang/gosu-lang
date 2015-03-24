package gw.specification.dimensions.p0

uses java.lang.Class

/**
 * Created by sliu on 2/18/2015.
 */
final class SampleDimensionWithArith_SpecialNumberWithArith implements IDimension< SampleDimensionWithArith_SpecialNumberWithArith, SpecialNumberWithArith >{

    var _value : SpecialNumberWithArith

    construct(value : SpecialNumberWithArith){
      _value = value
    }

    override function toNumber(): SpecialNumberWithArith {
      return _value
    }

    override function fromNumber(specialNumber : SpecialNumberWithArith): SampleDimensionWithArith_SpecialNumberWithArith {
      return new SampleDimensionWithArith_SpecialNumberWithArith (specialNumber)
    }

    override function numberType(): Class< SpecialNumberWithArith > {
      return SpecialNumberWithArith
    }

    override function compareTo(o: SampleDimensionWithArith_SpecialNumberWithArith): int {
      return _value.doubleValue() < o.toNumber().doubleValue()  ? -1 : _value.doubleValue() > o.toNumber().doubleValue() ? 1 : 0
    }

    function add(o : SampleDimensionWithArith_SpecialNumberWithArith) : SampleDimensionWithArith_SpecialNumberWithArith {
      return new SampleDimensionWithArith_SpecialNumberWithArith (this.toNumber().add(o.toNumber()).add(new SpecialNumberWithArith(20)))
    }

    function multiply(o : SampleDimensionWithArith_SpecialNumberWithArith) : SampleDimensionWithArith_SpecialNumberWithArith {
      return new SampleDimensionWithArith_SpecialNumberWithArith (this.toNumber().multiply(o.toNumber()).add(new SpecialNumberWithArith(20)))
    }

    function subtract(o : SampleDimensionWithArith_SpecialNumberWithArith) : SampleDimensionWithArith_SpecialNumberWithArith {
      return new SampleDimensionWithArith_SpecialNumberWithArith (this.toNumber().subtract(o.toNumber()).add(new SpecialNumberWithArith(20)))
    }

    function divide(o : SampleDimensionWithArith_SpecialNumberWithArith) : SampleDimensionWithArith_SpecialNumberWithArith {
      return new SampleDimensionWithArith_SpecialNumberWithArith (this.toNumber().divide(o.toNumber()).add(new SpecialNumberWithArith(20)))
    }

    function modulo(o : SampleDimensionWithArith_SpecialNumberWithArith) : SampleDimensionWithArith_SpecialNumberWithArith {
      return new SampleDimensionWithArith_SpecialNumberWithArith (this.toNumber().modulo(o.toNumber()).add(new SpecialNumberWithArith(20)))
    }




}