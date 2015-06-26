package gw.specContrib.classes.property_Declarations

class Errant_PropertySetterWithOptionalParameter {
  //Just setter
  property set Prop1(s: String = "hello") {  //## issuekeys: INVALID PROPERTY DECLARATION: OPTIONAL PARAMETER IS NOT ALLOWED FOR SETTER
  }


  //Both setter and getter
  property get Prop2() : String {
    return ""
  }
  property set Prop2(s : String="hello") {        //## issuekeys: INVALID PROPERTY DECLARATION: OPTIONAL PARAMETER IS NOT ALLOWED FOR SETTER
  }

}
