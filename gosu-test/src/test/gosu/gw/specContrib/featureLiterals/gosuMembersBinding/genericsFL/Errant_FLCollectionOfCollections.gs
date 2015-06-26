package gw.specContrib.featureLiterals.gosuMembersBinding.genericsFL

uses java.io.Serializable
uses java.lang.Cloneable
uses java.lang.Integer
uses java.util.HashMap

/**
 * Created by vdahuja on 1/13/2015.
 */
class Errant_FLCollectionOfCollections {
  var gInstance : Errant_FLCollectionOfCollections

  //collections of collections cases

  function fooFun<T>(hm : HashMap<T, T>) : T {return null}

  var fooFL111 = gInstance#fooFun(HashMap) // OK
  var fooFL111a = gInstance#fooFun(HashMap<Object, Object>) // OK
  var fooFL111b = gInstance#fooFun(HashMap<String, String>) //## issuekeys: MSG_FL_METHOD_NOT_FOUND
  var fooFL112 = gInstance#fooFun({{1->2} -> {3->4}})
  var fooFL113 = gInstance#fooFun({{1->2} -> {3->"bar"}})
  //T is Serializable
  var fooFL114 = gInstance#fooFun({{1->2} -> {"foo", "bar"}})
  var fooFL115 = gInstance#fooFun(HashMap<HashMap<String, String>, HashMap<String, String>>) //## issuekeys: MSG_FL_METHOD_NOT_FOUND
  var fooFL116 = gInstance#fooFun({{"foo"->"bar"}->{"foo"->"bar"}})



  function funInvoke111() {

    var foo111 : Object = fooFL111.invoke(new HashMap())

    var foo112 : Object = fooFL111.invoke({{1->2} -> {3->4}})

    var foo211 : Object  = fooFL112.invoke()
    var foo212 : Object  = fooFL112.invoke({{1->2} -> {3->4}}) //## issuekeys: MSG_WRONG_NUM_OF_ARGS

    var foo311 : Object  = fooFL113.invoke()

    var foo411 : Object = fooFL114.invoke()

    var foo511 : Object = fooFL115.invoke(new HashMap<HashMap<String, String>, HashMap<String, String>>())

    var foo611 : Object = fooFL116.invoke()
  }

  function barFun<T1, T2>(hm : HashMap<T1, T2>) : T1 {return null}
  var barFL111 = gInstance#barFun(HashMap<Object, Object>)
  var barFL112 = gInstance#barFun({{1->2}->{"foo"->"bar"}})
  //IDE-1591 - Parser issue
  var barFL113 = gInstance#barFun(HashMap)(HashMap) //## issuekeys: ERROR
  //IDE-1591 - Parser issue
  var barFL114 = gInstance#barFun(HashMap<HashMap, HashMap<String, String>>)(HashMap<HashMap, HashMap<String, String>>)//## issuekeys: ERROR

  function funInvoke222() {
    //IDE-1592 OS Gosu Issue
    var bar111 : Object = barFL111.invoke(new HashMap<HashMap, HashMap<String, String>>())
    var bar112 : Object  = barFL111.invoke({{1->2} -> {"foo"->"bar"}})
    //IDE-1589
    var bar113 : HashMap = barFL111.invoke({{1,2} -> {"foo"->"bar"}})  //## issuekeys: ERROR

    var bar211 : Object = barFL112.invoke()
  }
}