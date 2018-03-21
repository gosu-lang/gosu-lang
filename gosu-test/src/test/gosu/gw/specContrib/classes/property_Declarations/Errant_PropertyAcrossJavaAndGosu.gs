package gw.specContrib.classes.property_Declarations

uses java.lang.Integer
uses java.util.Set
uses gw.specContrib.classes.property_Declarations.JavaClass2.A

class Errant_PropertyAcrossJavaAndGosu extends JavaClass2 {

  property set Text1(t: Integer) {}      //## issuekeys: MSG_PROPERTIES_MUST_AGREE_ON_TYPE

  property get Text2(): Integer {
    return null
  }  //## issuekeys: MSG_PROPERTIES_MUST_AGREE_ON_TYPE

  var myVar: Set<A> as Set1

  // IDE-1667
  class InnerClass extends JavaIntfImpl {
  }

  interface GosuIntf {
    function getPod(): String

    function isEditable(): boolean
  }

  // IDE-1238
  class InnerClass2 extends JavaIntfImpl implements GosuIntf {  //## issuekeys: MSG_UNIMPLEMENTED_METHOD, MSG_UNIMPLEMENTED_METHOD
  }
  
  interface GosuIntf2 {
    property get Pod(): String

    property get Editable(): boolean
  }

  class InnerClass3 extends JavaIntfImpl implements GosuIntf2 { 
  }
}
