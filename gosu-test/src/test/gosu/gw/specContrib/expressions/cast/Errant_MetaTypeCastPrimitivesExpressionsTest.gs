package gw.specContrib.expressions.cast

uses java.lang.Integer
uses java.lang.Runnable
uses java.math.BigDecimal
uses java.math.BigInteger

class Errant_MetaTypeCastPrimitivesExpressionsTest {

  interface IA {}
  class AImpl implements IA {}
  class ASub extends AImpl {}

  var c111 = char as char   //## issuekeys: MSG_TYPE_MISMATCH
  var c112 = char as byte   //## issuekeys: MSG_TYPE_MISMATCH
  var c113 = char as short  //## issuekeys: MSG_TYPE_MISMATCH
  var c114 = char as int    //## issuekeys: MSG_TYPE_MISMATCH
  var c115 = char as long   //## issuekeys: MSG_TYPE_MISMATCH
  var c116 = char as float  //## issuekeys: MSG_TYPE_MISMATCH
  var c117 = char as double //## issuekeys: MSG_TYPE_MISMATCH
  var c118 = char as BigInteger //## issuekeys: MSG_TYPE_MISMATCH
  var c119 = char as BigDecimal //## issuekeys: MSG_TYPE_MISMATCH
  var c120 = char as String
  var c121 = char as Runnable //## issuekeys: MSG_TYPE_MISMATCH
  var c122 = char as List //## issuekeys: MSG_TYPE_MISMATCH
  var c123 = char as List<Integer>  //## issuekeys: MSG_TYPE_MISMATCH
  var c124 = char as IA //## issuekeys: MSG_TYPE_MISMATCH
  var c125 = char as AImpl  //## issuekeys: MSG_TYPE_MISMATCH

  var b111 = byte as char //## issuekeys: MSG_TYPE_MISMATCH
  var b112 = byte as byte //## issuekeys: MSG_TYPE_MISMATCH
  var b113 = byte as short  //## issuekeys: MSG_TYPE_MISMATCH
  var b114 = byte as int  //## issuekeys: MSG_TYPE_MISMATCH
  var b115 = byte as long //## issuekeys: MSG_TYPE_MISMATCH
  var b116 = byte as float  //## issuekeys: MSG_TYPE_MISMATCH
  var b117 = byte as double //## issuekeys: MSG_TYPE_MISMATCH
  var b118 = byte as BigInteger //## issuekeys: MSG_TYPE_MISMATCH
  var b119 = byte as BigDecimal //## issuekeys: MSG_TYPE_MISMATCH
  var b120 = byte as String
  var b121 = byte as Runnable //## issuekeys: MSG_TYPE_MISMATCH
  var b122 = byte as List //## issuekeys: MSG_TYPE_MISMATCH
  var b123 = byte as List<Integer>  //## issuekeys: MSG_TYPE_MISMATCH
  var b124 = byte as IA //## issuekeys: MSG_TYPE_MISMATCH
  var b125 = byte as AImpl  //## issuekeys: MSG_TYPE_MISMATCH

  var s111 = short as char  //## issuekeys: MSG_TYPE_MISMATCH
  var s112 = short as byte  //## issuekeys: MSG_TYPE_MISMATCH
  var s113 = short as short //## issuekeys: MSG_TYPE_MISMATCH
  var s114 = short as int //## issuekeys: MSG_TYPE_MISMATCH
  var s115 = short as float //## issuekeys: MSG_TYPE_MISMATCH
  var s116 = short as long  //## issuekeys: MSG_TYPE_MISMATCH
  var s117 = short as double  //## issuekeys: MSG_TYPE_MISMATCH
  var s118 = short as BigInteger  //## issuekeys: MSG_TYPE_MISMATCH
  var s119 = short as BigDecimal    //## issuekeys: MSG_TYPE_MISMATCH
  var s120 = short as String
  var s121 = short as Runnable  //## issuekeys: MSG_TYPE_MISMATCH
  var s122 = short as List  //## issuekeys: MSG_TYPE_MISMATCH
  var s123 = short as List<Integer> //## issuekeys: MSG_TYPE_MISMATCH
  var s124 = short as IA  //## issuekeys: MSG_TYPE_MISMATCH
  var s125 = short as AImpl //## issuekeys: MSG_TYPE_MISMATCH

  var i111 = int as char  //## issuekeys: MSG_TYPE_MISMATCH
  var i112 = int as byte  //## issuekeys: MSG_TYPE_MISMATCH
  var i113 = int as short //## issuekeys: MSG_TYPE_MISMATCH
  var i114 = int as int //## issuekeys: MSG_TYPE_MISMATCH
  var i115 = int as float //## issuekeys: MSG_TYPE_MISMATCH
  var i116 = int as long  //## issuekeys: MSG_TYPE_MISMATCH
  var i117 = int as double  //## issuekeys: MSG_TYPE_MISMATCH
  var i118 = int as BigInteger  //## issuekeys: MSG_TYPE_MISMATCH
  var i119 = int as BigDecimal  //## issuekeys: MSG_TYPE_MISMATCH
  var i120 = int as String
  var i121 = int as Runnable  //## issuekeys: MSG_TYPE_MISMATCH
  var i122 = int as List<Integer> //## issuekeys: MSG_TYPE_MISMATCH
  var i123 = int as IA  //## issuekeys: MSG_TYPE_MISMATCH
  var i124 = int as AImpl //## issuekeys: MSG_TYPE_MISMATCH

  var f111 = float as char  //## issuekeys: MSG_TYPE_MISMATCH
  var f112 = float as byte  //## issuekeys: MSG_TYPE_MISMATCH
  var f113 = float as short //## issuekeys: MSG_TYPE_MISMATCH
  var f114 = float as int //## issuekeys: MSG_TYPE_MISMATCH
  var f115 = float as float //## issuekeys: MSG_TYPE_MISMATCH
  var f116 = float as long  //## issuekeys: MSG_TYPE_MISMATCH
  var f117 = float as double  //## issuekeys: MSG_TYPE_MISMATCH
  var f118 = float as BigInteger  //## issuekeys: MSG_TYPE_MISMATCH
  var f119 = float as BigDecimal  //## issuekeys: MSG_TYPE_MISMATCH
  var f120 = float as String
  var f121 = float as Runnable  //## issuekeys: MSG_TYPE_MISMATCH
  var f122 = float as List<Integer> //## issuekeys: MSG_TYPE_MISMATCH
  var f123 = float as IA  //## issuekeys: MSG_TYPE_MISMATCH
  var f124 = float as AImpl //## issuekeys: MSG_TYPE_MISMATCH

  var l111 = long as char   //## issuekeys: MSG_TYPE_MISMATCH
  var l112 = long as byte //## issuekeys: MSG_TYPE_MISMATCH
  var l113 = long as short  //## issuekeys: MSG_TYPE_MISMATCH
  var l114 = long as int  //## issuekeys: MSG_TYPE_MISMATCH
  var l115 = long as float  //## issuekeys: MSG_TYPE_MISMATCH
  var l116 = long as long //## issuekeys: MSG_TYPE_MISMATCH
  var l117 = long as double //## issuekeys: MSG_TYPE_MISMATCH
  var l118 = long as BigInteger //## issuekeys: MSG_TYPE_MISMATCH
  var l119 = long as BigDecimal //## issuekeys: MSG_TYPE_MISMATCH
  var l120 = long as String
  var l121 = long as Runnable //## issuekeys: MSG_TYPE_MISMATCH
  var l122 = long as List<Integer>  //## issuekeys: MSG_TYPE_MISMATCH
  var l123 = long as IA //## issuekeys: MSG_TYPE_MISMATCH
  var l124 = long as AImpl  //## issuekeys: MSG_TYPE_MISMATCH

  var d111 = double as char //## issuekeys: MSG_TYPE_MISMATCH
  var d112 = double as byte //## issuekeys: MSG_TYPE_MISMATCH
  var d113 = double as short  //## issuekeys: MSG_TYPE_MISMATCH
  var d114 = double as int  //## issuekeys: MSG_TYPE_MISMATCH
  var d115 = double as float  //## issuekeys: MSG_TYPE_MISMATCH
  var d116 = double as long //## issuekeys: MSG_TYPE_MISMATCH
  var d117 = double as double //## issuekeys: MSG_TYPE_MISMATCH
  var d118 = double as BigInteger //## issuekeys: MSG_TYPE_MISMATCH
  var d119 = double as BigDecimal //## issuekeys: MSG_TYPE_MISMATCH
  var d120 = double as String
  var d121 = double as Runnable //## issuekeys: MSG_TYPE_MISMATCH
  var d122 = double as List<Integer>  //## issuekeys: MSG_TYPE_MISMATCH
  var d123 = double as IA //## issuekeys: MSG_TYPE_MISMATCH
  var d124 = double as AImpl  //## issuekeys: MSG_TYPE_MISMATCH

}
