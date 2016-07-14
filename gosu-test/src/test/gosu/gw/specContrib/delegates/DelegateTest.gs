package gw.specContrib.delegates

uses gw.BaseVerifyErrantTest
uses gw.lang.reflect.gs.IGosuClass
uses java.math.BigDecimal

class DelegateTest extends BaseVerifyErrantTest {

  interface IHi {
    function blah<T>() : Type<T>
    function blah2<T>( t: T ) : T
  }

  static class HiDelegate implements IHi {
    override function blah<T>() : Type<T> {
      return T
    }
    function blah2<T>( t: T ) : T {
      return t
    }
  }

  class Hi extends HiDelegate {
  }

  class Hi2 implements IHi {
    delegate _hi represents IHi
    construct() {
      _hi = new HiDelegate()
    }
  }

  function testGenericMethod() {
    var hi: IHi = new Hi()
    assertEquals( Integer, hi.blah<Integer>() )
    assertEquals( 2, hi.blah2( 2 ) )
    hi = new Hi2()
    assertEquals( Integer, hi.blah<Integer>() )
    assertEquals( 3, hi.blah2( 3 ) )
  }
}