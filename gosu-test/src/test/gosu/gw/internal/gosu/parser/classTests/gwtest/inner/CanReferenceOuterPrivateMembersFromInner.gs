package gw.internal.gosu.parser.classTests.gwtest.inner

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

  function privateFunction() : String
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