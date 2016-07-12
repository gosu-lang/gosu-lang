package gw.specContrib.expressions

uses java.math.BigDecimal
uses java.math.BigInteger

class Errant_InfinityAndNaNTest {
  var i1: double = Infinity
  var i2 = Infinity
  var i3: float = i2      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'DOUBLE', REQUIRED: 'FLOAT'
  var i4: int = Infinity      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'DOUBLE', REQUIRED: 'INT'
  var i5: float = Infinity

  var n1: double = NaN
  var n2 = NaN
  var n3: float = n2      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'DOUBLE', REQUIRED: 'FLOAT'
  var n4: int = NaN      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'DOUBLE', REQUIRED: 'INT'
  var n5: float = NaN

  function math() {
    var x: float = Infinity + 1      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'DOUBLE', REQUIRED: 'FLOAT'
    var y: float = NaN + 1      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'DOUBLE', REQUIRED: 'FLOAT'
  }


  var infinity1 = Infinity
  var naN1 = NaN
  var int1: Integer
  var double1: double
  var double111: Double

  function assign1() {
    double1 = Infinity
    double1 = NaN
    double111 = Infinity
    double111 = NaN

    var bigDecimal: BigDecimal
    bigDecimal = Infinity
    bigDecimal = NaN

    var bigInteger: BigInteger
    bigInteger = Infinity                  //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'DOUBLE', REQUIRED: 'JAVA.MATH.BIGINTEGER'
    bigInteger = NaN                  //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'DOUBLE', REQUIRED: 'JAVA.MATH.BIGINTEGER'
  }

  function foo(inf: double = Infinity) {
    switch (inf) {
      case 42.5:
        break;
      case Infinity:
        break
      case NaN:
        break;

    }
    if (inf == Infinity || inf == NaN) {
      print("Infinity or NaN")
    }
    for (i in 42.5..Infinity) {
      i++;
    }
    for (i in 42.5..NaN) {
      i++;
    }
  }

}