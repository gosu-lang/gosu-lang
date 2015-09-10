package gw.internal.gosu.compiler.blocks;

public interface RemovalListener<K, V>  {
    void onRemoval( RemovalNotification<K, V> kvRemovalNotification );
}
