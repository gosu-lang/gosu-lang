package gw.spec.core.expressions.memberaccess
uses gw.test.TestClass
uses gw.lang.reflect.IType
uses gw.lang.reflect.IPropertyInfo
uses gw.lang.reflect.IPropertyAccessor
uses gw.lang.reflect.gs.IGosuClassTypeInfo

abstract class MemberAccessTestBase extends TestClass {

  // TODO - Request and session-scoped variables

  protected function findProp(t : IType, propName : String) : IPropertyAccessor {
    return (t.TypeInfo as IGosuClassTypeInfo).DeclaredProperties.singleWhere(\p -> p.Name == propName).Accessor  
  }

}
