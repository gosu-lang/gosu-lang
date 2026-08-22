package gw.internal.gosu.compiler.sample.signature

class Sig_MethodClassAndInterface {
  function foo<P extends Sig_Class & Sig_I1>( p: P ): void { }
}
