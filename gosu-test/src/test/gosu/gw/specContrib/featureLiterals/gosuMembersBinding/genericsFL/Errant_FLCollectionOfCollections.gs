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

  var fooFL111 = gInstance#fooFun(HashMap<HashMap, HashMap>)
  var fooFL112 = gInstance#fooFun({{1->2} -> {3->4}})
  var fooFL113 = gInstance#fooFun({{1->2} -> {3->"bar"}})
  //T is Serializable
  var fooFL114 = gInstance#fooFun({{1->2} -> {"foo", "bar"}})
  var fooFL115 = gInstance#fooFun(HashMap<HashMap<String, String>, HashMap<String, String>>)
  var fooFL116 = gInstance#fooFun({{"foo"->"bar"}->{"foo"->"bar"}})



  function funInvoke111() {
    //IDE-1592 OS Gosu Issue
    var foo111 : HashMap = fooFL111.invoke(new HashMap<HashMap, HashMap>())
    //IDE-1592 OS Gosu Issue
    var foo112 : HashMap = fooFL111.invoke({{1->2} -> {3->4}})

    //IDE-1592 OS Gosu Issue
    var foo211 : HashMap<Integer,Integer> = fooFL112.invoke()
    var foo212 : HashMap<Integer,Integer> = fooFL112.invoke({{1->2} -> {3->4}})      //## issuekeys: 'INVOKE()' IN '' CANNOT BE APPLIED TO '(JAVA.UTIL.HASHMAP<JAVA.UTIL.HASHMAP<JAVA.LANG.INTEGER,JAVA.LANG.INTEGER>,JAVA.UTIL.HASHMAP<JAVA.LANG.INTEGER,JAVA.LANG.INTEGER>>)'

    //IDE-1592 OS Gosu Issue
    var foo311 : HashMap<Integer,Serializable> = fooFL113.invoke()

    //IDE-1592 OS Gosu Issue
    var foo411 : Serializable & Cloneable = fooFL114.invoke()

    //IDE-1592 OS Gosu Issue
    var foo511 : HashMap<String,String> = fooFL115.invoke(new HashMap<HashMap<String, String>, HashMap<String, String>>())
    //IDE-1589 - Parser Issue
    var foo512 = fooFL115.invoke({{1->2} -> {3->4}})  //## issuekeys: ERROR

    //IDE-1592 OS Gosu Issue
    var foo611 : HashMap<String,String> = fooFL116.invoke()

  }

  function barFun<T1, T2>(hm : HashMap<T1, T2>) : T1 {return null}
  var barFL111 = gInstance#barFun(HashMap<HashMap, HashMap<String, String>>)
  var barFL112 = gInstance#barFun({{1->2}->{"foo"->"bar"}})
  //IDE-1591 - Parser issue
  var barFL113 = gInstance#barFun(HashMap)(HashMap) //## issuekeys: ERROR
  //IDE-1591 - Parser issue
  var barFL114 = gInstance#barFun(HashMap<HashMap, HashMap<String, String>>)(HashMap<HashMap, HashMap<String, String>>)//## issuekeys: ERROR

  function funInvoke222() {
    //IDE-1592 OS Gosu Issue
    var bar111 : HashMap = barFL111.invoke(new HashMap<HashMap, HashMap<String, String>>())
    var bar112 : HashMap  = barFL111.invoke({{1->2} -> {"foo"->"bar"}})
    //IDE-1589
    var bar113 : HashMap = barFL111.invoke({{1,2} -> {"foo"->"bar"}})  //## issuekeys: ERROR

    var bar211 : HashMap<Integer, Integer> = barFL112.invoke()
  }
}