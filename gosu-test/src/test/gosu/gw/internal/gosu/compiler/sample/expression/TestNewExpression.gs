package gw.internal.gosu.compiler.sample.expression

uses java.util.ArrayList
uses java.util.HashSet
uses java.util.HashMap

class TestNewExpression
{
  function newString() : String
  {
    return new String( "hello" )
  }

  function newStringArray() : String[]
  {
    return new String[1]
  }

  function newStringArrayInitialized() : String[]
  {
    return new String[] {"a", "b", "c"}
  }

  function newIntArrayInitialized() : int[]
  {
    return new int[] {2, 3, 4}
  }

  function newDoubleArray() : double[]
  {
    return new double[4]
  }

  function newDoubleArrayInitialized() : double[]
  {
    return new double[] {42}
  }

  function newThreeDDoubleArrayWithTwoSizes() : double[][][]
  {
    var x = 5
    return new double[x][6][]
  }

  function newStaticInner() : StaticInner
  {
    return new StaticInner()
  }
  static class StaticInner
  {
  }

  function newNonStaticInner() : NonStaticInner
  {
    return new NonStaticInner()
  }
  class NonStaticInner
  {
  }

  function newAnonymousInner() : StaticInner
  {
    return new StaticInner() {}
  }

  function newListWithInitializer() : ArrayList
  {
    return new ArrayList() {"a", "b", "c"}
  }

  function newSetWithInitializer() : HashSet
  {
    return new HashSet() {"a", "b", "c"}
  }

  function newMapWithInitializer() : HashMap
  {
    return new HashMap() {"a"->"AA", "b"->"BB"}
  }
}