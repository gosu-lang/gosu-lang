package gw.lang.enhancements

uses gw.test.TestClass
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
  
}