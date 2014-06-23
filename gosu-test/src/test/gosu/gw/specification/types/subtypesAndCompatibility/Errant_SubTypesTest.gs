package gw.specification.types.subtypesAndCompatibility
uses java.lang.*

class Errant_SubTypesTest {
  class cA extends cB {}
  class cB extends cC {}
  class cC {}

  function subTypeAssignmentWithClasses() {
    var a : cA = null
    var b : cB = null
    var c : cC = null
    a = a  //## issuekeys: MSG_SILLY_ASSIGNMENT
    b = a
    c = a
    a = b  //## issuekeys: MSG_TYPE_MISMATCH
    b = b  //## issuekeys: MSG_SILLY_ASSIGNMENT
    c = b
    a = c  //## issuekeys: MSG_TYPE_MISMATCH
    b = c  //## issuekeys: MSG_TYPE_MISMATCH
    c = c  //## issuekeys: MSG_SILLY_ASSIGNMENT
  }

  interface iA extends iB {}
  interface iB {}

  function subTypeAssignmentWithInterfaces() {
    var a : iA = null
    var b : iB = null
    a = a  //## issuekeys: MSG_SILLY_ASSIGNMENT
    b = a
    a = b  //## issuekeys: MSG_TYPE_MISMATCH
    b = b  //## issuekeys: MSG_SILLY_ASSIGNMENT
  }

  class A implements iA {}
  class B implements iA {}
  class C extends A {}
  class D extends B {}

  function subTypeAssignmentWithClassesAndInterfaces() {
    var a : A = null
    var ia : iA = null
    var b : B = null
    var ib : iB = null
    var c : C = null
    var d : D = null

    // t1 = A  t2 = iA
    ia = a

    // t1 = B  t2 = iB
    ib = b

    // t1 = C t2 = iA
    ia = c

    // t1 = D t2 = iB
    ib = d

    a = ia  //## issuekeys: MSG_TYPE_MISMATCH
    b = a  //## issuekeys: MSG_TYPE_MISMATCH
    ib = a
    a =  b  //## issuekeys: MSG_TYPE_MISMATCH
    ia =  b
    b = ia  //## issuekeys: MSG_TYPE_MISMATCH
    ib = ia
    a =  ib  //## issuekeys: MSG_TYPE_MISMATCH
    ia =  ib  //## issuekeys: MSG_TYPE_MISMATCH
    b = ib  //## issuekeys: MSG_TYPE_MISMATCH
    c = a  //## issuekeys: MSG_TYPE_MISMATCH
    d = a  //## issuekeys: MSG_TYPE_MISMATCH
    c = b  //## issuekeys: MSG_TYPE_MISMATCH
    d = b  //## issuekeys: MSG_TYPE_MISMATCH
    c = ia  //## issuekeys: MSG_TYPE_MISMATCH
    d = ia  //## issuekeys: MSG_TYPE_MISMATCH
    c = ib  //## issuekeys: MSG_TYPE_MISMATCH
    d = ib  //## issuekeys: MSG_TYPE_MISMATCH
    a = c
    b = c  //## issuekeys: MSG_TYPE_MISMATCH
    ib = c
    d = c  //## issuekeys: MSG_TYPE_MISMATCH
    a = d  //## issuekeys: MSG_TYPE_MISMATCH
    ia = d
    b = d
    c = d  //## issuekeys: MSG_TYPE_MISMATCH
  }

  function subTypeAssignmentWithArrays() {
    var arr_cA : cA[] = null
    var arr_cB : cB[] = null

    arr_cB = arr_cA
    arr_cA = arr_cB  //## issuekeys: MSG_TYPE_MISMATCH
  }

  structure struct {}

  enum enumeration {ONE, TWO}

  function subTypeOfObject() {
    var str : String = null
    var obj : Object = null
    var lst : List = null
    var blk : block(s : String) : int = null
    var arr : int[] = null
    var struct : struct = null
    var e : enumeration = ONE
    var o : Object = null

    o = str
    o = obj
    o = lst
    o = blk
    o = arr
    o = struct
    o = e
  }
}
