package gw.specContrib.featureLiterals.javaMembersBinding

class Errant_BindJavaFields {
  //binding Java class fields
  //IDE-1373 Parser issue. OS Gosu works fine
  var javaFieldInternalProp = JavaClass#javaFieldInternal
  var javaFieldPrivateProp = JavaClass#javaFieldPrivate
  var javaFieldProtectedProp = JavaClass#javaFieldProtected
  var javaFieldPublicProp = JavaClass#javaFieldPublic

  //setting field feature literals
  function getSetFieldFeatureLiterals() {
    var jInstance: JavaClass

    //set the values
    //correct number of arguments
    javaFieldInternalProp.set(jInstance, "internal")
    javaFieldPrivateProp.set(jInstance, "private")
    javaFieldProtectedProp.set(jInstance, "protected")
    javaFieldPublicProp.set(jInstance, "public")

    //incorrect number/type of arguments
    //should show error but not because set() function is not resolved. Rather for wrong number of arguments
    javaFieldInternalProp.set(jInstance, "internal", 42)           //## issuekeys: WRONG NUMBER OF ARGUMENTS
    javaFieldInternalProp.set(jInstance, 42)                     //## issuekeys: WRONG NUMBER OF ARGUMENTS
    javaFieldInternalProp.set("internal")                       //## issuekeys: WRONG NUMBER OF ARGUMENTS
    javaFieldInternalProp.set("internal", 42)                  //## issuekeys: WRONG NUMBER OF ARGUMENTS

    javaFieldPrivateProp.set(jInstance, "private", 42)       //## issuekeys: WRONG NUMBER OF ARGUMENTS
    javaFieldPrivateProp.set(jInstance, 42)                //## issuekeys: WRONG NUMBER OF ARGUMENTS
    javaFieldPrivateProp.set("private")                   //## issuekeys: WRONG NUMBER OF ARGUMENTS
    javaFieldPrivateProp.set("private", 42)               //## issuekeys: WRONG NUMBER OF ARGUMENTS

    javaFieldProtectedProp.set(jInstance, "protected", 42)        //## issuekeys: WRONG NUMBER OF ARGUMENTS
    javaFieldProtectedProp.set(jInstance, 42)                     //## issuekeys: WRONG NUMBER OF ARGUMENTS
    javaFieldProtectedProp.set("protected")                       //## issuekeys: WRONG NUMBER OF ARGUMENTS
    javaFieldProtectedProp.set("protected", 42)

    javaFieldPublicProp.set(jInstance, "public", 42)              //## issuekeys: WRONG NUMBER OF ARGUMENTS
    javaFieldPublicProp.set(jInstance, 42)                        //## issuekeys: WRONG NUMBER OF ARGUMENTS
    javaFieldPublicProp.set("public")                             //## issuekeys: WRONG NUMBER OF ARGUMENTS
    javaFieldPublicProp.set("public", 42)

    //get the values
    var javaGetterInternal11 = javaFieldInternalProp.get(jInstance)
    var javaGetterInternal12 = javaFieldInternalProp.get("hi")         //## issuekeys: WRONG NUMBER OF ARGUMENTS
    var javaGetterInternal13 = javaFieldInternalProp.get()              //## issuekeys: WRONG NUMBER OF ARGUMENTS

    var javaGetterPrivate11 = javaFieldPrivateProp.get(jInstance)
    var javaGetterPrivate12 = javaFieldPrivateProp.get("hi")            //## issuekeys: WRONG NUMBER OF ARGUMENTS
    var javaGetterPrivate13 = javaFieldPrivateProp.get()                 //## issuekeys: WRONG NUMBER OF ARGUMENTS

    var javaGetterProtected11 = javaFieldProtectedProp.get(jInstance)
    var javaGetterProtected12 = javaFieldProtectedProp.get("hi")         //## issuekeys: WRONG NUMBER OF ARGUMENTS
    var javaGetterProtected13 = javaFieldProtectedProp.get()            //## issuekeys: WRONG NUMBER OF ARGUMENTS

    var javaGetterPublic11 = javaFieldPublicProp.get(jInstance)
    var javaGetterPublic12 = javaFieldPublicProp.get("hi")              //## issuekeys: WRONG NUMBER OF ARGUMENTS
    var javaGetterPublic13 = javaFieldPublicProp.get()                  //## issuekeys: WRONG NUMBER OF ARGUMENTS
  }
}