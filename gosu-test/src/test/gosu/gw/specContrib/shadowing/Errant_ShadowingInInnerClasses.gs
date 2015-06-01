package gw.specContrib.shadowing

uses java.lang.Integer

class Errant_ShadowingInInnerClasses {
  public static class QaInner1 extends Errant_ShadowingInInnerClasses {
    public property set Bar(myVar: List<String>) {
    }

    public function doit(myVar: List<String>) {

    }
  }
  //IDE-442 - Same erasure functions but in different Inner classes. No error expected.
  public static class QaInner2 extends Errant_ShadowingInInnerClasses {
    public property set Bar(myVar: List<Integer>) {
    }

    public function doit(myVar: List<Integer>) {

    }
  }
}