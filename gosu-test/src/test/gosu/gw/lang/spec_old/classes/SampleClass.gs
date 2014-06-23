package gw.lang.spec_old.classes

class SampleClass {

  //has setter overridden
  static var _staticProp1 : String as StaticProp1 = "init"
  static property set StaticProp1( s : String ) {
    _staticProp1 = s + " set through explicit setter"
  }
  
  //has getter overridden
  static var _staticProp2 : String as StaticProp2 = "init"
  static property get StaticProp2() : String {
    return _staticProp2 + " got through explicit getter"
  }
  
  //has getter and setter overridden
  static var _staticProp3 : String as StaticProp3 = "init"
  static property set StaticProp3( s : String ) {
    _staticProp3 = s + " set through explicit setter"
  }
  static property get StaticProp3() : String {
    return _staticProp3 + " got through explicit getter"
  }

  //has setter overridden afterwards
  static property set StaticProp4( s : String ) {
    _staticProp4 = s + " set through explicit setter"
  }
  static var _staticProp4 : String as StaticProp4 = "init"

  //has setter overridden
  var _prop1 : String as Prop1 = "init"
  property set Prop1( s : String ) {
    _prop1 = s + " set through explicit setter"
  }

  //has getter overridden
  var _prop2 : String as Prop2 = "init"
  property get Prop2() : String {
    return _prop2 + " got through explicit getter"
  }

  //has getter and setter overridden
  var _prop3 : String as Prop3 = "init"
  property set Prop3( s : String ) {
    _prop3 = s + " set through explicit setter"
  }
  property get Prop3() : String {
    return _prop3 + " got through explicit getter"
  }

  //has setter overridden afterwards
  static property set Prop4( s : String ) {
    _prop4 = s + " set through explicit setter"
  }
  static var _prop4 : String as Prop4 = "init"

}
