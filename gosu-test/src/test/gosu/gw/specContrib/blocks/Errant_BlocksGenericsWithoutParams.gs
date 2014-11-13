package gw.specContrib.blocks

uses java.lang.Integer
uses java.util.ArrayList

class Errant_BlocksGenericsWithoutParams {
  function hello1<T>(p: block(): T): T {
    return p()
  }

  function caller() {

    var x1111 = hello1(\-> new ArrayList())
    var x1112 = hello1(\-> {
      return new ArrayList()
    })
    var x1113 = hello1(\-> new ArrayList<Integer>())
    var x1114 = hello1(\-> {
      return {1, 2, 3}
    })
    var x1115 = hello1(\-> {
      return {42, 'c'}
    })
    var x1116 = hello1(\-> {
      return {42, 'c', 42.5f}
    })
    var x1117 = hello1(\-> {
      return {42, 'c', 42.5f, "string"}
    })

    var x1211: ArrayList = hello1(\-> new ArrayList())
    var x1212: ArrayList = hello1(\-> {
      return new ArrayList()
    })
    var x1213: ArrayList = hello1(\-> {
      return new ArrayList<Integer>()
    })
    //IDE-1338 Should not show error - Next 6 cases
    var x1214: ArrayList = hello1(\-> {
      return {1, 2, 3}
    })
    var x1215: ArrayList = hello1(\-> {
      return {42, 'c'}
    })
    var x1216: ArrayList = hello1(\-> {
      return {42, 1b}
    })
    var x1217: ArrayList = hello1(\-> {
      return {42, 1s}
    })
    var x1218: ArrayList = hello1(\-> {
      return {42, 'c', 42.5f}
    })
    var x1219: ArrayList = hello1(\-> {
      return {42, 'c', 42.5f, "string"}
    })


    var x1311: ArrayList<Integer> = hello1(\-> new ArrayList())  //Error Expected      //## issuekeys: 'HELLO1(GW.LANG.__PSI__.IBLOCK0<T>)' IN 'TEST.GOSU.GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.BLOCKS.BLOCKSMAIN.ERRANT_BLOCKSGENERICSWITHOUTPARAMS' CANNOT BE APPLIED TO '(BLOCK():ARRAYLIST)'
    var x1312: ArrayList<Integer> = hello1(\-> new ArrayList<Integer>())
    var x1313: ArrayList<Integer> = hello1(\-> {
      return new ArrayList<Integer>()
    })
    //IDE-1338 Should not show error - Next 4 cases
    //Also x1315, x1316, x1317 are affected by IDE-1346 in OS Gosu
    var x1314: ArrayList<Integer> = hello1(\-> {
      return {1, 2, 3}
    })
    var x1315: ArrayList<Integer> = hello1(\-> {
      return {42, 'c'}
    })
    var x1316: ArrayList<Integer> = hello1(\-> {
      return {42, 1b}
    })
    var x1317: ArrayList<Integer> = hello1(\-> {
      return {42, 1s}
    })
    //Should show error but NOT because of IDE-1338
    var x1318: ArrayList<Integer> = hello1(\-> {      //## issuekeys: 'HELLO1(GW.LANG.__PSI__.IBLOCK0<T>)' IN 'TEST.GOSU.GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.BLOCKS.BLOCKSMAIN.ERRANT_BLOCKSGENERICSWITHOUTPARAMS' CANNOT BE APPLIED TO '(BLOCK():ARRAYLIST<FLOAT>)'
      return {42, 'c', 42.5f}
    })
    var x1319: ArrayList<Integer> = hello1(\-> {      //## issuekeys: 'HELLO1(GW.LANG.__PSI__.IBLOCK0<T>)' IN 'TEST.GOSU.GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.BLOCKS.BLOCKSMAIN.ERRANT_BLOCKSGENERICSWITHOUTPARAMS' CANNOT BE APPLIED TO '(BLOCK():ARRAYLIST<SERIALIZABLE & COMPARABLE<SERIALIZABLE & COMPARABLE<? EXTENDS COMPARABLE<?>>>>)'
      return {42, 'c', 42.5f, "string"}
    })

    var x1320: ArrayList<Integer> = hello1(\-> new ArrayList<String>())      //## issuekeys: 'HELLO1(GW.LANG.__PSI__.IBLOCK0<T>)' IN 'TEST.GOSU.GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.BLOCKS.BLOCKSMAIN.ERRANT_BLOCKSGENERICSWITHOUTPARAMS' CANNOT BE APPLIED TO '(BLOCK():ARRAYLIST<STRING>)'

  }

}