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

  function testMisc() {
     assertEquals( "hi", StaticImportMiscTest.testMe() )
     assertEquals( String, StaticImportMiscTest2.testMe() )
     assertEquals( "hi", StaticImportMiscTest2.testMe2() )
     assertEquals( "hi", StaticImportMiscTest3.testMe() )

    StaticImportDifferentMembersWithTheSameName.test()
    StaticImportDifferentMembersWithTheSameName2.test()

    StaticImportExplicit2Test.testMe()
  }
}
