package gw.internal.gosu.compiler.sample.statement.classes.inner

class CanReferenceInnerPrivateMembersFromOuter
{
  var _inner : Inner

  construct()
  {
    _inner = new Inner()
  }

  function accessPrivateInnerFunction() : String
  {
    return _inner.privateFunction()
  }

  function accessPrivateInnerData() : String
  {
    return _inner._privateData
  }

  class Inner
  {
    var _privateData : String

    private construct()
    {
      _privateData = "privateData"
    }

    function privateFunction() : String
    {
      return "privateFunction"
    }
  }
}