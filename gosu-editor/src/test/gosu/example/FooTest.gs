package example

uses example.Foo
uses org.junit.Assert
uses org.junit.Test

class FooTest {

  @Test
  function makeAFoo() {
    print("newing a Foo")
    var foo = new Foo()
    Assert.assertNotNull(foo)

    print(foo.doSomething("eureka"))
    Assert.assertEquals(42, foo.MeaningOfLife)
  }

}