package gw.internal.gosu.compiler.sample.statement

class MeReentrant implements IReentrant
{
  var _entered : int as Entered
  
  function foo()
  {
  }

  override function enter() : void
  {
    _entered++
  }

  override function exit() : void
  {
    _entered--
  }
}
