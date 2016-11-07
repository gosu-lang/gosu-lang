package gw.internal.gosu.regression

enhancement JandexRegressionTestEnhancement : JandexRegressionTest {

  function bar<T extends Object[]>(i : T) {
    print("do nothing")
  }

}
