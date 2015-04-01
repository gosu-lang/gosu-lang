package gw.specContrib.initializers.hashMapInitializers

uses java.math.BigDecimal
uses java.math.BigInteger
uses java.util.ArrayList
uses java.util.HashMap

class Errant_ObjectHashMapInitializer {
  class A{}
  class B{}
  var d1 : DateTime
  var d2 : DateTime
  var o : Object
  var aaa : A
  var bbb : B
  var arrayList : ArrayList
  var hashMap : HashMap

  var objectHashMap1111 : HashMap<Object, Object> = {new Object()->'c'}
  var objectHashMap1110 : HashMap<Object, Object> = {new Object()->1b}
  var objectHashMap1112 : HashMap<Object, Object> = {new Object()->1s}
  var objectHashMap1113 : HashMap<Object, Object> = {new Object()->42}
  var objectHashMap1114 : HashMap<Object, Object> = {new Object()->42.5f}
  var objectHashMap1115 : HashMap<Object, Object> = {new Object()->42.5}
  var objectHashMap1116 : HashMap<Object, Object> = {new Object()->BigInteger.ONE}
  var objectHashMap1117 : HashMap<Object, Object> = {new Object()->BigDecimal.TEN}
  var objectHashMap1118 : HashMap<Object, Object> = {new Object()->d1}
  var objectHashMap1119 : HashMap<Object, Object> = {new Object()->o}
  var objectHashMap1120 : HashMap<Object, Object> = {new Object()->aaa}
  var objectHashMap1121 : HashMap<Object, Object> = {new Object()->"mystring"}
  var objectHashMap1122 : HashMap<Object, Object> = {new Object()->arrayList}
  var objectHashMap1123 : HashMap<Object, Object> = {new Object()->hashMap}

  var objectHashMap1211 : HashMap<Object, Object> = {'c'-> new Object()}
  var objectHashMap1210 : HashMap<Object, Object> = {1b-> new Object()}
  var objectHashMap1212 : HashMap<Object, Object> = {1s-> new Object()}
  var objectHashMap1213 : HashMap<Object, Object> = {42-> new Object()}
  var objectHashMap1214 : HashMap<Object, Object> = {42.5f-> new Object()}
  var objectHashMap1215 : HashMap<Object, Object> = {42.5-> new Object()}
  var objectHashMap1216 : HashMap<Object, Object> = {BigInteger.ONE-> new Object()}
  var objectHashMap1217 : HashMap<Object, Object> = {BigDecimal.TEN-> new Object()}
  var objectHashMap1218 : HashMap<Object, Object> = {d1-> new Object()}
  var objectHashMap1219 : HashMap<Object, Object> = {o-> new Object()}
  var objectHashMap1220 : HashMap<Object, Object> = {aaa-> new Object()}
  var objectHashMap1221 : HashMap<Object, Object> = {"mystring"-> new Object()}
  var objectHashMap1222 : HashMap<Object, Object> = {arrayList-> new Object()}
  var objectHashMap1223 : HashMap<Object, Object> = {hashMap-> new Object()}

  var objectHashMap1311 : HashMap<Object, Object> = {new Object()-> new Object(), new Object()->'c'}
  var objectHashMap1310 : HashMap<Object, Object> = {new Object()-> new Object(), new Object()->1b}
  var objectHashMap1312 : HashMap<Object, Object> = {new Object()-> new Object(), new Object()->1s}
  var objectHashMap1313 : HashMap<Object, Object> = {new Object()-> new Object(), new Object()->42}
  var objectHashMap1314 : HashMap<Object, Object> = {new Object()-> new Object(), new Object()->42.5f}
  var objectHashMap1315 : HashMap<Object, Object> = {new Object()-> new Object(), new Object()->42.5}
  var objectHashMap1316 : HashMap<Object, Object> = {new Object()-> new Object(), new Object()->BigInteger.ONE}
  var objectHashMap1317 : HashMap<Object, Object> = {new Object()-> new Object(), new Object()->BigDecimal.TEN}
  var objectHashMap1318 : HashMap<Object, Object> = {new Object()-> new Object(), new Object()->d1}
  var objectHashMap1319 : HashMap<Object, Object> = {new Object()-> new Object(), new Object()->o}
  var objectHashMap1320 : HashMap<Object, Object> = {new Object()-> new Object(), new Object()->aaa}
  var objectHashMap1321 : HashMap<Object, Object> = {new Object()-> new Object(), new Object()->"mystring"}
  var objectHashMap1322 : HashMap<Object, Object> = {new Object()-> new Object(), new Object()->arrayList}
  var objectHashMap1323 : HashMap<Object, Object> = {new Object()-> new Object(), new Object()->hashMap}

  var objectHashMap1411 : HashMap<Object, Object> = {new Object()-> new Object(), 'c'->new Object()}
  var objectHashMap1410 : HashMap<Object, Object> = {new Object()-> new Object(), 1b->new Object()}
  var objectHashMap1412 : HashMap<Object, Object> = {new Object()-> new Object(), 1s->new Object()}
  var objectHashMap1413 : HashMap<Object, Object> = {new Object()-> new Object(), 42->new Object()}
  var objectHashMap1414 : HashMap<Object, Object> = {new Object()-> new Object(), 42.5f->new Object()}
  var objectHashMap1415 : HashMap<Object, Object> = {new Object()-> new Object(), 42.5->new Object()}
  var objectHashMap1416 : HashMap<Object, Object> = {new Object()-> new Object(), BigInteger.ONE->new Object()}
  var objectHashMap1417 : HashMap<Object, Object> = {new Object()-> new Object(), BigDecimal.TEN->new Object()}
  var objectHashMap1418 : HashMap<Object, Object> = {new Object()-> new Object(), d1->new Object()}
  var objectHashMap1419 : HashMap<Object, Object> = {new Object()-> new Object(), o->new Object()}
  var objectHashMap1420 : HashMap<Object, Object> = {new Object()-> new Object(), aaa->new Object()}
  var objectHashMap1421 : HashMap<Object, Object> = {new Object()-> new Object(), "mystring"->new Object()}
  var objectHashMap1422 : HashMap<Object, Object> = {new Object()-> new Object(), arrayList->new Object()}
  var objectHashMap1423 : HashMap<Object, Object> = {new Object()-> new Object(), hashMap->new Object()}
}