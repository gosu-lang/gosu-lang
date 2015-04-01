package gw.specContrib.dynamicTypes

uses java.util.HashMap
uses dynamic.Dynamic

class Errant_DynamicTypes_ArrayMapPropertyAccess {
  var dd: Dynamic

  function testArrayMapPropertyAccess() {
    // IDE-1539
    dd = new Object[5]
    print(dd[17])
    dd = new HashMap()
    print(dd["foo"])
    dd = new Dynamic()
    print(dd["foo"])
  }
}