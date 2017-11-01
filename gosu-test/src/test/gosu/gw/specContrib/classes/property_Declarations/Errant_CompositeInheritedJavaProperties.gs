package gw.specContrib.classes.property_Declarations

class Errant_CompositeInheritedJavaProperties {
  function testDiscreteHierarchy() {
    var x: IDerivedC;
    print( x.Foo );
    x.Foo = "hi"
  }

  function testHierarchyCrosses() {
    var x: IDerivedD;
    print( x.Foo );
    x.Foo = "hi"
  }
}