package gw.internal.gosu.regression

enhancement NamedArgWithinNamedArgWorksTestEnhancement : NamedArgWithinNamedArgWorksTest {

  function bar( val2 : String = null,  val : String = null) : String {
    return val
  }

}

