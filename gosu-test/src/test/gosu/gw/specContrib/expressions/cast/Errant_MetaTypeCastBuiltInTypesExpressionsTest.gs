package gw.specContrib.expressions.cast

uses java.util.*
uses java.lang.*
uses java.math.BigDecimal
uses java.lang.Class

class Errant_MetaTypeCastBuiltInTypesExpressionsTest <T extends java.lang.Number> {

  interface IA {}
  structure SA {}
  class AImpl implements IA {}

  //Lists
  var list111 = List as List  //## issuekeys: MSG_TYPE_MISMATCH
  var list112 = List as List<Integer>   //## issuekeys: MSG_TYPE_MISMATCH
  var list113 = List<Integer> as List //## issuekeys: MSG_TYPE_MISMATCH
  var list114 = List<Integer> as List<Integer>  //## issuekeys: MSG_TYPE_MISMATCH
  var list115 = List<Integer> as List<String> //## issuekeys: MSG_TYPE_MISMATCH
  var list116 = List<Integer> as List<Double> //## issuekeys: MSG_TYPE_MISMATCH
  var list117 = List<Integer> as List<java.lang.Number> //## issuekeys: MSG_TYPE_MISMATCH
  var list118 = List<Byte> as List<Integer> //## issuekeys: MSG_TYPE_MISMATCH
  var list119 = List<Integer> as List<Byte> //## issuekeys: MSG_TYPE_MISMATCH

  var list121 = List as ArrayList //## issuekeys: MSG_TYPE_MISMATCH
  var list122 = List as ArrayList<Integer>  //## issuekeys: MSG_TYPE_MISMATCH
  var list123 = List<Integer> as ArrayList  //## issuekeys: MSG_TYPE_MISMATCH
  var list124 = List<Integer> as ArrayList<Integer> //## issuekeys: MSG_TYPE_MISMATCH
  var list125 = List<Integer> as ArrayList<java.lang.Number>  //## issuekeys: MSG_TYPE_MISMATCH
  var list126 = List<Byte> as ArrayList<Integer>  //## issuekeys: MSG_TYPE_MISMATCH

  var list127 = List<Byte> as IA  //## issuekeys: MSG_TYPE_MISMATCH
  var list128 = List<Byte> as AImpl  //## issuekeys: MSG_TYPE_MISMATCH

  //ArrayLists
  var arrayList111 = ArrayList as ArrayList //## issuekeys: MSG_TYPE_MISMATCH
  var arrayList112 = ArrayList as ArrayList<String> //## issuekeys: MSG_TYPE_MISMATCH
  var arrayList113 = ArrayList<Integer> as ArrayList  //## issuekeys: MSG_TYPE_MISMATCH
  var arrayList114 = ArrayList<Integer> as ArrayList<Integer> //## issuekeys: MSG_TYPE_MISMATCH
  var arrayList115 = ArrayList<Integer> as ArrayList<String>  //## issuekeys: MSG_TYPE_MISMATCH
  var arrayList116 = ArrayList<Integer> as ArrayList<Double>  //## issuekeys: MSG_TYPE_MISMATCH
  var arrayList117 = ArrayList<Integer> as ArrayList<java.lang.Number>  //## issuekeys: MSG_TYPE_MISMATCH
  var arrayList118 = ArrayList<Byte> as ArrayList<Integer>  //## issuekeys: MSG_TYPE_MISMATCH
  var arrayList119 = ArrayList<Integer> as ArrayList<Byte>  //## issuekeys: MSG_TYPE_MISMATCH

  var arrayList121 = ArrayList as List  //## issuekeys: MSG_TYPE_MISMATCH
  var arrayList122 = ArrayList as List<Integer> //## issuekeys: MSG_TYPE_MISMATCH
  var arrayList123 = ArrayList<Integer> as List //## issuekeys: MSG_TYPE_MISMATCH
  var arrayList124 = ArrayList<Integer> as List<Integer>  //## issuekeys: MSG_TYPE_MISMATCH
  var arrayList125 = ArrayList<Integer> as List<java.lang.Number> //## issuekeys: MSG_TYPE_MISMATCH
  var arrayList126 = ArrayList<Byte> as List<Integer> //## issuekeys: MSG_TYPE_MISMATCH


  //Maps
  var map111 = Map as Map   //## issuekeys: MSG_TYPE_MISMATCH
  var map112 = Map as Map<Integer, Integer> //## issuekeys: MSG_TYPE_MISMATCH
  var map113 = Map<Integer, Integer> as Map //## issuekeys: MSG_TYPE_MISMATCH
  var map114 = Map<Integer, Integer> as Map<Integer, Integer> //## issuekeys: MSG_TYPE_MISMATCH
  var map115 = Map<Integer, Integer> as Map<String, Integer>  //## issuekeys: MSG_TYPE_MISMATCH
  var map116 = Map<Integer, Integer> as Map<Double, Double> //## issuekeys: MSG_TYPE_MISMATCH
  var map117 = Map<Integer, Integer> as Map<java.lang.Number, java.lang.Number> //## issuekeys: MSG_TYPE_MISMATCH
  var map118 = Map<Byte, Byte> as Map<Integer, Integer> //## issuekeys: MSG_TYPE_MISMATCH
  var map119 = Map<Integer, Integer> as Map<Byte, Byte> //## issuekeys: MSG_TYPE_MISMATCH
  var map120 = Map<Integer, Byte> as Map<Byte, Integer> //## issuekeys: MSG_TYPE_MISMATCH

  var map121 = Map as HashMap //## issuekeys: MSG_TYPE_MISMATCH
  var map122 = Map as HashMap<Integer, Integer> //## issuekeys: MSG_TYPE_MISMATCH
  var map123 = Map<Integer, Integer> as HashMap //## issuekeys: MSG_TYPE_MISMATCH
  var map124 = Map<Integer, Integer> as HashMap<Integer, Integer> //## issuekeys: MSG_TYPE_MISMATCH
  var map125 = Map<Integer, Integer> as HashMap<java.lang.Number, java.lang.Number> //## issuekeys: MSG_TYPE_MISMATCH
  var map126 = Map<Byte, Byte> as HashMap<Integer, Integer> //## issuekeys: MSG_TYPE_MISMATCH

  var map127 = Map<Byte, Byte> as IA //## issuekeys: MSG_TYPE_MISMATCH
  var map128 = Map<Byte, Byte> as AImpl //## issuekeys: MSG_TYPE_MISMATCH

  //HashMaps
  var hashMap111 = HashMap as HashMap   //## issuekeys: MSG_TYPE_MISMATCH
  var hashMap112 = HashMap as HashMap<Integer, Integer> //## issuekeys: MSG_TYPE_MISMATCH
  var hashMap113 = HashMap<Integer, Integer> as HashMap //## issuekeys: MSG_TYPE_MISMATCH
  var hashMap114 = HashMap<Integer, Integer> as HashMap<Integer, Integer> //## issuekeys: MSG_TYPE_MISMATCH
  var hashMap115 = HashMap<Integer, Integer> as HashMap<String, Integer>  //## issuekeys: MSG_TYPE_MISMATCH
  var hashMap116 = HashMap<Integer, Integer> as HashMap<Double, Double> //## issuekeys: MSG_TYPE_MISMATCH
  var hashMap117 = HashMap<Integer, Integer> as HashMap<java.lang.Number, java.lang.Number> //## issuekeys: MSG_TYPE_MISMATCH
  var hashMap118 = HashMap<Byte, Byte> as HashMap<Integer, Integer> //## issuekeys: MSG_TYPE_MISMATCH
  var hashMap119 = HashMap<Integer, Integer> as HashMap<Byte, Byte> //## issuekeys: MSG_TYPE_MISMATCH
  var hashMap120 = HashMap<Integer, Byte> as HashMap<Byte, Integer> //## issuekeys: MSG_TYPE_MISMATCH

  var hashMap121 = HashMap as Map   //## issuekeys: MSG_TYPE_MISMATCH
  var hashMap122 = HashMap as Map<Integer, Integer> //## issuekeys: MSG_TYPE_MISMATCH
  var hashMap123 = HashMap<Integer, Integer> as Map //## issuekeys: MSG_TYPE_MISMATCH
  var hashMap124 = HashMap<Integer, Integer> as Map<Integer, Integer> //## issuekeys: MSG_TYPE_MISMATCH
  var hashMap125 = HashMap<Integer, Integer> as Map<java.lang.Number, java.lang.Number> //## issuekeys: MSG_TYPE_MISMATCH
  var hashMap126 = HashMap<Byte, Byte> as Map<Integer, Integer> //## issuekeys: MSG_TYPE_MISMATCH

  //List Map Combination
  var listMap111 = List as Map  //## issuekeys: MSG_TYPE_MISMATCH
  var listMap112 = List as HashMap  //## issuekeys: MSG_TYPE_MISMATCH
  var listMap113 = ArrayList as Map //## issuekeys: MSG_TYPE_MISMATCH
  var listMap114 = ArrayList as HashMap //## issuekeys: MSG_TYPE_MISMATCH
  var listMap115 = List<Integer> as Map<Integer, Integer> //## issuekeys: MSG_TYPE_MISMATCH
  var listMap116 = ArrayList<Integer> as HashMap<Integer, Integer>  //## issuekeys: MSG_TYPE_MISMATCH


  //Primitive types with Collections
  var primitiveAndCollections111 = int as List  //## issuekeys: MSG_TYPE_MISMATCH
  var primitiveAndCollections112 = int as List<Integer> //## issuekeys: MSG_TYPE_MISMATCH
  var primitiveAndCollections113 = int as ArrayList //## issuekeys: MSG_TYPE_MISMATCH
  var primitiveAndCollections114 = int as ArrayList<Integer>  //## issuekeys: MSG_TYPE_MISMATCH
  var primitiveAndCollections115 = ArrayList<Integer> as int  //## issuekeys: MSG_TYPE_MISMATCH
  var primitiveAndCollections116 = ArrayList<Integer> as String
  var primitiveAndCollections117 = ArrayList<Integer> as Integer  //## issuekeys: MSG_TYPE_MISMATCH

  //Arrays
  var arrays111 = String[] as String[]  //## issuekeys: MSG_TYPE_MISMATCH
  var arrays112 = String[] as Integer[] //## issuekeys: MSG_TYPE_MISMATCH
  var arrays113 = Integer[] as java.lang.Number[] //## issuekeys: MSG_TYPE_MISMATCH
  var arrays114 = String[] as List<String>  //## issuekeys: MSG_TYPE_MISMATCH
  var arrays115 = List<String> as String[]  //## issuekeys: MSG_TYPE_MISMATCH
  var arrays116 = String[] as String

  //Blocks
  var block111 = block() as Runnable  //## issuekeys: MSG_TYPE_MISMATCH
  var block112 = Runnable as block()  //## issuekeys: MSG_TYPE_MISMATCH
  var block113 = block() as IA  //## issuekeys: MSG_TYPE_MISMATCH
  var block114 = block() as AImpl  //## issuekeys: MSG_TYPE_MISMATCH
  var block115 = block() as String

  //Interface and Structure

  var interface111 = IA as IA   //## issuekeys: MSG_TYPE_MISMATCH
  var interface112 = IA as SA
  var interface113 = IA as AImpl  //## issuekeys: MSG_TYPE_MISMATCH
  var interface114 = IA as String

  var interface115 = AImpl as IA  //## issuekeys: MSG_TYPE_MISMATCH
  var interface116 = AImpl as SA
  var interface117 = AImpl as AImpl //## issuekeys: MSG_TYPE_MISMATCH
  var interface118 = AImpl as String

  var interface119 = SA as IA     //## issuekeys: MSG_TYPE_MISMATCH
  var interface120 = SA as SA
  var interface121 = SA as AImpl  //## issuekeys: MSG_TYPE_MISMATCH
  var interface122 = SA as String

  //Cast to Class<T>
  var classInteger1 : Class<Integer> = Integer as Class<Integer>

  var classNumber1 : Class<Number> = Number as Class<Number>

  var classBigDecimal1 : Class<BigDecimal> = BigDecimal as Class<BigDecimal>

  var classGeneric1 : Class<T> = T as Class<T>

}
