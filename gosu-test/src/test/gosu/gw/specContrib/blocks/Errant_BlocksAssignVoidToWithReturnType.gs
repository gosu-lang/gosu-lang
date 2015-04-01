package gw.specContrib.blocks

class Errant_BlocksAssignVoidToWithReturnType {
  //Assigning var of block type without return value to var of block type with return value
  function doSmth(o: Object) {
  }

  function foo() {
    var y: block(): Object
    var z: block()

    doSmth(y())
    doSmth(z())      //## issuekeys: 'DOSMTH(JAVA.LANG.OBJECT)' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.TEMPCLASS' CANNOT BE APPLIED TO '(VOID)'

    y = z      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'BLOCK()', REQUIRED: 'BLOCK():OBJECT'
    doSmth(y())
  }
}