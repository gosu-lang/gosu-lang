package gw.specContrib.expressions

class Errant_RemoveAliasTypes {

  function foo() {
//    IDE-2356
    var bean1: Bean      //## issuekeys: CANNOT RESOLVE SYMBOL 'BEAN'
    var date1: DateTime      //## issuekeys: CANNOT RESOLVE SYMBOL 'DATETIME'
    var list1 = new List(){1, 2, 3}      //## issuekeys: 'LIST' IS ABSTRACT; CANNOT BE INSTANTIATED

    var l1 = new java.util.List(){1, 2, 3}      //## issuekeys: 'LIST' IS ABSTRACT; CANNOT BE INSTANTIATED
    var l2 = new List(){1, 2, 3}                              //## issuekeys: 'LIST' IS ABSTRACT; CANNOT BE INSTANTIATED
    var l3 = new java.util.ArrayList(){1, 2, 3}
  }
}