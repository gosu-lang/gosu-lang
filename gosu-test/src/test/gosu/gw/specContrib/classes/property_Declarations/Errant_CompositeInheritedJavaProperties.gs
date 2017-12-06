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

  static class GosuDerived extends Base {
    static function testMe() {
      getFoo()  //## issuekeys: MSG_NO_SUCH_FUNCTION
      setFoo( 8 )
      GosuDerived.setFoo( 8 )

      var v = getBar()
      setBar( 8 )

      var w = getBop()
      setBop()  //## issuekeys: MSG_NO_SUCH_FUNCTION
    }

    function testStaticSetterNonstaticGetter() {
      print( Thing )
      Thing = "nope"  //## issuekeys: MSG_PROPERTY_NOT_WRITABLE
      Base.setThing( "yep" )
      Base.Thing = "nope" //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND, MSG_NO_PROPERTY_DESCRIPTOR_FOUND
    }
  }
}