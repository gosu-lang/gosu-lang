package gw.specContrib.generics

class Errant_ChainedMethodInvocationWithRawType {
  function test(jc: Errant_ChainedMethodInvocationWithRawTypeJava) {
    // IDE-2281
    jc.getC2().create().foo()
    jc.C2.create().foo()

    // IDE-1872
    var l = jc.Prop1
    var s: String[] = l.toArray(new String[0])
  }
}
