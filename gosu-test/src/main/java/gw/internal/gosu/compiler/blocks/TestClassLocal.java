package gw.internal.gosu.compiler.blocks;

/**
 */
public class TestClassLocal<T> {
    private final InitialValue<T> _init;

    public TestClassLocal(boolean b, InitialValue<T> initialValue) {
        _init = initialValue;
    }

    public T getInit() {
        return _init.getInitialValue();
    }

    public static interface InitialValue<V> {
        V getInitialValue();
    }

    public static <V> TestClassLocal<V> constant(InitialValue<V> initialValue) {
        return new TestClassLocal<V>(false, initialValue);
    }
}