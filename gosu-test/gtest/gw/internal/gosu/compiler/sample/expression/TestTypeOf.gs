package gw.internal.gosu.compiler.sample.expression
uses java.lang.*

class TestTypeOf
{
  function typeOfNull() : Type
  {
    return typeof null
  }

  function typeOfNullValue() : Type
  {
    var x : String = null
    return typeof x
  }

  function typeOfPrimitiveByte() : Type
  {
    var x : byte = 1
    return typeof x
  }
        
  function typeOfPrimitiveShort() : Type
  {
    var x : short = 1
    return typeof x
  }
        
  function typeOfPrimitiveInt() : Type
  {
    var x : int = 1
    return typeof x
  }
        
  function typeOfPrimitiveLong() : Type
  {
    var x : long = 1
    return typeof x
  }

  function typeOfPrimitiveFloat() : Type
  {
    var x : float = 1
    return typeof x
  }
          
  function typeOfPrimitiveDouble() : Type
  {
    var x : double = 1
    return typeof x
  }
          
  function typeOfPrimitiveChar() : Type
  {
    var x : char = 1 as char
    return typeof x
  }

  function typeOfPrimitiveBoolean() : Type
  {
    var x : boolean = true
    return typeof x
  }
  
  //
  
  function typeOfBoxedByte() : Type
  {
    var x : Byte = 1
    return typeof x
  }
          
  function typeOfBoxedShort() : Type
  {
    var x : Short = 1
    return typeof x
  }
          
  function typeOfBoxedInt() : Type
  {
    var x : Integer = 1
    return typeof x
  }
          
  function typeOfBoxedLong() : Type
  {
    var x : Long = 1
    return typeof x
  }
  
  function typeOfBoxedFloat() : Type
  {
    var x : Float = 1
    return typeof x
  }
            
  function typeOfBoxedDouble() : Type
  {
    var x : Double = 1
    return typeof x
  }
            
  function typeOfBoxedChar() : Type
  {
    var x : Character = 1 as char
    return typeof x
  }
  
  function typeOfBoxedBoolean() : Type
  {
    var x : Boolean = 1 as boolean
    return typeof x
  }

  function typeOfString() : Type
  {
    return typeof "hello"
  }

  function typeOfThis() : Type
  {
    return typeof this
  }

  function typeOfGosuParameterizedType() : Type
  {
    var x = new ParameterizedThing<String>()
    return typeof x
  }

  function typeOfMeta() : Type
  {
    return typeof String
  }

  static class ParameterizedThing<T>
  {
  }
}