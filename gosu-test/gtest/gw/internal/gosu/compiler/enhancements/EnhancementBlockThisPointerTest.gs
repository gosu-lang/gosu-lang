package gw.internal.gosu.compiler.enhancements

uses gw.test.TestClass

class EnhancementBlockThisPointerTest extends TestClass 
{
  //=========================================================
  // helpers
  //=========================================================
  public var _testField : String as WritableProp

  public var _enhancementStorageSlot : String as EnhancementStorageSlot

  property get IntProperty() : int {
    return 42
  }

  property get RefProperty() : String {
    return "foo"
  }

  function intFunction() : int {
    return 42
  }
  
  function refFunction() : String {
    return "foo"
  }
   
  //=========================================================
  // 'this' pointer tests
  //=========================================================

  function testThisPointer() {
    _testThisPointer()
  }

  function testThisPointerMethodInvocation1_RefEnhancedFeatures() {
    _testThisPointerMethodInvocation1_RefEnhancedFeatures() 
  }
  
  function testThisPointerMethodInvocation2_RefEnhancedFeatures() {
    _testThisPointerMethodInvocation2_RefEnhancedFeatures() 
  }
  
  function testThisPointerPropertyInvocation1_RefEnhancedFeatures() {
    _testThisPointerPropertyInvocation1_RefEnhancedFeatures()
  }
  
  function testThisPointerPropertyInvocation2_RefEnhancedFeatures() {
   _testThisPointerPropertyInvocation2_RefEnhancedFeatures()
  }
  
  function testThisPointerPropertyWriting_RefEnhancedFeatures() {
    _testThisPointerPropertyWriting_RefEnhancedFeatures()
  }
  
  function testThisPointerFeildReading_RefEnhancedFeatures() {
    _testThisPointerFeildReading_RefEnhancedFeatures()
  }
  
  function testThisPointerFeildWriting_RefEnhancedFeatures() {
    _testThisPointerFeildWriting_RefEnhancedFeatures()
  }
  
  function testNestedThisPointer_RefEnhancedFeatures() {
    _testNestedThisPointer_RefEnhancedFeatures()
  }

  function testNestedThisPointerMethodInvocation1_RefEnhancedFeatures() {
    _testNestedThisPointerMethodInvocation1_RefEnhancedFeatures() 
  }
  
  function testNestedThisPointerMethodInvocation2_RefEnhancedFeatures() {
    _testNestedThisPointerMethodInvocation2_RefEnhancedFeatures() 
  }

  function testNestedThisPointerPropertyInvocation1_RefEnhancedFeatures() {
     _testNestedThisPointerPropertyInvocation1_RefEnhancedFeatures() 
  }
  
  function testNestedThisPointerPropertyInvocation2_RefEnhancedFeatures() {
    _testNestedThisPointerPropertyInvocation2_RefEnhancedFeatures() 
  }
  
  function testNestedThisPointerPropertyWriting_RefEnhancedFeatures() {
    _testNestedThisPointerPropertyWriting_RefEnhancedFeatures()
  }
  
  function testNestedThisPointerFeildReading_RefEnhancedFeatures() {
    _testNestedThisPointerFeildReading_RefEnhancedFeatures()
  }
  
  function testNestedThisPointerFeildWriting_RefEnhancedFeatures() {
    _testNestedThisPointerFeildWriting_RefEnhancedFeatures()
  }

  function testTripleNestedThisPointer_RefEnhancedFeatures() {
    _testTripleNestedThisPointer_RefEnhancedFeatures()
  }

  function testTripleNestedThisPointerMethodInvocation1_RefEnhancedFeatures() {
    _testTripleNestedThisPointerMethodInvocation1_RefEnhancedFeatures()
  }
  
  function testTripleNestedThisPointerMethodInvocation2_RefEnhancedFeatures() {
    _testTripleNestedThisPointerMethodInvocation2_RefEnhancedFeatures()
  }
  
  function testTripleNestedThisPointerPropertyInvocation1_RefEnhancedFeatures() {
    _testTripleNestedThisPointerPropertyInvocation1_RefEnhancedFeatures()
  }
  
  function testTripleNestedThisPointerPropertyInvocation2_RefEnhancedFeatures() {
    _testTripleNestedThisPointerPropertyInvocation2_RefEnhancedFeatures() 
  }
  
  function testTripleNestedThisPointerPropertyWriting_RefEnhancedFeatures() {
    _testTripleNestedThisPointerPropertyWriting_RefEnhancedFeatures() 
  }
  
  function testTripleNestedThisPointerFeildReading_RefEnhancedFeatures() {
    _testTripleNestedThisPointerFeildReading_RefEnhancedFeatures()
  }
  
  function testTripleNestedThisPointerFeildWriting_RefEnhancedFeatures() {
    _testTripleNestedThisPointerFeildWriting_RefEnhancedFeatures()
  }

  function testQuadrupleNestedThisPointer_RefEnhancedFeatures() {
    _testQuadrupleNestedThisPointer_RefEnhancedFeatures() 
  }

  function testQuadrupleNestedThisPointerMethodInvocation1_RefEnhancedFeatures() {
    _testQuadrupleNestedThisPointerMethodInvocation1_RefEnhancedFeatures()
  }
  
  function testQuadrupleNestedThisPointerMethodInvocation2_RefEnhancedFeatures() {
    _testQuadrupleNestedThisPointerMethodInvocation2_RefEnhancedFeatures() 
  }
  
  function testQuadrupleNestedThisPointerPropertyInvocation1_RefEnhancedFeatures() {
    _testQuadrupleNestedThisPointerPropertyInvocation1_RefEnhancedFeatures() 
  }
  
  function testQuadrupleNestedThisPointerPropertyInvocation2_RefEnhancedFeatures() {
    _testQuadrupleNestedThisPointerPropertyInvocation2_RefEnhancedFeatures()
  }

  function testQuadrupleNestedThisPointerPropertyWriting_RefEnhancedFeatures() {
    _testQuadrupleNestedThisPointerPropertyWriting_RefEnhancedFeatures()
  }
  
  function testQuadrupleNestedThisPointerFeildReading_RefEnhancedFeatures() {
    _testQuadrupleNestedThisPointerFeildReading_RefEnhancedFeatures()
  }
  
  function testQuadrupleNestedThisPointerFeildWriting_RefEnhancedFeatures() {
    _testQuadrupleNestedThisPointerFeildWriting_RefEnhancedFeatures()
  }

  function testThisPointerMethodInvocation1_RefEnhancementFeatures() {
    _testThisPointerMethodInvocation1_RefEnhancementFeatures() 
  }
  
  function testThisPointerMethodInvocation2_RefEnhancementFeatures() {
    _testThisPointerMethodInvocation2_RefEnhancementFeatures() 
  }
  
  function testThisPointerPropertyInvocation1_RefEnhancementFeatures() {
    _testThisPointerPropertyInvocation1_RefEnhancementFeatures()
  }
  
  function testThisPointerPropertyInvocation2_RefEnhancementFeatures() {
   _testThisPointerPropertyInvocation2_RefEnhancementFeatures()
  }
  
  function testThisPointerPropertyWriting_RefEnhancementFeatures() {
    _testThisPointerPropertyWriting_RefEnhancementFeatures()
  }

  function testNestedThisPointerMethodInvocation1_RefEnhancementFeatures() {
    _testNestedThisPointerMethodInvocation1_RefEnhancementFeatures() 
  }
  
  function testNestedThisPointerMethodInvocation2_RefEnhancementFeatures() {
    _testNestedThisPointerMethodInvocation2_RefEnhancementFeatures() 
  }

  function testNestedThisPointerPropertyInvocation1_RefEnhancementFeatures() {
     _testNestedThisPointerPropertyInvocation1_RefEnhancementFeatures() 
  }
  
  function testNestedThisPointerPropertyInvocation2_RefEnhancementFeatures() {
    _testNestedThisPointerPropertyInvocation2_RefEnhancementFeatures() 
  }
  
  function testNestedThisPointerPropertyWriting_RefEnhancementFeatures() {
    _testNestedThisPointerPropertyWriting_RefEnhancementFeatures()
  }

  function testTripleNestedThisPointerMethodInvocation1_RefEnhancementFeatures() {
    _testTripleNestedThisPointerMethodInvocation1_RefEnhancementFeatures()
  }
  
  function testTripleNestedThisPointerMethodInvocation2_RefEnhancementFeatures() {
    _testTripleNestedThisPointerMethodInvocation2_RefEnhancementFeatures()
  }
  
  function testTripleNestedThisPointerPropertyInvocation1_RefEnhancementFeatures() {
    _testTripleNestedThisPointerPropertyInvocation1_RefEnhancementFeatures()
  }
  
  function testTripleNestedThisPointerPropertyInvocation2_RefEnhancementFeatures() {
    _testTripleNestedThisPointerPropertyInvocation2_RefEnhancementFeatures() 
  }
  
  function testTripleNestedThisPointerPropertyWriting_RefEnhancementFeatures() {
    _testTripleNestedThisPointerPropertyWriting_RefEnhancementFeatures() 
  }
  
  function testQuadrupleNestedThisPointerMethodInvocation1_RefEnhancementFeatures() {
    _testQuadrupleNestedThisPointerMethodInvocation1_RefEnhancementFeatures()
  }
  
  function testQuadrupleNestedThisPointerMethodInvocation2_RefEnhancementFeatures() {
    _testQuadrupleNestedThisPointerMethodInvocation2_RefEnhancementFeatures() 
  }
  
  function testQuadrupleNestedThisPointerPropertyInvocation1_RefEnhancementFeatures() {
    _testQuadrupleNestedThisPointerPropertyInvocation1_RefEnhancementFeatures() 
  }
  
  function testQuadrupleNestedThisPointerPropertyInvocation2_RefEnhancementFeatures() {
    _testQuadrupleNestedThisPointerPropertyInvocation2_RefEnhancementFeatures()
  }

  function testQuadrupleNestedThisPointerPropertyWriting_RefEnhancementFeatures() {
    _testQuadrupleNestedThisPointerPropertyWriting_RefEnhancementFeatures()
  }
  
  function testImplicitThisPointerMethodInvocation1_RefEnhancementFeatures() {
    _testImplicitThisPointerMethodInvocation1_RefEnhancementFeatures() 
  }
    
  function testImplicitThisPointerMethodInvocation2_RefEnhancementFeatures() {
    _testImplicitThisPointerMethodInvocation2_RefEnhancementFeatures() 
  }

  function testImplicitThisPointerPropertyInvocation1_RefEnhancementFeatures() {
    _testImplicitThisPointerPropertyInvocation1_RefEnhancementFeatures() 
  }
    
  function testImplicitThisPointerPropertyInvocation2_RefEnhancementFeatures() {
    _testImplicitThisPointerPropertyInvocation2_RefEnhancementFeatures() 
  }
  
  function testImplicitThisPointerPropertyWriting_RefEnhancementFeatures() {
    _testImplicitThisPointerPropertyWriting_RefEnhancementFeatures() 
  }
    
  function testNestedImplicitThisPointerMethodInvocation1_RefEnhancementFeatures() {
    _testNestedImplicitThisPointerMethodInvocation1_RefEnhancementFeatures()
  }
    
  function testNestedImplicitThisPointerMethodInvocation2_RefEnhancementFeatures() {
    _testNestedImplicitThisPointerMethodInvocation2_RefEnhancementFeatures() 
  }

  function testNestedImplicitThisPointerPropertyInvocation1_RefEnhancementFeatures() {
    _testNestedImplicitThisPointerPropertyInvocation1_RefEnhancementFeatures() 
  }
    
  function testNestedImplicitThisPointerPropertyInvocation2_RefEnhancementFeatures() {
    _testNestedImplicitThisPointerPropertyInvocation2_RefEnhancementFeatures() 
  }

  function testNestedImplicitThisPointerPropertyWriting_RefEnhancementFeatures() {
    _testNestedImplicitThisPointerPropertyWriting_RefEnhancementFeatures()
  }
  
  function testTripleNestedImplicitThisPointerMethodInvocation1_RefEnhancementFeatures() {
    _testTripleNestedImplicitThisPointerMethodInvocation1_RefEnhancementFeatures() 
  }
    
  function testTripleNestedImplicitThisPointerMethodInvocation2_RefEnhancementFeatures() {
    _testTripleNestedImplicitThisPointerMethodInvocation2_RefEnhancementFeatures() 
  }

  function testTripleNestedImplicitThisPointerPropertyInvocation1_RefEnhancementFeatures() {
    _testTripleNestedImplicitThisPointerPropertyInvocation1_RefEnhancementFeatures() 
  }
    
  function testTripleNestedImplicitThisPointerPropertyInvocation2_RefEnhancementFeatures() {
    _testTripleNestedImplicitThisPointerPropertyInvocation2_RefEnhancementFeatures() 
  }
  
  function testTripleNestedImplicitThisPointerPropertyWriting_RefEnhancementFeatures() {
    _testTripleNestedImplicitThisPointerPropertyWriting_RefEnhancementFeatures() 
  }
}
