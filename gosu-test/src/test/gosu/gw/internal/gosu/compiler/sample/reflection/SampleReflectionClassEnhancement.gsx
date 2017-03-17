package gw.internal.gosu.compiler.sample.reflection
uses java.lang.CharSequence

enhancement SampleReflectionClassEnhancement : gw.internal.gosu.compiler.sample.reflection.SampleReflectionClass {

  function e_plainFunction() : String {
    return "plain function"
  }

  function e_functionWStringArg( s : String ) : String {
    return s
  }

  function e_functionWBooleanArg( s : boolean ) : boolean {
    return s
  }

  function e_functionWByteArg( s : byte ) : byte {
    return s
  }

  function e_functionWCharArg( s : char ) : char {
    return s
  }

  function e_functionWShortArg( s : short ) : short {
    return s
  }

  function e_functionWIntArg( s : int ) : int {
    return s
  }

  function e_functionWLongArg( s : long ) : long {
    return s
  }

  function e_functionWFloatArg( s : float ) : float {
    return s
  }

  function e_functionWDoubleArg( s : double ) : double {
    return s
  }

  function e_setWStringArg( s : String ) {
    this._s = s
  }

  function e_setWBooleanArg( s : boolean ) {
    this._b = s
  }

  function e_setWByteArg( s : byte ) {
    this._by = s
  }

  function e_setWCharArg( s : char ) {
    this._c = s
  }

  function e_setWShortArg( s : short ) {
    this._sh = s
  }

  function e_setWIntArg( s : int ) {
    this._i = s
  }

  function e_setWLongArg( s : long ) {
    this._l = s
  }

  function e_setWFloatArg( s : float ) {
    this._f = s
  }

  function e_setWDoubleArg( s : double ) {
    this._d = s
  }

  static function e_staticPlainFunction() : String {
    return "plain function"
  }

  static function e_staticFunctionWStringArg( s : String ) : String {
    return s
  }

  static function e_staticFunctionWBooleanArg( s : boolean ) : boolean {
    return s
  }

  static function e_staticFunctionWByteArg( s : byte ) : byte {
    return s
  }

  static function e_staticFunctionWCharArg( s : char ) : char {
    return s
  }

  static function e_staticFunctionWShortArg( s : short ) : short {
    return s
  }

  static function e_staticFunctionWIntArg( s : int ) : int {
    return s
  }

  static function e_staticFunctionWLongArg( s : long ) : long {
    return s
  }

  static function e_staticFunctionWFloatArg( s : float ) : float {
    return s
  }

  static function e_staticFunctionWDoubleArg( s : double ) : double {
    return s
  }

  function e_genericFunction<T>( arg : T ) : T {
    return arg
  }

  static function e_staticGenericFunction<T>( arg : T ) : T {
    return arg
  }

  reified function e_genericFunctionReturnsT<T>( arg : T ) : Type {
    return T
  }

  reified static function e_staticGenericFunctionReturnsT<T>( arg : T ) : Type {
    return T
  }

  reified function e_genericFunctionReturnsBoundedT<T extends CharSequence>( arg : T ) : Type {
    return T
  }

  reified static function e_staticGenericFunctionReturnsBoundedT<T extends CharSequence>( arg : T ) : Type {
    return T
  }
 
  property get e_StringProp() : String {
    return this._s
  }
 
  property set e_StringProp( s : String ) {
    this._s = s
  }

  property get e_BoolProp() : boolean {
    return this._b
  }
 
  property set e_BoolProp( s : boolean ) {
    this._b = s
  }

  property get e_ByteProp() : byte {
    return this._by
  }
 
  property set e_ByteProp( s : byte ) {
    this._by = s
  }

  property get e_CharProp() : char {
    return this._c
  }
 
  property set e_CharProp( s : char ) {
    this._c = s
  }

  property get e_ShortProp() : short {
    return this._sh
  }
 
  property set e_ShortProp( s : short ) {
    this._sh = s
  }

  property get e_IntProp() : int {
    return this._i
  }
 
  property set e_IntProp( s : int ) {
    this._i = s
  }

  property get e_LongProp() : long {
    return this._l
  }
 
  property set e_LongProp( s : long ) {
    this._l = s
  }

  property get e_FloatProp() : float {
    return this._f
  }
 
  property set e_FloatProp( s : float ) {
    this._f = s
  }

  property get e_DoubleProp() : double {
    return this._d
  }
 
  property set e_e_DoubleProp( s : double ) {
    this._d = s
  }
  
}
