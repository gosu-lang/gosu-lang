package gw.internal.gosu.regression
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_ConstructRecursiveType 
{
  class Blah<T extends Blah<T>> 
  {
  }
 
  class Fubar extends Blah<Fubar> 
  {
  }

  function errors()
  {
    var def = new Blah()    // error
    def = new Blah<Blah>()  // error 
    def = new Blah<Fubar>() // valid
    def = new Fubar()       // valid
  }
}
