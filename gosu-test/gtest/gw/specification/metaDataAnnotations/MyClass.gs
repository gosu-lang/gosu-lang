package gw.specification.metaDataAnnotations

@MyAnno(:stuff = "class")
class MyClass {

  @MyAnno(:stuff = "field")
  var _x : int

  @MyAnno(:stuff = "constructor")
  construct() {}

  @MyAnno(:stuff = "method")
  function foo() {}

  @MyAnno(:stuff = "property")
  property get Fred() : int { return 5 }
}