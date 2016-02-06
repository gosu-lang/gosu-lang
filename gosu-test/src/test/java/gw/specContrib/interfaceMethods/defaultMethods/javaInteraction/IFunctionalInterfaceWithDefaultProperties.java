package gw.specContrib.interfaceMethods.defaultMethods.javaInteraction;

/**
 * Created with IntelliJ IDEA.
 * User: smckinney
 * Date: 9/1/15
 * Time: 10:02 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IFunctionalInterfaceWithDefaultProperties {
    String notAProperty();

    default String getFoo() {
        return null;
    }
    default void setFoo( String s ) {
    }

    default boolean isBar() {
        return false;
    }
    default void setBar( boolean b ) {
    }
}
