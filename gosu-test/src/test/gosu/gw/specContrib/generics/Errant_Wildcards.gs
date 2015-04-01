package gw.specContrib.generics

uses java.lang.Integer

class Errant_Wildcards<T extends List<T>> {

 function fun() {
    var list : List<?>                 //## issuekeys: WILDCARDS ARE NO LONGER SUPPORTED IN GOSU
    var list1 : List<? extends T>      //## issuekeys: WILDCARDS ARE NO LONGER SUPPORTED IN GOSU
    var list2 : List<? extends Integer>    //## issuekeys: WILDCARDS ARE NO LONGER SUPPORTED IN GOSU
    var list3 : List<? super Integer>      //## issuekeys: WILDCARDS ARE NO LONGER SUPPORTED IN GOSU
  }

}
