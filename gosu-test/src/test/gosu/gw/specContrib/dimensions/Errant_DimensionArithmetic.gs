package gw.specContrib.dimensions

uses java.lang.Class
uses java.math.BigDecimal

class Errant_DimensionArithmetic {
  final class SampleDim implements IDimension<SampleDim, BigDecimal> {
    override function toNumber(): BigDecimal { return null }
    override function fromNumber(p0: BigDecimal): SampleDim { return null }
    override function numberType(): Class<BigDecimal> { return null }
    override function compareTo(o: SampleDim): int { return 0 }

    function divide(v: BigDecimal): SampleDim {
    return null
  }

    function divide(v: SampleDim): BigDecimal {
      return null
    }
  }

  class NonFinalDim implements IDimension<SampleDim, BigDecimal> {
    override function toNumber(): BigDecimal { return null }
    override function fromNumber(p0: BigDecimal): SampleDim { return null }
    override function numberType(): Class<BigDecimal> { return null }
    override function compareTo(o: SampleDim): int { return 0 }

    function divide(v: BigDecimal): SampleDim {
    return null
  }

    function divide(v: SampleDim): BigDecimal {
      return null
    }
  }

  function test(a: SampleDim, b: SampleDim) {
    var bd: BigDecimal

    // IDE-2227
    var c1: SampleDim = a / bd
    var c2: SampleDim = a / b    //## issuekeys: MSG_TYPE_MISMATCH
    var c3: BigDecimal = a / b
  }

  function test1() {
    var d: NonFinalDim
    var a = d as BigDecimal      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'GW.SPECCONTRIB.DIMENSIONS.ERRANT_DIMENSIONARITHMETIC.NONFINALDIM' TO 'JAVA.MATH.BIGDECIMAL'
  }
}