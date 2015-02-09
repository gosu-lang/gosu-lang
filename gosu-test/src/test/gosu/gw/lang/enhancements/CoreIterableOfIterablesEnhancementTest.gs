package gw.lang.enhancements

uses gw.test.TestClass

class CoreIterableOfIterablesEnhancementTest extends TestClass {
  function testFlattenEmpty() {
    var tester : List<List<String>> = {}
    assertIterableEquals( tester.flatten().toList(), {} )
  }
  
  function testFlattenSingle() {
    var tester : List<List<String>> = {{"1"}}
    assertIterableEquals( tester.flatten().toList(), {"1"} )
  }
  
  function testFlattenMultiple() {
    var tester : List<List<String>> = {{"1", "2"}, {"3"}}
    assertIterableEquals( tester.flatten().toList(), {"1", "2", "3"} )
  }
  
  function testFlattenMultipleWithEmpty() {
    var tester : List<List<String>> = {{"1", "2"}, {}, {"3"}}
    assertIterableEquals( tester.flatten().toList(), {"1", "2", "3"} )
  }
  
  function testFlattenAllEmpty() {
    var tester : List<List<String>> = {{}, {}}
    assertIterableEquals( tester.flatten().toList(), {} )
  }
}
