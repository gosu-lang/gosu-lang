package gw.specContrib.blocks

uses java.lang.Integer
uses java.lang.ref.WeakReference
uses java.util.ArrayList
uses java.util.Map

class Errant_BlocksGenericsWithParams {
  function hello1<T>(p(t: T): T): T {
    return p(null)
  }

  function caller() {

    //Set1 : Variable type not specified on LHS
    var x1210 = hello1(\x -> new ArrayList())

    //IDE-1336 - Not A Bug. The type of the block argument is actually a variable name here. No error expected
    var x11211 = hello1(\ArrayList -> new ArrayList())

    var x11212 = hello1(\x: ArrayList -> new ArrayList())
    var x11213 = hello1(\x: ArrayList -> {
      return new ArrayList()
    })
    var x11214 = hello1(\x: ArrayList<Integer> -> new ArrayList<Integer>())
    var x11215 = hello1(\x: ArrayList<Integer> -> {
      return new ArrayList<Integer>()
    })
    var x11216 = hello1(\x: ArrayList -> {
      return {1, 2, 'c'}
    })
    var x11217 = hello1(\x: ArrayList -> {
      return {1, 2, 42.5}
    })
    var x11218 = hello1(\x: ArrayList<Integer> -> {
      return {1, 2, 3}
    })
    //x11219 : IDE-1346 OS Gosu could be improved to not show error, but the improvement is postponed indefinitely, so keeping the errors
    var x11219 = hello1(\x: ArrayList<Integer> -> {      //## issuekeys: INCOMPATIBLE TYPES
      return {1, 2, 'c'}
    })
    var x11220 = hello1(\x: ArrayList<Integer> -> {      //## issuekeys: 'HELLO1(GW.LANG.__PSI__.IBLOCK1<JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>,JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>>)' IN 'TEST.GOSU.GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.BLOCKS.BLOCKSMAIN.ERRANT_BLOCKSGENERICSWITHPARAMS' CANNOT BE APPLIED TO '(BLOCK(ARRAYLIST<INTEGER>):ARRAYLIST<SERIALIZABLE & COMPARABLE<SERIALIZABLE & COMPARABLE<? EXTENDS COMPARABLE<?>>>>)'
      return {1, 2, 'c', "string"}
    })
    var x11221 = hello1(\x: ArrayList -> {
      return new ArrayList<Integer>()
    })
    //Error Expected. Both Parser and OS Gosu show
    var x11222 = hello1(\x: ArrayList<Integer> -> {      //## issuekeys: 'HELLO1(GW.LANG.__PSI__.IBLOCK1<JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>,JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>>)' IN 'TEST.GOSU.GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.BLOCKS.BLOCKSMAIN.ERRANT_BLOCKSGENERICSWITHPARAMS' CANNOT BE APPLIED TO '(BLOCK(ARRAYLIST<INTEGER>):ARRAYLIST)'
      return new ArrayList()
    })


    //Set2 : Variable type 'ArrayList' specified on LHS
    var x12220 = hello1(\x -> new ArrayList())
    //IDE-1336 - Not A Bug. The type of the block argument is actually a variable name here. No error expected
    var x12221: ArrayList = hello1(\ArrayList -> new ArrayList())
    var x12223: ArrayList = hello1(\x: ArrayList -> new ArrayList())
    var x12224: ArrayList = hello1(\x: ArrayList -> {
      return new ArrayList()
    })
    var x12225: ArrayList = hello1(\x: ArrayList<Integer> -> new ArrayList<Integer>())
    var x12226: ArrayList = hello1(\x: ArrayList<Integer> -> {
      return new ArrayList<Integer>()
    })
    var x12227: ArrayList = hello1(\x: ArrayList -> {
      return {1, 2, 'c'}
    })
    var x12228: ArrayList = hello1(\x: ArrayList<Integer> -> {
      return {1, 2, 3}
    })
    var x12229: ArrayList = hello1(\x: ArrayList<Integer> -> {
      return {1, 2, 3}
    })
    //x12230 : IDE-1346 OS Gosu could be improved to not show error, but the improvement is postponed indefinitely, so keeping the errors
    var x12230: ArrayList = hello1(\x: ArrayList<Integer> -> {      //## issuekeys: INCOMPATIBLE TYPES
      return {1, 2, 'c'}
    })
    var x12231: ArrayList = hello1(\x: ArrayList<Integer> -> {      //## issuekeys: 'HELLO1(GW.LANG.__PSI__.IBLOCK1<JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>,JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>>)' IN 'TEST.GOSU.GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.BLOCKS.BLOCKSMAIN.ERRANT_BLOCKSGENERICSWITHPARAMS' CANNOT BE APPLIED TO '(BLOCK(ARRAYLIST<INTEGER>):ARRAYLIST<SERIALIZABLE & COMPARABLE<SERIALIZABLE & COMPARABLE<? EXTENDS COMPARABLE<?>>>>)'
      return {1, 2, 'c', "string"}
    })
    var x12232: ArrayList = hello1(\x: ArrayList -> {
      return new ArrayList<Integer>()
    })
    //Error Expected. Both show
    var x12233: ArrayList = hello1(\x: ArrayList<Integer> -> {      //## issuekeys: 'HELLO1(GW.LANG.__PSI__.IBLOCK1<JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>,JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>>)' IN 'TEST.GOSU.GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.BLOCKS.BLOCKSMAIN.ERRANT_BLOCKSGENERICSWITHPARAMS' CANNOT BE APPLIED TO '(BLOCK(ARRAYLIST<INTEGER>):ARRAYLIST)'
      return new ArrayList()
    })


    //Set3 : Variable type 'ArrayList<Integer>' specified on LHS
    var x13230 = hello1(\x -> new ArrayList())
    //IDE-1336 - Not A Bug. The type of the block argument is actually a variable name here. No error expected
    var x13231: ArrayList<Integer> = hello1(\ArrayList -> new ArrayList<Integer>())
    var x13233: ArrayList<Integer> = hello1(\x: ArrayList<Integer> -> new ArrayList<Integer>())
    var x13234: ArrayList<Integer> = hello1(\x: ArrayList<Integer> -> {
      return new ArrayList<Integer>()
    })
    var x13235: ArrayList<Integer> = hello1(\x: ArrayList -> {
      return {1, 2, 'c'}
    })
    var x13236: ArrayList<Integer> = hello1(\x: ArrayList<Integer> -> {
      return {1, 2, 3}
    })
    //x13237, x13238, x13239 : IDE-1346 OS Gosu could be improved to not show error, but the improvement is postponed indefinitely, so keeping the errors
    var x13237: ArrayList<Integer> = hello1(\x: ArrayList<Integer> -> {
      return {1, 2, 'c'}
    })
    var x13238: ArrayList<Integer> = hello1(\x: ArrayList<Integer> -> {
      return {1, 2, 3b}
    })
    var x13239: ArrayList<Integer> = hello1(\x: ArrayList<Integer> -> {
      return {1, 2, 3 as short}
    })

    var x13240: ArrayList<Integer> = hello1(\x: ArrayList<Integer> -> { return {1, 2, 'c', "string"} })  //## issuekeys: MSG_TYPE_MISMATCH

    //IDE-1344 - Parser bug
    var x13241: ArrayList<Integer> = hello1(\x: ArrayList -> {
      return new ArrayList<Integer>()
    })
    // Gosu compiler no longer gives an error on this line:
    var x13242: ArrayList<Integer> = hello1(\x: ArrayList<Integer> -> { return new ArrayList() }) 
    //IDE-1344 - Gosu gives no error on this line either
    var x: Integer = hello1(\a: java.lang.Number -> 0)

  }

  abstract class A {
    abstract property get Prop1(): String
    abstract property get Prop2(): String
    abstract property get Prop3(): String
  }

  function test(l: List<A>, ints: Integer[][]) {
    //IDE-2297
    new WeakReference<Map<String, Map<String, List<String>>>>(
        l.partition(\t -> t.Prop1)
         .mapValues(\tl1 -> tl1
           .partition(\t -> t.Prop2)
           .mapValues(\tl2 -> tl2.map(\t -> t.Prop3))))

    //IDE-2299
    ints.map( \ array -> array.map( \ elem -> elem + 1 ))
  }

}
