package gw.lang.enhancements

uses gw.test.TestClass
uses java.lang.Integer
uses java.math.BigDecimal
uses java.lang.Short
uses java.lang.Long
uses java.lang.Float
uses java.lang.Double
uses java.math.BigInteger
uses java.lang.Character
uses java.lang.Byte
uses java.lang.ArrayStoreException

class CoreArrayEnhancementTest extends TestClass {

  function testToList(){
    assertTrue( new String[]{}.toList().equals( {} ) )
    assertTrue( new String[]{"a"}.toList().equals( {"a"} ) )
    assertTrue( new String[]{"a", "b"}.toList().equals( {"a", "b"} ) )
    assertTrue( new String[]{"a", "b", "c"}.toList().equals( {"a", "b", "c"} ) )
    assertFalse( new String[]{"a"}.toList().equals( {} ) )
    assertFalse( new String[]{}.toList().equals( {"a"} ) )
    assertFalse( new String[]{"a", "b"}.toList().equals( {"b", "a"} ) )
  }

  function testToSet(){
    assertTrue( new String[]{}.toSet().equals( {}.toSet() ) )
    assertTrue( new String[]{"a"}.toSet().equals( {"a"}.toSet() ) )
    assertTrue( new String[]{"a", "b"}.toSet().equals( {"a", "b"}.toSet() ) )
    assertTrue( new String[]{"a", "b", "c"}.toSet().equals( {"a", "b", "c"}.toSet() ) )
    assertFalse( new String[]{"a"}.toSet().equals( {}.toSet() ) )
    assertFalse( new String[]{}.toSet().equals( {"a"}.toSet() ) )
    assertEquals( new String[]{"a", "b"}.toSet(), {"b", "a"}.toSet() )
  }
  
  function testSort() {
    assertArrayEquals( new String[]{}, new String[]{}.sort( \ s1, s2 -> s1 < s2  ) ) 
    assertArrayEquals( new String[]{"a"}, new String[]{"a"}.sort( \ s1, s2 -> s1 < s2  ) ) 
    assertArrayEquals( new String[]{"a", "b"}, new String[]{"a", "b"}.sort( \ s1, s2 -> s1 < s2  ) ) 
    assertArrayEquals( new String[]{"a", "b"}, new String[]{"b", "a"}.sort( \ s1, s2 -> s1 < s2  ) ) 
    assertArrayEquals( new String[]{"a", "b", "c"}, new String[]{"a", "b", "c"}.sort( \ s1, s2 -> s1 < s2  ) ) 
    assertArrayEquals( new String[]{"a", "b", "c"}, new String[]{"a", "c", "b"}.sort( \ s1, s2 -> s1 < s2  ) ) 
    assertArrayEquals( new String[]{"a", "b", "c"}, new String[]{"b", "c", "a"}.sort( \ s1, s2 -> s1 < s2  ) ) 
    assertArrayEquals( new String[]{"a", "b", "c"}, new String[]{"b", "a", "c"}.sort( \ s1, s2 -> s1 < s2  ) ) 
    assertArrayEquals( new String[]{"a", "b", "c"}, new String[]{"c", "b", "a"}.sort( \ s1, s2 -> s1 < s2  ) ) 
    assertArrayEquals( new String[]{"a", "b", "c"}, new String[]{"c", "a", "b"}.sort( \ s1, s2 -> s1 < s2  ) ) 
  }

  function testSortBy() {
    assertArrayEquals( new String[]{}, new String[]{}.sortBy( \ s -> s ) ) 
    assertArrayEquals( new String[]{"a"}, new String[]{"a"}.sortBy( \ s -> s ) ) 
    assertArrayEquals( new String[]{"a", "b"}, new String[]{"a", "b"}.sortBy( \ s -> s ) ) 
    assertArrayEquals( new String[]{"a", "b"}, new String[]{"b", "a"}.sortBy( \ s -> s ) ) 
    assertArrayEquals( new String[]{"a", "b", "c"}, new String[]{"a", "b", "c"}.sortBy( \ s -> s ) ) 
    assertArrayEquals( new String[]{"a", "b", "c"}, new String[]{"a", "c", "b"}.sortBy( \ s -> s ) ) 
    assertArrayEquals( new String[]{"a", "b", "c"}, new String[]{"b", "c", "a"}.sortBy( \ s -> s ) ) 
    assertArrayEquals( new String[]{"a", "b", "c"}, new String[]{"b", "a", "c"}.sortBy( \ s -> s ) ) 
    assertArrayEquals( new String[]{"a", "b", "c"}, new String[]{"c", "b", "a"}.sortBy( \ s -> s ) ) 
    assertArrayEquals( new String[]{"a", "b", "c"}, new String[]{"c", "a", "b"}.sortBy( \ s -> s ) ) 
  }

  function testSortByDescending() {
    assertArrayEquals( new String[]{}, new String[]{}.sortByDescending( \ s -> s ) ) 
    assertArrayEquals( new String[]{"a"}, new String[]{"a"}.sortByDescending( \ s -> s ) ) 
    assertArrayEquals( new String[]{"b", "a"}, new String[]{"a", "b"}.sortByDescending( \ s -> s ) ) 
    assertArrayEquals( new String[]{"b", "a"}, new String[]{"b", "a"}.sortByDescending( \ s -> s ) ) 
    assertArrayEquals( new String[]{"c", "b", "a"}, new String[]{"a", "b", "c"}.sortByDescending( \ s -> s ) ) 
    assertArrayEquals( new String[]{"c", "b", "a"}, new String[]{"a", "c", "b"}.sortByDescending( \ s -> s ) ) 
    assertArrayEquals( new String[]{"c", "b", "a"}, new String[]{"b", "c", "a"}.sortByDescending( \ s -> s ) ) 
    assertArrayEquals( new String[]{"c", "b", "a"}, new String[]{"b", "a", "c"}.sortByDescending( \ s -> s ) ) 
    assertArrayEquals( new String[]{"c", "b", "a"}, new String[]{"c", "b", "a"}.sortByDescending( \ s -> s ) ) 
    assertArrayEquals( new String[]{"c", "b", "a"}, new String[]{"c", "a", "b"}.sortByDescending( \ s -> s ) ) 
  }

  function testJoin() {
    assertEquals( "", new String[]{}.join(",") )
    assertEquals( "a", new String[]{"a"}.join(",") )
    assertEquals( "a,b", new String[]{"a", "b"}.join(",") )
    assertEquals( "a,b,c", new String[]{"a", "b", "c"}.join(",") )
  }
  
  function testFirst() {
    assertEquals( "a", new String[]{"a"}.first() )
    assertEquals( "a", new String[]{"a", "b"}.first() )
    assertEquals( "a", new String[]{"a", "b", "c"}.first() )
    assertEquals( "b", new String[]{"b", "b", "c"}.first() )
  }
  
  function testLast() {
    assertEquals( "a", new String[]{"a"}.last() )
    assertEquals( "b", new String[]{"a", "b"}.last() )
    assertEquals( "c", new String[]{"a", "b", "c"}.last() )
    assertEquals( "c", new String[]{"b", "b", "c"}.last() )
  }
  
  function testContains() {
    assertFalse( new String[]{}.contains( "a" ) )
    assertTrue( new String[]{"a"}.contains( "a" ) )
    assertFalse( new String[]{"a"}.contains( "b" ) )
    assertTrue( new String[]{"a", "b"}.contains( "a" ) )
    assertTrue( new String[]{"a", "b"}.contains( "b" ) )
    assertFalse( new String[]{"a", "b"}.contains( "c" ) )
    assertTrue( new String[]{"a", "b", "c"}.contains( "a" ) )
    assertTrue( new String[]{"a", "b", "c"}.contains( "b" ) )
    assertTrue( new String[]{"a", "b", "c"}.contains( "c" ) )
  }
  
  function testCopy() {
    assertArrayEquals( new String[]{}, new String[]{}.copy() )
    assertArrayEquals( new String[]{"a"}, new String[]{"a"}.copy() )
    assertArrayEquals( new String[]{"a", "b"}, new String[]{"a", "b"}.copy() )
    assertArrayEquals( new String[]{"a", "b", "c"}, new String[]{"a", "b", "c"}.copy() )
  }

  function testFold(){
//    assertEquals( 0, new int[]{}.aggregate( \ i, j -> i + j ) )
    assertEquals( null, new Integer[]{}.fold( \ i, j -> i + j ) )
    assertEquals( 6, new Integer[]{1, 2, 3}.fold( \ i, j -> i + j ) ) 
  }
  
  function testCount() {
    assertEquals( 0, new Integer[]{}.Count )
    assertEquals( 1, new Integer[]{1}.Count )
    assertEquals( 2, new Integer[]{1, 2}.Count )
    assertEquals( 3, new Integer[]{1, 2, 3}.Count )
    assertEquals( 0, new int[]{}.Count )
    assertEquals( 1, new int[]{1}.Count )
    assertEquals( 2, new int[]{1, 2}.Count )
    assertEquals( 3, new int[]{1, 2, 3}.Count )
  }

  function testSum() {
    assertEquals( 0, new int[]{}.sum() )
    assertEquals( 0, new short[]{}.sum() )
    assertEquals( 0, new int[]{}.sum() )
    assertEquals( 0 as long, new long[]{}.sum() )
    assertEquals( 0 as float, new float[]{}.sum(), 0.01f )
    assertEquals( 0 as double, new double[]{}.sum(), 0.01 )
    assertEquals( 0, new Short[]{}.sum( \ i -> i as Short ) )
    assertEquals( 0 as Integer, new Integer[]{}.sum( \ i -> i as Integer ) )
    assertEquals( 0 as Long, new Long[]{}.sum( \ i -> i as Long ) )
    assertEquals( 0 as Float, new Float[]{}.sum( \ i -> i as Float ) )
    assertEquals( 0 as Double, new Double[]{}.sum( \ i -> i as Double ) )
    assertEquals( 0 as BigDecimal, new BigDecimal[]{}.sum( \ i -> i as BigDecimal ) )
    assertEquals( 0 as BigInteger, new BigInteger[]{}.sum( \ i -> i as BigInteger ) )
    
    assertEquals( 6 as Integer, new Integer[]{1, 2, 3}.sum( \ i -> i ) )
    assertEquals( 6, new short[]{1, 2, 3}.sum() )
    assertEquals( 6, new int[]{1, 2, 3}.sum() )
    assertEquals( 6 as long, new long[]{1, 2, 3}.sum() )
    assertEquals( 6 as float, new float[]{1, 2, 3}.sum(), 0.01f )
    assertEquals( 6 as double, new double[]{1, 2, 3}.sum(), 0.01 )
    assertEquals( 6, new Short[]{1, 2, 3}.sum( \ i -> i as Short ) )
    assertEquals( 6 as Integer, new Integer[]{1, 2, 3}.sum( \ i -> i as Integer ) )
    assertEquals( 6 as Long, new Long[]{1, 2, 3}.sum( \ i -> i as Long ) )
    assertEquals( 6 as Float, new Float[]{1, 2, 3}.sum( \ i -> i as Float ) )
    assertEquals( 6 as Double, new Double[]{1, 2, 3}.sum( \ i -> i as Double ) )
    assertEquals( 6 as BigDecimal, new BigDecimal[]{1, 2, 3}.sum( \ i -> i as BigDecimal ) )
    assertEquals( 6 as BigInteger, new BigInteger[]{1, 2, 3}.sum( \ i -> i as BigInteger ) )
  }
  
  function testArraysHaveAllMethodsAndPropertiesThatListsHave() {
    var iterableMethods = CoreIterableEnhancement.Type.TypeInfo.Methods.concat( 
                          CoreListEnhancement.Type.TypeInfo.Methods )
          
    var methodExceptions = {"toTypedArray", "toCollection", "retainWhere", 
                            "removeWhere", "shuffle", "freeze", "cast"}
    for( method in iterableMethods ) {
      if( not methodExceptions.contains( method.DisplayName ) ) {
        assertNotNull( "Couldn't find method ${method.DisplayName}", CoreArrayEnhancement.Type.TypeInfo.Methods.firstWhere( \ m -> m.DisplayName == method.DisplayName ) )
      }
    }
    
    var iterableProps = CoreIterableEnhancement.Type.TypeInfo.Properties.concat( 
                          CoreListEnhancement.Type.TypeInfo.Properties )
    for( prop in iterableProps ) {
      assertNotNull( "Couldn't find property ${prop.Name}", CoreArrayEnhancement.Type.TypeInfo.getProperty( prop.Name ) )
    }
  }

  function testPrimitiveArraysCoerceToProperBoxedTypesAsLists() {
     assertEquals( List<Boolean>, statictypeof new boolean[0].toList() )
     assertEquals( List<Byte>, statictypeof new byte[0].toList() )
     assertEquals( List<Character>, statictypeof new char[0].toList() )
     assertEquals( List<Short>, statictypeof new short[0].toList() )
     assertEquals( List<Integer>, statictypeof new int[0].toList() )
     assertEquals( List<Long>, statictypeof new long[0].toList() )
     assertEquals( List<Float>, statictypeof new float[0].toList() )
     assertEquals( List<Double>, statictypeof new double[0].toList() )
  }

  function testCastFromJavaTypeToJavaType() {
    var x = new Object[]{"a", "b"}
    var y = x.cast(String)
    assertFalse(x === y)
    assertEquals(String[], typeof(y))
    assertEquals(2, y.length)
    assertEquals("a", y[0])
    assertEquals("b", y[1])  
  }
  
  function testIllegalCastFromJavaTypeToJavaType() {
    var x = new Object[]{"a", "b", 5}
    try {
      var y = x.cast(String)
      fail()
    } catch (e : ArrayStoreException) {
      // Expected
    }
  }
  
  function testCastFromJavaTypeToGosuType() {
    var x = new Object[]{new TestClass("a"), new TestClass("b")}
    var y = x.cast(TestClass)
    assertFalse(x === y)
    assertEquals(TestClass[], typeof(y))
    assertEquals(2, y.length)
    assertEquals("a", y[0].Value)
    assertEquals("b", y[1].Value)      
  }
  
  function testIllegalCastFromJavaTypeToGosuType() {
    var x = new Object[]{new TestClass("a"), new TestClass("b"), "c"}
    try {
      var y = x.cast(TestClass)
      fail()
    } catch (e : ArrayStoreException) {
      // Expected
    }  
  }
  
  static class TestClass {
    var _value : String as Value
    
    construct(valueArg : String) {
      _value = valueArg  
    }
  }

}
