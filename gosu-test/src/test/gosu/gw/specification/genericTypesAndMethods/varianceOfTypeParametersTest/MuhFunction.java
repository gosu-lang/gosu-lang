package gw.specification.genericTypesAndMethods.varianceOfTypeParametersTest;

import java.util.Objects;

public interface MuhFunction<T, R> {

    R apply( T t );

    default <V> MuhFunction<V, R> compose( MuhFunction<? super V, ? extends T> before ) {
        Objects.requireNonNull(before);
        return (V v) -> apply(before.apply(v));
    }

    default <V> MuhFunction<T, V> andThen( MuhFunction<? super R, ? extends V> after ) {
        Objects.requireNonNull(after);
        return (T t) -> after.apply(apply(t));
    }

    static <T> MuhFunction<T, T> identity() {
        return t -> t;
    }
}
