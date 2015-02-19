package gw.internal.gosu.compiler.featureliteral

uses gw.test.TestClass
uses gw.testharness.DoNotVerifyResource
uses gw.lang.reflect.features.IMethodReference

@DoNotVerifyResource
class CoreFeatureLiteralTest extends TestClass {

  function testBasicInstanceFunctionWorks() {
    var f = FeatureLiteralClass#instFunc1()
    assertNotNull( f )
    assertEquals( FeatureLiteralClass, f.RootType )
    assertEquals( "doh", f.invoke(new FeatureLiteralClass()) )
  }

  function testOverloadedInstanceFunctionWork() {
    var f1 = FeatureLiteralClass#instFunc2(Object) 
    assertNotNull( f1 )
    assertEquals( FeatureLiteralClass, f1.RootType )
    assertEquals( "foo", f1.invoke(new FeatureLiteralClass(), null) )

    var f2 = FeatureLiteralClass#instFunc2(String)
    assertNotNull( f2 )
    assertEquals( FeatureLiteralClass, f2.RootType )
    assertEquals( "bar", f2.invoke(new FeatureLiteralClass(), null) )
  }
 
  function testGenericInstanceFunctionWorks() {
    var f = FeatureLiteralClass<String>#instFunc3()
    assertNotNull( f )
    assertEquals( FeatureLiteralClass<String>, f.RootType )
  }

  function testBoundBasicInstanceFunctionWorks() {
    var i = new FeatureLiteralClass()
    var f = i#instFunc1()
    assertNotNull( f )
    assertEquals( FeatureLiteralClass, f.RootType )
    assertEquals( i, f.Ctx )
    assertEquals( "doh", f.invoke() )
  }

  function testBoundGenericInstanceFunctionWorks() {
    var i = new FeatureLiteralClass<String>("blahblah")
    var f = i#instFunc3()
    assertNotNull( f )
    assertEquals( FeatureLiteralClass<String>, f.RootType )
    assertEquals( i, f.Ctx )
    assertEquals( "blahblah", f.invoke() )
  }

  function testBasicStaticFunctionWorks() {
    var f = FeatureLiteralClass#staticInstFunc1()
    assertNotNull( f )
    assertEquals( FeatureLiteralClass, f.RootType )
    assertEquals( "doh", f.invoke() )
  }

  function testOverloadedStaticFunctionWork() {
    var f1 = FeatureLiteralClass#staticInstFunc2(Object) 
    assertNotNull( f1 )
    assertEquals( FeatureLiteralClass, f1.RootType )
    assertEquals( "foo", f1.invoke(null) )

    var f2 = FeatureLiteralClass#staticInstFunc2(String)
    assertNotNull( f2 )
    assertEquals( FeatureLiteralClass, f2.RootType )
    assertEquals( "bar", f2.invoke(null) )
  }
  
  function testPrivateBasicInstanceFunctionWorks() {
    var f = FeatureLiteralClass#privateInstFunc1()
    assertNotNull( f )
    assertEquals( FeatureLiteralClass, f.RootType )
    assertEquals( "doh", f.invoke(new FeatureLiteralClass()) )
  }

  function testPrivateOverloadedInstanceFunctionWork() {
    var f1 = FeatureLiteralClass#privateInstFunc2(Object) 
    assertNotNull( f1 )
    assertEquals( FeatureLiteralClass, f1.RootType )
    assertEquals( "foo", f1.invoke(new FeatureLiteralClass(), null) )

    var f2 = FeatureLiteralClass#privateInstFunc2(String)
    assertNotNull( f2 )
    assertEquals( FeatureLiteralClass, f2.RootType )
    assertEquals( "bar", f2.invoke(new FeatureLiteralClass(), null) )
  }
 
  function testPrivateGenericInstanceFunctionWorks() {
    var f = FeatureLiteralClass<String>#privateInstFunc3()
    assertNotNull( f )
    assertEquals( FeatureLiteralClass<String>, f.RootType )
  }

  function testPrivateBoundBasicInstanceFunctionWorks() {
    var i = new FeatureLiteralClass()
    var f = i#privateInstFunc1()
    assertNotNull( f )
    assertEquals( FeatureLiteralClass, f.RootType )
    assertEquals( i, f.Ctx )
    assertEquals( "doh", f.invoke() )
  }

  function testPrivateBoundGenericInstanceFunctionWorks() {
    var i = new FeatureLiteralClass<String>("blahblah")
    var f = i#privateInstFunc3()
    assertNotNull( f )
    assertEquals( FeatureLiteralClass<String>, f.RootType )
    assertEquals( i, f.Ctx )
    assertEquals( "blahblah", f.invoke() )
  }

  function testPrivateBasicStaticFunctionWorks() {
    var f = FeatureLiteralClass#privateStaticInstFunc1()
    assertNotNull( f )
    assertEquals( FeatureLiteralClass, f.RootType )
    assertEquals( "doh", f.invoke() )
  }

  function testPrivateOverloadedStaticFunctionWork() {
    var f1 = FeatureLiteralClass#privateStaticInstFunc2(Object) 
    assertNotNull( f1 )
    assertEquals( FeatureLiteralClass, f1.RootType )
    assertEquals( "foo", f1.invoke(null) )

    var f2 = FeatureLiteralClass#privateStaticInstFunc2(String)
    assertNotNull( f2 )
    assertEquals( FeatureLiteralClass, f2.RootType )
    assertEquals( "bar", f2.invoke(null) )
  }
  
  function testBasicInstancePropertyWorks() {
    var f = FeatureLiteralClass#Tee
    assertNotNull( f )
    assertEquals( FeatureLiteralClass, f.RootType )
    var flc = new FeatureLiteralClass("doh")
    assertEquals( "doh", f.get(flc) )    
    f.set(flc, true)
    assertEquals( true, f.get(flc) )    

    var f2 = FeatureLiteralClass<String>#Tee
    assertNotNull( f2 )
    assertEquals( FeatureLiteralClass<String>, f2.RootType )
    var flc2 = new FeatureLiteralClass<String>("doh")
    assertEquals( "doh", f2.get(flc2) )    
    f2.set(flc2, "bar")
    assertEquals( "bar", f2.get(flc2) )    
  }
  
  function testBasicBoundInstancePropertyWorks() {
    var flc = new FeatureLiteralClass("doh")
    var f = flc#Tee
    assertNotNull( f )
    assertEquals( FeatureLiteralClass, f.RootType )
    assertEquals( "doh", f.get() )    
    f.set(true)
    assertEquals( true, f.get() )    

    var flc2 = new FeatureLiteralClass<String>("doh")
    var f2 = flc2#Tee
    assertNotNull( f2 )
    assertEquals( FeatureLiteralClass<String>, f2.RootType )
    assertEquals( "doh", f2.get() )    
    f2.set("bar")
    assertEquals( "bar", f2.get() )    
  }

  function testBasicStaticPropertyWorks() {
    FeatureLiteralClass.StaticStringProp = "doh"
    var f = FeatureLiteralClass#StaticStringProp
    assertNotNull( f )
    assertEquals( FeatureLiteralClass, f.RootType )
    assertEquals( "doh", f.get(null) )
    f.set(null, "bar")
    assertEquals( "bar", f.get(null) )
  }

  function testPrivateBasicInstancePropertyWorks() {
    var f = FeatureLiteralClass#PrivateTee
    assertNotNull( f )
    assertEquals( FeatureLiteralClass, f.RootType )
    var flc = new FeatureLiteralClass("doh")
    assertEquals( "doh", f.get(flc) )    
    f.set(flc, true)
    assertEquals( true, f.get(flc) )    

    var f2 = FeatureLiteralClass<String>#PrivateTee
    assertNotNull( f2 )
    assertEquals( FeatureLiteralClass<String>, f2.RootType )
    var flc2 = new FeatureLiteralClass<String>("doh")
    assertEquals( "doh", f2.get(flc2) )    
    f2.set(flc2, "bar")
    assertEquals( "bar", f2.get(flc2) )    
  }
  
  function testPrivateBasicBoundInstancePropertyWorks() {
    var flc = new FeatureLiteralClass("doh")
    var f = flc#PrivateTee
    assertNotNull( f )
    assertEquals( FeatureLiteralClass, f.RootType )
    assertEquals( "doh", f.get() )    
    f.set(true)
    assertEquals( true, f.get() )    

    var flc2 = new FeatureLiteralClass<String>("doh")
    var f2 = flc2#PrivateTee
    assertNotNull( f2 )
    assertEquals( FeatureLiteralClass<String>, f2.RootType )
    assertEquals( "doh", f2.get() )    
    f2.set("bar")
    assertEquals( "bar", f2.get() )    
  }

  function testPrivateBasicStaticPropertyWorks() {
    FeatureLiteralClass.StaticStringProp = "doh"
    var f = FeatureLiteralClass#PrivateStaticStringProp
    assertNotNull( f )
    assertEquals( FeatureLiteralClass, f.RootType )
    assertEquals( "doh", f.get(null) )
    f.set(null, "bar")
    assertEquals( "bar", f.get(null) )
  }
  
  function testBasicConstructorReference() {
     var cr = FeatureLiteralClass#construct()
     var val = cr.invoke()
     assertNotNull( val )

     var cr2 = FeatureLiteralClass#construct(Object)
     var val2 = cr2.invoke("foo")
     assertNotNull( val2 )
     assertEquals( "foo", val2.Tee )
     
     var cr3 = FeatureLiteralClass#construct(boolean)
     var val3 = cr3.invoke(false)
     assertNotNull( val3 )
  }
  
  function testPrivateBasicConstructorReference() {
     var cr = FeatureLiteralClass#construct(boolean, boolean)
     var val = cr.invoke(false, false)
     assertNotNull( val )
  }

  function testBasicSimplePropertyChaningWithPropRoot() {
    var mc = FeatureLiteralClass<String>#ThisProp#Tee
    var flc = new FeatureLiteralClass<String>("doh")
    assertEquals( "doh", mc.get( flc ) )
    mc.set( flc,  "bar" )
    assertEquals( "bar", mc.get( flc ) )
  } 
  
  function testBasicBoundSimplePropChaningWithPropRoot() {
    var mc = new FeatureLiteralClass<String>("doh")#ThisProp#Tee
    assertEquals( "doh", mc.get() )
  }

  function testBasicSimplePropChaningWithStaticPropRoot() {
    var mc = FeatureLiteralClass#StaticNewProp#Tee
    assertEquals( null, mc.get(null) )
  }

  function testBasicBoundPropertyChaning() {
    var mc = new FeatureLiteralClass<String>("bar")#ThisProp#Tee
    assertEquals( "bar", mc.get() )
  }

  function testBasicFunctionBoundArguments() {
     var fl = FeatureLiteralClass#instFunc5(null, "foo", false)
     assertEquals( {null, "foo", false}, fl.invoke(new FeatureLiteralClass() ) )

     var fl2 = new FeatureLiteralClass()#instFunc5(null, "foo", false)
     assertEquals( {null, "foo", false}, fl2.invoke() )
  }

  function testAnnotationHasFeatures() {
    var fa = FeatureLiteralClass.Type.TypeInfo.Annotations.map( \ ai -> ai.Instance ).whereTypeIs(FeatureAnnotation).first()
    assertEquals( 3, fa.Features.Count ) 
  }
  
  function testRelativeReferencesWorks() {
    new FeatureLiteralClass().testRelativeReferences() 
  }

  function testInnerClassRefsWor() {
    var mr = FeatureLiteralClass.Inner#innerFunc()
    assertEquals( "inner", mr.invoke( new FeatureLiteralClass().makeInner() ) )
  }

  function testBadCaseCausesErrors() {
    assertFalse(Errant_HasBadCaseInFeatureLiteral.Type.Valid)
    var pes = Errant_HasBadCaseInFeatureLiteral.Type.ParseResultsException.ParseExceptions
    for(line in 6..11) {
      assertTrue(pes.hasMatch( \ pe -> pe.Line == line ))
    }
  }

  // spooky
  function featureLiteralsWorkInPrograms() {
    var src = "function foo() : Object { return #foo() }\n" +
              " return foo()"
    var mr = eval( src ) as IMethodReference
    assertEquals( mr, mr.evaluate({}) ) 
  }

  // spookier
  function featureLiteralsWorkInPrograms2() {
    var src = "class Foo { function bar() : Object { return #foo() } }\n" +
              "return {new Foo(), Foo#bar()}"
    var lst = eval( src ) as List
    var obj = lst.first()
    var mr = lst[1] as IMethodReference
    assertEquals( mr, mr.evaluate({obj}) ) 
  }
  
  class Foo {
    var _bar : Bar as BarProp
  }
  
  class Bar {
    var _str : String as StringProp
  }
  
  function testChainedPropsWorkOnInnerClasses() {
    var f = new Foo() { :BarProp = new Bar() }
  
    var pr = Foo#BarProp#StringProp
    assertNull( f.BarProp.StringProp )
    pr.set(f, "val1")
    assertEquals( "val1", f.BarProp.StringProp )
    assertEquals( "val1", pr.get(f) )

    var bpr = f#BarProp#StringProp
    f.BarProp.StringProp = null
    assertNull( f.BarProp.StringProp )
    bpr.set("val1")
    assertEquals( "val1", f.BarProp.StringProp )
    assertEquals( "val1", bpr.get() )
  }
}