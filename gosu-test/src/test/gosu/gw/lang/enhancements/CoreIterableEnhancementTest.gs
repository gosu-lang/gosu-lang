package gw.lang.enhancements

uses gw.test.TestClass
uses gw.util.Pair
uses java.lang.Iterable
uses java.lang.Integer
uses java.lang.IllegalStateException
uses java.util.ArrayList
uses java.util.LinkedHashSet

class CoreIterableEnhancementTest extends TestClass {  
  
  function testCount() {
    assertEquals( 0, {}.Count )
    assertEquals( 1, {1}.Count )
    assertEquals( 2, {1, 2}.Count )
    assertEquals( 3, {1, 2, 3}.Count )
    assertEquals( 0, ({} as Iterable).Count )
    assertEquals( 1, ({1} as Iterable).Count )
    assertEquals( 2, ({1, 2} as Iterable).Count )
    assertEquals( 3, ({1, 2, 3} as Iterable).Count )
  }
 
  function testToCollection() {
    var x = {1, 2, 3}.toCollection()
    for( num in x index i ) {
      assertEquals( num, i + 1 )
    }
  }
  
  function testSingle() {
    assertCausesException( \ -> ({}.single()), IllegalStateException )
    assertEquals( 1, {1}.single() )
    assertCausesException( \ -> ({1, 2}.single()), IllegalStateException )
  }
  
  function testReverseDoesNotUpdateOriginalList() {
    var x = new ArrayList(){1, 2, 3}
    assertEquals({3, 2, 1}, x.reverse())  
    assertEquals({1, 2, 3}, x)  

    var y = new LinkedHashSet(){1, 2, 3}
    assertEquals({3, 2, 1}, y.reverse())  
    assertEquals({1, 2, 3}, y.toList())  
  }


  function testMinMaxWithNull() {
    var x : List<Integer> = {1, 2, 3, null}

    assertEquals( x.max( \ e -> e ), 3 )
    assertEquals( x.min( \ e -> e ), 1 )

    x = {null, 1, 2, 3}

    assertEquals( x.max( \ e -> e ), 3 )
    assertEquals( x.min( \ e -> e ), 1 )

    x = {null, 1, 2, null, 3, null}

    assertEquals( x.max( \ e -> e ), 3 )
    assertEquals( x.min( \ e -> e ), 1 )

    x = {null, null, null}

    var hasThrown = false
    try
    {
      x.max( \e -> e )
    }
    catch( ex : IllegalStateException )
    {
      hasThrown = true
    }
    assertTrue( hasThrown )

    hasThrown = false
    try
    {
      x.min( \ e -> e )
    }
    catch( ex : IllegalStateException )
    {
      hasThrown = true
    }

    assertTrue( hasThrown )

  }

  function testZipShouldReturnEmptyListWhenThatIterableEmpty() {
    // given
    var thisIterable : Iterable<Integer> = new ArrayList<Integer>(){1, 2, 3}
    var thatIterable : Iterable<String> = new ArrayList<String>()

    // when
    var result : List<Pair<Integer, String>> = thisIterable.zip(thatIterable);

    // then
    assertNotNull(result)
    assertEquals(0, result.Count)
  }

  function testZipShouldReturnEmptyListWhenThisIterableEmpty() {
    // given
    var thisIterable : Iterable<Integer> = new ArrayList<Integer>()
    var thatIterable : Iterable<String> = new ArrayList<String>(){"a", "b", "c"}

    // when
    var result : List<Pair<Integer, String>> = thisIterable.zip(thatIterable);

    // then
    assertNotNull(result)
    assertEquals(0, result.Count)
  }

  function testZipShouldReturnNullPointerWhenThatIterableIsNull() {
    // given
    var thisIterable : Iterable<Integer> = new ArrayList<Integer>(){1, 2, 3}
    var thatIterable : Iterable<String> = null

    // when
    var hasThrown = false
    try {
     thisIterable.zip(thatIterable);
    } catch (e : NullPointerException) {
      hasThrown = true
    }

    // then
    assertTrue(hasThrown)
  }

  function testZipShouldCreateIterableOfPairsWithAllElementsWhenBothIterablesOfEqualLength() {
    // given
    var thisIterable : Iterable<String> =
        new ArrayList<String>(){"foo", "bar", "baz", "foobar", "barfoo", "barbaz", "bazbar"}
    var thatIterable : Iterable<Integer> = 1..thisIterable.Count

    // when
    var zippedList = thisIterable.zip(thatIterable)

    // then
    assertEquals(thisIterable.Count, zippedList.Count)

    assertEquals("foo", zippedList.get(0).First)
    assertEquals("bar", zippedList.get(1).First)
    assertEquals("baz", zippedList.get(2).First)
    assertEquals("foobar", zippedList.get(3).First)
    assertEquals("barfoo", zippedList.get(4).First)
    assertEquals("barbaz", zippedList.get(5).First)
    assertEquals("bazbar", zippedList.get(6).First)

    assertEquals(1, zippedList.get(0).Second)
    assertEquals(2, zippedList.get(1).Second)
    assertEquals(3, zippedList.get(2).Second)
    assertEquals(4, zippedList.get(3).Second)
    assertEquals(5, zippedList.get(4).Second)
    assertEquals(6, zippedList.get(5).Second)
    assertEquals(7, zippedList.get(6).Second)
  }

  function testZipShouldCreateIterableOfPairsTheLengthOfThisIterableWhereIsShorter() {
    // given
    var thisIterable : Iterable<String> =
        new ArrayList<String>(){"foo", "bar", "baz"}
    var thatIterable : Iterable<Integer> = 1..100

    // when
    var zippedList = thisIterable.zip(thatIterable)

    // then
    assertEquals(thisIterable.Count, zippedList.Count)

    assertEquals("foo", zippedList.get(0).First)
    assertEquals("bar", zippedList.get(1).First)
    assertEquals("baz", zippedList.get(2).First)

    assertEquals(1, zippedList.get(0).Second)
    assertEquals(2, zippedList.get(1).Second)
    assertEquals(3, zippedList.get(2).Second)
  }

  function testZipShouldCreateIterableOfPairsTheLengthOfThatIterableWhereIsShorter() {
    // given
    var thisIterable : Iterable<String> =
        new ArrayList<String>(){"foo", "bar", "baz", "foobar", "barfoo", "barbaz", "bazbar"}
    var thatIterable : Iterable<Integer> = 1..3

    // when
    var zippedList = thisIterable.zip(thatIterable)

    // then
    assertEquals(thatIterable.Count, zippedList.Count)

    assertEquals("foo", zippedList.get(0).First)
    assertEquals("bar", zippedList.get(1).First)
    assertEquals("baz", zippedList.get(2).First)

    assertEquals(1, zippedList.get(0).Second)
    assertEquals(2, zippedList.get(1).Second)
    assertEquals(3, zippedList.get(2).Second)
  }

}
