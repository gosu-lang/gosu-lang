package gw.specContrib.featureLiterals.gosuMembersBinding.genericsFL

uses java.lang.Integer
uses java.util.HashMap

/**
 * Created by vdahuja on 1/12/2015.
 */
class Errant_HashMapFL {

  var hmapObj : HashMap
  var hmapStrStr : HashMap<String, String>
  var hmapStrInt : HashMap<String, Integer>

  //Case#1 HashMap<Object, Object>
  var hmapfun11 = HashMap#put()

  function invokeFun11() {
    hmapfun11.invoke(new HashMap(), "mystring1", "mystring2")
    hmapfun11.invoke(new HashMap<String, String>(), "mystring1", "mystring2")
    hmapfun11.invoke(new HashMap<String, String>(), 42, 42)
    hmapfun11.invoke(new HashMap<String, Integer>(), "mystring1", 42)
    hmapfun11.invoke(new HashMap<String, Integer>(), "mystring1", "mystring2")

    hmapfun11.invoke(hmapObj, "mystring1", "mystring2")
    hmapfun11.invoke(hmapStrStr, "mystring1", "mystring2")
    hmapfun11.invoke(hmapStrStr, 42, 42)
    hmapfun11.invoke(hmapStrInt, "mystring1", 42)
    hmapfun11.invoke(hmapStrInt, "mystring1", "mystring2")
  }

  //Case#2 HashMap<String, String>
  var hmapfun12 = HashMap<String, String>#put()

  function invokeFun12() {
    hmapfun12.invoke(new HashMap(), "mystring1", "mystring2")      //## issuekeys: 'INVOKE(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.STRING>, JAVA.LANG.STRING, JAVA.LANG.STRING)' IN '' CANNOT BE APPLIED TO '(JAVA.UTIL.HASHMAP, JAVA.LANG.STRING, JAVA.LANG.STRING)'
    hmapfun12.invoke(new HashMap<String, String>(), "mystring1", "mystring2")
    hmapfun12.invoke(new HashMap<String, String>(), 42, 42)      //## issuekeys: 'INVOKE(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.STRING>, JAVA.LANG.STRING, JAVA.LANG.STRING)' IN '' CANNOT BE APPLIED TO '(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.STRING>, INT, INT)'
    hmapfun12.invoke(new HashMap<String, Integer>(), "mystring1", 42)      //## issuekeys: 'INVOKE(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.STRING>, JAVA.LANG.STRING, JAVA.LANG.STRING)' IN '' CANNOT BE APPLIED TO '(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.INTEGER>, JAVA.LANG.STRING, INT)'
    hmapfun12.invoke(new HashMap<String, Integer>(), "mystring1", "mystring2")      //## issuekeys: 'INVOKE(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.STRING>, JAVA.LANG.STRING, JAVA.LANG.STRING)' IN '' CANNOT BE APPLIED TO '(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.INTEGER>, JAVA.LANG.STRING, JAVA.LANG.STRING)'

    hmapfun12.invoke(hmapObj, "mystring1", "mystring2")      //## issuekeys: 'INVOKE(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.STRING>, JAVA.LANG.STRING, JAVA.LANG.STRING)' IN '' CANNOT BE APPLIED TO '(JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>, JAVA.LANG.STRING, JAVA.LANG.STRING)'
    hmapfun12.invoke(hmapStrStr, "mystring1", "mystring2")
    hmapfun12.invoke(hmapStrStr, 42, 42)      //## issuekeys: 'INVOKE(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.STRING>, JAVA.LANG.STRING, JAVA.LANG.STRING)' IN '' CANNOT BE APPLIED TO '(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.STRING>, INT, INT)'
    hmapfun12.invoke(hmapStrInt, "mystring1", 42)      //## issuekeys: 'INVOKE(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.STRING>, JAVA.LANG.STRING, JAVA.LANG.STRING)' IN '' CANNOT BE APPLIED TO '(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.INTEGER>, JAVA.LANG.STRING, INT)'
    hmapfun12.invoke(hmapStrInt, "mystring1", "mystring2")      //## issuekeys: 'INVOKE(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.STRING>, JAVA.LANG.STRING, JAVA.LANG.STRING)' IN '' CANNOT BE APPLIED TO '(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.INTEGER>, JAVA.LANG.STRING, JAVA.LANG.STRING)'
  }

  //Case#3 HashMap<String, String>#put(String, String)
  var hmapfun13 = HashMap<String, String>#put(String, String)

  function invokeFun13() {
    hmapfun13.invoke(new HashMap(), "mystring1", "mystring2")      //## issuekeys: 'INVOKE(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.STRING>, JAVA.LANG.STRING, JAVA.LANG.STRING)' IN '' CANNOT BE APPLIED TO '(JAVA.UTIL.HASHMAP, JAVA.LANG.STRING, JAVA.LANG.STRING)'
    hmapfun13.invoke(new HashMap<String, String>(), "mystring1", "mystring2")
    hmapfun13.invoke(new HashMap<String, String>(), 42, 42)      //## issuekeys: 'INVOKE(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.STRING>, JAVA.LANG.STRING, JAVA.LANG.STRING)' IN '' CANNOT BE APPLIED TO '(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.STRING>, INT, INT)'
    hmapfun13.invoke(new HashMap<String, Integer>(), "mystring1", 42)      //## issuekeys: 'INVOKE(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.STRING>, JAVA.LANG.STRING, JAVA.LANG.STRING)' IN '' CANNOT BE APPLIED TO '(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.INTEGER>, JAVA.LANG.STRING, INT)'
    hmapfun13.invoke(new HashMap<String, Integer>(), "mystring1", "mystring2")      //## issuekeys: 'INVOKE(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.STRING>, JAVA.LANG.STRING, JAVA.LANG.STRING)' IN '' CANNOT BE APPLIED TO '(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.INTEGER>, JAVA.LANG.STRING, JAVA.LANG.STRING)'

    hmapfun13.invoke(hmapObj, "mystring1", "mystring2")      //## issuekeys: 'INVOKE(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.STRING>, JAVA.LANG.STRING, JAVA.LANG.STRING)' IN '' CANNOT BE APPLIED TO '(JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>, JAVA.LANG.STRING, JAVA.LANG.STRING)'
    hmapfun13.invoke(hmapStrStr, "mystring1", "mystring2")
    hmapfun13.invoke(hmapStrStr, 42, 42)      //## issuekeys: 'INVOKE(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.STRING>, JAVA.LANG.STRING, JAVA.LANG.STRING)' IN '' CANNOT BE APPLIED TO '(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.STRING>, INT, INT)'
    hmapfun13.invoke(hmapStrInt, "mystring1", 42)      //## issuekeys: 'INVOKE(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.STRING>, JAVA.LANG.STRING, JAVA.LANG.STRING)' IN '' CANNOT BE APPLIED TO '(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.INTEGER>, JAVA.LANG.STRING, INT)'
    hmapfun13.invoke(hmapStrInt, "mystring1", "mystring2")      //## issuekeys: 'INVOKE(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.STRING>, JAVA.LANG.STRING, JAVA.LANG.STRING)' IN '' CANNOT BE APPLIED TO '(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.INTEGER>, JAVA.LANG.STRING, JAVA.LANG.STRING)'
  }

  //Case#5 HashMap<String, String>#put("mystring", "mystring")
  var hmapfun15 = HashMap<String, String>#put("mystring", "mystring")
  function invokeFun15() {
    hmapfun15.invoke(new HashMap<String, String>())
    hmapfun15.invoke(new HashMap(), "mystring1", "mystring2")      //## issuekeys: 'INVOKE(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.STRING>)' IN '' CANNOT BE APPLIED TO '(JAVA.UTIL.HASHMAP, JAVA.LANG.STRING, JAVA.LANG.STRING)'
    hmapfun15.invoke(new HashMap<String, String>(), "mystring1", "mystring2")      //## issuekeys: 'INVOKE(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.STRING>)' IN '' CANNOT BE APPLIED TO '(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.STRING>, JAVA.LANG.STRING, JAVA.LANG.STRING)'
    hmapfun15.invoke(new HashMap<String, String>(), 42, 42)      //## issuekeys: 'INVOKE(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.STRING>)' IN '' CANNOT BE APPLIED TO '(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.STRING>, INT, INT)'
    hmapfun15.invoke(new HashMap<String, Integer>(), "mystring1", 42)      //## issuekeys: 'INVOKE(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.STRING>)' IN '' CANNOT BE APPLIED TO '(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.INTEGER>, JAVA.LANG.STRING, INT)'
    hmapfun15.invoke(new HashMap<String, Integer>(), "mystring1", "mystring2")      //## issuekeys: 'INVOKE(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.STRING>)' IN '' CANNOT BE APPLIED TO '(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.INTEGER>, JAVA.LANG.STRING, JAVA.LANG.STRING)'

    hmapfun15.invoke(hmapStrStr)
    hmapfun15.invoke(hmapObj, "mystring1", "mystring2")      //## issuekeys: 'INVOKE(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.STRING>)' IN '' CANNOT BE APPLIED TO '(JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>, JAVA.LANG.STRING, JAVA.LANG.STRING)'
    hmapfun15.invoke(hmapStrStr, "mystring1", "mystring2")      //## issuekeys: 'INVOKE(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.STRING>)' IN '' CANNOT BE APPLIED TO '(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.STRING>, JAVA.LANG.STRING, JAVA.LANG.STRING)'
    hmapfun15.invoke(hmapStrStr, 42, 42)      //## issuekeys: 'INVOKE(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.STRING>)' IN '' CANNOT BE APPLIED TO '(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.STRING>, INT, INT)'
    hmapfun15.invoke(hmapStrInt, "mystring1", 42)      //## issuekeys: 'INVOKE(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.STRING>)' IN '' CANNOT BE APPLIED TO '(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.INTEGER>, JAVA.LANG.STRING, INT)'
    hmapfun15.invoke(hmapStrInt, "mystring1", "mystring2")      //## issuekeys: 'INVOKE(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.STRING>)' IN '' CANNOT BE APPLIED TO '(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.INTEGER>, JAVA.LANG.STRING, JAVA.LANG.STRING)'
  }

  //Case#3 HashMap#put("mystring", "mystring")
  var hmapfun16 = HashMap#put("mystring", "mystring")
  function invokeFun16() {
    hmapfun16.invoke(new HashMap<String, String>())
    hmapfun16.invoke(new HashMap(), "mystring1", "mystring2")      //## issuekeys: 'INVOKE(JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>)' IN '' CANNOT BE APPLIED TO '(JAVA.UTIL.HASHMAP, JAVA.LANG.STRING, JAVA.LANG.STRING)'
    hmapfun16.invoke(new HashMap<String, String>(), "mystring1", "mystring2")      //## issuekeys: 'INVOKE(JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>)' IN '' CANNOT BE APPLIED TO '(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.STRING>, JAVA.LANG.STRING, JAVA.LANG.STRING)'
    hmapfun16.invoke(new HashMap<String, String>(), 42, 42)      //## issuekeys: 'INVOKE(JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>)' IN '' CANNOT BE APPLIED TO '(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.STRING>, INT, INT)'
    hmapfun16.invoke(new HashMap<String, String>())
    hmapfun16.invoke(new HashMap<String, Integer>())
    hmapfun16.invoke(new HashMap<Integer, Integer>())
    hmapfun16.invoke(new HashMap<String, Integer>(), "mystring1", 42)      //## issuekeys: 'INVOKE(JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>)' IN '' CANNOT BE APPLIED TO '(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.INTEGER>, JAVA.LANG.STRING, INT)'
    hmapfun16.invoke(new HashMap<String, Integer>(), "mystring1", "mystring2")      //## issuekeys: 'INVOKE(JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>)' IN '' CANNOT BE APPLIED TO '(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.INTEGER>, JAVA.LANG.STRING, JAVA.LANG.STRING)'

    hmapfun16.invoke(hmapStrStr, "mystring1", "mystring2")      //## issuekeys: 'INVOKE(JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>)' IN '' CANNOT BE APPLIED TO '(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.STRING>, JAVA.LANG.STRING, JAVA.LANG.STRING)'
    hmapfun16.invoke(hmapStrStr)
    hmapfun16.invoke(hmapObj, "mystring1", "mystring2")      //## issuekeys: 'INVOKE(JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>)' IN '' CANNOT BE APPLIED TO '(JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>, JAVA.LANG.STRING, JAVA.LANG.STRING)'
    hmapfun16.invoke(hmapObj)
    hmapfun16.invoke(hmapStrInt, "mystring1", 42)      //## issuekeys: 'INVOKE(JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>)' IN '' CANNOT BE APPLIED TO '(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.INTEGER>, JAVA.LANG.STRING, INT)'
    hmapfun16.invoke(hmapStrInt)
  }

  //Case#4 HashMap<String, Integer>#put(String, Integer)
  var hmapfun14 = HashMap<String, Integer>#put(String, Integer)

  function invokeFun14() {
    hmapfun14.invoke(new HashMap(), "mystring1", "mystring2")      //## issuekeys: 'INVOKE(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.INTEGER>, JAVA.LANG.STRING, JAVA.LANG.INTEGER)' IN '' CANNOT BE APPLIED TO '(JAVA.UTIL.HASHMAP, JAVA.LANG.STRING, JAVA.LANG.STRING)'
    hmapfun14.invoke(new HashMap<String, String>(), "mystring1", "mystring2")      //## issuekeys: 'INVOKE(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.INTEGER>, JAVA.LANG.STRING, JAVA.LANG.INTEGER)' IN '' CANNOT BE APPLIED TO '(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.STRING>, JAVA.LANG.STRING, JAVA.LANG.STRING)'
    hmapfun14.invoke(new HashMap<String, String>(), 42, 42)      //## issuekeys: 'INVOKE(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.INTEGER>, JAVA.LANG.STRING, JAVA.LANG.INTEGER)' IN '' CANNOT BE APPLIED TO '(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.STRING>, INT, INT)'
    hmapfun14.invoke(new HashMap<String, Integer>(), "mystring1", 42)
    hmapfun14.invoke(new HashMap<String, Integer>(), "mystring1", "mystring2")      //## issuekeys: 'INVOKE(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.INTEGER>, JAVA.LANG.STRING, JAVA.LANG.INTEGER)' IN '' CANNOT BE APPLIED TO '(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.INTEGER>, JAVA.LANG.STRING, JAVA.LANG.STRING)'

    hmapfun14.invoke(hmapObj, "mystring1", "mystring2")      //## issuekeys: 'INVOKE(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.INTEGER>, JAVA.LANG.STRING, JAVA.LANG.INTEGER)' IN '' CANNOT BE APPLIED TO '(JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>, JAVA.LANG.STRING, JAVA.LANG.STRING)'
    hmapfun14.invoke(hmapStrStr, "mystring1", "mystring2")      //## issuekeys: 'INVOKE(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.INTEGER>, JAVA.LANG.STRING, JAVA.LANG.INTEGER)' IN '' CANNOT BE APPLIED TO '(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.STRING>, JAVA.LANG.STRING, JAVA.LANG.STRING)'
    hmapfun14.invoke(hmapStrStr, 42, 42)      //## issuekeys: 'INVOKE(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.INTEGER>, JAVA.LANG.STRING, JAVA.LANG.INTEGER)' IN '' CANNOT BE APPLIED TO '(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.STRING>, INT, INT)'
    hmapfun14.invoke(hmapStrInt, "mystring1", 42)
    hmapfun14.invoke(hmapStrInt, "mystring1", "mystring2")      //## issuekeys: 'INVOKE(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.INTEGER>, JAVA.LANG.STRING, JAVA.LANG.INTEGER)' IN '' CANNOT BE APPLIED TO '(JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.INTEGER>, JAVA.LANG.STRING, JAVA.LANG.STRING)'
  }


  //At declaration, type of both should be same - arguments (Type) as well as Collection parameters
  var hmapfun18 = HashMap<String, String>#put(String, Integer)      //## issuekeys: CANNOT RESOLVE METHOD 'PUT(JAVA.LANG.STRING, JAVA.LANG.INTEGER)'
  var hmapfun19 = HashMap#put(Object, Object)
  var hmapfun20 = HashMap#put(String, String)      //## issuekeys: CANNOT RESOLVE METHOD 'PUT(JAVA.LANG.STRING, JAVA.LANG.STRING)'
  var hmapfun21 = HashMap#put("mystring", 42)
  var hmapfun22 = HashMap<String, String>#put("mystring", 42)      //## issuekeys: 'PUT(JAVA.LANG.STRING, JAVA.LANG.STRING)' IN 'JAVA.UTIL.HASHMAP' CANNOT BE APPLIED TO '(JAVA.LANG.STRING, INT)'

}