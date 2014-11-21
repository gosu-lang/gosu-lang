package gw.specContrib.featureLiterals.gosuMembersBinding

class Errant_BindGosuFields {
  var fieldInternalProp = GosuFL#gosuFieldInternal
  var fieldPrivateProp = GosuFL#gosuFieldPrivate
  var fieldProtectedProp = GosuFL#gosuFieldProtected
  var fieldPublicProp = GosuFL#gosuFieldPublic

  //setting field feature literals
  function getSetFieldFeatureLiterals () {
    var gInstance : GosuFL

    //set the values
    //correct number of arguments
    fieldInternalProp.set(gInstance, "internal")
    fieldPrivateProp.set(gInstance, "private")
    fieldProtectedProp.set(gInstance, "protected")
    fieldPublicProp.set(gInstance, "public")

    //incorrect number/type of arguments
    //should show error but not because set() function is not resolved. Rather for wrong number of arguments
    fieldInternalProp.set(gInstance, "internal", 42)           //## issuekeys: WRONG NUMBER OF ARGUMENTS
    fieldInternalProp.set(gInstance, 42)                     //## issuekeys: WRONG NUMBER OF ARGUMENTS
    fieldInternalProp.set("internal")                       //## issuekeys: WRONG NUMBER OF ARGUMENTS
    fieldInternalProp.set("internal", 42)                  //## issuekeys: WRONG NUMBER OF ARGUMENTS

    fieldPrivateProp.set(gInstance, "private", 42)       //## issuekeys: WRONG NUMBER OF ARGUMENTS
    fieldPrivateProp.set(gInstance, 42)                //## issuekeys: WRONG NUMBER OF ARGUMENTS
    fieldPrivateProp.set("private")                   //## issuekeys: WRONG NUMBER OF ARGUMENTS
    fieldPrivateProp.set("private", 42)               //## issuekeys: WRONG NUMBER OF ARGUMENTS

    fieldProtectedProp.set(gInstance, "protected", 42 )        //## issuekeys: WRONG NUMBER OF ARGUMENTS
    fieldProtectedProp.set(gInstance, 42)                     //## issuekeys: WRONG NUMBER OF ARGUMENTS
    fieldProtectedProp.set("protected")                       //## issuekeys: WRONG NUMBER OF ARGUMENTS
    fieldProtectedProp.set("protected", 42)

    fieldPublicProp.set(gInstance, "public", 42)              //## issuekeys: WRONG NUMBER OF ARGUMENTS
    fieldPublicProp.set(gInstance, 42)                        //## issuekeys: WRONG NUMBER OF ARGUMENTS
    fieldPublicProp.set("public")                             //## issuekeys: WRONG NUMBER OF ARGUMENTS
    fieldPublicProp.set("public", 42)

    //get the values
    var javaGetterInternal11 = fieldInternalProp.get(gInstance)
    var javaGetterInternal12 = fieldInternalProp.get("hi")         //## issuekeys: WRONG NUMBER OF ARGUMENTS
    var javaGetterInternal13 = fieldInternalProp.get()              //## issuekeys: WRONG NUMBER OF ARGUMENTS

    var javaGetterPrivate11 = fieldPrivateProp.get(gInstance)
    var javaGetterPrivate12 = fieldPrivateProp.get("hi")            //## issuekeys: WRONG NUMBER OF ARGUMENTS
    var javaGetterPrivate13 = fieldPrivateProp.get()                 //## issuekeys: WRONG NUMBER OF ARGUMENTS

    var javaGetterProtected11 = fieldProtectedProp.get(gInstance)
    var javaGetterProtected12 = fieldProtectedProp.get("hi")         //## issuekeys: WRONG NUMBER OF ARGUMENTS
    var javaGetterProtected13 = fieldProtectedProp.get()            //## issuekeys: WRONG NUMBER OF ARGUMENTS

    var javaGetterPublic11 = fieldPublicProp.get(gInstance)
    var javaGetterPublic12 = fieldPublicProp.get("hi")              //## issuekeys: WRONG NUMBER OF ARGUMENTS
    var javaGetterPublic13 = fieldPublicProp.get()                  //## issuekeys: WRONG NUMBER OF ARGUMENTS
  }


}