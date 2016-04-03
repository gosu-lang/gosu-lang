package gw.specContrib.statements.usesStatement

uses gw.BaseVerifyErrantTest

class StaticImportTest extends BaseVerifyErrantTest {
  function testNonStaticContext() {
    new StaticImportOnDemandTest().testMe_NonStatic()
    new StaticImportExplicitTest().testMe_NonStatic()
  }

  function testStaticContext() {
    StaticImportOnDemandTest.testMe_Static()
    StaticImportExplicitTest.testMe_Static()
  }
}