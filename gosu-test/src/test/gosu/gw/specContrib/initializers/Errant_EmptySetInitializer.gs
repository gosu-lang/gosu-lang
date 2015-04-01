package gw.specContrib.initializers

uses java.util.ArrayList
uses java.util.HashSet
uses java.util.Set

class Errant_EmptySetInitializer {
  //IDE-1535
  var s1: Set<String> = { }
  var s2: Set = { }
  var s3: Set<String> = { "string" }

  var h1: HashSet<String> = { }
  var h2: HashSet = { }

  var aList1 : ArrayList<String> = { }
  var aList2 : ArrayList = { }

  var list : List<String> = { }
  var list2 : List = {}

  var array1 : int[] = { }
}