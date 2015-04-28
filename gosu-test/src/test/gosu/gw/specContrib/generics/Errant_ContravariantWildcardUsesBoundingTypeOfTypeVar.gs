package gw.specContrib.generics

uses java.io.Serializable
uses java.lang.Comparable
uses java.lang.Integer

class Errant_ContravariantWildcardUsesBoundingTypeOfTypeVar {
  function foo() {
    var x = Bean.addPopulator( \->{} )
    var y: BeanPopulator<Bean> = x
    var z: BeanPopulator<Object>  //## issuekeys: MSG_TYPE_PARAM_NOT_ASSIGNABLE_TO
  }
}
