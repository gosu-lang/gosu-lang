package gw.specContrib.classes.property_Declarations.new_syntax
uses java.lang.annotation.RetentionPolicy
uses java.lang.annotation.Retention

@Retention(RetentionPolicy.RUNTIME)
annotation MyNoTargetAnno {
  function value(): int = 9
}