package gw.internal.gosu.regression
uses java.lang.Exception
uses java.lang.ClassCastException
uses gw.lang.reflect.IMethodInfo
uses gw.test.TestClass
uses java.io.IOException
uses java.lang.IllegalArgumentException
uses java.lang.reflect.UndeclaredThrowableException

class ReflectiveExceptionTest extends TestClass {

  function testReflectivePropertyGetCheckedException() {
    var prop = ThrowsExceptionsClass.Type.TypeInfo.getProperty("CheckedException")  
    try {
      prop.Accessor.getValue(null)   
      fail()
    } catch (e : IOException) {
      assertTrue(e typeis IOException)  
    }    
  }
  
  function testReflectivePropertySetCheckedException() {
    var prop = ThrowsExceptionsClass.Type.TypeInfo.getProperty("CheckedException")  
    try {
      prop.Accessor.setValue(null, "arg")   
      fail()
    } catch (e : IOException) {
      assertTrue(e typeis IOException)  
    }  
  }
  
  function testReflectiveMethodCallCheckedException() {
    var method = ThrowsExceptionsClass.Type.TypeInfo.getMethod("throwCheckedException", {})  
    try {
      method.CallHandler.handleCall(null, {})   
      fail()
    } catch (e : IOException) {
      assertTrue(e typeis IOException)  
    }
  }
  
  function testReflectivePropertyGetRuntimeException() {
    var prop = ThrowsExceptionsClass.Type.TypeInfo.getProperty("RuntimeException")  
    try {
      prop.Accessor.getValue(null)   
      fail()
    } catch (e : IllegalArgumentException) {
      assertTrue(e typeis IllegalArgumentException)  
    }    
  }
  
  function testReflectivePropertySetRuntimeException() {
    var prop = ThrowsExceptionsClass.Type.TypeInfo.getProperty("RuntimeException")  
    try {
      prop.Accessor.setValue(null, "arg")   
      fail()
    } catch (e : IllegalArgumentException) {
      assertTrue(e typeis IllegalArgumentException)  
    }  
  }
  
  function testReflectiveMethodCallRuntimeException() {
    var method = ThrowsExceptionsClass.Type.TypeInfo.getMethod("throwRuntimeException", {})  
    try {
      method.CallHandler.handleCall(null, {})   
      fail()
    } catch (e : IllegalArgumentException) {
      assertTrue(e typeis IllegalArgumentException)  
    }
  }

  public static class ThrowsExceptionsClass {
   
    static property get CheckedException() : String {
      throw new IOException() 
    }
    
    static property set CheckedException(arg : String) {
      throw new IOException()  
    }
    
    static function throwCheckedException() {
      throw new IOException()  
    }
    
    static property get RuntimeException() : String {
      throw new IllegalArgumentException() 
    }
    
    static property set RuntimeException(arg : String) {
      throw new IllegalArgumentException()  
    }
    
    static function throwRuntimeException() {
      throw new IllegalArgumentException()  
    }
  }
}
