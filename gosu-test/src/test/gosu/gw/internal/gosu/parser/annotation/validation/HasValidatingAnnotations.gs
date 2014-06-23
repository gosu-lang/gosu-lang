package gw.internal.gosu.parser.annotation.validation
uses gw.testharness.DoNotVerifyResource

@DeclarationSiteAnnotationValidator1
@DoNotVerifyResource
class HasValidatingAnnotations {

  @DeclarationSiteAnnotationValidator1
  construct() {
  }

  @DeclarationSiteAnnotationValidator1
  function HasValidatingAnnotations(b: boolean) {
  }

  @DeclarationSiteAnnotationValidator1
  function foo() {
  }

  @DeclarationSiteAnnotationValidator1
  property get Prop1() : String { return null }

  @DeclarationSiteAnnotationValidator1
  property set Prop2(s : String ) {}


  @DeclarationSiteAnnotationValidator1
  class Inner {

    @DeclarationSiteAnnotationValidator1
    construct() {
    }

    @DeclarationSiteAnnotationValidator1
    function Inner(b: boolean) {
    }

    @DeclarationSiteAnnotationValidator1
    function fooInner() {
    }

    @DeclarationSiteAnnotationValidator1
    property get Prop1Inner() : String { return null }

    @DeclarationSiteAnnotationValidator1
    property set Prop2Inner(s : String ) {}
  }
}
