

structure Foo {
  function foo()
}

class Bar {
  function foo() {
    print("bar")
  }
}

class Doh {
  function foo() {
    print("doh")
  }
}

class Ray {
  function bar() {
    print("ray")
  }
}

var list = {new Bar(), new Doh(), new Ray()}

list.whereTypeIs( Foo ).each( \elt -> elt.foo() )