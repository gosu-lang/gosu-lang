package gw.internal.gosu.parser.annotation.validation
uses gw.lang.parser.IDeclarationSiteValidator
uses gw.lang.parser.IParsedElement
uses gw.lang.parser.resources.Res

class DeclarationSiteAnnotationValidator1 implements IAnnotation, IDeclarationSiteValidator {

  override function validate(pe : IParsedElement) {
    pe.addParseException(Res.MSG_BAD_IDENTIFIER_NAME, {"DeclarationSiteAnnotationValidator1"})
  }

}
