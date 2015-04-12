package gw.specContrib.generics;

public class CacheBuilder<K,V> {
    public <K1 extends K, V1 extends V> CacheBuilder<K1, V1> build( K1 k, V1 v ) { return new CacheBuilder<K1, V1>(); }
}
