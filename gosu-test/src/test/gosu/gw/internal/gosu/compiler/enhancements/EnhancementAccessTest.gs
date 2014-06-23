package gw.internal.gosu.compiler.enhancements
uses gw.test.TestClass
uses gw.internal.gosu.compiler.sample.enhancements._Enhanced
uses gw.internal.gosu.compiler.sample.enhancements._ExtendsEnhanced

class EnhancementAccessTest extends TestClass {

  function testEnhancementCanAccessProtectedMethod() {
    var x = new _Enhanced()
    assertEquals( "protected function",  x.accessesProtectedMethod() ) 
  }

  function testEnhancementCanAccessProtectedProperty() {
    var x = new _Enhanced()
    assertEquals( "protected property",  x.accessesProtectedProperty() ) 
  }

  function testEnhancementCanWriteProtectedProperty() {
    var x = new _Enhanced()
    assertEquals( "updated property",  x.writesProtectedProperty() ) 
  }

  function testEnhancementCanAccessProtectedField() {
    var x = new _Enhanced()
    assertEquals( "protected field",  x.accessesProtectedField() ) 
  }

  function testEnhancementCanWriteProtectedField() {
    var x = new _Enhanced()
    assertEquals( "updated field",  x.writesProtectedField() ) 
  }

  function testEnhancementCanAccessProtectedConstructor() {
    var x = new _Enhanced()
    assertTrue( "updated field",  x.accessesProtectedConstructor() ) 
  }

  function testEnhancementCanAccessProtectedMethodIndirectly() {
    var x = new _Enhanced()
    assertEquals( "protected function",  x.accessesProtectedMethodIndirectly() ) 
  }

  function testEnhancementCanAccessProtectedPropertyIndirectly() {
    var x = new _Enhanced()
    assertEquals( "protected property",  x.accessesProtectedPropertyIndirectly() ) 
  }

  function testEnhancementCanWriteProtectedPropertyIndirectly() {
    var x = new _Enhanced()
    assertEquals( "updated property",  x.writesProtectedPropertyIndirectly() ) 
  }

  function testEnhancementCanAccessProtectedFieldIndirectly() {
    var x = new _Enhanced()
    assertEquals( "protected field",  x.accessesProtectedFieldIndirectly() ) 
  }

  function testEnhancementCanWriteProtectedFieldIndirectly() {
    var x = new _Enhanced()
    assertEquals( "updated field",  x.writesProtectedFieldIndirectly() ) 
  }

  function testEnhancementCanAccessProtectedStaticMethod() {
    var x = new _Enhanced()
    assertEquals( "static protected function",  x.accessesStaticProtectedMethod() ) 
  }

  function testEnhancementCanAccessProtectedStaticProperty() {
    var x = new _Enhanced()
    assertEquals( "static protected property",  x.accessesStaticProtectedProperty() ) 
  }

  function testEnhancementCanWriteProtectedStaticProperty() {
    var x = new _Enhanced()
    assertEquals( "updated static property",  x.writesStaticProtectedProperty() ) 
  }

  function testEnhancementCanAccessStaticProtectedStaticField() {
    var x = new _Enhanced()
    assertEquals( "static protected field",  x.accessesStaticProtectedField() ) 
  }

  function testEnhancementCanWriteProtectedStaticField() {
    var x = new _Enhanced()
    assertEquals( "updated static field",  x.writesStaticProtectedField() ) 
  }
  
  function testEnhancementCanAccessProtectedMethodStaticThroughThisPointer() {
    var x = new _Enhanced()
    assertEquals( "static protected function",  x.accessesStaticProtectedMethodThroughThisPointer() ) 
  }

  function testEnhancementCanAccessProtectedPropertyStaticThroughThisPointer() {
    var x = new _Enhanced()
    assertEquals( "static protected property",  x.accessesStaticProtectedPropertyThroughThisPointer() ) 
  }

  function testEnhancementCanWriteProtectedPropertyStaticThroughThisPointer() {
    var x = new _Enhanced()
    assertEquals( "updated static property",  x.writesStaticProtectedPropertyThroughThisPointer() ) 
  }

  function testEnhancementCanAccessProtectedStaticFieldThroughThisPointer() {
    var x = new _Enhanced()
    assertEquals( "static protected field",  x.accessesStaticProtectedFieldThroughThisPointer() ) 
  }

  function testEnhancementCanWriteProtectedFieldStaticThroughThisPointer() {
    var x = new _Enhanced()
    assertEquals( "updated static field",  x.writesStaticProtectedFieldThroughThisPointer() ) 
  }
  
  function testEnhancementCanAccessProtectedMethodStaticIndirectly() {
    var x = new _Enhanced()
    assertEquals( "static protected function",  x.accessesStaticProtectedMethodThroughThisPointer() ) 
  }

  function testEnhancementCanAccessProtectedPropertyStaticIndirectly() {
    var x = new _Enhanced()
    assertEquals( "static protected property",  x.accessesStaticProtectedPropertyThroughThisPointer() ) 
  }

  function testEnhancementCanWriteProtectedPropertyStaticIndirectly() {
    var x = new _Enhanced()
    assertEquals( "updated static property",  x.writesStaticProtectedPropertyThroughThisPointer() ) 
  }

  function testEnhancementCanAccessProtectedStaticFieldIndirectly() {
    var x = new _Enhanced()
    assertEquals( "static protected field",  x.accessesStaticProtectedFieldThroughThisPointer() ) 
  }

  function testEnhancementCanWriteProtectedFieldStaticIndirectly() {
    var x = new _Enhanced()
    assertEquals( "updated static field",  x.writesStaticProtectedFieldThroughThisPointer() ) 
  }

  // In Block ---

  function testEnhancementCanAccessProtectedMethodInBlock() {
    var x = new _Enhanced()
    var blk = x.accessesProtectedMethodInBlock()
    assertEquals( "protected function", blk()  )
  }

  function testEnhancementCanAccessProtectedPropertyInBlock() {
    var x = new _Enhanced()
    var blk = x.accessesProtectedPropertyInBlock()
    assertEquals( "protected property", blk() )
  }

  function testEnhancementCanWriteProtectedPropertyInBlock() {
    var x = new _Enhanced()
    var blk = x.writesProtectedPropertyInBlock()
    assertEquals( "updated property",  blk() )
  }

  function testEnhancementCanAccessProtectedFieldInBlock() {
    var x = new _Enhanced()
    var blk = x.accessesProtectedFieldInBlock()
    assertEquals( "protected field",  blk() )
  }

  function testEnhancementCanWriteProtectedFieldInBlock() {
    var x = new _Enhanced()
    var blk = x.writesProtectedFieldInBlock()
    assertEquals( "updated field",  blk() )
  }

  function testEnhancementCanAccessProtectedConstructorInBlock() {
    var x = new _Enhanced()
    var blk = x.accessesProtectedConstructorInBlock()
    assertTrue( "updated field",  blk() )
  }

  function testEnhancementCanAccessProtectedMethodIndirectlyInBlock() {
    var x = new _Enhanced()
    assertEquals( "protected function",  x.accessesProtectedMethodIndirectly() )
  }

  function testEnhancementCanAccessProtectedPropertyIndirectlyInBlock() {
    var x = new _Enhanced()
    var blk = x.accessesProtectedPropertyIndirectlyInBlock()
    assertEquals( "protected property",  blk() )
  }

  function testEnhancementCanWriteProtectedPropertyIndirectlyInBlock() {
    var x = new _Enhanced()
    var blk = x.writesProtectedPropertyIndirectlyInBlock()
    assertEquals( "updated property",  blk() )
  }

  function testEnhancementCanAccessProtectedFieldIndirectlyInBlock() {
    var x = new _Enhanced()
    var blk = x.accessesProtectedFieldIndirectlyInBlock()
    assertEquals( "protected field",  blk() )
  }

  function testEnhancementCanWriteProtectedFieldIndirectlyInBlock() {
    var x = new _Enhanced()
    var blk = x.writesProtectedFieldIndirectlyInBlock()
    assertEquals( "updated field",  blk() )
  }

  function testEnhancementCanAccessProtectedStaticMethodInBlock() {
    var x = new _Enhanced()
    var blk = x.accessesStaticProtectedMethodInBlock()
    assertEquals( "static protected function",  blk() )
  }

  function testEnhancementCanAccessProtectedStaticPropertyInBlock() {
    var x = new _Enhanced()
    var blk = x.accessesStaticProtectedPropertyInBlock()
    assertEquals( "static protected property",  blk() )
  }

  function testEnhancementCanWriteProtectedStaticPropertyInBlock() {
    var x = new _Enhanced()
    var blk = x.writesStaticProtectedPropertyInBlock()
    assertEquals( "updated static property",  blk() )
  }

  function testEnhancementCanAccessStaticProtectedStaticFieldInBlock() {
    var x = new _Enhanced()
    var blk = x.accessesStaticProtectedFieldInBlock()
    assertEquals( "static protected field",  blk() )
  }

  function testEnhancementCanWriteProtectedStaticFieldInBlock() {
    var x = new _Enhanced()
    var blk = x.writesStaticProtectedFieldInBlock()
    assertEquals( "updated static field",  blk() )
  }

  function testEnhancementCanAccessProtectedMethodStaticThroughThisPointerInBlock() {
    var x = new _Enhanced()
    var blk = x.accessesStaticProtectedMethodInBlock()
    assertEquals( "static protected function",  blk() )
  }

  function testEnhancementCanAccessProtectedPropertyStaticThroughThisPointerInBlock() {
    var x = new _Enhanced()
    var blk = x.accessesStaticProtectedPropertyInBlock()
    assertEquals( "static protected property",  blk() )
  }

  function testEnhancementCanWriteProtectedPropertyStaticThroughThisPointerInBlock() {
    var x = new _Enhanced()
    var blk = x.writesStaticProtectedPropertyInBlock()
    assertEquals( "updated static property",  blk() )
  }

  function testEnhancementCanAccessProtectedStaticFieldThroughThisPointerInBlock() {
    var x = new _Enhanced()
    var blk = x.accessesStaticProtectedFieldInBlock()
    assertEquals( "static protected field",  blk() )
  }

  function testEnhancementCanWriteProtectedFieldStaticThroughThisPointerInBlock() {
    var x = new _Enhanced()
    var blk = x.writesStaticProtectedFieldInBlock()
    assertEquals( "updated static field",  blk() )
  }

  function testEnhancementCanAccessProtectedMethodStaticIndirectlyInBlock() {
    var x = new _Enhanced()
    var blk = x.accessesStaticProtectedMethodInBlock()
    assertEquals( "static protected function",  blk() )
  }

  function testEnhancementCanAccessProtectedPropertyStaticIndirectlyInBlock() {
    var x = new _Enhanced()
    var blk = x.accessesStaticProtectedPropertyInBlock()
    assertEquals( "static protected property",  blk() )
  }

  function testEnhancementCanWriteProtectedPropertyStaticIndirectlyInBlock() {
    var x = new _Enhanced()
    var blk = x.writesStaticProtectedPropertyInBlock()
    assertEquals( "updated static property",  blk() )
  }

  function testEnhancementCanAccessProtectedStaticFieldIndirectlyInBlock() {
    var x = new _Enhanced()
    var blk = x.accessesStaticProtectedFieldInBlock()
    assertEquals( "static protected field",  blk() )
  }

  function testEnhancementCanWriteProtectedFieldStaticIndirectlyInBlock() {
    var x = new _Enhanced()
    var blk = x.writesStaticProtectedFieldInBlock()
    assertEquals( "updated static field",  blk() )
  }

  // In Block Within Block ---

  function testEnhancementCanAccessProtectedMethodInBlockInBlock() {
    var x = new _Enhanced()
    var blk1 = x.accessesProtectedMethodInBlockInBlock()
    var blk = blk1()
    assertEquals( "protected function", blk()  )
  }

  function testEnhancementCanAccessProtectedPropertyInBlockInBlock() {
    var x = new _Enhanced()
    var blk1 = x.accessesProtectedPropertyInBlockInBlock()
    var blk = blk1()
    assertEquals( "protected property", blk() )
  }

  function testEnhancementCanWriteProtectedPropertyInBlockInBlock() {
    var x = new _Enhanced()
    var blk1 = x.writesProtectedPropertyInBlockInBlock()
    var blk = blk1()
    assertEquals( "updated property",  blk() )
  }

  function testEnhancementCanAccessProtectedFieldInBlockInBlock() {
    var x = new _Enhanced()
    var blk1 = x.accessesProtectedFieldInBlockInBlock()
    var blk = blk1()
    assertEquals( "protected field",  blk() )
  }

  function testEnhancementCanWriteProtectedFieldInBlockInBlock() {
    var x = new _Enhanced()
    var blk1 = x.writesProtectedFieldInBlockInBlock()
    var blk = blk1()
    assertEquals( "updated field",  blk() )
  }

  function testEnhancementCanAccessProtectedConstructorInBlockInBlock() {
    var x = new _Enhanced()
    var blk1 = x.accessesProtectedConstructorInBlockInBlock()
    var blk = blk1()
    assertTrue( "updated field",  blk() )
  }

  function testEnhancementCanAccessProtectedMethodIndirectlyInBlockInBlock() {
    var x = new _Enhanced()
    assertEquals( "protected function",  x.accessesProtectedMethodIndirectly() )
  }

  function tes1tEnhancementCanAccessProtectedPropertyIndirectlyInBlockInBlock() {
    var x = new _Enhanced()
    var blk1 = x.accessesProtectedPropertyIndirectlyInBlockInBlock()
    var blk = blk1()
    assertEquals( "protected property",  blk() )
  }

  function testEnhancementCanWriteProtectedPropertyIndirectlyInBlockInBlock() {
    var x = new _Enhanced()
    var blk1 = x.writesProtectedPropertyIndirectlyInBlockInBlock()
    var blk = blk1()
    assertEquals( "updated property",  blk() )
  }

  function testEnhancementCanAccessProtectedFieldIndirectlyInBlockInBlock() {
    var x = new _Enhanced()
    var blk1 = x.accessesProtectedFieldIndirectlyInBlockInBlock()
    var blk = blk1()
    assertEquals( "protected field",  blk() )
  }

  function testEnhancementCanWriteProtectedFieldIndirectlyInBlockInBlock() {
    var x = new _Enhanced()
    var blk1 = x.writesProtectedFieldIndirectlyInBlockInBlock()
    var blk = blk1()
    assertEquals( "updated field",  blk() )
  }

  function testEnhancementCanAccessProtectedStaticMethodInBlockInBlock() {
    var x = new _Enhanced()
    var blk1 = x.accessesStaticProtectedMethodInBlockInBlock()
    var blk = blk1()
    assertEquals( "static protected function",  blk() )
  }

  function testEnhancementCanAccessProtectedStaticPropertyInBlockInBlock() {
    var x = new _Enhanced()
    var blk1 = x.accessesStaticProtectedPropertyInBlockInBlock()
    var blk = blk1()
    assertEquals( "static protected property",  blk() )
  }

  function testEnhancementCanWriteProtectedStaticPropertyInBlockInBlock() {
    var x = new _Enhanced()
    var blk1 = x.writesStaticProtectedPropertyInBlockInBlock()
    var blk = blk1()
    assertEquals( "updated static property",  blk() )
  }

  function testEnhancementCanAccessStaticProtectedStaticFieldInBlockInBlock() {
    var x = new _Enhanced()
    var blk1 = x.accessesStaticProtectedFieldInBlockInBlock()
    var blk = blk1()
    assertEquals( "static protected field",  blk() )
  }

  function testEnhancementCanWriteProtectedStaticFieldInBlockInBlock() {
    var x = new _Enhanced()
    var blk1 = x.writesStaticProtectedFieldInBlockInBlock()
    var blk = blk1()
    assertEquals( "updated static field",  blk() )
  }

  function testEnhancementCanAccessProtectedMethodStaticThroughThisPointerInBlockInBlock() {
    var x = new _Enhanced()
    var blk1 = x.accessesStaticProtectedMethodInBlockInBlock()
    var blk = blk1()
    assertEquals( "static protected function",  blk() )
  }

  function testEnhancementCanAccessProtectedPropertyStaticThroughThisPointerInBlockInBlock() {
    var x = new _Enhanced()
    var blk1 = x.accessesStaticProtectedPropertyInBlockInBlock()
    var blk = blk1()
    assertEquals( "static protected property",  blk() )
  }

  function testEnhancementCanWriteProtectedPropertyStaticThroughThisPointerInBlockInBlock() {
    var x = new _Enhanced()
    var blk1 = x.writesStaticProtectedPropertyInBlockInBlock()
    var blk = blk1()
    assertEquals( "updated static property",  blk() )
  }

  function testEnhancementCanAccessProtectedStaticFieldThroughThisPointerInBlockInBlock() {
    var x = new _Enhanced()
    var blk1 = x.accessesStaticProtectedFieldInBlockInBlock()
    var blk = blk1()
    assertEquals( "static protected field",  blk() )
  }

  function testEnhancementCanWriteProtectedFieldStaticThroughThisPointerInBlockInBlock() {
    var x = new _Enhanced()
    var blk1 = x.writesStaticProtectedFieldInBlockInBlock()
    var blk = blk1()
    assertEquals( "updated static field",  blk() )
  }

  function testEnhancementCanAccessProtectedMethodStaticIndirectlyInBlockInBlock() {
    var x = new _Enhanced()
    var blk1 = x.accessesStaticProtectedMethodInBlockInBlock()
    var blk = blk1()
    assertEquals( "static protected function",  blk() )
  }

  function testEnhancementCanAccessProtectedPropertyStaticIndirectlyInBlockInBlock() {
    var x = new _Enhanced()
    var blk1 = x.accessesStaticProtectedPropertyInBlockInBlock()
    var blk = blk1()
    assertEquals( "static protected property",  blk() )
  }

  function testEnhancementCanWriteProtectedPropertyStaticIndirectlyInBlockInBlock() {
    var x = new _Enhanced()
    var blk1 = x.writesStaticProtectedPropertyInBlockInBlock()
    var blk = blk1()
    assertEquals( "updated static property",  blk() )
  }

  function testEnhancementCanAccessProtectedStaticFieldIndirectlyInBlockInBlock() {
    var x = new _Enhanced()
    var blk1 = x.accessesStaticProtectedFieldInBlockInBlock()
    var blk = blk1()
    assertEquals( "static protected field",  blk() )
  }

  function testEnhancementCanWriteProtectedFieldStaticIndirectlyInBlockInBlock() {
    var x = new _Enhanced()
    var blk1 = x.writesStaticProtectedFieldInBlockInBlock()
    var blk = blk1()
    assertEquals( "updated static field",  blk() )
  }

  // Sub ---

  function testEnhancementCanAccessProtectedMethodOnSuper() {
    var x = new _ExtendsEnhanced()
    assertEquals( "protected function",  x.ex_accessesProtectedMethod() )
  }

  function testEnhancementCanAccessProtectedPropertyOnSuper() {
    var x = new _ExtendsEnhanced()
    assertEquals( "protected property",  x.ex_accessesProtectedProperty() )
  }

  function testEnhancementCanWriteProtectedPropertyOnSuper() {
    var x = new _ExtendsEnhanced()
    assertEquals( "updated property",  x.ex_writesProtectedProperty() )
  }

  function testEnhancementCanAccessProtectedFieldOnSuper() {
    var x = new _ExtendsEnhanced()
    assertEquals( "protected field",  x.ex_accessesProtectedField() )
  }

  function testEnhancementCanWriteProtectedFieldOnSuper() {
    var x = new _ExtendsEnhanced()
    assertEquals( "updated field",  x.ex_writesProtectedField() )
  }

  function testEnhancementCanAccessProtectedConstructorOnSuper() {
    var x = new _ExtendsEnhanced()
    assertTrue( "updated field",  x.ex_accessesProtectedConstructor() )
  }

  function testEnhancementCanAccessProtectedMethodIndirectlyOnSuper() {
    var x = new _ExtendsEnhanced()
    assertEquals( "protected function",  x.ex_accessesProtectedMethodIndirectly() )
  }

  function testEnhancementCanAccessProtectedPropertyIndirectlyOnSuper() {
    var x = new _ExtendsEnhanced()
    assertEquals( "protected property",  x.ex_accessesProtectedPropertyIndirectly() )
  }

  function testEnhancementCanWriteProtectedPropertyIndirectlyOnSuper() {
    var x = new _ExtendsEnhanced()
    assertEquals( "updated property",  x.ex_writesProtectedPropertyIndirectly() )
  }

  function testEnhancementCanAccessProtectedFieldIndirectlyOnSuper() {
    var x = new _ExtendsEnhanced()
    assertEquals( "protected field",  x.ex_accessesProtectedFieldIndirectly() )
  }

  function testEnhancementCanWriteProtectedFieldIndirectlyOnSuper() {
    var x = new _ExtendsEnhanced()
    assertEquals( "updated field",  x.ex_writesProtectedFieldIndirectly() )
  }

  function testEnhancementCanAccessProtectedStaticMethodOnSuper() {
    var x = new _ExtendsEnhanced()
    assertEquals( "static protected function",  x.ex_accessesStaticProtectedMethod() )
  }

  function testEnhancementCanAccessProtectedStaticPropertyOnSuper() {
    var x = new _ExtendsEnhanced()
    assertEquals( "static protected property",  x.ex_accessesStaticProtectedProperty() )
  }

  function testEnhancementCanWriteProtectedStaticPropertyOnSuper() {
    var x = new _ExtendsEnhanced()
    assertEquals( "updated static property",  x.ex_writesStaticProtectedProperty() )
  }

  function testEnhancementCanAccessStaticProtectedStaticFieldOnSuper() {
    var x = new _ExtendsEnhanced()
    assertEquals( "static protected field",  x.ex_accessesStaticProtectedField() )
  }

  function testEnhancementCanWriteProtectedStaticFieldOnSuper() {
    var x = new _ExtendsEnhanced()
    assertEquals( "updated static field",  x.ex_writesStaticProtectedField() )
  }

  function testEnhancementCanAccessProtectedMethodStaticThroughThisPointerOnSuper() {
    var x = new _ExtendsEnhanced()
    assertEquals( "static protected function",  x.ex_accessesStaticProtectedMethodThroughThisPointer() )
  }

  function testEnhancementCanAccessProtectedPropertyStaticThroughThisPointerOnSuper() {
    var x = new _ExtendsEnhanced()
    assertEquals( "static protected property",  x.ex_accessesStaticProtectedPropertyThroughThisPointer() )
  }

  function testEnhancementCanWriteProtectedPropertyStaticThroughThisPointerOnSuper() {
    var x = new _ExtendsEnhanced()
    assertEquals( "updated static property",  x.ex_writesStaticProtectedPropertyThroughThisPointer() )
  }

  function testEnhancementCanAccessProtectedStaticFieldThroughThisPointerOnSuper() {
    var x = new _ExtendsEnhanced()
    assertEquals( "static protected field",  x.ex_accessesStaticProtectedFieldThroughThisPointer() )
  }

  function testEnhancementCanWriteProtectedFieldStaticThroughThisPointerOnSuper() {
    var x = new _ExtendsEnhanced()
    assertEquals( "updated static field",  x.ex_writesStaticProtectedFieldThroughThisPointer() )
  }

  function testEnhancementCanAccessProtectedMethodStaticIndirectlyOnSuper() {
    var x = new _ExtendsEnhanced()
    assertEquals( "static protected function",  x.ex_accessesStaticProtectedMethodThroughThisPointer() )
  }

  function testEnhancementCanAccessProtectedPropertyStaticIndirectlyOnSuper() {
    var x = new _ExtendsEnhanced()
    assertEquals( "static protected property",  x.ex_accessesStaticProtectedPropertyThroughThisPointer() )
  }

  function testEnhancementCanWriteProtectedPropertyStaticIndirectlyOnSuper() {
    var x = new _ExtendsEnhanced()
    assertEquals( "updated static property",  x.ex_writesStaticProtectedPropertyThroughThisPointer() )
  }

  function testEnhancementCanAccessProtectedStaticFieldIndirectlyOnSuper() {
    var x = new _ExtendsEnhanced()
    assertEquals( "static protected field",  x.ex_accessesStaticProtectedFieldThroughThisPointer() )
  }

  function testEnhancementCanWriteProtectedFieldStaticIndirectlyOnSuper() {
    var x = new _ExtendsEnhanced()
    assertEquals( "updated static field",  x.ex_writesStaticProtectedFieldThroughThisPointer() ) 
  }

  function testEnhancementCanAccessProtectedMethodWPrimitive() {
    var x = new _Enhanced()
    assertEquals( 42,  x.i_accessesProtectedMethod() ) 
  }

  function testEnhancementCanAccessProtectedStaticMethodWPrimitive() {
    var x = new _Enhanced()
    assertEquals( 42,  x.i_accessesStaticProtectedMethod() ) 
  }

  function testEnhancementCanAccessProtectedPropertyWPrimitive() {
    var x = new _Enhanced()
    assertEquals( 42,  x.i_accessesProtectedProperty() ) 
  }

  function testEnhancementCanAccessProtectedStaticPropertyWPrimitive() {
    var x = new _Enhanced()
    assertEquals( 42,  x.i_accessesStaticProtectedProperty() ) 
  }

  function testEnhancementCanWriteProtectedPropertyWPrimitive() {
    var x = new _Enhanced()
    assertEquals( 52,  x.i_writesProtectedProperty() ) 
  }

  function testEnhancementCanWriteProtectedStaticPropertyWPrimitive() {
    var x = new _Enhanced()
    assertEquals( 52,  x.i_writesStaticProtectedProperty() ) 
  }

  function testEnhancementCanAccessProtectedFieldWPrimitve() {
    var x = new _Enhanced()
    assertEquals( 42,  x.i_accessesProtectedField() ) 
  }

  function testEnhancementCanAccessStaticProtectedStaticFieldWPrimitive() {
    var x = new _Enhanced()
    assertEquals( 42,  x.i_accessesStaticProtectedField() ) 
  }

  function testEnhancementCanWriteProtectedFieldWPrimitive() {
    var x = new _Enhanced()
    assertEquals( 52,  x.i_writesProtectedField() ) 
  }

  function testEnhancementCanWriteProtectedStaticFieldWPrimitive() {
    var x = new _Enhanced()
    assertEquals( 52,  x.i_writesStaticProtectedField() ) 
  }
   
  function testEnhancementCanAccessStaticProtectedFieldOfSuperClassOfEnhancedType() {
    assertEquals( "hello", gw.internal.gosu.parser.sub.SubClassOfSuperClassWithStaticProtectedMembers.accessStaticProtectedFieldOnSuper() )
  }
  
  function testEnhancementCanAccessStaticProtectedMethodOfSuperClassOfEnhancedType() {
    assertEquals( "poo", gw.internal.gosu.parser.sub.SubClassOfSuperClassWithStaticProtectedMembers.accessStaticProtectedMethodOnSuper() )
  }
}