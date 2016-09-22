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

  class SampleDimNonFinal implements IDimension<SampleDim, BigDecimal> {
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

  function testArithmetic(a: SampleDim, b: SampleDim) {
    var bd: BigDecimal

    // IDE-2227
    var c1: SampleDim = a / bd
    var c2: SampleDim = a / b    //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.MATH.BIGDECIMAL', REQUIRED: 'GW.SPECCONTRIB.DIMENSIONS.ERRANT_DIMENSIONARITHMETIC.SAMPLEDIM'
    var c3: BigDecimal = a / b
  }

  function testFinalCoercion() {
    var finalDim: SampleDim
    var nonFinalDim: SampleDimNonFinal

    var works = finalDim as BigDecimal
    var fails = nonFinalDim as BigDecimal //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'GW.SPECCONTRIB.DIMENSIONS.ERRANT_DIMENSIONARITHMETIC.SAMPLEDIMNONFINAL' TO 'JAVA.MATH.BIGDECIMAL'
  }

  function testFinalVsNonFinalMath() {
    var fa : SampleDim
    var fb : SampleDim
    var nf : SampleDimNonFinal

    var sum1 = fa + fb
    var sum2 = fa + nf      //## issuekeys: OPERATOR '+' CANNOT BE APPLIED TO 'GW.SPECCONTRIB.DIMENSIONS.ERRANT_DIMENSIONARITHMETIC.SAMPLEDIM', 'GW.SPECCONTRIB.DIMENSIONS.ERRANT_DIMENSIONARITHMETIC.SAMPLEDIMNONFINAL'
  }
}
