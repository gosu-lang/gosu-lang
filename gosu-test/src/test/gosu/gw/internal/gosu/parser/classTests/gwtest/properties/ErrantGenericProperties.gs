package gw.internal.gosu.parser.classTests.gwtest.properties
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class ErrantGenericProperties {

  var _field : String as Field<T>

  property get Foo<T>() : String { return null }    

  property set Foo<T>(s : String ) {}    
  
}
