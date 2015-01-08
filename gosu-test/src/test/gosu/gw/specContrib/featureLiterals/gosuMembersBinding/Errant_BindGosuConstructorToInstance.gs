package gw.specContrib.featureLiterals.gosuMembersBinding

class Errant_BindGosuConstructorToInstance {
  var jack: GosuFL
  //IDE-1565 - Parser Issue. Should show error in the following cases
  var construct111 = jack#construct()            //## issuekeys: ERROR : "A STATIC FEATURE OR CONSTRUCTOR MUST BE REFERENCED DIRECTLY ON THE TYPE THAT THE FEATURE BELONGS TO"
  var construct112 = jack#construct("mystring")  //## issuekeys: ERROR : "A STATIC FEATURE OR CONSTRUCTOR MUST BE REFERENCED DIRECTLY ON THE TYPE THAT THE FEATURE BELONGS TO"
  var construct113 = jack#construct("mystring", 42)  //## issuekeys: ERROR : "A STATIC FEATURE OR CONSTRUCTOR MUST BE REFERENCED DIRECTLY ON THE TYPE THAT THE FEATURE BELONGS TO"
  var construct114 = jack#construct(String)     //## issuekeys: ERROR : "A STATIC FEATURE OR CONSTRUCTOR MUST BE REFERENCED DIRECTLY ON THE TYPE THAT THE FEATURE BELONGS TO"
  var construct115 = jack#construct(String, int)  //## issuekeys: ERROR : "A STATIC FEATURE OR CONSTRUCTOR MUST BE REFERENCED DIRECTLY ON THE TYPE THAT THE FEATURE BELONGS TO"

  function invokeConstructors() {
    var gInstance: GosuFL
    construct111.invoke()       //## issuekeys: ERROR : CANNOT RESOLVE SYMBOL FOR
    construct112.invoke()       //## issuekeys: ERROR : CANNOT RESOLVE SYMBOL FOR
    construct113.invoke()       //## issuekeys: ERROR : CANNOT RESOLVE SYMBOL FOR
    construct114.invoke()       //## issuekeys: ERROR : CANNOT RESOLVE SYMBOL FOR
    construct114.invoke("mystring") //## issuekeys: ERROR : CANNOT RESOLVE SYMBOL FOR
    construct114.invoke(gInstance, "mystring")      //## issuekeys: ERROR : CANNOT RESOLVE SYMBOL FOR
    construct115.invoke("mystring", 42)   //## issuekeys: ERROR : CANNOT RESOLVE SYMBOL FOR
    construct115.invoke(gInstance, "mystring", 42)      //## issuekeys: ERROR : CANNOT RESOLVE SYMBOL FOR
    //IDE-1563 - OS Gosu issue
  }
}