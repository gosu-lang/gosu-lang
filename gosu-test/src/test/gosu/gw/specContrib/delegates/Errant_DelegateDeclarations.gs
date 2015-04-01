package gw.specContrib.delegates

uses java.io.Serializable
uses java.lang.CharSequence
uses java.lang.Cloneable
uses java.lang.Comparable
uses java.lang.Runnable

class Errant_DelegateDeclarations {
  abstract class AbstractClass implements Cloneable {}

  class ClassWithDelegates extends AbstractClass implements Serializable, Comparable<String> {
    delegate d1 represents Cloneable, Serializable, Comparable<String>
  }

  class ClassWithDelegates1 extends AbstractClass implements Serializable, Comparable<String> {  //## issuekeys: MSG_
    delegate d1 represents Cloneable, Serializable, Comparable<CharSequence>
  }

  class ClassWithDelegates2 extends AbstractClass implements Serializable {
    delegate d1 represents Cloneable, Serializable
    delegate d2 represents AbstractClass      //## issuekeys: MSG_
    delegate d3 represents Runnable           //## issuekeys: MSG_
    delegate d4: int represents Serializable  //## issuekeys: MSG_
    delegate d5: AbstractClass represents Serializable  //## issuekeys: MSG_
    delegate d6: Cloneable & Serializable represents Cloneable, Serializable
  }

  class ExtendClassWithDelegates extends ClassWithDelegates {
  }
}