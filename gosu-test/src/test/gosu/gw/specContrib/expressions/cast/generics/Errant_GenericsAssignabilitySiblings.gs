package gw.specContrib.expressions.cast.generics

class Errant_GenericsAssignabilitySiblings {
  class Super<T>{}
  class Sub<T> extends Super<T> {}

  class Parent {}
  class Child extends Parent {}
  class Child2 extends Parent {}

  var superParent: Super<Parent>
  var superChild: Super<Child>

  var subParent: Sub<Parent>
  var subChild: Sub<Child>

  var superChild2: Super<Child2>
  var subChild2: Sub<Child2>

  function testAssignability() {

    //IDE-1719 Parser Issue
    var a111: Super<Child2> = superParent      //## issuekeys: MSG_TYPE_MISMATCH
    //IDE-1726 Parser Issue
    var a112: Super<Child2> = superChild      //## issuekeys: MSG_TYPE_MISMATCH
    //Covered in? IDE-1719 Parser Issue
    var a113: Super<Child2> = subParent      //## issuekeys: MSG_TYPE_MISMATCH
    //IDE-1726 Parser Issue
    var a114: Super<Child2> = subChild      //## issuekeys: MSG_TYPE_MISMATCH
//    var a115: Super<Child2> = superChild2
    var a116: Super<Child2> = subChild2

    var a211: Sub<Child2> = superParent      //## issuekeys: MSG_TYPE_MISMATCH
    var a212: Sub<Child2> = superChild      //## issuekeys: MSG_TYPE_MISMATCH
    //IDE-1719 Parser Issue
    var a213: Sub<Child2> = subParent      //## issuekeys: MSG_TYPE_MISMATCH
     //IDE-1726 Parser Issue
    var a214: Sub<Child2> = subChild      //## issuekeys: MSG_TYPE_MISMATCH
    var a215: Sub<Child2> = superChild2      //## issuekeys: MSG_TYPE_MISMATCH
//    var a216: Sub<Child2> = subChild2

    var a311: Super<Parent> = superChild2
    var a312: Super<Parent> = subChild2
    var a313: Sub<Parent> = superChild2      //## issuekeys: MSG_TYPE_MISMATCH
    var a314: Sub<Parent> = subChild2

    //IDE-1726 Parser Issue
    var a411: Super<Child> = superChild2      //## issuekeys: MSG_TYPE_MISMATCH
    var a412: Super<Child> = subChild2      //## issuekeys: MSG_TYPE_MISMATCH
    var a413: Sub<Child> = superChild2      //## issuekeys: MSG_TYPE_MISMATCH
    var a414: Sub<Child> = subChild2      //## issuekeys: MSG_TYPE_MISMATCH
  }

  function testCastability() {
    var c111 = superChild2 as Super<Parent>  //## issuekeys: MSG_UNNECESSARY_COERCION
    //IDE-1727 Parser Issue
    var c112 = superChild2 as Super<Child>      //## issuekeys: MSG_TYPE_MISMATCH
//    var c113 = superChild2 as Super<Child2>
    var c114 = superChild2 as Sub<Parent>
    //IDE-1727 Parser Issue
    var c115 = superChild2 as Sub<Child>      //## issuekeys: MSG_TYPE_MISMATCH
    var c116 = superChild2 as Sub<Child2>

    var c211 = subChild2 as Super<Parent>  //## issuekeys: MSG_UNNECESSARY_COERCION
    //IDE-1727 Parser Issue
    var c212 = subChild2 as Super<Child>      //## issuekeys: MSG_TYPE_MISMATCH
    var c213 = subChild2 as Super<Child2>  //## issuekeys: MSG_UNNECESSARY_COERCION
    var c214 = subChild2 as Sub<Parent>  //## issuekeys: MSG_UNNECESSARY_COERCION
    //IDE-1727 Parser Issue
    var c215 = subChild2 as Sub<Child>      //## issuekeys: MSG_TYPE_MISMATCH
//    var c216 = subChild2 as Sub<Child2>
  }
}