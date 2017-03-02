package gw.specContrib.classes.property_Declarations.new_syntax
uses gw.specContrib.classes.property_Declarations.new_syntax.abc.TargetModifierClass

class Errant_TargetModifierClass {
  function foo() {
     var test = new TargetModifierClass()
     
     test.PublicField = 1
     print( test.PublicField )
     print( test._PublicField ) 
     
     test.ProtectedField = 1
     print( test.ProtectedField )
     print( test._ProtectedField )   //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND
     
     test.ProtectedGet = 1
     print( test.ProtectedGet )  //## issuekeys: MSG_CLASS_PROPERTY_NOT_READABLE
     print( test._ProtectedGet )   //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND
     
     test.ProtectedSet = 1  //## issuekeys: MSG_CLASS_PROPERTY_NOT_WRITABLE
     print( test.ProtectedSet )
     print( test._ProtectedSet )   //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND
     
     test.ProtectedAccessors = 1  //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND, MSG_CLASS_PROPERTY_NOT_WRITABLE
     print( test.ProtectedAccessors )  //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND, MSG_CLASS_PROPERTY_NOT_READABLE
     print( test._ProtectedAccessors )   //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND
     
     
  }
  
  static class Subclass extends TargetModifierClass {
    function bar() {
       var test = new Subclass()
     
       test.PublicField = 1
       print( test.PublicField )
       print( test._PublicField ) 

       test.ProtectedField = 1
       print( test.ProtectedField )
       print( test._ProtectedField ) 
     
       test.ProtectedGet = 1
       print( test.ProtectedGet )
       print( test._ProtectedGet )   //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND
     
       test.ProtectedSet = 1
       print( test.ProtectedSet )
       print( test._ProtectedSet )   //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND
     
       test.ProtectedAccessors = 1
       print( test.ProtectedAccessors )
       print( test._ProtectedAccessors )   //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND
    }
  }
}
