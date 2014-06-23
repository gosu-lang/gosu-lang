package gw.internal.gosu.parser.classTests.gwtest.modifiers

class HasFinalPropertyGetterAndSetter
{
  var _value : String

  final property get FinalProperty() : String
  {
    return _value
  }
  final property set FinalProperty( value : String )
  {
    _value = value
  }
}