package gw.lang.spec_old.generics
uses gw.lang.parser.resources.Res

class RecursiveGenericTypeTest extends gw.test.TestClass
{
  static class RecursiveType<T extends RecursiveType<T>>
  {
    var _t : T as Whatever
    
    function testRecursiveTypeIsAssignableToDefaultParameterizedType() : T
    {
      var defType : RecursiveType<RecursiveType>
      defType = this
      return defType as T
    }
  }
  
  static class SubClass extends RecursiveType<SubClass>
  {
  }
  
  function testRecursiveTypeIsAssignableToDefaultParameterizedType()
  {
    assertNotNull( new SubClass() )
  }

  function testErrant_RecursiveTypeRequiresArgs()
  {
    assertFalse( Errant_RecursiveTypeRequiresArgs.Bar.Type.Valid )
    var errors = Errant_RecursiveTypeRequiresArgs.Bar.Type.ParseResultsException.ParseExceptions
    assertEquals( 1, errors.Count )
    assertEquals( Res.MSG_CANNOT_EXTEND_RAW_GENERIC_TYPE, errors.get( 0 ).MessageKey )
  }

  function testEnhancementOnRecursiveSubType()
  {
    var x = new ConcreteSubOfSubOfRecursiveType2()
    var y = x.enhMethod( "hello", x )
    assertSame( x, y )
  }
  
  function testIndirectRecursiveGenericType()
  {
    var c1 = new IndirectRecursiveGenericType<String>( "ABC" ) 
    var c2 = new IndirectRecursiveGenericType<String>( "DEF" ) 
    assertEquals( -3, c1.compareTo( c2 ) )
  }
}
