package gw.specContrib.generics;

public interface BeanPopulatorNonFunctional<T extends Bean> {
  void foo();
  void bar(); // this method exists to make this interface non-functional (more than one abstract method)
}