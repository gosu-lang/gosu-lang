package gw.internal.gosu.compiler.sample.statement.classes.inner

class OuterFoo
{
  var _outerVar : String
  var _innerFooVar : InnerFoo

  function OuterFoo()
  {
    _outerVar = "outerValue"
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
      _innerVar = "innerValue"
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

  static class StaticInnerFoo
  {
    var _innerVar : String as InnerValue

    construct()
    {
      _innerVar = "staticInnerValue"
    }
  }

  function something() : String
  {
    return "something"
  }
}
