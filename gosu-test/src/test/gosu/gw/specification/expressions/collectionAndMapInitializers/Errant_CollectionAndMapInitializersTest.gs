package gw.specification.expressions.collectionAndMapInitializers

uses java.util.*
uses java.lang.*

class Errant_CollectionAndMapInitializersTest {
  function testBasicCollection() {
    var x0 = {}
    var x1 : List = {}
    var x4 : Collection = {1, 2, 3}
    var x5 : List<List<Integer>>  = {{1}, {1, 2}}
    var x6 : Collection = new ArrayList() {1, 2, 3}
    var x7 : Collection = new {1, 2, 3}  //## issuekeys: MSG_EXPECTING_NEW_ARRAY_OR_CTOR, MSG_EXPECTING_TYPE_NAME, MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN
    var x8 : Collection = {null}
    var x10  =  new String() {1, 2, 3}  //## issuekeys: MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN
    var x15 : Collection = new ArrayList(2) {1, 2, 3}
    var x16 : List = new ArrayList({4,5}) {1, 2, 3}
    var list2 : List = {"a", 1}
  }

  function testBasicMap() {
    var x0 : Map = {}
    var x2 = { 1-> "2", 2 -> "4"}
    var x3 : Map = {{1->2} -> 3}
    var x1 : Map = { 1, 2 -> 3}  //## issuekeys: MSG_EXPECTING_ARROW_AFTER_MAP_KEY
    var hash5 : Map = {1 -> "a", 1}   //## issuekeys: MSG_EXPECTING_ARROW_AFTER_MAP_KEY
    var hash1 : Map = {1, 1, "foo"}  //## issuekeys: MSG_TYPE_MISMATCH
    var hash7 : Map = { {1 -> 2} -> 3 }
  }

  function testErrors(){
    var x11 : Collection  =  {1 -> 2}  //## issuekeys: MSG_EXPECTING_CLOSE_BRACE_FOR_INITIALIZER, MSG_UNEXPECTED_ARROW, MSG_UNEXPECTED_TOKEN
    var x3 : List = { 1, 2 -> 3}  //## issuekeys: MSG_END_OF_STMT, MSG_EXPECTING_CLOSE_BRACE_FOR_INITIALIZER, MSG_UNEXPECTED_ARROW, MSG_UNEXPECTED_TOKEN
  }

}