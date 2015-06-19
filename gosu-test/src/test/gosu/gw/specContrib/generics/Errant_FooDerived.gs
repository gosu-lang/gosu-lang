package gw.specContrib.generics

class Errant_FooDerived extends BaseFoo { //## issuekeys: FUNCTION_NOT_IMPLEMENTED
  override function foo<B extends Gosh<B>>(): Gosh<B> { return null }
  override function bar<B extends Gosh<B>>(): Gosh<T> { return null } //## issuekeys:  DOES_NOT_OVERRIDE
  override function baz<B extends Gosh<B>, I extends B>(): Gosh<I> { return null }
}