package gw.internal.gosu.compiler.blocks

class BlockThisPointerTest extends gw.test.TestClass
{
  //=========================================================
  // helpers
  //=========================================================
  var _testField : String as WritableProp

  property get IntProperty() : int {
    return 42
  }

  property get RefProperty() : String {
    return "foo"
  }

  function intFunction() : int {
    return 42
  }
  
  function refFunction() : String {
    return "foo"
  }
   
  //=========================================================
  // 'this' pointer tests
  //=========================================================

  function testThisPointer() {
    var blk = \-> this
    assertEquals( this, blk() )
  }

  function testThisPointerMethodInvocation1() {
    var blk = \-> this.intFunction()
    assertEquals( 42, blk() )
  }
  
  function testThisPointerMethodInvocation2() {
    var blk = \-> this.refFunction()
    assertEquals( "foo", blk() )
  }
  
  function testThisPointerPropertyInvocation1() {
    var blk = \-> this.IntProperty
    assertEquals( 42, blk() )
  }
  
  function testThisPointerPropertyInvocation2() {
    var blk = \-> this.RefProperty
    assertEquals( "foo", blk() )
  }
  
  function testThisPointerPropertyWriting() {
    WritableProp = "foo"
    var blk = \-> { this.WritableProp = "bar" }
    blk()
    assertEquals( "bar", WritableProp  )
    WritableProp = "foo"
    blk()
    assertEquals( "bar", WritableProp )
  }
  
  function testThisPointerFeildReading() {
    _testField = "foo"
    var blk = \-> this._testField
    assertEquals( "foo", blk() )
    _testField = "bar"
    assertEquals( "bar", blk() )
  }
  
  function testThisPointerFeildWriting() {
    _testField = "foo"
    var blk = \-> { this._testField = "bar" }
    blk()
    assertEquals( "bar", _testField  )
    _testField = "foo"
    blk()
    assertEquals( "bar", _testField )
  }
  
  function testNestedThisPointer() {
    var blk = \-> \-> this
    var blk2 = blk()
    assertEquals( this, blk2() )
  }

  function testNestedThisPointerMethodInvocation1() {
    var blk = \-> \-> this.intFunction()
    var blk2 = blk()
    assertEquals( 42, blk2() )
  }
  
  function testNestedThisPointerMethodInvocation2() {
    var blk = \-> \-> this.refFunction()
    var blk2 = blk()
    assertEquals( "foo", blk2() )
  }
  
  function testNestedThisPointerPropertyInvocation1() {
    var blk = \-> \-> this.IntProperty
    var blk2 = blk()
    assertEquals( 42, blk2() )
  }
  
  function testNestedThisPointerPropertyInvocation2() {
    var blk = \-> \-> this.RefProperty
    var blk2 = blk()
    assertEquals( "foo", blk2() )
  }
  
  function testNestedThisPointerPropertyWriting() {
    WritableProp = "foo"
    var blk = \-> \-> { this.WritableProp = "bar" }
    var blk2 = blk()
    blk2()
    assertEquals( "bar", WritableProp  )
    WritableProp = "foo"
    blk2()
    assertEquals( "bar", WritableProp )
  }
  
  function testNestedThisPointerFeildReading() {
    _testField = "foo"
    var blk = \-> \-> this._testField
    var blk2 = blk()
    assertEquals( "foo", blk2() )
    _testField = "bar"
    assertEquals( "bar", blk2() )
  }
  
  function testNestedThisPointerFeildWriting() {
    _testField = "foo"
    var blk = \-> \-> { this._testField = "bar" }
    var blk2 = blk()
    blk2()
    assertEquals( "bar", _testField  )
    _testField = "foo"
    blk2()
    assertEquals( "bar", _testField )
  }

  function testTripleNestedThisPointer() {
    var blk = \-> \-> \-> this
    var blk2 = blk()
    var blk3 = blk2()
    assertEquals( this, blk3() )
  }

  function testTripleNestedThisPointerMethodInvocation1() {
    var blk = \-> \-> \-> this.intFunction()
    var blk2 = blk()
    var blk3 = blk2()
    assertEquals( 42, blk3() )
  }
  
  function testTripleNestedThisPointerMethodInvocation2() {
    var blk = \-> \-> \-> this.refFunction()
    var blk2 = blk()
    var blk3 = blk2()
    assertEquals( "foo", blk3() )
  }
  
  function testTripleNestedThisPointerPropertyInvocation1() {
    var blk = \-> \-> \-> this.intFunction()
    var blk2 = blk()
    var blk3 = blk2()
    assertEquals( 42, blk3() )
  }
  
  function testTripleNestedThisPointerPropertyInvocation2() {
    var blk = \-> \-> \-> this.RefProperty
    var blk2 = blk()
    var blk3 = blk2()
    assertEquals( "foo", blk3() )
  }
  
  function testTripleNestedThisPointerPropertyWriting() {
    WritableProp = "foo"
    var blk = \-> \-> \-> { this.WritableProp = "bar" }
    var blk2 = blk()
    var blk3 = blk2()
    blk3()
    assertEquals( "bar", WritableProp  )
    WritableProp = "foo"
    blk3()
    assertEquals( "bar", WritableProp )
  }
  
  function testTripleNestedThisPointerFeildReading() {
    _testField = "foo"
    var blk = \-> \-> \-> this._testField
    var blk2 = blk()
    var blk3 = blk2()
    assertEquals( "foo", blk3() )
    _testField = "bar"
    assertEquals( "bar", blk3() )
  }
  
  function testTripleNestedThisPointerFeildWriting() {
    _testField = "foo"
    var blk = \-> \-> \-> { this._testField = "bar" }
    var blk2 = blk()
    var blk3 = blk2()
    blk3()
    assertEquals( "bar", _testField  )
    _testField = "foo"
    blk3()
    assertEquals( "bar", _testField )
  }

  function testQuadrupleNestedThisPointer() {
    var blk = \-> \-> \->\-> this
    var blk2 = blk()
    var blk3 = blk2()
    var blk4 = blk3()
    assertEquals( this, blk4() )
  }

  function testQuadrupleNestedThisPointerMethodInvocation1() {
    var blk = \-> \-> \->\-> this.intFunction()
    var blk2 = blk()
    var blk3 = blk2()
    var blk4 = blk3()
    assertEquals( 42, blk4() )
  }
  
  function testQuadrupleNestedThisPointerMethodInvocation2() {
    var blk = \-> \-> \->\-> this.refFunction()
    var blk2 = blk()
    var blk3 = blk2()
    var blk4 = blk3()
    assertEquals( "foo", blk4() )
  }
  
  function testQuadrupleNestedThisPointerPropertyInvocation1() {
    var blk = \-> \-> \->\-> this.intFunction()
    var blk2 = blk()
    var blk3 = blk2()
    var blk4 = blk3()
    assertEquals( 42, blk4() )
  }
  
  function testQuadrupleNestedThisPointerPropertyInvocation2() {
    var blk = \-> \-> \->\-> this.RefProperty
    var blk2 = blk()
    var blk3 = blk2()
    var blk4 = blk3()
    assertEquals( "foo", blk4() )
  }

  function testQuadrupleNestedThisPointerPropertyWriting() {
    WritableProp = "foo"
    var blk = \-> \-> \->\-> { this.WritableProp = "bar" }
    var blk2 = blk()
    var blk3 = blk2()
    var blk4 = blk3()
    blk4()
    assertEquals( "bar", WritableProp  )
    WritableProp = "foo"
    blk4()
    assertEquals( "bar", WritableProp )
  }
  
  function testQuadrupleNestedThisPointerFeildReading() {
    _testField = "foo"
    var blk = \-> \-> \->\-> this._testField
    var blk2 = blk()
    var blk3 = blk2()
    var blk4 = blk3()
    assertEquals( "foo", blk4() )
    _testField = "bar"
    assertEquals( "bar", blk4() )
  }
  
  function testQuadrupleNestedThisPointerFeildWriting() {
    _testField = "foo"
    var blk = \-> \-> \->\-> { this._testField = "bar" }
    var blk2 = blk()
    var blk3 = blk2()
    var blk4 = blk3()
    blk4()
    assertEquals( "bar", _testField  )
    _testField = "foo"
    blk4()
    assertEquals( "bar", _testField )
  }

  //=========================================================
  // implicit this pointer tests
  //=========================================================

  function testImplicitThisPointerMethodInvocation1() {
    var blk = \-> intFunction()
    assertEquals( 42, blk() )
  }
    
  function testImplicitThisPointerMethodInvocation2() {
    var blk = \-> refFunction()
    assertEquals( "foo", blk() )
  }

  function testImplicitThisPointerPropertyInvocation1() {
    var blk = \-> IntProperty
    assertEquals( 42, blk() )
  }
    
  function testImplicitThisPointerPropertyInvocation2() {
    var blk = \-> RefProperty
    assertEquals( "foo", blk() )
  }
  
  function testImplicitThisPointerPropertyWriting() {
    WritableProp = "foo"
    var blk = \-> { WritableProp = "bar" }
    blk()
    assertEquals( "bar", WritableProp  )
    WritableProp = "foo"
    blk()
    assertEquals( "bar", WritableProp )
  }
  
  function testImplicitThisPointerFeildReading() {
    _testField = "foo"
    var blk = \-> _testField
    assertEquals( "foo", blk() )
    _testField = "bar"
    assertEquals( "bar", blk() )
  }
  
  function testImplicitThisPointerFeildWriting() {
    _testField = "foo"
    var blk = \-> { _testField = "bar" }
    blk()
    assertEquals( "bar", _testField  )
    _testField = "foo"
    blk()
    assertEquals( "bar", _testField )
  }
  
  function testNestedImplicitThisPointerMethodInvocation1() {
    var blk = \->\-> intFunction()
    var blk2 = blk()
    assertEquals( 42, blk2() )
  }
    
  function testNestedImplicitThisPointerMethodInvocation2() {
    var blk = \->\-> refFunction()
    var blk2 = blk()
    assertEquals( "foo", blk2() )
  }

  function testNestedImplicitThisPointerPropertyInvocation1() {
    var blk = \->\-> IntProperty
    var blk2 = blk()
    assertEquals( 42, blk2() )
  }
    
  function testNestedImplicitThisPointerPropertyInvocation2() {
    var blk = \->\-> this.RefProperty
    var blk2 = blk()
    assertEquals( "foo", blk2() )
  }

  function testNestedImplicitThisPointerPropertyWriting() {
    WritableProp = "foo"
    var blk = \->\-> { WritableProp = "bar" }
    var blk2 = blk()
    blk2()
    assertEquals( "bar", WritableProp  )
    WritableProp = "foo"
    blk2()
    assertEquals( "bar", WritableProp )
  }
  
  function testNestedImplicitThisPointerFeildReading() {
    _testField = "foo"
    var blk = \->\-> _testField
    var blk2 = blk()
    assertEquals( "foo", blk2() )
    _testField = "bar"
    assertEquals( "bar", blk2() )
  }
  
  function testNestedImplicitThisPointerFeildWriting() {
    _testField = "foo"
    var blk = \->\-> { _testField = "bar" }
    var blk2 = blk()
    blk2()
    assertEquals( "bar", _testField  )
    _testField = "foo"
    blk2()
    assertEquals( "bar", _testField )
  }

  function testTripleNestedImplicitThisPointerMethodInvocation1() {
    var blk = \->\->\-> intFunction()
    var blk2 = blk()
    var blk3 = blk2()
    assertEquals( 42, blk3() )
  }
    
  function testTripleNestedImplicitThisPointerMethodInvocation2() {
    var blk = \->\->\-> refFunction()
    var blk2 = blk()
    var blk3 = blk2()
    assertEquals( "foo", blk3() )
  }

  function testTripleNestedImplicitThisPointerPropertyInvocation1() {
    var blk = \->\->\-> IntProperty
    var blk2 = blk()
    var blk3 = blk2()
    assertEquals( 42, blk3() )
  }
    
  function testTripleNestedImplicitThisPointerPropertyInvocation2() {
    var blk = \->\->\-> this.RefProperty
    var blk2 = blk()
    var blk3 = blk2()
    assertEquals( "foo", blk3() )
  }
  
  function testTripleNestedImplicitThisPointerPropertyWriting() {
    WritableProp = "foo"
    var blk = \->\-> \-> { WritableProp = "bar" }
    var blk2 = blk()
    var blk3 = blk2()
    blk3()
    assertEquals( "bar", WritableProp  )
    WritableProp = "foo"
    blk3()
    assertEquals( "bar", WritableProp )
  }
  
  function testTripleNestedImplicitThisPointerFeildReading() {
    _testField = "foo"
    var blk = \->\->\->  _testField
    var blk2 = blk()
    var blk3 = blk2()
    assertEquals( "foo", blk3() )
    _testField = "bar"
    assertEquals( "bar", blk3() )
  }
  
  function testTripleNestedImplicitThisPointerFeildWriting() {
    _testField = "foo"
    var blk = \->\->\-> { _testField = "bar" }
    var blk2 = blk()
    var blk3 = blk2()
    blk3()
    assertEquals( "bar", _testField  )
    _testField = "foo"
    blk3()
    assertEquals( "bar", _testField )
  }

}