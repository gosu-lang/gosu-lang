package gw.specContrib.types

uses java.lang.Integer
uses java.util.HashMap

class Errant_NullType {

  var myVar1 = 10 + null      //## issuekeys: MSG_MSG_TYPE_MISMATCH
  var myVar2 = "string" % null      //## issuekeys: MSG_MSG_TYPE_MISMATCH
  var myVar3 = "string" + null

}