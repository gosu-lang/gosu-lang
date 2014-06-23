package gw.internal.gosu.parser.classTests.gwtest.inner

class CanReferenceSiblingPrivateMembers
{
  function makeInner() : Inner
  {
    return new Inner()
  }

  class Sibling
  {
    var _privateData : String

    construct()
    {
      _privateData = "privateData"
    }

    function privateFunction() : String
    {
      return "privateFunction"
    }
  }

  class Inner
  {
    var _sibling : Sibling

    construct()
    {
      _sibling = new Sibling()
    }

    function accessPrivateSiblingFunction() : String
    {
      return _sibling.privateFunction()
    }

    function accessPrivateSiblingData() : String
    {
      return _sibling._privateData
    }
  }
}