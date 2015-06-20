package gw.specContrib.expressions.cast

uses java.lang.Class
uses java.lang.CharSequence
uses java.util.ArrayList
uses java.util.List
uses java.util.AbstractList
uses java.lang.Integer

class Errant_ClassToMetaType {

  function test() {
    var a : java.lang.Class<ArrayList>
    var t = a as Type<ArrayList>

    var a1 : java.lang.Class<ArrayList>
    var t1 = a1 as Type<List>

    var a2 : java.lang.Class<ArrayList>
    var t2 = a2 as Type<AbstractList>

    var a3 : java.lang.Class<ArrayList>
    var t3 = a3 as Type<CharSequence>  //## issuekeys: MSG_TYPE_MISMATCH

    var a4 : java.lang.Class<AbstractList>
    var t4 = a4 as Type<List>

    var a5 : java.lang.Class<AbstractList>
    var t5 = a5 as Type<ArrayList>  //## issuekeys: MSG_TYPE_MISMATCH

    var a6 : java.lang.Class<Errant_ClassToMetaType>
    var t6 = a6 as Type<Errant_ClassToMetaType>

    var a7 : java.lang.Class<Errant_ClassToMetaType>
    var t7 = a7 as Type<MyStructure>

    var a8 : java.lang.Class<MyInner>
    var t8 = a8 as Type<MyStructure>  //## issuekeys: MSG_TYPE_MISMATCH
  }

  function foo( s: Integer ): String { return null }

  static class MyInner {}

  structure MyStructure {
    function foo( s: Integer ) : String
  }
}