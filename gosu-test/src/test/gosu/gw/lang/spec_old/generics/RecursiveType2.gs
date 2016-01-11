package gw.lang.spec_old.generics
uses java.lang.CharSequence

class RecursiveType2<T extends CharSequence, B extends RecursiveType2<T,B>> {

  function foo( t : T, b : B ) : B {
    return b
  }

}
