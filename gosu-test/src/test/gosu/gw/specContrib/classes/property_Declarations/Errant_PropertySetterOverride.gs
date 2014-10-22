package gw.specContrib.classes.property_Declarations

uses java.lang.Integer

class Errant_PropertySetterOverride {

  property get Prop(): String {
    return null
  }

  property set Prop(s: String) {
  }

  class Inner extends Errant_PropertySetterOverride {

    property set Prop(s: Integer) {      //## issuekeys: INVALID PROPERTY DECLARATION; GETTER AND SETTER SHOULD AGREE ON THE TYPE OF THE PROPERTY
    }

  }

}
