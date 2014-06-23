package gw.lang.enhancements

uses gw.test.TestClass
uses org.fest.assertions.Assertions

class CoreIterableOfIterablesEnhancementTest extends TestClass {
  function testFlattenEmpty() {
    var tester : List<List<String>> = {}
    Assertions.assertThat(tester.flatten().toList()).isEqualTo({})
  }
  
  function testFlattenSingle() {
    var tester : List<List<String>> = {{"1"}}
    Assertions.assertThat(tester.flatten().toList()).isEqualTo({"1"})
  }
  
  function testFlattenMultiple() {
    var tester : List<List<String>> = {{"1", "2"}, {"3"}}
    Assertions.assertThat(tester.flatten().toList()).isEqualTo({"1", "2", "3"})
  }
  
  function testFlattenMultipleWithEmpty() {
    var tester : List<List<String>> = {{"1", "2"}, {}, {"3"}}
    Assertions.assertThat(tester.flatten().toList()).isEqualTo({"1", "2", "3"})
  }
  
  function testFlattenAllEmpty() {
    var tester : List<List<String>> = {{}, {}}
    Assertions.assertThat(tester.flatten().toList()).isEqualTo({})
  }
}
