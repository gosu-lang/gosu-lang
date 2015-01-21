package gw.specContrib.classes.method_Scoring.arrays_And_Lists

uses java.lang.Integer
uses java.util.ArrayList

class Errant_ListArrayList {
  class AAA {}
  class BBB {}
  function foo9(l : List<Integer>) : AAA { return null }
  function foo9(a : ArrayList<Integer>) :BBB { return null }

  function caller() {
    //IDE-1521
    var a : AAA = foo9({1, 2, 3})                  //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.CLASSES.METHOD_SCORING.ARRAYS_AND_LISTS.ERRANT_LISTARRAYLIST.BBB', REQUIRED: 'GW.SPECCONTRIB.CLASSES.METHOD_SCORING.ARRAYS_AND_LISTS.ERRANT_LISTARRAYLIST.AAA'
    var b : BBB = foo9({1, 2, 3})
  }
}