package gw.specContrib.enhancements
uses java.lang.annotation.Target
uses java.lang.annotation.ElementType
uses java.lang.annotation.RetentionPolicy
uses java.lang.annotation.Retention

@Target( ElementType.PARAMETER )
@Retention(RetentionPolicy.RUNTIME)
annotation MyReceiverAnno {
  function value() : Class
}