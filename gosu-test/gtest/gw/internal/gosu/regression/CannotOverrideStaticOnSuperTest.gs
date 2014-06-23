package gw.internal.gosu.regression

uses gw.test.TestClass

class CannotOverrideStaticOnSuperTest extends TestClass {

  function testOverridingStaticMethod() {
    assertFalse( Errant_OverridesStaticOnSuper.Type.Valid )
  }

}