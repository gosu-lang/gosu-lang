package gw.specContrib.typeinference

uses java.math.BigDecimal

class Errant_InferenceWithNull {
  static class ValueRange<T extends Object> {
    var _first : T
    var _last : T

    construct(first : T, last : T) {
      _first = first
      _last = last
    }
  }

  function testWithDifferentTypes()
  {
    var n: Number
    var x = new ValueRange( n, 5bd )
    var bad: ValueRange<Object>
    x = bad  //## issuekeys: MSG_TYPE_MISMATCH
    var good: ValueRange<Number>
    x = good
  }

  function testWithNull()
  {
    var x = new ValueRange( null, 5bd )
    var bad: ValueRange<Object>
    x = bad  //## issuekeys: MSG_TYPE_MISMATCH
    var notgood: ValueRange<Number>
    x = notgood  //## issuekeys: MSG_TYPE_MISMATCH
    var good: ValueRange<BigDecimal>
    x = good
  }

  function other()
  {
    // no lhs context, infer lhs from null as Object
    var l = {null}
    var ls: List<String> = l  //## issuekeys: MSG_TYPE_MISMATCH
    var lo: List<Object> = l

    // lhs context is String, infer null's type from lhs as String
    ls = {null}
  }

  static class Foo<T extends Object> {
    construct( t: T ) {
    }
    construct( t: T, tee: T ) {
    }
  }

  function testNull()
  {
    var x = new Foo( null )
    x = new Foo<Object>( null )

    var y: Foo<String> = new Foo( null )

    var a = new Foo( null, null )


    var bd = new Foo( null, 5bd )
    bd = new Foo<Number>( null, null )  //## issuekeys: MSG_TYPE_MISMATCH
  }
}
