package gw.specContrib.generics

uses gw.test.TestClass

class GenericsContribTest extends TestClass {
  function testJavaClassWithRecursiveTypeVarRetainsRecursiveBounds() {
    assertSame( JavaClassWithRecursiveTypeVar.Type.GenericType.GenericTypeVariables[0].BoundingType,
                java.util.List.Type.GenericType.getParameterizedType( {JavaClassWithRecursiveTypeVar.Type.GenericType.GenericTypeVariables[0].TypeVariableDefinition.Type} ) )
    assertEquals( JavaClassWithRecursiveTypeVar.Type.GenericType.GenericTypeVariables[0].BoundingType.Name,
                  "java.util.List<T>" )
  }
}