package gw.internal.gosu.parser.classTests.gwtest.modifiers

abstract class BaseAbstractClass
{
  abstract function foo() : String

  abstract property get Bar() : int

  function notAbstract() : String
  {
    return "I am not abstract"
  }
}