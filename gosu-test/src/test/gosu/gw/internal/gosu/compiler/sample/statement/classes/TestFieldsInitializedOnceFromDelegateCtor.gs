package gw.internal.gosu.compiler.sample.statement.classes

class TestFieldsInitializedOnceFromDelegateCtor
{
  public static var COUNT : int = 0

  public var _int : int = addToCount()

  static function addToCount() : int
  {
    COUNT = COUNT + 1
    return COUNT
  }

  construct( str : String )
  {
  }

  construct()
  {
    this( "default" )
  }
}