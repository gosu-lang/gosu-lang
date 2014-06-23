package gw.lang.enhancements
uses gw.test.TestClass
uses java.lang.IllegalStateException

class CoreArrayOfComparablesEnhancementTest extends TestClass
{
  function testMin() {
    assertCausesException( \-> new String[]{}.min(), IllegalStateException ) 
    assertEquals( "a", new String[]{"a"}.min() ) 
    assertEquals( "a", new String[]{"a", "a"}.min() ) 
    assertEquals( "a", new String[]{"a", "b"}.min() ) 
    assertEquals( "a", new String[]{"b", "a"}.min() ) 
    assertEquals( "a", new String[]{"b", "a", "a"}.min() ) 
    assertEquals( "a", new String[]{"a", "b", "c"}.min() ) 
    assertEquals( "a", new String[]{"a", "c", "b"}.min() ) 
    assertEquals( "a", new String[]{"b", "a", "c"}.min() ) 
    assertEquals( "a", new String[]{"b", "c", "a"}.min() ) 
    assertEquals( "a", new String[]{"c", "b", "a"}.min() ) 
    assertEquals( "a", new String[]{"c", "a", "b"}.min() ) 
    assertEquals( "a", new String[]{"b", "a", "c", "c"}.min() ) 
  }
  
  function testMax() {
    assertCausesException( \-> new String[]{}.max(), IllegalStateException ) 
    assertEquals( "a", new String[]{"a"}.max() ) 
    assertEquals( "a", new String[]{"a", "a"}.max() ) 
    assertEquals( "b", new String[]{"a", "b"}.max() ) 
    assertEquals( "b", new String[]{"b", "a"}.max() ) 
    assertEquals( "b", new String[]{"b", "a", "a"}.max() ) 
    assertEquals( "c", new String[]{"a", "b", "c"}.max() ) 
    assertEquals( "c", new String[]{"a", "c", "b"}.max() ) 
    assertEquals( "c", new String[]{"b", "a", "c"}.max() ) 
    assertEquals( "c", new String[]{"b", "c", "a"}.max() ) 
    assertEquals( "c", new String[]{"c", "b", "a"}.max() ) 
    assertEquals( "c", new String[]{"c", "a", "b"}.max() ) 
    assertEquals( "c", new String[]{"b", "a", "c", "c"}.max() ) 
  }
  
  function testSort() {
    assertArrayEquals( {}, new String[]{}.sort() ) 
    assertArrayEquals( {"a"}, new String[]{"a"}.sort() ) 
    assertArrayEquals( {"a", "a"}, new String[]{"a", "a"}.sort() ) 
    assertArrayEquals( {"a", "b"}, new String[]{"a", "b"}.sort() ) 
    assertArrayEquals( {"a", "b"}, new String[]{"b", "a"}.sort() ) 
    assertArrayEquals( {"a", "b", "c"}, new String[]{"a", "b", "c"}.sort() ) 
    assertArrayEquals( {"a", "b", "c"}, new String[]{"a", "c", "b"}.sort() ) 
    assertArrayEquals( {"a", "b", "c"}, new String[]{"b", "a", "c"}.sort() ) 
    assertArrayEquals( {"a", "b", "c"}, new String[]{"b", "c", "a"}.sort() ) 
    assertArrayEquals( {"a", "b", "c"}, new String[]{"c", "a", "b"}.sort() ) 
    assertArrayEquals( {"a", "b", "c"}, new String[]{"c", "b", "a"}.sort() ) 
  }
  
  function testSortDescending() {
    assertArrayEquals( {}, new String[]{}.sortDescending() ) 
    assertArrayEquals( {"a"}, new String[]{"a"}.sortDescending() ) 
    assertArrayEquals( {"a", "a"}, new String[]{"a", "a"}.sortDescending() ) 
    assertArrayEquals( {"b", "a"}, new String[]{"a", "b"}.sortDescending() ) 
    assertArrayEquals( {"b", "a"}, new String[]{"b", "a"}.sortDescending() ) 
    assertArrayEquals( {"c", "b", "a"}, new String[]{"a", "b", "c"}.sortDescending() ) 
    assertArrayEquals( {"c", "b", "a"}, new String[]{"a", "c", "b"}.sortDescending() ) 
    assertArrayEquals( {"c", "b", "a"}, new String[]{"b", "a", "c"}.sortDescending() ) 
    assertArrayEquals( {"c", "b", "a"}, new String[]{"b", "c", "a"}.sortDescending() ) 
    assertArrayEquals( {"c", "b", "a"}, new String[]{"c", "a", "b"}.sortDescending() ) 
    assertArrayEquals( {"c", "b", "a"}, new String[]{"c", "b", "a"}.sortDescending() ) 
  }

}
