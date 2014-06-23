package gw.internal.gosu.compiler.sample.statement.classes

uses java.lang.StringBuffer
uses java.lang.StringBuilder

class ReflectionGenericClass<T>
{
  var _testTypeFromCtor : Type as TestTypeFromCtor

  static function paramterizeOnStringBuilder() : ReflectionGenericClass<StringBuilder>
  {
    return new ReflectionGenericClass<StringBuilder>()
  }

//  static function returnClassTypeParamFromStaticFunction() : Type
//  {
//    return T
//  }
//
//  static function indirectCallReturnClassTypeParamFromStaticFunction() : Type
//  {
//    return returnClassTypeParamFromStaticFunction()
//  }

  construct()
  {
    assignTypeFromCtor()
  }

  private function assignTypeFromCtor()
  {
    _testTypeFromCtor = T
  }

  property get TestTypeParamInProperty() : Type
  {
    return T
  }

  property get IndirectTestTypeParamInProperty() : Type
  {
    return returnClassTypeParam()
  }

  function returnClassTypeParam() : Type
  {
    return T
  }

  function indirectCallReturnClassTypeParam() : Type
  {
    return returnClassTypeParam()
  }

  function returnFunctionTypeParamFalse() : Type
  {
    return this.returnFunctionTypeParam<String>()
  }
  function returnFunctionTypeParamTrue() : Type
  {
    return this.returnFunctionTypeParam<java.lang.StringBuilder>()
  }

  private function returnFunctionTypeParam<F>() : Type
  {
    return F
  }

  function returnInstanceOfT( p : Object ) : Boolean
  {
    return p typeis T
  }

  function constructInstanceOfT() : T
  {
    return new T()
  }

  function returnJavaClassParameterizedWithT() : java.util.ArrayList<T>
  {
    return new java.util.ArrayList<T>()
  }

  function returnGosuClassParameterizedWithT() : ReflectionGenericClass<T>
  {
    return new ReflectionGenericClass<T>()
  }

  function indirectCallToGenericFunctionParameterizedWithATypeVar() : GClass<StringBuffer>
  {
    var x = new HasGenericFunction()
    return x.testMe()
  }

  function createInner() : Inner
  {
    return new Inner()
  }

  function ensureReferenceToThisDoesNotOverwriteSymbolInStack() : Type
  {
    print( this )
    return T
  }

  class Inner
  {
    function returnOuterTyperParamFromInnerClass() : Type
    {
      return T
    }
  }
}