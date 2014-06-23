package gw.lang.parser
uses gw.BaseVerifyErrantTest

class RelativeImportsAreNotSupportedTest extends BaseVerifyErrantTest {

  function testRelativeImportCausesError() {
    processErrantType( Errant_UsesPackageRelativeImports )
  }
}