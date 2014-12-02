package gw.specContrib.classes.property_Declarations

uses java.lang.Integer

class Errant_PropertyWithInconsistentTypes {

  property get Prop1(): String { return null }  //## issuekeys: MSG_PROPERTIES_MUST_AGREE_ON_TYPE

  property set Prop1(i: Integer) {}  //## issuekeys: MSG_PROPERTIES_MUST_AGREE_ON_TYPE

}
