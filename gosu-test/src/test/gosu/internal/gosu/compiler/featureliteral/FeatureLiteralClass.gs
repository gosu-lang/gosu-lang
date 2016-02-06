package gw.internal.gosu.compiler.featureliteral

uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
@FeatureAnnotation({FeatureLiteralClass#_tee, FeatureLiteralClass#instFunc1(), FeatureLiteralClass#construct()})
class FeatureLiteralClass<T> {

  private var _tee : T as Tee
  private static var _str : String as StaticStringProp

  construct() {}
  construct(o : T) { _tee = o }
  construct(s : boolean) {}
  private construct(s : boolean, s2: boolean) {}
  
  function instFunc1() : String { return "doh" }
  function instFunc2( o : Object ) : Object{ return "foo" }
  function instFunc2( o : String ) : String{ return "bar" }
  function instFunc3() : T { return _tee }
  function instFunc4(x : T) : String { return "bwha?"}

  function instFunc5( o : Object, s : String, b : boolean ) : List {
    return {o, s, b}
  }
  
  function thisAndSetFunc( t : T ) : FeatureLiteralClass {
    Tee = t
    return this
  }

  static function staticInstFunc1() : String { return "doh" }
  static function staticInstFunc2( o : Object ) : Object{ return "foo" }
  static function staticInstFunc2( o : String ) : String{ return "bar" }

  private function privateInstFunc1() : String { return "doh" }
  private function privateInstFunc2( o : Object ) : Object{ return "foo" }
  private function privateInstFunc2( o : String ) : String{ return "bar" }
  private function privateInstFunc3() : T { return _tee }

  private static function privateStaticInstFunc1() : String { return "doh" }
  private static function privateStaticInstFunc2( o : Object ) : Object{ return "foo" }
  private static function privateStaticInstFunc2( o : String ) : String{ return "bar" }

  property get PrivateTee() : T {
    return _tee
  }
  
  property set PrivateTee( t : T ) {
    _tee = t
  }

  private static property get PrivateStaticStringProp() : String {
    return _str
  }
  
  private static property set PrivateStaticStringProp( s : String ) {
    _str = s
  }

  property get ThisProp() : FeatureLiteralClass<T> {
    return this
  }

  static property get StaticNewProp() : FeatureLiteralClass<String> {
    return new FeatureLiteralClass<String>()
  }

  function thisFunc() : FeatureLiteralClass<T> {
    return this
  }

  function thisFunc( s : String ) : FeatureLiteralClass<T> {
    return this
  }

  static function staticNewFunc() : FeatureLiteralClass<String> {
    return new FeatureLiteralClass<String>()
  }
  
  @FeatureAnnotation({#Tee, #instFunc1(), #construct()})
  function functionWithAnnotations() {}
  
  function testRelativeReferences() {
    var mr = #instFunc1()
    var mr2 = #instFunc2(Object)
    var mr3 = #instFunc4(T)

    var p1 = #Tee    
    var p2 = #StaticStringProp 
    
    var c1 = #construct()
  }

  function makeInner() : Inner {
    return new Inner() 
  }
  
  class Inner {
    @FeatureAnnotation( { #innerFunc() } )
    function innerFunc() : String { return "inner" } 
  }
}