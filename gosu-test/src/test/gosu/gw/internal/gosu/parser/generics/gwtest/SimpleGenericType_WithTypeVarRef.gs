package gw.internal.gosu.parser.generics.gwtest

class SimpleGenericType_WithTypeVarRef<T, U extends T>
{
  static function foo() : SimpleGenericType_WithTypeVarRef<java.lang.CharSequence, String> {
    return new SimpleGenericType_WithTypeVarRef<java.lang.CharSequence, String>()
  }
}