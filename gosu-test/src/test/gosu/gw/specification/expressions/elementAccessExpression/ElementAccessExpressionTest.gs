package gw.specification.expressions.elementAccessExpression

uses gw.BaseVerifyErrantTest
uses java.util.Iterator
uses junit.framework.TestCase
uses java.lang.Integer
uses java.util.ArrayList
uses java.util.Collection
uses java.lang.CharSequence
uses java.lang.StringBuilder
uses java.util.Map
uses java.lang.Character
uses java.lang.IllegalArgumentException

class ElementAccessExpressionTest extends BaseVerifyErrantTest {

  function testErrant_ElementAccessExpressionTest() {
    processErrantType(Errant_ElementAccessExpressionTest)
  }

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
     assertEquals(1, i)
     a[0] = 8
     assertEquals(8, a[0])

     var b : List<Integer> = {1,2,3}
     i = b[0]
     assertEquals(1, i)
     b[new Integer(0)] = 8
     assertEquals(8, b[0])

     var c : Collection<Integer> = {1,2,3}
     //i = c[0]
     var d : Iterator<Integer> = {1,2,3}.iterator()
     //i = d[0]
     var e : ArrayList<Integer> = {1,2,3}
     i = e[0]
     assertEquals(1, i)
     e[0] = 8
     assertEquals(8, e[0])

     var f : CharSequence = "123"
     x = f[0]
     assertEquals('1', x)
     //f[0] = '8'
     //assertEquals('8', f[0])

     var g : StringBuilder = new StringBuilder("123")
     x = g[0]
     assertEquals('1', x)
     g[0] = '8'
     assertEquals('8', g[0])

     var h : String = "123"
     x = h[0]
     assertEquals('1', x)
     //h[0] = '8'
     //assertEquals('8', x)
     var j : dynamic.Dynamic = {1,2,3}
     i = j[0]
     assertEquals(1, i)
     j[0] = 8
     assertEquals(8, j[0])

     var k : Map<Integer, Character> = {1-> 'a', 2 -> 'b', 3 -> 'c'}
     x = k[1]
     assertEquals('a', x)
     k[1] = '8'
     assertEquals('8', k[1])

     var l : Map<String, Character> = {"A"-> 'a', "B"-> 'b', "C" -> 'c'}
    // x = l[1]
     x = l["A"]
     assertEquals('a', x)
     l["A"] = '8'
     assertEquals('8', l["A"])

     var m : Map =  {1-> 'a', 2 -> 'b', 3 -> 'c'}
     x = m[1] as Character
     assertEquals('a', x)
     m[1] = '8'
     assertEquals('8', m[1])

     var n : MyClass = new MyClass()
     i = n["A"] as int
     assertEquals(1, i)
     n["A"] = 8
     assertEquals(8, n["A"])
     i = n["B"] as int
     assertEquals(2, i)
     n["B"] = 8
     assertEquals(8, n["B"])
     var ex : boolean = false
     try {i = n["C"] as int}
     catch (e_ : IllegalArgumentException){ ex = true}
     assertTrue(ex)


   }

}