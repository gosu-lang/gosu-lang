package gw.specContrib.expressions

class Errant_EmptyIndexArrayMapExpressions {
  //IDE-2317
  var x1 = {1->222}["d"]      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.STRING', REQUIRED: 'JAVA.LANG.INTEGER'
  var inta: int[] = {1, 2, 33}
  var y = inta[]      //## issuekeys: ARRAY TYPE ACCESS MUST BE INDEXED
  var z = Integer[]
  var x2 = int[]
  var aa10: Map<Integer, Integer> = {1->2}[]      //## issuekeys: ARRAY TYPE ACCESS MUST BE INDEXED
  var aa11 = {1->22}[]      //## issuekeys: ARRAY TYPE ACCESS MUST BE INDEXED
  var aa2 = {"a"->2}[]      //## issuekeys: ARRAY TYPE ACCESS MUST BE INDEXED
  var aa3 = {"a"->"b"}["a"]
  var aa4 = {"a"->2}[42]      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'INT', REQUIRED: 'JAVA.LANG.STRING'
  var aa5 = {null->2}[]      //## issuekeys: ARRAY TYPE ACCESS MUST BE INDEXED

  //tests to make sure this change does not break the other simple cases
  function foo(): int[] {
    return null
  }

  function bar(): int[][] {
    return null;
  }

  class MyClass {
  }

  var v = MyClass[]

  function foobar(): MyClass[] {
    return null
  }
}