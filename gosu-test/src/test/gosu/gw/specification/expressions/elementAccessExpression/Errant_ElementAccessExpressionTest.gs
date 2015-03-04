package gw.specification.expressions.elementAccessExpression

uses java.util.ArrayList
uses java.lang.Integer
uses java.util.Collection
uses java.util.Iterator
uses java.lang.CharSequence
uses java.lang.StringBuilder
uses java.util.Map
uses java.lang.Character


class Errant_ElementAccessExpressionTest {

  static class MyList extends ArrayList<Integer> {

      function food() {
        var f : Integer = this[0]
      }
  }

  static class MyClass {
    public var A : int
    var b : int as B

    construct() {
      A = 1
      b = 2
    }
  }

  function testBasic() {
    var i : int
    var x : char

    var a : int[] = {1,2,3}
    i = a[0]
    a[0] = 8
    a["x"] = 3  //## issuekeys: MSG_TYPE_MISMATCH, MSG_ARRAY_INDEX_MUST_BE_INT
    a['x'] = 3

    var b : List<Integer> = {1,2,3}
    i = b[0]
    b[new Integer(0)] = 8
    b["x"] = 3  //## issuekeys: MSG_TYPE_MISMATCH, MSG_ARRAY_INDEX_MUST_BE_INT
    b['x'] = 3

    var c : Collection<Integer> = {1,2,3}
    i = c[0]  //## issuekeys: MSG_TYPE_MISMATCH, MSG_PROPERTY_REFLECTION_ONLY_WITH_STRINGS
    var d : Iterator<Integer> = {1,2,3}.iterator()
    i = d[0]  //## issuekeys: MSG_TYPE_MISMATCH, MSG_PROPERTY_REFLECTION_ONLY_WITH_STRINGS
    var e : ArrayList<Integer> = {1,2,3}
    i = e[0]
    e[0] = 8
    i = e[new Integer(1)]
    e[new Integer(1)] = 8

    var f : CharSequence = "123"
    x = f[0]
    x = f[new Integer(1)]
    f[0] = '8'  //## issuekeys: MSG_STR_IMMUTABLE
    x = f["A"]   //## issuekeys: MSG_TYPE_MISMATCH, MSG_ARRAY_INDEX_MUST_BE_INT

    var g : StringBuilder = new StringBuilder("123")
    x = g[0]
    g[0] = '8'

    var h : String = "123"
    x = h[0]
    h[0] = '8'  //## issuekeys: MSG_STR_IMMUTABLE
    var j : dynamic.Dynamic = {1,2,3}
    i = j[0]
    j[0] = 8

    var k : Map<Integer, Character> = {1-> 'a', 2 -> 'b', 3 -> 'c'}
    x = k[1]
    k[1] = '8'

    var l : Map<String, Character> = {"A"-> 'a', "B"-> 'b', "C" -> 'c'}
     x = l[1]  //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
    x = l["A"]
    l["A"] = '8'
    l['a'] = '6'

    var m : Map =  {1-> 'a', 2 -> 'b', 3 -> 'c'}
    x = m[1] as Character
    m[1] = '8'

    var n : MyClass = new MyClass()
    i = n["A"] as int
    n["A"] = 8
    i = n["B"] as int
    n["B"] = 8
    var ex : boolean = false
    i = n["C"] as int
  }
}