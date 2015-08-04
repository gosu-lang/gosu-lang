package gw.specContrib.expressions

class Errant_SuperAccess {
  function test() {
    var x = super[Object]  //## issuekeys: MSG_MEMBER_ACCESS_REQUIRED_FOR_SUPER
    var y = super[Object].hashCode()
  }
}