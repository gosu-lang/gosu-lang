package gw.spec.core.expressions.memberaccess
uses gw.lang.reflect.gs.IGosuClassTypeInfo
uses gw.lang.reflect.IType
uses gw.lang.reflect.IPropertyAccessor

class MemberAccessTestHelper {

  public static function findProp(t : IType, propName : String) : IPropertyAccessor {
    var props = t.TypeInfo.Properties.where(\p -> p.Name == propName)
    if (props.size() == 1) {
      return props[0].Accessor
    } else {
      return (t.TypeInfo as IGosuClassTypeInfo).DeclaredProperties.singleWhere(\p -> p.Name == propName).Accessor  
    }
  }

}
