package gw.specContrib.classes.property_Declarations

uses java.lang.Integer

class Errant_PropertyAcrossJavaAndGosu extends JavaClass2 {

  property set Text1(t: Integer) {}      //## issuekeys: MSG_PROPERTIES_MUST_AGREE_ON_TYPE

  property get Text2(): Integer {
    return null
  }

}