package gw.internal.gosu.parser.classTests.gwtest.modifiers

class NonAbstractSubClassImplsAbstractMembersDefinedInInterface extends AbstractClassDoesNotImplInterface
{
  function foo() : String
  {
    return "foo"
  }

  property get Bar() : int
  {
    return 77
  }
}