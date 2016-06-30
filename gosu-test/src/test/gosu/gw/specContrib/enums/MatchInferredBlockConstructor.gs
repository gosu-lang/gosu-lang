package gw.specContrib.enums

enum Foo {
  Bar(\ b -> {}),
  Baz(\ b:int -> {}),      //## issuekeys: 'CONSTRUCT(BLOCK(JAVA.LANG.STRING))' IN 'GW.SPECCONTRIB.ENUMS.FOO' CANNOT BE APPLIED TO '(BLOCK(INT))'
  Bax(\ b:String -> {}),
  Box(\ b,c -> {}),
  Bix(\ b:String,c:int ->  {}),      //## issuekeys: CANNOT RESOLVE CONSTRUCTOR 'FOO(BLOCK(STRING, INT))'
  Bex(\ b:int,c:String ->  {}),      //## issuekeys: 'CONSTRUCT(BLOCK(JAVA.LANG.STRING, JAVA.LANG.STRING))' IN 'GW.SPECCONTRIB.ENUMS.FOO' CANNOT BE APPLIED TO '(BLOCK(INT, STRING))'
  Bux(\ b:String,c:String -> {})

  private construct(b : block(v : String)) {}
  private construct(b : block(v1 : String, v2:String)) {}
}