package gw.specContrib.delegates

class Errant_StructureTest {
  interface   IA {
    function afun()
  }

  structure SA extends IA {
  }

  class B implements SA {
    //IDE-1251 - should not show any error
    delegate b represents IA
  }
  class C implements SA {
    delegate c represents SA
  }
}