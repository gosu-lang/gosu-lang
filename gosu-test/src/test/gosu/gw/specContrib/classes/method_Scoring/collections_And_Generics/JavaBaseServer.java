package gw.specContrib.classes.method_Scoring.collections_And_Generics;

import java.util.Collection;
import java.util.Set;

public class JavaBaseServer {
    <T> void foo(Collection<T> c) {}
    <T> void foo(Set<T> s) {}
}
