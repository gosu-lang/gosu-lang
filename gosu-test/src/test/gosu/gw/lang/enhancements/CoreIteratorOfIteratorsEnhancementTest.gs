package gw.lang.enhancements

uses gw.test.TestClass

class CoreIteratorOfIteratorsEnhancementTest extends TestClass   {
  function testFlattenEmpty() {
    var tester : List<List<String>> = {}
    var testIterator = tester.map(\ t -> t.iterator()).iterator()
    assertIterableEquals( testIterator.flatten().toList(), {} )
  }
  
  function testFlattenSingle() {
    var tester : List<List<String>> = {{"1"}}
    var testIterator = tester.map(\ t -> t.iterator()).iterator()
    assertIterableEquals( testIterator.flatten().toList(), {"1"} )
  }
  
  function testFlattenMultiple() {
    var tester : List<List<String>> = {{"1", "2"}, {"3"}}
    var testIterator = tester.map(\ t -> t.iterator()).iterator()
    assertIterableEquals( testIterator.flatten().toList(), {"1", "2", "3"} )
  }
  
  function testFlattenMultipleWithEmpty() {
    var tester : List<List<String>> = {{"1", "2"}, {}, {"3"}}
    var testIterator = tester.map(\ t -> t.iterator()).iterator()
    assertIterableEquals( testIterator.flatten().toList(), {"1", "2", "3"} )
  }
  
  function testFlattenAllEmpty() {
    var tester : List<List<String>> = {{}, {}}
    var testIterator = tester.map(\ t -> t.iterator()).iterator()
    assertIterableEquals( testIterator.flatten().toList(), {} )
  }
}
