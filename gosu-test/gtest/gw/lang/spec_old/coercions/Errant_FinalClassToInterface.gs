package gw.lang.spec_old.coercions

uses java.util.concurrent.Callable
uses java.util.Collection


class Errant_FinalClassToInterface {

  function bad()
  {
    foo( \-> "" )  // String is final, cannot be coerced to Collection
  }
  
  function foo( call: Callable<Collection> )
  {
  }        
}
