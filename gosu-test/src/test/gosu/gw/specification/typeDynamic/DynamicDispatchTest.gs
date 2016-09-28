package gw.specification.typeDynamic

uses dynamic.Dynamic
uses java.lang.*
uses gw.lang.reflect.IExpando
uses java.util.Map
uses java.util.HashMap
uses java.math.BigInteger
uses java.math.BigDecimal

class DynamicDispatchTest extends gw.BaseVerifyErrantTest {
  function testDynamicDispatch() {
    var animal : Animal = new Chicken()
    var eater = new Eater()
    var deater : Dynamic = animal
    assertEquals( "Chicken", (eater as Dynamic).eat( deater ) )
    assertEquals( "Chicken", (eater as Dynamic).eat( deater ) )
  }

  static class Animal{}
  static class Chicken extends Animal {}
  static class Cow extends Animal {}
  static class Eater {
    function eat(animal : Animal) : String {
      return "Animal"
    }
    function eat(chicken : Chicken) : String {
      return "Chicken"
    }
    function eat(cow : Cow) : String {
      return "Cow"
    }
  }
}