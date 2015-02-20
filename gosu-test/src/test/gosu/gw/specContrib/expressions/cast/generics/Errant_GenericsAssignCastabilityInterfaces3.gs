package gw.specContrib.expressions.cast.generics

class Errant_GenericsAssignCastabilityInterfaces3 {
  interface Super<T> {
  }

  interface Sub<T> extends Super<T> {
  }

  interface Parent {
  }

  interface Child extends Parent {
  }

  var superParent: Super<Parent>
  var superChild: Super<Child>

  var subParent: Sub<Parent>
  var subChild: Sub<Child>

  class Cl<P extends Parent, C extends Child, T extends P> {

    var superP: Super<P>
    var subP: Sub<P>
    var superC: Super<C>
    var subC: Sub<C>
    var superT: Super<T>
    var subT: Sub<T>

    function testAssignability() {
      //Set#1
      var superP111: Super<Parent> = superP
      var superP112: Super<Parent> = subP
      var superP113: Super<Parent> = superC
      var superP114: Super<Parent> = subC
      var superP115: Super<Parent> = superT
      var superP116: Super<Parent> = subT

      var subP111: Sub<Parent> = superP                  //## issuekeys: MSG_TYPE_MISMATCH
      var subP112: Sub<Parent> = subP
      var subP113: Sub<Parent> = superC                  //## issuekeys: MSG_TYPE_MISMATCH
      var subP114: Sub<Parent> = subC
      var subP115: Sub<Parent> = superT                  //## issuekeys: MSG_TYPE_MISMATCH
      var subP116: Sub<Parent> = subT

      var superC111: Super<Child> = superP                  //## issuekeys: MSG_TYPE_MISMATCH
      var superC112: Super<Child> = subP                  //## issuekeys: MSG_TYPE_MISMATCH
      var superC113: Super<Child> = superC
      var superC114: Super<Child> = subC
      var superC115: Super<Child> = superT                  //## issuekeys: MSG_TYPE_MISMATCH
      var superC116: Super<Child> = subT                  //## issuekeys: MSG_TYPE_MISMATCH

      var subC111: Sub<Child> = superP                  //## issuekeys: MSG_TYPE_MISMATCH
      var subC112: Sub<Child> = subP                  //## issuekeys: MSG_TYPE_MISMATCH
      var subC113: Sub<Child> = superC                  //## issuekeys: MSG_TYPE_MISMATCH
      var subC114: Sub<Child> = subC
      var subC115: Sub<Child> = superT                  //## issuekeys: MSG_TYPE_MISMATCH
      var subC116: Sub<Child> = subT                  //## issuekeys: MSG_TYPE_MISMATCH

      //Set#2
      var p111: Super<P> = superParent                  //## issuekeys: MSG_TYPE_MISMATCH
      var p112: Super<P> = subParent                  //## issuekeys: MSG_TYPE_MISMATCH
      var p113: Sub<P> = superParent                  //## issuekeys: MSG_TYPE_MISMATCH
      var p114: Sub<P> = subParent                  //## issuekeys: MSG_TYPE_MISMATCH

      var p211: Super<P> = superChild                  //## issuekeys: MSG_TYPE_MISMATCH
      var p212: Super<P> = subChild                  //## issuekeys: MSG_TYPE_MISMATCH
      var p213: Sub<P> = superChild                  //## issuekeys: MSG_TYPE_MISMATCH
      var p214: Sub<P> = subChild                  //## issuekeys: MSG_TYPE_MISMATCH

      var c111: Super<C> = superParent                  //## issuekeys: MSG_TYPE_MISMATCH
      var c112: Super<C> = subParent                  //## issuekeys: MSG_TYPE_MISMATCH
      var c113: Sub<C> = superParent                  //## issuekeys: MSG_TYPE_MISMATCH
      var c114: Sub<C> = subParent                  //## issuekeys: MSG_TYPE_MISMATCH

      var c211: Super<C> = superChild                  //## issuekeys: MSG_TYPE_MISMATCH
      var c212: Super<C> = subChild                  //## issuekeys: MSG_TYPE_MISMATCH
      var c213: Sub<C> = superChild                  //## issuekeys: MSG_TYPE_MISMATCH
      var c214: Sub<C> = subChild                  //## issuekeys: MSG_TYPE_MISMATCH

      var t111: Super<T> = superParent                  //## issuekeys: MSG_TYPE_MISMATCH
      var t112: Super<T> = subParent                  //## issuekeys: MSG_TYPE_MISMATCH
      var t113: Sub<T> = superParent                  //## issuekeys: MSG_TYPE_MISMATCH
      var t114: Sub<T> = subParent                  //## issuekeys: MSG_TYPE_MISMATCH

      var t211: Super<T> = superChild                  //## issuekeys: MSG_TYPE_MISMATCH
      var t212: Super<T> = subChild                  //## issuekeys: MSG_TYPE_MISMATCH
      var t213: Sub<T> = superChild                  //## issuekeys: MSG_TYPE_MISMATCH
      var t214: Sub<T> = subChild                  //## issuekeys: MSG_TYPE_MISMATCH


    }
    function testCastability() {
      //Set#1
      var superP111 = superP as Super<Parent>  //## issuekeys: MSG_UNNECESSARY_COERCION
      var superP112 = subP as Super<Parent>  //## issuekeys: MSG_UNNECESSARY_COERCION
      var superP113 = superC as Super<Parent>  //## issuekeys: MSG_UNNECESSARY_COERCION
      var superP114 = subC as Super<Parent>  //## issuekeys: MSG_UNNECESSARY_COERCION
      var superP115 = superT as Super<Parent>  //## issuekeys: MSG_UNNECESSARY_COERCION
      var superP116 = subT as Super<Parent>  //## issuekeys: MSG_UNNECESSARY_COERCION

      var subP111 = superP as Sub<Parent>
      var subP112 = subP as Sub<Parent>  //## issuekeys: MSG_UNNECESSARY_COERCION
      var subP113 = superC as Sub<Parent>
      var subP114 = subC as Sub<Parent>  //## issuekeys: MSG_UNNECESSARY_COERCION
      var subP115 = superT as Sub<Parent>
      var subP116 = subT as Sub<Parent>  //## issuekeys: MSG_UNNECESSARY_COERCION

      var superC111 = superP as Super<Child>
      var superC112 = subP as Super<Child>
      var superC113 = superC as Super<Child>  //## issuekeys: MSG_UNNECESSARY_COERCION
      var superC114 = subC as Super<Child>  //## issuekeys: MSG_UNNECESSARY_COERCION
      var superC115 = superT as Super<Child>
      //IDE-1731 - Issue in OS Gosu
      var superC116 = subT as Super<Child>


      var subC111 = superP as Sub<Child>
      var subC112 = subP as Sub<Child>
      var subC113 = superC as Sub<Child>
      var subC114 = subC as Sub<Child>  //## issuekeys: MSG_UNNECESSARY_COERCION
      var subC115 = superT as Sub<Child>
      var subC116 = subT as Sub<Child>

      //Set#2
      var p111 = superParent as Super<P>
      var p112 = subParent as Super<P>
      var p113= superParent as Sub<P>
      var p114= subParent as Sub<P>

      var p211 = superChild as Super<P>
      var p212 = subChild as Super<P>
      var p213 = superChild as Sub<P>
      var p214 = subChild as Sub<P>

      var c111 = superParent as Super<C>
      var c112 = subParent as Super<C>
      var c113 = superParent as Sub<C>
      var c114 = subParent as Sub<C>

      var c211 = superChild as Super<C>
      var c212 = subChild as Super<C>
      var c213 = superChild as Sub<C>
      var c214 = subChild as Sub<C>

      var t111 = superParent as Super<T>
      var t112 = subParent as Super<T>
      var t113 = superParent as Sub<T>
      var t114 = subParent as Sub<T>

      var t211 = superChild as Super<T>
      var t212 = subChild as Super<T>
      var t213 = superChild as Sub<T>
      var t214 = subChild as Sub<T>

    }
  }

}