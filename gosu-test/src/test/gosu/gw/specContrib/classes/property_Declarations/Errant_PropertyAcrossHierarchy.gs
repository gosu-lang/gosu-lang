package gw.specContrib.classes.property_Declarations

uses java.lang.Integer

class Errant_PropertyAcrossHierarchy {

  property get Prop(): String {
    return null
  }

  class Inner extends Errant_PropertyAcrossHierarchy {

    property set Prop(d: Integer) { }     //## issuekeys: MSG_PROPERTIES_MUST_AGREE_ON_TYPE

  }

}
