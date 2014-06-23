package gw.internal.gosu.compiler.enhancements

enhancement EnhancementBlockThisPointerTestEnhancement : EnhancementBlockThisPointerTest {

  //=========================================================
  // helpers
  //=========================================================

  property get Enh_IntProperty() : int {
    return 42
  }

  property get Enh_RefProperty() : String {
    return "foo"
  }

  function enh_intFunction() : int {
    return 42
  }
  
  function enh_refFunction() : String {
    return "foo"
  }
  
  property get Enh_WritableProp() : String {
    return this.EnhancementStorageSlot
  }

  property set Enh_WritableProp( s : String ) {
    this.EnhancementStorageSlot = s
  }

  //=========================================================
  // explicit this pointer tests w/ features on enhanced
  //=========================================================

  function _testThisPointer() {
    var blk = \-> this
    this.assertEquals( this, blk() )
  }

  function _testThisPointerMethodInvocation1_RefEnhancedFeatures() {
    var blk = \-> this.intFunction()
    this.assertEquals( 42, blk() )
  }
  
  function _testThisPointerMethodInvocation2_RefEnhancedFeatures() {
    var blk = \-> this.refFunction()
    this.assertEquals( "foo", blk() )
  }
  
  function _testThisPointerPropertyInvocation1_RefEnhancedFeatures() {
    var blk = \-> this.IntProperty
    this.assertEquals( 42, blk() )
  }
  
  function _testThisPointerPropertyInvocation2_RefEnhancedFeatures() {
    var blk = \-> this.RefProperty
    this.assertEquals( "foo", blk() )
  }
  
  function _testThisPointerPropertyWriting_RefEnhancedFeatures() {
    this.WritableProp = "foo"
    var blk = \-> { this.WritableProp = "bar" }
    blk()
    this.assertEquals( "bar", this.WritableProp  )
    this.WritableProp = "foo"
    blk()
    this.assertEquals( "bar", this.WritableProp )
  }
  
  function _testThisPointerFeildReading_RefEnhancedFeatures() {
    this._testField = "foo"
    var blk = \-> this._testField
    this.assertEquals( "foo", blk() )
    this._testField = "bar"
    this.assertEquals( "bar", blk() )
  }
  
  function _testThisPointerFeildWriting_RefEnhancedFeatures() {
    this._testField = "foo"
    var blk = \-> { this._testField = "bar" }
    blk()
    this.assertEquals( "bar", this._testField  )
    this._testField = "foo"
    blk()
    this.assertEquals( "bar", this._testField )
  }
  
  function _testNestedThisPointer_RefEnhancedFeatures() {
    var blk = \-> \-> this
    var blk2 = blk()
    this.assertEquals( this, blk2() )
  }

  function _testNestedThisPointerMethodInvocation1_RefEnhancedFeatures() {
    var blk = \-> \-> this.intFunction()
    var blk2 = blk()
    this.assertEquals( 42, blk2() )
  }
  
  function _testNestedThisPointerMethodInvocation2_RefEnhancedFeatures() {
    var blk = \-> \-> this.refFunction()
    var blk2 = blk()
    this.assertEquals( "foo", blk2() )
  }
  
  function _testNestedThisPointerPropertyInvocation1_RefEnhancedFeatures() {
    var blk = \-> \-> this.IntProperty
    var blk2 = blk()
    this.assertEquals( 42, blk2() )
  }
  
  function _testNestedThisPointerPropertyInvocation2_RefEnhancedFeatures() {
    var blk = \-> \-> this.RefProperty
    var blk2 = blk()
    this.assertEquals( "foo", blk2() )
  }
  
  function _testNestedThisPointerPropertyWriting_RefEnhancedFeatures() {
    this.WritableProp = "foo"
    var blk = \-> \-> { this.WritableProp = "bar" }
    var blk2 = blk()
    blk2()
    this.assertEquals( "bar", this.WritableProp  )
    this.WritableProp = "foo"
    blk2()
    this.assertEquals( "bar", this.WritableProp )
  }
  
  function _testNestedThisPointerFeildReading_RefEnhancedFeatures() {
    this._testField = "foo"
    var blk = \-> \-> this._testField
    var blk2 = blk()
    this.assertEquals( "foo", blk2() )
    this._testField = "bar"
    this.assertEquals( "bar", blk2() )
  }
  
  function _testNestedThisPointerFeildWriting_RefEnhancedFeatures() {
    this._testField = "foo"
    var blk = \-> \-> { this._testField = "bar" }
    var blk2 = blk()
    blk2()
    this.assertEquals( "bar", this._testField  )
    this._testField = "foo"
    blk2()
    this.assertEquals( "bar", this._testField )
  }

  function _testTripleNestedThisPointer_RefEnhancedFeatures() {
    var blk = \-> \-> \-> this
    var blk2 = blk()
    var blk3 = blk2()
    this.assertEquals( this, blk3() )
  }

  function _testTripleNestedThisPointerMethodInvocation1_RefEnhancedFeatures() {
    var blk = \-> \-> \-> this.intFunction()
    var blk2 = blk()
    var blk3 = blk2()
    this.assertEquals( 42, blk3() )
  }
  
  function _testTripleNestedThisPointerMethodInvocation2_RefEnhancedFeatures() {
    var blk = \-> \-> \-> this.refFunction()
    var blk2 = blk()
    var blk3 = blk2()
    this.assertEquals( "foo", blk3() )
  }
  
  function _testTripleNestedThisPointerPropertyInvocation1_RefEnhancedFeatures() {
    var blk = \-> \-> \-> this.intFunction()
    var blk2 = blk()
    var blk3 = blk2()
    this.assertEquals( 42, blk3() )
  }
  
  function _testTripleNestedThisPointerPropertyInvocation2_RefEnhancedFeatures() {
    var blk = \-> \-> \-> this.RefProperty
    var blk2 = blk()
    var blk3 = blk2()
    this.assertEquals( "foo", blk3() )
  }
  
  function _testTripleNestedThisPointerPropertyWriting_RefEnhancedFeatures() {
    this.WritableProp = "foo"
    var blk = \-> \-> \-> { this.WritableProp = "bar" }
    var blk2 = blk()
    var blk3 = blk2()
    blk3()
    this.assertEquals( "bar", this.WritableProp  )
    this.WritableProp = "foo"
    blk3()
    this.assertEquals( "bar", this.WritableProp )
  }
  
  function _testTripleNestedThisPointerFeildReading_RefEnhancedFeatures() {
    this._testField = "foo"
    var blk = \-> \-> \-> this._testField
    var blk2 = blk()
    var blk3 = blk2()
    this.assertEquals( "foo", blk3() )
    this._testField = "bar"
    this.assertEquals( "bar", blk3() )
  }
  
  function _testTripleNestedThisPointerFeildWriting_RefEnhancedFeatures() {
    this._testField = "foo"
    var blk = \-> \-> \-> { this._testField = "bar" }
    var blk2 = blk()
    var blk3 = blk2()
    blk3()
    this.assertEquals( "bar", this._testField  )
    this._testField = "foo"
    blk3()
    this.assertEquals( "bar", this._testField )
  }

  function _testQuadrupleNestedThisPointer_RefEnhancedFeatures() {
    var blk = \-> \-> \->\-> this
    var blk2 = blk()
    var blk3 = blk2()
    var blk4 = blk3()
    this.assertEquals( this, blk4() )
  }

  function _testQuadrupleNestedThisPointerMethodInvocation1_RefEnhancedFeatures() {
    var blk = \-> \-> \->\-> this.intFunction()
    var blk2 = blk()
    var blk3 = blk2()
    var blk4 = blk3()
    this.assertEquals( 42, blk4() )
  }
  
  function _testQuadrupleNestedThisPointerMethodInvocation2_RefEnhancedFeatures() {
    var blk = \-> \-> \->\-> this.refFunction()
    var blk2 = blk()
    var blk3 = blk2()
    var blk4 = blk3()
    this.assertEquals( "foo", blk4() )
  }
  
  function _testQuadrupleNestedThisPointerPropertyInvocation1_RefEnhancedFeatures() {
    var blk = \-> \-> \->\-> this.intFunction()
    var blk2 = blk()
    var blk3 = blk2()
    var blk4 = blk3()
    this.assertEquals( 42, blk4() )
  }
  
  function _testQuadrupleNestedThisPointerPropertyInvocation2_RefEnhancedFeatures() {
    var blk = \-> \-> \->\-> this.RefProperty
    var blk2 = blk()
    var blk3 = blk2()
    var blk4 = blk3()
    this.assertEquals( "foo", blk4() )
  }

  function _testQuadrupleNestedThisPointerPropertyWriting_RefEnhancedFeatures() {
    this.WritableProp = "foo"
    var blk = \-> \-> \->\-> { this.WritableProp = "bar" }
    var blk2 = blk()
    var blk3 = blk2()
    var blk4 = blk3()
    blk4()
    this.assertEquals( "bar", this.WritableProp  )
    this.WritableProp = "foo"
    blk4()
    this.assertEquals( "bar", this.WritableProp )
  }
  
  function _testQuadrupleNestedThisPointerFeildReading_RefEnhancedFeatures() {
    this._testField = "foo"
    var blk = \-> \-> \->\-> this._testField
    var blk2 = blk()
    var blk3 = blk2()
    var blk4 = blk3()
    this.assertEquals( "foo", blk4() )
    this._testField = "bar"
    this.assertEquals( "bar", blk4() )
  }
  
  function _testQuadrupleNestedThisPointerFeildWriting_RefEnhancedFeatures() {
    this._testField = "foo"
    var blk = \-> \-> \->\-> { this._testField = "bar" }
    var blk2 = blk()
    var blk3 = blk2()
    var blk4 = blk3()
    blk4()
    this.assertEquals( "bar", this._testField  )
    this._testField = "foo"
    blk4()
    this.assertEquals( "bar", this._testField )
  }

  //=========================================================
  // implicit this pointer tests w/ features on enhanced
  //=========================================================

//  TODO cgross - enable these tests if we allow unqualified access to enhanced features with enhancements
//
//  function _testImplicitThisPointerMethodInvocation1_RefEnhancedFeatures() {
//    var blk = \-> intFunction()
//    this.assertEquals( 42, blk() )
//  }
//    
//  function _testImplicitThisPointerMethodInvocation2_RefEnhancedFeatures() {
//    var blk = \-> refFunction()
//    this.assertEquals( "foo", blk() )
//  }
//
//  function _testImplicitThisPointerPropertyInvocation1_RefEnhancedFeatures() {
//    var blk = \-> IntProperty
//    this.assertEquals( 42, blk() )
//  }
//    
//  function _testImplicitThisPointerPropertyInvocation2_RefEnhancedFeatures() {
//    var blk = \-> RefProperty
//    this.assertEquals( "foo", blk() )
//  }
//  
//  function _testImplicitThisPointerPropertyWriting_RefEnhancedFeatures() {
//    WritableProp = "foo"
//    var blk = \-> { WritableProp = "bar" }
//    blk()
//    this.assertEquals( "bar", WritableProp  )
//    WritableProp = "foo"
//    blk()
//    this.assertEquals( "bar", WritableProp )
//  }
//  
//  function _testImplicitThisPointerFeildReading_RefEnhancedFeatures() {
//    _testField = "foo"
//    var blk = \-> _testField
//    this.assertEquals( "foo", blk() )
//    _testField = "bar"
//    this.assertEquals( "bar", blk() )
//  }
//  
//  function _testImplicitThisPointerFeildWriting_RefEnhancedFeatures() {
//    _testField = "foo"
//    var blk = \-> { _testField = "bar" }
//    blk()
//    this.assertEquals( "bar", _testField  )
//    _testField = "foo"
//    blk()
//    this.assertEquals( "bar", _testField )
//  }
//  
//  function _testNestedImplicitThisPointerMethodInvocation1_RefEnhancedFeatures() {
//    var blk = \->\-> intFunction()
//    var blk2 = blk()
//    this.assertEquals( 42, blk2() )
//  }
//    
//  function _testNestedImplicitThisPointerMethodInvocation2_RefEnhancedFeatures() {
//    var blk = \->\-> refFunction()
//    var blk2 = blk()
//    this.assertEquals( "foo", blk2() )
//  }
//
//  function _testNestedImplicitThisPointerPropertyInvocation1_RefEnhancedFeatures() {
//    var blk = \->\-> IntProperty
//    var blk2 = blk()
//    this.assertEquals( 42, blk2() )
//  }
//    
//  function _testNestedImplicitThisPointerPropertyInvocation2_RefEnhancedFeatures() {
//    var blk = \->\-> this.RefProperty
//    var blk2 = blk()
//    this.assertEquals( "foo", blk2() )
//  }
//
//  function _testNestedImplicitThisPointerPropertyWriting_RefEnhancedFeatures() {
//    WritableProp = "foo"
//    var blk = \->\-> { WritableProp = "bar" }
//    var blk2 = blk()
//    blk2()
//    this.assertEquals( "bar", WritableProp  )
//    WritableProp = "foo"
//    blk2()
//    this.assertEquals( "bar", WritableProp )
//  }
//  
//  function _testNestedImplicitThisPointerFeildReading_RefEnhancedFeatures() {
//    _testField = "foo"
//    var blk = \->\-> _testField
//    var blk2 = blk()
//    this.assertEquals( "foo", blk2() )
//    _testField = "bar"
//    this.assertEquals( "bar", blk2() )
//  }
//  
//  function _testNestedImplicitThisPointerFeildWriting_RefEnhancedFeatures() {
//    _testField = "foo"
//    var blk = \->\-> { _testField = "bar" }
//    var blk2 = blk()
//    blk2()
//    this.assertEquals( "bar", _testField  )
//    _testField = "foo"
//    blk2()
//    this.assertEquals( "bar", _testField )
//  }
//
//  function _testTripleNestedImplicitThisPointerMethodInvocation1_RefEnhancedFeatures() {
//    var blk = \->\->\-> intFunction()
//    var blk2 = blk()
//    var blk3 = blk2()
//    this.assertEquals( 42, blk3() )
//  }
//    
//  function _testTripleNestedImplicitThisPointerMethodInvocation2_RefEnhancedFeatures() {
//    var blk = \->\->\-> refFunction()
//    var blk2 = blk()
//    var blk3 = blk2()
//    this.assertEquals( "foo", blk3() )
//  }
//
//  function _testTripleNestedImplicitThisPointerPropertyInvocation1_RefEnhancedFeatures() {
//    var blk = \->\->\-> IntProperty
//    var blk2 = blk()
//    var blk3 = blk2()
//    this.assertEquals( 42, blk3() )
//  }
//    
//  function _testTripleNestedImplicitThisPointerPropertyInvocation2_RefEnhancedFeatures() {
//    var blk = \->\->\-> this.RefProperty
//    var blk2 = blk()
//    var blk3 = blk2()
//    this.assertEquals( "foo", blk3() )
//  }
//  
//  function _testTripleNestedImplicitThisPointerPropertyWriting_RefEnhancedFeatures() {
//    WritableProp = "foo"
//    var blk = \->\-> \-> { WritableProp = "bar" }
//    var blk2 = blk()
//    var blk3 = blk2()
//    blk3()
//    this.assertEquals( "bar", WritableProp  )
//    WritableProp = "foo"
//    blk3()
//    this.assertEquals( "bar", WritableProp )
//  }
//  
//  function _testTripleNestedImplicitThisPointerFeildReading_RefEnhancedFeatures() {
//    _testField = "foo"
//    var blk = \->\->\->  _testField
//    var blk2 = blk()
//    var blk3 = blk2()
//    this.assertEquals( "foo", blk3() )
//    _testField = "bar"
//    this.assertEquals( "bar", blk3() )
//  }
//  
//  function _testTripleNestedImplicitThisPointerFeildWriting_RefEnhancedFeatures() {
//    _testField = "foo"
//    var blk = \->\->\-> { _testField = "bar" }
//    var blk2 = blk()
//    var blk3 = blk2()
//    blk3()
//    this.assertEquals( "bar", _testField  )
//    _testField = "foo"
//    blk3()
//    this.assertEquals( "bar", _testField )
//  }
  
  //=========================================================
  // implicit this pointer tests w/ features on enhancement
  //=========================================================

  function _testThisPointerMethodInvocation1_RefEnhancementFeatures() {
    var blk = \-> this.enh_intFunction()
    this.assertEquals( 42, blk() )
  }
  
  function _testThisPointerMethodInvocation2_RefEnhancementFeatures() {
    var blk = \-> this.enh_refFunction()
    this.assertEquals( "foo", blk() )
  }
  
  function _testThisPointerPropertyInvocation1_RefEnhancementFeatures() {
    var blk = \-> this.enh_intFunction()
    this.assertEquals( 42, blk() )
  }
  
  function _testThisPointerPropertyInvocation2_RefEnhancementFeatures() {
    var blk = \-> this.Enh_RefProperty
    this.assertEquals( "foo", blk() )
  }
  
  function _testThisPointerPropertyWriting_RefEnhancementFeatures() {
    this.Enh_WritableProp = "foo"
    var blk = \-> { this.Enh_WritableProp = "bar" }
    blk()
    this.assertEquals( "bar", this.Enh_WritableProp  )
    this.Enh_WritableProp = "foo"
    blk()
    this.assertEquals( "bar", this.Enh_WritableProp )
  }
  
  function _testNestedThisPointerMethodInvocation1_RefEnhancementFeatures() {
    var blk = \-> \-> this.enh_intFunction()
    var blk2 = blk()
    this.assertEquals( 42, blk2() )
  }
  
  function _testNestedThisPointerMethodInvocation2_RefEnhancementFeatures() {
    var blk = \-> \-> this.enh_refFunction()
    var blk2 = blk()
    this.assertEquals( "foo", blk2() )
  }
  
  function _testNestedThisPointerPropertyInvocation1_RefEnhancementFeatures() {
    var blk = \-> \-> this.Enh_IntProperty
    var blk2 = blk()
    this.assertEquals( 42, blk2() )
  }
  
  function _testNestedThisPointerPropertyInvocation2_RefEnhancementFeatures() {
    var blk = \-> \-> this.Enh_RefProperty
    var blk2 = blk()
    this.assertEquals( "foo", blk2() )
  }
  
  function _testNestedThisPointerPropertyWriting_RefEnhancementFeatures() {
    this.Enh_WritableProp = "foo"
    var blk = \-> \-> { this.Enh_WritableProp = "bar" }
    var blk2 = blk()
    blk2()
    this.assertEquals( "bar", this.Enh_WritableProp  )
    this.Enh_WritableProp = "foo"
    blk2()
    this.assertEquals( "bar", this.Enh_WritableProp )
  }

  function _testTripleNestedThisPointerMethodInvocation1_RefEnhancementFeatures() {
    var blk = \-> \-> \-> this.enh_intFunction()
    var blk2 = blk()
    var blk3 = blk2()
    this.assertEquals( 42, blk3() )
  }
  
  function _testTripleNestedThisPointerMethodInvocation2_RefEnhancementFeatures() {
    var blk = \-> \-> \-> this.enh_refFunction()
    var blk2 = blk()
    var blk3 = blk2()
    this.assertEquals( "foo", blk3() )
  }
  
  function _testTripleNestedThisPointerPropertyInvocation1_RefEnhancementFeatures() {
    var blk = \-> \-> \-> this.enh_intFunction()
    var blk2 = blk()
    var blk3 = blk2()
    this.assertEquals( 42, blk3() )
  }
  
  function _testTripleNestedThisPointerPropertyInvocation2_RefEnhancementFeatures() {
    var blk = \-> \-> \-> this.Enh_RefProperty
    var blk2 = blk()
    var blk3 = blk2()
    this.assertEquals( "foo", blk3() )
  }
  
  function _testTripleNestedThisPointerPropertyWriting_RefEnhancementFeatures() {
    this.Enh_WritableProp = "foo"
    var blk = \-> \-> \-> { this.Enh_WritableProp = "bar" }
    var blk2 = blk()
    var blk3 = blk2()
    blk3()
    this.assertEquals( "bar", this.Enh_WritableProp  )
    this.Enh_WritableProp = "foo"
    blk3()
    this.assertEquals( "bar", this.Enh_WritableProp )
  }

  function _testQuadrupleNestedThisPointerMethodInvocation1_RefEnhancementFeatures() {
    var blk = \-> \-> \->\-> this.enh_intFunction()
    var blk2 = blk()
    var blk3 = blk2()
    var blk4 = blk3()
    this.assertEquals( 42, blk4() )
  }
  
  function _testQuadrupleNestedThisPointerMethodInvocation2_RefEnhancementFeatures() {
    var blk = \-> \-> \->\-> this.enh_refFunction()
    var blk2 = blk()
    var blk3 = blk2()
    var blk4 = blk3()
    this.assertEquals( "foo", blk4() )
  }
  
  function _testQuadrupleNestedThisPointerPropertyInvocation1_RefEnhancementFeatures() {
    var blk = \-> \-> \->\-> this.enh_intFunction()
    var blk2 = blk()
    var blk3 = blk2()
    var blk4 = blk3()
    this.assertEquals( 42, blk4() )
  }
  
  function _testQuadrupleNestedThisPointerPropertyInvocation2_RefEnhancementFeatures() {
    var blk = \-> \-> \->\-> this.Enh_RefProperty
    var blk2 = blk()
    var blk3 = blk2()
    var blk4 = blk3()
    this.assertEquals( "foo", blk4() )
  }

  function _testQuadrupleNestedThisPointerPropertyWriting_RefEnhancementFeatures() {
    this.Enh_WritableProp = "foo"
    var blk = \-> \-> \->\-> { this.Enh_WritableProp = "bar" }
    var blk2 = blk()
    var blk3 = blk2()
    var blk4 = blk3()
    blk4()
    this.assertEquals( "bar", this.Enh_WritableProp  )
    this.Enh_WritableProp = "foo"
    blk4()
    this.assertEquals( "bar", this.Enh_WritableProp )
  }

  //=========================================================
  // implicit this pointer tests  w/ features on enhancement
  //=========================================================

  function _testImplicitThisPointerMethodInvocation1_RefEnhancementFeatures() {
    var blk = \-> enh_intFunction()
    this.assertEquals( 42, blk() )
  }
    
  function _testImplicitThisPointerMethodInvocation2_RefEnhancementFeatures() {
    var blk = \-> enh_refFunction()
    this.assertEquals( "foo", blk() )
  }

  function _testImplicitThisPointerPropertyInvocation1_RefEnhancementFeatures() {
    var blk = \-> Enh_IntProperty
    this.assertEquals( 42, blk() )
  }
    
  function _testImplicitThisPointerPropertyInvocation2_RefEnhancementFeatures() {
    var blk = \-> Enh_RefProperty
    this.assertEquals( "foo", blk() )
  }
  
  function _testImplicitThisPointerPropertyWriting_RefEnhancementFeatures() {
    Enh_WritableProp = "foo"
    var blk = \-> { Enh_WritableProp = "bar" }
    blk()
    this.assertEquals( "bar", Enh_WritableProp  )
    Enh_WritableProp = "foo"
    blk()
    this.assertEquals( "bar", Enh_WritableProp )
  }
    
  function _testNestedImplicitThisPointerMethodInvocation1_RefEnhancementFeatures() {
    var blk = \->\-> enh_intFunction()
    var blk2 = blk()
    this.assertEquals( 42, blk2() )
  }
    
  function _testNestedImplicitThisPointerMethodInvocation2_RefEnhancementFeatures() {
    var blk = \->\-> enh_refFunction()
    var blk2 = blk()
    this.assertEquals( "foo", blk2() )
  }

  function _testNestedImplicitThisPointerPropertyInvocation1_RefEnhancementFeatures() {
    var blk = \->\-> Enh_IntProperty
    var blk2 = blk()
    this.assertEquals( 42, blk2() )
  }
    
  function _testNestedImplicitThisPointerPropertyInvocation2_RefEnhancementFeatures() {
    var blk = \->\-> this.Enh_RefProperty
    var blk2 = blk()
    this.assertEquals( "foo", blk2() )
  }

  function _testNestedImplicitThisPointerPropertyWriting_RefEnhancementFeatures() {
    Enh_WritableProp = "foo"
    var blk = \->\-> { Enh_WritableProp = "bar" }
    var blk2 = blk()
    blk2()
    this.assertEquals( "bar", Enh_WritableProp  )
    Enh_WritableProp = "foo"
    blk2()
    this.assertEquals( "bar", Enh_WritableProp )
  }
  
  function _testTripleNestedImplicitThisPointerMethodInvocation1_RefEnhancementFeatures() {
    var blk = \->\->\-> enh_intFunction()
    var blk2 = blk()
    var blk3 = blk2()
    this.assertEquals( 42, blk3() )
  }
    
  function _testTripleNestedImplicitThisPointerMethodInvocation2_RefEnhancementFeatures() {
    var blk = \->\->\-> enh_refFunction()
    var blk2 = blk()
    var blk3 = blk2()
    this.assertEquals( "foo", blk3() )
  }

  function _testTripleNestedImplicitThisPointerPropertyInvocation1_RefEnhancementFeatures() {
    var blk = \->\->\-> Enh_IntProperty
    var blk2 = blk()
    var blk3 = blk2()
    this.assertEquals( 42, blk3() )
  }
    
  function _testTripleNestedImplicitThisPointerPropertyInvocation2_RefEnhancementFeatures() {
    var blk = \->\->\-> this.Enh_RefProperty
    var blk2 = blk()
    var blk3 = blk2()
    this.assertEquals( "foo", blk3() )
  }
  
  function _testTripleNestedImplicitThisPointerPropertyWriting_RefEnhancementFeatures() {
    Enh_WritableProp = "foo"
    var blk = \->\-> \-> { Enh_WritableProp = "bar" }
    var blk2 = blk()
    var blk3 = blk2()
    blk3()
    this.assertEquals( "bar", Enh_WritableProp  )
    Enh_WritableProp = "foo"
    blk3()
    this.assertEquals( "bar", Enh_WritableProp )
  }
  
}
