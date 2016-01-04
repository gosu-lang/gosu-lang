package gw.lang.spec_old.statements.using_stmt
uses java.io.BufferedWriter
uses java.io.StringWriter
uses java.io.IOException
uses java.util.concurrent.locks.ReentrantLock
uses java.lang.Thread
uses gw.lang.parser.resources.Res
uses gw.lang.parser.resources.ResourceKey
uses gw.lang.reflect.gs.IGosuClass
uses java.io.FileWriter
uses java.io.File
  
class UsingStatementTest extends gw.test.TestClass
{
  construct( testname : String )
  {
    super( testname )
  }

  function testUsingStatementBlockExecutes()
  {
    var value = false
    var disposable = new DisposableImpl()
    using( disposable )
    {
      value = true
    }
    assertTrue( value )
  }

  function testDisposableExpression()
  {
    var disposable = new DisposableImpl()
    var value = false
    using( disposable )
    {
      value = disposable.Disposed
      assertFalse( value )
      value = true
    }
    assertTrue( value )
    assertTrue( disposable.Disposed )
  }

  function testDisposableVar()
  {
    var disposable = new DisposableImpl()
    assertFalse( disposable.Disposed )
    var value = false
    using( var d = disposable )
    {
      value = d.Disposed
      assertFalse( value )
      value = true
    }
    assertTrue( value )
    assertTrue( disposable.Disposed )
  }

  function testMultipleDisposableVar()
  {
    var disposable = new DisposableImpl()
    var disposable2 = new DisposableImpl()
    assertFalse( disposable.Disposed )
    assertFalse( disposable2.Disposed )
    using( var d = disposable,
           var d2 = disposable2 )
    {
      assertFalse( d.Disposed )
      assertFalse( d2.Disposed )
    }
    assertTrue( disposable.Disposed )
    assertTrue( disposable2.Disposed )
  }

  function testCloseableVar()
  {
    var sw = new StringWriter()
    var bw = new BufferedWriter( sw )
    using( var writer = bw )
    {
      writer.write( "hello" )
    }
    assertEquals( "hello", sw.toString() )
    try
    {
      bw.write( "fail" )
      fail()
    }
    catch( e : IOException )
    {
      assertEquals( "Stream closed", e.Message )
    }
  }

  function testMultipleCloseableVar()
  {
    var sw = new StringWriter()
    var bw = new BufferedWriter( sw )
    var sw2 = new StringWriter()
    var bw2 = new BufferedWriter( sw2 )
    using( var writer = bw,
           var writer2 = bw2 )
    {
      writer.write( "hello" )
      writer2.write( "bye" )
    }
    assertEquals( "hello", sw.toString() )
    assertEquals( "bye", sw2.toString() )
    try
    {
      bw.write( "fail" )
      fail()
    }
    catch( e : IOException )
    {
      assertEquals( "Stream closed", e.Message )
    }
    try
    {
      bw2.write( "fail" )
      fail()
    }
    catch( e : IOException )
    {
      assertEquals( "Stream closed", e.Message )
    }
  }
  
  function testLock()
  {
    var lock = new ReentrantLock()
    assertFalse( lock.HeldByCurrentThread )
    var value = false
    using( lock )
    {      
      value = lock.HeldByCurrentThread
      assertTrue( value )
    }
    assertFalse( lock.HeldByCurrentThread )
    assertTrue( value )
  }

  function testLockInVar()
  {
    var lock = new ReentrantLock()
    assertFalse( lock.HeldByCurrentThread )
    var value = false
    using( var l = lock )
    {      
      value = lock.HeldByCurrentThread
      assertTrue( value )
    }
    assertFalse( lock.HeldByCurrentThread )
    assertTrue( value )
  }

  function testLockInVarNested()
  {
    var lock = new ReentrantLock()
    var lock2 = new ReentrantLock()
    assertFalse( lock.HeldByCurrentThread )
    var value = false
    using( var l = lock,
           var l2 = lock2 )
    {      
      value = lock.HeldByCurrentThread
      assertTrue( value )
      value = lock2.HeldByCurrentThread
      assertTrue( value )
    }
    assertFalse( lock.HeldByCurrentThread )
    assertFalse( lock2.HeldByCurrentThread )
    assertTrue( value )
  }

  function testMonitorLock()
  {
    var lock = new Object()
    assertFalse( Thread.holdsLock( lock ) )
    var value = false
    using( lock as IMonitorLock )
    {      
      value = Thread.holdsLock( lock )
      assertTrue( value )
    }
    assertFalse( Thread.holdsLock( lock ) )
    assertTrue( value )
  }
  function testReturnInsideUsingStatement()
  {
    var disposable = new DisposableImpl()
    assertTrue( returnsInsideUsingStatement( disposable ) )
    assertTrue( disposable.Disposed )
  }

  function testWorksWithDisposeFunction()
  {
    var res = new MeHaveDisposeFunction()
    assertFalse( res.Disposed )
    using( var meHaveDisposeFunction = res )
    {
      assertFalse( meHaveDisposeFunction.Disposed )
      meHaveDisposeFunction.foo()
      assertFalse( meHaveDisposeFunction.Disposed )
    }
    assertTrue( res.Disposed )
  }

  function testWorksWithInnerDisposeFunction()
  {
    var outerRes = new MeHaveDisposeFunction()
    var res = outerRes.makeInner()
    assertFalse( res.InnerDisposed )
    using( var meHaveDisposeFunction = res )
    {
      assertFalse( meHaveDisposeFunction.InnerDisposed )
      meHaveDisposeFunction.bar()
      assertFalse( meHaveDisposeFunction.InnerDisposed )
    }
    assertTrue( res.InnerDisposed )
  }

  function testWorksWithCloseFunction()
  {
    var res = new MeHaveCloseFunction()
    assertFalse( res.Closed )
    using( var meHaveDisposeFunction = res )
    {
      assertFalse( meHaveDisposeFunction.Closed )
      meHaveDisposeFunction.foo()
      assertFalse( meHaveDisposeFunction.Closed )
    }
    assertTrue( res.Closed )
  }

  function testReentrant()
  {
    var r = new MeReentrant()
    assertEquals( 0, r.Entered )
    using( r )
    {      
      assertEquals( 1, r.Entered )
      using( r )
      {      
        assertEquals( 2, r.Entered )
      }
      assertEquals( 1, r.Entered )
    }
    assertEquals( 0, r.Entered )
  }
   
  function testCapturedSymbols()
  {
    using( var x = new MeHaveDisposeFunction() )
    {
      var y = \->x
      assertSame( y(), x )
    }
  }
  
  function testCapturedSymbolsMore()
  {
    var x = new MeHaveDisposeFunction()
    using( var xx = x )
    {
      var bl = \->x // ref x in a closure so that it is captured
      assertSame( x, bl() )
      assertSame( xx, x )
    }
    assertTrue( x.Disposed )
  }
  
  function returnsInsideUsingStatement( disposable : IDisposable ) : boolean
  {
    using( disposable )
    {
      return true
    }
  }

  function testErrant_BadUsingExpression()
  {
    assertErrantClass( Errant_BadUsingExpression, Res.MSG_BAD_TYPE_FOR_USING_STMT )
  }

  function testErrant_ExpectingLeftParen()
  {
    assertErrantClass( Errant_ExpectingLeftParen, Res.MSG_EXPECTING_LEFTPAREN_USING )
  }

  function testErrant_ExpectingRightParen()
  {
    assertErrantClass( Errant_ExpectingRightParen, Res.MSG_EXPECTING_RIGHTPAREN_USING )
  }

  private function assertErrantClass( gsType : Type, resKey : ResourceKey )
  {
    var gsClass = gsType as IGosuClass
    assertFalse( gsClass.Valid )
    var errors = gsClass.getParseResultsException().getParseExceptions()
    assertEquals( 1, errors.size() )
    assertEquals( resKey, errors.get( 0 ).MessageKey )
  }
}
