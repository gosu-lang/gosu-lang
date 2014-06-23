package gw.internal.gosu.parser.expressions

uses java.lang.StringBuilder
uses gw.lang.reflect.features.PropertyReference

class OptionalParamClass extends BaseOptionalParamClass
{
  var _r1 : String
  var _r2 : int
  var _o1 : String
  var _o2 : int


  public static var _SB : StringBuilder = new StringBuilder()

  static function getValue<V>( value: V, sb: StringBuilder ) : V
  {
    sb.append( value as Object ).append( " " )
    return value
  }
  
  construct( r1: String = "r1value", r2 = 2, o1: String = "o1value", o2 = 22 )
  {
    super( :p2 = getValue( r2, _SB ), :p1 = getValue( r1, _SB ) )
    _r1 = r1
    _r2 = r2
    _o1 = o1
    _o2 = o2
  }
   
  override function toString() : String
  {
    return _r1 + ", " + _r2 + ", " + _o1 + ", " + _o2
  }
  
  function hasOneOptional( o1: String = "avalue" ) : String
  {
    return o1
  }

  function hasOneOptionalOneRequired( r1: int, o1: String = "avalue" ) : String
  {
    return r1 + ", " + o1
  }
  
  function hasTwoOptionalOneRequired( r1: int, o1: String = "avalue", o2: int = 2 ) : String
  {
    return r1 + ", " + o1 + ", " + o2
  }

  function hasTwoOptionalTwoRequired( r1: int, r2: int, o1: String = "avalue", o2: int = 2 ) : String
  {
    return r1 + ", " + r2 + ", " + o1 + ", " + o2
  }

  function hasOneOptionalSH( o1 = "avalue" ) : String
  {
    return o1
  }
  
  function hasOneOptionalSHOneRequired( r1: int, o1 = "avalue" ) : String
  {
    return r1 + ", " + o1
  }
  
  function hasTwoOptionalOneSHOneRequired( r1: int, o1: String = "avalue", o2 = 2 ) : String
  {
    return r1 + ", " + o1 + ", " + o2
  }
  
  function hasThreeOptionalOneSHOneRequired( r1: int, o1: String = "avalue", o2 = 2, o3 : String = "optional3" ) : String
  {
    return r1 + ", " + o1 + ", " + o2 + ", " + o3
  }

  static function hasOneOptionalOneRequired_Static( r1: int, o1: String = "avalue" ) : String
  {
    return r1 + ", " + o1
  }

  static function hasOptionalParamThatImplicitlyCoerces<T, U, V>( a : PropertyReference<T, V>,
                                                                  b : PropertyReference<U, V>,
                                                                  defaultValue : V = null ) : V
  {
    return defaultValue
  }
  static function callHasOptionalParamThatImplicitlyCoerces() : Object
  {
    return hasOptionalParamThatImplicitlyCoerces( String#Alpha, String#Alpha )
  }

  override function overrideMe( x = "default" )
  {
  }
}
