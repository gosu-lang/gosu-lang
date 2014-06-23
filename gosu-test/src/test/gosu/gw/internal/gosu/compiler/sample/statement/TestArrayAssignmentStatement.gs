package gw.internal.gosu.compiler.sample.statement

uses java.util.Map
uses java.util.HashMap
uses java.lang.Integer
  
class TestArrayAssignmentStatement
{
  function stringArray() : String
  {
    var arr = new String[] {"a", "b", "c"}
    arr[1] = "x"
    return arr[1]
  }

  function booleanArray() : boolean
  {
    var arr = new boolean[] {false, false, false}
    arr[1] = true
    return arr[1]
  }

  function byteArray() : byte
  {
    var arr = new byte[] {1, 2, 3}
    arr[1] = 8
    return arr[1]
  }

  function shortArray() : short
  {
    var arr = new short[] {1, 2, 3}
    arr[1] = 8
    return arr[1]
  }

  function intArray() : int
  {
    var arr = new int[] {1, 2, 3}
    arr[1] = 8
    return arr[1]
  }

  function longArray() : long
  {
    var arr = new long[] {1, 2, 3}
    arr[1] = 8
    return arr[1]
  }

  function doubleArray() : double
  {
    var arr = new double[] {1, 2, 3}
    arr[1] = 8
    return arr[1]
  }

  function charArray() : char
  {
    var arr = new char[] {'a', 'b', 'c'}
    arr[1] = 'x'
    return arr[1]
  }

  function listAccess() : String
  {
    var l = new java.util.ArrayList<String>() {"a", "b", "c"}
    l[1] = "x"
    return l[1]
  }

  // Hmmm
  function charSequence() : char
  {
    var s = "hello"
    return s.charAt(1)
  }

  function nullRoot() : String
  {
    var arr : String[]
    arr[1] = "fail" // should throw npe here
    return arr[1]
  }
}