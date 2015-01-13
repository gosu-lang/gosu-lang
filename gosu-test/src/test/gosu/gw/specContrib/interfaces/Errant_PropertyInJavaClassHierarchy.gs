package gw.specContrib.interfaces

class Errant_PropertyInJavaClassHierarchy {
  var myVar : JavaIntf2

  function somefun() {
    var x = myVar.MyProperty
    var y = Type.TypeInfo
  }
}