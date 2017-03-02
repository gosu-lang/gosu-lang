package gw.specContrib.enhancements
uses java.time.LocalDate
uses gw.specContrib.classes.property_Declarations.new_syntax.MyMethodAnno

enhancement Errant_ReceiverEnh : ReceiverTestClass
{
  @receiver:MyMethodAnno(1)  //## issuekeys: MSG_ANNOTATION_WHEN_NONE_ALLOWED
  @receiver:MyReceiverAnno( LocalDate )
  function enhFunc() : String {
     return "nonstatic" 
  }
  
  function enhFuncNoAnno() : String {
     return "nonstatic" 
  }
     
  @receiver:MyReceiverAnno( LocalDate )  //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  static function staticFunc() : String {
     return "static" 
  }
}
