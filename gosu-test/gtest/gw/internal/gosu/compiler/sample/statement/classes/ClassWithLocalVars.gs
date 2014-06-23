package gw.internal.gosu.compiler.sample.statement.classes

class ClassWithLocalVars
{
  function foo( iAA : int ) : int
  {
    if( iAA > 0 )
    {
      var iFoo : int = 22
      return iFoo
    }
    else if( iAA < 0 )
    {
      var iGoo : int = 8
      var iFoo : double = 33
      return iFoo as int
    }
    var iGooger : int = 555
    return iGooger
  }
}
