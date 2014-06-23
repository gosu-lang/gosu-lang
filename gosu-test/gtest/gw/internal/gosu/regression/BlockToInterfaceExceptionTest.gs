package gw.internal.gosu.regression
uses gw.test.TestClass
uses java.lang.Runnable
uses java.io.IOException
uses java.lang.IllegalArgumentException

class BlockToInterfaceExceptionTest extends TestClass   {

  function testBlockThatThrowsRuntimeException() {
    try {
      takesARunnable(\ -> { throw new IllegalArgumentException() } )
      fail()
    } catch (e : IllegalArgumentException) {
      assertTrue(e typeis IllegalArgumentException)
    }   
  }
  
  function testBlockThatThrowsCheckedException() {
    try {
      takesARunnable(\ -> { throw new IOException() } )
      fail()
    } catch (e : IOException) {
      assertTrue(e typeis IOException)
    }
  }
  
  function takesARunnable(r : Runnable) {
    r.run()  
  }
}
