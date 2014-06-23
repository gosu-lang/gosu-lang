package gw.internal.gosu.parser.annotation.validation
uses gw.testharness.DoNotVerifyResource
uses gw.lang.parser.IUsageSiteValidatorReference

@IUsageSiteValidatorReference( CallSiteAnnotationValidator1 )
@DoNotVerifyResource
class HasCallSiteValidatingAnnotationReferences {

  function usage() { 
    var var1 = HasCallSiteValidatingAnnotationReferences
    var var2 = new HasCallSiteValidatingAnnotationReferences() 
    var var3 = func1()
    var var4 = this.func1()
    var var5 = Prop1
    var var6 = this.Prop1
  }

  @IUsageSiteValidatorReference( CallSiteAnnotationValidator1 )
  construct() {
  }

  @IUsageSiteValidatorReference( CallSiteAnnotationValidator1 )
  function func1() : String {
    return null
  }

  @IUsageSiteValidatorReference( CallSiteAnnotationValidator1 )
  property get Prop1() : String { return null }

}
