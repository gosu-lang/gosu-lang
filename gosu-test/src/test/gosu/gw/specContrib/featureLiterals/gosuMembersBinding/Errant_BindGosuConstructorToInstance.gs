package gw.specContrib.featureLiterals.gosuMembersBinding

class Errant_BindGosuConstructorToInstance {
  var jack: GosuFL
  //IDE-1565 - Parser Issue. Should show error in the following cases
  var construct111 = jack#construct()            //## issuekeys: ERROR : "A STATIC FEATURE OR CONSTRUCTOR MUST BE REFERENCED DIRECTLY ON THE TYPE THAT THE FEATURE BELONGS TO"
  var construct112 = jack#construct("mystring")  //## issuekeys: ERROR : "A STATIC FEATURE OR CONSTRUCTOR MUST BE REFERENCED DIRECTLY ON THE TYPE THAT THE FEATURE BELONGS TO"
  var construct113 = jack#construct("mystring", 42)  //## issuekeys: ERROR : "A STATIC FEATURE OR CONSTRUCTOR MUST BE REFERENCED DIRECTLY ON THE TYPE THAT THE FEATURE BELONGS TO"
  var construct114 = jack#construct(String)     //## issuekeys: ERROR : "A STATIC FEATURE OR CONSTRUCTOR MUST BE REFERENCED DIRECTLY ON THE TYPE THAT THE FEATURE BELONGS TO"
  var construct115 = jack#construct(String, int)  //## issuekeys: ERROR : "A STATIC FEATURE OR CONSTRUCTOR MUST BE REFERENCED DIRECTLY ON THE TYPE THAT THE FEATURE BELONGS TO"

  //IDE-1565 Adding more cases
  construct() {}
  construct(str : String) {}
  construct(str : String, i : int) {}

  var construct116 = #construct()
  var construct117 = #construct(String)
  var construct118 = #construct(String, int)
  var construct119 = #construct("mystring")
  var construct120 = #construct("mystring", 42)

  var construct121 = this#construct()                 //## issuekeys: ERROR
  var construct122 = this#construct(String)           //## issuekeys: ERROR
  var construct123 = this#construct(String, int)      //## issuekeys: ERROR
  var construct124 = this#construct("mystring")       //## issuekeys: ERROR
  var construct125 = this#construct("mystring", 42)   //## issuekeys: ERROR


}