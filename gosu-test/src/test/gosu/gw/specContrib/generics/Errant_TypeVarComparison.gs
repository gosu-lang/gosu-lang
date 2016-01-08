package gw.specContrib.generics

class Errant_TypeVarComparison {
  static class Class1<T> {
    function create() {
      if( T == String ) {}
    }
  }

  static class Class2<T extends StringBuilder> {
    function create() {
      if( T == String ) {}  //## issuekeys: MSG_TYPE_MISMATCH
    }
  }
}