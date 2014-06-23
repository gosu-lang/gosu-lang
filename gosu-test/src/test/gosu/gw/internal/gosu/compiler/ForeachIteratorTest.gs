package gw.internal.gosu.compiler
uses gw.test.TestClass
uses gw.util.GosuTestUtil
uses gw.lang.parser.resources.Res
uses java.util.Iterator

class ForeachIteratorTest extends TestClass {

  function testBasicIteratorWorks() {
    var x = {"a", "b", "c"}
    for( s in x iterator it ) {
      it.remove()
    }
    assertEquals(0, x.Count)
  }

  function testBasicIteratorWorks2() {
    var x = {"a", "b", "c"}
    for( s in x iterator it ) {
      if( s == "b" ) {
        it.remove()
      }
    }
    assertEquals(2, x.Count)
  }

  function testBasicIteratorWorks3() {
    var x = {"a", "b", "c"}
    for( s in x iterator it ) {
      if( s != "b" ) {
        it.remove()
      }
    }
    assertEquals(1, x.Count)
  }

  function testBasicIteratorWorks4() {
    var x = {"a", "b", "c", "d"}
    for( s in x iterator it ) {
      it.remove()
      it.next()
    }
    assertEquals({"b", "d"}, x)
  }

  function testBasicIteratorWorks5() {
    var x = {"a", "b", "c", "d"}
    for( s in x iterator it ) {
      it.next()
      it.remove()
    }
    assertEquals({"a", "c"}, x)
  }
  
  function testBasicIteratorWorks6() {
    var x = {"a", "b", "c", "d"}
    for( s in x iterator it ) {
      assertEquals( s != "d", it.hasNext() )
    }
  }
  
  function testIteratorWorksBeforeIndex() {
    var x = {"0", "1", "2", "3"}
    for( s in x iterator it index i ) {
      assertEquals( i as String, s )
      it.remove()
    }
    assertEquals(0, x.Count)
  }

  function testIteratorWorksAfterIndex() {
    var x = {"0", "1", "2", "3"}
    for( s in x index i iterator it ) {
      assertEquals( i as String, s )
      it.remove()
    }
    assertEquals(0, x.Count)
  }

  function testSyntaxDoesNotInterfereWithIteratorDeclarations() {
    var it : Iterator
    var listIt : List<Iterator>
    var itString : Iterator<String>
    var iterator = 10
  }
  
  function testBasicEvalFormIsGood() {
    eval("for( i in {} iterator j){}" )
  }

  function testIteratorCantRepeat() {
    var pe = GosuTestUtil.getParseResultsException("for( i in {} iterator j iterator k){}" )
    assertTrue( pe.ParseIssues.hasMatch(\ i -> i.MessageKey == Res.MSG_UNEXPECTED_TOKEN ) )
  }

  function testIteratorCantRepeatWithIndex() {
    var pe = GosuTestUtil.getParseResultsException("for( i in {} iterator j index i iterator k){}" )
    assertTrue( pe.ParseIssues.hasMatch(\ i -> i.MessageKey == Res.MSG_UNEXPECTED_TOKEN ) )
    
    pe = GosuTestUtil.getParseResultsException("for( i in {} index i iterator j iterator k){}" )
    assertTrue( pe.ParseIssues.hasMatch(\ i -> i.MessageKey == Res.MSG_UNEXPECTED_TOKEN ) )
    
    pe = GosuTestUtil.getParseResultsException("for( i in {} iterator j iterator k index i ){}" )
    assertTrue( pe.ParseIssues.hasMatch(\ i -> i.MessageKey == Res.MSG_UNEXPECTED_TOKEN ) )
  }

  function testIteratorAndIndexNamesMustBeDistinct() {
    var pe = GosuTestUtil.getParseResultsException("for( i in {} iterator j index j){}" )
    assertTrue( pe.ParseIssues.hasMatch(\ i -> i.MessageKey == Res.MSG_VARIABLE_ALREADY_DEFINED ) )
  }

  function testIteratorAndIndexNamesMustBeDistinct2() {
    var pe = GosuTestUtil.getParseResultsException("for( i in {} index j iterator j ){}" )
    assertTrue( pe.ParseIssues.hasMatch(\ i -> i.MessageKey == Res.MSG_VARIABLE_ALREADY_DEFINED ) )
  }

  function testIteratorAndLoopVarNamesMustBeDistinct() {
    var pe = GosuTestUtil.getParseResultsException("for( i in {} iterator i ){}" )
    assertTrue( pe.ParseIssues.hasMatch(\ i -> i.MessageKey == Res.MSG_VARIABLE_ALREADY_DEFINED ) )
  }

  function testIteratorValueIsFinal() {
    var pe = GosuTestUtil.getParseResultsException("for( i in {} iterator it ){ it = null }" )
    assertTrue( pe.ParseIssues.hasMatch(\ i -> i.MessageKey == Res.MSG_PROPERTY_NOT_WRITABLE ) )
  }
  
  function testIteratorKeywordOnlyWorksForIterables() {
    var pe = GosuTestUtil.getParseResultsException("for( i in new String[0] iterator it ){}" )
    assertTrue( pe.ParseIssues.hasMatch(\ i -> i.MessageKey == Res.MSG_ITERATOR_SYMBOL_ONLY_SUPPORTED_ON_ITERABLE_OBJECTS ) )
  }

  function testIteratorKeywordOnlyWorksForIterables2() {
    var pe = GosuTestUtil.getParseResultsException("for( i in null as java.lang.Iterator iterator it ){}" )
    assertTrue( pe.ParseIssues.hasMatch(\ i -> i.MessageKey == Res.MSG_ITERATOR_SYMBOL_ONLY_SUPPORTED_ON_ITERABLE_OBJECTS ) )
  }

  function testIteratorKeywordMustBeWord() {
    var pe = GosuTestUtil.getParseResultsException("for( i in {} iterator 10 ){}" )
    assertTrue( pe.ParseIssues.hasMatch(\ i -> i.MessageKey == Res.MSG_EXPECTING_IDENTIFIER_FOREACH_ITERATOR ) )
  }

}
