package gw.lang.spec_old.classes
uses gw.test.TestClass

class CoreClassTest extends TestClass {

  function testStaticPropertyOverridingSetterProperlyWorks() {
    assertEquals( "init", SampleClass.StaticProp1 )
    SampleClass.StaticProp1 = "test"
    assertEquals( "test set through explicit setter", SampleClass.StaticProp1 )    
  }
  
  function testStaticPropertyOverridingGetterProperlyWorks() {
    assertEquals( "init got through explicit getter", SampleClass.StaticProp2 )
    SampleClass.StaticProp2 = "test"
    assertEquals( "test got through explicit getter", SampleClass.StaticProp2 )    
  }

  function testStaticPropertyOverridingGetterAndSetterProperlyWorks() {
    assertEquals( "init got through explicit getter", SampleClass.StaticProp3 )
    SampleClass.StaticProp3 = "test"
    assertEquals( "test set through explicit setter got through explicit getter", SampleClass.StaticProp3 )    
  }

  function testStaticPropertyOverridingSetterProperlyWorksEvenIfSetterIsDefinedBeforeField() {
    assertEquals( "init", SampleClass.StaticProp4 )
    SampleClass.StaticProp4 = "test"
    assertEquals( "test set through explicit setter", SampleClass.StaticProp4 )    
  }

  function testPropertyOverridingSetterProperlyWorks() {
    var sc = new SampleClass()
    assertEquals( "init", sc.Prop1 )
    sc.Prop1 = "test"
    assertEquals( "test set through explicit setter", sc.Prop1 )    
  }
  
  function testPropertyOverridingGetterProperlyWorks() {
    var sc = new SampleClass()
    assertEquals( "init got through explicit getter", sc.Prop2 )
    sc.Prop2 = "test"
    assertEquals( "test got through explicit getter", sc.Prop2 )    
  }

  function testPropertyOverridingGetterAndSetterProperlyWorks() {
    var sc = new SampleClass()
    assertEquals( "init got through explicit getter", sc.Prop3 )
    sc.Prop3 = "test"
    assertEquals( "test set through explicit setter got through explicit getter", sc.Prop3 )    
  }

  function testPropertyOverridingSetterProperlyWorksEvenIfSetterIsDefinedBeforeField() {
    var sc = new SampleClass()
    assertEquals( "init", sc.Prop4 )
    sc.Prop4 = "test"
    assertEquals( "test set through explicit setter", sc.Prop4 )    
  }

}
