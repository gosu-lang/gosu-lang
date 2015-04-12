package gw.specContrib.generics

uses java.io.Serializable
uses java.lang.Comparable
uses java.lang.Integer

uses java.lang.CharSequence
uses java.lang.StringBuilder

class Errant_TestGosuGenericMethodWithTypeVarExtendingEnclosingClassTypeVar {

  static class CacheEntry {}
  static class Key {}

  static function stuff() {
    var builder = new GosuCacheBuilder<Object, Object>()

    var r1 = builder.build<Key, CacheEntry>( null, null )
    var a1 : GosuCacheBuilder<Key, CacheEntry> = r1
    r1 = new GosuCacheBuilder<Object, Object>()  //## issuekeys: MSG_TYPE_MISMATCH

    var r2 = builder.build( null, null )
    var a2 : GosuCacheBuilder<Key, CacheEntry> = r2  //## issuekeys: MSG_TYPE_MISMATCH
    r2 = new GosuCacheBuilder<Object, Object>()

    var r3 = builder.build( new Key(), new CacheEntry() )
    var a3 : GosuCacheBuilder<Key, CacheEntry> = r3
    r3 = new GosuCacheBuilder<Object, Object>()  //## issuekeys: MSG_TYPE_MISMATCH

    var builderString = new GosuCacheBuilder<String, CharSequence>()
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

  static class Hiper<K,V> {
    function getMuh() : Inner<K, V> { return new Inner<K, V>() }
    class Inner<Z extends K, X extends V> {
      function build<K1 extends Z, V1 extends X>( k: K1, v: V1 ) : Hiper<K1, V1> { return null }
    }
  }

  static function initMuh( c: int ): Hiper<Key, CacheEntry>  {
    var builder = new Hiper<Object, Object>().getMuh()
    return builder.build( new Key(), new CacheEntry() )
  }

  static function initMuh_Errant( c: int ): Hiper<Key, CacheEntry>  {
    var builder = new Hiper<Object, Object>().getMuh()
    return builder.build( new String(), new CacheEntry() )  //## issuekeys: MSG_TYPE_MISMATCH
  }

}
