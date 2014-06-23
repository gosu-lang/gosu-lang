package gw.specification.blocks

uses gw.BaseVerifyErrantTest
uses java.lang.Integer

class BlockTest extends BaseVerifyErrantTest {

  //=====================================================
  // Error tests
  //=====================================================

  function testErrant_ArrayTest() {
    processErrantType(Errant_BlockTest)
  }

  //=====================================================
  // Basic semantic tests
  //=====================================================

  function testInvocation()
  {
    var x = \-> 10
    assertEquals( 10, x())
  }

  function testArgPassing()
  {
    var x = \ y : int -> y + 10
    assertEquals( 20, x(10) )
  }

  function testBasicClosure()
  {
    var z = 10

    var x = \ y : int -> y + z

    assertEquals( 20, x(10) )
  }

  function introduceConflictingVars(it(): int) : int{
    var x = 10
    var y = 20
    return it()
  }

  function testDownwardFunArgs()
  {
    var x = 1
    var y = 1
    var it = \-> x + y
    assertEquals( 2, introduceConflictingVars( it ) )
  }

  function makeAdder() : block(i:int):int
  {
    var z = 0
    return \ y : int -> {
      z += y
      return z
    }
  }

  function testUpwardFunArgs()
  {
    var x = makeAdder()
    assertEquals( 10, x(10) )
    assertEquals( 20, x(10) )
    assertEquals( 30, x(10) )
  }

  function apply( x : int , y : block(z:int):int ) : int
  {
    return  y(x)
  }

  function testBlockPassingWorksProperly()
  {
    assertEquals( 20, apply( 10, \ z -> z + 10 ) )
  }

  //================================================
  // Block type positive tests
  //================================================

  function testBlockTypesAssignCorrectly() {

    var b1 : block()
    var b2 : block()
    var b3 : block():int
    var b4 : block():Integer
    var b5 : block():Object
    var b6 : block(x:Object)
    var b7 : block(x:String)

    b1 = b1  //obvious assignment
    b1 = b2  //obvious compatible

    b1 = b3  //returns void <- returns val
    b1 = b4  //returns void <- returns val

    b3 = b4  //returns primitive <- returns non-primitive
    b4 = b3  //returns non-primitive <- returns primitive

    b5 = b4  //returns super <- returns sub
    b5 = b3  //returns super <- returns primitive of sub

    b7 = b6 // covariance of args
  }

}