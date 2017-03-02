package gw.specContrib.classes.property_Declarations.new_syntax
uses java.lang.annotation.ElementType
uses java.lang.annotation.Target
uses java.lang.annotation.Retention
uses java.lang.annotation.RetentionPolicy

@Target({ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
annotation MyCtorAnno {
  function value(): String
}