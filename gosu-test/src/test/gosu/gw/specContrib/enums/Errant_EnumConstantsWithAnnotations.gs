package gw.specContrib.enums

enum Errant_EnumConstantsWithAnnotations {
  @Deprecated("") Foo,
  public Bar,  //## issuekeys: MSG_MODIFIERS_NOT_ALLOWED_HERE
  @Deprecated("") Foo,  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED
  Baz,
  @Deprecated("") Boo

  @Deprecated("")
  var _i: int
}