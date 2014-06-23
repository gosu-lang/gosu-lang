package gw.specification.types.subtypesAndCompatibility

uses gw.BaseVerifyErrantTest

class CompatibilityTest extends BaseVerifyErrantTest {
  function testErrant_SubTypesTest() {
    processErrantType(Errant_SubTypesTest)
  }

  function testErrant_StructureCompatibilityTest() {
    processErrantType(Errant_StructureCompatibilityTest)
  }
}