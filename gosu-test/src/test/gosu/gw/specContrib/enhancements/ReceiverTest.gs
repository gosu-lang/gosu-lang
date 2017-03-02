package gw.specContrib.enhancements
uses gw.test.TestClass
uses java.time.LocalDate

class ReceiverTest extends TestClass 
{
  function testReceiver()
  {
    var cls: Class = ReceiverTestClassEnh
    var m = cls.getDeclaredMethod( "good_enhFunc", {ReceiverTestClass} )
    var anno = m.Parameters[0].Annotations[0]
    assertEquals( LocalDate as Class, (anno as MyReceiverAnno).value() )
  }
}