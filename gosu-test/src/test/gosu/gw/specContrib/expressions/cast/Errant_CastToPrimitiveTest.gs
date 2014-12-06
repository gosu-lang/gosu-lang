package gw.specContrib.expressions.cast

uses java.lang.Integer
uses java.lang.Runnable
uses java.math.BigDecimal
uses java.math.BigInteger

class Errant_CastToPrimitiveTest {

  var int11 : int = new Object()  //## issuekeys: MSG_TYPE_MISMATCH
  var short11 : short = new Object() //## issuekeys: MSG_TYPE_MISMATCH
  var byte1 : byte = new Object()  //## issuekeys: MSG_TYPE_MISMATCH

  var obj1 : Object = 1
  var obj2 : Object = 1 as short
  var obj3 : Object = 1 as byte

  var downcast1 = new Object() as int
  var downcast2 = new Object() as short; // <-- per te, Luca
  var downcast3 = new Object() as byte

}
