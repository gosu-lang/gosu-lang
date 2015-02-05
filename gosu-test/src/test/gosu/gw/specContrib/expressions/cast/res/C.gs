package gw.specContrib.expressions.cast.res

uses java.lang.Deprecated
/**
 * Created by Sky on 2015/02/02 with IntelliJ IDEA.
 */
class C {
  @Deprecated
  property get deprecatedProp() : A { return new B()}

  @Deprecated
  function deprecatedMethod() : A { return new B()}

  @Deprecated
  public var deprecatedField : A = new B()

  function nonDeprecatedMethod() : A {return new B()}

}