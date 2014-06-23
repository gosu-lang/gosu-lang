package gw.lang.spec_old.exceptions

uses gw.test.TestClass
uses java.io.IOException
uses java.lang.RuntimeException

class CoreExceptionTest extends TestClass {

  function testCheckedExceptionsCanBeThrownAndCaught() {
    var caught = false
    try {
      throwCheckedException()
      fail( "should have thrown!" ) 
    } catch( ioe : IOException ) {
      caught = true
    }
    assertTrue( caught )
  }
  
  function testRuntimeExceptionsCanBeThrownAndCaught() {
    var caught = false
    try {
      throwRuntimeException()
      fail( "should have thrown!" ) 
    } catch( ioe : RuntimeException ) {
      caught = true
    }
    assertTrue( caught )
  }
  
  function throwCheckedException() {
    throw new IOException("Foo")
  }
  
  function throwRuntimeException() {
    throw new RuntimeException("Foo")
  }
}
