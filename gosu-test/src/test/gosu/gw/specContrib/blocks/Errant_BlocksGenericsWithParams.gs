package gw.specContrib.blocks

uses java.lang.Integer
uses java.util.ArrayList

class Errant_BlocksGenericsWithParams {
  function hello1<T>(p: block(t: T): T): T {
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
    //x11219 : OS Gosu issue - IDE-1346. Parser is good : No error
    var x11219 = hello1(\x: ArrayList<Integer> -> {
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
    //x12230 : OS Gosu issue - IDE-1346. Parser is good : No error
    var x12230: ArrayList = hello1(\x: ArrayList<Integer> -> {
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
    //x13235 : Parser and OS Gosu show error because of different issues
    // Parser should not show error IDE-1344. And OS Gosu should not show error IDE-1346
    var x13235: ArrayList<Integer> = hello1(\x: ArrayList -> {
      return {1, 2, 'c'}
    })
    var x13236: ArrayList<Integer> = hello1(\x: ArrayList<Integer> -> {
      return {1, 2, 3}
    })
    //IDE-1346 Next 3 cases. OS Gosu Bug only. Parser is good
    var x13237: ArrayList<Integer> = hello1(\x: ArrayList<Integer> -> {
      return {1, 2, 'c'}
    })
    var x13238: ArrayList<Integer> = hello1(\x: ArrayList<Integer> -> {
      return {1, 2, 3b}
    })
    var x13239: ArrayList<Integer> = hello1(\x: ArrayList<Integer> -> {
      return {1, 2, 3s}
    })

    var x13240: ArrayList<Integer> = hello1(\x: ArrayList<Integer> -> {      //## issuekeys: 'HELLO1(GW.LANG.__PSI__.IBLOCK1<JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>,JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>>)' IN 'TEST.GOSU.GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.BLOCKS.BLOCKSMAIN.ERRANT_BLOCKSGENERICSWITHPARAMS' CANNOT BE APPLIED TO '(BLOCK(ARRAYLIST<INTEGER>):ARRAYLIST<SERIALIZABLE & COMPARABLE<SERIALIZABLE & COMPARABLE<? EXTENDS COMPARABLE<?>>>>)'
      return {1, 2, 'c', "string"}
    })

    //IDE-1344 - Parser bug
    var x13241: ArrayList<Integer> = hello1(\x: ArrayList -> {
      return new ArrayList<Integer>()
    })
    //Error Expected. Both show
    var x13242: ArrayList<Integer> = hello1(\x: ArrayList<Integer> -> {      //## issuekeys: 'HELLO1(GW.LANG.__PSI__.IBLOCK1<JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>,JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>>)' IN 'TEST.GOSU.GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.BLOCKS.BLOCKSMAIN.ERRANT_BLOCKSGENERICSWITHPARAMS' CANNOT BE APPLIED TO '(BLOCK(ARRAYLIST<INTEGER>):ARRAYLIST)'
      return new ArrayList()
    })

  }

}