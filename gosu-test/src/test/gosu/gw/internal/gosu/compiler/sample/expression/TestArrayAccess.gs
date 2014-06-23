package gw.internal.gosu.compiler.sample.expression

class TestArrayAccess
{
  function stringArray() : String
  {
    var arr = new String[] {"a", "b", "c"}
    return arr[1]
  }

  function booleanArray() : boolean
  {
    var arr = new boolean[] {false, true, false}
    return arr[1]
  }

  function byteArray() : byte
  {
    var arr = new byte[] {1, 2, 3}
    return arr[1]
  }

  function shortArray() : short
  {
    var arr = new short[] {1, 2, 3}
    return arr[1]
  }

  function intArray() : int
  {
    var arr = new int[] {1, 2, 3}
    return arr[1]
  }

  function longArray() : long
  {
    var arr = new long[] {1, 2, 3}
    return arr[1]
  }

  function doubleArray() : double
  {
    var arr = new double[] {1, 2, 3}
    return arr[1]
  }

  function charArray() : char
  {
    var arr = new char[] {'a', 'b', 'c'}
    return arr[1]
  }

  function listAccess() : String
  {
    var l = new java.util.ArrayList<String>() {"a", "b", "c"}
    return l[1]
  }

  function charSequence() : char
  {
    var s = "hello"
    return s.charAt(1)
  }

  function shortCircuitNullArray() : String
  {
    var arr : String[]
    return arr?[1]
  }
}