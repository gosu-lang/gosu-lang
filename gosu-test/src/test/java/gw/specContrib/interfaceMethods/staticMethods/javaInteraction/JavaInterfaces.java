package gw.specContrib.interfaceMethods.staticMethods.javaInteraction;

public class JavaInterfaces {
    //Case#1
    static interface JavaInterfaceWithStaticMethod {
        static void foo() {
        }
    }
    //Case#2
    static interface JavaInterfaceWithDefaultMethod {
        default void bar() {
        }
    }
}
