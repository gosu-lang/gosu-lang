package gw.internal.gosu.compiler.sample.statement.classes
uses java.lang.StringBuffer

class HasGenericFunction
{
  reified function button<E>( id: String, targetPage: E ) : GClass<E>
  {
    // Note the interesting part of the test is that we parameterized the ctor for GClass with the type var from this generic function
    return new GClass<E>( id, targetPage )
  }

  function testMe() : GClass<StringBuffer>
  {
    return button( "hello", new StringBuffer( "sb" ) )
  }
}
