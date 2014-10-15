package gw.specContrib.expressions.cast

uses java.lang.Integer
uses java.util.ArrayList
uses java.util.HashMap

class Errant_CastToParameterizedCollections {
  var list1 = {"1", "2"} as List<Integer>      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.STRING>' TO 'JAVA.UTIL.LIST<JAVA.LANG.INTEGER>'
  var list2: ArrayList<String> = {1, 2, 3} as ArrayList<String>      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>' TO 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.STRING>'
  var map1: HashMap<String, String> = {1->2} as HashMap<String, String>      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.HASHMAP<JAVA.LANG.INTEGER,JAVA.LANG.INTEGER>' TO 'JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.STRING>'
  var map2 = {1->2} as HashMap<String, String>      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.HASHMAP<JAVA.LANG.INTEGER,JAVA.LANG.INTEGER>' TO 'JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.STRING>'
  var map3 = {1->"2"} as HashMap<String, String>      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.HASHMAP<JAVA.LANG.INTEGER,JAVA.LANG.STRING>' TO 'JAVA.UTIL.HASHMAP<JAVA.LANG.STRING,JAVA.LANG.STRING>'
}
