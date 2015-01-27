package gw.specification.expressions.typeCastExpressionsAndTypeConversion

uses java.lang.*
uses java.util.*
uses java.net.SocketOption
uses gw.specification.types.typeConversion.conversionInvolvingReferenceTypes.CharAt
uses gw.specification.types.typeConversion.conversionInvolvingReferenceTypes.MyNonStaticCharAt
uses gw.specification.types.typeConversion.conversionInvolvingReferenceTypes.MyStaticCharAt
uses gw.specification.types.typeConversion.conversionInvolvingReferenceTypes.MyStaticFoo
uses gw.specification.types.typeConversion.conversionInvolvingReferenceTypes.MyNonStaticFoo

class Errant_TypeCastExpressionsTest {
  function testInterfaceToInterfaceCasting() {
    var lstStrings : List<String>
    var runnable : Runnable
    var so : SocketOption
    var soInt : SocketOption<Integer>
    var lstInt : List<Integer>

    var x0  =  lstStrings as Runnable
    var x1  =  lstStrings as SocketOption
    var x2  =  lstStrings as SocketOption<Integer>
    var x4  =  lstStrings as List<Integer>  //## issuekeys: MSG_TYPE_MISMATCH

    var x5  =  runnable as List<String>
    var x6  =  so as List<String>
    var x7  =  soInt as List<String>
    var x8  =  lstInt as List<String>  //## issuekeys: MSG_TYPE_MISMATCH
  }

  class A { }
  final class B {}
  class C<T>  {}

  function testInterfaceToClassCasting() {
    var lstStrings : List<String>

    var x0  = lstStrings as A
    var x1  = lstStrings as B  //## issuekeys: MSG_TYPE_MISMATCH
    var x2  = lstStrings as int  //## issuekeys: MSG_TYPE_MISMATCH
    var x3 =  lstStrings as String[]  //## issuekeys: MSG_TYPE_MISMATCH
    var x4 =  lstStrings as LinkedList
    var x5 =  lstStrings as LinkedList<String>
    var x6 =  lstStrings as LinkedList<Integer>  //## issuekeys: MSG_TYPE_MISMATCH
    var x7 =  lstStrings as C
    var x8 =  lstStrings as C<Integer>
    var x10 = lstStrings as block( i : int )  //## issuekeys: MSG_TYPE_MISMATCH
  }

  class D { function charAt( i: int ) : char {return '0'} }

  function testClassToInterfaceCasting() {
    var lst : LinkedList
    var lstStrings : LinkedList<String>
    var lstInt : LinkedList<Integer>
    var fun = \ ->  1;

    var x0  = new A() as List<String>
    var x1  = new B() as List<String>  //## issuekeys: MSG_TYPE_MISMATCH
    var x2  = int as List<String>  //## issuekeys: MSG_TYPE_MISMATCH
    var x3 =  String[] as List<String>  //## issuekeys: MSG_TYPE_MISMATCH
    var x4 =  lst as List<String>
    var x5 =  lstStrings as List<String>  //## issuekeys: MSG_UNNECESSARY_COERCION
    var x6 =  lstInt as List<String>  //## issuekeys: MSG_TYPE_MISMATCH
    var x7 =  fun as List<String>  //## issuekeys: MSG_TYPE_MISMATCH

    var x8 = A as CharAt   //## issuekeys: MSG_TYPE_MISMATCH
    var x9 = D as CharAt  //## issuekeys: MSG_TYPE_MISMATCH
    var x10 = D as Class<CharAt>
    var d : Class<D>
    var x11 = d as CharAt
  }

  interface I0 {}
  interface I1<T> {}
  interface Bar extends I0 {}
  class Foo<E extends I0> implements  I1<E>
  {
     function testgenericInterfacesClash() {
       var ggg = this as I1<Bar>
     }
  }

}
