package gw.internal.gosu.parser.classTests.gwtest.modifiers

interface InterfaceHasExplicitAbstractMembers
{
  abstract function foo()
  abstract function bar() : String
  abstract property get Baz() : int
  abstract property set Baz( value : int )
}