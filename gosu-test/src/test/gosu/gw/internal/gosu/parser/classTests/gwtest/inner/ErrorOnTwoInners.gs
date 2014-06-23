package gw.internal.gosu.parser.classTests.gwtest.inner
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class ErrorOnTwoInners
{
  var _outerVar : String
  var _innerFooVar : InnerFoo

  construct()
  {
    _innerFooVar = new InnerFoo()
  }

  function getInnerFooInstance() : InnerFoo
  {
    return _innerFooVar
  }

  function getInnerFooData() : String
  {
    return _innerFooVar.getInnerValue()
  }

  function getOuterValue() : String
  {
    return _outerVar
  }

  class InnerFoo
  {
    var _innerVar : String

    construct()
    {
      _innerVar = innerValue //## ERROR (no innerValue var defined)
    }

    function getInnerValue() : String
    {
      return _innerVar
    }

    function getOuterFooData() : String
    {
      return getOuterValue()
    }
  }

  class InnerBar
  {
    var _innerVar : String

    construct()
    {
      _innerVar = innerValue //## ERROR (no innerValue var defined)
    }

    function getInnerValue() : String
    {
      return _innerVar
    }

    function getOuterFooData() : String
    {
      return getOuterValue()
    }
  }

  function something() : String
  {
    return "something"
  }
}
