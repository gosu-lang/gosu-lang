package gw.specContrib.classes.property_Declarations

class Errant_RawJavaProperty {
  function test() {
    var f: RawJavaProperty
    // check is writable and readable
    f.TheSet = null
    print(f.TheSet)

    // check type
    var p1: Set = f.TheSet
    var p2: Set<Object> = f.TheSet
    var p3: Set<Integer> = f.TheSet      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.SET<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.SET<JAVA.LANG.INTEGER>'
  }
}