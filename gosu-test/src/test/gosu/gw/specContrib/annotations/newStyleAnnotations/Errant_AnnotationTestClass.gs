package gw.specContrib.annotations.newStyleAnnotations

class Errant_AnnotationTestClass {
  annotation MyAnnotation1 {
  }

  @MyAnnotation1
  annotation AnotherAnnotation {
  }

  @MyAnnotation1
  interface MyInterface {
  }

  //IDE-1416 - OS Gosu issue
  class TestClass1 implements MyAnnotation1 {      //## issuekeys: ERROR
  }

  @MyAnnotation1
  class MyClass {

    @MyAnnotation1
    var x = 5;

    @MyAnnotation1
    var _name: String as NameProp

    @MyAnnotation1()
    var _age: int as AgeProp

    @MyAnnotation1
        property get Name(): String {
      return null
    }

    @MyAnnotation1
        property set Name(s: String) {
    }

    @MyAnnotation1
    function hello() {
      @MyAnnotation1      //## issuekeys: ANNOTATIONS ARE NOT ALLOWED HERE
          var str = "zyxn"
    }
  }
}