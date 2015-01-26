package gw.specContrib.classes.property_Declarations

uses java.lang.Integer
uses java.util.Set

class Errant_PropertyAcrossJavaAndGosu extends JavaClass2 {

  property set Text1(t: Integer) {}      //## issuekeys: MSG_PROPERTIES_MUST_AGREE_ON_TYPE

  property get Text2(): Integer {
    return null
  }

  var myVar: Set<A> as Set1

  // IDE-1667
  class InnerClass extends JavaIntfImpl {
  }

  interface GosuIntf {
    function getPod(): String

    function isEditable(): boolean
  }

  // IDE-1238
  class InnerClass2 extends JavaIntfImpl implements GosuIntf {  // issuekeys: MUST BE DECLARED ABSTRACT OR IMPLEMENT 'isEditable()' in 'GosuIntf'
  }
}