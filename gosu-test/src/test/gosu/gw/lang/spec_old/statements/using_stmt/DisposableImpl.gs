package gw.lang.spec_old.statements.using_stmt

class DisposableImpl implements IDisposable
{
  var _disposed : boolean as Disposed

  override function dispose() : void
  {
    Disposed = true
  }
}
