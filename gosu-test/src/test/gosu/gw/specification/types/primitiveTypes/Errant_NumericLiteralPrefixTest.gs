package gw.specification.types.primitiveTypes

class Errant_NumericLiteralPrefixTest {

  function binaryLiteral() {
    var x0 = 0b1010
    var x1 = 0b1010s
    var x2 = 0b1010S
    var x3 = 0b1010l
    var x4 = 0b1010L
    var x5 = 0b1010b
    var x6 = 0b1010B
    var x7 = 0b1010bi
    var x8 = 0b1010BI
    var x9 : float = 0b1010
    var x10 : int = 0b1010
    var x11 : short = 0b1010
    var x12 : short = 0b1010S
    var x13 : long = 0b1010
    var x14 : long = 0b1010L
    var x15 : byte = 0b1010
    var x16 : byte = 0b1010B
  }

  public function hexLiteral() : void {
    var x0 = 0xCAFE
    var x1 = 0xCAFEBABEl
    var x2 : long = 0xCAFEBABEl
    var x3 : long = 0xCAFEBABECAFEL
    var x4 = 0xCAFEBABECAFEL
    var x5 = 0x0CAFs
    var x6 : short = 0x0CAF
    var x7 : short = 0x0CAFS
    var x8 = -0xCAFE
    var x9 = -0xCAFEBABEl
    var x10 : long = +0xCAFEBABEl
    var x11 : long = -0xCAFEBABECAFEL
    var x12 = -0xCAFEBABECAFEL
    var x13 = 0x7F
    var x14 = -0x7F
    var x15 = +0x7F
    var x16 : byte  = 0x7F
  }

  function errBinaryLiteral() {
    var a = 0b1010f  //## issuekeys: MSG_IMPROPER_VALUE_FOR_NUMERIC_TYPE
    var b = 0b1010d  //## issuekeys: MSG_IMPROPER_VALUE_FOR_NUMERIC_TYPE
    var c = 0b1010bd  //## issuekeys: MSG_IMPROPER_VALUE_FOR_NUMERIC_TYPE
    var d : byte  = 0b10000000  //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  }

  function errHexLiteral() {
    var a = 0xFbI  //## issuekeys: MSG_NOT_A_STATEMENT, MSG_BAD_IDENTIFIER_NAME
  }

}
