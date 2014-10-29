package gw.specContrib.generics

uses java.io.Serializable
uses java.lang.Double
uses java.lang.Integer

class Errant_GenericMethodBounds {
  function upperBoundNumber<T extends Number>(t : T) : T { return null }
  function caller1() {
    upperBoundNumber("string")   //## issuekeys: MSG_TYPE_MISMATCH
  }

  function upperBoundInteger<T extends Integer>(t1 : T, t2 : T, t3 : T) {}
  function caller2() {
    upperBoundInteger(1, 2, 3)
    upperBoundInteger(1, 2)          //## issuekeys: MSG_WRONG_NUMBER_OF_ARGS_TO_FUNCTION
    upperBoundInteger('c')           //## issuekeys: MSG_WRONG_NUMBER_OF_ARGS_TO_FUNCTION
    upperBoundInteger('c', 'c')      //## issuekeys: MSG_WRONG_NUMBER_OF_ARGS_TO_FUNCTION
    upperBoundInteger('c', 'c', 'c')
    upperBoundInteger(1b)            //## issuekeys: MSG_WRONG_NUMBER_OF_ARGS_TO_FUNCTION
    upperBoundInteger(1 as short, 1s, 1s, 1s, 1s)  //## issuekeys: MSG_WRONG_NUMBER_OF_ARGS_TO_FUNCTION
  }

  function upperBoundDouble<T extends Double>(t1 : T, t2 : T) {}
  function caller3() {
    upperBoundDouble(2.5)        //## issuekeys: MSG_WRONG_NUMBER_OF_ARGS_TO_FUNCTION
    upperBoundDouble('c')        //## issuekeys: MSG_WRONG_NUMBER_OF_ARGS_TO_FUNCTION
    upperBoundDouble(1, 2)
    upperBoundDouble('c', 'c')
    upperBoundDouble(222)        //## issuekeys: MSG_WRONG_NUMBER_OF_ARGS_TO_FUNCTION
  }

  function upperBoundSerializable<T extends Serializable>(t1: T, t2: T) {}
  function caller4() {
    var m = {1 -> 2}
    var l = {1, 2, 3}
    upperBoundSerializable(m, l)  // note this is intersection type: Serializable & Cloneable
    upperBoundSerializable({1 -> 2}, {1, 2, 3})  //## issuekeys: MSG_EXPECTING_ARROW_AFTER_MAP_KEY, MSG_EXPECTING_ARROW_AFTER_MAP_KEY, MSG_EXPECTING_ARROW_AFTER_MAP_KEY
  }
}
