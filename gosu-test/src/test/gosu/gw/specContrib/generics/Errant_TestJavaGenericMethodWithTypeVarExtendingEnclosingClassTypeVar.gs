package gw.specContrib.generics

uses java.io.Serializable
uses java.lang.Comparable
uses java.lang.Integer

uses java.lang.CharSequence
uses java.lang.StringBuilder

class Errant_TestJavaGenericMethodWithTypeVarExtendingEnclosingClassTypeVar {

  static class CacheEntry {}
  static class Key {}

  static function stuff() {
    var builder = new CacheBuilder<Object, Object>()

    var r1 = builder.build<Key, CacheEntry>( null, null )
    var a1 : CacheBuilder<Key, CacheEntry> = r1
    r1 = new CacheBuilder<Object, Object>()  //## issuekeys: MSG_TYPE_MISMATCH

    var r2 = builder.build( null, null )
    var a2 : CacheBuilder<Key, CacheEntry> = r2  //## issuekeys: MSG_TYPE_MISMATCH
    r2 = new CacheBuilder<Object, Object>()

    var r3 = builder.build( new Key(), new CacheEntry() )
    var a3 : CacheBuilder<Key, CacheEntry> = r3
    r3 = new CacheBuilder<Object, Object>()  //## issuekeys: MSG_TYPE_MISMATCH

    var builderString = new CacheBuilder<String, CharSequence>()
    builderString = builderString.build<Key, CacheEntry>( null, null )  //## issuekeys: MSG_TYPE_PARAM_NOT_ASSIGNABLE_TO, MSG_NO_SUCH_FUNCTION
    builderString = builderString.build( null, null )
    builderString = builderString.build( new Key(), new CacheEntry() )  //## issuekeys: MSG_IMPLICIT_COERCION_ERROR, MSG_TYPE_MISMATCH
    builderString = builderString.build( new Key(), new StringBuilder() )  //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
    builderString = builderString.build( "", new CacheEntry() )  //## issuekeys: MSG_TYPE_MISMATCH
    builderString = builderString.build( "", new StringBuilder() )
    builderString = builderString.build<String, StringBuilder>( null, null )
    builderString = builderString.build<String, String>( null, null )
    builderString = builderString.build<Object, String>( null, null )  //## issuekeys: MSG_TYPE_PARAM_NOT_ASSIGNABLE_TO, MSG_NO_SUCH_FUNCTION
  }
}
