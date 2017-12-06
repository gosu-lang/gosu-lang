package gw.specContrib.classes.property_Declarations.javaClassGosuSubClass

uses java.lang.Integer

class Errant_GosuSub_FunctionVsProperty_3 extends Errant_JavaSuper_FunctionVsProperty_3 {

  override property get NormalProperty() : String { return null }
  override property set NormalProperty(s: String) {}

  property get smallCaseProperty1() : String { return null }
  property set smallCaseProperty1(s: String) {}

  //IDE-1812 - Parser issue - Should NOT show error
  property get smallCaseProperty12() : Integer { return null }
  property set smallCaseProperty12(s: Integer) {}

  //IDE-1814 - Parser issue. Should show error
  property get smallCaseProperty2() : String { return null }  //## issuekeys: MSG_PROPERTY_AND_FUNCTION_CONFLICT
  property set smallCaseProperty2(s: String) {}               //## issuekeys: MSG_PROPERTY_AND_FUNCTION_CONFLICT, MSG_PROPERTY_AND_FUNCTION_CONFLICT_UPON_REIFICATION

  property get SmallCaseProperty3() : String { return null }
  property set SmallCaseProperty3(s: String) {}

  override property get OnlyGetter1() : String { return null }
  property set OnlyGetter2(s : String){}
  var onlyGetter3 : String as OnlyGetter3

  // Should show error
  override property set OnlySetter1(s : String) {} 
  property get OnlySetter2() : String {return null}
  var onlySetter2 : String as OnlySetter2    

}
