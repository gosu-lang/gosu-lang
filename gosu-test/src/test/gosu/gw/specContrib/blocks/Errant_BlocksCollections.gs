package gw.specContrib.blocks

uses java.lang.Double
uses java.lang.Integer
uses java.util.ArrayList
uses java.util.HashMap

class Errant_BlocksCollections {

  function testCollectionsBlocks() {
    var block6111: block(): ArrayList = \-> {
      return {1, 2, 3}
    }
    var block6112: block(): ArrayList = \-> { 1,2,3  }    //## issuekeys: NOT A STATEMENT
    var block6113: block(): ArrayList<Object> = \-> {
      return {1, 2, 3}
    }
    var block6114: block(): ArrayList<String> = \-> {  return {1, 2, 3} }     //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'BLOCK():ARRAYLIST<INTEGER>', REQUIRED: 'BLOCK():ARRAYLIST<STRING>'

    var block6115: block(): HashMap = \-> {
      return {1->2, 3->4}
    }

    //ALERT (inconsistent)- IDE-1322 - Should NOT show error
    var block6116: block(): HashMap<Object, Object> = \-> {
      return {1->2, 3->4}
    }

    var block6117: block(): HashMap<Object, Object> = \-> {
      return new HashMap()
    }

    var block6118: block(): HashMap<Object, Object> = \-> new HashMap()

    var block6119: block(): HashMap = \-> {
      1->2,3->4 }     //## issuekeys: NOT A STATEMENT

    var block6120: block(): HashMap = \-> { return {1, 2, 3}  }    //## issuekeys: EXPECTED '->' AFTER MAP KEY

    var block6121: block(): HashMap = \-> {
      return {1->2, 3->4.5}
    }

    var block6122: block(): HashMap<Integer, Integer> = \-> {
      return {1->2, 3->4.5}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'DOUBLE', REQUIRED: 'JAVA.LANG.INTEGER'
    }

    var block6123: block(): HashMap<Double, Double> = \-> {
      return {1->2, 3->4.5}
    }

    var block6124: block(): HashMap<Integer, Double> = \-> {
      return {1.5->2, 3->4.5}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'DOUBLE', REQUIRED: 'JAVA.LANG.INTEGER'
    }

    var block6125: block(): HashMap<Integer, String> = \-> { return {1->2, 3->4.5} }     //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'INT', REQUIRED: 'JAVA.LANG.STRING'

  }

  //sort, each, map
  function testBlocksAndCollections() {
    var myStrings = {"a", "d", "c", "e"}
    var sortedStrings111 = myStrings.sortBy(\ele -> ele.length())
    myStrings.each(\elt -> elt.toLowerCase())
    var mappedList = myStrings.map(\elt -> elt)
  }

}