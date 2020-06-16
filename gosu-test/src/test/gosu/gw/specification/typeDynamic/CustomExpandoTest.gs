package gw.specification.typeDynamic

uses dynamic.Dynamic
uses java.lang.*
uses gw.lang.reflect.IExpando
uses java.util.Map
uses java.util.HashMap
uses java.math.BigInteger
uses java.math.BigDecimal

class CustomExpandoTest extends gw.BaseVerifyErrantTest {
  function testCustomExpando() {
    var expando : Dynamic = new CustomExpando()
    expando.MyProperty = "hello"
    expando.Foo = 8
    assertEquals( "hello", expando.MyProperty )
    assertEquals( 8, expando.Foo )

    // Call method on Map, CustomExpando delegates back to normal dispatching
    var res = expando.size()
    assertEquals( 2, res )

    // CustomExpando directly handles foo methods
    res = expando.custom( "foo" )
    assertEquals( "special", res )
  }

  static class CustomExpando extends manifold.json.rt.api.DataBindings implements IExpando {
    override function getFieldValue( field: String ) : Object {
      return get( field )
    }

    override function setFieldValue( field: String, value: Object ) {
      put( field, value )
    }

    override property set DefaultFieldValue( name: String ) {
      setFieldValue( name, new CustomExpando() );
    }

    override function invoke( methodName: String, args: Object[] ) : Object {
      if( methodName == "custom" ) {
        return "special"
      }
      // Delegate to default behavior
      return gw.lang.reflect.ReflectUtil.invokeMethod( this, methodName, args )
    }
  }
}