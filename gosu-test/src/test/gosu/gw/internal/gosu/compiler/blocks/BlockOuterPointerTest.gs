package gw.internal.gosu.compiler.blocks
uses java.util.concurrent.Callable

class BlockOuterPointerTest extends gw.test.TestClass
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
  // 'outer' pointer tests
  //=========================================================

  function testOuterPointerOneDeep() {
    var c1 = new Callable() {
      override function call() : block():Object {
        return \-> outer
      }
    }
    var blk = c1.call()
    assertEquals( this, blk() )
  }

  function testOuterPointerMethodInvocation1() {
    var c1 = new Callable() {
      override function call() : block():int {
        return \-> outer.intFunction()
      }
    }
    var blk = c1.call()
    assertEquals( 42, blk() )
  }

  function testOuterPointerMethodInvocation2() {
    var c1 = new Callable() {
      override function call() : block():Object {
        return \-> outer.refFunction()
      }
    }
    var blk = c1.call()
    assertEquals( "foo", blk() )
  }
  
  function testOuterPointerPropertyInvocation1() {
    var c1 = new Callable() {
      override function call() : block():int {
        return \-> outer.IntProperty
      }
    }
    var blk = c1.call()
    assertEquals( 42, blk() )
  }
  
  function testOuterPointerPropertyInvocation2() {
    var c1 = new Callable() {
      override function call() : block():Object {
        return \-> outer.RefProperty
      }
    }
    var blk = c1.call()
    assertEquals( "foo", blk() )
  }
  
  function testOuterPointerPropertyWriting() {
    WritableProp = "foo"
    var c1 = new Callable() {
      override function call() : block() {
        return \-> { outer.WritableProp = "bar" }
      }
    }
    var blk = c1.call()
    blk()
    assertEquals( "bar", WritableProp  )
    WritableProp = "foo"
    blk()
    assertEquals( "bar", WritableProp )
  }

  function testOuterPointerFeildReading() {
    _testField = "foo"
    var c1 = new Callable() {
      override function call() : block():Object {
        return \-> outer._testField
      }
    }
    var blk = c1.call()
    assertEquals( "foo", blk() )
    _testField = "bar"
    assertEquals( "bar", blk() )
  }
  
  function testOuterPointerFeildWriting() {
    _testField = "foo"
    var c1 = new Callable() {
      override function call() : block() {
        return \-> { outer._testField = "bar" }
      }
    }
    var blk = c1.call()
    blk()
    assertEquals( "bar", _testField  )
    _testField = "foo"
    blk()
    assertEquals( "bar", _testField )
  }
  
  function testNestedOuterPointer() {
    var c1 = new Callable() {
      override function call() : block():block():Object {
        return \->\-> outer
      }
    }
    var blk = c1.call()
    var blk2 = blk()
    assertEquals( this, blk2() )
  }
  
  function testNestedOuterPointerMethodInvocation1() {
    var c1 = new Callable() {
      override function call() : block():block():int {
        return \->\-> outer.intFunction()
      }
    }
    var blk = c1.call()
    var blk2 = blk()
    assertEquals( 42, blk2() )
  }

  function testNestedOuterPointerMethodInvocation2() {
    var c1 = new Callable() {
      override function call() : block():block():Object {
        return \-> \-> outer.refFunction()
      }
    }
    var blk = c1.call()
    var blk2 = blk()
    assertEquals( "foo", blk2() )
  }
  
  function testNestedOuterPointerPropertyInvocation1() {
    var c1 = new Callable() {
      override function call() : block():block():int {
        return \-> \-> outer.IntProperty
      }
    }
    var blk = c1.call()
    var blk2 = blk()
    assertEquals( 42, blk2() )
  }
  
  function testNestedOuterPointerPropertyInvocation2() {
    var c1 = new Callable() {
      override function call() : block():block():Object {
        return \-> \-> outer.RefProperty
      }
    }
    var blk = c1.call()
    var blk2 = blk()
    assertEquals( "foo", blk2() )
  }

  function testNestedOuterPointerPropertyWriting() {
    var c1 = new Callable() {
      override function call() : block():block() {
        return \-> \-> { outer.WritableProp = "bar" }
      }
    }
    var blk = c1.call()
    WritableProp = "foo"
    var blk2 = blk()
    blk2()
    assertEquals( "bar", WritableProp  )
    WritableProp = "foo"
    blk2()
    assertEquals( "bar", WritableProp )
  }
  
  function testNestedOuterPointerFeildReading() {
    var c1 = new Callable() {
      override function call() : block():block():Object {
        return \-> \-> outer._testField
      }
    }
    var blk = c1.call()
    _testField = "foo"
    var blk2 = blk()
    assertEquals( "foo", blk2() )
    _testField = "bar"
    assertEquals( "bar", blk2() )
  }
  
  function testNestedOuterPointerFeildWriting() {
    var c1 = new Callable() {
      override function call() : block():block() {
        return \-> \-> { outer._testField = "bar" }
      }
    }
    var blk = c1.call()
    _testField = "foo"
    var blk2 = blk()
    blk2()
    assertEquals( "bar", _testField  )
    _testField = "foo"
    blk2()
    assertEquals( "bar", _testField )
  }
  
  function testTripleNestedOuterPointer() {
    var c1 = new Callable() {
      override function call() : block():block():block():Object {
        return \-> \-> \-> outer
      }
    }
    var blk = c1.call()
    var blk2 = blk()
    var blk3 = blk2()
    assertEquals( this, blk3() )
  }


  function testTripleNestedOuterPointerMethodInvocation1() {
    var c1 = new Callable() {
      override function call() : block():block():block():int {
        return \-> \-> \-> outer.intFunction()
      }
    }
    var blk = c1.call()
    var blk2 = blk()
    var blk3 = blk2()
    assertEquals( 42, blk3() )
  }
  
  function testTripleNestedOuterPointerMethodInvocation2() {
    var c1 = new Callable() {
      override function call() : block():block():block():Object {
        return \-> \-> \-> outer.refFunction()
      }
    }
    var blk = c1.call()
    var blk2 = blk()
    var blk3 = blk2()
    assertEquals( "foo", blk3() )
  }
  
  function testTripleNestedOuterPointerPropertyInvocation1() {
    var c1 = new Callable() {
      override function call() : block():block():block():int {
        return \-> \-> \-> outer.IntProperty
      }
    }
    var blk = c1.call()
    var blk2 = blk()
    var blk3 = blk2()
    assertEquals( 42, blk3() )
  }
  
  function testTripleNestedOuterPointerPropertyInvocation2() {
    var c1 = new Callable() {
      override function call() : block():block():block():Object {
        return \-> \-> \-> outer.RefProperty
      }
    }
    var blk = c1.call()
    var blk2 = blk()
    var blk3 = blk2()
    assertEquals( "foo", blk3() )
  }
  
  function testTripleNestedOuterPointerPropertyWriting() {
    var c1 = new Callable() {
      override function call() : block():block():block() {
        return \-> \-> \-> { outer.WritableProp = "bar" }
      }
    }
    var blk = c1.call()
    WritableProp = "foo"
    var blk2 = blk()
    var blk3 = blk2()
    blk3()
    assertEquals( "bar", WritableProp  )
    WritableProp = "foo"
    blk3()
    assertEquals( "bar", WritableProp )
  }
  
  function testTripleNestedOuterPointerFeildReading() {
    var c1 = new Callable() {
      override function call() : block():block():block():Object {
        return \-> \-> \-> outer._testField
      }
    }
    var blk = c1.call()
    _testField = "foo"
    var blk2 = blk()
    var blk3 = blk2()
    assertEquals( "foo", blk3() )
    _testField = "bar"
    assertEquals( "bar", blk3() )
  }
  
  function testTripleNestedOuterPointerFeildWriting() {
    var c1 = new Callable() {
      override function call() : block():block():block() {
        return \-> \-> \-> { outer._testField = "bar" }
      }
    }
    var blk = c1.call()
    _testField = "foo"
    var blk2 = blk()
    var blk3 = blk2()
    blk3()
    assertEquals( "bar", _testField  )
    _testField = "foo"
    blk3()
    assertEquals( "bar", _testField )
  }

  //=========================================================
  // implicit outer pointer tests
  //=========================================================

  function testImplicitOuterPointerMethodInvocation1() {
    var c1 = new Callable() {
      override function call() : block():int {
        return \-> intFunction()
      }
    }
    var blk = c1.call()
    assertEquals( 42, blk() )
  }

  function testImplicitOuterPointerMethodInvocation2() {
    var c1 = new Callable() {
      override function call() : block():Object {
        return \-> refFunction()
      }
    }
    var blk = c1.call()
    assertEquals( "foo", blk() )
  }

  function testImplicitOuterPointerPropertyInvocation1() {
    var c1 = new Callable() {
      override function call() : block():int {
        return \-> IntProperty
      }
    }
    var blk = c1.call()
    assertEquals( 42, blk() )
  }

  function testImplicitOuterPointerPropertyInvocation2() {
    var c1 = new Callable() {
      override function call() : block():Object {
        return \-> RefProperty
      }
    }
    var blk = c1.call()
    assertEquals( "foo", blk() )
  }

  function testImplicitOuterPointerPropertyWriting() {
    WritableProp = "foo"
    var c1 = new Callable() {
      override function call() : block() {
        return \-> { WritableProp = "bar" }
      }
    }
    var blk = c1.call()
    blk()
    assertEquals( "bar", WritableProp  )
    WritableProp = "foo"
    blk()
    assertEquals( "bar", WritableProp )
  }

  function testImplicitOuterPointerFeildReading() {
    _testField = "foo"
    var c1 = new Callable() {
      override function call() : block():Object {
        return \-> _testField
      }
    }
    var blk = c1.call()
    assertEquals( "foo", blk() )
    _testField = "bar"
    assertEquals( "bar", blk() )
  }

  function testImplicitOuterPointerFeildWriting() {
    _testField = "foo"
    var c1 = new Callable() {
      override function call() : block() {
        return \-> { _testField = "bar" }
      }
    }
    var blk = c1.call()
    blk()
    assertEquals( "bar", _testField  )
    _testField = "foo"
    blk()
    assertEquals( "bar", _testField )
  }
 
  function testNestedImplicitOuterPointerMethodInvocation1() {
    var c1 = new Callable() {
      override function call() : block():block():int {
        return \->\-> intFunction()
      }
    }
    var blk = c1.call()
    var blk2 = blk()
    assertEquals( 42, blk2() )
  }

  function testNestedImplicitOuterPointerMethodInvocation2() {
    var c1 = new Callable() {
      override function call() : block():block():Object {
        return \-> \-> refFunction()
      }
    }
    var blk = c1.call()
    var blk2 = blk()
    assertEquals( "foo", blk2() )
  }
  
  function testNestedImplicitOuterPointerPropertyInvocation1() {
    var c1 = new Callable() {
      override function call() : block():block():int {
        return \-> \-> IntProperty
      }
    }
    var blk = c1.call()
    var blk2 = blk()
    assertEquals( 42, blk2() )
  }
  
  function testNestedImplicitOuterPointerPropertyInvocation2() {
    var c1 = new Callable() {
      override function call() : block():block():Object {
        return \-> \-> RefProperty
      }
    }
    var blk = c1.call()
    var blk2 = blk()
    assertEquals( "foo", blk2() )
  }

  function testNestedImplicitOuterPointerPropertyWriting() {
    var c1 = new Callable() {
      override function call() : block():block() {
        return \-> \-> { WritableProp = "bar" }
      }
    }
    var blk = c1.call()
    WritableProp = "foo"
    var blk2 = blk()
    blk2()
    assertEquals( "bar", WritableProp  )
    WritableProp = "foo"
    blk2()
    assertEquals( "bar", WritableProp )
  }
  
  function testNestedImplicitOuterPointerFeildReading() {
    var c1 = new Callable() {
      override function call() : block():block():Object {
        return \-> \-> _testField
      }
    }
    var blk = c1.call()
    _testField = "foo"
    var blk2 = blk()
    assertEquals( "foo", blk2() )
    _testField = "bar"
    assertEquals( "bar", blk2() )
  }
  
  function testNestedImplicitOuterPointerFeildWriting() {
    var c1 = new Callable() {
      override function call() : block():block() {
        return \-> \-> { _testField = "bar" }
      }
    }
    var blk = c1.call()
    _testField = "foo"
    var blk2 = blk()
    blk2()
    assertEquals( "bar", _testField  )
    _testField = "foo"
    blk2()
    assertEquals( "bar", _testField )
  }

  function testTripleNestedImplicitOuterPointerMethodInvocation1() {
    var c1 = new Callable() {
      override function call() : block():block():block():int {
        return \-> \-> \-> intFunction()
      }
    }
    var blk = c1.call()
    var blk2 = blk()
    var blk3 = blk2()
    assertEquals( 42, blk3() )
  }
  
  function testTripleNestedImplicitOuterPointerMethodInvocation2() {
    var c1 = new Callable() {
      override function call() : block():block():block():Object {
        return \-> \-> \-> refFunction()
      }
    }
    var blk = c1.call()
    var blk2 = blk()
    var blk3 = blk2()
    assertEquals( "foo", blk3() )
  }
  
  function testTripleNestedImplicitOuterPointerPropertyInvocation1() {
    var c1 = new Callable() {
      override function call() : block():block():block():int {
        return \-> \-> \-> IntProperty
      }
    }
    var blk = c1.call()
    var blk2 = blk()
    var blk3 = blk2()
    assertEquals( 42, blk3() )
  }
  
  function testTripleNestedImplicitOuterPointerPropertyInvocation2() {
    var c1 = new Callable() {
      override function call() : block():block():block():Object {
        return \-> \-> \-> RefProperty
      }
    }
    var blk = c1.call()
    var blk2 = blk()
    var blk3 = blk2()
    assertEquals( "foo", blk3() )
  }
  
  function testTripleNestedImplicitOuterPointerPropertyWriting() {
    var c1 = new Callable() {
      override function call() : block():block():block() {
        return \-> \-> \-> { WritableProp = "bar" }
      }
    }
    var blk = c1.call()
    WritableProp = "foo"
    var blk2 = blk()
    var blk3 = blk2()
    blk3()
    assertEquals( "bar", WritableProp  )
    WritableProp = "foo"
    blk3()
    assertEquals( "bar", WritableProp )
  }
  
  function testTripleNestedImplicitOuterPointerFeildReading() {
    var c1 = new Callable() {
      override function call() : block():block():block():Object {
        return \-> \-> \-> _testField
      }
    }
    var blk = c1.call()
    _testField = "foo"
    var blk2 = blk()
    var blk3 = blk2()
    assertEquals( "foo", blk3() )
    _testField = "bar"
    assertEquals( "bar", blk3() )
  }
  
  function testTripleNestedImplicitOuterPointerFeildWriting() {
    var c1 = new Callable() {
      override function call() : block():block():block() {
        return \-> \-> \-> { _testField = "bar" }
      }
    }
    var blk = c1.call()
    _testField = "foo"
    var blk2 = blk()
    var blk3 = blk2()
    blk3()
    assertEquals( "bar", _testField  )
    _testField = "foo"
    blk3()
    assertEquals( "bar", _testField )
  }

  //=========================================================
  // 'outer outer' pointer tests
  //=========================================================

  function testOuterOuterPointerOneDeep() {
    var c1 = new Callable() {
      override function call() : Callable<block():Object> {
        return new Callable<block():Object>() {
          override function call() : block():Object {
            return \-> outer.outer
          }
        }
      }
    }
    var c2 = c1.call()
    var blk = c2.call()
    assertEquals( this, blk() )
  }


  function testInnerOuterPointerOneDeep() {
    var c1 = new Callable() {
      override function call() : Callable<block():Object> {
        return new Callable<block():Object>() {
          override function call() : block():Object {
            return \-> outer
          }
        }
      }
    }
    var c2 = c1.call()
    var blk = c2.call()
    assertEquals( c1, blk() )
  }

  function testOuterOuterPointerMethodInvocation1() {
    var c1 = new Callable() {
      override function call() : Callable<block():int> {
        return new Callable<block():int>() {
          override function call() : block():int {
            return \-> outer.outer.intFunction()
          }
        }
      }
    }
    var c2 = c1.call()
    var blk = c2.call()
    assertEquals( 42, blk() )
  }

  function testOuterOuterPointerMethodInvocation2() {
    var c1 = new Callable() {
      override function call() : Callable<block():Object> {
        return new Callable<block():Object>() {
          override function call() : block():Object {
            return \-> outer.outer.refFunction()
          }
        }
      }
    }
    var c2 = c1.call()
    var blk = c2.call()
    assertEquals( "foo", blk() )
  }
  
  function testOuterOuterPointerPropertyInvocation1() {
    var c1 = new Callable() {
      override function call() : Callable<block():int> {
        return new Callable<block():int>() {
          override function call() : block():int {
            return \-> outer.outer.IntProperty
          }
        }
      }
    }
    var c2 = c1.call()
    var blk = c2.call()
    assertEquals( 42, blk() )
  }

  function testOuterOuterPointerPropertyInvocation2() {
    var c1 = new Callable() {
      override function call() : Callable<block():Object> {
        return new Callable<block():Object>() {
          override function call() : block():Object {
            return \-> outer.outer.RefProperty
          }
        }
      }
    }
    var c2 = c1.call()
    var blk = c2.call()
    assertEquals( "foo", blk() )
  }

  function testOuterOuterPointerPropertyWriting() {
    WritableProp = "foo"
    var c1 = new Callable() {
      override function call() : Callable<block()> {
        return new Callable<block()>() {
          override function call() : block() {
            return \-> { outer.outer.WritableProp = "bar" }  
          }
        }
      }
    }
    var c2 = c1.call()
    var blk = c2.call()
    blk()
    assertEquals( "bar", WritableProp  )
    WritableProp = "foo"
    blk()
    assertEquals( "bar", WritableProp )
  }

  function testOuterOuterPointerFieldReading() {
    WritableProp = "foo"
    var c1 = new Callable() {
      override function call() : Callable<block():Object> {
        return new Callable<block():Object>() {
          override function call() : block():Object {
            return \-> outer.outer._testField  
          }
        }
      }
    }
    var c2 = c1.call()
    var blk = c2.call()
    assertEquals( "foo", blk() )
    _testField = "bar"
    assertEquals( "bar", blk() )
  }

  function testOuterOuterPointerFieldWriting() {
    WritableProp = "foo"
    var c1 = new Callable() {
      override function call() : Callable<block()> {
        return new Callable<block()>() {
          override function call() : block() {
            return \-> { outer.outer._testField = "bar" }  
          }
        }
      }
    }
    var c2 = c1.call()
    var blk = c2.call()
    blk()
    assertEquals( "bar", _testField  )
    _testField = "foo"
    blk()
    assertEquals( "bar", _testField )
  }
     
  function testNestedOuterOuterPointer() {
    var c1 = new Callable() {
      override function call() : Callable<block():block():Object> {
        return new Callable<block():block():Object> (){
          override function call() : block():block():Object {
            return \->\-> outer.outer
          }  
        }
      }
    }
    var c2 = c1.call()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( this, blk2() )
  }
  
  function testBlockThenNestedOuterInnerPointer() {
    var c1 = new Callable() {
      override function call() : Callable<block():block():Object> {
        return new Callable<block():block():Object> (){
          override function call() : block():block():Object {
            return \->\-> outer
          }  
        }
      }
    }
    var c2 = c1.call()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( c1, blk2() )
  }

  function testNestedOuterOuterPointerMethodInvocation1() {
    var c1 = new Callable() {
      override function call() : Callable<block():block():int> {
        return new Callable<block():block():int> (){
          override function call() : block():block():int {
            return \->\-> outer.outer.intFunction()
          }  
        }
      }
    }
    var c2 = c1.call()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( 42, blk2() )
  }

  function testNestedOuterOuterPointerMethodInvocation2() {
    var c1 = new Callable() {
      override function call() : Callable<block():block():Object> {
        return new Callable<block():block():Object> (){
          override function call() : block():block():Object {
            return \->\-> outer.outer.refFunction()
          }  
        }
      }
    }
    var c2 = c1.call()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( "foo", blk2() )
  }
  
  function testNestedOuterOuterPointerPropertyInvocation1() {
    var c1 = new Callable() {
      override function call() : Callable<block():block():int> {
        return new Callable<block():block():int> (){
          override function call() : block():block():int {
            return \->\-> outer.outer.IntProperty
          }  
        }
      }
    }
    var c2 = c1.call()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( 42, blk2() )
  }

  function testNestedOuterOuterPointerPropertyInvocation2() {
    var c1 = new Callable() {
      override function call() : Callable<block():block():Object> {
        return new Callable<block():block():Object> (){
          override function call() : block():block():Object {
            return \->\-> outer.outer.RefProperty
          }  
        }
      }
    }
    var c2 = c1.call()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( "foo", blk2() )
  }

  function testNestedOuterOuterPointerPropertyWriting() {
    var c1 = new Callable() {
      override function call() : Callable<block():block()> {
        return new Callable<block():block()> (){
          override function call() : block():block() {
            return \-> \-> { outer.outer.WritableProp = "bar" }
          }  
        }
      }
    }
    var c2 = c1.call()
    var blk = c2.call()
    WritableProp = "foo"
    var blk2 = blk()
    blk2()
    assertEquals( "bar", WritableProp  )
    WritableProp = "foo"
    blk2()
    assertEquals( "bar", WritableProp )
  }

  function testNestedOuterOuterPointerFieldWriting() {
    var c1 = new Callable() {
      override function call() : Callable<block():block()> {
        return new Callable<block():block()> (){
          override function call() : block():block() {
            return \-> \-> { outer.outer._testField = "bar" }
          }  
        }
      }
    }
    var c2 = c1.call()
    var blk = c2.call()
    _testField = "foo"
    var blk2 = blk()
    blk2()
    assertEquals( "bar", _testField  )
    WritableProp = "foo"
    blk2()
    assertEquals( "bar", _testField )
  }

  function testTripleNestedOuterOuterPointer() {
    var c1 = new Callable() {
      override function call() : Callable<block():block():block():Object> {
        return new Callable<block():block():block():Object> (){
          override function call() : block():block():block():Object {
            return \-> \-> \-> outer.outer
          }
        }
      }
    }
    var c2 = c1.call()
    var blk = c2.call()
    var blk2 = blk()
    var blk3 = blk2()
    assertEquals( this, blk3() )
  }

  function testTripleNestedOuterInnerPointer() {
    var c1 = new Callable() {
      override function call() : Callable<block():block():block():Object> {
        return new Callable<block():block():block():Object> (){
          override function call() : block():block():block():Object {
            return \-> \-> \-> outer
          }
        }
      }
    }
    var c2 = c1.call()
    var blk = c2.call()
    var blk2 = blk()
    var blk3 = blk2()
    assertEquals( c1, blk3() )
  }

  //=========================================================
  // implicit 'outer outer' pointer tests
  //=========================================================

  function testImplicitOuterOuterPointerMethodInvocation1() {
    var c1 = new Callable() {
      override function call() : Callable<block():int> {
        return new Callable<block():int>() {
          override function call() : block():int {
            return \-> intFunction()
          }
        }
      }
    }
    var c2 = c1.call()
    var blk = c2.call()
    assertEquals( 42, blk() )
  }

  function testImplicitOuterOuterPointerMethodInvocation2() {
    var c1 = new Callable() {
      override function call() : Callable<block():Object> {
        return new Callable<block():Object>() {
          override function call() : block():Object {
            return \-> refFunction()
          }
        }
      }
    }
    var c2 = c1.call()
    var blk = c2.call()
    assertEquals( "foo", blk() )
  }

  function testImplicitOuterOuterPointerPropertyInvocation1() {
    var c1 = new Callable() {
      override function call() : Callable<block():int> {
        return new Callable<block():int>() {
          override function call() : block():int {
            return \-> IntProperty
          }
        }
      }
    }
    var c2 = c1.call()
    var blk = c2.call()
    assertEquals( 42, blk() )
  }

  function testImplicitOuterOuterPointerPropertyInvocation2() {
    var c1 = new Callable() {
      override function call() : Callable<block():Object> {
        return new Callable<block():Object>() {
          override function call() : block():Object {
            return \-> RefProperty
          }
        }
      }
    }
    var c2 = c1.call()
    var blk = c2.call()
    assertEquals( "foo", blk() )
  }

  function testImplicitOuterOuterPointerPropertyWriting() {
    WritableProp = "foo"
    var c1 = new Callable() {
      override function call() : Callable<block()> {
        return new Callable<block()>() {
          override function call() : block() {
            return \-> { WritableProp = "bar" }
          }
        }
      }
    }
    var c2 = c1.call()
    var blk = c2.call()
    blk()
    assertEquals( "bar", WritableProp  )
    WritableProp = "foo"
    blk()
    assertEquals( "bar", WritableProp )
  }

  function testImplicitOuterOuterPointerFieldReading() {
    WritableProp = "foo"
    var c1 = new Callable() {
      override function call() : Callable<block():Object> {
        return new Callable<block():Object>() {
          override function call() : block():Object {
            return \-> _testField
          }
        }
      }
    }
    var c2 = c1.call()
    var blk = c2.call()
    assertEquals( "foo", blk() )
    _testField = "bar"
    assertEquals( "bar", blk() )
  }

  function testImplicitOuterOuterPointerFieldWriting() {
    WritableProp = "foo"
    var c1 = new Callable() {
      override function call() : Callable<block()> {
        return new Callable<block()>() {
          override function call() : block() {
            return \-> { _testField = "bar" }
          }
        }
      }
    }
    var c2 = c1.call()
    var blk = c2.call()
    blk()
    assertEquals( "bar", _testField  )
    _testField = "foo"
    blk()
    assertEquals( "bar", _testField )
  }

  function testNestedImplicitOuterOuterPointerMethodInvocation1() {
    var c1 = new Callable() {
      override function call() : Callable<block():block():int> {
        return new Callable<block():block():int> (){
          override function call() : block():block():int {
            return \->\-> intFunction()
          }
        }
      }
    }
    var c2 = c1.call()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( 42, blk2() )
  }

  function testNestedImplicitOuterOuterPointerMethodInvocation2() {
    var c1 = new Callable() {
      override function call() : Callable<block():block():Object> {
        return new Callable<block():block():Object> (){
          override function call() : block():block():Object {
            return \->\-> refFunction()
          }
        }
      }
    }
    var c2 = c1.call()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( "foo", blk2() )
  }

  function testNestedImplicitOuterOuterPointerPropertyInvocation1() {
    var c1 = new Callable() {
      override function call() : Callable<block():block():int> {
        return new Callable<block():block():int> (){
          override function call() : block():block():int {
            return \->\-> IntProperty
          }
        }
      }
    }
    var c2 = c1.call()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( 42, blk2() )
  }

  function testNestedImplicitOuterOuterPointerPropertyInvocation2() {
    var c1 = new Callable() {
      override function call() : Callable<block():block():Object> {
        return new Callable<block():block():Object> (){
          override function call() : block():block():Object {
            return \->\-> RefProperty
          }
        }
      }
    }
    var c2 = c1.call()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( "foo", blk2() )
  }

  function testNestedImplicitOuterOuterPointerPropertyWriting() {
    var c1 = new Callable() {
      override function call() : Callable<block():block()> {
        return new Callable<block():block()> (){
          override function call() : block():block() {
            return \-> \-> { WritableProp = "bar" }
          }
        }
      }
    }
    var c2 = c1.call()
    var blk = c2.call()
    WritableProp = "foo"
    var blk2 = blk()
    blk2()
    assertEquals( "bar", WritableProp  )
    WritableProp = "foo"
    blk2()
    assertEquals( "bar", WritableProp )
  }

  function testNestedImplicitOuterOuterPointerFieldWriting() {
    var c1 = new Callable() {
      override function call() : Callable<block():block()> {
        return new Callable<block():block()> (){
          override function call() : block():block() {
            return \-> \-> { _testField = "bar" }
          }
        }
      }
    }
    var c2 = c1.call()
    var blk = c2.call()
    _testField = "foo"
    var blk2 = blk()
    blk2()
    assertEquals( "bar", _testField  )
    WritableProp = "foo"
    blk2()
    assertEquals( "bar", _testField )
  }

  //=========================================================
  // block then 'outer outer' pointer tests
  //=========================================================

  function testBlockThenOuterOuterPointerOneDeep() {
    var b1 = \-> new Callable() {
      override function call() : Callable<block():Object> {
        return new Callable<block():Object>() {
          override function call() : block():Object {
            return \-> outer.outer
          }
        }
      }
    }
    var c1 = b1()
    var c2 = c1.call()
    var blk = c2.call()
    assertEquals( this, blk() )
  }

  function testBlockThenInnerOuterPointerOneDeep() {
    var b1 = \-> new Callable() {
      override function call() : Callable<block():Object> {
        return new Callable<block():Object>() {
          override function call() : block():Object {
            return \-> outer
          }
        }
      }
    }
    var c1 = b1()
    var c2 = c1.call()
    var blk = c2.call()
    assertEquals( c1, blk() )
  }

  function testBlockThenOuterOuterPointerMethodInvocation1() {
    var b1 = \-> new Callable() {
      override function call() : Callable<block():int> {
        return new Callable<block():int>() {
          override function call() : block():int {
            return \-> outer.outer.intFunction()
          }
        }
      }
    }
    var c1 = b1()
    var c2 = c1.call()
    var blk = c2.call()
    assertEquals( 42, blk() )
  }

  function testBlockThenOuterOuterPointerMethodInvocation2() {
    var b1 = \-> new Callable() {
      override function call() : Callable<block():Object> {
        return new Callable<block():Object>() {
          override function call() : block():Object {
            return \-> outer.outer.refFunction()
          }
        }
      }
    }
    var c1 = b1()
    var c2 = c1.call()
    var blk = c2.call()
    assertEquals( "foo", blk() )
  }

  function testBlockThenOuterOuterPointerPropertyInvocation1() {
    var b1 = \-> new Callable() {
      override function call() : Callable<block():int> {
        return new Callable<block():int>() {
          override function call() : block():int {
            return \-> outer.outer.IntProperty
          }
        }
      }
    }
    var c1 = b1()
    var c2 = c1.call()
    var blk = c2.call()
    assertEquals( 42, blk() )
  }

  function testBlockThenOuterOuterPointerPropertyInvocation2() {
    var b1 = \-> new Callable() {
      override function call() : Callable<block():Object> {
        return new Callable<block():Object>() {
          override function call() : block():Object {
            return \-> outer.outer.RefProperty
          }
        }
      }
    }
    var c1 = b1()
    var c2 = c1.call()
    var blk = c2.call()
    assertEquals( "foo", blk() )
  }

  function testBlockThenOuterOuterPointerPropertyWriting() {
    WritableProp = "foo"
    var b1 = \-> new Callable() {
      override function call() : Callable<block()> {
        return new Callable<block()>() {
          override function call() : block() {
            return \-> { outer.outer.WritableProp = "bar" }
          }
        }
      }
    }
    var c1 = b1()
    var c2 = c1.call()
    var blk = c2.call()
    blk()
    assertEquals( "bar", WritableProp  )
    WritableProp = "foo"
    blk()
    assertEquals( "bar", WritableProp )
  }

  function testBlockThenOuterOuterPointerFieldReading() {
    WritableProp = "foo"
    var b1 = \-> new Callable() {
      override function call() : Callable<block():Object> {
        return new Callable<block():Object>() {
          override function call() : block():Object {
            return \-> outer.outer._testField
          }
        }
      }
    }
    var c1 = b1()
    var c2 = c1.call()
    var blk = c2.call()
    assertEquals( "foo", blk() )
    _testField = "bar"
    assertEquals( "bar", blk() )
  }

  function testBlockThenOuterOuterPointerFieldWriting() {
    WritableProp = "foo"
    var b1 = \-> new Callable() {
      override function call() : Callable<block()> {
        return new Callable<block()>() {
          override function call() : block() {
            return \-> { outer.outer._testField = "bar" }
          }
        }
      }
    }
    var c1 = b1()
    var c2 = c1.call()
    var blk = c2.call()
    blk()
    assertEquals( "bar", _testField  )
    _testField = "foo"
    blk()
    assertEquals( "bar", _testField )
  }

  function testNestedBlockThenOuterOuterPointer() {
    var b1 = \-> new Callable() {
      override function call() : Callable<block():block():Object> {
        return new Callable<block():block():Object> (){
          override function call() : block():block():Object {
            return \->\-> outer.outer
          }
        }
      }
    }
    var c1 = b1()
    var c2 = c1.call()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( this, blk2() )
  }

  function testNestedBlockThenOuterInnerPointer() {
    var b1 = \-> new Callable() {
      override function call() : Callable<block():block():Object> {
        return new Callable<block():block():Object> (){
          override function call() : block():block():Object {
            return \->\-> outer
          }
        }
      }
    }
    var c1 = b1()
    var c2 = c1.call()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( c1, blk2() )
  }

  function testNestedBlockThenOuterOuterPointerMethodInvocation1() {
    var b1 = \-> new Callable() {
      override function call() : Callable<block():block():int> {
        return new Callable<block():block():int> (){
          override function call() : block():block():int {
            return \->\-> outer.outer.intFunction()
          }
        }
      }
    }
    var c1 = b1()
    var c2 = c1.call()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( 42, blk2() )
  }

  function testNestedBlockThenOuterOuterPointerMethodInvocation2() {
    var b1 = \-> new Callable() {
      override function call() : Callable<block():block():Object> {
        return new Callable<block():block():Object> (){
          override function call() : block():block():Object {
            return \->\-> outer.outer.refFunction()
          }
        }
      }
    }
    var c1 = b1()
    var c2 = c1.call()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( "foo", blk2() )
  }

  function testNestedBlockThenOuterOuterPointerPropertyInvocation1() {
    var b1 = \-> new Callable() {
      override function call() : Callable<block():block():int> {
        return new Callable<block():block():int> (){
          override function call() : block():block():int {
            return \->\-> outer.outer.IntProperty
          }
        }
      }
    }
    var c1 = b1()
    var c2 = c1.call()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( 42, blk2() )
  }

  function testNestedBlockThenOuterOuterPointerPropertyInvocation2() {
    var b1 = \-> new Callable() {
      override function call() : Callable<block():block():Object> {
        return new Callable<block():block():Object> (){
          override function call() : block():block():Object {
            return \->\-> outer.outer.RefProperty
          }
        }
      }
    }
    var c1 = b1()
    var c2 = c1.call()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( "foo", blk2() )
  }

  function testNestedBlockThenOuterOuterPointerPropertyWriting() {
    var b1 = \-> new Callable() {
      override function call() : Callable<block():block()> {
        return new Callable<block():block()> (){
          override function call() : block():block() {
            return \-> \-> { outer.outer.WritableProp = "bar" }
          }
        }
      }
    }
    var c1 = b1()
    var c2 = c1.call()
    var blk = c2.call()
    WritableProp = "foo"
    var blk2 = blk()
    blk2()
    assertEquals( "bar", WritableProp  )
    WritableProp = "foo"
    blk2()
    assertEquals( "bar", WritableProp )
  }

  function testNestedBlockThenOuterOuterPointerFieldWriting() {
    var b1 = \-> new Callable() {
      override function call() : Callable<block():block()> {
        return new Callable<block():block()> (){
          override function call() : block():block() {
            return \-> \-> { outer.outer._testField = "bar" }
          }
        }
      }
    }
    var c1 = b1()
    var c2 = c1.call()
    var blk = c2.call()
    _testField = "foo"
    var blk2 = blk()
    blk2()
    assertEquals( "bar", _testField  )
    WritableProp = "foo"
    blk2()
    assertEquals( "bar", _testField )
  }

  function testTripleNestedBlockThenOuterOuterPointer() {
    var b1 = \-> new Callable() {
      override function call() : Callable<block():block():block():Object> {
        return new Callable<block():block():block():Object> (){
          override function call() : block():block():block():Object {
            return \-> \-> \-> outer.outer
          }
        }
      }
    }
    var c1 = b1()
    var c2 = c1.call()
    var blk = c2.call()
    var blk2 = blk()
    var blk3 = blk2()
    assertEquals( this, blk3() )
  }

  function testTripleNestedBlockOuterInnerPointer() {
    var b1 = \-> new Callable() {
      override function call() : Callable<block():block():block():Object> {
        return new Callable<block():block():block():Object> (){
          override function call() : block():block():block():Object {
            return \-> \-> \-> outer
          }
        }
      }
    }
    var c1 = b1()
    var c2 = c1.call()
    var blk = c2.call()
    var blk2 = blk()
    var blk3 = blk2()
    assertEquals( c1, blk3() )
  }

  //=========================================================
  // implicit block then 'outer outer' pointer tests
  //=========================================================

  function testImplicitBlockThenOuterOuterPointerMethodInvocation1() {
    var b1 = \-> new Callable() {
      override function call() : Callable<block():int> {
        return new Callable<block():int>() {
          override function call() : block():int {
            return \-> intFunction()
          }
        }
      }
    }
    var c1 = b1()
    var c2 = c1.call()
    var blk = c2.call()
    assertEquals( 42, blk() )
  }

  function testImplicitBlockThenOuterOuterPointerMethodInvocation2() {
    var b1 = \-> new Callable() {
      override function call() : Callable<block():Object> {
        return new Callable<block():Object>() {
          override function call() : block():Object {
            return \-> refFunction()
          }
        }
      }
    }
    var c1 = b1()
    var c2 = c1.call()
    var blk = c2.call()
    assertEquals( "foo", blk() )
  }

  function testImplicitBlockThenOuterOuterPointerPropertyInvocation1() {
    var b1 = \-> new Callable() {
      override function call() : Callable<block():int> {
        return new Callable<block():int>() {
          override function call() : block():int {
            return \-> IntProperty
          }
        }
      }
    }
    var c1 = b1()
    var c2 = c1.call()
    var blk = c2.call()
    assertEquals( 42, blk() )
  }

  function testImplicitBlockThenOuterOuterPointerPropertyInvocation2() {
    var b1 = \-> new Callable() {
      override function call() : Callable<block():Object> {
        return new Callable<block():Object>() {
          override function call() : block():Object {
            return \-> RefProperty
          }
        }
      }
    }
    var c1 = b1()
    var c2 = c1.call()
    var blk = c2.call()
    assertEquals( "foo", blk() )
  }

  function testImplicitBlockThenOuterOuterPointerPropertyWriting() {
    WritableProp = "foo"
    var b1 = \-> new Callable() {
      override function call() : Callable<block()> {
        return new Callable<block()>() {
          override function call() : block() {
            return \-> { WritableProp = "bar" }
          }
        }
      }
    }
    var c1 = b1()
    var c2 = c1.call()
    var blk = c2.call()
    blk()
    assertEquals( "bar", WritableProp  )
    WritableProp = "foo"
    blk()
    assertEquals( "bar", WritableProp )
  }

  function testImplicitBlockThenOuterOuterPointerFieldReading() {
    WritableProp = "foo"
    var b1 = \-> new Callable() {
      override function call() : Callable<block():Object> {
        return new Callable<block():Object>() {
          override function call() : block():Object {
            return \-> _testField
          }
        }
      }
    }
    var c1 = b1()
    var c2 = c1.call()
    var blk = c2.call()
    assertEquals( "foo", blk() )
    _testField = "bar"
    assertEquals( "bar", blk() )
  }

  function testImplicitBlockThenOuterOuterPointerFieldWriting() {
    WritableProp = "foo"
    var b1 = \-> new Callable() {
      override function call() : Callable<block()> {
        return new Callable<block()>() {
          override function call() : block() {
            return \-> { _testField = "bar" }
          }
        }
      }
    }
    var c1 = b1()
    var c2 = c1.call()
    var blk = c2.call()
    blk()
    assertEquals( "bar", _testField  )
    _testField = "foo"
    blk()
    assertEquals( "bar", _testField )
  }

  function testNestedImplicitBlockThenOuterOuterPointerMethodInvocation1() {
    var b1 = \-> new Callable() {
      override function call() : Callable<block():block():int> {
        return new Callable<block():block():int> (){
          override function call() : block():block():int {
            return \->\-> intFunction()
          }
        }
      }
    }
    var c1 = b1()
    var c2 = c1.call()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( 42, blk2() )
  }

  function testNestedImplicitBlockThenOuterOuterPointerMethodInvocation2() {
    var b1 = \-> new Callable() {
      override function call() : Callable<block():block():Object> {
        return new Callable<block():block():Object> (){
          override function call() : block():block():Object {
            return \->\-> refFunction()
          }
        }
      }
    }
    var c1 = b1()
    var c2 = c1.call()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( "foo", blk2() )
  }

  function testNestedImplicitBlockThenOuterOuterPointerPropertyInvocation1() {
    var b1 = \-> new Callable() {
      override function call() : Callable<block():block():int> {
        return new Callable<block():block():int> (){
          override function call() : block():block():int {
            return \->\-> IntProperty
          }
        }
      }
    }
    var c1 = b1()
    var c2 = c1.call()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( 42, blk2() )
  }

  function testNestedImplicitBlockThenOuterOuterPointerPropertyInvocation2() {
    var b1 = \-> new Callable() {
      override function call() : Callable<block():block():Object> {
        return new Callable<block():block():Object> (){
          override function call() : block():block():Object {
            return \->\-> RefProperty
          }
        }
      }
    }
    var c1 = b1()
    var c2 = c1.call()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( "foo", blk2() )
  }

  function testNestedImplicitBlockThenOuterOuterPointerPropertyWriting() {
    var b1 = \-> new Callable() {
      override function call() : Callable<block():block()> {
        return new Callable<block():block()> (){
          override function call() : block():block() {
            return \-> \-> { WritableProp = "bar" }
          }
        }
      } 
    }
    var c1 = b1()
    var c2 = c1.call()
    var blk = c2.call()
    WritableProp = "foo"
    var blk2 = blk() 
    blk2()
    assertEquals( "bar", WritableProp  )
    WritableProp = "foo"
    blk2()
    assertEquals( "bar", WritableProp )
  }
 
  function testNestedImplicitBlockThenOuterOuterPointerFieldWriting() {
    var b1 = \-> new Callable() {
      override function call() : Callable<block():block()> {
        return new Callable<block():block()> (){
          override function call() : block():block() {
            return \-> \-> { _testField = "bar" }
          }
        }
      }
    }
    var c1 = b1()
    var c2 = c1.call()
    var blk = c2.call()
    _testField = "foo"
    var blk2 = blk()
    blk2()
    assertEquals( "bar", _testField  )
    WritableProp = "foo"
    blk2()
    assertEquals( "bar", _testField )
  }

  //=========================================================
  // block then 'outer then block then outer' pointer tests
  //=========================================================

  function testBlockThenOuterThenBlockOuterPointerOneDeep() {
    var b1 = \-> new Callable() {
      override function call() : block():Callable<block():Object> {
        return \-> new Callable<block():Object>() {
          override function call() : block():Object {
            return \-> outer.outer
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk = c2.call()
    assertEquals( this, blk() )
  }

  function testBlockThenBlockThenInnerOuterPointerOneDeep() {
    var b1 = \-> new Callable() {
      override function call() : block():Callable<block():Object> {
        return \-> new Callable<block():Object>() {
          override function call() : block():Object {
            return \-> outer
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk = c2.call()
    assertEquals( c1, blk() )
  }

  function testBlockThenOuterThenBlockOuterPointerMethodInvocation1() {
    var b1 = \-> new Callable() {
      override function call() : block():Callable<block():int> {
        return \-> new Callable<block():int>() {
          override function call() : block():int {
            return \-> outer.outer.intFunction()
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk = c2.call()
    assertEquals( 42, blk() )
  }

  function testBlockThenOuterThenBlockOuterPointerMethodInvocation2() {
    var b1 = \-> new Callable() {
      override function call() : block():Callable<block():Object> {
        return \-> new Callable<block():Object>() {
          override function call() : block():Object {
            return \-> outer.outer.refFunction()
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk = c2.call()
    assertEquals( "foo", blk() )
  }

  function testBlockThenOuterThenBlockOuterPointerPropertyInvocation1() {
    var b1 = \-> new Callable() {
      override function call() : block():Callable<block():int> {
        return \-> new Callable<block():int>() {
          override function call() : block():int {
            return \-> outer.outer.IntProperty
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk = c2.call()
    assertEquals( 42, blk() )
  }

  function testBlockThenOuterThenBlockOuterPointerPropertyInvocation2() {
    var b1 = \-> new Callable() {
      override function call() : block():Callable<block():Object> {
        return \-> new Callable<block():Object>() {
          override function call() : block():Object {
            return \-> outer.outer.RefProperty
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk = c2.call()
    assertEquals( "foo", blk() )
  }

  function testBlockThenOuterThenBlockOuterPointerPropertyWriting() {
    WritableProp = "foo"
    var b1 = \-> new Callable() {
      override function call() : block():Callable<block()> {
        return \-> new Callable<block()>() {
          override function call() : block() {
            return \-> { outer.outer.WritableProp = "bar" }
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk = c2.call()
    blk()
    assertEquals( "bar", WritableProp  )
    WritableProp = "foo"
    blk()
    assertEquals( "bar", WritableProp )
  }

  function testBlockThenOuterThenBlockOuterPointerFieldReading() {
    WritableProp = "foo"
    var b1 = \-> new Callable() {
      override function call() : block():Callable<block():Object> {
        return \-> new Callable<block():Object>() {
          override function call() : block():Object {
            return \-> outer.outer._testField
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk = c2.call()
    assertEquals( "foo", blk() )
    _testField = "bar"
    assertEquals( "bar", blk() )
  }

  function testBlockThenOuterThenBlockOuterPointerFieldWriting() {
    WritableProp = "foo"
    var b1 = \-> new Callable() {
      override function call() : block():Callable<block()> {
        return \-> new Callable<block()>() {
          override function call() : block() {
            return \-> { outer.outer._testField = "bar" }
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk = c2.call()
    blk()
    assertEquals( "bar", _testField  )
    _testField = "foo"
    blk()
    assertEquals( "bar", _testField )
  }

  function testNestedBlockThenOuterThenBlockOuterPointer() {
    var b1 = \-> new Callable() {
      override function call() : block():Callable<block():block():Object> {
        return \-> new Callable<block():block():Object> (){
          override function call() : block():block():Object {
            return \->\-> outer.outer
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( this, blk2() )
  }

  function testNestedBlockThenBlockThenOuterInnerPointer() {
    var b1 = \-> new Callable() {
      override function call() : block():Callable<block():block():Object> {
        return \-> new Callable<block():block():Object> (){
          override function call() : block():block():Object {
            return \->\-> outer
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( c1, blk2() )
  }

  function testNestedBlockThenOuterThenBlockOuterPointerMethodInvocation1() {
    var b1 = \-> new Callable() {
      override function call() : block():Callable<block():block():int> {
        return \-> new Callable<block():block():int> (){
          override function call() : block():block():int {
            return \->\-> outer.outer.intFunction()
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( 42, blk2() )
  }

  function testNestedBlockThenOuterThenBlockOuterPointerMethodInvocation2() {
    var b1 = \-> new Callable() {
      override function call() : block():Callable<block():block():Object> {
        return \-> new Callable<block():block():Object> (){
          override function call() : block():block():Object {
            return \->\-> outer.outer.refFunction()
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( "foo", blk2() )
  }

  function testNestedBlockThenOuterThenBlockOuterPointerPropertyInvocation1() {
    var b1 = \-> new Callable() {
      override function call() : block():Callable<block():block():int> {
        return \-> new Callable<block():block():int> (){
          override function call() : block():block():int {
            return \->\-> outer.outer.IntProperty
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( 42, blk2() )
  }

  function testNestedBlockThenOuterThenBlockOuterPointerPropertyInvocation2() {
    var b1 = \-> new Callable() {
      override function call() : block():Callable<block():block():Object> {
        return \-> new Callable<block():block():Object> (){
          override function call() : block():block():Object {
            return \->\-> outer.outer.RefProperty
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( "foo", blk2() )
  }

  function testNestedBlockThenOuterThenBlockOuterPointerPropertyWriting() {
    var b1 = \-> new Callable() {
      override function call() : block():Callable<block():block()> {
        return \-> new Callable<block():block()> (){
          override function call() : block():block() {
            return \-> \-> { outer.outer.WritableProp = "bar" }
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk = c2.call()
    WritableProp = "foo"
    var blk2 = blk()
    blk2()
    assertEquals( "bar", WritableProp  )
    WritableProp = "foo"
    blk2()
    assertEquals( "bar", WritableProp )
  }

  function testNestedBlockThenOuterThenBlockOuterPointerFieldWriting() {
    var b1 = \-> new Callable() {
      override function call() : block():Callable<block():block()> {
        return \-> new Callable<block():block()> (){
          override function call() : block():block() {
            return \-> \-> { outer.outer._testField = "bar" }
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk = c2.call()
    _testField = "foo"
    var blk2 = blk()
    blk2()
    assertEquals( "bar", _testField  )
    WritableProp = "foo"
    blk2()
    assertEquals( "bar", _testField )
  }

  function testTripleNestedBlockThenOuterThenBlockOuterPointer() {
    var b1 = \-> new Callable() {
      override function call() : block():Callable<block():block():block():Object> {
        return \-> new Callable<block():block():block():Object> (){
          override function call() : block():block():block():Object {
            return \-> \-> \-> outer.outer
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk = c2.call()
    var blk2 = blk()
    var blk3 = blk2()
    assertEquals( this, blk3() )
  }

  function testTripleNestedBlockBlockThenOuterInnerPointer() {
    var b1 = \-> new Callable() {
      override function call() : block():Callable<block():block():block():Object> {
        return \-> new Callable<block():block():block():Object> (){
          override function call() : block():block():block():Object {
            return \-> \-> \-> outer
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk = c2.call()
    var blk2 = blk()
    var blk3 = blk2()
    assertEquals( c1, blk3() )
  }

  //=========================================================
  // implicit block then 'outer outer' pointer tests
  //=========================================================

  function testImplicitBlockThenOuterThenBlockOuterPointerMethodInvocation1() {
    var b1 = \-> new Callable() {
      override function call() : block():Callable<block():int> {
        return \-> new Callable<block():int>() {
          override function call() : block():int {
            return \-> intFunction()
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk = c2.call()
    assertEquals( 42, blk() )
  }

  function testImplicitBlockThenOuterThenBlockOuterPointerMethodInvocation2() {
    var b1 = \-> new Callable() {
      override function call() : block():Callable<block():Object> {
        return \-> new Callable<block():Object>() {
          override function call() : block():Object {
            return \-> refFunction()
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk = c2.call()
    assertEquals( "foo", blk() )
  }

  function testImplicitBlockThenOuterThenBlockOuterPointerPropertyInvocation1() {
    var b1 = \-> new Callable() {
      override function call() : block():Callable<block():int> {
        return \-> new Callable<block():int>() {
          override function call() : block():int {
            return \-> IntProperty
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk = c2.call()
    assertEquals( 42, blk() )
  }

  function testImplicitBlockThenOuterThenBlockOuterPointerPropertyInvocation2() {
    var b1 = \-> new Callable() {
      override function call() : block():Callable<block():Object> {
        return \-> new Callable<block():Object>() {
          override function call() : block():Object {
            return \-> RefProperty
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk = c2.call()
    assertEquals( "foo", blk() )
  }

  function testImplicitBlockThenOuterThenBlockOuterPointerPropertyWriting() {
    WritableProp = "foo"
    var b1 = \-> new Callable() {
      override function call() : block():Callable<block()> {
        return \-> new Callable<block()>() {
          override function call() : block() {
            return \-> { WritableProp = "bar" }
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk = c2.call()
    blk()
    assertEquals( "bar", WritableProp  )
    WritableProp = "foo"
    blk()
    assertEquals( "bar", WritableProp )
  }

  function testImplicitBlockThenOuterThenBlockOuterPointerFieldReading() {
    WritableProp = "foo"
    var b1 = \-> new Callable() {
      override function call() : block():Callable<block():Object> {
        return \-> new Callable<block():Object>() {
          override function call() : block():Object {
            return \-> _testField
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk = c2.call()
    assertEquals( "foo", blk() )
    _testField = "bar"
    assertEquals( "bar", blk() )
  }

  function testImplicitBlockThenOuterThenBlockOuterPointerFieldWriting() {
    WritableProp = "foo"
    var b1 = \-> new Callable() {
      override function call() : block():Callable<block()> {
        return \-> new Callable<block()>() {
          override function call() : block() {
            return \-> { _testField = "bar" }
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk = c2.call()
    blk()
    assertEquals( "bar", _testField  )
    _testField = "foo"
    blk()
    assertEquals( "bar", _testField )
  }

  function testNestedImplicitBlockThenOuterThenBlockOuterPointerMethodInvocation1() {
    var b1 = \-> new Callable() {
      override function call() : block():Callable<block():block():int> {
        return \-> new Callable<block():block():int> (){
          override function call() : block():block():int {
            return \->\-> intFunction()
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( 42, blk2() )
  }

  function testNestedImplicitBlockThenOuterThenBlockOuterPointerMethodInvocation2() {
    var b1 = \-> new Callable() {
      override function call() : block():Callable<block():block():Object> {
        return \-> new Callable<block():block():Object> (){
          override function call() : block():block():Object {
            return \->\-> refFunction()
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( "foo", blk2() )
  }

  function testNestedImplicitBlockThenOuterThenBlockOuterPointerPropertyInvocation1() {
    var b1 = \-> new Callable() {
      override function call() : block():Callable<block():block():int> {
        return \-> new Callable<block():block():int> (){
          override function call() : block():block():int {
            return \->\-> IntProperty
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( 42, blk2() )
  }

  function testNestedImplicitBlockThenOuterThenBlockOuterPointerPropertyInvocation2() {
    var b1 = \-> new Callable() {
      override function call() : block():Callable<block():block():Object> {
        return \-> new Callable<block():block():Object> (){
          override function call() : block():block():Object {
            return \->\-> RefProperty
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( "foo", blk2() )
  }

  function testNestedImplicitBlockThenOuterThenBlockOuterPointerPropertyWriting() {
    var b1 = \-> new Callable() {
      override function call() : block():Callable<block():block()> {
        return \-> new Callable<block():block()> (){
          override function call() : block():block() {
            return \-> \-> { WritableProp = "bar" }
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk = c2.call()
    WritableProp = "foo"
    var blk2 = blk()
    blk2()
    assertEquals( "bar", WritableProp  )
    WritableProp = "foo"
    blk2()
    assertEquals( "bar", WritableProp )
  }

  function testNestedImplicitBlockThenOuterThenBlockOuterPointerFieldWriting() {
    var b1 = \-> new Callable() {
      override function call() : block():Callable<block():block()> {
        return \-> new Callable<block():block()> (){
          override function call() : block():block() {
            return \-> \-> { _testField = "bar" }
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk = c2.call()
    _testField = "foo"
    var blk2 = blk()
    blk2()
    assertEquals( "bar", _testField  )
    WritableProp = "foo"
    blk2()
    assertEquals( "bar", _testField )
  }

  //=========================================================
  // block then 'outer then block then block then outer' pointer tests
  //=========================================================

  function testBlockThenOuterThenBlockThenBlockOuterPointerOneDeep() {
    var b1 = \-> new Callable() {
      override function call() : block():block():Callable<block():Object> {
        return \-> \-> new Callable<block():Object>() {
          override function call() : block():Object {
            return \-> outer.outer
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk = c2.call()
    assertEquals( this, blk() )
  }

  function testBlockThenBlockThenBlockThenInnerOuterPointerOneDeep() {
    var b1 = \-> new Callable() {
      override function call() : block():block():Callable<block():Object> {
        return \-> \-> new Callable<block():Object>() {
          override function call() : block():Object {
            return \-> outer
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk = c2.call()
    assertEquals( c1, blk() )
  }

  function testBlockThenOuterThenBlockThenBlockOuterPointerMethodInvocation1() {
    var b1 = \-> new Callable() {
      override function call() : block():block():Callable<block():int> {
        return \-> \-> new Callable<block():int>() {
          override function call() : block():int {
            return \-> outer.outer.intFunction()
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk = c2.call()
    assertEquals( 42, blk() )
  }

  function testBlockThenOuterThenBlockThenBlockOuterPointerMethodInvocation2() {
    var b1 = \-> new Callable() {
      override function call() : block():block():Callable<block():Object> {
        return \-> \-> new Callable<block():Object>() {
          override function call() : block():Object {
            return \-> outer.outer.refFunction()
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk = c2.call()
    assertEquals( "foo", blk() )
  }

  function testBlockThenOuterThenBlockThenBlockOuterPointerPropertyInvocation1() {
    var b1 = \-> new Callable() {
      override function call() : block():block():Callable<block():int> {
        return \-> \-> new Callable<block():int>() {
          override function call() : block():int {
            return \-> outer.outer.IntProperty
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk = c2.call()
    assertEquals( 42, blk() )
  }

  function testBlockThenOuterThenBlockThenBlockOuterPointerPropertyInvocation2() {
    var b1 = \-> new Callable() {
      override function call() : block():block():Callable<block():Object> {
        return \-> \-> new Callable<block():Object>() {
          override function call() : block():Object {
            return \-> outer.outer.RefProperty
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk = c2.call()
    assertEquals( "foo", blk() )
  }

  function testBlockThenOuterThenBlockThenBlockOuterPointerPropertyWriting() {
    WritableProp = "foo"
    var b1 = \-> new Callable() {
      override function call() : block():block():Callable<block()> {
        return \-> \-> new Callable<block()>() {
          override function call() : block() {
            return \-> { outer.outer.WritableProp = "bar" }
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk = c2.call()
    blk()
    assertEquals( "bar", WritableProp  )
    WritableProp = "foo"
    blk()
    assertEquals( "bar", WritableProp )
  }

  function testBlockThenOuterThenBlockThenBlockOuterPointerFieldReading() {
    WritableProp = "foo"
    var b1 = \-> new Callable() {
      override function call() : block():block():Callable<block():Object> {
        return \-> \-> new Callable<block():Object>() {
          override function call() : block():Object {
            return \-> outer.outer._testField
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk = c2.call()
    assertEquals( "foo", blk() )
    _testField = "bar"
    assertEquals( "bar", blk() )
  }

  function testBlockThenOuterThenBlockThenBlockOuterPointerFieldWriting() {
    WritableProp = "foo"
    var b1 = \-> new Callable() {
      override function call() : block():block():Callable<block()> {
        return \-> \-> new Callable<block()>() {
          override function call() : block() {
            return \-> { outer.outer._testField = "bar" }
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk = c2.call()
    blk()
    assertEquals( "bar", _testField  )
    _testField = "foo"
    blk()
    assertEquals( "bar", _testField )
  }

  function testNestedBlockThenOuterThenBlockThenBlockOuterPointer() {
    var b1 = \-> new Callable() {
      override function call() : block():block():Callable<block():block():Object> {
        return \-> \-> new Callable<block():block():Object> (){
          override function call() : block():block():Object {
            return \->\-> outer.outer
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( this, blk2() )
  }

  function testNestedBlockThenBlockThenBlockThenOuterInnerPointer() {
    var b1 = \-> new Callable() {
      override function call() : block():block():Callable<block():block():Object> {
        return \-> \-> new Callable<block():block():Object> (){
          override function call() : block():block():Object {
            return \->\-> outer
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( c1, blk2() )
  }

  function testNestedBlockThenOuterThenBlockThenBlockOuterPointerMethodInvocation1() {
    var b1 = \-> new Callable() {
      override function call() : block():block():Callable<block():block():int> {
        return \-> \-> new Callable<block():block():int> (){
          override function call() : block():block():int {
            return \->\-> outer.outer.intFunction()
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( 42, blk2() )
  }

  function testNestedBlockThenOuterThenBlockThenBlockOuterPointerMethodInvocation2() {
    var b1 = \-> new Callable() {
      override function call() : block():block():Callable<block():block():Object> {
        return \-> \-> new Callable<block():block():Object> (){
          override function call() : block():block():Object {
            return \->\-> outer.outer.refFunction()
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( "foo", blk2() )
  }

  function testNestedBlockThenOuterThenBlockThenBlockOuterPointerPropertyInvocation1() {
    var b1 = \-> new Callable() {
      override function call() : block():block():Callable<block():block():int> {
        return \-> \-> new Callable<block():block():int> (){
          override function call() : block():block():int {
            return \->\-> outer.outer.IntProperty
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( 42, blk2() )
  }

  function testNestedBlockThenOuterThenBlockThenBlockOuterPointerPropertyInvocation2() {
    var b1 = \-> new Callable() {
      override function call() : block():block():Callable<block():block():Object> {
        return \-> \-> new Callable<block():block():Object> (){
          override function call() : block():block():Object {
            return \->\-> outer.outer.RefProperty
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( "foo", blk2() )
  }

  function testNestedBlockThenOuterThenBlockThenBlockOuterPointerPropertyWriting() {
    var b1 = \-> new Callable() {
      override function call() : block():block():Callable<block():block()> {
        return \-> \-> new Callable<block():block()> (){
          override function call() : block():block() {
            return \-> \-> { outer.outer.WritableProp = "bar" }
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk = c2.call()
    WritableProp = "foo"
    var blk2 = blk()
    blk2()
    assertEquals( "bar", WritableProp  )
    WritableProp = "foo"
    blk2()
    assertEquals( "bar", WritableProp )
  }

  function testNestedBlockThenOuterThenBlockThenBlockOuterPointerFieldWriting() {
    var b1 = \-> new Callable() {
      override function call() : block():block():Callable<block():block()> {
        return \-> \-> new Callable<block():block()> (){
          override function call() : block():block() {
            return \-> \-> { outer.outer._testField = "bar" }
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk = c2.call()
    _testField = "foo"
    var blk2 = blk()
    blk2()
    assertEquals( "bar", _testField  )
    WritableProp = "foo"
    blk2()
    assertEquals( "bar", _testField )
  }

  function testTripleNestedBlockThenOuterThenBlockThenBlockOuterPointer() {
    var b1 = \-> new Callable() {
      override function call() : block():block():Callable<block():block():block():Object> {
        return \-> \-> new Callable<block():block():block():Object> (){
          override function call() : block():block():block():Object {
            return \-> \-> \-> outer.outer
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk = c2.call()
    var blk2 = blk()
    var blk3 = blk2()
    assertEquals( this, blk3() )
  }

  function testTripleNestedBlockBlockBlockThenOuterInnerPointer() {
    var b1 = \-> new Callable() {
      override function call() : block():block():Callable<block():block():block():Object> {
        return \-> \-> new Callable<block():block():block():Object> (){
          override function call() : block():block():block():Object {
            return \-> \-> \-> outer
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk = c2.call()
    var blk2 = blk()
    var blk3 = blk2()
    assertEquals( c1, blk3() )
  }

  //=========================================================
  // implicit block then 'outer then block then block outer' pointer tests
  //=========================================================

  function testImplicitBlockThenOuterThenBlockThenBlockOuterPointerMethodInvocation1() {
    var b1 = \-> new Callable() {
      override function call() : block():block():Callable<block():int> {
        return \-> \-> new Callable<block():int>() {
          override function call() : block():int {
            return \-> intFunction()
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk = c2.call()
    assertEquals( 42, blk() )
  }

  function testImplicitBlockThenOuterThenBlockThenBlockOuterPointerMethodInvocation2() {
    var b1 = \-> new Callable() {
      override function call() : block():block():Callable<block():Object> {
        return \-> \-> new Callable<block():Object>() {
          override function call() : block():Object {
            return \-> refFunction()
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk = c2.call()
    assertEquals( "foo", blk() )
  }

  function testImplicitBlockThenOuterThenBlockThenBlockOuterPointerPropertyInvocation1() {
    var b1 = \-> new Callable() {
      override function call() : block():block():Callable<block():int> {
        return \-> \-> new Callable<block():int>() {
          override function call() : block():int {
            return \-> IntProperty
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk = c2.call()
    assertEquals( 42, blk() )
  }

  function testImplicitBlockThenOuterThenBlockThenBlockOuterPointerPropertyInvocation2() {
    var b1 = \-> new Callable() {
      override function call() : block():block():Callable<block():Object> {
        return \-> \-> new Callable<block():Object>() {
          override function call() : block():Object {
            return \-> RefProperty
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk = c2.call()
    assertEquals( "foo", blk() )
  }

  function testImplicitBlockThenOuterThenBlockThenBlockOuterPointerPropertyWriting() {
    WritableProp = "foo"
    var b1 = \-> new Callable() {
      override function call() : block():block():Callable<block()> {
        return \-> \-> new Callable<block()>() {
          override function call() : block() {
            return \-> { WritableProp = "bar" }
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk = c2.call()
    blk()
    assertEquals( "bar", WritableProp  )
    WritableProp = "foo"
    blk()
    assertEquals( "bar", WritableProp )
  }

  function testImplicitBlockThenOuterThenBlockThenBlockOuterPointerFieldReading() {
    WritableProp = "foo"
    var b1 = \-> new Callable() {
      override function call() : block():block():Callable<block():Object> {
        return \-> \-> new Callable<block():Object>() {
          override function call() : block():Object {
            return \-> _testField
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk = c2.call()
    assertEquals( "foo", blk() )
    _testField = "bar"
    assertEquals( "bar", blk() )
  }

  function testImplicitBlockThenOuterThenBlockThenBlockOuterPointerFieldWriting() {
    WritableProp = "foo"
    var b1 = \-> new Callable() {
      override function call() : block():block():Callable<block()> {
        return \-> \-> new Callable<block()>() {
          override function call() : block() {
            return \-> { _testField = "bar" }
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk = c2.call()
    blk()
    assertEquals( "bar", _testField  )
    _testField = "foo"
    blk()
    assertEquals( "bar", _testField )
  }

  function testNestedImplicitBlockThenOuterThenBlockThenBlockOuterPointerMethodInvocation1() {
    var b1 = \-> new Callable() {
      override function call() : block():block():Callable<block():block():int> {
        return \-> \-> new Callable<block():block():int> (){
          override function call() : block():block():int {
            return \->\-> intFunction()
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( 42, blk2() )
  }

  function testNestedImplicitBlockThenOuterThenBlockThenBlockOuterPointerMethodInvocation2() {
    var b1 = \-> new Callable() {
      override function call() : block():block():Callable<block():block():Object> {
        return \-> \-> new Callable<block():block():Object> (){
          override function call() : block():block():Object {
            return \->\-> refFunction()
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( "foo", blk2() )
  }

  function testNestedImplicitBlockThenOuterThenBlockThenBlockOuterPointerPropertyInvocation1() {
    var b1 = \-> new Callable() {
      override function call() : block():block():Callable<block():block():int> {
        return \-> \-> new Callable<block():block():int> (){
          override function call() : block():block():int {
            return \->\-> IntProperty
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( 42, blk2() )
  }

  function testNestedImplicitBlockThenOuterThenBlockThenBlockOuterPointerPropertyInvocation2() {
    var b1 = \-> new Callable() {
      override function call() : block():block():Callable<block():block():Object> {
        return \-> \-> new Callable<block():block():Object> (){
          override function call() : block():block():Object {
            return \->\-> RefProperty
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( "foo", blk2() )
  }

  function testNestedImplicitBlockThenOuterThenBlockThenBlockOuterPointerPropertyWriting() {
    var b1 = \-> new Callable() {
      override function call() : block():block():Callable<block():block()> {
        return \-> \-> new Callable<block():block()> (){
          override function call() : block():block() {
            return \-> \-> { WritableProp = "bar" }
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk = c2.call()
    WritableProp = "foo"
    var blk2 = blk()
    blk2()
    assertEquals( "bar", WritableProp  )
    WritableProp = "foo"
    blk2()
    assertEquals( "bar", WritableProp )
  }

  function testNestedImplicitBlockThenOuterThenBlockThenBlockOuterPointerFieldWriting() {
    var b1 = \-> new Callable() {
      override function call() : block():block():Callable<block():block()> {
        return \-> \-> new Callable<block():block()> (){
          override function call() : block():block() {
            return \-> \-> { _testField = "bar" }
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk = c2.call()
    _testField = "foo"
    var blk2 = blk()
    blk2()
    assertEquals( "bar", _testField  )
    WritableProp = "foo"
    blk2()
    assertEquals( "bar", _testField )
  }

  // Dup mark -

  //=========================================================
  // 'outer outer' pointer tests
  //=========================================================

  function testOuterAnonPointerMethodInvocation1() {
    var c1 = new Callable() {

      var _i_testField : String as _i_WritableProp

      property get _i_IntProperty() : int {
        return 42
      }

      property get _i_RefProperty() : String {
        return "foo"
      }

      function _i_intFunction() : int {
        return 42
      }

      function _i_refFunction() : String {
        return "foo"
      }

      override function call() : Callable<block():int> {
        return new Callable<block():int>() {
          override function call() : block():int {
            return \-> outer._i_intFunction()
          }
        }
      }
    }
    var c2 = c1.call()
    var blk = c2.call()
    assertEquals( 42, blk() )
  }

  function testOuterAnonPointerMethodInvocation2() {
    var c1 = new Callable() {

      var _i_testField : String as _i_WritableProp

      property get _i_IntProperty() : int {
        return 42
      }

      property get _i_RefProperty() : String {
        return "foo"
      }

      function _i_intFunction() : int {
        return 42
      }

      function _i_refFunction() : String {
        return "foo"
      }

      override function call() : Callable<block():Object> {
        return new Callable<block():Object>() {
          override function call() : block():Object {
            return \-> outer._i_refFunction()
          }
        }
      }
    }
    var c2 = c1.call()
    var blk = c2.call()
    assertEquals( "foo", blk() )
  }

  function testOuterAnonPointerPropertyInvocation1() {
    var c1 = new Callable() {

       var _i_testField : String as _i_WritableProp

       property get _i_IntProperty() : int {
         return 42
       }

       property get _i_RefProperty() : String {
         return "foo"
       }

       function _i_intFunction() : int {
         return 42
       }

       function _i_refFunction() : String {
         return "foo"
       }

         override function call() : Callable<block():int> {
        return new Callable<block():int>() {
          override function call() : block():int {
            return \-> outer._i_IntProperty
          }
        }
      }
    }
    var c2 = c1.call()
    var blk = c2.call()
    assertEquals( 42, blk() )
  }

  function testOuterAnonPointerPropertyInvocation2() {
    var c1 = new Callable() {

       var _i_testField : String as _i_WritableProp

       property get _i_IntProperty() : int {
         return 42
       }

       property get _i_RefProperty() : String {
         return "foo"
       }

       function _i_intFunction() : int {
         return 42
       }

       function _i_refFunction() : String {
         return "foo"
       }

      override function call() : Callable<block():Object> {
        return new Callable<block():Object>() {
          override function call() : block():Object {
            return \-> outer._i_RefProperty
          }
        }
      }
    }
    var c2 = c1.call()
    var blk = c2.call()
    assertEquals( "foo", blk() )
  }

  function testOuterAnonPointerPropertyWriting() {
    var c1 = new Callable() {

       var _i_testField : String as _i_WritableProp

       property get _i_IntProperty() : int {
         return 42
       }

       property get _i_RefProperty() : String {
         return "foo"
       }

       function _i_intFunction() : int {
         return 42
       }

       function _i_refFunction() : String {
         return "foo"
       }

      override function call() : Callable<block()> {
        return new Callable<block()>() {
          override function call() : block() {
            return \-> { outer._i_WritableProp = "bar" }
          }
        }
      }
    }
    c1._i_WritableProp = "foo"
    var c2 = c1.call()
    var blk = c2.call()
    blk()
    assertEquals( "bar", c1._i_WritableProp  )
    c1._i_WritableProp = "foo"
    blk()
    assertEquals( "bar", c1._i_WritableProp )
  }

  function testOuterAnonPointerFieldReading() {
    var c1 = new Callable() {

       var _i_testField : String as _i_WritableProp

       property get _i_IntProperty() : int {
         return 42
       }

       property get _i_RefProperty() : String {
         return "foo"
       }

       function _i_intFunction() : int {
         return 42
       }

       function _i_refFunction() : String {
         return "foo"
       }

      override function call() : Callable<block():Object> {
        return new Callable<block():Object>() {
          override function call() : block():Object {
            return \->_i_testField
          }
        }
      }
    }
    c1._i_WritableProp = "foo"
    var c2 = c1.call()
    var blk = c2.call()
    assertEquals( "foo", blk() )
    c1._i_testField = "bar"
    assertEquals( "bar", blk() )
  }

  function testOuterAnonPointerFieldWriting() {
    var c1 = new Callable() {

       var _i_testField : String as _i_WritableProp

       property get _i_IntProperty() : int {
         return 42
       }

       property get _i_RefProperty() : String {
         return "foo"
       }

       function _i_intFunction() : int {
         return 42
       }

       function _i_refFunction() : String {
         return "foo"
       }

      override function call() : Callable<block()> {
        return new Callable<block()>() {
          override function call() : block() {
            return \-> {_i_testField = "bar" }
          }
        }
      }
    }
    c1._i_WritableProp = "foo"
    var c2 = c1.call()
    var blk = c2.call()
    blk()
    assertEquals( "bar", c1._i_testField  )
    c1._i_testField = "foo"
    blk()
    assertEquals( "bar", c1._i_testField )
  }

  function testNestedOuterAnonPointerMethodInvocation1() {
    var c1 = new Callable() {

           var _i_testField : String as _i_WritableProp

           property get _i_IntProperty() : int {
             return 42
           }

           property get _i_RefProperty() : String {
             return "foo"
           }

           function _i_intFunction() : int {
             return 42
           }

           function _i_refFunction() : String {
             return "foo"
           }


      override function call() : Callable<block():block():int> {
        return new Callable<block():block():int> (){
          override function call() : block():block():int {
            return \->\-> outer._i_intFunction()
          }
        }
      }
    }
    var c2 = c1.call()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( 42, blk2() )
  }

  function testNestedOuterAnonPointerMethodInvocation2() {
    var c1 = new Callable() {

     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : Callable<block():block():Object> {
        return new Callable<block():block():Object> (){
          override function call() : block():block():Object {
            return \->\-> outer._i_refFunction()
          }
        }
      }
    }
    var c2 = c1.call()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( "foo", blk2() )
  }

  function testNestedOuterAnonPointerPropertyInvocation1() {
    var c1 = new Callable() {

     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }


      override function call() : Callable<block():block():int> {
        return new Callable<block():block():int> (){
          override function call() : block():block():int {
            return \->\-> outer._i_IntProperty
          }
        }
      }
    }
    var c2 = c1.call()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( 42, blk2() )
  }

  function testNestedOuterAnonPointerPropertyInvocation2() {
    var c1 = new Callable() {

     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }


      override function call() : Callable<block():block():Object> {
        return new Callable<block():block():Object> (){
          override function call() : block():block():Object {
            return \->\-> outer._i_RefProperty
          }
        }
      }
    }
    var c2 = c1.call()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( "foo", blk2() )
  }

  function testNestedOuterAnonPointerPropertyWriting() {
    var c1 = new Callable() {

     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }


      override function call() : Callable<block():block()> {
        return new Callable<block():block()> (){
          override function call() : block():block() {
            return \-> \-> { outer._i_WritableProp = "bar" }
          }
        }
      }
    }
    var c2 = c1.call()
    var blk = c2.call()
    c1._i_WritableProp = "foo"
    var blk2 = blk()
    blk2()
    assertEquals( "bar", c1._i_WritableProp  )
    c1._i_WritableProp = "foo"
    blk2()
    assertEquals( "bar", c1._i_WritableProp )
  }

  function testNestedOuterAnonPointerFieldWriting() {
    var c1 = new Callable() {

     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }


      override function call() : Callable<block():block()> {
        return new Callable<block():block()> (){
          override function call() : block():block() {
            return \-> \-> {_i_testField = "bar" }
          }
        }
      }
    }
    var c2 = c1.call()
    var blk = c2.call()
    c1._i_testField = "foo"
    var blk2 = blk()
    blk2()
    assertEquals( "bar", c1._i_testField  )
    c1._i_WritableProp = "foo"
    blk2()
    assertEquals( "bar", c1._i_testField )
  }

  function testTripleNestedOuterAnonPointer() {
    var c1 = new Callable() {

     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }


      override function call() : Callable<block():block():block():Object> {
        return new Callable<block():block():block():Object> (){
          override function call() : block():block():block():Object {
            return \-> \-> \-> outer.outer
          }
        }
      }
    }
    var c2 = c1.call()
    var blk = c2.call()
    var blk2 = blk()
    var blk3 = blk2()
    assertEquals( this, blk3() )
  }

  //=========================================================
  // implicit 'outer outer' pointer tests
  //=========================================================

  function testImplicitOuterAnonPointerMethodInvocation1() {
    var c1 = new Callable() {

     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }


      override function call() : Callable<block():int> {
        return new Callable<block():int>() {
          override function call() : block():int {
            return \-> _i_intFunction()
          }
        }
      }
    }
    var c2 = c1.call()
    var blk = c2.call()
    assertEquals( 42, blk() )
  }

  function testImplicitOuterAnonPointerMethodInvocation2() {
    var c1 = new Callable() {

     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }


      override function call() : Callable<block():Object> {
        return new Callable<block():Object>() {
          override function call() : block():Object {
            return \-> _i_refFunction()
          }
        }
      }
    }
    var c2 = c1.call()
    var blk = c2.call()
    assertEquals( "foo", blk() )
  }

  function testImplicitOuterAnonPointerPropertyInvocation1() {
    var c1 = new Callable() {

     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }


      override function call() : Callable<block():int> {
        return new Callable<block():int>() {
          override function call() : block():int {
            return \-> _i_IntProperty
          }
        }
      }
    }
    var c2 = c1.call()
    var blk = c2.call()
    assertEquals( 42, blk() )
  }

  function testImplicitOuterAnonPointerPropertyInvocation2() {
    var c1 = new Callable() {

     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }


      override function call() : Callable<block():Object> {
        return new Callable<block():Object>() {
          override function call() : block():Object {
            return \-> _i_RefProperty
          }
        }
      }
    }
    var c2 = c1.call()
    var blk = c2.call()
    assertEquals( "foo", blk() )
  }

  function testImplicitOuterAnonPointerPropertyWriting() {
    var c1 = new Callable() {

     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }


      override function call() : Callable<block()> {
        return new Callable<block()>() {
          override function call() : block() {
            return \-> { _i_WritableProp = "bar" }
          }
        }
      }
    }
    c1._i_WritableProp = "foo"
    var c2 = c1.call()
    var blk = c2.call()
    blk()
    assertEquals( "bar", c1._i_WritableProp  )
    c1._i_WritableProp = "foo"
    blk()
    assertEquals( "bar", c1._i_WritableProp )
  }

  function testImplicitOuterAnonPointerFieldReading() {

    var c1 = new Callable() {

     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }


      override function call() : Callable<block():Object> {
        return new Callable<block():Object>() {
          override function call() : block():Object {
            return \-> _i_testField
          }
        }
      }
    }
    c1._i_WritableProp = "foo"
    var c2 = c1.call()
    var blk = c2.call()
    assertEquals( "foo", blk() )
    c1._i_testField = "bar"
    assertEquals( "bar", blk() )
  }

  function testImplicitOuterAnonPointerFieldWriting() {
    var c1 = new Callable() {

     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }


      override function call() : Callable<block()> {
        return new Callable<block()>() {
          override function call() : block() {
            return \-> { _i_testField = "bar" }
          }
        }
      }
    }
    c1._i_WritableProp = "foo"
    var c2 = c1.call()
    var blk = c2.call()
    blk()
    assertEquals( "bar", c1._i_testField  )
    c1._i_testField = "foo"
    blk()
    assertEquals( "bar", c1._i_testField )
  }

  function testNestedImplicitOuterAnonPointerMethodInvocation1() {
    var c1 = new Callable() {

     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }


      override function call() : Callable<block():block():int> {
        return new Callable<block():block():int> (){
          override function call() : block():block():int {
            return \->\-> _i_intFunction()
          }
        }
      }
    }
    var c2 = c1.call()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( 42, blk2() )
  }

  function testNestedImplicitOuterAnonPointerMethodInvocation2() {
    var c1 = new Callable() {

     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }


      override function call() : Callable<block():block():Object> {
        return new Callable<block():block():Object> (){
          override function call() : block():block():Object {
            return \->\-> _i_refFunction()
          }
        }
      }
    }
    var c2 = c1.call()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( "foo", blk2() )
  }

  function testNestedImplicitOuterAnonPointerPropertyInvocation1() {
    var c1 = new Callable() {

     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }


      override function call() : Callable<block():block():int> {
        return new Callable<block():block():int> (){
          override function call() : block():block():int {
            return \->\-> _i_IntProperty
          }
        }
      }
    }
    var c2 = c1.call()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( 42, blk2() )
  }

  function testNestedImplicitOuterAnonPointerPropertyInvocation2() {
    var c1 = new Callable() {

     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }


      override function call() : Callable<block():block():Object> {
        return new Callable<block():block():Object> (){
          override function call() : block():block():Object {
            return \->\-> _i_RefProperty
          }
        }
      }
    }
    var c2 = c1.call()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( "foo", blk2() )
  }

  function testNestedImplicitOuterAnonPointerPropertyWriting() {
    var c1 = new Callable() {

     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }


      override function call() : Callable<block():block()> {
        return new Callable<block():block()> (){
          override function call() : block():block() {
            return \-> \-> { _i_WritableProp = "bar" }
          }
        }
      }
    }
    var c2 = c1.call()
    var blk = c2.call()
    c1._i_WritableProp = "foo"
    var blk2 = blk()
    blk2()
    assertEquals( "bar", c1._i_WritableProp  )
    c1._i_WritableProp = "foo"
    blk2()
    assertEquals( "bar", c1._i_WritableProp )
  }

  function testNestedImplicitOuterAnonPointerFieldWriting() {
    var c1 = new Callable() {

     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }


      override function call() : Callable<block():block()> {
        return new Callable<block():block()> (){
          override function call() : block():block() {
            return \-> \-> { _i_testField = "bar" }
          }
        }
      }
    }
    var c2 = c1.call()
    var blk = c2.call()
    c1._i_testField = "foo"
    var blk2 = blk()
    blk2()
    assertEquals( "bar", c1._i_testField  )
    c1._i_WritableProp = "foo"
    blk2()
    assertEquals( "bar", c1._i_testField )
  }

  //=========================================================
  // block then 'outer outer' pointer tests
  //=========================================================

  function testBlockThenOuterAnonPointerMethodInvocation1() {
    var b1 = \-> new Callable() {

     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }


      override function call() : Callable<block():int> {
        return new Callable<block():int>() {
          override function call() : block():int {
            return \-> outer._i_intFunction()
          }
        }
      }
    }
    var c1 = b1()
    var c2 = c1.call()
    var blk = c2.call()
    assertEquals( 42, blk() )
  }

  function testBlockThenOuterAnonPointerMethodInvocation2() {
    var b1 = \-> new Callable() {

     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }


      override function call() : Callable<block():Object> {
        return new Callable<block():Object>() {
          override function call() : block():Object {
            return \-> outer._i_refFunction()
          }
        }
      }
    }
    var c1 = b1()
    var c2 = c1.call()
    var blk = c2.call()
    assertEquals( "foo", blk() )
  }

  function testBlockThenOuterAnonPointerPropertyInvocation1() {
    var b1 = \-> new Callable() {

     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }


      override function call() : Callable<block():int> {
        return new Callable<block():int>() {
          override function call() : block():int {
            return \-> outer._i_IntProperty
          }
        }
      }
    }
    var c1 = b1()
    var c2 = c1.call()
    var blk = c2.call()
    assertEquals( 42, blk() )
  }

  function testBlockThenOuterAnonPointerPropertyInvocation2() {
    var b1 = \-> new Callable() {

     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }


      override function call() : Callable<block():Object> {
        return new Callable<block():Object>() {
          override function call() : block():Object {
            return \-> outer._i_RefProperty
          }
        }
      }
    }
    var c1 = b1()
    var c2 = c1.call()
    var blk = c2.call()
    assertEquals( "foo", blk() )
  }

  function testBlockThenOuterAnonPointerPropertyWriting() {
    var b1 = \-> new Callable() {

     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }


      override function call() : Callable<block()> {
        return new Callable<block()>() {
          override function call() : block() {
            return \-> { outer._i_WritableProp = "bar" }
          }
        }
      }
    }
    var c1 = b1()
    c1._i_WritableProp = "foo"
    var c2 = c1.call()
    var blk = c2.call()
    blk()
    assertEquals( "bar", c1._i_WritableProp  )
    c1._i_WritableProp = "foo"
    blk()
    assertEquals( "bar", c1._i_WritableProp )
  }

  function testBlockThenOuterAnonPointerFieldReading() {

    var b1 = \-> new Callable() {

     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }


      override function call() : Callable<block():Object> {
        return new Callable<block():Object>() {
          override function call() : block():Object {
            return \->_i_testField
          }
        }
      }
    }
    var c1 = b1()
    c1._i_WritableProp = "foo"
    var c2 = c1.call()
    var blk = c2.call()
    assertEquals( "foo", blk() )
    c1._i_testField = "bar"
    assertEquals( "bar", blk() )
  }

  function testBlockThenOuterAnonPointerFieldWriting() {

    var b1 = \-> new Callable() {

     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }


      override function call() : Callable<block()> {
        return new Callable<block()>() {
          override function call() : block() {
            return \-> {_i_testField = "bar" }
          }
        }
      }
    }
    var c1 = b1()
    c1._i_WritableProp = "foo"
    var c2 = c1.call()
    var blk = c2.call()
    blk()
    assertEquals( "bar", c1._i_testField  )
    c1._i_testField = "foo"
    blk()
    assertEquals( "bar", c1._i_testField )
  }

  function testNestedBlockThenOuterAnonPointerMethodInvocation1() {
    var b1 = \-> new Callable() {

     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }


      override function call() : Callable<block():block():int> {
        return new Callable<block():block():int> (){
          override function call() : block():block():int {
            return \->\-> outer._i_intFunction()
          }
        }
      }
    }
    var c1 = b1()
    var c2 = c1.call()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( 42, blk2() )
  }

  function testNestedBlockThenOuterAnonPointerMethodInvocation2() {
    var b1 = \-> new Callable() {

     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }


      override function call() : Callable<block():block():Object> {
        return new Callable<block():block():Object> (){
          override function call() : block():block():Object {
            return \->\-> outer._i_refFunction()
          }
        }
      }
    }
    var c1 = b1()
    var c2 = c1.call()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( "foo", blk2() )
  }

  function testNestedBlockThenOuterAnonPointerPropertyInvocation1() {
    var b1 = \-> new Callable() {

     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }


      override function call() : Callable<block():block():int> {
        return new Callable<block():block():int> (){
          override function call() : block():block():int {
            return \->\-> outer._i_IntProperty
          }
        }
      }
    }
    var c1 = b1()
    var c2 = c1.call()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( 42, blk2() )
  }

  function testNestedBlockThenOuterAnonPointerPropertyInvocation2() {
    var b1 = \-> new Callable() {

     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : Callable<block():block():Object> {
        return new Callable<block():block():Object> (){
          override function call() : block():block():Object {
            return \->\-> outer._i_RefProperty
          }
        }
      }
    }
    var c1 = b1()
    var c2 = c1.call()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( "foo", blk2() )
  }

  function testNestedBlockThenOuterAnonPointerPropertyWriting() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }


      override function call() : Callable<block():block()> {
        return new Callable<block():block()> (){
          override function call() : block():block() {
            return \-> \-> { outer._i_WritableProp = "bar" }
          }
        }
      }
    }
    var c1 = b1()
    var c2 = c1.call()
    var blk = c2.call()
    c1._i_WritableProp = "foo"
    var blk2 = blk()
    blk2()
    assertEquals( "bar", c1._i_WritableProp  )
    c1._i_WritableProp = "foo"
    blk2()
    assertEquals( "bar", c1._i_WritableProp )
  }

  function testNestedBlockThenOuterAnonPointerFieldWriting() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : Callable<block():block()> {
        return new Callable<block():block()> (){
          override function call() : block():block() {
            return \-> \-> {_i_testField = "bar" }
          }
        }
      }
    }
    var c1 = b1()
    var c2 = c1.call()
    var blk = c2.call()
    c1._i_testField = "foo"
    var blk2 = blk()
    blk2()
    assertEquals( "bar", c1._i_testField  )
    c1._i_WritableProp = "foo"
    blk2()
    assertEquals( "bar", c1._i_testField )
  }

  //=========================================================
  // implicit block then 'outer outer' pointer tests
  //=========================================================

  function testImplicitBlockThenOuterAnonPointerMethodInvocation1() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : Callable<block():int> {
        return new Callable<block():int>() {
          override function call() : block():int {
            return \-> intFunction()
          }
        }
      }
    }
    var c1 = b1()
    var c2 = c1.call()
    var blk = c2.call()
    assertEquals( 42, blk() )
  }

  function testImplicitBlockThenOuterAnonPointerMethodInvocation2() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : Callable<block():Object> {
        return new Callable<block():Object>() {
          override function call() : block():Object {
            return \-> refFunction()
          }
        }
      }
    }
    var c1 = b1()
    var c2 = c1.call()
    var blk = c2.call()
    assertEquals( "foo", blk() )
  }

  function testImplicitBlockThenOuterAnonPointerPropertyInvocation1() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : Callable<block():int> {
        return new Callable<block():int>() {
          override function call() : block():int {
            return \-> IntProperty
          }
        }
      }
    }
    var c1 = b1()
    var c2 = c1.call()
    var blk = c2.call()
    assertEquals( 42, blk() )
  }

  function testImplicitBlockThenOuterAnonPointerPropertyInvocation2() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : Callable<block():Object> {
        return new Callable<block():Object>() {
          override function call() : block():Object {
            return \-> RefProperty
          }
        }
      }
    }
    var c1 = b1()
    var c2 = c1.call()
    var blk = c2.call()
    assertEquals( "foo", blk() )
  }

  function testImplicitBlockThenOuterAnonPointerPropertyWriting() {

    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : Callable<block()> {
        return new Callable<block()>() {
          override function call() : block() {
            return \-> { _i_WritableProp = "bar" }
          }
        }
      }
    }
    var c1 = b1()
    c1._i_WritableProp = "foo"
    var c2 = c1.call()
    var blk = c2.call()
    blk()
    assertEquals( "bar", c1._i_WritableProp  )
    c1._i_WritableProp = "foo"
    blk()
    assertEquals( "bar", c1._i_WritableProp )
  }

  function testImplicitBlockThenOuterAnonPointerFieldReading() {

    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : Callable<block():Object> {
        return new Callable<block():Object>() {
          override function call() : block():Object {
            return \-> _i_testField
          }
        }
      }
    }
    var c1 = b1()
    c1._i_WritableProp = "foo"
    var c2 = c1.call()
    var blk = c2.call()
    assertEquals( "foo", blk() )
    c1._i_testField = "bar"
    assertEquals( "bar", blk() )
  }

  function testImplicitBlockThenOuterAnonPointerFieldWriting() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : Callable<block()> {
        return new Callable<block()>() {
          override function call() : block() {
            return \-> { _i_testField = "bar" }
          }
        }
      }
    }
    var c1 = b1()
    c1._i_WritableProp = "foo"
    var c2 = c1.call()
    var blk = c2.call()
    blk()
    assertEquals( "bar", c1._i_testField  )
    c1._i_testField = "foo"
    blk()
    assertEquals( "bar", c1._i_testField )
  }

  function testNestedImplicitBlockThenOuterAnonPointerMethodInvocation1() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : Callable<block():block():int> {
        return new Callable<block():block():int> (){
          override function call() : block():block():int {
            return \->\-> intFunction()
          }
        }
      }
    }
    var c1 = b1()
    var c2 = c1.call()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( 42, blk2() )
  }

  function testNestedImplicitBlockThenOuterAnonPointerMethodInvocation2() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : Callable<block():block():Object> {
        return new Callable<block():block():Object> (){
          override function call() : block():block():Object {
            return \->\-> refFunction()
          }
        }
      }
    }
    var c1 = b1()
    var c2 = c1.call()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( "foo", blk2() )
  }

  function testNestedImplicitBlockThenOuterAnonPointerPropertyInvocation1() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : Callable<block():block():int> {
        return new Callable<block():block():int> (){
          override function call() : block():block():int {
            return \->\-> IntProperty
          }
        }
      }
    }
    var c1 = b1()
    var c2 = c1.call()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( 42, blk2() )
  }

  function testNestedImplicitBlockThenOuterAnonPointerPropertyInvocation2() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : Callable<block():block():Object> {
        return new Callable<block():block():Object> (){
          override function call() : block():block():Object {
            return \->\-> RefProperty
          }
        }
      }
    }
    var c1 = b1()
    var c2 = c1.call()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( "foo", blk2() )
  }

  function testNestedImplicitBlockThenOuterAnonPointerPropertyWriting() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : Callable<block():block()> {
        return new Callable<block():block()> (){
          override function call() : block():block() {
            return \-> \-> { _i_WritableProp = "bar" }
          }
        }
      }
    }
    var c1 = b1()
    var c2 = c1.call()
    var blk = c2.call()
    c1._i_WritableProp = "foo"
    var blk2 = blk()
    blk2()
    assertEquals( "bar", c1._i_WritableProp  )
    c1._i_WritableProp = "foo"
    blk2()
    assertEquals( "bar", c1._i_WritableProp )
  }

  function testNestedImplicitBlockThenOuterAnonPointerFieldWriting() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : Callable<block():block()> {
        return new Callable<block():block()> (){
          override function call() : block():block() {
            return \-> \-> { _i_testField = "bar" }
          }
        }
      }
    }
    var c1 = b1()
    var c2 = c1.call()
    var blk = c2.call()
    c1._i_testField = "foo"
    var blk2 = blk()
    blk2()
    assertEquals( "bar", c1._i_testField  )
    c1._i_WritableProp = "foo"
    blk2()
    assertEquals( "bar", c1._i_testField )
  }

  //=========================================================
  // block then 'outer then block then outer' pointer tests
  //=========================================================

  function testBlockThenOuterThenBlockAnonPointerMethodInvocation1() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():Callable<block():int> {
        return \-> new Callable<block():int>() {
          override function call() : block():int {
            return \-> outer._i_intFunction()
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk = c2.call()
    assertEquals( 42, blk() )
  }

  function testBlockThenOuterThenBlockAnonPointerMethodInvocation2() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():Callable<block():Object> {
        return \-> new Callable<block():Object>() {
          override function call() : block():Object {
            return \-> outer._i_refFunction()
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk = c2.call()
    assertEquals( "foo", blk() )
  }

  function testBlockThenOuterThenBlockAnonPointerPropertyInvocation1() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():Callable<block():int> {
        return \-> new Callable<block():int>() {
          override function call() : block():int {
            return \-> outer._i_IntProperty
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk = c2.call()
    assertEquals( 42, blk() )
  }

  function testBlockThenOuterThenBlockAnonPointerPropertyInvocation2() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():Callable<block():Object> {
        return \-> new Callable<block():Object>() {
          override function call() : block():Object {
            return \-> outer._i_RefProperty
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk = c2.call()
    assertEquals( "foo", blk() )
  }

  function testBlockThenOuterThenBlockAnonPointerPropertyWriting() {

    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():Callable<block()> {
        return \-> new Callable<block()>() {
          override function call() : block() {
            return \-> { outer._i_WritableProp = "bar" }
          }
        }
      }
    }
    var c1 = b1()
    c1._i_WritableProp = "foo"
    var b2 = c1.call()
    var c2 = b2()
    var blk = c2.call()
    blk()
    assertEquals( "bar", c1._i_WritableProp  )
    c1._i_WritableProp = "foo"
    blk()
    assertEquals( "bar", c1._i_WritableProp )
  }

  function testBlockThenOuterThenBlockAnonPointerFieldReading() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():Callable<block():Object> {
        return \-> new Callable<block():Object>() {
          override function call() : block():Object {
            return \->_i_testField
          }
        }
      }
    }
    var c1 = b1()
    c1._i_WritableProp = "foo"
    var b2 = c1.call()
    var c2 = b2()
    var blk = c2.call()
    assertEquals( "foo", blk() )
    c1._i_testField = "bar"
    assertEquals( "bar", blk() )
  }

  function testBlockThenOuterThenBlockAnonPointerFieldWriting() {

    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():Callable<block()> {
        return \-> new Callable<block()>() {
          override function call() : block() {
            return \-> {_i_testField = "bar" }
          }
        }
      }
    }
    var c1 = b1()
    c1._i_WritableProp = "foo"
    var b2 = c1.call()
    var c2 = b2()
    var blk = c2.call()
    blk()
    assertEquals( "bar", c1._i_testField  )
    c1._i_testField = "foo"
    blk()
    assertEquals( "bar", c1._i_testField )
  }

  function testNestedBlockThenOuterThenBlockAnonPointerMethodInvocation1() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():Callable<block():block():int> {
        return \-> new Callable<block():block():int> (){
          override function call() : block():block():int {
            return \->\-> outer._i_intFunction()
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( 42, blk2() )
  }

  function testNestedBlockThenOuterThenBlockAnonPointerMethodInvocation2() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():Callable<block():block():Object> {
        return \-> new Callable<block():block():Object> (){
          override function call() : block():block():Object {
            return \->\-> outer._i_refFunction()
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( "foo", blk2() )
  }

  function testNestedBlockThenOuterThenBlockAnonPointerPropertyInvocation1() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():Callable<block():block():int> {
        return \-> new Callable<block():block():int> (){
          override function call() : block():block():int {
            return \->\-> outer._i_IntProperty
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( 42, blk2() )
  }

  function testNestedBlockThenOuterThenBlockAnonPointerPropertyInvocation2() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():Callable<block():block():Object> {
        return \-> new Callable<block():block():Object> (){
          override function call() : block():block():Object {
            return \->\-> outer._i_RefProperty
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( "foo", blk2() )
  }

  function testNestedBlockThenOuterThenBlockAnonPointerPropertyWriting() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():Callable<block():block()> {
        return \-> new Callable<block():block()> (){
          override function call() : block():block() {
            return \-> \-> { outer._i_WritableProp = "bar" }
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk = c2.call()
    c1._i_WritableProp = "foo"
    var blk2 = blk()
    blk2()
    assertEquals( "bar", c1._i_WritableProp  )
    c1._i_WritableProp = "foo"
    blk2()
    assertEquals( "bar", c1._i_WritableProp )
  }

  function testNestedBlockThenOuterThenBlockAnonPointerFieldWriting() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():Callable<block():block()> {
        return \-> new Callable<block():block()> (){
          override function call() : block():block() {
            return \-> \-> {_i_testField = "bar" }
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk = c2.call()
    c1._i_testField = "foo"
    var blk2 = blk()
    blk2()
    assertEquals( "bar", c1._i_testField  )
    c1._i_WritableProp = "foo"
    blk2()
    assertEquals( "bar", c1._i_testField )
  }

  function testTripleNestedBlockThenOuterThenBlockAnonPointer() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():Callable<block():block():block():Object> {
        return \-> new Callable<block():block():block():Object> (){
          override function call() : block():block():block():Object {
            return \-> \-> \-> outer.outer
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk = c2.call()
    var blk2 = blk()
    var blk3 = blk2()
    assertEquals( this, blk3() )
  }

  //=========================================================
  // implicit block then 'outer outer' pointer tests
  //=========================================================

  function testImplicitBlockThenOuterThenBlockAnonPointerMethodInvocation1() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():Callable<block():int> {
        return \-> new Callable<block():int>() {
          override function call() : block():int {
            return \-> intFunction()
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk = c2.call()
    assertEquals( 42, blk() )
  }

  function testImplicitBlockThenOuterThenBlockAnonPointerMethodInvocation2() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():Callable<block():Object> {
        return \-> new Callable<block():Object>() {
          override function call() : block():Object {
            return \-> refFunction()
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk = c2.call()
    assertEquals( "foo", blk() )
  }

  function testImplicitBlockThenOuterThenBlockAnonPointerPropertyInvocation1() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():Callable<block():int> {
        return \-> new Callable<block():int>() {
          override function call() : block():int {
            return \-> IntProperty
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk = c2.call()
    assertEquals( 42, blk() )
  }

  function testImplicitBlockThenOuterThenBlockAnonPointerPropertyInvocation2() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():Callable<block():Object> {
        return \-> new Callable<block():Object>() {
          override function call() : block():Object {
            return \-> RefProperty
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk = c2.call()
    assertEquals( "foo", blk() )
  }

  function testImplicitBlockThenOuterThenBlockAnonPointerPropertyWriting() {

    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():Callable<block()> {
        return \-> new Callable<block()>() {
          override function call() : block() {
            return \-> { _i_WritableProp = "bar" }
          }
        }
      }
    }
    var c1 = b1()
    c1._i_WritableProp = "foo"
    var b2 = c1.call()
    var c2 = b2()
    var blk = c2.call()
    blk()
    assertEquals( "bar", c1._i_WritableProp  )
    c1._i_WritableProp = "foo"
    blk()
    assertEquals( "bar", c1._i_WritableProp )
  }

  function testImplicitBlockThenOuterThenBlockAnonPointerFieldReading() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():Callable<block():Object> {
        return \-> new Callable<block():Object>() {
          override function call() : block():Object {
            return \-> _i_testField
          }
        }
      }
    }
    var c1 = b1()
    c1._i_WritableProp = "foo"
    var b2 = c1.call()
    var c2 = b2()
    var blk = c2.call()
    assertEquals( "foo", blk() )
    c1._i_testField = "bar"
    assertEquals( "bar", blk() )
  }

  function testImplicitBlockThenOuterThenBlockAnonPointerFieldWriting() {

    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():Callable<block()> {
        return \-> new Callable<block()>() {
          override function call() : block() {
            return \-> { _i_testField = "bar" }
          }
        }
      }
    }
    var c1 = b1()
    c1._i_WritableProp = "foo"
    var b2 = c1.call()
    var c2 = b2()
    var blk = c2.call()
    blk()
    assertEquals( "bar", c1._i_testField  )
    c1._i_testField = "foo"
    blk()
    assertEquals( "bar", c1._i_testField )
  }

  function testNestedImplicitBlockThenOuterThenBlockAnonPointerMethodInvocation1() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():Callable<block():block():int> {
        return \-> new Callable<block():block():int> (){
          override function call() : block():block():int {
            return \->\-> intFunction()
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( 42, blk2() )
  }

  function testNestedImplicitBlockThenOuterThenBlockAnonPointerMethodInvocation2() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():Callable<block():block():Object> {
        return \-> new Callable<block():block():Object> (){
          override function call() : block():block():Object {
            return \->\-> refFunction()
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( "foo", blk2() )
  }

  function testNestedImplicitBlockThenOuterThenBlockAnonPointerPropertyInvocation1() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():Callable<block():block():int> {
        return \-> new Callable<block():block():int> (){
          override function call() : block():block():int {
            return \->\-> IntProperty
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( 42, blk2() )
  }

  function testNestedImplicitBlockThenOuterThenBlockAnonPointerPropertyInvocation2() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():Callable<block():block():Object> {
        return \-> new Callable<block():block():Object> (){
          override function call() : block():block():Object {
            return \->\-> RefProperty
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( "foo", blk2() )
  }

  function testNestedImplicitBlockThenOuterThenBlockAnonPointerPropertyWriting() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():Callable<block():block()> {
        return \-> new Callable<block():block()> (){
          override function call() : block():block() {
            return \-> \-> { _i_WritableProp = "bar" }
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk = c2.call()
    c1._i_WritableProp = "foo"
    var blk2 = blk()
    blk2()
    assertEquals( "bar", c1._i_WritableProp  )
    c1._i_WritableProp = "foo"
    blk2()
    assertEquals( "bar", c1._i_WritableProp )
  }

  function testNestedImplicitBlockThenOuterThenBlockAnonPointerFieldWriting() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():Callable<block():block()> {
        return \-> new Callable<block():block()> (){
          override function call() : block():block() {
            return \-> \-> { _i_testField = "bar" }
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var c2 = b2()
    var blk = c2.call()
    c1._i_testField = "foo"
    var blk2 = blk()
    blk2()
    assertEquals( "bar", c1._i_testField  )
    c1._i_WritableProp = "foo"
    blk2()
    assertEquals( "bar", c1._i_testField )
  }

  //=========================================================
  // block then 'outer then block then block then outer' pointer tests
  //=========================================================

  function testBlockThenOuterThenBlockThenBlockAnonPointerMethodInvocation1() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():block():Callable<block():int> {
        return \-> \-> new Callable<block():int>() {
          override function call() : block():int {
            return \-> outer._i_intFunction()
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk = c2.call()
    assertEquals( 42, blk() )
  }

  function testBlockThenOuterThenBlockThenBlockAnonPointerMethodInvocation2() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():block():Callable<block():Object> {
        return \-> \-> new Callable<block():Object>() {
          override function call() : block():Object {
            return \-> outer._i_refFunction()
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk = c2.call()
    assertEquals( "foo", blk() )
  }

  function testBlockThenOuterThenBlockThenBlockAnonPointerPropertyInvocation1() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():block():Callable<block():int> {
        return \-> \-> new Callable<block():int>() {
          override function call() : block():int {
            return \-> outer._i_IntProperty
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk = c2.call()
    assertEquals( 42, blk() )
  }

  function testBlockThenOuterThenBlockThenBlockAnonPointerPropertyInvocation2() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():block():Callable<block():Object> {
        return \-> \-> new Callable<block():Object>() {
          override function call() : block():Object {
            return \-> outer._i_RefProperty
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk = c2.call()
    assertEquals( "foo", blk() )
  }

  function testBlockThenOuterThenBlockThenBlockAnonPointerPropertyWriting() {

    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():block():Callable<block()> {
        return \-> \-> new Callable<block()>() {
          override function call() : block() {
            return \-> { outer._i_WritableProp = "bar" }
          }
        }
      }
    }
    var c1 = b1()
    c1._i_WritableProp = "foo"
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk = c2.call()
    blk()
    assertEquals( "bar", c1._i_WritableProp  )
    c1._i_WritableProp = "foo"
    blk()
    assertEquals( "bar", c1._i_WritableProp )
  }

  function testBlockThenOuterThenBlockThenBlockAnonPointerFieldReading() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():block():Callable<block():Object> {
        return \-> \-> new Callable<block():Object>() {
          override function call() : block():Object {
            return \->_i_testField
          }
        }
      }
    }
    var c1 = b1()
    c1._i_WritableProp = "foo"
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk = c2.call()
    assertEquals( "foo", blk() )
    c1._i_testField = "bar"
    assertEquals( "bar", blk() )
  }

  function testBlockThenOuterThenBlockThenBlockAnonPointerFieldWriting() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():block():Callable<block()> {
        return \-> \-> new Callable<block()>() {
          override function call() : block() {
            return \-> { _i_testField = "bar" }
          }
        }
      }
    }
    var c1 = b1()
    c1._i_WritableProp = "foo"
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk = c2.call()
    blk()
    assertEquals( "bar", c1._i_testField  )
    c1._i_testField = "foo"
    blk()
    assertEquals( "bar", c1._i_testField )
  }

  function testNestedBlockThenOuterThenBlockThenBlockAnonPointerMethodInvocation1() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():block():Callable<block():block():int> {
        return \-> \-> new Callable<block():block():int> (){
          override function call() : block():block():int {
            return \->\-> outer._i_intFunction()
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( 42, blk2() )
  }

  function testNestedBlockThenOuterThenBlockThenBlockAnonPointerMethodInvocation2() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():block():Callable<block():block():Object> {
        return \-> \-> new Callable<block():block():Object> (){
          override function call() : block():block():Object {
            return \->\-> outer._i_refFunction()
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( "foo", blk2() )
  }

  function testNestedBlockThenOuterThenBlockThenBlockAnonPointerPropertyInvocation1() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():block():Callable<block():block():int> {
        return \-> \-> new Callable<block():block():int> (){
          override function call() : block():block():int {
            return \->\-> outer._i_IntProperty
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( 42, blk2() )
  }

  function testNestedBlockThenOuterThenBlockThenBlockAnonPointerPropertyInvocation2() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():block():Callable<block():block():Object> {
        return \-> \-> new Callable<block():block():Object> (){
          override function call() : block():block():Object {
            return \->\-> outer._i_RefProperty
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( "foo", blk2() )
  }

  function testNestedBlockThenOuterThenBlockThenBlockAnonPointerPropertyWriting() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():block():Callable<block():block()> {
        return \-> \-> new Callable<block():block()> (){
          override function call() : block():block() {
            return \-> \-> { outer._i_WritableProp = "bar" }
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk = c2.call()
    c1._i_WritableProp = "foo"
    var blk2 = blk()
    blk2()
    assertEquals( "bar", c1._i_WritableProp  )
    c1._i_WritableProp = "foo"
    blk2()
    assertEquals( "bar", c1._i_WritableProp )
  }

  function testNestedBlockThenOuterThenBlockThenBlockAnonPointerFieldWriting() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():block():Callable<block():block()> {
        return \-> \-> new Callable<block():block()> (){
          override function call() : block():block() {
            return \-> \-> {_i_testField = "bar" }
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk = c2.call()
    c1._i_testField = "foo"
    var blk2 = blk()
    blk2()
    assertEquals( "bar", c1._i_testField  )
    c1._i_WritableProp = "foo"
    blk2()
    assertEquals( "bar", c1._i_testField )
  }

  //=========================================================
  // implicit block then 'outer then block then block outer' pointer tests
  //=========================================================

  function testImplicitBlockThenOuterThenBlockThenBlockAnonPointerMethodInvocation1() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():block():Callable<block():int> {
        return \-> \-> new Callable<block():int>() {
          override function call() : block():int {
            return \-> intFunction()
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk = c2.call()
    assertEquals( 42, blk() )
  }

  function testImplicitBlockThenOuterThenBlockThenBlockAnonPointerMethodInvocation2() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():block():Callable<block():Object> {
        return \-> \-> new Callable<block():Object>() {
          override function call() : block():Object {
            return \-> refFunction()
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk = c2.call()
    assertEquals( "foo", blk() )
  }

  function testImplicitBlockThenOuterThenBlockThenBlockAnonPointerPropertyInvocation1() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():block():Callable<block():int> {
        return \-> \-> new Callable<block():int>() {
          override function call() : block():int {
            return \-> IntProperty
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk = c2.call()
    assertEquals( 42, blk() )
  }

  function testImplicitBlockThenOuterThenBlockThenBlockAnonPointerPropertyInvocation2() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():block():Callable<block():Object> {
        return \-> \-> new Callable<block():Object>() {
          override function call() : block():Object {
            return \-> RefProperty
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk = c2.call()
    assertEquals( "foo", blk() )
  }

  function testImplicitBlockThenOuterThenBlockThenBlockAnonPointerPropertyWriting() {

    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():block():Callable<block()> {
        return \-> \-> new Callable<block()>() {
          override function call() : block() {
            return \-> { _i_WritableProp = "bar" }
          }
        }
      }
    }
    var c1 = b1()
    c1._i_WritableProp = "foo"
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk = c2.call()
    blk()
    assertEquals( "bar", c1._i_WritableProp  )
    c1._i_WritableProp = "foo"
    blk()
    assertEquals( "bar", c1._i_WritableProp )
  }

  function testImplicitBlockThenOuterThenBlockThenBlockAnonPointerFieldReading() {

    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():block():Callable<block():Object> {
        return \-> \-> new Callable<block():Object>() {
          override function call() : block():Object {
            return \-> _i_testField
          }
        }
      }
    }
    var c1 = b1()
    c1._i_WritableProp = "foo"
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk = c2.call()
    assertEquals( "foo", blk() )
    c1._i_testField = "bar"
    assertEquals( "bar", blk() )
  }

  function testImplicitBlockThenOuterThenBlockThenBlockAnonPointerFieldWriting() {

    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():block():Callable<block()> {
        return \-> \-> new Callable<block()>() {
          override function call() : block() {
            return \-> { _i_testField = "bar" }
          }
        }
      }
    }
    var c1 = b1()
    c1._i_WritableProp = "foo"
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk = c2.call()
    blk()
    assertEquals( "bar", c1._i_testField  )
    c1._i_testField = "foo"
    blk()
    assertEquals( "bar", c1._i_testField )
  }

  function testNestedImplicitBlockThenOuterThenBlockThenBlockAnonPointerMethodInvocation1() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():block():Callable<block():block():int> {
        return \-> \-> new Callable<block():block():int> (){
          override function call() : block():block():int {
            return \->\-> intFunction()
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( 42, blk2() )
  }

  function testNestedImplicitBlockThenOuterThenBlockThenBlockAnonPointerMethodInvocation2() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():block():Callable<block():block():Object> {
        return \-> \-> new Callable<block():block():Object> (){
          override function call() : block():block():Object {
            return \->\-> refFunction()
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( "foo", blk2() )
  }

  function testNestedImplicitBlockThenOuterThenBlockThenBlockAnonPointerPropertyInvocation1() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():block():Callable<block():block():int> {
        return \-> \-> new Callable<block():block():int> (){
          override function call() : block():block():int {
            return \->\-> IntProperty
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( 42, blk2() )
  }

  function testNestedImplicitBlockThenOuterThenBlockThenBlockAnonPointerPropertyInvocation2() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():block():Callable<block():block():Object> {
        return \-> \-> new Callable<block():block():Object> (){
          override function call() : block():block():Object {
            return \->\-> RefProperty
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk1 = c2.call()
    var blk2 = blk1()
    assertEquals( "foo", blk2() )
  }

  function testNestedImplicitBlockThenOuterThenBlockThenBlockAnonPointerPropertyWriting() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():block():Callable<block():block()> {
        return \-> \-> new Callable<block():block()> (){
          override function call() : block():block() {
            return \-> \-> { _i_WritableProp = "bar" }
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk = c2.call()
    c1._i_WritableProp = "foo"
    var blk2 = blk()
    blk2()
    assertEquals( "bar", c1._i_WritableProp  )
    c1._i_WritableProp = "foo"
    blk2()
    assertEquals( "bar", c1._i_WritableProp )
  }

  function testNestedImplicitBlockThenOuterThenBlockThenBlockAnonPointerFieldWriting() {
    var b1 = \-> new Callable() {
            
     var _i_testField : String as _i_WritableProp

     property get _i_IntProperty() : int {
       return 42
     }

     property get _i_RefProperty() : String {
       return "foo"
     }

     function _i_intFunction() : int {
       return 42
     }

     function _i_refFunction() : String {
       return "foo"
     }

      override function call() : block():block():Callable<block():block()> {
        return \-> \-> new Callable<block():block()> (){
          override function call() : block():block() {
            return \-> \-> { _i_testField = "bar" }
          }
        }
      }
    }
    var c1 = b1()
    var b2 = c1.call()
    var b3 = b2()
    var c2 = b3()
    var blk = c2.call()
    c1._i_testField = "foo"
    var blk2 = blk()
    blk2()
    assertEquals( "bar", c1._i_testField  )
    c1._i_WritableProp = "foo"
    blk2()
    assertEquals( "bar", c1._i_testField )
  }
}