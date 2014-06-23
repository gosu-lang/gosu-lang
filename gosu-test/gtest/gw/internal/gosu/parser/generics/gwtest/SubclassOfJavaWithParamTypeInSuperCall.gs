package gw.internal.gosu.parser.generics.gwtest

uses gw.internal.gosu.parser.generics.gwtest.java.ParameterizedConstructorTestClass

class SubclassOfJavaWithParamTypeInSuperCall extends ParameterizedConstructorTestClass<GClass> {
  construct(param : GClass) {
    super(param)
  }
}
