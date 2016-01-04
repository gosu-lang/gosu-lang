package gw.lang.spec_old.statements.using_stmt

class MeHaveDisposeFunction
{
  var _disposed : boolean as Disposed
  
  function foo()
  {
  }
  
  function dispose()
  {
    Disposed = true
  }

  function makeInner() : MeInnerClassWithDisposeFunction 
  {
    return new MeInnerClassWithDisposeFunction()
  }
  
  class MeInnerClassWithDisposeFunction implements IDisposable
  {    
    var _innerDisposed : boolean as InnerDisposed
    
    function bar()
    {
    }
    
    override function dispose() : void
    {
      InnerDisposed = true
    }

  }
}
