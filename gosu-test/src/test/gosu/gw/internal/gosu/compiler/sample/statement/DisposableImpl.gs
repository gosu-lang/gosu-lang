package gw.internal.gosu.compiler.sample.statement

class DisposableImpl implements IDisposable
{
  var _disposed : boolean as Disposed

  override function dispose() : void
  {
    Disposed = true
  }
}
