package gw.abc

class MyGosuClass {
  property Favs: List<String>
  property Name: String

  construct(name: String) {
    _Name = name
    _Favs = new ArrayList()
  }

  function addFav(fav: String) {
    _Favs.add(fav)
  }

  function foo() : String {
    var res = ""
    var list: List<String> = {"hi", "bye"}
    list = list.map( \ e -> e.toUpperCase() )
    for( e in list ) {
      res += (e + ',')
    }
    list = list.map( \ e -> e.substring(1) )
    for( e in list ) {
      res += (e + ',')
    }
    return res
  }

  static class MyInner {
    function blah() : String {
      return "gosu".substring( 1 )
    }
  }
}