package gw.specification.classes.the_Member_Access_Modifiers
uses gw.BaseVerifyErrantTest

class MemberAccessModifierTest extends BaseVerifyErrantTest {
  function testErrant_NoShadowingInnerClasses() {
    processErrantType(Errant_NoShadowingInnerClasses)
  }

  function testErrant_PrivateAccessModifier() {
    processErrantType(Errant_PrivateAccessModifier)
  }

  function testErrant_PrivateAccessModifierAndSubclasses() {
    processErrantType(Errant_PrivateAccessModifierAndSubclasses)
  }

  function testErrant_ProtectedAccessModifier() {
    processErrantType(Errant_ProtectedAccessModifier)
  }

  function testErrant_ProtectedAccessModifierAndSubclasses() {
    processErrantType(Errant_ProtectedAccessModifierAndSubclasses)
  }

  function testErrant_InternalAccessModifier() {
    processErrantType(Errant_InternalAccessModifier)
  }

  function testErrant_InternalAccessModifierAndSubclasses() {
    processErrantType(Errant_InternalAccessModifierAndSubclasses)
  }
}