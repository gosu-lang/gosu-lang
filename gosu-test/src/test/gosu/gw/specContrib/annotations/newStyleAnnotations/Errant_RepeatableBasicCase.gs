package gw.specContrib.annotations.newStyleAnnotations

uses java.lang.annotation.Repeatable

public class Errant_RepeatableBasicCase {
  //IDE-2631 - Parser issue. Should not show error
  @Repeatable(Schedules)
  annotation Schedule {

  }
  annotation Schedules {
    function  value(): Schedule[];
  }
  @Schedule
  @Schedule()
  @Schedule
  class MyClass{
  }
}