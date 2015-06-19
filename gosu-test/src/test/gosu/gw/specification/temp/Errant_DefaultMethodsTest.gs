package gw.specification.temp

class Errant_DefaultMethodsTest extends FooBase implements IFoo, IFu {
  override function fred(): String {
    super[IFu].fred()
    super[IFoo].fred()

    super[java.util.List].toString()  //## issuekeys: MSG_NOT_A_SUPERTYPE

    super.fred()  //## issuekeys: MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD, MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD
    super.abstFromBase()  //## issuekeys: MSG_ABSTRACT_METHOD_CANNOT_BE_ACCESSED_DIRECTLY
    super.fromBase()

    super[FooBase].fred()  //## issuekeys: MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD, MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD
    super[FooBase].abstFromBase()  //## issuekeys: MSG_ABSTRACT_METHOD_CANNOT_BE_ACCESSED_DIRECTLY
    super[FooBase].fromBase()

    super[IFoo].noDefault()  //## issuekeys: MSG_ABSTRACT_METHOD_CANNOT_BE_ACCESSED_DIRECTLY
    super[IFoo].hashCode()  //## issuekeys: MSG_ABSTRACT_METHOD_CANNOT_BE_ACCESSED_DIRECTLY

    return ""
  }

  override property get MyProp() : String {
    var x = super[IFoo].MyProp
    var y = super.MyProp  //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND
    super[IFoo].MyProp = "hi"
    return super[IFu].MyProp
  }

  override function noDefault() {
    super.noDefault()  //## issuekeys: MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD, MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD
    super[IFoo].noDefault()  //## issuekeys: MSG_ABSTRACT_METHOD_CANNOT_BE_ACCESSED_DIRECTLY
  }

  override function abstFromBase() {
  }
}
