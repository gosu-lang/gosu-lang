package gw.internal.gosu.parser.classTests.gwtest.inner

class ThisPtrTest extends ThisPtrTestBase {
  var y : String
  var innerInstance : ThisPtrInner

  construct() {
    x = "outer"
    y = "outeronly"
    innerInstance = new ThisPtrInner()
  }

  property get outerProp() : String {
    return x
  }

  function outerFunc() : String {
    return x
  }

  property get outerNoCommonProp() : String {
    return y
  }

  function outerNoCommonFunc() : String {
    return y
  }

  class ThisPtrInner extends ThisPtrTestBase {
    construct() {
      x = "inner"
    }

    function getOuterProp() : String {
      return outerProp
    }

    function getOuterFunc() : String {
      return outerFunc()
    }

    function getOuterNoCommonProp() : String {
      return outerNoCommonProp
    }

    function getOuterNoCommonFunc() : String {
      return outerNoCommonFunc()
    }
  }
}
