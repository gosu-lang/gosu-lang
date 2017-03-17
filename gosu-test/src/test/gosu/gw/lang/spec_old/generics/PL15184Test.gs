package gw.lang.spec_old.generics

uses gw.test.TestClass

class PL15184Test extends TestClass {

  class SomeComponent<A> { 
    reified function map<B>(f(a : A) : B) : SomeComponent<B> { return new SomeComponent<B>() }
  } 


  class SomeComposite<A, B> { 
    var _a : SomeComponent<A> 
    var _b : SomeComponent<B> 
    reified function mapA<C>(f(a : A) : C) : SomeComposite<C, B> { return new SomeComposite<C, B>() { :_a = this._a.map(f), :_b = this._b } }
    reified function mapB<C>(f(b : B) : C) : SomeComposite<A, C> { return new SomeComposite<A, C>() { :_a = this._a, :_b = this._b.map(f) } }
  } 

  function testPlug() {
  }
}
