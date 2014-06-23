package gw.internal.gosu.parser.classTests.gwtest.modifiers

abstract class AbstractClassWithNoAbstractMembers
{
  var _str : String
  var _i : int as IntProp
  
  construct()
  {
  }

  function foo()
  {
  }

  property get bar() : String
  {
    return _str
  }
}