package gw.internal.gosu.regression
uses gw.test.TestClass

class NestedBlocksAndStringTemplatesTest extends TestClass {

  construct() {

  }
  
  function testUsingNestedBlocksWithStringTemplatesInThem() {
    new HasDoublyNestedBlocksAndStringTemplates().doStuffThatMightBreak({{"a", "b"}, {"c", "d"}})  
  }

}
