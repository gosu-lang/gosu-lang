static class TestClass {

  property get Foo() : String { return "hi" }

  construct() {
    this(1)
  }

  construct(n: int) {
  }

}

var a = new TestClass()
return a.Foo
