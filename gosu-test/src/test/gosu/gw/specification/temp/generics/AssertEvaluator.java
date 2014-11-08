package gw.specification.temp.generics;

public interface AssertEvaluator<T, A extends BaseAssert<T,A>> {
    A evaluate( A assertion );
}