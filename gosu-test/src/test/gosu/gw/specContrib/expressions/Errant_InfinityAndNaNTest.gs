package gw.specContrib.expressions

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
}