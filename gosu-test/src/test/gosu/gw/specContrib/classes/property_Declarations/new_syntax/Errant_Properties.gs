package gw.specContrib.classes.property_Declarations.new_syntax

abstract class Errant_Properties {
  // instance

  property Both: String
  property Both_Init: String = "hi"
  property Both_Init_Err: String = 4  //## issuekeys: MSG_IMPLICIT_COERCION_ERROR

  property get Get_Only: String
  property get Get_Only_Init: String = "hi"
  property get Get_Only_Init_Err: String = 5  //## issuekeys: MSG_IMPLICIT_COERCION_ERROR

  property set Set_Only: String
  property set Set_Only_Init: String = "hi"
  property set Set_Only_Init_Err: String = 4  //## issuekeys: MSG_IMPLICIT_COERCION_ERROR

  abstract property AbsProp: String = "bad"  //## issuekeys: MSG_INITIALIZER_NOT_ALLOWED_ABSTRACT_PROPERTY
  abstract property get GAbsProp: String = "bad"  //## issuekeys: MSG_INITIALIZER_NOT_ALLOWED_ABSTRACT_PROPERTY
  abstract property set SAbsProp: String = "bad"  //## issuekeys: MSG_INITIALIZER_NOT_ALLOWED_ABSTRACT_PROPERTY

  // dup

  property Both_Dup: String
  property Both_Dup: String  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED, MSG_PROPERTY_ALREADY_DEFINED

  property get Get_Only_Dup: String
  property get Get_Only_Dup: String  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED, MSG_PROPERTY_ALREADY_DEFINED

  property set Set_Only_Dup: String
  property set Set_Only_Dup: String  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED, MSG_PROPERTY_ALREADY_DEFINED

  property Both_Dup_G: String
  property get Both_Dup_G: String  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED, MSG_PROPERTY_ALREADY_DEFINED

  property Both_Dup_S: String
  property set Both_Dup_S: String  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED, MSG_PROPERTY_ALREADY_DEFINED

  property get Get_Only_Dup_G: String
  property Get_Only_Dup_G: String  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED, MSG_PROPERTY_ALREADY_DEFINED

  property set Get_Only_Dup_S: String
  property Get_Only_Dup_S: String  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED, MSG_PROPERTY_ALREADY_DEFINED

  property Both_Dup_MethodOverride: String
  property get Both_Dup_MethodOverride(): String { return "" }
  property set Both_Dup_MethodOverride(v: String) {}

  property get Get_Dup_MethodOverride: String
  property get Get_Dup_MethodOverride(): String { return "" }
  property set Get_Dup_MethodOverride(v: String) {}

  property set Set_Dup_MethodOverride: String
  property get Set_Dup_MethodOverride(): String { return "" }
  property set Set_Dup_MethodOverride(v: String) {}

  property Both_Dup_MethodOverride_Disagree: Object  //## issuekeys: MSG_PROPERTIES_MUST_AGREE_ON_TYPE
  property get Both_Dup_MethodOverride_Disagree(): String { return "" }  //## issuekeys: MSG_PROPERTIES_MUST_AGREE_ON_TYPE
  property set Both_Dup_MethodOverride_Disagree(v: String) {}  //## issuekeys: MSG_PROPERTIES_MUST_AGREE_ON_TYPE


  // static

  static property S_Both: String
  static property S_Both_Init: String = "hi"
  static property S_Both_Init_Err: String = 4  //## issuekeys: MSG_IMPLICIT_COERCION_ERROR

  static property get S_Get_Only: String
  static property get S_Get_Only_Init: String = "hi"
  static property get S_Get_Only_Init_Err: String = 5  //## issuekeys: MSG_IMPLICIT_COERCION_ERROR

  static property set S_Set_Only: String
  static property set S_Set_Only_Init: String = "hi"
  static property set S_Set_Only_Init_Err: String = 4  //## issuekeys: MSG_IMPLICIT_COERCION_ERROR

  // override

  static class BaseClass
  {
    property Foo: String
    property Bar: String
    property get Baz: String
    property set Biz: String

    final property FinalProp: String
    final property get FinalGetProp: String
    final property set FinalSetProp: String
  }
  static class Sub extends BaseClass
  {
    override property get FinalProp(): String { return null }  //## issuekeys: MSG_CANNOT_OVERRIDE_FINAL
    override property set FinalProp( value: String ) {}  //## issuekeys: MSG_CANNOT_OVERRIDE_FINAL
  }
  static class SubClass extends BaseClass
  {
    override property Foo: String
    override property get Bar: String
    override property Baz: String
    override property Biz: String
    override property NotOverride: String  //## issuekeys: MSG_FUNCTION_NOT_OVERRIDE

    property FinalProp: String  //## issuekeys: MSG_CANNOT_OVERRIDE_FINAL, MSG_CANNOT_OVERRIDE_FINAL

    property get FinalGetProp: String  //## issuekeys: MSG_CANNOT_OVERRIDE_FINAL
    property set FinalGetProp( value: String ) {}

    property set FinalSetProp: String  //## issuekeys: MSG_CANNOT_OVERRIDE_FINAL
    property get FinalSetProp(): String { return "" }
  }
  static class SubClassMissingOverride extends BaseClass
  {
    property Foo: String   //## issuekeys: MSG_MISSING_OVERRIDE_MODIFIER
    property get Bar: String   //## issuekeys: MSG_MISSING_OVERRIDE_MODIFIER
    property get Baz: String   //## issuekeys: MSG_MISSING_OVERRIDE_MODIFIER
    property set Biz: String   //## issuekeys: MSG_MISSING_OVERRIDE_MODIFIER
  }
  static class SubClassMissingOverride2 extends BaseClass
  {
    property get Foo(): String { return null }  //## issuekeys: MSG_MISSING_OVERRIDE_MODIFIER
    property set Foo( value: String ) {}  //## issuekeys: MSG_MISSING_OVERRIDE_MODIFIER
  }
  static class DefineGetOrSetNotDefinedInSuper extends BaseClass
  {
    property set Baz: String
    property get Biz: String
  }

  // interface

  interface IFace
  {
    property Prop1: String

    property Prop2: String = "hi"

    property get Prop3: String = "aaa"

    property set Prop4: String = "bad"

    property Prop5: String = "test"
    property get Prop5() : String {
       return _Prop5  //## issuekeys: MSG_BAD_IDENTIFIER_NAME
    }

    property Prop6: String = "test"
    property set Prop6( v: String ) {
      _Prop6 = "#" + v  //## issuekeys: MSG_BAD_IDENTIFIER_NAME
    }

    property Prop7: String = "test"
    property get Prop7() : String {
       return _Prop7  //## issuekeys: MSG_BAD_IDENTIFIER_NAME
    }
    property set Prop7( v: String ) {
      _Prop7 = "#" + v  //## issuekeys: MSG_BAD_IDENTIFIER_NAME
    }
  }

  static class FooImpl implements IFace  //## issuekeys: MSG_UNIMPLEMENTED_METHOD, MSG_UNIMPLEMENTED_METHOD
  {

  }

  static class FooImpl2 implements IFace
  {
    override property Prop1: String
  }

  static class NotAbstract {
    abstract property Hi: String  //## issuekeys: MSG_ABSTRACT_MEMBER_NOT_IN_ABSTRACT_CLASS
  }

  abstract static class AbstractClass {
    abstract property Hi: String

    static class Foo extends AbstractClass {  //## issuekeys: MSG_UNIMPLEMENTED_METHOD, MSG_UNIMPLEMENTED_METHOD
    }
  }

  static class ImplIFaceMissingOverrideKeyword implements IFace
  {
    property Prop1: String  //## issuekeys: MSG_MISSING_OVERRIDE_MODIFIER
    property set Prop2: String  //## issuekeys: MSG_MISSING_OVERRIDE_MODIFIER
    property set Prop4: String  //## issuekeys: MSG_MISSING_OVERRIDE_MODIFIER
    property set Prop5: String  //## issuekeys: MSG_MISSING_OVERRIDE_MODIFIER
  }
  static class ImplIFaceHasOverrideKeyword implements IFace
  {
    override property Prop1: String
    override property set Prop2: String
    override property set Prop4: String
    override property set Prop5: String
  }

  function foo() {
    var both = Both
    Both = both

    var getOnly = Get_Only
    Get_Only = "nope"  //## issuekeys: MSG_PROPERTY_NOT_WRITABLE

    var setOnly = Set_Only  //## issuekeys: MSG_CLASS_PROPERTY_NOT_READABLE
    Set_Only = setOnly
  }

  static function bar() {
    var both = S_Both
    S_Both = both

    var getOnly = S_Get_Only
    S_Get_Only = "nope"  //## issuekeys: MSG_PROPERTY_NOT_WRITABLE

    var setOnly = S_Set_Only  //## issuekeys: MSG_CLASS_PROPERTY_NOT_READABLE
    S_Set_Only = setOnly

    var noAccess = Both  //## issuekeys: MSG_CANNOT_REFERENCE_NON_STATIC_PROPERTY_FROM_STATIC_CONTEXT
    var noAccess2 = Get_Only  //## issuekeys: MSG_CANNOT_REFERENCE_NON_STATIC_PROPERTY_FROM_STATIC_CONTEXT
    Set_Only = ""  //## issuekeys: MSG_CANNOT_REFERENCE_NON_STATIC_PROPERTY_FROM_STATIC_CONTEXT
  }
}
