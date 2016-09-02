package gw.specContrib.enhancements

uses java.time.LocalDate
uses gw.specContrib.classes.property_Declarations.new_syntax.MyMethodAnno

enhancement ReceiverTestClassEnh : ReceiverTestClass
{
  @MyMethodAnno(1)
  @receiver:MyReceiverAnno( LocalDate )
  function good_enhFunc() : String {
     return "nonstatic" 
  }
  
  function errant_enhFuncNoAnno() : String {
     return "nonstatic" 
  }
}