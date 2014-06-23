package gw.lang.parser

uses java.util.concurrent.atomic.AtomicInteger
uses gw.lang.parser.resources.Res

class CoreGosuParserTest extends gw.test.TestClass
{
  
  function testThatBlocksCanAccessVirtualFunctionsInConstructors() {
    assertFalse( CoreGosuParserTest.Type.hasWarnings() )  // the constructor below shouldn't have warnings
                                                          // since the virtual function is accessed in a block
  }
  
  function testAtomicIntegerCannotBeUsedWithNumericLiterals() {
    var t = SampleErrantClass.Type
    assertFalse( t.Valid )
    assertNotNull( t.ParseResultsException.ParseExceptions.singleWhere(\ i -> i.Line == 8 and i.MessageKey == Res.MSG_TYPE_MISMATCH ) )
  }

  function testIterationOverRawIntegerIsCompliationError() {
    var t = SampleErrantClass.Type
    assertFalse( t.Valid )
  }

  function testExtensionOfAClassWithValueKeywordsWorks() {
    assertNotNull( new ExtendsHasFieldAndPropNamedValue() )
  }

  function testAbstractGosuClassesAreNotInstantiable() {
    assertFalse( Errant_AbstractInstantiation.Type.Valid )
    assertTrue( Errant_AbstractInstantiation.Type.ParseResultsException.ParseExceptions.hasMatch(\ i -> i.Line == 11 and i.MessageKey == Res.MSG_CANNOT_CONSTRUCT_ABSTRACT_CLASS ) )    
    assertTrue( Errant_AbstractInstantiation.Type.ParseResultsException.ParseExceptions.hasMatch(\ i -> i.Line == 17 and i.MessageKey == Res.MSG_CANNOT_CONSTRUCT_ABSTRACT_CLASS ) )    
  }

  construct() {
    var blk = \->{
      virtualFunction()
    }
    blk()
  }

  function virtualFunction(){}
}
