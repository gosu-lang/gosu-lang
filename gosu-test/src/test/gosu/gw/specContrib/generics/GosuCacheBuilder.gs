package gw.specContrib.generics;

public class GosuCacheBuilder<K,V> {
  function build<K1 extends K, V1 extends V>( k: K1, v: V1 ) : GosuCacheBuilder<K1, V1> { return new GosuCacheBuilder<K1, V1>(); }
}
