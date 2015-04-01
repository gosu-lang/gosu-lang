package gw.specContrib.featureLiterals.gosuMembersBinding

class Errant_BindGosuConstructorToInstance {
  var jack: GosuFL
  //IDE-1565 - Parser Issue. Should show error in the following cases
  var construct111 = jack#construct()            //## issuekeys: ERROR : "A STATIC FEATURE OR CONSTRUCTOR MUST BE REFERENCED DIRECTLY ON THE TYPE THAT THE FEATURE BELONGS TO"
  var construct112 = jack#construct("mystring")  //## issuekeys: ERROR : "A STATIC FEATURE OR CONSTRUCTOR MUST BE REFERENCED DIRECTLY ON THE TYPE THAT THE FEATURE BELONGS TO"
  var construct113 = jack#construct("mystring", 42)  //## issuekeys: ERROR : "A STATIC FEATURE OR CONSTRUCTOR MUST BE REFERENCED DIRECTLY ON THE TYPE THAT THE FEATURE BELONGS TO"
  var construct114 = jack#construct(String)     //## issuekeys: ERROR : "A STATIC FEATURE OR CONSTRUCTOR MUST BE REFERENCED DIRECTLY ON THE TYPE THAT THE FEATURE BELONGS TO"
  var construct115 = jack#construct(String, int)  //## issuekeys: ERROR : "A STATIC FEATURE OR CONSTRUCTOR MUST BE REFERENCED DIRECTLY ON THE TYPE THAT THE FEATURE BELONGS TO"
}