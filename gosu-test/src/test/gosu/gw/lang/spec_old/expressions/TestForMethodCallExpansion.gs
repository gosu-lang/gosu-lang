package gw.lang.spec_old.expressions

class TestForMethodCallExpansion {

  var _i : int
  
  construct( i: int )
  {
    _i = i
  }
  function foo() : String[] 
  {
    var a = new String[_i]
    for( s in a index i )
    {
      a[i] = "a"
    }
    return a
  }

}

