package gw.specification.classes.NestedClasses_MemberClasses_And_InnerClasses

uses gw.BaseVerifyErrantTest

class InnerClassesTest extends BaseVerifyErrantTest {
  function testErrant_InnerClassesTest() {
    processErrantType(Errant_InnerClassesTest)
  }

  function testErrant_OuterKeywordTest() {
    processErrantType(Errant_OuterKeywordTest)
  }

}
