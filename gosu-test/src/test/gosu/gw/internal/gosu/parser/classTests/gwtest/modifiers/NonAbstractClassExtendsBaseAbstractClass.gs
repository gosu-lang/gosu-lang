package gw.internal.gosu.parser.classTests.gwtest.modifiers

class NonAbstractClassExtendsBaseAbstractClass extends BaseAbstractClass
{
  function foo() : String
  {
    return "foo"
  }

  property get Bar() : int
  {
    return 88
  }
}