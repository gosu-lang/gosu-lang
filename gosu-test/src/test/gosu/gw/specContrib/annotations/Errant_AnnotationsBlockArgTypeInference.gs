package gw.specContrib.annotations

class Errant_AnnotationsBlockArgTypeInference {

  static class Author1 implements IAnnotation{
     var _name : String as AuthorName
     construct() {}
     construct(s: block(p: String): int){
    }
  }

  @Author1(\ p  -> p.length())  //p is infered as string here. p.length is good
  class TestClass{}
}