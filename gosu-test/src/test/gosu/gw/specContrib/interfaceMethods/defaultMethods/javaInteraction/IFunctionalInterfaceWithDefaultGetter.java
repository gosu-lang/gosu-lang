package gw.specContrib.interfaceMethods.defaultMethods.javaInteraction;

/**
 * Created with IntelliJ IDEA.
 * User: smckinney
 * Date: 9/1/15
 * Time: 10:02 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IFunctionalInterfaceWithDefaultGetter {
    default String getFoo() {
        return null;
    }
    void setFoo( String s );
}
