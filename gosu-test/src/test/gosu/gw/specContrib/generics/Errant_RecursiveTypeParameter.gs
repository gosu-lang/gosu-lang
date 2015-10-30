package gw.specContrib.generics

uses java.io.Serializable

class Errant_RecursiveTypeParameter {
  class A11<T extends A11> {}
  class B11<T extends A11<T>> extends A11<T> {}
  class B12 extends A11<B12> {}

  class A21<T extends A21 & Serializable> {}
  class B21 extends A21<B21> implements Serializable {}

  function test() {
    var a111: A11 = new B12()
    var a211: A21 = new B21()
  }

  // IDE-1786
  static class A311<T extends A311> {}  // note: it's not 'T extends A<T>'
  static class D311 extends A311<D311> {}
  static class B311 extends A311<C311> {}  //## issuekeys: TYPE PARAMETER IS NOT WITHIN THE BOUNDS
  static class C311 extends A311<D311> {}

  // IDE-1938
  function test(p: List<Errant_JavaRecursiveTypeParameter>) {
    var deepNode: Errant_JavaRecursiveTypeParameter = p.get(0).Children.get(0).Children.get(0)
  }

  // IDE-2203
  class A411<T, S extends A411<T, S>>  {}
  class B411<T, S extends A411<T, S>> extends A411<T, S> {}
  class C411 extends B411<String, C411> {}

  function test411() {
    var a: B411 = new C411()
  }

  class A51<T extends A51> {}
  var a5: A51<A51> = new A51()

  static class Errant_InheritRecursiveTypeNotRecursivelyError {
    static class RecursiveComparable<T extends Comparable<T>> {
    }

    static abstract class InterfaceError implements Comparable<String> {}
    var fail = new RecursiveComparable<InterfaceError>()  //## issuekeys: MSG_TYPE_PARAM_NOT_ASSIGNABLE_TO

    static abstract class InterfaceWorks extends ComparableClass<InterfaceWorks> {}
    var works = new RecursiveComparable<InterfaceWorks>()


    static abstract class ComparableClass<T> implements Comparable<T> {
    }
    static class RecursiveComparableClass<T extends ComparableClass<T>> {
    }
    static abstract class SuperError extends ComparableClass<String> {}
    var fail2 = new RecursiveComparableClass<SuperError>()    //## issuekeys: MSG_TYPE_PARAM_NOT_ASSIGNABLE_TO

    static abstract class SuperWorks extends ComparableClass<SuperWorks> {}
    var works2 = new RecursiveComparableClass<SuperWorks>()


    static class Base<U extends Base<U>> {}
    static class Derived<R, T> extends Base<Derived<R, T>> {}
    static class Derived2<R, T> extends Base<Derived2<R, R>> {}
    static class Derived3<R, T> extends Base<Derived3<T, T>> {}
    static class Derived4<R, T> extends Base<Derived4<T, R>> {}  //## issuekeys: MSG_TYPE_PARAM_NOT_ASSIGNABLE_TO


    abstract class BaseSampleDim<T extends Number> implements IDimension<BaseSampleDim<T>, T>{
    }

    abstract class BaseSampleDim2<T extends Number, U extends Number> implements IDimension<BaseSampleDim2<T, U>, T> {
    }
    abstract class BaseSampleDim3<T extends Number, U extends Number> implements IDimension<BaseSampleDim3<T, T>, T> {
    }
    abstract class BaseSampleDim4<T extends Number, U extends Number> implements IDimension<BaseSampleDim4<U, U>, T> {
    }
    abstract class BaseSampleDim5<T extends Number, U extends Number> implements IDimension<BaseSampleDim5<U, T>, T> {  //## issuekeys: MSG_TYPE_PARAM_NOT_ASSIGNABLE_TO
    }
  }
}
