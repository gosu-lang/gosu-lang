package gw.specContrib.annotations.newStyleAnnotations

uses java.lang.annotation.Repeatable

public class Errant_RepeatableTestClass {
  //IDE-2631 - Parser issue. Should not show error
  @Repeatable(Schedules)
  annotation Schedule {

  }
  annotation Schedules {
    function  value(): Schedule[];
  }

  @Schedule
  annotation AnotherAnnotation {
  }

  @Schedule
  interface MyInterface {
  }

  //IDE-1416 - OS Gosu issue
  class MyClass1 implements Schedule {          //## issuekeys: ERROR - OS Gosu shows error here
  }

  @Schedule
  @Schedule
  @Schedule
  class MyClass2 {

    @Schedule
    @Schedule
    @Schedule
    var x = 5;

    @Schedule
    @Schedule
    @Schedule
    var _name: String as NameProp

    @Schedule
    @Schedule
    @Schedule
        property get Name(): String {
      return null
    }

    @Schedule
    @Schedule
    @Schedule
        property set Name(s: String) {
    }

    @Schedule
    @Schedule
    @Schedule
    function hello() {
      @Schedule      //## issuekeys: ANNOTATIONS ARE NOT ALLOWED HERE
          var str = "zyxn"
    }
  }
}