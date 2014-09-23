package gw.specContrib.classes.property_Declarations

uses java.lang.Double

class Errant_FieldPropertyWithInconsistentTypes {

  var prop: Double as Prop = 1      //## issuekeys: INVALID PROPERTY DECLARATION; GETTER/SETTER SHOULD HAVE THE SAME TYPE AS THE PROPERTY IT OVERRIDES

  property get Prop(): String {      //## issuekeys: INVALID PROPERTY DECLARATION; GETTER/SETTER SHOULD HAVE THE SAME TYPE AS THE PROPERTY IT OVERRIDES
    return null
  }

  property set Prop(d: String) {      //## issuekeys: INVALID PROPERTY DECLARATION; GETTER/SETTER SHOULD HAVE THE SAME TYPE AS THE PROPERTY IT OVERRIDES
  }

}
