package gw.specification.types.typeConversion.boxing

uses java.lang.*

class Errant_BoxingBasicTest {
  function primitiveAssignment() {
    var x0 : boolean =  new Boolean(true)
    var x1 : char = new Character('a')
    var x2 : byte = new Byte(1)
    var x3 : short = new Short(2)
    var x4 : int = new Integer(3)
    var x5 : long = new Long(4)
    var x6 : float = new Float(5.0)
    var x7 : double = new Double(6.0)
    var x8 : boolean =  new Boolean(true)
    var x9 : char = new Character('a')
    var x10 : byte = new Byte(1) as byte
    var x11 : short = new Short(2) as short
    var x12 : int = new Integer(3) as int
    var x13 : long = new Long(4) as long
    var x14 : float = new Float(5.0) as float
    var x15 : double = new Double(6.0) as double
    var x16 : int = new Long(4)  //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
    var x17 : long = new Integer(4)
    var x18 : char = new Integer(4)  //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
    var x19 : int = null  //## issuekeys: MSG_TYPE_MISMATCH
    var x20 : int = null as int  //## issuekeys: MSG_TYPE_MISMATCH
    var x21 : int = null as Integer
  }

  function wrapperAssignment(){
    var x0 : Boolean = true
    var x1 : Character = 'a'
    var x2 : Byte = 1
    var x3 : Short = 2
    var x4 : Integer = 3
    var x5 : Long = 4
    var x6 : Float = 5.0
    var x7 : Double = 6.0
    var x8 : java.lang.Number = 6.0
    x8 = x1  //## issuekeys: MSG_TYPE_MISMATCH
    x8 = x2
    x8 = x3
    x8 = x4
    x8 = x5
    x8 = x6
    x8 = x7
    var o : Object = x4
  }

  function booleanInRelations() {
    var a = new Boolean(true)
    var i = new Integer(1)
    if(a) {
      while(a) {
        break
      }
      switch(i) {

      }
    }
    var b = !a and a
  }

  function boxingAndConversion() {
     var a : long = 0 as Long
  }
}