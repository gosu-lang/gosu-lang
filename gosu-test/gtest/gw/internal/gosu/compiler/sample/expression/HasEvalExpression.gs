package gw.internal.gosu.compiler.sample.expression
uses java.lang.Integer
uses java.lang.StringBuilder
uses java.lang.CharSequence

class HasEvalExpression
{
  var _data : String

  function testCanAccessEnclosingField() : String
  {
    var y = eval( "_data = \"Yay\"" )
    return _data
  }

  function testCanAccessEnclosingPrivateMethod() : String
  {
    var y = eval( "privateMethod()" ) as String
    return y
  }

  function testCanAccessEnclosingPrivateMethodReturnsInt() : int
  {
    return eval( "privateMethodReturnsInt()" ) as java.lang.Integer
  }

  function testCanAccessEnclosingPrivateMethodWithArgs() : String
  {
    return eval( "privateMethodWithArgs( 2, new int[] {1,2,3}, \"hello\" )" ) as String
  }

  function testCanAccessEnclosingPrivateMethodFromInnerClass() : String
  {
    return new Foo().testCanAccessEnclosingPrivateMethod()
  }

  function testCanAccessEnclosingPrivateMethodReturnsIntFromInnerClass() : int
  {
    return new Foo().testCanAccessEnclosingPrivateMethodReturnsInt()
  }

  function testCanAccessEnclosingPrivateMethodWithArgsFromInnerClass() : String
  {
    return new Foo().testCanAccessEnclosingPrivateMethodWithArgs()
  }

  function testCanAccessThis() : String
  {
    var y = eval( "this._data = \"Hey\"" )
    return _data
  }

  private function privateMethod() : String
  {
    _data = "I'm private"
    return _data
  }
  private function privateMethodReturnsInt() : int
  {
    return 7
  }
  private function privateMethodWithArgs( i : int, ia : int[], obj : String ) : String
  {
    return obj
  }

  function testEvalStatement() : String
  {
    eval( "_data = \"Yay\"" )
    return _data
  }

  class Foo
  {
    function testCanAccessEnclosingPrivateMethod() : String
    {
      var y = eval( "privateMethod()" ) as String
      return y
    }

    function testCanAccessEnclosingPrivateMethodReturnsInt() : int
    {
      return eval( "privateMethodReturnsInt()" ) as java.lang.Integer
    }

    function testCanAccessEnclosingPrivateMethodWithArgs() : String
    {
      return eval( "privateMethodWithArgs( 2, new int[] {1,2,3}, \"hello\" )" ) as String
    }
  }

  function testReifiedTypeVarFromGenericFunction() : String
  {
    return _testReifiedTypeVarFromGenericFunction( new StringBuilder() )
  }
  private function _testReifiedTypeVarFromGenericFunction<A extends CharSequence>( cs : A ) : String
  {
    var x = "hello"
    return eval( "x + A" ) as String
  }

  function testReifiedTypeVarParameterizedWithGosuClassFromGenericFunction() : String
  {
    return _testReifiedTypeVarParameterizedWithGosuClassFromGenericFunction( this )
  }
  private function _testReifiedTypeVarParameterizedWithGosuClassFromGenericFunction<A>( cs : A ) : String
  {
    return eval( "A" ) as String
  }

  function testReifiedTypeVarParameterizedWithNullFromGenericFunction() : String
  {
    return _testReifiedTypeVarParameterizedWithGosuClassFromGenericFunction( null )
  }

  function testReifiedTypeVarParameterizedWithNullFromGenericFunctionWithNullCapture() : String
  {
    return _testReifiedTypeVarParameterizedWithNullFromGenericFunctionWithNullCapture( null )
  }
  private function _testReifiedTypeVarParameterizedWithNullFromGenericFunctionWithNullCapture<A>( cs : A ) : String
  {
    return eval( "A.Type.Name + cs" ) as String
  }

  function testReifiedTypeVarsParameterizedWithGosuClassFromGenericFunction() : String
  {
    return _testReifiedTypeVarsParameterizedWithGosuClassFromGenericFunction( this, 3 as java.lang.Integer )
  }
  private function _testReifiedTypeVarsParameterizedWithGosuClassFromGenericFunction<A, B>( cs : A, bs : B ) : String
  {
    return eval( "A.Type.Name + B.Type.Name" ) as String
  }

  function testReifiedTypeVarsParameterizedWithGosuClassFromGenericFunctionWithCapturedSymbols() : String
  {
    return _testReifiedTypeVarsParameterizedWithGosuClassFromGenericFunctionWithCapturedSymbols( this, 3 as java.lang.Integer )
  }
  private function _testReifiedTypeVarsParameterizedWithGosuClassFromGenericFunctionWithCapturedSymbols<A, B>( cs : A, bs : B ) : String
  {
    return eval( "A.Type.Name + B.Type.Name + typeof cs + bs " ) as String
  }

  function testReifiedTypeVarFromGenericFunctionInNestedEval() : String
  {
    return _testReifiedTypeVarFromGenericFunctionInNestedEval( "" )
  }
  private function _testReifiedTypeVarFromGenericFunctionInNestedEval<A extends CharSequence>( cs : A ) : String
  {
    var x = "hello_nested_"
    return eval( "eval( \"x + A\" )" ) as String
  }

  function testNestedEvalWithCapture() : int
  {
    var x = 1
    var y = eval( "eval( \"x++\" )" )
    return x
  }

  function testTripleNestedEvalWithCapture() : int
  {
    var x = 1
    var y = eval( "eval( \"eval( \\\"x++\\\" )\" )" )
    return x
  }

  function testCapturedSymbolsFromStaticMethod() : String
  {
    return _testCapturedSymbolsFromStaticMethod()
  }
  static function _testCapturedSymbolsFromStaticMethod() : String
  {
    var l = new String[] {"a", "b", "c"}
    var res = ""
    for( str in l index i )
    {
      res += eval( "str + i + res" )
    }
    return res
  }

  function testReifiedTypeVarFromStaticMethod() : Type
  {
    return _testReifiedTypeVarFromStaticMethod( new java.lang.StringBuilder() )
  }
  private static function _testReifiedTypeVarFromStaticMethod<M>( value : M ) : Type
  {
    return eval( "M" ) as Type
  }

  function testReifiedTypeVarFromStaticMethodWithCapturedSymbols() : String
  {
    return _testReifiedTypeVarFromStaticMethodWithCapturedSymbols( new java.lang.StringBuilder() )
  }
  private static function _testReifiedTypeVarFromStaticMethodWithCapturedSymbols<M>( value : M ) : String
  {
    var dude = "duuude"
    return eval( "M.Type.Name + dude" ) as String
  }

  function testReifiedTypeVarsFromStaticMethodWithCapturedSymbols() : String
  {
    return _testReifiedTypeVarsFromStaticMethodWithCapturedSymbols( new java.lang.StringBuilder(), new java.lang.Integer( 3 ) )
  }
  private static function _testReifiedTypeVarsFromStaticMethodWithCapturedSymbols<M,N>( value : M, value2 : N ) : String
  {
    var dude = "duuude"
    return eval( "M.Type.Name + N.Type.Name + dude" ) as String
  }

  function testReifiedTypeVarFromInnerClass() : Type
  {
    return new InnerClass<Integer>().innerClassMethod()
  }

  class InnerClass<T>
  {
    function innerClassMethod() : Type
    {
      return eval( "T" ) as Type
    }
  }
}
