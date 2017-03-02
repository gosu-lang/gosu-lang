package gw.specContrib.classes.property_Declarations.new_syntax

uses java.lang.annotation.ElementType
uses java.lang.annotation.Target
uses java.lang.annotation.RetentionPolicy
uses java.lang.annotation.Retention

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
annotation MyMethodAnno2 {
  function value() : int
}