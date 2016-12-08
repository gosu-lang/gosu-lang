package gw.internal.gosu.compiler.blocks
uses gw.test.TestClass

class BlockRecursiveCaptureTest extends TestClass {

  static class CapturesBlockInConstructor {
    var _blk : block():String
    construct( blk() : String ) {
      _blk = \-> {
        return blk()
      }
    }
    
    function callIt() : String {
      return _blk()
    }
  }

  function testConstructorCaptureWorks() {
    var cc = new CapturesBlockInConstructor( \-> "foo" )
    assertEquals( "foo", cc.callIt() ) 
  }

  static class CapturesNestedBlockInConstructor {
    var _nestedBlock : block():CapturesBlockInConstructor
    construct( blk() : String ) {
      _nestedBlock = \-> new CapturesBlockInConstructor( \-> blk == null ? null : blk() ) 
    }
    
    function callIt() : String {
      return _nestedBlock().callIt() 
    }
  }

  function testNestedConstructorCaptureWorks() {
    var cc = new CapturesNestedBlockInConstructor( \-> "foo" )
    assertEquals( "foo", cc.callIt() ) 
  }

  static class CallsMethodInClassFromBlock {    
    
    var _str  : String
    
    function setIt( s : String ) {
      _str = s
    }

    construct( blk() : String ) {
      var blk2 = \-> {
        setIt( blk() ) 
      }
      blk2()
    }
    
    function callIt() : String {
      return _str
    }
  }
  
  function testNestedCallbackToClassOnOuterWorks() {
    var cc = new CallsMethodInClassFromBlock( \-> "foo" )
    assertEquals( "foo", cc.callIt() ) 
  }

  function testRecursiveLocalVarInitialer() {
    var bar(x: int): int = \ x -> x == 0 ? 0 : x + bar( x - 1 )
    assertEquals( 10, bar( 4 ) )
  }

  static class CallsMethodInClassFromBlockImplementsJavaInterface implements ISampleInterface {    
    
    var _str  : String
    
    override function setIt( s : String ) {
      _str = s
    }

    construct( blk() : String ) {
      var blk2 = \-> {
        setIt( blk() ) 
      }
      blk2()
    }
    
    function callIt() : String {
      return _str
    }
  }
  
  function testNestedCallbackToClassOnOuterWorksWhenOuterImplementsJavaInterface() {
    var cc = new CallsMethodInClassFromBlockImplementsJavaInterface( \-> "foo" )
    assertEquals( "foo", cc.callIt() ) 
  }

  static class CallsMethodInClassFromBlockExtendsJavaClass extends SampleSuperClass {    
    construct( blk() : String ) {
      var blk2 = \-> {
        setIt( blk() ) 
      }
      blk2()
    }
  }
  
  function testNestedCallbackToClassOnOuterWorksWhenOuterExtendsJavaClass() {
    var cc = new CallsMethodInClassFromBlockExtendsJavaClass( \-> "foo" )
    assertEquals( "foo", cc.callIt() ) 
  }

}