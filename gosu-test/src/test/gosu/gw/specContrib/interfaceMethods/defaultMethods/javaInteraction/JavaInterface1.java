package gw.specContrib.interfaceMethods.defaultMethods.javaInteraction;

import java.util.List;

public interface JavaInterface1 {
    public default List foo() {
        return null;
    }
}