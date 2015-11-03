package gw.specContrib.generics;

import java.util.Objects;

/**
 */
public interface WildcardsInFunctionParam_Super_Extends<T, R> {
    R apply(T t);

    default <V> WildcardsInFunctionParam_Super_Extends<V, R> compose(WildcardsInFunctionParam_Super_Extends<? super V, ? extends T> before) {
        Objects.requireNonNull( before );
        return (V v) -> apply(before.apply(v));
    }
}

