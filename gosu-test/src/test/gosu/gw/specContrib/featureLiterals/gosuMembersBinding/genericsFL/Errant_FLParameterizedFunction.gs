package gw.specContrib.featureLiterals.gosuMembersBinding.genericsFL

uses java.lang.CharSequence
uses java.lang.Integer

class Errant_FLParameterizedFunction {
  var gInstance : Errant_FLParameterizedFunction
  //Parameterized method
  function pFun<T>(t : T) : T { return null }

  //1st Set
  var pFunFL111 = #pFun()
  var pFunFL112 = #pFun("mystring")
  //IDE-1588 - OS Gosu issue
  var pFunFL113 = #pFun(String) //## issuekeys: MSG_FL_METHOD_NOT_FOUND

  //2nd Set
  var pFunFL211 = Errant_FLParameterizedFunction#pFun()
  var pFunFL212 = Errant_FLParameterizedFunction#pFun("mystring")
  //IDE-1588 - OS Gosu issue
  var pFunFL213 = Errant_FLParameterizedFunction#pFun(String)  //## issuekeys: MSG_FL_METHOD_NOT_FOUND

  //3rd Set
  var pFunFL311 = gInstance#pFun()
  var pFunFL312 = gInstance#pFun("mystring")
  //IDE-1588 - OS Gosu issue
  var pFunFL313 = gInstance#pFun(String)  //## issuekeys: MSG_FL_METHOD_NOT_FOUND

  function invokeFun() {
    //1st Set
    //IDE-1587
    var pFunFLInvoke1111 = pFunFL111.invoke(this, "sdf")
    var pFunFLInvoke1112 = pFunFL111.invoke(this)      //## issuekeys: 'INVOKE(JAVA.LANG.OBJECT)' IN '' CANNOT BE APPLIED TO '()'
    var pFunFLInvoke1113 = pFunFL111.invoke(this, new Object())
    //check return type
    //IDE-1587
    var pFunFLInvoke1114 : Object = pFunFL111.invoke(this, "sdf")

    var pFunFLInvoke1121 = pFunFL112.invoke(this, "sdf")      //## issuekeys: 'INVOKE()' IN '' CANNOT BE APPLIED TO '(JAVA.LANG.STRING)'
    //IDE-1587
    var pFunFLInvoke1122 = pFunFL112.invoke(this)
    var pFunFLInvoke1123 = pFunFL112.invoke(this, new Object())      //## issuekeys: 'INVOKE()' IN '' CANNOT BE APPLIED TO '(JAVA.LANG.OBJECT)'
    //Check return type
    //IDE-1587
    var pFunFLInvoke1124 : Object = pFunFL112.invoke(this)

    //2nd Set
    //IDE-1599 OS Gosu issue for unbounded parameters in a generic function FL
    var pFunFLInvoke2111 = pFunFL211.invoke(gInstance, "sdf")
    var pFunFLInvoke2112 = pFunFL211.invoke(gInstance)      //## issuekeys: 'INVOKE(GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.FEATURELITERALS.FEATURELITERALSMAIN.GOSUFEATURELITERALS.GENERICSFL.ERRANT_FLPARAMETERIZEDFUNCTION, JAVA.LANG.OBJECT)' IN '' CANNOT BE APPLIED TO '(GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.FEATURELITERALS.FEATURELITERALSMAIN.GOSUFEATURELITERALS.GENERICSFL.ERRANT_FLPARAMETERIZEDFUNCTION)'
    //IDE-1599 OS Gosu issue for unbounded parameters in a generic function FL
    var pFunFLInvoke2113 = pFunFL211.invoke(gInstance, new Object())

    var pFunFLInvoke2121 = pFunFL212.invoke(gInstance, "sdf")      //## issuekeys: 'INVOKE(GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.FEATURELITERALS.FEATURELITERALSMAIN.GOSUFEATURELITERALS.GENERICSFL.ERRANT_FLPARAMETERIZEDFUNCTION)' IN '' CANNOT BE APPLIED TO '(GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.FEATURELITERALS.FEATURELITERALSMAIN.GOSUFEATURELITERALS.GENERICSFL.ERRANT_FLPARAMETERIZEDFUNCTION, JAVA.LANG.STRING)'
    var pFunFLInvoke2122 = pFunFL212.invoke(gInstance)
    var pFunFLInvoke2123 = pFunFL212.invoke(gInstance, new Object())      //## issuekeys: 'INVOKE(GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.FEATURELITERALS.FEATURELITERALSMAIN.GOSUFEATURELITERALS.GENERICSFL.ERRANT_FLPARAMETERIZEDFUNCTION)' IN '' CANNOT BE APPLIED TO '(GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.FEATURELITERALS.FEATURELITERALSMAIN.GOSUFEATURELITERALS.GENERICSFL.ERRANT_FLPARAMETERIZEDFUNCTION, JAVA.LANG.OBJECT)'

    //3rd Set
    //IDE-1599 OS Gosu issue for unbounded parameters in a generic function FL
    var pFunFLInvoke3111 = pFunFL111.invoke(gInstance, "sdf")
    var pFunFLInvoke3112 = pFunFL111.invoke(gInstance)      //## issuekeys: 'INVOKE(JAVA.LANG.OBJECT)' IN '' CANNOT BE APPLIED TO '()'
    //IDE-1599 OS Gosu issue for unbounded parameters in a generic function FL
    var pFunFLInvoke3113 = pFunFL111.invoke(gInstance, new Object())

    var pFunFLInvoke3121 = pFunFL112.invoke(gInstance, "sdf")      //## issuekeys: 'INVOKE()' IN '' CANNOT BE APPLIED TO '(JAVA.LANG.STRING)'
    //IDE-1587
    var pFunFLInvoke3122 = pFunFL112.invoke(gInstance)
    var pFunFLInvoke3123 = pFunFL112.invoke(gInstance, new Object())      //## issuekeys: 'INVOKE()' IN '' CANNOT BE APPLIED TO '(JAVA.LANG.OBJECT)'
  }

  function fun<T extends CharSequence>(p: T): T { return null }
  function fun<T>(p: T): T { return null }

  function testOverloadedGenericMethod() {
    // IDE-2557
    var fl1 = this#fun(CharSequence)
    var res11: String = fl1.invoke("")        //## issuekeys: MSG_TYPE_MISMATCH
    var res12: CharSequence = fl1.invoke("")

    var fl2 = this#fun("")
    var res21: String = fl2.invoke()          //## issuekeys: MSG_TYPE_MISMATCH
    var res22: CharSequence = fl2.invoke()

    var fl3 = this#fun(Object)
    var res2: Object = fl3.invoke(fl3)

    var fl4 = this#fun(Integer.MAX_VALUE)
    var res41: Integer = fl4.invoke()        //## issuekeys: MSG_TYPE_MISMATCH
    var res42: Object = fl4.invoke()
  }
}