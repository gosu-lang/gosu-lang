package gw.specContrib.classes.method_Scoring

uses gw.specContrib.Type1
uses gw.specContrib.Type2

uses java.lang.Integer
uses java.util.ArrayList
uses java.util.HashMap
uses java.util.HashSet

class Errant_CollectionMethodScoring {

  function fun(arrayList1 : ArrayList<Integer>): Type1 {return null}
  function fun(hashMap1 : HashMap<Integer, String>): Type2 {return null}

  function fun2(list11: HashMap<Integer, String>): Type1 {return null}
  function fun2(list11: HashSet<Integer>): Type2 {return null}

  function foo(list11: List<Integer>): Type1 {return null}
  function foo(list22: String[]): Type2 {return null}

  function caller() {
    var r1: Type1 = fun({1,2,3})
    var r2: Type2 = fun({1,2,3})  //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.TYPE1', REQUIRED: 'GW.SPECCONTRIB.TYPE2'

    var r3: Type1 = fun2({1,2,3})    //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.TYPE2', REQUIRED: 'GW.SPECCONTRIB.TYPE1'
    var r4: Type2 = fun2({1,2,3})

    var r5: Type2 = foo({"1", "2", "3"})
    var r6: Type1 = foo({"1", "2", "3"} as List<Integer>)
    var r7: Type2 = foo({
        1,       //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'INT', REQUIRED: 'JAVA.LANG.STRING'
        "2",
        3         //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'INT', REQUIRED: 'JAVA.LANG.STRING'
    })
  }

}
