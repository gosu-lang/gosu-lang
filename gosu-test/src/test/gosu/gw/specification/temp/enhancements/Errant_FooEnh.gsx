package gw.specification.temp.enhancements

enhancement Errant_FooEnh : Foo {
  property set Me( s: String ) { }  //## issuekeys: MSG_PROPERTY_AND_FUNCTION_CONFLICT
  property get Me() : String {  //## issuekeys: MSG_PROPERTY_AND_FUNCTION_CONFLICT
    return null
  }

  function getYou() : String { return null }  //## issuekeys: MSG_PROPERTY_AND_FUNCTION_CONFLICT

  static function doit( f: Foo ) : String { return null }
  function doit() : String { return null }  //## issuekeys: MSG_CANNOT_OVERRIDE_FUNCTION_FROM_ENHANCEMENT
}