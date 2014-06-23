package gw.internal.gosu.regression
uses gw.test.TestClass
uses gw.lang.parser.resources.Res

class InnerStaticFeaturesTest extends TestClass {

  function testNonStaticInnerClassesCannotHaveInnerInterfaces() {
    assertFalse( Errant_HasInvalidInnerUsagesAndDecls.Type.Valid )
    assertTrue( Errant_HasInvalidInnerUsagesAndDecls.Type.ParseResultsException.ParseExceptions.
                hasMatch(\ i -> i.Line == 10 and i.MessageKey == Res.MSG_STATIC_MODIFIER_NOT_ALLOWED_HERE ) )
  }

  function testNonStaticInnerClassesCannotHaveStaticInnerClasses() {
    assertFalse( Errant_HasInvalidInnerUsagesAndDecls.Type.Valid )
    assertTrue( Errant_HasInvalidInnerUsagesAndDecls.Type.ParseResultsException.ParseExceptions.
                hasMatch(\ i -> i.Line == 11 and i.MessageKey == Res.MSG_STATIC_MODIFIER_NOT_ALLOWED_HERE ) )
  }

  function testNonStaticInnerClassesCannotHaveStaticMethods() {
    assertFalse( Errant_HasInvalidInnerUsagesAndDecls.Type.Valid )
    assertTrue( Errant_HasInvalidInnerUsagesAndDecls.Type.ParseResultsException.ParseExceptions.
                hasMatch(\ i -> i.Line == 12 and i.MessageKey == Res.MSG_STATIC_MODIFIER_NOT_ALLOWED_HERE ) )
  }

  function testNonStaticInnerClassesCannotHaveStaticProperties() {
    assertFalse( Errant_HasInvalidInnerUsagesAndDecls.Type.Valid )
    assertTrue( Errant_HasInvalidInnerUsagesAndDecls.Type.ParseResultsException.ParseExceptions.
                hasMatch(\ i -> i.Line == 13 and i.MessageKey == Res.MSG_STATIC_MODIFIER_NOT_ALLOWED_HERE ) )
  }

  function testNonStaticInnerClassesCannotHaveStaticVars() {
    assertFalse( Errant_HasInvalidInnerUsagesAndDecls.Type.Valid )
    assertTrue( Errant_HasInvalidInnerUsagesAndDecls.Type.ParseResultsException.ParseExceptions.
                hasMatch(\ i -> i.Line == 14 and i.MessageKey == Res.MSG_STATIC_MODIFIER_NOT_ALLOWED_HERE ) )
  }

  function testNonStaticClassesCannotBeInstantiatedInStaticPlaces() {
    assertFalse( Errant_HasInvalidInnerUsagesAndDecls.Type.Valid )
    assertTrue( Errant_HasInvalidInnerUsagesAndDecls.Type.ParseResultsException.ParseExceptions.
                hasMatch(\ i -> i.Line == 22 and i.MessageKey == Res.MSG_CANNOT_INSTANTIATE_NON_STATIC_CLASSES_HERE ) )
    assertTrue( Errant_HasInvalidInnerUsagesAndDecls.Type.ParseResultsException.ParseExceptions.
                hasMatch(\ i -> i.Line == 23 and i.MessageKey == Res.MSG_CANNOT_INSTANTIATE_NON_STATIC_CLASSES_HERE ) )

  }

  function testInterfaceInStaticInnerClassIsOK() {
    assertFalse( Errant_HasInvalidInnerUsagesAndDecls.Type.Valid )
    assertTrue( Errant_HasInvalidInnerUsagesAndDecls.StaticInnerClass1.
                Type.ClassStatement.ParseExceptions.Empty )
    assertTrue( Errant_HasInvalidInnerUsagesAndDecls.StaticInnerClass1.InnerInnerInterface1.
                Type.ClassStatement.ParseExceptions.Empty )
  }

  function testStaticInnerClassesCanBeInstantiated() {
    assertFalse( Errant_HasInvalidInnerUsagesAndDecls.Type.Valid )
    for( line in 29..32 ) {
      assertFalse( Errant_HasInvalidInnerUsagesAndDecls.Type.ParseResultsException.ParseExceptions.
                   hasMatch(\ i -> i.Line == line ) )
    }
  }

  function testStaticInterfacesLoad() {
    assertNotNull( ExplicitlyStaticInterface.Type.BackingClass )
    assertNotNull( ImplicitlyStaticInterface.Type.BackingClass )
  }

  interface ImplicitlyStaticInterface{
  }

  static interface ExplicitlyStaticInterface{
  }
}
