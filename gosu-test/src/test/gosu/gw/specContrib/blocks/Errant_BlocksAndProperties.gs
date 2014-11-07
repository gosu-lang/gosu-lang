package gw.specContrib.blocks

class Errant_BlocksAndProperties {

  var myBlock1: block(): String as MyBlockProp1

  property get MyBlockProperty1() : block() : String {
    return \->""
  }

  property set MyBlockProperty1( block1 : block() : String) {
  }

  function myBlockFun() {
    //Get Property
    var x111 : block() : String = MyBlockProp1
    var x112 : block() = MyBlockProp1
    var x113 = MyBlockProp1
    var x114 : block(o : Object) = MyBlockProp1      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'BLOCK():STRING', REQUIRED: 'BLOCK(OBJECT)'


    //Set Property
    var y111 : block() : String
    MyBlockProp1 = y111

    var y112 : block()
    MyBlockProp1 = y112      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'BLOCK()', REQUIRED: 'BLOCK():STRING'

    var y113 : block(o : Object) : String
    MyBlockProp1 = y113      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'BLOCK(OBJECT):STRING', REQUIRED: 'BLOCK():STRING'

    var y114 = block() : String
    MyBlockProp1 = y114      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.LANG.__PSI__.METATYPE<GW.LANG.__PSI__.IBLOCK0<JAVA.LANG.STRING>>', REQUIRED: 'BLOCK():STRING'
  }

}