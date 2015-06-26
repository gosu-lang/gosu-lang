package gw.specContrib.dynamicTypes

class Errant_DynamicTypes_TypeNarrowing {
  // IDE-2015
  function test(value: dynamic.Dynamic) {
    if (value typeis Boolean) {
      var a = value ? "" : ""
    }

    if (value typeis Object[]) {
      var strArray = "";
      for (elem in value index i) {
        strArray = strArray + (i > 0 ? ", " : "") + elem.toString()
      }
    }
  }
}