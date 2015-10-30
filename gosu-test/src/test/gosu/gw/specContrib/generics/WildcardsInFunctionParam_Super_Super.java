package gw.specContrib.generics;

import java.util.Objects;

/**
 */
public interface WildcardsInFunctionParam_Super_Super<T, R> {
    R apply( T t );

    default <V> WildcardsInFunctionParam_Super_Super<V, R> compose( WildcardsInFunctionParam_Super_Super<? super V, ? super T> before ) {
      return null;
    }
}

