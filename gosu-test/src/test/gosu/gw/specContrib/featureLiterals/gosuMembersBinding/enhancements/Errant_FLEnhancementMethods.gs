package gw.specContrib.featureLiterals.gosuMembersBinding.enhancements

uses java.lang.Integer
uses java.util.ArrayList

class Errant_FLEnhancementMethods {
  var listInt : List<Integer>
  var listString : List<String>
  var list : List
  var arrayList : ArrayList<Integer>
  //IDE-1605
  var enhFun111 = List<Integer>#enhListIntegerFun1()
  var enhFun112 = listInt#enhListIntegerFun1()

  var aenhFun111 = ArrayList<Integer>#enhListIntegerFun1()
  var aenhFun112 = arrayList#enhListIntegerFun1()

  var enhString111 = List<String>#enhListIntegerFun1()      //## issuekeys: CANNOT RESOLVE METHOD 'ENHLISTINTEGERFUN1()'
  var enhString112 = listString#enhListIntegerFun1()      //## issuekeys: CANNOT RESOLVE METHOD 'ENHLISTINTEGERFUN1()'

  var enhObj111 = List#enhListIntegerFun1()      //## issuekeys: CANNOT RESOLVE METHOD 'ENHLISTINTEGERFUN1()'
  var enhObj112 = list#enhListIntegerFun1()      //## issuekeys: CANNOT RESOLVE METHOD 'ENHLISTINTEGERFUN1()'

}