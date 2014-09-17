package gw.specContrib.expressions

uses java.util.*
uses java.lang.*

class Errant_AdditionalCollectionAndMapInitializersTest {

  function test() {
    var hash1: Map = {1, 1, "foo"}            //## issuekeys: MSG_EXPECTING_ARROW_AFTER_MAP_KEY
    var hash2: Map<Integer, String> = {"foo", "bar", "foobar"}    //## issuekeys: MSG_EXPECTING_ARROW_AFTER_MAP_KEY
    var hash3: Map<Integer, String> = {1, "foo"}     //## issuekeys: MSG_EXPECTING_ARROW_AFTER_MAP_KEY
    var hash4: Map<Integer, String> = {}
    var hash5 = {1 -> "a", 1}                 //## issuekeys: MSG_EXPECTING_ARROW_AFTER_MAP_KEY
    var hash6 = {1, 1 -> "a"}                 //## issuekeys: MSG_EXPECTING_ARROW_AFTER_MAP_KEY
    var hash7 = { {1 -> 2} -> 3 }

    var list1 = {}
    var list2 = {"a", 1}
    var list3: List = { 2 ->  "3"}    //## issuekeys: MSG_EXPECTING_ARROW_AFTER_MAP_KEY
  }

}