package gw.lang.annotations;

/**
 */
class HasRepeatableAnnos {

  @MyAnno(:value="hi", :type=String)
  @MyAnno(:value="bye", :type=String)
  function foo() {}
}
