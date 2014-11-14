package gw.specification.arrays

uses java.lang.Integer

class Errant_ArrayTest {
  class A { function ohoh() {}}
  class B extends A {}
  class C extends B {}

  function arrayCreationAndAccess() {
    var u : int[]

    u = new int[2]
    var u0 = u[0]
    var u1 = u[1]
    var u2 = u[2]


    var u3 = u[true]  //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
    var u4 = u['3']
    var u5 = u[3b]
    var u6 = u[3s]
    var u7 = u[3]
    var u8 = u[3L]  //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
    var u9 = u[3.0f]  //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
    var u10 = u[3.0]  //## issuekeys: MSG_IMPLICIT_COERCION_ERROR

    u = new int[true]  //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
    u = new int['3']
    u = new int[3b]
    u = new int[3s]
    u = new int[3]
    u = new int[3L]  //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
    u = new int[3.0f]  //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
    u = new int[3.0]  //## issuekeys: MSG_IMPLICIT_COERCION_ERROR

    var uA = new A[u.length]
    var uB = new B[u.length]
    var x : A[]
    x = uA
    x = uB
    var o : Object[]
    o = uA
    o[0] = new Integer(10)
    x = o as A[]
    x[0].ohoh()

    u = null
    uA = null
    uB = null
    o = null

    u = u  //## issuekeys: MSG_SILLY_ASSIGNMENT
    u = uA  //## issuekeys: MSG_TYPE_MISMATCH
    u = uB  //## issuekeys: MSG_TYPE_MISMATCH
    u = o  //## issuekeys: MSG_TYPE_MISMATCH
    u = new int[2]
    u = new byte[2]  //## issuekeys: MSG_TYPE_MISMATCH
    u = new short[2]  //## issuekeys: MSG_TYPE_MISMATCH
    u = new char[2]  //## issuekeys: MSG_TYPE_MISMATCH

    uA = u  //## issuekeys: MSG_TYPE_MISMATCH
    uA = uA  //## issuekeys: MSG_SILLY_ASSIGNMENT
    uA = uB
    uA = o  //## issuekeys: MSG_TYPE_MISMATCH

    uB = u  //## issuekeys: MSG_TYPE_MISMATCH
    uB = uA  //## issuekeys: MSG_TYPE_MISMATCH
    uB = uB  //## issuekeys: MSG_SILLY_ASSIGNMENT
    uB = o  //## issuekeys: MSG_TYPE_MISMATCH

    o = u  //## issuekeys: MSG_TYPE_MISMATCH
    o = uA
    o = uB
    o = o  //## issuekeys: MSG_SILLY_ASSIGNMENT
  }

  function arrayInitializersAndMultidimensionalArray() {
    var x0 : A[] = { new A(), new B(), new C()}
    var x1 : A[] = { new A(), new Object(), new C()}  //## issuekeys: MSG_TYPE_MISMATCH
    var x2 : A[] = { new A(), x0[0], new C()}
    var x3 : A[] = { new x3[0], x0[0], new C()}  //## issuekeys: MSG_TYPE_MISMATCH, MSG_TYPE_MISMATCH, MSG_INVALID_TYPE
    var x4 : A[] = new A[] { new A(), new B(), new C()}
    var x5 = new A[] { new A(), new B(), new C()}

    var r1 = new double[3][2]
    var r2 = new double[3][]
    for (i in 0..|3) {
      r2[i] = new double[2]
    }
    var t1 = new double[3][]
    for (i in 0..|3) {
      t1[i] = new double[i + 1]
    }
    var t2 : double[][] = {{0.0}, {0.0, 1.0}, {0.0, 0.0, 0.0}}
    var t3 = new double[][]{{0.0}, {0.0, 0.0}, {0.0, 0.0, 0.0}}
    var i = t2[0][1]

  }
}
