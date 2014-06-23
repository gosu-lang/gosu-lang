package testpackage.impl

uses testpackage.IFoo

class Foo implements IFoo {
  function bar() : String {
    return "baz"
  }
}
