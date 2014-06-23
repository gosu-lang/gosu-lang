package gw.internal.gosu.parser.classTests.gwtest.modifiers
uses gw.testharness.DoNotVerifyResource
uses java.lang.Runnable

@DoNotVerifyResource
class BadProperties { 
   
  static property get GetterStaticSetterNot() : String { return null }
  property set GetterStaticSetterNot( s : String ) {} 
  
  property set GetterStaticSetterNot2( s : String ) {} 
  static property get GetterStaticSetterNot2() : String { return null }
  
  static property get SetterStaticGetterNot() : String { return null }
  property set SetterStaticGetterNot( s : String ) {}
 
  property set SetterStaticGetterNot2( s : String ) {}
  static property get SetterStaticGetterNot2() : String { return null }
 
  static var _s1 : String as GetterOnStaticFieldSetterNot 
  property set GetterOnStaticFieldSetterNot( s : String ) {}
    
  property set GetterOnStaticFieldSetterNot2( s : String ) {}
  static var _s3 : String as GetterOnStaticFieldSetterNot2 

  static var _s2 : String as SetterOnStaticFieldGetterNot
  property get SetterOnStaticFieldGetterNot() : String { return null }

  property get SetterOnStaticFieldGetterNot2() : String { return null }
  static var _s4 : String as SetterOnStaticFieldGetterNot2

  property get SetterAndGetterDisagreeOnType() : String { return null }
  property set SetterAndGetterDisagreeOnType( s : Runnable ) {}

  property set SetterAndGetterDisagreeOnType2( s : Runnable ) {}
  property get SetterAndGetterDisagreeOnType2() : String { return null }

  var _s5 : String as VarSetterAndGetterDisagreeOnType 
  property set VarSetterAndGetterDisagreeOnType( s : Runnable ) {}
    
  property set VarGetterOnStaticFieldSetterNot2( s : Runnable ) {}
  var _s6 : String as VarGetterOnStaticFieldSetterNot2 

  var _s7 : String as VarSetterOnStaticFieldGetterNot
  property get VarSetterOnStaticFieldGetterNot() : Runnable { return null }

  property get VarSetterOnStaticFieldGetterNot2() : Runnable { return null }
  var _s8 : String as VarSetterOnStaticFieldGetterNot2

}
