package gw.internal.gosu.parser.classTests.gwtest.dynamic

uses dynamic.Dynamic
uses java.lang.*
uses gw.lang.reflect.IExpando
uses java.util.Map
uses java.util.HashMap
uses java.math.BigInteger
uses java.math.BigDecimal

class ExpandoTest extends gw.BaseVerifyErrantTest {
  function testExpandoDefaultConstructor() {
    var expando = new Dynamic()
    assertTrue( expando typeis IExpando )
    expando.Foo = "hey"
    assertEquals( "hey", expando.Foo )
  }

  function testExpandoSingleArgConstructor() {
    var expando = new Dynamic( new CustomExpandoTest.CustomExpando() )
    assertTrue( expando typeis IExpando )
    expando.Foo = "hey"
    assertEquals( "hey", expando.Foo )
  }

  function testAutoAssign() {
    var expando = new Dynamic()
    try {
      print( expando.A.B.C )
      fail( "Auto assign should work only when the expression is an l-value" )
    }
    catch( e: java.lang.Exception ) {
      // expected
    }
    expando.A.B.C = "yo"
    assertEquals( "yo", expando.A.B.C )
    assertTrue( expando.A typeis IExpando )
    assertTrue( expando.A.B typeis IExpando )
  }

  function testJsonLikeSyntax() {
    var person = new Dynamic() {
      :Name = "Joe",
      :Address = new() {
        :Line1 = "123 Main St.",
        :City = "Cupertino",
        :State = "CA"
      },
      :Hobbies = {
        "miniature golf",
        "sperm doning",
        "cage fighting"
      },
      :Cars = {
        new() {
          :Make = "Acura",
          :Model = "Integra",
          :Year = 1991
        },
        new() {
          :Make = "Audi",
          :Model = "POS",
          :Year = 2003
        }
      }
    }
    assertEquals( "Joe", person.Name )
    var address = person.Address
    assertEquals( "123 Main St.", address.Line1 )
    assertEquals( "Cupertino", address.City )
    assertEquals( "CA", address.State )
    var hobbies = person.Hobbies
    assertEquals( {"miniature golf", "sperm doning", "cage fighting"}, hobbies )
    var cars = person.Cars
    var car = cars[0]
    assertEquals( "Acura", car.Make )
    assertEquals( "Integra", car.Model )
    assertEquals( 1991, car.Year)
    car = cars[1]
    assertEquals( "Audi", car.Make )
    assertEquals( "POS", car.Model )
    assertEquals( 2003, car.Year)
  }
}