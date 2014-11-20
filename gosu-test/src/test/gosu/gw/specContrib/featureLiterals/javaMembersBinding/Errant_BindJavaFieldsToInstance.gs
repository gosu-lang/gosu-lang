package gw.specContrib.featureLiterals.javaMembersBinding

class Errant_BindJavaFieldsToInstance {
  //INSTANCE
  var jack: JavaClass
  //binding Java class fields to an instance
  //IDE-1373 Parser issue. OS Gosu works fine
  var javaFieldInternalInstanceProp = jack#javaFieldInternal
  var javaFieldPrivateInstanceProp = jack#javaFieldPrivate
  var javaFieldProtectedInstanceProp = jack#javaFieldProtected
  var javaFieldPublicInstanceProp = jack#javaFieldPublic

  //setting field feature literals
  function getSetFieldFeatureLiterals() {
    var jInstance: JavaClass

    //set the values
    //correct number of arguments
    javaFieldInternalInstanceProp.set("internal")
    javaFieldPrivateInstanceProp.set("private")
    javaFieldProtectedInstanceProp.set("protected")
    javaFieldPublicInstanceProp.set("public")

    //incorrect number/type of arguments
    javaFieldInternalInstanceProp.set(jInstance, "internal", 42)      //## issuekeys: WRONG NUMBER OF ARGUMENTS
    javaFieldInternalInstanceProp.set(jInstance, 42)      //## issuekeys: WRONG NUMBER OF ARGUMENTS
    javaFieldInternalInstanceProp.set(jInstance, "internal")    //## issuekeys: WRONG NUMBER OF ARGUMENTS
    javaFieldInternalInstanceProp.set("internal", 42)      //## issuekeys: WRONG NUMBER OF ARGUMENTS
    javaFieldInternalInstanceProp.set()      //## issuekeys: WRONG NUMBER OF ARGUMENTS

    javaFieldPrivateInstanceProp.set(jInstance, "private", 42)   //## issuekeys: WRONG NUMBER OF ARGUMENTS
    javaFieldPrivateInstanceProp.set(jInstance, 42)      //## issuekeys: WRONG NUMBER OF ARGUMENTS
    javaFieldPrivateInstanceProp.set(jInstance, "private")      //## issuekeys: WRONG NUMBER OF ARGUMENTS
    javaFieldPrivateInstanceProp.set("private", 42)      //## issuekeys: WRONG NUMBER OF ARGUMENTS
    javaFieldPrivateInstanceProp.set()      //## issuekeys: WRONG NUMBER OF ARGUMENTS

    javaFieldProtectedInstanceProp.set(jInstance, "protected", 42)     //## issuekeys: WRONG NUMBER OF ARGUMENTS
    javaFieldProtectedInstanceProp.set(jInstance, 42)      //## issuekeys: WRONG NUMBER OF ARGUMENTS
    javaFieldProtectedInstanceProp.set(jInstance, "protected")      //## issuekeys: WRONG NUMBER OF ARGUMENTS
    javaFieldProtectedInstanceProp.set("protected", 42)      //## issuekeys: WRONG NUMBER OF ARGUMENTS
    javaFieldProtectedInstanceProp.set()      //## issuekeys: WRONG NUMBER OF ARGUMENTS

    javaFieldPublicInstanceProp.set(jInstance, "public", 42)    //## issuekeys: WRONG NUMBER OF ARGUMENTS
    javaFieldPublicInstanceProp.set(jInstance, 42)      //## issuekeys: WRONG NUMBER OF ARGUMENTS
    javaFieldPublicInstanceProp.set(jInstance, "public")      //## issuekeys: WRONG NUMBER OF ARGUMENTS
    javaFieldPublicInstanceProp.set("public", 42)      //## issuekeys: WRONG NUMBER OF ARGUMENTS
    javaFieldPublicInstanceProp.set()      //## issuekeys: WRONG NUMBER OF ARGUMENTS

    //get the values
    var javaGetterInternal11 = javaFieldInternalInstanceProp.get()
    var javaGetterInternal12 = javaFieldInternalInstanceProp.get(jInstance)   //## issuekeys: WRONG NUMBER OF ARGUMENTS
    var javaGetterInternal13 = javaFieldInternalInstanceProp.get("hi")    //## issuekeys: WRONG NUMBER OF ARGUMENTS

    var javaGetterPrivate11 = javaFieldPrivateInstanceProp.get()
    var javaGetterPrivate12 = javaFieldPrivateInstanceProp.get(jInstance)  //## issuekeys: WRONG NUMBER OF ARGUMENTS
    var javaGetterPrivate13 = javaFieldPrivateInstanceProp.get("hi")    //## issuekeys: WRONG NUMBER OF ARGUMENTS

    var javaGetterProtected11 = javaFieldProtectedInstanceProp.get()
    var javaGetterProtected12 = javaFieldProtectedInstanceProp.get(jInstance)  //## issuekeys: WRONG NUMBER OF ARGUMENTS
    var javaGetterProtected13 = javaFieldProtectedInstanceProp.get("hi")

    var javaGetterPublic11 = javaFieldPublicInstanceProp.get()
    var javaGetterPublic12 = javaFieldPublicInstanceProp.get(jInstance) //## issuekeys: WRONG NUMBER OF ARGUMENTS
    var javaGetterPublic13 = javaFieldPublicInstanceProp.get("hi")     //## issuekeys: WRONG NUMBER OF ARGUMENTS
  }
}