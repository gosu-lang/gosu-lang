package gw.lang.enhancements

uses gw.test.TestClass
uses java.util.HashMap

class CoreListEnhancementTest extends TestClass {
  function testPartition() {
    assertEquals( new HashMap(), {}.partition( \ k -> true ) )
    assertEquals( new HashMap(){"a" -> {"a"}} , {"a"}.partition( \ elt -> elt ) )
    assertEquals( new HashMap(){"a" -> {"a"}, "b" -> {"b"}} , {"a", "b"}.partition( \ elt -> elt ) )
    assertEquals( new HashMap(){"a" -> {"a"}, "b" -> {"b"}, "c" -> {"c"}} , {"a", "b", "c"}.partition( \ elt -> elt ) )
    assertEquals( new HashMap(){"a" -> {"a", "a"}, "b" -> {"b"}, "c" -> {"c"}} , {"a", "b", "c", "a"}.partition( \ elt -> elt ) )
    var map = {"a", "b", "c", "a"}.partition( \ elt -> elt.length )
    assertEquals( 1, map.Keys.Count )
    assertIterableEqualsIgnoreOrder( {"a", "b", "c", "a"}, map[1] )
  }

}
