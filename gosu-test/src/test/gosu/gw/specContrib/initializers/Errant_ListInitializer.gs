package gw.specContrib.initializers

class Errant_ListInitializer {
  //IDE-3447
  function foo() {
    var list1 = new List()      //## issuekeys: 'LIST' IS ABSTRACT; CANNOT BE INSTANTIATED
    var list2 = new List() {1,2,3}      //## issuekeys: 'LIST' IS ABSTRACT; CANNOT BE INSTANTIATED
    var list3 = new List<Integer>()      //## issuekeys: 'LIST' IS ABSTRACT; CANNOT BE INSTANTIATED
    var list4 = new List<Integer>() {1,2,3}      //## issuekeys: 'LIST' IS ABSTRACT; CANNOT BE INSTANTIATED
  }
}