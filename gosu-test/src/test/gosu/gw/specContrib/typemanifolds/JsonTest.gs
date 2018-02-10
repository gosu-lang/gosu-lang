package gw.specContrib.typemanifolds

uses gw.BaseVerifyErrantTest
uses gw.lang.reflect.gs.IGosuProgram

class JsonTest extends BaseVerifyErrantTest {

  function testMe() {
    var person: Person = new Dynamic()
    person.Name = "Joe Namath"
    assertEquals( "Joe Namath", person.Name )

    var address: Person.Address = new Dynamic()
    address.City = "Dunedin"
    person.Address = address
    assertEquals( "Dunedin", person.Address.City )

    var baseball: Person.Hobby = new Dynamic()
    baseball.Category = "Sport"
    var fishing: Person.Hobby = new Dynamic()
    fishing.Category = "Recreation"
    var hobbies = {baseball, fishing}
    person.Hobby = hobbies
    var h = person.Hobby
    assertEquals( 2, h.size() )
    assertEquals( baseball, h.get( 0 ) )
    assertEquals( fishing, h.get( 1 ) )
  }

}