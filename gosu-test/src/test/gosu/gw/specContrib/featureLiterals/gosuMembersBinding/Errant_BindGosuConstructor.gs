package gw.specContrib.featureLiterals.gosuMembersBinding

class Errant_BindGosuConstructor {
  var construct111 = GosuFL#construct()
  var construct112 = GosuFL#construct("mystring")
  var construct113 = GosuFL#construct("mystring", 42)
  var construct114 = GosuFL#construct(String)
  var construct115 = GosuFL#construct(String, int)

  function invokeConstructors() {
    //gInstance is not required to invoke constructors so cases with gInstance are negative cases
    var gInstance: GosuFL
    construct111.invoke()
    //IDE-1564 - OS Gosu issue
    construct112.invoke()
    //IDE-1564 - OS Gosu issue
    construct113.invoke()
    construct114.invoke()      //## issuekeys: 'INVOKE(JAVA.LANG.STRING)' IN '' CANNOT BE APPLIED TO '()'
    construct114.invoke("mystring")
    construct114.invoke(gInstance, "mystring")      //## issuekeys: 'INVOKE(JAVA.LANG.STRING)' IN '' CANNOT BE APPLIED TO '(GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.FEATURELITERALS.FEATURELITERALSMAIN.GOSUFEATURELITERALS.GOSUFL, JAVA.LANG.STRING)'
    construct115.invoke("mystring", 42)
    construct115.invoke(gInstance, "mystring", 42)      //## issuekeys: 'INVOKE(JAVA.LANG.STRING, INT)' IN '' CANNOT BE APPLIED TO '(GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.FEATURELITERALS.FEATURELITERALSMAIN.GOSUFEATURELITERALS.GOSUFL, JAVA.LANG.STRING, INT)'
    //IDE-1563 - OS Gosu issue. Error at the closing braces of function
  }
}