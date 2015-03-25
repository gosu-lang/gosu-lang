package gw.specContrib.delegates

class Errant_DelegateJavaPropertyMethod {
  interface I1 extends Errant_DelegateJavaPropertyMethod_JavaInterface {
  }

  // IDE-1826
  class A implements I1 {
    delegate d: Errant_DelegateJavaPropertyMethod_JavaInterface represents Errant_DelegateJavaPropertyMethod_JavaInterface
  }
}