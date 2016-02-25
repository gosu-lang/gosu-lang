// 
// A property is an absraction for reading, writing, or computing the value of a private 
// or virtual field.  Properties can be used as if they are public data members, but they 
// are actually special methods called accessors.  This enables data to be accessed easily 
// and still helps promote the safety and flexibility of methods.
//
var ex = new PropertyExample()
print( ex.Bar )

class PropertyExample {
  var _bar = "bar"

  property get Bar() : String {
    return _bar
  }
  property set Bar( value : String ) {
    if( value == "Foo" ) throw "That's not a valid value for Bar!"
    _bar = value
  }
}

//
// Shorthand syntax for declaring a read/write property
//
var shortEx = new ShorthandExample()
print( shortEx.Bar )

class ShorthandExample {
 var _bar : String as Bar = "shortbar"
}