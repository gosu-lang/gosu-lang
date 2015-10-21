package gw.specContrib.generics;

/**
 */
public interface WildcardsInFunctionParam_Extends_Super<T, R> {
    R apply( T t );

    default <V> WildcardsInFunctionParam_Extends_Super<V, R> compose( WildcardsInFunctionParam_Extends_Super<? extends V, ? super T> before ) {
      return null;
    }
}

