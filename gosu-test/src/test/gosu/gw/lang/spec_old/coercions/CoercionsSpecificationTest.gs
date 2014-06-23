package gw.lang.spec_old.coercions

uses gw.test.TestClass
uses java.lang.Integer
uses java.lang.Runnable
uses java.lang.ClassCastException
uses gw.lang.reflect.gs.IGosuObject
uses java.lang.Comparable
uses java.lang.Short
uses java.lang.Long
uses java.lang.Float
uses java.lang.Double
uses gw.lang.parser.resources.Res
uses java.util.concurrent.Callable

class CoercionsSpecificationTest extends TestClass
{
  
  function testExistingIGosuObjectFunctionality() {
    // This is how these things currently work, not saying it's right, but this test should ensure things don't change inadvertently
    var obj = CoercionsSpecificationTestGosuObject.INSTANCE
    assertEquals( "gw.lang.spec_old.coercions.CoercionsSpecificationTestGosuObject", obj.Class.Name )
    assertTrue( Runnable.Type.isAssignableFrom( CoercionsSpecificationTestGosuObject ) )
    assertTrue( IGosuObject.Type.isAssignableFrom( CoercionsSpecificationTestGosuObject ) )
  }

  function testAllPrimitiveTypesAndStringAreCoercableToOneAnother() {
    print( "Testing numbers" )
    //numeric coercions
    for(i in -64..64 ) {
      //byte
      assertEquals( i as byte, i as byte as byte ) 
      assertEquals( i as byte, i as short as byte ) 
      assertEquals( i as byte, i as int as byte ) 
      assertEquals( i as byte, i as long as byte ) 
      assertEquals( i as byte, i as float as byte ) 
      assertEquals( i as byte, i as double as byte ) 

      //short
      assertEquals( i as short, i as byte as short ) 
      assertEquals( i as short, i as short as short ) 
      assertEquals( i as short, i as int as short ) 
      assertEquals( i as short, i as long as short ) 
      assertEquals( i as short, i as float as short ) 
      assertEquals( i as short, i as double as short ) 

      //int
      assertEquals( i as int, i as byte as int ) 
      assertEquals( i as int, i as short as int ) 
      assertEquals( i as int, i as int as int ) 
      assertEquals( i as int, i as long as int ) 
      assertEquals( i as int, i as float as int ) 
      assertEquals( i as int, i as double as int ) 

      //long
      assertEquals( i as long, i as byte as long ) 
      assertEquals( i as long, i as short as long ) 
      assertEquals( i as long, i as int as long ) 
      assertEquals( i as long, i as long as long ) 
      assertEquals( i as long, i as float as long ) 
      assertEquals( i as long, i as double as long ) 

      //float
      assertEquals( i as float, i as byte as float, 0.01f )
      assertEquals( i as float, i as short as float, 0.01f )
      assertEquals( i as float, i as int as float, 0.01f )
      assertEquals( i as float, i as long as float, 0.01f )
      assertEquals( i as float, i as float as float, 0.01f )
      assertEquals( i as float, i as double as float, 0.01f )

      //double
      assertEquals( i as double, i as byte as double, 0.01 )
      assertEquals( i as double, i as short as double, 0.01 )
      assertEquals( i as double, i as int as double, 0.01 )
      assertEquals( i as double, i as long as double, 0.01 )
      assertEquals( i as double, i as float as double, 0.01 )
      assertEquals( i as double, i as double as double, 0.01 )
    }
    
    print( "Testing boolean from" )
    //boolean from
    assertFalse( 0 as byte as boolean ) 
    assertFalse( 0 as short as boolean ) 
    assertFalse( 0 as short as int as boolean ) 
    assertFalse( 0 as long as boolean ) 
    assertFalse( 0 as float as boolean ) 
    assertFalse( 0 as double as boolean ) 
    assertTrue( 1 as byte as boolean )
    assertTrue( 1 as short as boolean ) 
    assertTrue( 1 as short as int as boolean ) 
    assertTrue( 1 as long as boolean ) 
    assertTrue( 1 as float as boolean ) 
    assertTrue( 1 as double as boolean ) 
    
    print( "Testing boolean to" )
    //boolean to
    assertEquals( 0 as byte, false as byte )    
    assertEquals( 0 as short, false as short )    
    assertEquals( 0 as short as int, false as int )    
    assertEquals( 0 as long, false as long )    
    assertEquals( 0 as float, false as float, 0.01f )
    assertEquals( 0 as double, false as double, 0.01 )
    assertEquals( "false", false as String )
    assertEquals( 1 as byte, true as byte )    
    assertEquals( 1 as short, true as short )    
    assertEquals( 1 as short as int, true as int )    
    assertEquals( 1 as long, true as long )    
    assertEquals( 1 as float, true as float, 0.01f )
    assertEquals( 1 as double, true as double, 0.01 )
    assertEquals( "true", true as String )    

    //TODO - char  
  }
  
  function testPrimitiveToComparableCoercion() {

    //positive cases
    var comp : Comparable
    comp = true as java.lang.Comparable<java.lang.Object>
    assertEquals( comp, true )
    comp = 1 as short as java.lang.Comparable<java.lang.Object>
    assertEquals( 1 as Short, comp ) 
    comp = 1 as long as int as java.lang.Comparable<java.lang.Object>
    assertEquals( 1 as Integer, comp ) 
    comp = 1 as long as java.lang.Comparable<java.lang.Object> 
    assertEquals( 1 as Long, comp ) 
    comp = .1 as float as java.lang.Comparable<java.lang.Object> 
    assertEquals( .1 as Float, comp ) 
    comp = .1 as float as double as java.lang.Comparable<java.lang.Object> 
    assertEquals( 0.10000000149011612 as Double, comp ) // Conversion from float->double is lossy in this case
   
    //negative cases
    assertFalse( Errant_BadCoercions.Type.Valid )
    var parseIssues = Errant_BadCoercions.Type.ParseResultsException.ParseIssues
    for( i in 10..15 ) {
      var match = parseIssues.hasMatch( \ is -> is.Line == i and
                                                is.MessageKey == Res.MSG_TYPE_MISMATCH )
      assertTrue( "Expected to find a type mismatch on line ${i} in Errant_BadCoercions", match )
    }
  }
  
  function testBlockToInterfaceCoercion() {
    var tmp = this.callIt( \-> "hello" )
    assertEquals( String, statictypeof tmp )
    assertEquals( "hello", tmp )
    
    var tmp2 = callIt( \-> "hello" ) 
    assertEquals( String, statictypeof tmp2 )
    assertEquals( "hello", tmp2 )
  }

//todo: uncomment after push
//  function testErrant_FinalClassToInterface() {
//    assertFalse( Errant_FinalClassToInterface.Type.Valid )
//    var errors = Errant_FinalClassToInterface.Type.ParseResultsException.ParseExceptions
//    assertEquals( 2, errors.Count )
//    assertEquals( Res.MSG_TYPE_MISMATCH, errors[0].MessageKey )
//  }
  
  function callIt<T>( c : Callable<T> ) : T {
    return c.call() 
  }
}
