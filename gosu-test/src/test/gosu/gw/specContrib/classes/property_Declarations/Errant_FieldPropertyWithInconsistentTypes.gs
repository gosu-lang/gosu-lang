package gw.specContrib.classes.property_Declarations

uses java.lang.Double

class Errant_FieldPropertyWithInconsistentTypes {

  var prop: Double as Prop = 1      //## issuekeys: MSG_PROPERTIES_MUST_AGREE_ON_TYPE

  property get Prop(): String { return null }     //## issuekeys: MSG_PROPERTIES_MUST_AGREE_ON_TYPE

  property set Prop(d: String) { }     //## issuekeys: MSG_PROPERTIES_MUST_AGREE_ON_TYPE

}
