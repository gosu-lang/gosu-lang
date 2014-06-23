package gw.lang.enhancements

uses gw.test.TestClass
uses java.lang.Double
uses java.lang.IllegalStateException
uses java.util.Date
uses java.lang.Integer
uses java.lang.Short
uses java.lang.Long
uses java.lang.Float
uses java.math.BigDecimal
uses java.math.BigInteger
uses java.util.ArrayList
uses java.util.HashSet
uses java.util.HashMap
uses java.lang.ArithmeticException

class CoreCollectionEnhancementTest extends TestClass {

  function testsAllMatch() {
    assertTrue( {1, 2, 3}.allMatch( \ i -> i > 0 ) )
    assertFalse( {1, 2, 3}.allMatch( \ i -> i > 1 ) )
    assertFalse( {1, 2, 3}.allMatch( \ i -> i == 1 ) )
    assertTrue( {"a", "ab", "abc"}.allMatch( \ s -> s.startsWith( "a" ) ) )
    assertFalse( {"a", "ab", "abc"}.allMatch( \ s -> s.startsWith( "b" ) ) )
    assertFalse( {"a", "ab", "abc", "z"}.allMatch( \ s -> s.startsWith( "a" ) ) )
    assertTrue( {}.allMatch( \ i -> true ) )
    assertTrue( {}.allMatch( \ i -> false ) )
  }
  
  function testAverage() {
    assertCausesException( \ -> ({}.average( \ i -> 0 )), ArithmeticException  )
    assertEquals( 1.5 as BigDecimal, {1, 2}.average( \ i -> i ) )
    assertEquals( 2 as BigDecimal, {1, 2, 3}.average( \ i -> i ) )
    assertEquals( 2 as BigDecimal, {1, 2, 3, 1, 2, 3}.average( \ i -> i ) )
    assertEquals( 2 as BigDecimal, {1, 2, 3, 1, 2, 3, 1, 2, 3}.average( \ i -> i ) )
    assertEquals( 0 as BigDecimal, {-1, 1}.average( \ i -> i ) )
    assertEquals( 0 as BigDecimal, {-1, 0, 1}.average( \ i -> i ) )
    assertEquals( 0 as BigDecimal, {-2, -1, 0, 1, 2}.average( \ i -> i ) )
    
    assertCausesException( \ -> ({}.average( \ i -> 0.0 )), ArithmeticException  )
    assertEquals( 1.5 as BigDecimal, {1.0, 2.0}.average( \ i -> i ) )
    assertEquals( 2.0 as BigDecimal, {1.0, 2.0, 3.0}.average( \ i -> i ) )
    assertEquals( 2.0 as BigDecimal, {1.0, 2.0, 3.0, 1.0, 2.0, 3.0}.average( \ i -> i ) )
    assertEquals( 2.0 as BigDecimal, {1.0, 2.0, 3.0, 1.0, 2.0, 3.0, 1.0, 2.0, 3.0}.average( \ i -> i ) )
    assertEquals( 0.0 as BigDecimal, {-1.0, 1.0}.average( \ i -> i ) )
    assertEquals( 0.0 as BigDecimal, {-1.0, 0.0, 1.0}.average( \ i -> i ) )
    assertEquals( 0.0 as BigDecimal, {-2.0, -1.0, 0.0, 1.0, 2.0}.average( \ i -> i ) )
  }
  
  function testConcat() {
    assertEquals( {1, 2, 3}, {1}.concat( {2}.concat( {3} ))) 
    assertEquals( {1}, {1}.concat( {} ) )
  }
    
  function testCountMatches() {
    assertEquals( 0, {1}.countWhere( \ i -> i % 2 == 0 ) )
    assertEquals( 1, {1, 2}.countWhere( \ i -> i % 2 == 0 ) )
    assertEquals( 1, {1, 2, 3}.countWhere( \ i -> i % 2 == 0 ) )
    assertEquals( 2, {1, 2, 3}.countWhere( \ i -> i % 2 == 1 ) )
    assertEquals( 2, {1, 2, 3, 4}.countWhere( \ i -> i % 2 == 1 ) )
    assertEquals( 2, {1, 2, 3, 4}.countWhere( \ i -> i % 2 == 0 ) )
  }
  
  function testDisjunction() {
    assertIterableEqualsIgnoreOrder( {}, {}.disjunction( {} ) )
    assertIterableEqualsIgnoreOrder( {}, {"a"}.disjunction( {"a"} ) )
    assertIterableEqualsIgnoreOrder( {"a", "b"}, {"a"}.disjunction( {"b"} ) )
    assertIterableEqualsIgnoreOrder( {"a"}, {"b", "a"}.disjunction( {"b"} ) )
    assertIterableEqualsIgnoreOrder( {"a", "b"}, {"a"}.disjunction( {"b", "b"} ) )
    assertIterableEqualsIgnoreOrder( {"a", "d"}, {"a", "b", "c"}.disjunction( {"b", "c", "d"} ) )
  }
  
  function testEach() {
    var str = ""
    var lst = {}
    lst.each( \ elt ->{ str += elt } )
    assertEquals( "", str)
    
    lst = {"a"} 
    lst.each( \ elt -> { str += elt } )
    assertEquals( "a", str )
    
    lst = {"a", "b"} 
    lst.each( \ elt -> { str += elt } )
    assertEquals( "aab", str )

    lst = {"a", "b", "c"} 
    lst.each( \ elt -> { str += elt } )
    assertEquals( "aababc", str )
  }
  
  function testEachWithIndex() {
    var str = ""
    var lst = {}
    lst.eachWithIndex( \ elt, i -> { str += elt } )
    assertEquals( "", str)
    
    lst = {"a"} 
    lst.eachWithIndex( \ elt, i -> { str += "${elt}${i}" } )
    assertEquals( "a0", str )
    
    lst = {"a", "b"} 
    lst.eachWithIndex( \ elt, i -> { str += "${elt}${i}" } )
    assertEquals( "a0a0b1", str )

    lst = {"a", "b", "c"} 
    lst.eachWithIndex( \ elt, i -> { str += "${elt}${i}" } )
    assertEquals( "a0a0b1a0b1c2", str )
  }
  
  function testFirst() {
    assertEquals( 1, {1, 2, 3}.first() )
    assertEquals( 2, {2, 3, 1}.first() )
    assertEquals( 3, {3, 1, 2}.first() )
    assertEquals( null, {}.first() )
    assertEquals( null, new HashSet(){}.first() )
  }

  function testFirstWhere() {
    assertEquals( 1, {1, 2, 3}.firstWhere( \ i -> i == 1 ) )
    assertEquals( 2, {1, 2, 3}.firstWhere( \ i -> i == 2 ) )
    assertEquals( 3, {1, 2, 3}.firstWhere( \ i -> i == 3 ) )
    assertEquals( 1, {1, 2, 3}.firstWhere( \ i -> i % 2 == 1 ) )
    assertEquals( null, {1, 2, 3}.firstWhere(\ i -> i == 4) )
    assertEquals( null, {1, 2, 3}.firstWhere(\ i -> i == 0) )
    assertEquals( null, {1, 2, 3}.firstWhere(\ i -> false) )
    assertEquals( null, {}.firstWhere(\ i -> false) )
  }

  function testFlatMap() {
    assertIterableEquals( {}, {}.flatMap( \ s -> new ArrayList() )  )
    assertIterableEquals( {"a"}, {{"a"}}.flatMap( \ s -> s )  )
    assertIterableEquals( {"a", "b"}, {{"a"}, {"b"}}.flatMap( \ s -> s )  )
    assertIterableEquals( {"a", "b", "c"}, {{"a"}, {"b"}, {"c"}}.flatMap( \ s -> s )  )
    assertIterableEquals( {"a", "b", "c"}, {{"a"}, {"b", "c"}}.flatMap( \ s -> s )  )
    assertIterableEquals( {"a", "b", "c"}, {{"a", "b"}, {"c"}}.flatMap( \ s -> s )  )
    assertIterableEquals( {"a", "b", "c"}, {{"a", "b", "c"}}.flatMap( \ s -> s )  )
  }
  
  function testFold() {
    assertEquals( 6, {1, 2, 3}.fold( \ i1, i2 -> i1 + i2 ) )
    assertEquals( null, {}.fold( \ i1, i2 -> i1 ) )
  }
  
  function testHasElements() {
    assertFalse( {}.HasElements )
    assertFalse( (null as List)?.HasElements )
    assertTrue( {1}.HasElements )
    assertTrue( {1, 2}.HasElements)
    assertTrue( {1, 2, 3}.HasElements ) 
  }

  function testHasMatch() {
    assertFalse( {1, 2, 3}.hasMatch( \ i -> i == 0 ) )
    assertTrue( {1, 2, 3}.hasMatch( \ i -> i == 1 ) )
    assertTrue( {1, 2, 3}.hasMatch( \ i -> i == 2 ) )
    assertTrue( {1, 2, 3}.hasMatch( \ i -> i == 3 ) )
    assertFalse( {1, 2, 3}.hasMatch( \ i -> i == 4 ) )
    assertFalse( {}.hasMatch( \ i -> true ) )
    assertFalse( (null as List)?.hasMatch( \ i -> true ) )
  }

  function testIntersect() {
    assertIterableEqualsIgnoreOrder( {}, {}.intersect( {} ) )
    assertIterableEqualsIgnoreOrder( {}, {"a"}.intersect( {} ) )
    assertIterableEqualsIgnoreOrder( {}, {}.intersect( {"a"} ) )
    assertIterableEqualsIgnoreOrder( {}, {"a"}.intersect( {"b"} ) )
    assertIterableEqualsIgnoreOrder( {"a"}, {"a"}.intersect( {"a"} ) )
    assertIterableEqualsIgnoreOrder( {"a"}, {"a", "b"}.intersect( {"a"} ) )
    assertIterableEqualsIgnoreOrder( {"a"}, {"a"}.intersect( {"a", "b"} ) )
    assertIterableEqualsIgnoreOrder( {"b"}, {"b"}.intersect( {"a", "b"} ) )
    assertIterableEqualsIgnoreOrder( {"a", "c"}, {"a", "b", "c"}.intersect( {"a", "c", "d"} ) )
    assertIterableEqualsIgnoreOrder( {"a", "c"}, {"a", "b", "c"}.intersect( {"a", "d", "c"} ) )
    assertIterableEqualsIgnoreOrder( {"a", "c"}, {"a", "b", "c"}.intersect( {"a", "d", "c", "e", "f"} ) )
    assertIterableEqualsIgnoreOrder( {"a", "c"}, {"a", "b", "c", "g", "h"}.intersect( {"a", "d", "c", "e", "f"} ) )
    assertIterableEqualsIgnoreOrder( {"a", "b", "c"}, {"a", "b", "c"}.intersect( {"a", "b", "c"} ) )
  }
  
  function testJoin() {
    assertEquals( "", {}.join(", ") )
    assertEquals( "a", {"a"}.join(", ") )
    assertEquals( "a, b", {"a", "b"}.join(", ") )
    assertEquals( "a, b, c", {"a", "b", "c"}.join(", ") )
  }
  
  function testLast() {
    assertEquals( 3, {1, 2, 3}.last() )
    assertEquals( 1, {2, 3, 1}.last() )
    assertEquals( 2, {3, 1, 2}.last() )
    assertEquals( null, {}.last() )
    assertEquals( null, new HashSet().last() )
  }

  function testLastWhere() {
    assertEquals( 1, {1, 2, 3}.lastWhere( \ i -> i == 1 ) )
    assertEquals( 2, {1, 2, 3}.lastWhere( \ i -> i == 2 ) )
    assertEquals( 3, {1, 2, 3}.lastWhere( \ i -> i == 3 ) )
    assertEquals( 3, {1, 2, 3}.lastWhere( \ i -> i % 2 == 1 ) )
    assertEquals( null, {1, 2, 3}.firstWhere(\ i -> i == 4) )
    assertEquals( null, {1, 2, 3}.firstWhere(\ i -> i == 0) )
    assertEquals( null, {1, 2, 3}.firstWhere(\ i -> false) )
    assertEquals( null, {}.firstWhere(\ i -> i == 4) )
    assertEquals( null, {}.firstWhere(\ i -> i == 0) )
    assertEquals( null, {}.firstWhere(\ i -> false) )
  }

  function testMap() {
    assertIterableEquals( {}, {}.map( \ i -> 0 )  )
    assertIterableEquals( {1}, {"a"}.map( \ s -> s.length )  )
    assertIterableEquals( {1, 1}, {"a", "b"}.map( \ s -> s.length )  )
    assertIterableEquals( {1, 1, 2}, {"a", "b", "bc"}.map( \ s -> s.length )  )
    assertIterableEquals( {1, 2, 1}, {"a", "bc", "c"}.map( \ s -> s.length )  )
    assertIterableEquals( {2, 1, 1}, {"ab", "b", "c"}.map( \ s -> s.length )  )
    assertIterableEquals( {1, 1, 1}, {"ab", "b", "c"}.map( \ s -> 1 )  )
    assertIterableEquals( {2, 2, 2}, {"ab", "b", "c"}.map( \ s -> 2 )  )
    assertIterableEquals( {3, 3, 3}, {"ab", "b", "c"}.map( \ s -> 3 )  )
  }

  function testMax() {
    assertCausesException( \-> ({}.max( \ o -> "a" )), IllegalStateException ) 

    //Integer
    assertEquals( 1, {1}.max( \ i -> i ) )
    assertEquals( 2, {1, 2}.max( \ i -> i ) )
    assertEquals( 2, {2, 1}.max( \ i -> i ) )
    assertEquals( 3, {1, 2, 3}.max( \ i -> i ) )
    assertEquals( 3, {1, 3, 2}.max( \ i -> i ) )
    assertEquals( 3, {2, 1, 3}.max( \ i -> i ) )
    assertEquals( 3, {2, 3, 1}.max( \ i -> i ) )
    assertEquals( 3, {3, 1, 2}.max( \ i -> i ) )
    assertEquals( 3, {3, 2, 1}.max( \ i -> i ) )
    assertEquals( 10, {9, 10}.max( \ i -> i ) )
    
    //Double
    assertEquals( 1.0, {1.0}.max( \ i -> i ) )
    assertEquals( 2.0, {1.0, 2.0}.max( \ i -> i ) )
    assertEquals( 2.0, {2.0, 1.0}.max( \ i -> i ) )
    assertEquals( 3.0, {1.0, 2.0, 3.0}.max( \ i -> i ) )
    assertEquals( 3.0, {1.0, 3.0, 2.0}.max( \ i -> i ) )
    assertEquals( 3.0, {2.0, 1.0, 3.0}.max( \ i -> i ) )
    assertEquals( 3.0, {2.0, 3.0, 1.0}.max( \ i -> i ) )
    assertEquals( 3.0, {3.0, 1.0, 2.0}.max( \ i -> i ) )
    assertEquals( 3.0, {3.0, 2.0, 1.0}.max( \ i -> i ) )
    assertEquals( 10.0, {9.0, 10.0}.max( \ i -> i ) )
    
    //Date
    var date1 = new Date( 1 )
    var date2 = new Date( 2 )
    var date3 = new Date( 3 )
    assertEquals( date1, {date1}.max( \ i -> i ) )
    assertEquals( date2, {date1, date2}.max( \ i -> i ) )
    assertEquals( date2, {date2, date1}.max( \ i -> i ) )
    assertEquals( date3, {date1, date2, date3}.max( \ i -> i ) )
    assertEquals( date3, {date1, date3, date2}.max( \ i -> i ) )
    assertEquals( date3, {date2, date1, date3}.max( \ i -> i ) )
    assertEquals( date3, {date2, date3, date1}.max( \ i -> i ) )
    assertEquals( date3, {date3, date1, date2}.max( \ i -> i ) )
    assertEquals( date3, {date3, date2, date1}.max( \ i -> i ) )
  }

  function testMaxBy() {
    assertEquals( "a", {"a"}.maxBy( \ elt -> elt ) ) 
    assertEquals( "a", {"a", "a"}.maxBy( \ elt -> elt ) ) 
    assertEquals( "b", {"a", "b"}.maxBy( \ elt -> elt ) ) 
    assertEquals( "b", {"b", "a"}.maxBy( \ elt -> elt ) ) 
    assertEquals( "b", {"b", "a", "a"}.maxBy( \ elt -> elt ) ) 
    assertEquals( "c", {"a", "b", "c"}.maxBy( \ elt -> elt ) ) 
    assertEquals( "c", {"a", "c", "b"}.maxBy( \ elt -> elt ) ) 
    assertEquals( "c", {"b", "a", "c"}.maxBy( \ elt -> elt ) ) 
    assertEquals( "c", {"b", "c", "a"}.maxBy( \ elt -> elt ) ) 
    assertEquals( "c", {"c", "b", "a"}.maxBy( \ elt -> elt ) ) 
    assertEquals( "c", {"c", "a", "b"}.maxBy( \ elt -> elt ) ) 
    assertEquals( "c", {"b", "a", "c", "c"}.maxBy( \ elt -> elt ) ) 
    assertEquals( 10, {9, 10}.maxBy( \ i -> i ) )
  }

  function testMin() {
    assertCausesException( \-> ({}.min( \ o -> "a" )), IllegalStateException ) 
    
    //Integer
    assertEquals( 1, {1}.min( \ i -> i ) )
    assertEquals( 1, {1, 2}.min( \ i -> i ) )
    assertEquals( 1, {2, 1}.min( \ i -> i ) )
    assertEquals( 1, {1, 2, 3}.min( \ i -> i ) )
    assertEquals( 1, {1, 3, 2}.min( \ i -> i ) )
    assertEquals( 1, {2, 1, 3}.min( \ i -> i ) )
    assertEquals( 1, {2, 3, 1}.min( \ i -> i ) )
    assertEquals( 1, {3, 1, 2}.min( \ i -> i ) )
    assertEquals( 1, {3, 2, 1}.min( \ i -> i ) )
    assertEquals( 2, {3, 2}.min( \ i -> i ) )
    assertEquals( 9, {9, 10}.min( \ i -> i ) )

    
    //Double
    assertEquals( 1.0, {1.0}.min( \ i -> i ) )
    assertEquals( 1.0, {1.0, 2.0}.min( \ i -> i ) )
    assertEquals( 1.0, {2.0, 1.0}.min( \ i -> i ) )
    assertEquals( 1.0, {1.0, 2.0, 3.0}.min( \ i -> i ) )
    assertEquals( 1.0, {1.0, 3.0, 2.0}.min( \ i -> i ) )
    assertEquals( 1.0, {2.0, 1.0, 3.0}.min( \ i -> i ) )
    assertEquals( 1.0, {2.0, 3.0, 1.0}.min( \ i -> i ) )
    assertEquals( 1.0, {3.0, 1.0, 2.0}.min( \ i -> i ) )
    assertEquals( 1.0, {3.0, 2.0, 1.0}.min( \ i -> i ) )
    assertEquals( 2.0, {3.0, 2.0}.min( \ i -> i ) )
    assertEquals( 9.0, {9.0, 10.0}.min( \ i -> i ) )

    //Date
    var date1 = new Date( 1 )
    var date2 = new Date( 2 )
    var date3 = new Date( 3 )
    assertEquals( date1, {date1}.min( \ i -> i ) )
    assertEquals( date1, {date1, date2}.min( \ i -> i ) )
    assertEquals( date1, {date2, date1}.min( \ i -> i ) )
    assertEquals( date1, {date1, date2, date3}.min( \ i -> i ) )
    assertEquals( date1, {date1, date3, date2}.min( \ i -> i ) )
    assertEquals( date1, {date2, date1, date3}.min( \ i -> i ) )
    assertEquals( date1, {date2, date3, date1}.min( \ i -> i ) )
    assertEquals( date1, {date3, date1, date2}.min( \ i -> i ) )
    assertEquals( date1, {date3, date2, date1}.min( \ i -> i ) )
    assertEquals( date2, {date3, date2}.min( \ i -> i ) )
  }

  function testMinBy() {
    assertEquals( "a", {"a"}.minBy( \ elt -> elt ) ) 
    assertEquals( "a", {"a", "a"}.minBy( \ elt -> elt ) ) 
    assertEquals( "a", {"a", "b"}.minBy( \ elt -> elt ) ) 
    assertEquals( "a", {"b", "a"}.minBy( \ elt -> elt ) ) 
    assertEquals( "a", {"b", "a", "a"}.minBy( \ elt -> elt ) ) 
    assertEquals( "a", {"a", "b", "c"}.minBy( \ elt -> elt ) ) 
    assertEquals( "a", {"a", "c", "b"}.minBy( \ elt -> elt ) ) 
    assertEquals( "a", {"b", "a", "c"}.minBy( \ elt -> elt ) ) 
    assertEquals( "a", {"b", "c", "a"}.minBy( \ elt -> elt ) ) 
    assertEquals( "a", {"c", "b", "a"}.minBy( \ elt -> elt ) ) 
    assertEquals( "a", {"c", "a", "b"}.minBy( \ elt -> elt ) ) 
    assertEquals( "a", {"b", "a", "c", "c"}.minBy( \ elt -> elt ) ) 
    assertEquals( 9, {9, 10}.minBy( \ i -> i ) )
  }
  
  function testOrderBy() {
    assertIterableEquals( {}, {}.orderBy( \ i -> "a" ) )
    assertIterableEquals( {1, 2, 3}, {1, 2, 3}.orderBy( \ i -> i ) )
    assertIterableEquals( {1, 2, 3}, {1, 3, 2}.orderBy( \ i -> i ) )
    assertIterableEquals( {1, 2, 3}, {2, 1, 3}.orderBy( \ i -> i ) )
    assertIterableEquals( {1, 2, 3}, {2, 3, 1}.orderBy( \ i -> i ) )
    assertIterableEquals( {1, 2, 3}, {3, 1, 2}.orderBy( \ i -> i ) )
    assertIterableEquals( {1, 2, 3}, {3, 2, 1}.orderBy( \ i -> i ) )
    assertCausesException( \ -> ({1, 2, 3}.orderBy( \ i -> i ).orderBy( \ i -> i )), IllegalStateException )
    assertCausesException( \ -> ({1, 2, 3}.orderByDescending( \ i -> i ).orderBy( \ i -> i )), IllegalStateException )
  }
  
  function testOrderByDescending() {
    assertIterableEquals( {}, {}.orderBy( \ i -> "a" ) )
    assertIterableEquals( {3, 2, 1}, {1, 2, 3}.orderByDescending( \ i -> i ) )
    assertIterableEquals( {3, 2, 1}, {1, 3, 2}.orderByDescending( \ i -> i ) )
    assertIterableEquals( {3, 2, 1}, {2, 1, 3}.orderByDescending( \ i -> i ) )
    assertIterableEquals( {3, 2, 1}, {2, 3, 1}.orderByDescending( \ i -> i ) )
    assertIterableEquals( {3, 2, 1}, {3, 1, 2}.orderByDescending( \ i -> i ) )
    assertIterableEquals( {3, 2, 1}, {3, 2, 1}.orderByDescending( \ i -> i ) )
    assertCausesException( \ -> ({1, 2, 3}.orderByDescending( \ i -> "a" ).orderByDescending( \ i -> "a" )), IllegalStateException )
    assertCausesException( \ -> ({1, 2, 3}.orderBy( \ i -> "a" ).orderByDescending( \ i -> "a" )), IllegalStateException )
  }

  function testPartitionUniquely() {
    assertEquals( new HashMap(), {}.partitionUniquely( \ k -> true ) )
    assertEquals( new HashMap(){"a" -> "a"} , {"a"}.partitionUniquely( \ elt -> elt ) )
    assertEquals( new HashMap(){"a" -> "a", "b" -> "b"} , {"a", "b"}.partitionUniquely( \ elt -> elt ) )
    assertEquals( new HashMap(){"a" -> "a", "b" -> "b", "c" -> "c"} , {"a", "b", "c"}.partitionUniquely( \ elt -> elt ) )
    assertCausesException( \-> ({"a", "b", "c", "a"}.partitionUniquely( \ elt -> elt )), IllegalStateException )
    var map = {"a", "ab", "abc"}.partitionUniquely( \ elt -> elt.length )
    assertEquals( 3, map.Keys.Count )
    assertEquals( "a", map[1] )
    assertEquals( "ab", map[2] )
    assertEquals( "abc", map[3] )
  }

  function testRemoveWhere() {
    var lst = {"a", "b", "c"}
    lst.removeWhere( \ elt -> true )
    assertEquals( {}, lst )

    lst = {"a", "b", "c"}
    lst.removeWhere( \ elt -> false )
    assertEquals( {"a", "b", "c"}, lst )
    
    lst = {"a", "b", "c"}
    lst.removeWhere( \ elt -> elt == "b" )
    assertEquals( {"a", "c"}, lst )
    
    lst = {"a", "b", "c"}
    lst.removeWhere( \ elt -> elt != "b" )
    assertEquals( {"b"}, lst )
  }
  
  function testRetainWhere() {
    var lst = {"a", "b", "c"}
    lst.retainWhere( \ elt -> false )
    assertEquals( {}, lst )

    lst = {"a", "b", "c"}
    lst.retainWhere( \ elt -> true )
    assertEquals( {"a", "b", "c"}, lst )
    
    lst = {"a", "b", "c"}
    lst.retainWhere( \ elt -> elt == "b" )
    assertEquals( {"b"}, lst )
    
    lst = {"a", "b", "c"}
    lst.retainWhere( \ elt -> elt != "b" )
    assertEquals( {"a", "c"}, lst )
  }
  
  function testReverse() {
    assertIterableEquals( {}, {}.reverse()  )
    assertIterableEquals( {1}, {1}.reverse()  )
    assertIterableEquals( {2, 1}, {1, 2}.reverse()  )
    assertIterableEquals( {3, 2, 1}, {1, 2, 3}.reverse()  )
  }

  function testSingleWhere() {
    assertCausesException( \ -> ({}.singleWhere( \ elt -> true )), IllegalStateException )
    assertCausesException( \ -> ({1}.singleWhere( \ elt -> false )), IllegalStateException )
    assertCausesException( \ -> ({1, 2}.singleWhere( \ elt -> false )), IllegalStateException )
    assertCausesException( \ -> ({1, 2, 3}.singleWhere( \ elt -> false )), IllegalStateException )
    assertEquals( 1, {1}.singleWhere( \ elt -> elt % 2 == 1 ) )
    assertEquals( 1, {1, 2}.singleWhere( \ elt -> elt % 2 == 1 ) )
    assertEquals( 1, {2, 1}.singleWhere( \ elt -> elt % 2 == 1 ) )
    assertEquals( 2, {1, 2}.singleWhere( \ elt -> elt % 2 == 0 ) )
    assertEquals( 2, {2, 1}.singleWhere( \ elt -> elt % 2 == 0 ) )
    assertEquals( 1, {2, 1, 2}.singleWhere( \ elt -> elt % 2 == 1 ) )
    assertEquals( 2, {1, 2, 3}.singleWhere( \ elt -> elt % 2 == 0 ) )
    assertCausesException( \ -> ({1, 2, 3}.singleWhere(\ elt -> elt % 2 == 1)), IllegalStateException )
    assertCausesException( \ -> ({3, 2, 1}.singleWhere(\ elt -> elt % 2 == 1)), IllegalStateException )
    assertCausesException( \ -> ({3, 1, 2}.singleWhere(\ elt -> elt % 2 == 1)), IllegalStateException )    
  }
  
  function testSubtract() {
    assertIterableEqualsIgnoreOrder( {1, 2, 3}, {1, 2, 3}.subtract( {} ) )
    assertIterableEqualsIgnoreOrder( {2, 3}, {1, 2, 3}.subtract( {1} ) )
    assertIterableEqualsIgnoreOrder( {1, 3}, {1, 2, 3}.subtract( {2} ) )
    assertIterableEqualsIgnoreOrder( {1, 2}, {1, 2, 3}.subtract( {3} ) )
    assertIterableEqualsIgnoreOrder( {1}, {1, 2, 3}.subtract( {2, 3} ) )
    assertIterableEqualsIgnoreOrder( {2}, {1, 2, 3}.subtract( {1, 3} ) )
    assertIterableEqualsIgnoreOrder( {3}, {1, 2, 3}.subtract( {1, 2} ) )
    assertIterableEqualsIgnoreOrder( {}, {1, 2, 3}.subtract( {1, 2, 3} ) )
    assertIterableEqualsIgnoreOrder( {1, 2, 3}, {1, 2, 3}.subtract( {4, 5, 6} ) )
    assertIterableEqualsIgnoreOrder( {1, 3}, {1, 2, 3}.subtract( {0, 2, 4} ) )
  }
   
  function testSum() {
    assertEquals( 0, new java.util.ArrayList<Integer>(){}.sum( \ i -> 10 ) )   

    assertEquals( 6, {1, 2, 3}.sum( \ i -> i ) )
    assertEquals( 6, {1, 2, 3}.sum( \ i -> i as short ) )
    assertEquals( 6, {1, 2, 3}.sum( \ i -> i as int ) )
    assertEquals( 6 as long, {1, 2, 3}.sum( \ i -> i as long ) )
    assertEquals( 6 as float, {1, 2, 3}.sum( \ i -> i as float ), 0.01f )
    assertEquals( 6 as double, {1, 2, 3}.sum( \ i -> i as double ), 0.01 )
    assertEquals( 6, {1, 2, 3}.sum( \ i -> i as Short ) )
    assertEquals( 6 as Integer, {1, 2, 3}.sum( \ i -> i ) )
    assertEquals( 6 as Long, {1, 2, 3}.sum( \ i -> i as Long ) )
    assertEquals( 6 as Float, {1, 2, 3}.sum( \ i -> i as Float ) )
    assertEquals( 6 as Double, {1, 2, 3}.sum( \ i -> i as Double ) )
    assertEquals( 6 as BigDecimal, {1, 2, 3}.sum( \ i -> i as BigDecimal ) )
    assertEquals( 6 as BigInteger, {1, 2, 3}.sum( \ i -> i as BigInteger ) )
  }
  
  class SampleClass1 {
    var _firstName : String as FirstName
    var _lastName : String as LastName
    var _age : Integer as Age
    override function toString() : String {
      return "SampleClass( ${FirstName} ${LastName} )" 
    }
  }
  
  function testThenBy() {
    
    var ted = new SampleClass1(){ :FirstName="Ted", :LastName="Smith", :Age=35 }
    var karen = new SampleClass1(){ :FirstName="Karen", :LastName="Smith", :Age=31 }
    var john = new SampleClass1(){ :FirstName="John", :LastName="Jones", :Age=42 }
    var josh = new SampleClass1(){ :FirstName="Josh", :LastName="Zachary", :Age=14 }
    var jill = new SampleClass1(){ :FirstName="Jill", :LastName="Zachary", :Age=14 }
    var johnZ = new SampleClass1(){ :FirstName="John", :LastName="Zachary", :Age=42 }
    
    var people = {ted, karen, john, josh, jill, johnZ}
    
    assertIterableEquals( {jill, josh, karen, ted, john, johnZ}, people.orderBy( \ s -> s.Age ).thenBy( \ s -> s.FirstName ) )
    assertIterableEquals( {josh, jill, karen, ted, john, johnZ}, people.orderBy( \ s -> s.Age ).thenBy( \ s -> s.LastName ) )
    assertIterableEquals( {jill, josh, karen, ted, john, johnZ}, people.orderBy( \ s -> s.Age ).thenBy( \ s -> s.LastName ).thenBy( \ s -> s.FirstName ) )
    assertCausesException( \ -> (people.orderBy( \ s -> s.Age ).thenBy( \ s -> s.FirstName ).orderBy( \ s -> s.Age)), IllegalStateException )
  }
  
  function testThenByDescending() {

    var ted = new SampleClass1(){ :FirstName="Ted", :LastName="Smith", :Age=35 }
    var karen = new SampleClass1(){ :FirstName="Karen", :LastName="Smith", :Age=31 }
    var john = new SampleClass1(){ :FirstName="John", :LastName="Jones", :Age=42 }
    var josh = new SampleClass1(){ :FirstName="Josh", :LastName="Zachary", :Age=14 }
    var jill = new SampleClass1(){ :FirstName="Jill", :LastName="Zachary", :Age=14 }
    var johnZ = new SampleClass1(){ :FirstName="John", :LastName="Zachary", :Age=42 }
    
    var people = {ted, karen, john, josh, jill, johnZ}
    
    assertIterableEquals( {josh, jill, karen, ted, john, johnZ}, people.orderBy( \ s -> s.Age ).thenByDescending( \ s -> s.FirstName ) )
    assertIterableEquals( {josh, jill, karen, ted, johnZ, john}, people.orderBy( \ s -> s.Age ).thenByDescending( \ s -> s.LastName ) )
    assertIterableEquals( {josh, jill, karen, ted, johnZ, john}, people.orderBy( \ s -> s.Age ).thenByDescending( \ s -> s.LastName ).thenByDescending( \ s -> s.FirstName ) )
    assertCausesException( \ -> (people.orderBy( \ s -> s.Age ).thenByDescending( \ s -> s.FirstName ).orderBy( \ s -> s.Age)), IllegalStateException )
  }

  function testToTypedArray() {
    assertArrayEquals( new Integer[]{}, new ArrayList<Integer>(){}.toTypedArray() )
    assertArrayEquals( new Integer[]{1, 2, 3}, new ArrayList<Integer>(){1, 2, 3}.toTypedArray() )
// following is dubious...
//    assertArrayEquals( new Integer[]{}, new ArrayList<int>(){}.toTypedArray() )  
//    assertArrayEquals( new Integer[]{1, 2, 3}, new ArrayList<int>(){1, 2, 3}.toTypedArray() )  
  }

  function testToList() {
    assertEquals( {}, {}.toList() )
    assertEquals( {1}, {1}.toList() )
    assertEquals( {1, 2}, {1, 2}.toList() )
    assertEquals( {1, 2, 3}, {1, 2, 3}.toList() )
  } 
  
  function testToSet() {
    assertEquals( new HashSet(){}, {}.toSet() )
    assertEquals( new HashSet(){1}, {1}.toSet() )
    assertEquals( new HashSet(){1, 2}, {1, 2}.toSet() )
    assertEquals( new HashSet(){1, 2, 3}, {1, 2, 3}.toSet() )
    assertEquals( new HashSet(){1, 2, 3}, {1, 2, 3, 3}.toSet() )
  }

  function testUnion() {
    assertIterableEqualsIgnoreOrder( {}, {}.union( {} ) ) 
    assertIterableEqualsIgnoreOrder( {"a"}, {"a"}.union( {} ) ) 
    assertIterableEqualsIgnoreOrder( {"a"}, {}.union( {"a"} ) ) 
    assertIterableEqualsIgnoreOrder( {"a"}, {"a"}.union( {"a"} ) ) 
    assertIterableEqualsIgnoreOrder( {"a", "b"}, {"a"}.union( {"b"} ) ) 
    assertIterableEqualsIgnoreOrder( {"a", "b"}, {"a", "b"}.union( {"a"} ) ) 
    assertIterableEqualsIgnoreOrder( {"a", "b"}, {"a"}.union( {"a", "b"} ) ) 
    assertIterableEqualsIgnoreOrder( {"a", "b"}, {"a", "b"}.union( {"a", "b"} ) ) 
    assertIterableEqualsIgnoreOrder( {"a", "b", "c"}, {"a", "b", "c"}.union( {"a", "b"} ) ) 
    assertIterableEqualsIgnoreOrder( {"a", "b", "c"}, {"a", "b", "c", "c"}.union( {"a", "b"} ) ) 
    assertIterableEqualsIgnoreOrder( {"a", "b", "c"}, {"a", "b"}.union( {"a", "b", "c", "c"} ) ) 
  }

  function testWhere() {
    assertIterableEqualsIgnoreOrder( {}, {}.where( \ elt -> true ) )
    assertIterableEqualsIgnoreOrder( {}, {}.where( \ elt -> false ) )
    assertIterableEqualsIgnoreOrder( {"a"}, {"a"}.where( \ elt -> true ) )
    assertIterableEqualsIgnoreOrder( {}, {"a"}.where( \ elt -> false ) )
    assertIterableEqualsIgnoreOrder( {1, 3, 5}, {1, 2, 3, 4, 5, 6}.where( \ elt -> elt % 2 == 1 ) )
    assertIterableEqualsIgnoreOrder( {2, 4, 6}, {1, 2, 3, 4, 5, 6}.where( \ elt -> elt % 2 == 0 ) )
  }

  function testEachIsNullSafe() {
    var lst : java.util.List
    lst?.each( \ e -> print( e ) ) // should not cause NPE
  }
}
