package gw.specContrib.statements.usesStatement

class MuhClass<T>
{
  static function staticFunc<T>( t: T ) : Type<T> { return T }
  static function staticFunc2( t: String ) : String { return t }
}
