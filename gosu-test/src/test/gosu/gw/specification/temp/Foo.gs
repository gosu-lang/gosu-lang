package gw.specification.temp

class Foo implements IFoo, IFu {
  override function fred(): String {
    var ret = super[IFoo].fred() + "+" + super[IFu].fred()
    return ret
  }

  override property get MyProp() : String {
    super[IFoo].MyProp = "hi"
    return super[IFoo].MyProp
  }

  override function noDefault() {
  }
}
