package gw.lang.spec_old.generics
uses java.lang.CharSequence

class SubOfRecursiveType2<T extends CharSequence, B extends SubOfRecursiveType2<T,B>> extends RecursiveType2<T,B> {

  construct() {

  }
}
