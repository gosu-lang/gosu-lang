package gw.specification.temp.enhancements

enhancement Errant_FooEnh : Foo {
  property set Me( s: String ) { }  //## issuekeys: MSG_PROPERTY_AND_FUNCTION_CONFLICT
  property get Me() : String {  //## issuekeys: MSG_PROPERTY_AND_FUNCTION_CONFLICT
    return null
  }

  function getYou() : String { return null }  //## issuekeys: MSG_PROPERTY_AND_FUNCTION_CONFLICT
}