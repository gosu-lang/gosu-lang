package gw.specContrib.classes.inheritance

class Errant_CyclicInheritance {
  class A extends A {}  //## issuekeys: MSG_

  class B extends C {}  //## issuekeys: MSG_

  class C extends B {}  //## issuekeys: MSG_
}