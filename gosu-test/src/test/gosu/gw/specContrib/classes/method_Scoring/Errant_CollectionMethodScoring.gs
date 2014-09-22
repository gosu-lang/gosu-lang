package gw.specContrib.classes.method_Scoring

uses java.lang.Integer
uses java.util.ArrayList
uses java.util.HashMap
uses java.util.HashSet
uses java.util.Map

uses gw.specContrib.Type1
uses gw.specContrib.Type2

class Errant_CollectionMethodScoring {

  function fun(arrayList1 : ArrayList<Integer>): Type1 {return null}
  function fun(hashMap1 : HashMap<Integer, String>): Type2 {return null}

  function fun2(list11: HashMap<Integer, String>): Type1 {return null}
  function fun2(list11: HashSet<Integer>): Type2 {return null}

  function foo(list11: List<Integer>): Type1 {return null}
  function foo(list22: String[]): Type2 {return null}

  function caller() {
    var r1: Type1 = fun({1,2,3})
    var r2: Type2 = fun({1,2,3})  //## issuekeys: MSG_

    var r3: Type1 = fun2({1,2,3})    //## issuekeys: MSG_
    var r4: Type2 = fun2({1,2,3})

    var r5: Type2 = foo({"1", "2", "3"})
    var r6: Type1 = foo({"1", "2", "3"} as List<Integer>)
    var r7: Type2 = foo({
        1,       //## issuekeys: MSG_
        "2",
        3         //## issuekeys: MSG_
    })
  }

}
