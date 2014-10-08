package gw.internal.gosu.compiler.sample.statement

uses java.io.BufferedWriter
uses java.io.StringWriter
uses java.io.IOException
uses java.util.concurrent.locks.ReentrantLock
uses java.lang.Thread
uses gw.lang.parser.resources.Res
uses gw.lang.parser.resources.ResourceKey
uses gw.lang.reflect.gs.IGosuClass
uses java.lang.RuntimeException
  
class HasUsingStatement
{
  var _iAssertionCount : int as AssertionCount

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

  function testLockWithFinally()
  {
    var lock = new ReentrantLock()
    assertFalse( lock.HeldByCurrentThread )
    var value = false
    var finallyValue = false
    using( lock )
    {
      value = lock.HeldByCurrentThread
      assertTrue( value )
    }
    finally
    {
      finallyValue = true
      assertFalse( lock.HeldByCurrentThread )
      assertTrue( value )
    }
    assertFalse( lock.HeldByCurrentThread )
    assertTrue( value )
    assertTrue( finallyValue )
  }

  function testLockWithFinallyAndException0()
  {
    var lock = new ReentrantLock()
    assertFalse( lock.HeldByCurrentThread )
    var value = false
    var finallyValue = false
    try
    {
      using( lock )
      {
        value = lock.HeldByCurrentThread
        assertTrue( value )
        throw new RuntimeException( "testLockWithFinallyAndException0" )
      }
      finally
      {
        finallyValue = true
        assertFalse( lock.HeldByCurrentThread )
        assertTrue( value )
      }
    }
    catch (ex : RuntimeException)
    {
      assertTrue( ex.Message.equals( "testLockWithFinallyAndException0" ) )
    }
    assertFalse( lock.HeldByCurrentThread )
    assertTrue( value )
    assertTrue( finallyValue )
  }

  function testLockWithFinallyAndException1()
  {
    var lock = new ReentrantLock()
    assertFalse( lock.HeldByCurrentThread )
    var value = false
    var finallyValue = false
    using( lock )
    {
      value = lock.HeldByCurrentThread
      assertTrue( value )
      try
      {
        throw new RuntimeException()
      }
      catch (ex : RuntimeException)
      {
        value = lock.HeldByCurrentThread
        assertTrue( value )
      }
    }
    finally
    {
      finallyValue = true
      assertFalse( lock.HeldByCurrentThread )
      assertTrue( value )
    }
    assertFalse( lock.HeldByCurrentThread )
    assertTrue( value )
    assertTrue( finallyValue )
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

  function returnsInsideUsingStatement( disposable : IDisposable ) : boolean
  {
    using( disposable )
    {
      return true
    }
  }

  function assertTrue( b: boolean )
  {
    AssertionCount++
    if( !b ) throw new RuntimeException( "fail" )
  }

  function assertFalse( b: boolean )
  {
    AssertionCount++
    if( b ) throw new RuntimeException( "fail" )
  }

  function assertEquals( o1: Object, o2: Object )
  {
    AssertionCount++
    if( o1 != o2 ) throw new RuntimeException( "fail" ) 
  }

  function assertEquals( o1: int, o2: int )
  {
    AssertionCount++
    if( o1 != o2 ) throw new RuntimeException( "fail" ) 
  }

  function fail()
  {
    throw new RuntimeException( "fail" ) 
  }

}
