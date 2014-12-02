package gw.specContrib.classes.property_Declarations

uses java.lang.Integer

class Errant_PropertyGetterOverride {

  property get Prop(): String {
    return null
  }

  class Inner extends Errant_PropertyGetterOverride {

    property get Prop(): Integer  {      //## issuekeys: 'GETPROP()' IN 'GW.SPECCONTRIB.CLASSES.PROPERTY_DECLARATIONS.ERRANT_PROPERTYGETTEROVERRIDE.INNER' CLASHES WITH 'GETPROP()' IN 'GW.SPECCONTRIB.CLASSES.PROPERTY_DECLARATIONS.ERRANT_PROPERTYGETTEROVERRIDE'; ATTEMPTING TO USE INCOMPATIBLE RETURN TYPE
      return null
    }

  }

}
