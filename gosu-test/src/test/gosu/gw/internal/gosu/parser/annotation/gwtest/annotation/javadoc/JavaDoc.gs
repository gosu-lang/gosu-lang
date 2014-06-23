package gw.internal.gosu.parser.annotation.gwtest.annotation.javadoc

/**
  * class doc
  */
class JavaDoc {
  /**
    * Doc for feature
    * line 2
    */
  public var varFeature : String

  /**
    * Doc for var property
    * line 2
    */
  private var _varProperty : String as varProperty

  /**
    * Doc for property
    * line 2
    */
  public property get prop() : String { return "" }

  /**
    * Doc for f1
    * line 2
    *
    * @return return doc
    */
  public function f1() : String { return "" }

  /**
    * Doc for f2
    * line 2
    *
    * @return return doc
    * multi-line
    */
  public function f2() : String { return "" }

  /**
    * Doc for f3
    * @param s string param
    * multi-line
    */
  public function f3(s : String) {}

  /**
    * Doc for f4
    * @param s1 string param1
    * multi-line
    * @param s2 string param2
    * multi-line
    */
  public function f4(s1 : String, s2 : String) {}

  function ReturnsDoc() {}
}
