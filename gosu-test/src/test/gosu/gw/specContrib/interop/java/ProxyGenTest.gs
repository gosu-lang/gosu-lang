package gw.specContrib.interop.java

uses gw.test.TestClass

class ProxyGenTest extends TestClass {

  interface Foo extends JSubInterface{}
  interface Bar extends JInterface{}

  function testInterfaceGen() {
    var actual = new HashSet<String>()
    Bar.Type.TypeInfo.Methods.each(\e-> actual.add(e+": "+e.Abstract))
    var expected : HashSet<String> = {
        "@IntrinsicType(): false",
        "@Class(): false",
        "equals(java.lang.Object): true",
        "hashCode(): true",
        "toString(): true",
        "notify(): true",
        "notifyAll(): true",
        "wait(long): true",
        "wait(long, int): true",
        "wait(): true",
        "foo(): true"
    }
    assertEquals(expected, actual)
  }
}
