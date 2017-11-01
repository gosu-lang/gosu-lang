package gw.specContrib.classes.property_Declarations

class Errant_CompositeInheritedJavaProperties {
  function testMe() {
    var x: IDerivedC;
    print( x.Foo );
    x.Foo = "hi"
  }
}