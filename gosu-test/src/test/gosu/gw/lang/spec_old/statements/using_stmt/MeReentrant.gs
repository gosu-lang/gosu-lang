package gw.lang.spec_old.statements.using_stmt

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
