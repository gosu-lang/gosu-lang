package gw.lang.spec_old.generics
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource 
class Errant_BadlyConstrainedClass<T> extends SelfConstrainedGenericAbstractJavaClass<T> 
{

}
