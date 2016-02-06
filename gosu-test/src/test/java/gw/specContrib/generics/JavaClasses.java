package gw.specContrib.generics;

class A {}
class B1<T extends A> {}
class B2<T> {}
class B3<T extends A & Cloneable> {}

class C {
  static B1 getB1() {
    return new B1();
  }

  static B2 getB2() {
    return new B2();
  }

  static B3 getB3() {
    return new B3();
  }
}
