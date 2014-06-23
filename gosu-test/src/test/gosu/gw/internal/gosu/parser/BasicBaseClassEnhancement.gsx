package gw.internal.gosu.parser

enhancement BasicBaseClassEnhancement : gw.internal.gosu.parser.GosuProgramSuperClassTest.BasicBaseClass {

  function basicMethodEnh() : int {
    return 42;
  }

  function basicMethodStringEnh() : String {
    return "42";
  }

  static function staticMethodEnh() : int {
    return 42;
  }

  static function staticMethodStringEnh() : String {
    return "42";
  }

  property get BasicPropEnh() : int {
    return 42;
  }

  property get BasicPropStringEnh() : String {
    return "42";
  }

  property get StaticPropEnh() : int {
    return 42;
  }

  property get StaticPropStringEnh() : String {
    return "42";
  }
}

