package gw.specContrib.generics;

/**
 * Created with IntelliJ IDEA.
 * User: lboasso
 * Date: 3/30/15
 * Time: 4:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class Bean {
  static <T extends Bean> BeanPopulator<? super T> addPopulator( BeanPopulator<? super T> populator) {
    populator.foo();
    return null;
  }

  static <T extends Bean> BeanPopulator<? super T> addPopulatorNonFunctional( BeanPopulatorNonFunctional<? super T> populator) {
    populator.foo();
    return null;
  }

}
