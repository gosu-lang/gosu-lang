package gw.specContrib.interfaceMethods.defaultMethods.javaInteraction;

import java.util.ArrayList;
import java.util.List;

interface JavaInterface2 {
    default List foo(ArrayList<Integer> arrayList) {
        return null;
    }
}