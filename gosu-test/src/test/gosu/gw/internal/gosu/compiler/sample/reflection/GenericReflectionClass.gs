package gw.internal.gosu.compiler.sample.reflection
uses java.lang.CharSequence

class GenericReflectionClass<T, QQ, RR> {


  function getT() : Type { 
    return T
  }
  
  public var _s : String as StringProp = "Default Value"
  public var _b : boolean as BoolProp = false
  public var _by : byte as ByteProp = 42
  public var _c : char as CharProp = 'a'
  public var _sh : short as ShortProp = 42
  public var _i : int as IntProp = 42
  public var _l : long as LongProp = 42
  public var _f : float as FloatProp = 42.0
  public var _d : double as DoubleProp = 42.0

  construct() {
  }

  construct( s : String ) {
    _s = s
  }

  construct( s : boolean ) {
    _b = s
  }

  construct( s : byte ) {
    _by = s
  }

  construct( s : char ) {
    _c = s
  }

  construct( s : short ) {
    _sh = s
  }

  construct( s : int ) {
    _i = s
  }

  construct( s : long ) {
    _l = s
  }

  construct( s : float ) {
    _f = s
  }

  construct( s : double ) {
    _d = s
  }

  function plainFunction() : String {
    return "plain function"
  }

  function functionWStringArg( s : String ) : String {
    return s
  }

  function functionWBooleanArg( s : boolean ) : boolean {
    return s
  }

  function functionWByteArg( s : byte ) : byte {
    return s
  }

  function functionWCharArg( s : char ) : char {
    return s
  }

  function functionWShortArg( s : short ) : short {
    return s
  }

  function functionWIntArg( s : int ) : int {
    return s
  }

  function functionWLongArg( s : long ) : long {
    return s
  }

  function functionWFloatArg( s : float ) : float {
    return s
  }

  function functionWDoubleArg( s : double ) : double {
    return s
  }

  function setWStringArg( s : String ) {
    _s = s
  }

  function setWBooleanArg( s : boolean ) {
    _b = s
  }

  function setWByteArg( s : byte ) {
    _by = s
  }

  function setWCharArg( s : char ) {
    _c = s
  }

  function setWShortArg( s : short ) {
    _sh = s
  }

  function setWIntArg( s : int ) {
    _i = s
  }

  function setWLongArg( s : long ) {
    _l = s
  }

  function setWFloatArg( s : float ) {
    _f = s
  }

  function setWDoubleArg( s : double ) {
    _d = s
  }

  static function staticPlainFunction() : String {
    return "plain function"
  }

  static function staticFunctionWStringArg( s : String ) : String {
    return s
  }

  static function staticFunctionWBooleanArg( s : boolean ) : boolean {
    return s
  }

  static function staticFunctionWByteArg( s : byte ) : byte {
    return s
  }

  static function staticFunctionWCharArg( s : char ) : char {
    return s
  }

  static function staticFunctionWShortArg( s : short ) : short {
    return s
  }

  static function staticFunctionWIntArg( s : int ) : int {
    return s
  }

  static function staticFunctionWLongArg( s : long ) : long {
    return s
  }

  static function staticFunctionWFloatArg( s : float ) : float {
    return s
  }

  static function staticFunctionWDoubleArg( s : double ) : double {
    return s
  }

  function genericFunction<Q>( arg : Q ) : Q {
    return arg
  }

  static function staticGenericFunction<Q>( arg : Q ) : Q {
    return arg
  }

  reified function genericFunctionReturnsQ<Q>( arg : Q ) : Type {
    return Q
  }

  reified static function staticGenericFunctionReturnsQ<Q>( arg : Q ) : Type {
    return Q
  }

  reified function genericFunctionReturnsBoundedQ<Q extends CharSequence>( arg : Q ) : Type {
    return Q
  }

  reified static function staticGenericFunctionReturnsBoundedQ<Q extends CharSequence>( arg : Q ) : Type {
    return Q
  }
}
