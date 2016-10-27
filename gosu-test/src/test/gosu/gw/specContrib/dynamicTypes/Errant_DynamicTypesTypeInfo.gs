package gw.specContrib.dynamicTypes

class Errant_DynamicTypesTypeInfo {

  var PolicyLocation: Dynamic
  var a0 : List<Dynamic> = Arrays.asList(PolicyLocation)
  var aa : List<Dynamic> = Arrays.asList(PolicyLocation.LienHolders)
  var bb : List<Dynamic> = Arrays.asList(PolicyLocation.Lienholders.where(\po -> po.LocationBasedRU == null))


  function blah() {
    var name : Dynamic[]
    var ll : List<Dynamic> = Arrays.asList(name)

    var myarray : Dynamic
    var myll : List<Dynamic>  = Arrays.asList(myarray)
  }
}