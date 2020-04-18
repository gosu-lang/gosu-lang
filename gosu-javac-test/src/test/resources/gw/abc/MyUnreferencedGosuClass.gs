package gw.abc

class MyUnreferencedGosuClass implements Runnable {
  property Name: String

  construct(name: String) {
    _Name = name
  }

  function run() {
    print("run")
  }

  class MyInnerClass {
    property Foo: String
    construct(foo: String) {
      _Foo = foo
    }
  }
}