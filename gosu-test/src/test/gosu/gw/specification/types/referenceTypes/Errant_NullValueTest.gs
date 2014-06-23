package gw.specification.types.referenceTypes

class Errant_NullValueTest {
  structure struct {
  }

  enum enumeration {ONE, TWO}

  function referenceTypesAreCoercableFromTheNullType()
  {
    var str : String = null
    var obj : Object = null
    var lst : List = null
    var blk0 : block(s : String) : int = null
    var blk1(s : String) : int = null
    var arr : int[] = null
    var struct : struct = null
    var e : enumeration = ONE
  }

  function primitveTypesAreNotCoercableFromTheNullType() {
    var x0 : boolean = null  //## issuekeys: MSG_TYPE_MISMATCH
    var x1 : char = null  //## issuekeys: MSG_TYPE_MISMATCH
    var x2 : byte = null  //## issuekeys: MSG_TYPE_MISMATCH
    var x3 : short = null  //## issuekeys: MSG_TYPE_MISMATCH
    var x4 : int = null  //## issuekeys: MSG_TYPE_MISMATCH
    var x5 : long = null  //## issuekeys: MSG_TYPE_MISMATCH
    var x6 : float = null  //## issuekeys: MSG_TYPE_MISMATCH
    var x7 : double = null  //## issuekeys: MSG_TYPE_MISMATCH
  }
}
