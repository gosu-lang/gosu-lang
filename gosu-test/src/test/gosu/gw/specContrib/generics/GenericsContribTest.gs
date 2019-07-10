package gw.specContrib.generics

uses gw.test.TestClass
uses gw.lang.reflect.IRelativeTypeInfo

class GenericsContribTest extends TestClass {

  function testRecursiveBoundingTypePreserved() {
    assertTrue( RecursiveGosu.Type.Valid )
    assertEquals( RecursiveGosu.Type.GenericType.GenericTypeVariables[0].BoundingType.TypeParameters[0], RecursiveGosu.Type.GenericType.GenericTypeVariables[0].TypeVariableDefinition.Type )
    assertEquals( RecursiveJava.Type.GenericType.GenericTypeVariables[0].BoundingType.TypeParameters[0], RecursiveJava.Type.GenericType.GenericTypeVariables[0].TypeVariableDefinition.Type )
  }

  function testJavaClassWithRecursiveTypeVarRetainsRecursiveBounds() {
    assertSame( JavaClassWithRecursiveTypeVar.Type.GenericType.GenericTypeVariables[0].BoundingType,
                java.util.List.Type.GenericType.getParameterizedType( {JavaClassWithRecursiveTypeVar.Type.GenericType.GenericTypeVariables[0].TypeVariableDefinition.Type} ) )
    assertEquals( JavaClassWithRecursiveTypeVar.Type.GenericType.GenericTypeVariables[0].BoundingType.Name,
                  "java.util.List<T>" )
  }

  function testContravariantWildcardTypeUsesBoundingTypeOfItsTypeVarBoundIfOnNonFunctionalInterface() {
    assertEquals( BeanPopulatorNonFunctional<Bean>, (Bean.Type.TypeInfo as IRelativeTypeInfo).getMethod( Bean, "addPopulatorNonFunctional", {BeanPopulatorNonFunctional<Bean>} ).Parameters[0].FeatureType )
  }

  function testContravariantWildcardTypeRetainsTypeVarAsUPPERBoundIfOnFunctionalInterface() {
    assertEquals( "gw.specContrib.generics.BeanPopulator<T>", (Bean.Type.TypeInfo as IRelativeTypeInfo).getMethod( Bean, "addPopulator", {BeanPopulator<Bean>} ).Parameters[0].FeatureType.Name )
  }

  function testMohrRecursives() {
    var yey: Yey
    assertEquals( Yey, statictypeof yey.foo() )
  }

  function testDefaultMetaType() {
    assertEquals( String, typeof new DefaultMetaType().foo() )
  }

  function testGosuReferencesRecursiveJavaTypeVar() {
    var t = new GosuReferencesRecursiveJavaTypeVar()
    assertEquals( MuhEnum.HI, t.getMeBack( MuhEnum.HI ) )
  }

  function testInferTypeParamsInCtor() {
    var x : List<String> = new ArrayList()

    var y: Map<String, Integer> = new StringMap()
    assertEquals( StringMap<Integer>, typeof y )
  }

  static class StringMap<V> extends HashMap<String, V> {
  }

  function testAnonymousImplicitlyPassingOuterTypeParams() {
    new Foo( "hi" ).foo( "bye" )
  }
  static class Foo<T extends CharSequence> {
    construct( t: T ) {
      new BeanPopulator2<T>() {
        override function execute( a: T ) {
          print( T )
        }
      }.execute( t )
    }
    function foo( t: T ) {
      new BeanPopulator2<T>() {
        override function execute( a: T ) {
          print( T )
        }
      }.execute( t )
    }
  }

  function testRefiedTypeVarToClassCoercion() {
    assertEquals( Hello.Name, new ReifySample<Hello>().className() )
  }
  static class ReifySample<T> {
    function className() : String {
      return ClassStuff.forClass( T ).replace( '$', '.' );
    }
  }
  static class Hello {}

  function testReifyEnclosingTypeVarFromGenericFunctionParameterizedWithDefaultBoundingType() {
    var hello = new ReifyEnclosingTypeVar<String>()
    var type = hello.foo()
    assertEquals( ReifyEnclosingTypeVar<String>, type )
  }
  static class ReifyEnclosingTypeVar<T> {
    reified function foo<E extends ReifyEnclosingTypeVar<T>>() : Type<E> {
      return E
    }
  }

  function testGenericSignatureHandlesCompoundTypes() {
    var ret = (HasFunctionWithGenericReturnTypeAndCompoundTypeParameter as Class).getMethod( "foo", {} ).GenericReturnType
  }
  static class HasFunctionWithGenericReturnTypeAndCompoundTypeParameter {
    function foo() : java.util.List<java.io.Serializable & java.lang.Comparable<java.lang.Object>> {
      return null
    }
  }
}
