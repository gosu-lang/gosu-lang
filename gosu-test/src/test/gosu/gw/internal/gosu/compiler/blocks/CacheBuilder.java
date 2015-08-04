package gw.internal.gosu.compiler.blocks;

public class CacheBuilder<K, V>
{

  public static CacheBuilder newBuilder()
  {
    return new CacheBuilder();
  }

  public <K1 extends K, V1 extends V> CacheBuilder<K1, V1> removalListener( RemovalListener<? super K1, ? super V1> listener )
  {
    return (CacheBuilder<K1, V1>)this;
  }
}
