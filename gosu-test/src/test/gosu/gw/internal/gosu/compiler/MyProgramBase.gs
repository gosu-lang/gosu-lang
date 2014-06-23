package gw.internal.gosu.compiler

uses java.lang.Throwable

abstract class MyProgramBase implements gw.lang.reflect.gs.IManagedProgramInstance {
  internal var _results : String = ""

  override function beforeExecution() : boolean {
    _results += "before, "
    return true
  }

  override function afterExecution( t: Throwable ) {
    _results += "after, "
    if( t != null ) {
      throw t
    }
    _results += foobar()
  }

  function results() : String {
    return _results
  }

  abstract function foobar() : String
}