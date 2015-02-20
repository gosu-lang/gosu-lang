package gw.specContrib.featureLiterals.gosuMembersBinding.genericsFL

uses java.util.ArrayList
uses java.util.HashMap

class Errant_FLCollections {
  var array1 = new int[5]

  var lengthFL = array1#length
  //IDE-1605 - Parser Issue
  var avgArrayArrayFL = array1#average()
  //IDE-1606 - OS Gosu + IDE-1605 - Parser Issue
  var getCountArrayFL = array1#getCount()
  var sumArrayFL = array1#sum()
  var cloneArrayFL = array1#clone()
  var toListArrayFL = array1#toList()
  var equalsArrayFL = array1#equals(Object)
  var hashCodeArrayFL = array1#hashCode()
  var toStringArrayFL = array1#toString()

  var aavgArrayArrayFL = Array#average()
  var agetCountArrayFL = Array#getCount()
  var asumArrayFL = Array#sum()            //## issuekeys: AMBIGUOUS METHOD CALL: BOTH 'CoreArrayDoubleSumEnhancement.sum(IBlock1<Double, Object>)' AND 'CoreArrayFloatSumEnhancement.sum(IBlock1<Float, Object>)' MATCH
  var acloneArrayFL = Array#clone()
  var atoListArrayFL = Array#toList()
  var aequalsArrayFL = Array#equals(Object)
  var ahashCodeArrayFL = Array#hashCode()
  var atoStringArrayFL = Array#toString()

  var hm : HashMap
  var clearHashMapFL = hm#clear()
  var sizeHashMapFL = hm#size()
  var isEmptyHashMapFL = hm#isEmpty()
  var cloneHashMapFL = hm#clone()
  var valuesHashMapFL = hm#values()
  var entrySetHashMapFL = hm#entrySet()
  var containsKeyHashMapFL = hm#containsKey(Object)
  var containsValueHashMapFL = hm#containsValue(Object)
  var getHashMapFL = hm#get(Object)
  var putHashMapFL = hm#put(Object, Object)
  var keySetHashMapFL = hm#keySet()
//  var putAllHashMapFL = hm#putAll(Object)
  var removeHashMapFL = hm#remove(Object)

  var hclearHashMapFL = HashMap#clear()
  var hsizeHashMapFL = HashMap#size()
  var hisEmptyHashMapFL = HashMap#isEmpty()
  var hcloneHashMapFL = HashMap#clone()
  var hvaluesHashMapFL = HashMap#values()
  var hentrySetHashMapFL = HashMap#entrySet()
  var hcontainsKeyHashMapFL = HashMap#containsKey(Object)
  var hcontainsValueHashMapFL = HashMap#containsValue(Object)
  var hgetHashMapFL = HashMap#get(Object)
  var hputHashMapFL = HashMap#put(Object, Object)
  var hkeySetHashMapFL = HashMap#keySet()
  //  var putAllHashMapFL = HashMap#putAll(Object)
  var hremoveHashMapFL = HashMap#remove(Object)

  var al : ArrayList
  var clearArrayListInstFL = al#clear()
  var sizeArrayListInstFL = al#size()
  var addArrayListInstFL = al#add(Object)
  var cloneArrayListInstFL = al#clone()
  var containsArrayListInstFL = al#contains(Object)
  var getArrayListInstFL = al#get(int)
  var isEmptyArrayListInstFL = al#isEmpty()
  var indexOfArrayListInstFL = al#indexOf(Object)
  var lastIndexOfArrayListInstFL = al#lastIndexOf(Object)
  var ensureCapacityArrayListInstFL = al#ensureCapacity(int)
  var removeArrayListInstFL = al#remove(int)
  var toArrayArrayListInstFL = al#toArray()
  var trimToSizeArrayListInstFL = al#trimToSize()

  var clearArrayListFL = ArrayList#clear()
  var sizeArrayListFL = ArrayList#size()
  var addArrayListFL = ArrayList#add(Object)
  var cloneArrayListFL = ArrayList#clone()
  var containsArrayListFL = ArrayList#contains(Object)
  var getArrayListFL = ArrayList#get(int)
  var isEmptyArrayListFL = ArrayList#isEmpty()
  var indexOfArrayListFL = ArrayList#indexOf(Object)
  var lastIndexOfArrayListFL = ArrayList#lastIndexOf(Object)
  var ensureCapacityArrayListFL = ArrayList#ensureCapacity(int)
  var removeArrayListFL = ArrayList#remove(int)
  var toArrayArrayListFL = ArrayList#toArray()
  var trimToSizeArrayListFL = ArrayList#trimToSize()
}