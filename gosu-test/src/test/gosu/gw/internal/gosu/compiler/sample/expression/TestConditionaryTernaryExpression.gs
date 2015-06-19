package gw.internal.gosu.compiler.sample.expression

uses java.util.ArrayList
uses java.util.HashSet
uses java.util.HashMap

class TestConditionaryTernaryExpression
{
  function stringTernaryPos() : String
  {
    var x = 5
    return x > 0 ? "a" : "b"
  }
  function stringTernaryNeg() : String
  {
    var x = 5
    return x < 0 ? "a" : "b"
  }

  function charTernaryPos() : char
  {
    var x = 5
    return x > 0 ? 'a' : 'b'
  }
  function charTernaryNeg() : char
  {
    var x = 5
    return x < 0 ? 'a' : 'b'
  }
  
  function byteTernaryPos() : byte
  {
    var x = 5
    return (x > 0 ? 1 : 2b) as byte
  }
  function byteTernaryNeg() : byte
  {
    var x = 5
    return (x < 0 ? 1 : 2b) as byte
  }
  
  function shortTernaryPos() : short
  {
    var x = 5
    return (x > 0 ? 1 : 2s) as short
  }
  function shortTernaryNeg() : short
  {
    var x = 5
    return (x < 0 ? 1 : 2s) as short
  }
  
  function intTernaryPos() : int
  {
    var x = 5
    return x > 0 ? 1 : 2
  }
  function intTernaryNeg() : int
  {
    var x = 5
    return x < 0 ? 1 : 2
  }
  
  function longTernaryPos() : long
  {
    var x = 5
    return x > 0 ? 1 : 2
  }
  function longTernaryNeg() : long
  {
    var x = 5
    return x < 0 ? 1 : 2
  }
  
  function floatTernaryPos() : float
  {
    var x = 5
    return x > 0 ? 1 : 2
  }
  function floatTernaryNeg() : float
  {
    var x = 5
    return x < 0 ? 1 : 2
  }

  function doubleTernaryPos() : double
  {
    var x = 5
    return x > 0 ? 1 : 2
  }
  function doubleTernaryNeg() : double
  {
    var x = 5
    return x < 0 ? 1 : 2
  }

  function testElvisDoesNotEvaluatingConditionTwice() : int
  {
    var counter = 0
    var incr : block() : String = \ -> {
      counter += 1
      return "a"
    }

    var t = incr() ?: "b"  // incr() should only be evaluated once

    return counter // should be 1
  }
}