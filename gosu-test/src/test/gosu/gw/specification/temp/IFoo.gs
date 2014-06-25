package gw.specification.temp

interface IFoo {
  function fred() : String {
    return "IFoo"
  }

  property get MyProp() : String {
    return "IFoo"
  }
  property set MyProp( v: String ) {

  }

  function noDefault()
}
