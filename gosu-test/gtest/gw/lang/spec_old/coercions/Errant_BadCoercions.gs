package gw.lang.spec_old.coercions
uses gw.testharness.DoNotVerifyResource
uses java.lang.Comparable

@DoNotVerifyResource
class Errant_BadCoercions {

  function primitivesShouldNotCoerceToAConstrainedComparable() {
    var compOfString : Comparable<String>
    compOfString = true
    compOfString = 1 as short
    compOfString = 1 as int
    compOfString = 1 as long
    compOfString = 1.0 as float
    compOfString = 1.0 as double
  }

}
