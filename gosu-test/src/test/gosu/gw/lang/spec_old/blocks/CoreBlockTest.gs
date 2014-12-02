package gw.lang.spec_old.blocks
uses gw.test.TestClass
uses gw.lang.parser.exceptions.ParseResultsException
uses gw.testharness.KnownBreak
uses gw.lang.parser.resources.Res
uses java.lang.Integer

class CoreBlockTest extends TestClass
{

  function testBasicBlockInvocationWorks() 
  {
    var x = \->10
    assertEquals( 10, x())
  }

  function testBasicBlockArgPassing() 
  {
    var x = \y : int -> y + 10
    assertEquals( 20, x(10) )
  }

  function testInScopeClosureCapture() 
  {
    var z = 10
    var x = \ y : int -> y + z
    assertEquals( 20, x(10) )
  }

  function executeIt(it():int) : int{
    var x = 10 //here to ensure that offsets are respected
    var y = 20   
    print("${x} ${y}")
    return it()
  }
    
  function testDownwardScopeClosureCapture() 
  {
    var x = 1
    var myIt = \-> x 
    assertEquals( 1, executeIt( myIt ) )
  }
  
  function makeBlock() : block(i:int):int
  {
    var z = 10
    return \ y : int -> y + z 
  }
  
  function testUpwardClosureCapture() 
  {
    var x = makeBlock()
    assertEquals( 20, x(10) )
  }

  function callSecondBlock( x : int , y : block():int ) : int
  {
    print(x)
    return  y()
  }
  
  function testClosureArgPassingWorksWithMultipleArgs() 
  {
    assertEquals( 20, callSecondBlock( 10, \-> 20 ))
  }

  function chained1( x : int , y : block():int ) : int
  { 
    var z = 25
    print("${z} ${x}")
    return  chained2( y )
  }
  
  function chained2( y : block():int ) : int
  { 
    var z = 25
    print(z)
    return y()
  }

  function testClosureArgPassingWorksWithMultipleArgPasses() 
  {
    assertEquals( 20, chained1(10, \-> 20) )
  }

  //TODO cgross - port over remaining block tests
  static class SuperTest {
    construct(a : String) {
      print(a)
    }
  }
  
  static class Test extends SuperTest {
    var _block : block(s:String) : String as MyBlock
    construct(c : String) {
      super( c )
      _block = \ x : String -> c + x
    }
    property get Chained() : Test {
      return this
    }

    function chainedFunc() : Test {
      return this
    }
    
    function returnBlock() : block(s:String):String {
      return _block
    }
    
    function invokesBlockViaReturnBlock( s : String ) : String {
      return returnBlock()( s )
    }

    function invokesBlockViaProperty( s : String ) : String {
      return MyBlock( s )
    }

    function invokesBlockViaField( s : String ) : String {
      return _block( s )
    }
  }  
  
  function testSuperClassCanInvokeBlockInConstructor() {
    var t = new Test( "Gosu rocks" )
    var blk = t.MyBlock
    assertEquals( "Gosu rocks the house", blk( " the house" ) )
  }

  function testSuperClassCanInvokeBlockInConstructorIndirectly() {
    var t = new Test( "Gosu rocks" )
    assertEquals( "Gosu rocks the house", t.MyBlock(" the house") )
  }

  function testSuperClassCanInvokeBlockInConstructorIndirectly2() {
    var t = new Test( "Gosu rocks" )
    assertEquals( "Gosu rocks the house", t.Chained.MyBlock(" the house") )
  }

  function testSuperClassCanInvokeBlockInConstructorIndirectly3() {
    var t = new Test( "Gosu rocks" )
    assertEquals( "Gosu rocks the house", t.Chained.Chained.MyBlock(" the house") )
  }
  
  function testSuperClassCanInvokeBlockInConstructorIndirectly4() {
    var t = new Test( "Gosu rocks" )
    assertEquals( "Gosu rocks the house", t.Chained.Chained.Chained.MyBlock(" the house") )
  }
  
  function testSuperClassCanInvokeBlockInConstructorIndirectlyViaFunction() {
    var t = new Test( "Gosu rocks" )
    assertEquals( "Gosu rocks the house", t.returnBlock()(" the house") )
  }

  function testSuperClassCanInvokeBlockInConstructorIndirectlyViaFunction2() {
    var t = new Test( "Gosu rocks" )
    assertEquals( "Gosu rocks the house", t.chainedFunc().returnBlock()(" the house") )
  }

  function testSuperClassCanInvokeBlockInConstructorIndirectlyViaFunction3() {
    var t = new Test( "Gosu rocks" )
    assertEquals( "Gosu rocks the house", t.chainedFunc().chainedFunc().returnBlock()(" the house") )
  }
  
  function testSuperClassCanInvokeBlockInConstructorIndirectlyViaFunction4() {
    var t = new Test( "Gosu rocks" )
    assertEquals( "Gosu rocks the house", t.chainedFunc().chainedFunc().chainedFunc().returnBlock()(" the house") )
  }
  
  function testDirectInvocationOfBlockViaFunction() {
    var t = new Test( "Gosu rocks" )
    assertEquals( "Gosu rocks the house", t.invokesBlockViaReturnBlock(" the house") )     
  }

  function testDirectInvocationOfBlockViaProperty() {
    var t = new Test( "Gosu rocks" )
    assertEquals( "Gosu rocks the house", t.invokesBlockViaProperty(" the house") )     
  }

  function testDirectInvocationOfBlockViaField() {
    var t = new Test( "Gosu rocks" )
    assertEquals( "Gosu rocks the house", t.invokesBlockViaField(" the house") )     
  }
  
  function testIndirectBlockInvocationInList() {
    var blkList = { \ s : String -> s + s }
    assertEquals( "foofoo", blkList[0]("foo") )
  }

  function testIndirectBlockInvocationInMap() {
    var blkList = { "foo" -> \ s : String -> s + s }
    assertEquals( "foofoo", blkList["foo"]("foo") )
  }

  function testIndirectBlockInvocationWithBlockStatement() {
    assertEquals( "foofoo", (\ s : String -> s + s)("foo") )
  }

  function testIndirectBlockInvocationInTernaryExpression() {
    assertEquals( "foofoo", (true ? \ s : String -> s + s : \ s : String -> "bad" )("foo") )
  }
  
  function testAbsurdBlocks() {
    var blk2 = \->\-> "foo"
    assertEquals( "foo", blk2()() )

    var blk3 = \->\->\-> "foo"
    assertEquals( "foo", blk3()()() )

    var blk4 = \->\->\->\-> "foo"
    assertEquals( "foo", blk4()()()() )

    var blk5 = \->\->\->\->\-> "foo"
    assertEquals( "foo", blk5()()()()() )
  }

  function testBlockAssignment() {
    blockAssignment<Boolean>(false)
    blockAssignment(false)
  }

  function blockAssignment<T>(b : boolean) {
    var blockType = block(s:String):T
    assertEquals( b, blockType.isAssignableFrom( block(s:String) )) // block():void is not assignable to block():non-void
  }

  function testBlockWithOnlyThrowsIsAssignableToAnyReturnType() {
    var blk():String
    
    blk = \-> {
      throw "This should compile"
    }
    
    blk = \-> {
      if( true) { 
        throw "This should compile"
      } else {
        throw "This should compile"
      }
    }
    
    blk = \-> {
      var x = 10
      if( true) {
        for( i in 0..|100 ) {
          x = i
        }
        throw "This should compile"
      } else {
        x = 100
        throw "This should compile"
      }
    }    
  }

  function testNakedBlocksHandleThrowStatementsCorrectly() {
    var blk1 = \-> {
      throw "This should compile"
    }
    assertEquals( block():void, statictypeof blk1 )

    var blk2 = \-> {
      if( true) { 
        throw "This should compile"
      } else {
        throw "This should compile"
      }
    }
    assertEquals( block():void, statictypeof blk2 )
    
    var blk3 = \-> {
      var x = 10
      if( true) {
        for( i in 0..|100 ) {
          x = i
        }
        throw "This should compile"
      } else {
        x = 100
        throw "This should compile"
      }
    }
    assertEquals( block():void, statictypeof blk3 )

    var blk4 = \-> {
      var x = 10
      if( true) {
        for( i in 0..|100 ) {
          x = i
        }
        throw "This should compile"
      } else {
        return x
      }
    }
    assertEquals( block():int, statictypeof blk4 )
  }
  
  function testBlockWithStatementWithoutDominatingReturnsWorksIfNoValueExpected() {
    var x()
    x = \-> { throw "Hello" }
    x = \-> { if(true) throw "Hello" }
    x = \-> { for( i in 0..|100 ) print( i ) }
  }
  
  function testBlockWithStatementWithoutDominatingReturnsCausesError() {
    var x():String
    assertCausesException( \-> eval("x = \\-> { if(true) throw \"\" }" ), ParseResultsException )
    assertCausesException( \-> eval("x = \\-> { if(true) return \"\" }" ), ParseResultsException )
    assertCausesException( \-> eval("x = \\-> { for( i in 0..|100) { return \"\" } } " ), ParseResultsException )
  }

  function testBlocksCannotBeDeclaredWithMoreThanSixteenArgs() {
    assertFalse( Errant_BadBlocks.Type.Valid )
    var parseExceptions = Errant_BadBlocks.Type.ParseResultsException.ParseExceptions
    print( parseExceptions.map( \ ex -> ex.Line ) )
    assertEquals( 6, parseExceptions.countWhere( \ ex -> ex.MessageKey == Res.MSG_BLOCKS_CAN_HAVE_A_MOST_SIXTEEN_ARGS) )
  }

  function testTypeOfWorksWithBlockTypes() {
    assertTrue( (\-> "foo") typeis block():String )
    assertTrue( (\-> print( "foo" )) typeis block())
    assertTrue( (\-> \-> print( "foo" )) typeis block():block())
    assertTrue( (\x:String-> print( x )) typeis block(s:String))
  }

  function testBlockWithBlockArgument() {
    var b0( x(i : Integer) : String) : String
    var b1( x : block(i : Integer) : String) : String
    var b2 = \ i : Integer -> i.toString() + ". Hello "

    b0 = \ x : block(i : Integer) : String ->  x(1) + "World"
    assertEquals("1. Hello World", b0(b2))
    b0 = \x(i : Integer) : String -> x(2) + "World"
    assertEquals("2. Hello World", b0(b2))

    b1 = \ x : block(i : Integer) : String ->  x(3) + "World"
    assertEquals("3. Hello World", b1(b2))
    b1 = \x(i : Integer) : String -> x(4) + "World"
    assertEquals("4. Hello World", b1(b2))
  }
}
