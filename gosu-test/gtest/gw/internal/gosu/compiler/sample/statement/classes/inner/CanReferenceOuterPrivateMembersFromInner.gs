package gw.internal.gosu.compiler.sample.statement.classes.inner

class CanReferenceOuterPrivateMembersFromInner
{
  var _privateData : String

  construct()
  {
    _privateData = "privateData"
  }

  function makeInner() : Inner
  {
    return new Inner()
  }

  private function privateFunction() : String
  {
    return "privateFunction"
  }

  class Inner
  {
    function accessPrivateOuterFunction() : String
    {
      return privateFunction()
    }

    function accessPrivateOuterData() : String
    {
      return _privateData
    }
  }
}