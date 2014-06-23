package gw.internal.gosu.regression
uses gw.test.TestClass

class PropertyReflectionRegressionTest extends TestClass {

  var x : Object

  function testPropertyReflectionWorksAgainstRuntimeType() {
    this.x = new HasAProperty()
    this.x["Value"] = "foo"
    assertEquals("foo", (this.x as HasAProperty).Value)  
  }

  function testReflectivePropertySetWhereGetterIsDefinedInJavaAndSetterIsOverriddenInGosu() {
    var obj = new OverridesPropertySetter()
    OverridesPropertySetter.Type.TypeInfo.getProperty("StringProp").Accessor.setValue(obj, "foo")
    assertEquals("foo-overridden", obj.StringProp)
  }

  function testReflectivePropertyGetWhereGetterIsDefinedInJavaAndSetterIsOverriddenInGosu() {
    var obj = new OverridesPropertySetter()
    obj.StringProp = "foo"
    assertEquals("foo-overridden", OverridesPropertySetter.Type.TypeInfo.getProperty("StringProp").Accessor.getValue(obj))
  }

  function testReflectivePropertySetWhereSetterIsDefinedInJavaAndGetterIsOverriddenInGosu() {
    var obj = new OverridesPropertyGetter()
    OverridesPropertyGetter.Type.TypeInfo.getProperty("StringProp").Accessor.setValue(obj, "foo")
    assertEquals("overridden", obj.StringProp)
  }

  function testReflectivePropertyGetWhereSetterIsDefinedInJavaAndGetterIsOverriddenInGosu() {
    var obj = new OverridesPropertyGetter()
    obj.StringProp = "foo"
    assertEquals("overridden", OverridesPropertyGetter.Type.TypeInfo.getProperty("StringProp").Accessor.getValue(obj))
  }

  public static class HasAProperty {
    var _value : String as Value  
  }

  public static class OverridesPropertyGetter extends JavaClassWithGetterAndSetter {
    override property get StringProp() : String {
      return "overridden"
    }
  }

  public static class OverridesPropertySetter extends JavaClassWithGetterAndSetter {
    override property set StringProp( arg : String ) {
      super.StringProp = arg + "-overridden"
    }
  }
}
