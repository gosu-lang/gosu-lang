package gw.specContrib.featureLiterals.javaMembersBinding

class Errant_BindJavaMethodsToInstance {

  var jack: JavaClass
  //methods
  //IDE-1375 - Should show errors where the number/type of arguments is correct in definition
  //Issue in both OS Gosu and Parser
  var internalFunction111 = jack#javaInternalFun(String, int)
  var internalFunction112 = jack#javaInternalFun(JavaClass, String, int) //## issuekeys:
  var internalFunction113 = jack#javaInternalFun(JavaClass)  //## issuekeys:
  var internalFunction114 = jack#javaInternalFun(JavaClass, String)  //## issuekeys:
  var internalFunction115 = jack#javaInternalFun(String)             //## issuekeys:
  var internalFunction116 = jack#javaInternalFun(int, int, int)      //## issuekeys:
  var internalFunction117 = jack#javaInternalFun()                   //## issuekeys:
  var internalFunction118 = jack#javaInternalFun      //## issuekeys: CANNOT RESOLVE SYMBOL 'JAVAINTERNALFUN'

  var privateFunction111 = jack#javaPrivateFun(String, int)
  var privateFunction112 = jack#javaPrivateFun(JavaClass, String, int)  //## issuekeys:
  var privateFunction113 = jack#javaPrivateFun(JavaClass)  //## issuekeys:
  var privateFunction114 = jack#javaPrivateFun(JavaClass, String) //## issuekeys:
  var privateFunction115 = jack#javaPrivateFun(String)         //## issuekeys:
  var privateFunction116 = jack#javaPrivateFun(int, int, int)    //## issuekeys:
  var privateFunction117 = jack#javaPrivateFun()   //## issuekeys:
  var privateFunction118 = jack#javaPrivateFun      //## issuekeys: CANNOT RESOLVE SYMBOL 'JAVAPRIVATEFUN'

  var protectedFunction111 = jack#javaProtectedFun(String, int)
  var protectedFunction112 = jack#javaProtectedFun(JavaClass, String, int)   //## issuekeys:
  var protectedFunction113 = jack#javaProtectedFun(JavaClass)    //## issuekeys:
  var protectedFunction114 = jack#javaProtectedFun(JavaClass, String)    //## issuekeys:
  var protectedFunction115 = jack#javaProtectedFun(String)   //## issuekeys:
  var protectedFunction116 = jack#javaProtectedFun(int, int, int)    //## issuekeys:
  var protectedFunction117 = jack#javaProtectedFun()   //## issuekeys:
  var protectedFunction118 = jack#javaProtectedFun      //## issuekeys: CANNOT RESOLVE SYMBOL 'JAVAPROTECTEDFUN'

  var publicFunction111 = jack#javaPublicFun(String, int)
  var publicFunction112 = jack#javaPublicFun(JavaClass, String, int)//## issuekeys:
  var publicFunction113 = jack#javaPublicFun(JavaClass)//## issuekeys:
  var publicFunction114 = jack#javaPublicFun(JavaClass, String)//## issuekeys:
  var publicFunction115 = jack#javaPublicFun(String)//## issuekeys:
  var publicFunction116 = jack#javaPublicFun(int, int, int)//## issuekeys:
  var publicFunction117 = jack#javaPublicFun()//## issuekeys:
  var publicFunction118 = jack#javaPublicFun      //## issuekeys: CANNOT RESOLVE SYMBOL 'JAVAPUBLICFUN'


  function invokeJavaFunctions() {
    var jInstance: JavaClass
    internalFunction111.invoke("Ed", 42)
    internalFunction111.invoke(42, "Ed")      //## issuekeys: 'INVOKE(JAVA.LANG.STRING, INT)' IN '' CANNOT BE APPLIED TO '(INT, JAVA.LANG.STRING)'
    internalFunction111.invoke(jInstance, "Ed", 42)      //## issuekeys: 'INVOKE(JAVA.LANG.STRING, INT)' IN '' CANNOT BE APPLIED TO '(GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.FEATURELITERALS.FEATURELITERALSMAIN.JAVAFEATURELITERALS.JAVACLASS, JAVA.LANG.STRING, INT)'
    internalFunction111.invoke(jInstance, "Ed")      //## issuekeys: 'INVOKE(JAVA.LANG.STRING, INT)' IN '' CANNOT BE APPLIED TO '(GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.FEATURELITERALS.FEATURELITERALSMAIN.JAVAFEATURELITERALS.JAVACLASS, JAVA.LANG.STRING)'
    internalFunction111.invoke(jInstance)      //## issuekeys: 'INVOKE(JAVA.LANG.STRING, INT)' IN '' CANNOT BE APPLIED TO '(GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.FEATURELITERALS.FEATURELITERALSMAIN.JAVAFEATURELITERALS.JAVACLASS)'
    internalFunction111.invoke("Ed", 42)      //## issuekeys: 'INVOKE(GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.FEATURELITERALS.FEATURELITERALSMAIN.JAVAFEATURELITERALS.JAVACLASS, JAVA.LANG.STRING, INT)' IN '' CANNOT BE APPLIED TO '(JAVA.LANG.STRING, INT)'
    internalFunction111.invoke(42)      //## issuekeys: 'INVOKE(JAVA.LANG.STRING, INT)' IN '' CANNOT BE APPLIED TO '(INT)'

    privateFunction111.invoke("Ed", 42)
    privateFunction111.invoke(42, "Ed")        //## issuekeys: 'INVOKE(JAVA.LANG.STRING, INT)' IN '' CANNOT BE APPLIED TO '(INT, JAVA.LANG.STRING)'
    privateFunction111.invoke(jInstance, "Ed", 42)      //## issuekeys: 'INVOKE(JAVA.LANG.STRING, INT)' IN '' CANNOT BE APPLIED TO '(GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.FEATURELITERALS.FEATURELITERALSMAIN.JAVAFEATURELITERALS.JAVACLASS, JAVA.LANG.STRING, INT)'
    privateFunction111.invoke(jInstance, "Ed")      //## issuekeys: 'INVOKE(JAVA.LANG.STRING, INT)' IN '' CANNOT BE APPLIED TO '(GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.FEATURELITERALS.FEATURELITERALSMAIN.JAVAFEATURELITERALS.JAVACLASS, JAVA.LANG.STRING)'
    privateFunction111.invoke(jInstance)      //## issuekeys: 'INVOKE(JAVA.LANG.STRING, INT)' IN '' CANNOT BE APPLIED TO '(GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.FEATURELITERALS.FEATURELITERALSMAIN.JAVAFEATURELITERALS.JAVACLASS)'
    privateFunction111.invoke("Ed", 42)      //## issuekeys: 'INVOKE(GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.FEATURELITERALS.FEATURELITERALSMAIN.JAVAFEATURELITERALS.JAVACLASS, JAVA.LANG.STRING, INT)' IN '' CANNOT BE APPLIED TO '(JAVA.LANG.STRING, INT)'
    privateFunction111.invoke(42)      //## issuekeys: 'INVOKE(JAVA.LANG.STRING, INT)' IN '' CANNOT BE APPLIED TO '(INT)'

    protectedFunction111.invoke("Ed", 42)
    protectedFunction111.invoke(42, "Ed")      //## issuekeys: 'INVOKE(JAVA.LANG.STRING, INT)' IN '' CANNOT BE APPLIED TO '(INT, JAVA.LANG.STRING)'
    protectedFunction111.invoke(jInstance, "Ed", 42)      //## issuekeys: 'INVOKE(JAVA.LANG.STRING, INT)' IN '' CANNOT BE APPLIED TO '(GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.FEATURELITERALS.FEATURELITERALSMAIN.JAVAFEATURELITERALS.JAVACLASS, JAVA.LANG.STRING, INT)'
    protectedFunction111.invoke(jInstance, "Ed")      //## issuekeys: 'INVOKE(JAVA.LANG.STRING, INT)' IN '' CANNOT BE APPLIED TO '(GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.FEATURELITERALS.FEATURELITERALSMAIN.JAVAFEATURELITERALS.JAVACLASS, JAVA.LANG.STRING)'
    protectedFunction111.invoke(jInstance)      //## issuekeys: 'INVOKE(JAVA.LANG.STRING, INT)' IN '' CANNOT BE APPLIED TO '(GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.FEATURELITERALS.FEATURELITERALSMAIN.JAVAFEATURELITERALS.JAVACLASS)'
    protectedFunction111.invoke("Ed", 42)      //## issuekeys: 'INVOKE(GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.FEATURELITERALS.FEATURELITERALSMAIN.JAVAFEATURELITERALS.JAVACLASS, JAVA.LANG.STRING, INT)' IN '' CANNOT BE APPLIED TO '(JAVA.LANG.STRING, INT)'
    protectedFunction111.invoke(42)      //## issuekeys: 'INVOKE(JAVA.LANG.STRING, INT)' IN '' CANNOT BE APPLIED TO '(INT)'

    publicFunction111.invoke("Ed", 42)
    publicFunction111.invoke(42, "Ed")         //## issuekeys: 'INVOKE(JAVA.LANG.STRING, INT)' IN '' CANNOT BE APPLIED TO '(INT, JAVA.LANG.STRING)'
    publicFunction111.invoke(jInstance, "Ed", 42)      //## issuekeys: 'INVOKE(JAVA.LANG.STRING, INT)' IN '' CANNOT BE APPLIED TO '(GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.FEATURELITERALS.FEATURELITERALSMAIN.JAVAFEATURELITERALS.JAVACLASS, JAVA.LANG.STRING, INT)'
    publicFunction111.invoke(jInstance, "Ed")      //## issuekeys: 'INVOKE(JAVA.LANG.STRING, INT)' IN '' CANNOT BE APPLIED TO '(GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.FEATURELITERALS.FEATURELITERALSMAIN.JAVAFEATURELITERALS.JAVACLASS, JAVA.LANG.STRING)'
    publicFunction111.invoke(jInstance)      //## issuekeys: 'INVOKE(JAVA.LANG.STRING, INT)' IN '' CANNOT BE APPLIED TO '(GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.FEATURELITERALS.FEATURELITERALSMAIN.JAVAFEATURELITERALS.JAVACLASS)'
    publicFunction111.invoke("Ed", 42)      //## issuekeys: 'INVOKE(GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.FEATURELITERALS.FEATURELITERALSMAIN.JAVAFEATURELITERALS.JAVACLASS, JAVA.LANG.STRING, INT)' IN '' CANNOT BE APPLIED TO '(JAVA.LANG.STRING, INT)'
    publicFunction111.invoke(42)      //## issuekeys: 'INVOKE(JAVA.LANG.STRING, INT)' IN '' CANNOT BE APPLIED TO '(INT)'

  }
}