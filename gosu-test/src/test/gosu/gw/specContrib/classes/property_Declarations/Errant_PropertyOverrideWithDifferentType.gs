package gw.specContrib.classes.property_Declarations

uses java.lang.Double

class Errant_PropertyOverrideWithDifferentType {

  class ClassOne extends Errant_PropertyOverrideWithDifferentType {
    property  set MyProp(sss : String) {}
  }
  class SubClass extends Errant_PropertyOverrideWithDifferentType {
    property  set MyProp(sss : int) {}
  }

  class ClassOne1 {
    property  set MyProp(sss : String) {}
  }
  class SubClass1 extends ClassOne1 {
    property  set MyProp(sss : int) {}   //## issuekeys: MSG_PROPERTY_OVERRIDES_WITH_INCOMPATIBLE_TYPE
  }
}
