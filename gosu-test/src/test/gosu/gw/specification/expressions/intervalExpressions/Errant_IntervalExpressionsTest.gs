package gw.specification.expressions.intervalExpressions

uses gw.lang.reflect.interval.IntegerInterval
uses gw.lang.reflect.interval.LongInterval
uses gw.lang.reflect.interval.BigDecimalInterval
uses gw.lang.reflect.interval.DateInterval
uses gw.lang.reflect.interval.ComparableInterval
uses gw.lang.reflect.interval.BigIntegerInterval
uses gw.lang.reflect.interval.ISequenceable
uses gw.lang.reflect.interval.SequenceableInterval

class Errant_IntervalExpressionsTest {
  function testIntervalType() {
    var s0 : CharSequence = "0"
    var s1 : CharSequence = "2"

    var i0 : IntegerInterval = 0..2
    var i1 : IntegerInterval = 0 as short..2 as short
    var i2 : LongInterval = 0l..2l
    var i3 : IntegerInterval = 0b..2b
    var i4 : IntegerInterval = 'a'..'c'
    var i5 : BigDecimalInterval= 0.0..2.0
    var i6 : BigDecimalInterval = 0.0f..2.0f
    var i7 = true..false  //## issuekeys: MSG_RELATIONAL_OPERATOR_CANNOT_BE_APPLIED_TO_TYPE
    var i8 : IntegerInterval = Integer.valueOf(0)..Integer.valueOf(2)
    var i9 = Boolean.TRUE..Boolean.FALSE  //## issuekeys: MSG_RELATIONAL_OPERATOR_CANNOT_BE_APPLIED_TO_TYPE
    var i10 : ComparableInterval<MyDim> = new MyDim(0)..new MyDim(2)
    var i11 : ComparableInterval= s0..s1
    var i12 : BigIntegerInterval = 0bi..2bi
    var i13 : DateInterval = new Date(1980, 12, 25)..new Date(1980, 12, 31)
  }

  function testIntervalRange() {
    var i0 = 0..2
    var i1 = 0|..2
    var i2 = 0..|2
    var i3 = 0|..|2
  }
}