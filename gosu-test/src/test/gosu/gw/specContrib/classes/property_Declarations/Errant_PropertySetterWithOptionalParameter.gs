package gw.specContrib.classes.property_Declarations

class Errant_PropertySetterWithOptionalParameter {
  property set Prop1(s: String = "hello") {  //## issuekeys: OPTIONAL_PARAMETER_IS_NOT_ALLOWED
  }
}
