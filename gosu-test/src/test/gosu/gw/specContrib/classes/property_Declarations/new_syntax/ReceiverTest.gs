package gw.specContrib.classes.property_Declarations.new_syntax

uses gw.test.TestClass
uses java.time.LocalDate
uses gw.specContrib.classes.property_Declarations.new_syntax.abc.TargetModifierClass

class ReceiverTest extends TestClass 
{
  function testReceiver()
  {
    var cls: Class = TargetModifierClassEnh
    var m = cls.getDeclaredMethod( "setEnhProp", {TargetModifierClass, String} )
    var anno = m.Parameters[0].Annotations[0]
    assertEquals( 1, (anno as MyParamAnno).value() )
    anno = m.Parameters[1].Annotations[0]
    assertEquals( 2, (anno as MyParamAnno).value() )
  }
}