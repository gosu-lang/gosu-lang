package gw.specContrib.annotations.newStyleAnnotations

public class Errant_NonRepatableRepeated {

  annotation Schedule {
  }

  //IDE-2632 - Parser issue
  @Schedule
  @Schedule      //## issuekeys: ERROR - Not a repeatable annotation
  class MyClass {
  }
}