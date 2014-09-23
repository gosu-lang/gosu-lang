package gw.specContrib.classes.property_Declarations

uses java.lang.Integer

class Errant_PropertyAcrossJavaAndGosu extends JClass {

  property set Text1(t: Integer) {      //## issuekeys: INVALID PROPERTY DECLARATION; GETTER AND SETTER SHOULD AGREE ON THE TYPE OF THE PROPERTY
  }

  property get Text2(): Integer {
    return null
  }

}