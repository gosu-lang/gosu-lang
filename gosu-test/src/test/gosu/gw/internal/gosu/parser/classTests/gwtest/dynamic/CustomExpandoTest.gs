package gw.internal.gosu.parser.classTests.gwtest.dynamic

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

  static class CustomExpando implements IExpando {
    var _map = new HashMap<String, Object>()

    override function getFieldValue( field: String ) : Object {
      return _map.get( field )
    }

    override function setFieldValue( field: String, value: Object ) {
      _map.put( field, value )
    }

    override function setDefaultFieldValue( name: String ) {
      setFieldValue( name, new CustomExpando() );
    }

    override function invoke( methodName: String, args: Object[] ) : Object {
      if( methodName == "custom" ) {
        return "special"
      }
      // Delegate to default behavior
      return gw.lang.reflect.ReflectUtil.invokeMethod( _map, methodName, args )
    }

    override property get Map() : java.util.Map<String, Object> {
      return _map
    }
  }
}