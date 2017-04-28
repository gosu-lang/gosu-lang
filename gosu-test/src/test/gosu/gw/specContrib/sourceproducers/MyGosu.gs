package gw.specContrib.sourceproducers

class MyGosu implements Runnable {
  static internal var _static_string: String = "_static_string"
  static internal var _static_int: int = 5
  static internal var _static_stingArray: String[] = {"a","b"}
  static internal var _static_stringList: List<String> = {"b", "c"}
  static internal var _static_stringMap: Map<String, MyGosu> = {"hi" ->new MyGosu()}
  
  static var _static_private: String = "_static_private"
  static internal var _static_internal: String = "_static_internal"
  static protected var _static_protected: String = "_static_protected"
  static public var _static_public: String = "_static_public"
  static public final var _static_private_final: String = "_static_private_final"

  @gw.lang.Deprecated(:value=MyGosu._static_private_final)
  static property Static_NewVarProp: String = "Static_NewVarProp"
  
  static var _static_oldVarProp: String as Static_OldVarProp = "Static_OldVarProp"
  
  static property Static_NewPropBoth: String = "Static_NewPropBoth"
  static property get Static_NewPropGet: String = "Static_NewPropGet"
  static property set Static_NewPropSet: String = "Static_NewPropSet"
  
  static function static_publicFunction() : String
  {
    return "static_publicFunction"
  }
  static protected function static_protectedFunction() : String
  {
    return "static_protectedFunction"
  }
  static internal function static_internalFunction() : String
  {
    return "static_internalFunction"
  }
  static private function static_privateFunction() : String
  {
    return "static_privateFunction"
  }
  
  static class Static_PublicInnerClass<S, T extends CharSequence, P> extends ArrayList<T> {
    function hi( s: String ) : String {
      return "hi"  
    }
  }
  static protected class Static_ProtectedInnerClass<S, T extends CharSequence, P> extends ArrayList<T> {
    function one( s: String ) : String { return s }
  }
  static internal class Static_InternalInnerClass<S, T extends CharSequence, P> extends ArrayList<T> {
    function two( s: String ) : String { return s }
  }
  static private class Static_PrivateInnerClass<S, T extends CharSequence, P> extends ArrayList<T> {
    function three( s: String ) : String { return s }
  }


  internal var _string: String = "_string"
  internal var _int: int = 6
  internal var _stingArray: String[] = {"x", "y"}
  internal var _stringList: List<String> = {"g", "s"}
  internal var _stringMap: Map<String, Integer> = {"one" ->1} 
  
  var _private: String = "_private"
  internal var _internal: String = "_internal"
  protected var _protected: String = "_protected"
  public var _public: String = "_public"
  
  property NewVarProp: String = "NewVarProp"
  
  var _oldVarProp: String as OldVarProp = "OldVarProp"
  
  property NewPropBoth: String = "NewPropBoth"
  property get NewPropGet: String = "NewPropGet"
  property set NewPropSet: String
  
  function publicFunction() : String
  {
    return "publicFunction"
  }
  protected function protectedFunction() : String
  {
    return "protectedFunction"
  }
  internal function internalFunction() : String
  {
    return "internalFunction"
  }
  private function privateFunction() : String
  {
    return "privateFunction"
  }
  
  function allTheThings( hi(x(s:String):block(y:String)):block(w:String):int ) : block(x(s:String):block(y:String)):block(w:String):int
  {
    return hi
  }
  
  public class Public_InnerClass<S, T extends CharSequence, P> extends ArrayList<T> {
    function hi( s: String ) : String { return s }
  }
  protected class Protected_InnerClass<S, T extends CharSequence, P> extends ArrayList<T> {
    function hi( s: String ) : String { return s }
  }
  internal class Internal_InnerClass<S, T extends CharSequence, P> extends ArrayList<T> {
    function hi( s: String ) : String { return s }
  }
  private class Private_InnerClass<S, T extends CharSequence, P> extends ArrayList<T> {
    function hi( s: String ) : String { return s }
  }
  
  structure PublicStructure
  {
    function foo( s: String, t: int )
  }
 
  delegate _delegate represents Runnable = \ -> {print("hi")}
  
  function generic<T extends CharSequence>( t: T ) : List<T> {
    return {t}
  }
}