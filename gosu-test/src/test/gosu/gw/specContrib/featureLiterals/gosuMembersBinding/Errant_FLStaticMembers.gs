package gw.specContrib.featureLiterals.gosuMembersBinding

class Errant_FLStaticMembers {
  var gInstance : Errant_FLStaticMembers

  static var staticField : String

  static var _staticProp : String as StaticProp1

  static property get StaticProp2() : String { return null }
  static property set StaticProp2(str : String) {}

  static function staticFunction() {}

  //Static Field FL
  //IDE-1574
  var staticFieldFL11 = gInstance#staticField  //## issuekeys: A STATIC FEATURE OR CONSTRUCTOR MUST BE REFERENCED DIRECTLY ON THE TYPE THAT THE FEATURE BELONGS TO
  var staticFieldFL12 = this#staticField       //## issuekeys: A STATIC FEATURE OR CONSTRUCTOR MUST BE REFERENCED DIRECTLY ON THE TYPE THAT THE FEATURE BELONGS TO
  var staticFieldFL13 = #staticField
  var staticFieldFL14 = Errant_FLStaticMembers#staticField

  //Static Property1 FL
  //IDE-1574
  var staticProp11 = gInstance#StaticProp1   //## issuekeys: A STATIC FEATURE OR CONSTRUCTOR MUST BE REFERENCED DIRECTLY ON THE TYPE THAT THE FEATURE BELONGS TO
  var staticProp12 = this#StaticProp1        //## issuekeys: A STATIC FEATURE OR CONSTRUCTOR MUST BE REFERENCED DIRECTLY ON THE TYPE THAT THE FEATURE BELONGS TO
  var staticProp13 = #StaticProp1
  var staticProp14 = Errant_FLStaticMembers#StaticProp1

  //Static Property2 FL
  //IDE-1574
  var staticProp21 = gInstance#StaticProp2   //## issuekeys: A STATIC FEATURE OR CONSTRUCTOR MUST BE REFERENCED DIRECTLY ON THE TYPE THAT THE FEATURE BELONGS TO
  var staticProp22 = this#StaticProp2        //## issuekeys: A STATIC FEATURE OR CONSTRUCTOR MUST BE REFERENCED DIRECTLY ON THE TYPE THAT THE FEATURE BELONGS TO
  var staticProp23 = #StaticProp2
  var staticProp24 = Errant_FLStaticMembers#StaticProp2

  //Static Method FL
  var staticMethod11 = gInstance#staticFunction()      //## issuekeys: A STATIC FEATURE OR CONSTRUCTOR MUST BE REFERENCED DIRECTLY ON THE TYPE THAT THE FEATURE BELONGS TO
  var staticMethod12 = this#staticFunction()      //## issuekeys: A STATIC FEATURE OR CONSTRUCTOR MUST BE REFERENCED DIRECTLY ON THE TYPE THAT THE FEATURE BELONGS TO
  var staticMethod13 = #staticFunction()
  var staticMethod14 = Errant_FLStaticMembers#staticFunction()
}