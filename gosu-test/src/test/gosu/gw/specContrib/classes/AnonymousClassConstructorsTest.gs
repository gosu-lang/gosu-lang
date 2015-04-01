package gw.specContrib.classes

uses gw.BaseVerifyErrantTest

class AnonymousClassConstructorsTest extends BaseVerifyErrantTest {

  function testAnonymousOverridingWorksProperly() {
    var wasSet = false
    new Object() {
      construct() {
        wasSet = true
      }
    }
    assertTrue(wasSet)
  }
}