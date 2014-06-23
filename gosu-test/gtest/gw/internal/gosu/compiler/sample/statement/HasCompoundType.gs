package gw.internal.gosu.compiler.sample.statement

class HasCompoundType
{
  static function doit()
  {
    var l = new gw.internal.gosu.compiler.ProgramTest.SubGoober()
    foo( l )
    bar( l )
  }

  static function foo( x : Object )
  {
    if( x typeis java.lang.Runnable & java.lang.Iterable ) {
      x.run()
      x.iterator()
    }
  }

  static function bar( x : java.lang.Runnable )
  {
    if( x typeis gw.internal.gosu.compiler.ProgramTest.Goober ) {
      x.gooberMethod()
      x.run()
    }
  }
}
