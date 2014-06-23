package gw.internal.gosu.compiler.sample.statement.classes

class SubclassOfGenericBase extends GenericBase<String>
{
  var _x : String = "asfd"

  // Tests that the default ctor invokes super() with the String type as a hidden arg

  function accessPropertyIdentifier() : String
  {
    Foo = "hello"       // Tests that the assignment statement uses the generic version of the property setter i.e., setFoo( Object ) not setFoo( String )
    Foo.substring( 0 )  // Tests that the bean member assignment statement uses the generic version of Foo's getter and does a checkcast before calling substring()
    return Foo          // Tests that the identifier expression uses the generic version of the Foo's getter with a checkcast
  }
}
