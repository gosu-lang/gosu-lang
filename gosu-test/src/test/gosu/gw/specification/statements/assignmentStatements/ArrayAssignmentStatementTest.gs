package gw.specification.statements.assignmentStatements

uses java.lang.Exception
uses gw.BaseVerifyErrantTest

class ArrayAssignmentStatementTest extends BaseVerifyErrantTest {

  function testStringArray()
  {
    var arr = new String[] {"a", "b", "c"}
    arr[1] = "x"
    assertEquals(arr[1],"x")
  }

  function testBooleanArray()
  {
    var arr = new boolean[] {false, false, false}
    arr[1] = true
    assertEquals(arr[1],true)
  }

  function testByteArray()
  {
    var arr = new byte[] {1, 2, 3}
    arr[1] = 8
    assertEquals(arr[1],8)
  }

  function testShortArray()
  {
    var arr = new short[] {1, 2, 3}
    arr[1] = 8
    assertEquals(arr[1],8)
  }

  function testIntArray()
  {
    var arr = new int[] {1, 2, 3}
    arr[1] = 8
    assertEquals(arr[1],8)
  }

  function testLongArray()
  {
    var arr = new long[] {1, 2, 3}
    arr[1] = 8
    assertEquals(arr[1],8L)
  }

  function testDoubleArray()
  {
    var arr = new double[] {1, 2, 3}
    arr[1] = 8
    assertEquals(arr[1],8D)
  }

  function testCharArray()
  {
    var arr = new char[] {'a', 'b', 'c'}
    arr[1] = 'x'
    assertEquals(arr[1],'x')
  }

  function testListAccess()
  {
    var l = new java.util.ArrayList () {"a", "b", "c"}
    l[1] = "x"
    assertEquals(l[1],"x")
  }

  function testNullRoot()
  {
    var arr : String[]
    var x = 0
    try{
      arr[1] = "fail"
    }catch(e : Exception){
      x = 1
    }
    assertEquals(x,1)
  }

}