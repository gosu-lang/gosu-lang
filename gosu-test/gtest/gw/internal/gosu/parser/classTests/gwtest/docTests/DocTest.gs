package gw.internal.gosu.parser.classTests.gwtest.docTests

/**
  * class doc
  */
class DocTest {
  /**
    * var doc
    */
  public var aVar : boolean

  /**
    * property doc
    */
  private var aPropVar : boolean as aProp

  /**
    * function doc
    */
  public function aMethod() {
  }

  /**
    * function doc with param and returns
    * @throws foobar
    * @param p1 doc for p1
    * @param p2 doc for p2
    * @returns returns doc
    */
  public function aMethodWithParam(p1 : String, p2 : String) : String {
    return ""
  }

  public function noDoc() {
  }

  /**
    * var2 doc
    */
  public var aVar2 : boolean

  /**
    * constructor doc
    */
  public function DocTest() {
  }

  /**
    * property2 doc
    */
  private var aProp2Var : boolean as aProp2

}
