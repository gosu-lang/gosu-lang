package gw.specContrib.generics

class Errant_ChainedMethodInvocationWithRawType {
  // IDE-2281
  function test(jc: Errant_ChainedMethodInvocationWithRawTypeJava) {
    jc.getC2().create().foo()
    jc.C2.create().foo()
  }
}
