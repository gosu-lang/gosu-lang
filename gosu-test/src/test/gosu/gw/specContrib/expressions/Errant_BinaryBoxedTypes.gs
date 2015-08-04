package gw.specContrib.expressions

uses java.lang.Double
uses java.lang.Integer

class Errant_BinaryBoxedTypes {
  function test(iP: int, iB: Integer, dP: double, dB: Double) {
    var s0 = (iP + 1).toString()  //## issuekeys: CANNOT RESOLVE 'toString()'
    // IDE-2254
    var s1 = (iB + 1).toString()
    var s2 = (1 + iB).toString()
    var s3 = (iB + iP).toString()
    var s4 = (iB + dB).toString()
    var s5 = (iB + dP).toString()
  }
}