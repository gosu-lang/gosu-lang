package gw.specification.classes.the_Member_Access_Modifiers

uses gw.specification.classes.the_Member_Access_Modifiers.p0.DefaultAccessC0

class Errant_DefaultAccess {

  function testField(){
    var x = (new DefaultAccessC0()).f1  //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND
    var y = DefaultAccessC0.f2  //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND
  }

  function testMethod(){
    (new DefaultAccessC0()).m1()
    DefaultAccessC0.m2()
    (new DefaultAccessC0()).m3()
  }

  function testConstructor(){
    var x = new DefaultAccessC0()
  }

  function testProperty(){
    var x = (new DefaultAccessC0()).Prop1
    var y = DefaultAccessC0.Prop2
    var z = (new DefaultAccessC0()).Prop3
    var a = DefaultAccessC0.Prop4
    var b = (new DefaultAccessC0()).Prop5
    var c = DefaultAccessC0.Prop6
  }

  function testEnum(){
    var x = gw.specification.classes.the_Member_Access_Modifiers.p0.DefaultAccessC0.enumX.TEST
    var y = gw.specification.classes.the_Member_Access_Modifiers.p0.DefaultAccessC0.enumY.TEST
  }

  function testAnnotation(){
    var x = gw.specification.classes.the_Member_Access_Modifiers.p0.DefaultAccessC0.MyAnno  //## KB(IDE-1908)
    var y = gw.specification.classes.the_Member_Access_Modifiers.p0.DefaultAccessC0.MyAnno1  //## KB(IDE-1908)

  }

  function testDelegate(){
    var x = new DefaultAccessC0()
    var y = x.methodThruDelegate1()
    var z = (new DefaultAccessC0()).delegate1
  }


  function testInnerClass(){
    var x = (new DefaultAccessC0()).new innerC1()  //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND, MSG_NO_SUCH_FUNCTION
    var y = gw.specification.classes.the_Member_Access_Modifiers.p0.DefaultAccessC0.innerC1
    var z = new gw.specification.classes.the_Member_Access_Modifiers.p0.DefaultAccessC0.innerC2 ()
  }

  // Testing 'nested interface'
  class testInnerInterfaceAccess1 implements gw.specification.classes.the_Member_Access_Modifiers.p0.DefaultAccessC0.innerI1 {}  //## KB(IDE-1908)
  class testInnerInterfaceAccess2 implements gw.specification.classes.the_Member_Access_Modifiers.p0.DefaultAccessC0.innerI2 {}  //## KB(IDE-1908)
  function testInnerInterface(){
    var x = new testInnerInterfaceAccess1()
    var y = gw.specification.classes.the_Member_Access_Modifiers.p0.DefaultAccessC0.innerI1  //## KB(IDE-1908)
    var z = new testInnerInterfaceAccess2()
    var a = gw.specification.classes.the_Member_Access_Modifiers.p0.DefaultAccessC0.innerI2  //## KB(IDE-1908)
  }

  // Testing Gosu 'nested structure'
  class ClassTransformer{  function transform(){}   }
  function testStructure(){
    var x : gw.specification.classes.the_Member_Access_Modifiers.p0.DefaultAccessC0.Transformer1 =  new ClassTransformer()  //## KB(IDE-1920)
    var y : gw.specification.classes.the_Member_Access_Modifiers.p0.DefaultAccessC0.Transformer2 =  new ClassTransformer()  //## KB(IDE-1920)

  }
}