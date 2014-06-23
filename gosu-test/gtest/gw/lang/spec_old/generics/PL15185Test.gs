package gw.lang.spec_old.generics

uses gw.test.TestClass

class PL15185Test extends TestClass {

  static class P1<T> {
    var _one : T as One
  }

  final class P2<A, B> { 
    var _one : P1<A> 
    var _two : P1<B> 
   
    private construct() { } 
   
    property get One() : A { return _one.One } 
   
    property get Two() : B { return _two.One } 

  } 

  final class P3<A, B, C> { 
    var _one : P1<A> 
    var _twoThree : P2<B, C> 
   
    private construct() { } 
   
    property get One() : A { return _one.One } 
   
    property get Two() : B { return _twoThree.One } 
  } 

  function testPlug() {
  }
}
