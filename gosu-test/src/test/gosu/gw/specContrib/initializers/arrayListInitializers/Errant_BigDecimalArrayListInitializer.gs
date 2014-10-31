package gw.specContrib.initializers.arrayListInitializers

uses java.math.BigDecimal
uses java.math.BigInteger
uses java.util.ArrayList
uses java.util.HashMap

class Errant_BigDecimalArrayListInitializer {
  class A{}
  class B{}
  var d1 : DateTime
  var d2 : DateTime
  var o : Object
  var aaa : A
  var bbb : B
  var arrayList : ArrayList
  var hashMap : HashMap

  var bigDecimalArrayList120 : ArrayList<BigDecimal> = {BigDecimal.ONE, 1b, 'c'} //IDE-1284
  var bigDecimalArrayList121 : ArrayList<BigDecimal> = {BigDecimal.ONE, 1b, 1s}
  var bigDecimalArrayList123 : ArrayList<BigDecimal> = {BigDecimal.ONE, 45.5f, 43.5}
  var bigDecimalArrayList124 : ArrayList<BigDecimal> = {BigDecimal.ONE, 100L, 42.5f}
  var bigDecimalArrayList129 : ArrayList<BigDecimal> = {'c', 1b, 1s, 42, 42.5f, 42L, 42.5}
  var bigDecimalArrayList130 : ArrayList<BigDecimal> = {'c', 1b, 1s, 42, 42.5f, 42L, 42.5, BigInteger.ONE, BigDecimal.TEN}


  var bigDecimalArrayList1111 : ArrayList<BigDecimal> = {'c', 'c'}
  var bigDecimalArrayList1110 : ArrayList<BigDecimal> = {'c', 1b}
  var bigDecimalArrayList1112 : ArrayList<BigDecimal> = {'c', 1s}
  var bigDecimalArrayList1113 : ArrayList<BigDecimal> = {'c', 42}
  var bigDecimalArrayList1114 : ArrayList<BigDecimal> = {'c', 42.5f}
  var bigDecimalArrayList1115 : ArrayList<BigDecimal> = {'c', 42.5}
  var bigDecimalArrayList1116 : ArrayList<BigDecimal> = {'c', BigInteger.ONE}
  var bigDecimalArrayList1117 : ArrayList<BigDecimal> = {'c', BigDecimal.TEN}
  var bigDecimalArrayList1118 : ArrayList<BigDecimal> = {'c', d1}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1119 : ArrayList<BigDecimal> = {'c', o}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1120 : ArrayList<BigDecimal> = {'c', aaa}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1121 : ArrayList<BigDecimal> = {'c', "mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1122 : ArrayList<BigDecimal> = {'c', arrayList}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1123 : ArrayList<BigDecimal> = {'c', hashMap}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'

  var bigDecimalArrayList1211 : ArrayList<BigDecimal> = {1b, 'c'}
  var bigDecimalArrayList1210 : ArrayList<BigDecimal> = {1b, 1b}
  var bigDecimalArrayList1212 : ArrayList<BigDecimal> = {1b, 1s}
  var bigDecimalArrayList1213 : ArrayList<BigDecimal> = {1b, 42}
  var bigDecimalArrayList1214 : ArrayList<BigDecimal> = {1b, 42.5f}
  var bigDecimalArrayList1215 : ArrayList<BigDecimal> = {1b, 42.5}
  var bigDecimalArrayList1216 : ArrayList<BigDecimal> = {1b, BigInteger.ONE}
  var bigDecimalArrayList1217 : ArrayList<BigDecimal> = {1b, BigDecimal.TEN}
  var bigDecimalArrayList1218 : ArrayList<BigDecimal> = {1b, d1}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1219 : ArrayList<BigDecimal> = {1b, o}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1220 : ArrayList<BigDecimal> = {1b, aaa}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1221 : ArrayList<BigDecimal> = {1b, "mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1222 : ArrayList<BigDecimal> = {1b, arrayList}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1223 : ArrayList<BigDecimal> = {1b, hashMap}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'

  var bigDecimalArrayList1311 : ArrayList<BigDecimal> = {1s, 'c'}
  var bigDecimalArrayList1310 : ArrayList<BigDecimal> = {1s, 1b}
  var bigDecimalArrayList1312 : ArrayList<BigDecimal> = {1s, 1s}
  var bigDecimalArrayList1313 : ArrayList<BigDecimal> = {1s, 42}
  var bigDecimalArrayList1314 : ArrayList<BigDecimal> = {1s, 42.5f}
  var bigDecimalArrayList1315 : ArrayList<BigDecimal> = {1s, 42.5}
  var bigDecimalArrayList1316 : ArrayList<BigDecimal> = {1s, BigInteger.ONE}
  var bigDecimalArrayList1317 : ArrayList<BigDecimal> = {1s, BigDecimal.TEN}
  var bigDecimalArrayList1318 : ArrayList<BigDecimal> = {1s, d1}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1319 : ArrayList<BigDecimal> = {1s, o}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1320 : ArrayList<BigDecimal> = {1s, aaa}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1321 : ArrayList<BigDecimal> = {1s, "mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1322 : ArrayList<BigDecimal> = {1s, arrayList}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1323 : ArrayList<BigDecimal> = {1s, hashMap}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'


  var bigDecimalArrayList1411 : ArrayList<BigDecimal> = {42, 'c'}
  var bigDecimalArrayList1410 : ArrayList<BigDecimal> = {42, 1b}
  var bigDecimalArrayList1412 : ArrayList<BigDecimal> = {42, 1s}
  var bigDecimalArrayList1413 : ArrayList<BigDecimal> = {42, 42}
  var bigDecimalArrayList1414 : ArrayList<BigDecimal> = {42, 42.5f}
  var bigDecimalArrayList1415 : ArrayList<BigDecimal> = {42, 42.5}
  var bigDecimalArrayList1416 : ArrayList<BigDecimal> = {42, BigInteger.ONE}
  var bigDecimalArrayList1417 : ArrayList<BigDecimal> = {42, BigDecimal.TEN}
  var bigDecimalArrayList1418 : ArrayList<BigDecimal> = {42, d1}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1419 : ArrayList<BigDecimal> = {42, o}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1420 : ArrayList<BigDecimal> = {42, aaa}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1421 : ArrayList<BigDecimal> = {42, "mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1422 : ArrayList<BigDecimal> = {42, arrayList}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1423 : ArrayList<BigDecimal> = {42, hashMap}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'

  var bigDecimalArrayList1511 : ArrayList<BigDecimal> = {42.5f, 'c'}
  var bigDecimalArrayList1510 : ArrayList<BigDecimal> = {42.5f, 1b}
  var bigDecimalArrayList1512 : ArrayList<BigDecimal> = {42.5f, 1s}
  var bigDecimalArrayList1513 : ArrayList<BigDecimal> = {42.5f, 42}
  var bigDecimalArrayList1514 : ArrayList<BigDecimal> = {42.5f, 42.5f}
  var bigDecimalArrayList1515 : ArrayList<BigDecimal> = {42.5f, 42.5}
  var bigDecimalArrayList1516 : ArrayList<BigDecimal> = {42.5f, BigInteger.ONE}
  var bigDecimalArrayList1517 : ArrayList<BigDecimal> = {42.5f, BigDecimal.TEN}
  var bigDecimalArrayList1518 : ArrayList<BigDecimal> = {42.5f, d1}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1519 : ArrayList<BigDecimal> = {42.5f, o}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1520 : ArrayList<BigDecimal> = {42.5f, aaa}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1521 : ArrayList<BigDecimal> = {42.5f, "mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1522 : ArrayList<BigDecimal> = {42.5f, arrayList}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1523 : ArrayList<BigDecimal> = {42.5f, hashMap}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'

  var bigDecimalArrayList1611 : ArrayList<BigDecimal> = {42.5, 'c'}
  var bigDecimalArrayList1610 : ArrayList<BigDecimal> = {42.5, 1b}
  var bigDecimalArrayList1612 : ArrayList<BigDecimal> = {42.5, 1s}
  var bigDecimalArrayList1613 : ArrayList<BigDecimal> = {42.5, 42}
  var bigDecimalArrayList1614 : ArrayList<BigDecimal> = {42.5, 42.5f}
  var bigDecimalArrayList1615 : ArrayList<BigDecimal> = {42.5, 42.5}
  var bigDecimalArrayList1616 : ArrayList<BigDecimal> = {42.5, BigInteger.ONE}
  var bigDecimalArrayList1617 : ArrayList<BigDecimal> = {42.5, BigDecimal.TEN}
  var bigDecimalArrayList1618 : ArrayList<BigDecimal> = {42.5, d1}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1619 : ArrayList<BigDecimal> = {42.5, o}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1620 : ArrayList<BigDecimal> = {42.5, aaa}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1621 : ArrayList<BigDecimal> = {42.5, "mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1622 : ArrayList<BigDecimal> = {42.5, arrayList}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1623 : ArrayList<BigDecimal> = {42.5, hashMap}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'

  var bigDecimalArrayList1711 : ArrayList<BigDecimal> = {BigInteger.ONE, 'c'}
  var bigDecimalArrayList1710 : ArrayList<BigDecimal> = {BigInteger.ONE, 1b}
  var bigDecimalArrayList1712 : ArrayList<BigDecimal> = {BigInteger.ONE, 1s}
  var bigDecimalArrayList1713 : ArrayList<BigDecimal> = {BigInteger.ONE, 42}
  var bigDecimalArrayList1714 : ArrayList<BigDecimal> = {BigInteger.ONE, 42.5f}
  var bigDecimalArrayList1715 : ArrayList<BigDecimal> = {BigInteger.ONE, 42.5}
  var bigDecimalArrayList1716 : ArrayList<BigDecimal> = {BigInteger.ONE, BigInteger.ONE}
  var bigDecimalArrayList1717 : ArrayList<BigDecimal> = {BigInteger.ONE, BigDecimal.TEN}
  var bigDecimalArrayList1718 : ArrayList<BigDecimal> = {BigInteger.ONE, d1}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1719 : ArrayList<BigDecimal> = {BigInteger.ONE, o}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1720 : ArrayList<BigDecimal> = {BigInteger.ONE, aaa}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1721 : ArrayList<BigDecimal> = {BigInteger.ONE, "mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1722 : ArrayList<BigDecimal> = {BigInteger.ONE, arrayList}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1723 : ArrayList<BigDecimal> = {BigInteger.ONE, hashMap}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'

  var bigDecimalArrayList1811 : ArrayList<BigDecimal> = {BigDecimal.TEN, 'c'}
  var bigDecimalArrayList1810 : ArrayList<BigDecimal> = {BigDecimal.TEN, 1b}
  var bigDecimalArrayList1812 : ArrayList<BigDecimal> = {BigDecimal.TEN, 1s}
  var bigDecimalArrayList1813 : ArrayList<BigDecimal> = {BigDecimal.TEN, 42}
  var bigDecimalArrayList1814 : ArrayList<BigDecimal> = {BigDecimal.TEN, 42.5f}
  var bigDecimalArrayList1815 : ArrayList<BigDecimal> = {BigDecimal.TEN, 42.5}
  var bigDecimalArrayList1816 : ArrayList<BigDecimal> = {BigDecimal.TEN, BigInteger.ONE}
  var bigDecimalArrayList1817 : ArrayList<BigDecimal> = {BigDecimal.TEN, BigDecimal.TEN}
  var bigDecimalArrayList1818 : ArrayList<BigDecimal> = {BigDecimal.TEN, d1}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1819 : ArrayList<BigDecimal> = {BigDecimal.TEN, o}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1820 : ArrayList<BigDecimal> = {BigDecimal.TEN, aaa}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1821 : ArrayList<BigDecimal> = {BigDecimal.TEN, "mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1822 : ArrayList<BigDecimal> = {BigDecimal.TEN, arrayList}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1823 : ArrayList<BigDecimal> = {BigDecimal.TEN, hashMap}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'

  var bigDecimalArrayList1911 : ArrayList<BigDecimal> = {d1, 'c'}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1910 : ArrayList<BigDecimal> = {d1, 1b}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1912 : ArrayList<BigDecimal> = {d1, 1s}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1913 : ArrayList<BigDecimal> = {d1, 42}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1914 : ArrayList<BigDecimal> = {d1, 42.5f}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1915 : ArrayList<BigDecimal> = {d1, 42.5}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1916 : ArrayList<BigDecimal> = {d1, BigInteger.ONE}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1917 : ArrayList<BigDecimal> = {d1, BigDecimal.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1918 : ArrayList<BigDecimal> = {d1, d1}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.UTIL.DATE>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1919 : ArrayList<BigDecimal> = {d1, o}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1920 : ArrayList<BigDecimal> = {d1, aaa}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1921 : ArrayList<BigDecimal> = {d1, "mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1922 : ArrayList<BigDecimal> = {d1, arrayList}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.CLONEABLE>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList1923 : ArrayList<BigDecimal> = {d1, hashMap}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.CLONEABLE>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'

  var bigDecimalArrayList2011 : ArrayList<BigDecimal> = {o, 'c'}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2010 : ArrayList<BigDecimal> = {o, 1b}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2012 : ArrayList<BigDecimal> = {o, 1s}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2013 : ArrayList<BigDecimal> = {o, 42}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2014 : ArrayList<BigDecimal> = {o, 42.5f}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2015 : ArrayList<BigDecimal> = {o, 42.5}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2016 : ArrayList<BigDecimal> = {o, BigInteger.ONE}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2017 : ArrayList<BigDecimal> = {o, BigDecimal.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2018 : ArrayList<BigDecimal> = {o, d1}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2019 : ArrayList<BigDecimal> = {o, o}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2020 : ArrayList<BigDecimal> = {o, aaa}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2021 : ArrayList<BigDecimal> = {o, "mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2022 : ArrayList<BigDecimal> = {o, arrayList}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2023 : ArrayList<BigDecimal> = {o, hashMap}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'

  var bigDecimalArrayList2111 : ArrayList<BigDecimal> = {aaa, 'c'}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2110 : ArrayList<BigDecimal> = {aaa, 1b}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2112 : ArrayList<BigDecimal> = {aaa, 1s}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2113 : ArrayList<BigDecimal> = {aaa, 42}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2114 : ArrayList<BigDecimal> = {aaa, 42.5f}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2115 : ArrayList<BigDecimal> = {aaa, 42.5}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2116 : ArrayList<BigDecimal> = {aaa, BigInteger.ONE}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2117 : ArrayList<BigDecimal> = {aaa, BigDecimal.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2118 : ArrayList<BigDecimal> = {aaa, d1}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2119 : ArrayList<BigDecimal> = {aaa, o}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2120 : ArrayList<BigDecimal> = {aaa, aaa}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.INITIALIZERS.ARRAYLISTINITIALIZER.ERRANT_BIGDECIMALARRAYLISTINITIALIZER1.A>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2121 : ArrayList<BigDecimal> = {aaa, "mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2122 : ArrayList<BigDecimal> = {aaa, arrayList}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2123 : ArrayList<BigDecimal> = {aaa, hashMap}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'

  var bigDecimalArrayList2211 : ArrayList<BigDecimal> = {"mystring", 'c'}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2210 : ArrayList<BigDecimal> = {"mystring", 1b}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2212 : ArrayList<BigDecimal> = {"mystring", 1s}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2213 : ArrayList<BigDecimal> = {"mystring", 42}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2214 : ArrayList<BigDecimal> = {"mystring", 42.5f}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2215 : ArrayList<BigDecimal> = {"mystring", 42.5}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2216 : ArrayList<BigDecimal> = {"mystring", BigInteger.ONE}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2217 : ArrayList<BigDecimal> = {"mystring", BigDecimal.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2218 : ArrayList<BigDecimal> = {"mystring", d1}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2219 : ArrayList<BigDecimal> = {"mystring", o}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2220 : ArrayList<BigDecimal> = {"mystring", aaa}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2221 : ArrayList<BigDecimal> = {"mystring", "mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.STRING>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2222 : ArrayList<BigDecimal> = {"mystring", arrayList}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2223 : ArrayList<BigDecimal> = {"mystring", hashMap}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'


  var bigDecimalArrayList2311 : ArrayList<BigDecimal> = {arrayList, 'c'}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2310 : ArrayList<BigDecimal> = {arrayList, 1b}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2312 : ArrayList<BigDecimal> = {arrayList, 1s}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2313 : ArrayList<BigDecimal> = {arrayList, 42}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2314 : ArrayList<BigDecimal> = {arrayList, 42.5f}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2315 : ArrayList<BigDecimal> = {arrayList, 42.5}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2316 : ArrayList<BigDecimal> = {arrayList, BigInteger.ONE}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2317 : ArrayList<BigDecimal> = {arrayList, BigDecimal.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2318 : ArrayList<BigDecimal> = {arrayList, d1}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.CLONEABLE>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2319 : ArrayList<BigDecimal> = {arrayList, o}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2320 : ArrayList<BigDecimal> = {arrayList, aaa}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2321 : ArrayList<BigDecimal> = {arrayList, "mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2322 : ArrayList<BigDecimal> = {arrayList, arrayList}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2323 : ArrayList<BigDecimal> = {arrayList, hashMap}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.CLONEABLE>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'

  var bigDecimalArrayList2411 : ArrayList<BigDecimal> = {hashMap, 'c'}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2410 : ArrayList<BigDecimal> = {hashMap, 1b}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2412 : ArrayList<BigDecimal> = {hashMap, 1s}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2413 : ArrayList<BigDecimal> = {hashMap, 42}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2414 : ArrayList<BigDecimal> = {hashMap, 42.5f}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2415 : ArrayList<BigDecimal> = {hashMap, 42.5}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2416 : ArrayList<BigDecimal> = {hashMap, BigInteger.ONE}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2417 : ArrayList<BigDecimal> = {hashMap, BigDecimal.TEN}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2418 : ArrayList<BigDecimal> = {hashMap, d1}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.CLONEABLE>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2419 : ArrayList<BigDecimal> = {hashMap, o}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2420 : ArrayList<BigDecimal> = {hashMap, aaa}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2421 : ArrayList<BigDecimal> = {hashMap, "mystring"}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2422 : ArrayList<BigDecimal> = {hashMap, arrayList}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.CLONEABLE>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'
  var bigDecimalArrayList2423 : ArrayList<BigDecimal> = {hashMap, hashMap}      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.UTIL.HASHMAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>>', REQUIRED: 'JAVA.UTIL.ARRAYLIST<JAVA.MATH.BIGDECIMAL>'

}