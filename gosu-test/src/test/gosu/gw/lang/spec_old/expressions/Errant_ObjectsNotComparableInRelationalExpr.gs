package gw.lang.spec_old.expressions
uses gw.testharness.DoNotVerifyResource
uses java.math.BigDecimal

@DoNotVerifyResource
class Errant_ObjectsNotComparableInRelationalExpr {

  construct() {
    var x : Object = new BigDecimal("10000") 
    var y : Object = new BigDecimal("5000") 
    print( x > y )
  }

}
