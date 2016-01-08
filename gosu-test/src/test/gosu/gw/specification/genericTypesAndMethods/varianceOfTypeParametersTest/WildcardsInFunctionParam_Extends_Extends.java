package gw.specification.genericTypesAndMethods.varianceOfTypeParametersTest;

/**
 */
public interface WildcardsInFunctionParam_Extends_Extends<T, R> {
    R apply( T t );

    default <V> WildcardsInFunctionParam_Extends_Extends<V, R> compose( WildcardsInFunctionParam_Extends_Extends<? extends V, ? extends T> before ) {
      return null;
    }
}

