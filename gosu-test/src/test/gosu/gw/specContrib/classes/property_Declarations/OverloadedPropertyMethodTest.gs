package gw.specContrib.classes.property_Declarations

uses gw.BaseVerifyErrantTest

class OverloadedPropertyMethodTest extends OverloadedPropertyBase {

  function testMe() {
    setCurrentTime(Date.Now)
    this.setCurrentTime(Date.Tomorrow)
    
    CurrentTime = Date.Now
  }

}
