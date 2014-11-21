package gw.specContrib.featureLiterals.gosuMembersBinding

class Errant_BindGosuFieldsToInstance {
  //INSTANCE
  var jack : GosuFL
  //binding Gosu class fields to an instance
  //IDE-1373 Parser issue. OS Gosu works fine
  var gosuFieldInternalInstanceProp = jack#gosuFieldInternal
  var gosuFieldPrivateInstanceProp = jack#gosuFieldPrivate
  var gosuFieldProtectedInstanceProp = jack#gosuFieldProtected
  var gosuFieldPublicInstanceProp = jack#gosuFieldPublic

  //setting field feature literals
  function getSetFieldFeatureLiterals () {
    var gInstance : GosuFL

    //set the values
    //correct number of arguments
    gosuFieldInternalInstanceProp.set("internal")
    gosuFieldPrivateInstanceProp.set("private")
    gosuFieldProtectedInstanceProp.set("protected")
    gosuFieldPublicInstanceProp.set("public")

    //incorrect number/type of arguments
    gosuFieldInternalInstanceProp.set(gInstance, "internal", 42)      //## issuekeys: WRONG NUMBER OF ARGUMENTS
    gosuFieldInternalInstanceProp.set(gInstance, 42)      //## issuekeys: WRONG NUMBER OF ARGUMENTS
    gosuFieldInternalInstanceProp.set(gInstance, "internal")    //## issuekeys: WRONG NUMBER OF ARGUMENTS
    gosuFieldInternalInstanceProp.set("internal", 42)      //## issuekeys: WRONG NUMBER OF ARGUMENTS
    gosuFieldInternalInstanceProp.set()      //## issuekeys: WRONG NUMBER OF ARGUMENTS

    gosuFieldPrivateInstanceProp.set(gInstance, "private", 42)   //## issuekeys: WRONG NUMBER OF ARGUMENTS
    gosuFieldPrivateInstanceProp.set(gInstance, 42)      //## issuekeys: WRONG NUMBER OF ARGUMENTS
    gosuFieldPrivateInstanceProp.set(gInstance, "private")      //## issuekeys: WRONG NUMBER OF ARGUMENTS
    gosuFieldPrivateInstanceProp.set("private", 42)      //## issuekeys: WRONG NUMBER OF ARGUMENTS
    gosuFieldPrivateInstanceProp.set()      //## issuekeys: WRONG NUMBER OF ARGUMENTS

    gosuFieldProtectedInstanceProp.set(gInstance, "protected", 42 )     //## issuekeys: WRONG NUMBER OF ARGUMENTS
    gosuFieldProtectedInstanceProp.set(gInstance, 42)      //## issuekeys: WRONG NUMBER OF ARGUMENTS
    gosuFieldProtectedInstanceProp.set(gInstance, "protected")      //## issuekeys: WRONG NUMBER OF ARGUMENTS
    gosuFieldProtectedInstanceProp.set("protected", 42)      //## issuekeys: WRONG NUMBER OF ARGUMENTS
    gosuFieldProtectedInstanceProp.set()      //## issuekeys: WRONG NUMBER OF ARGUMENTS

    gosuFieldPublicInstanceProp.set(gInstance, "public", 42)    //## issuekeys: WRONG NUMBER OF ARGUMENTS
    gosuFieldPublicInstanceProp.set(gInstance, 42)      //## issuekeys: WRONG NUMBER OF ARGUMENTS
    gosuFieldPublicInstanceProp.set(gInstance, "public")      //## issuekeys: WRONG NUMBER OF ARGUMENTS
    gosuFieldPublicInstanceProp.set("public", 42)      //## issuekeys: WRONG NUMBER OF ARGUMENTS
    gosuFieldPublicInstanceProp.set()      //## issuekeys: WRONG NUMBER OF ARGUMENTS

    //get the values
    var javaGetterInternal11 = gosuFieldInternalInstanceProp.get()
    var javaGetterInternal12 = gosuFieldInternalInstanceProp.get(gInstance)   //## issuekeys: WRONG NUMBER OF ARGUMENTS
    var javaGetterInternal13 = gosuFieldInternalInstanceProp.get("hi")    //## issuekeys: WRONG NUMBER OF ARGUMENTS

    var javaGetterPrivate11 = gosuFieldPrivateInstanceProp.get()
    var javaGetterPrivate12 = gosuFieldPrivateInstanceProp.get(gInstance)  //## issuekeys: WRONG NUMBER OF ARGUMENTS
    var javaGetterPrivate13 = gosuFieldPrivateInstanceProp.get("hi")    //## issuekeys: WRONG NUMBER OF ARGUMENTS

    var javaGetterProtected11 = gosuFieldProtectedInstanceProp.get()
    var javaGetterProtected12 = gosuFieldProtectedInstanceProp.get(gInstance)  //## issuekeys: WRONG NUMBER OF ARGUMENTS
    var javaGetterProtected13 = gosuFieldProtectedInstanceProp.get("hi")

    var javaGetterPublic11 = gosuFieldPublicInstanceProp.get()
    var javaGetterPublic12 = gosuFieldPublicInstanceProp.get(gInstance) //## issuekeys: WRONG NUMBER OF ARGUMENTS
    var javaGetterPublic13 = gosuFieldPublicInstanceProp.get("hi")     //## issuekeys: WRONG NUMBER OF ARGUMENTS
  }

}