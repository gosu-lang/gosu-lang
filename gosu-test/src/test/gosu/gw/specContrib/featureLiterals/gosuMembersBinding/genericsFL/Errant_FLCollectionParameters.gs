package gw.specContrib.featureLiterals.gosuMembersBinding.genericsFL

uses java.lang.Integer
uses java.util.ArrayList
uses java.util.HashMap

/**
 * Created by vdahuja on 1/12/2015.
 */
class Errant_FLCollectionParameters {

  var gInstance : Errant_FLCollectionParameters

  function takeHashMapObject(hm : HashMap) {}
  var funhmo111 = #takeHashMapObject(HashMap)
  //IDE-1590 - OS Gosu Issue
  var funhmo112 = #takeHashMapObject(HashMap<String, String>)      //## issuekeys: CANNOT RESOLVE METHOD 'TAKEHASHMAPOBJECT(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.STRING>)'

  function takeArrayListObject1(al: ArrayList){}
  //IDE-1590 - OS Gosu Issue
  var hiAL1FL = #takeArrayListObject1(ArrayList<String>)      //## issuekeys: CANNOT RESOLVE METHOD 'TAKEARRAYLISTOBJECT1(JAVA.UTIL.ARRAYLIST<JAVA.LANG.STRING>)'

  function takeArrayListObject2(al : ArrayList<Object>){}
  //IDE-1590 - OS Gosu Issue
  var hiAL1FL1 = #takeArrayListObject2(ArrayList<String>)      //## issuekeys: CANNOT RESOLVE METHOD 'TAKEARRAYLISTOBJECT2(JAVA.UTIL.ARRAYLIST<JAVA.LANG.STRING>)'


  function  takeHashMap(hm : HashMap<String, String>) {}

  var funhm111 = #takeHashMap(HashMap<String, String>)
  var funhm112 = #takeHashMap()
  var funhm113 = #takeHashMap(ArrayList)      //## issuekeys: CANNOT RESOLVE METHOD 'TAKEHASHMAP(JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>)'
  var funhm114 = #takeHashMap(HashMap<String, Integer>)      //## issuekeys: CANNOT RESOLVE METHOD 'TAKEHASHMAP(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.INTEGER>)'
  var funhm115 = #takeHashMap(HashMap)      //## issuekeys: CANNOT RESOLVE METHOD 'TAKEHASHMAP(JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>)'

  var funhm211 = Errant_FLCollectionParameters#takeHashMap(HashMap<String, String>)
  var funhm212 = Errant_FLCollectionParameters#takeHashMap()
  var funhm213 = Errant_FLCollectionParameters#takeHashMap(ArrayList)      //## issuekeys: CANNOT RESOLVE METHOD 'TAKEHASHMAP(JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>)'
  var funhm214 = Errant_FLCollectionParameters#takeHashMap(HashMap<String, Integer>)      //## issuekeys: CANNOT RESOLVE METHOD 'TAKEHASHMAP(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.INTEGER>)'
  var funhm215 = Errant_FLCollectionParameters#takeHashMap(HashMap)      //## issuekeys: CANNOT RESOLVE METHOD 'TAKEHASHMAP(JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>)'


  var funhm311 = gInstance#takeHashMap(HashMap<String, String>)
  var funhm312 = gInstance#takeHashMap()
  var funhm313 = gInstance#takeHashMap(ArrayList)      //## issuekeys: CANNOT RESOLVE METHOD 'TAKEHASHMAP(JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>)'
  var funhm314 = gInstance#takeHashMap(HashMap<String, Integer>)      //## issuekeys: CANNOT RESOLVE METHOD 'TAKEHASHMAP(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.INTEGER>)'
  var funhm315 = gInstance#takeHashMap(HashMap)      //## issuekeys: CANNOT RESOLVE METHOD 'TAKEHASHMAP(JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>)'

  function invoke() {

    //funhm2
    //the following Method FL is unbound
    //so here instance is required as argument
    //IDE-1587
    funhm111.invoke(this, new HashMap<String, String>())
    funhm111.invoke(this, {"foo"->"bar"})
    //IDE-1589
    funhm111.invoke({"foo"->42})                //## issuekeys: ERROR
    funhm111.invoke(new HashMap<String, Integer>())      //## issuekeys: 'INVOKE(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.STRING>)' IN '' CANNOT BE APPLIED TO '(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.INTEGER>)'

    //IDE-1587
    funhm112.invoke(this, new HashMap<String, String>())
    funhm112.invoke(this, {"foo"->"bar"})
    //IDE-1589
    funhm112.invoke({"foo"->42})                 //## issuekeys: ERROR
    funhm112.invoke(new HashMap<String, Integer>())      //## issuekeys: 'INVOKE(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.STRING>)' IN '' CANNOT BE APPLIED TO '(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.INTEGER>)'

    //funhm2
    funhm211.invoke(gInstance, new HashMap<String, String>())
    funhm211.invoke(gInstance, {"foo"->"bar"})
    //IDE-1589
    funhm211.invoke(gInstance, {"foo"->42})     //## issuekeys: ERROR
    funhm211.invoke(gInstance, new HashMap<String, Integer>())      //## issuekeys: 'INVOKE(GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.FEATURELITERALS.FEATURELITERALSMAIN.GOSUFEATURELITERALS.GENERICSFL.ERRANT_FLCOLLECTIONPARAMETERS, JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.STRING>)' IN '' CANNOT BE APPLIED TO '(GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.FEATURELITERALS.FEATURELITERALSMAIN.GOSUFEATURELITERALS.GENERICSFL.ERRANT_FLCOLLECTIONPARAMETERS, JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.INTEGER>)'

    funhm212.invoke(gInstance, new HashMap<String, String>())
    funhm212.invoke(gInstance, {"foo"->"bar"})
    //IDE-1589
    funhm212.invoke(gInstance, {"foo"->42})        //## issuekeys: ERROR
    funhm212.invoke(gInstance, new HashMap<String, Integer>())      //## issuekeys: 'INVOKE(GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.FEATURELITERALS.FEATURELITERALSMAIN.GOSUFEATURELITERALS.GENERICSFL.ERRANT_FLCOLLECTIONPARAMETERS, JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.STRING>)' IN '' CANNOT BE APPLIED TO '(GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.FEATURELITERALS.FEATURELITERALSMAIN.GOSUFEATURELITERALS.GENERICSFL.ERRANT_FLCOLLECTIONPARAMETERS, JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.INTEGER>)'

    //funhm3
    funhm311.invoke(new HashMap<String, String>())
    funhm311.invoke({"foo"->"bar"})
    //IDE-1589
    funhm311.invoke({"foo"->42})                  //## issuekeys: ERROR
    funhm311.invoke(new HashMap<String, Integer>())      //## issuekeys: 'INVOKE(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.STRING>)' IN '' CANNOT BE APPLIED TO '(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.INTEGER>)'

    funhm312.invoke(new HashMap<String, String>())
    funhm312.invoke({"foo"->"bar"})
    //IDE-1589
    funhm312.invoke({"foo"->42})               //## issuekeys: ERROR
    funhm312.invoke(new HashMap<String, Integer>())      //## issuekeys: 'INVOKE(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.STRING>)' IN '' CANNOT BE APPLIED TO '(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.INTEGER>)'

  }
}