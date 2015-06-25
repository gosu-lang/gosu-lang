package gw.specContrib.annotations.newStyleAnnotations

class Errant_AnnotationsParamsTestClass {
  //Single param
  annotation MyAnnotation11 {
    function hello() : String
  }

  class MyClass11 {

    @MyAnnotation11("xyz")
    var x = 5;

    @MyAnnotation11("xyz")
    var _name: String as NameProp

    @MyAnnotation11("xyz")
    var _age: int as AgeProp

    @MyAnnotation11("xyz")
        property get Name(): String {
      return null
    }

    @MyAnnotation11("xyz")
        property set Name(s: String) {
    }

    @MyAnnotation11("xyz")
    function hello() {
      @MyAnnotation11("xyz")      //## issuekeys: ANNOTATIONS ARE NOT ALLOWED HERE
          var str = "zyxn"
    }
  }

  //More than one param
//Single param
  annotation MyAnnotation12 {
    function foo() : String
    function bar() : String
  }

  @MyAnnotation12("foo")      //## issuekeys: 'MYANNOTATION12(JAVA.LANG.STRING, JAVA.LANG.STRING)' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.ANNOTATIONS.NEWSTYLE.ERRANT_ANNOTATIONSPARAMSTESTCLASS.MYANNOTATION12' CANNOT BE APPLIED TO '(JAVA.LANG.STRING)'
  class SampleClass1{}

  @MyAnnotation12(:foo = "foo", :bar = "bar")
  class SampleClass2{}

  @MyAnnotation12(:bar = "bar", :foo = "foo")
  class SampleClass3{}

  //IDE-2634 - Parser issue
  @MyAnnotation12("bar", :foo = "sfd") //## issuekeys: ERROR
  class SampleClass4{}

  @MyAnnotation12("foo", 42)      //## issuekeys: 'MYANNOTATION12(JAVA.LANG.STRING, JAVA.LANG.STRING)' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.ANNOTATIONS.NEWSTYLE.ERRANT_ANNOTATIONSPARAMSTESTCLASS.MYANNOTATION12' CANNOT BE APPLIED TO '(JAVA.LANG.STRING, INT)'
  class SampleClass5{}


  @MyAnnotation12("foo", "bar")
  class MyClass12 {

    @MyAnnotation12("foo", "bar")
    var x = 5;

    @MyAnnotation12("foo", "bar")
    var _name: String as NameProp

    @MyAnnotation12("foo", "bar")
    var _age: int as AgeProp

    @MyAnnotation12("foo", "bar")
        property get Name(): String {
      return null
    }

    @MyAnnotation12("foo", "bar")
        property set Name(s: String) {
    }

    @MyAnnotation12("foo", "bar")
    function hello() {
      @MyAnnotation12("foo", "bar")      //## issuekeys: ANNOTATIONS ARE NOT ALLOWED HERE
          var str = "zyxn"
    }
  }


  //Default params
  annotation MyAnnotation13 {
    function foo() : String = "foo"
    function bar() : String = "bar"
  }

  @MyAnnotation13(:foo = "foo1")
  class MyClass13A {
  }
  @MyAnnotation13(:bar = "bar1")
  class MyClass13B {
  }
  @MyAnnotation13("foo2", :bar = "bar2")
  class MyClass13C {
  }
  @MyAnnotation13(:bar = "bar3", "foo3")      //## issuekeys: 'MYANNOTATION13(JAVA.LANG.STRING, JAVA.LANG.STRING)' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.ANNOTATIONS.NEWSTYLE.ERRANT_ANNOTATIONSPARAMSTESTCLASS.MYANNOTATION13' CANNOT BE APPLIED TO '(JAVA.LANG.STRING, JAVA.LANG.STRING)'
  class MyClass13D {
  }
  //IDE-2634 - Parser issue
  @MyAnnotation13("bar4", :foo = "foo4")     //## issuekeys:  ERROR
  class MyClass13E {
  }
}