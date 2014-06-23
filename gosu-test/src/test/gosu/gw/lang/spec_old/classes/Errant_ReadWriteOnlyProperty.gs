package gw.lang.spec_old.classes

@gw.testharness.DoNotVerifyResource
class Errant_ReadWriteOnlyProperty {
  
  property set WriteOnlyProp( str : String ) {
  }

  property get ReadOnlyProp() : String {
    return "foo"
  }
 
  function tryToReadWriteOnlyProp() {
    print( WriteOnlyProp ) 
    print( WriteOnlyProp + "" ) 
    print( this.WriteOnlyProp )
    print( this.WriteOnlyProp + "" ) 
    WriteOnlyProp = "asdf"
    this.WriteOnlyProp = "asdf"
  }
}
