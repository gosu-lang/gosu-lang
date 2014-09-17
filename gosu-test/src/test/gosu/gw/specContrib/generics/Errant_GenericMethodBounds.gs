uses java.io.Serializable
uses java.lang.Double
uses java.lang.Integer

class Errant_GenericMethodBounds {
  function upperBoundNumber<T extends Number>(t : T) : T { return null }
  function caller1() {
    upperBoundNumber("string")   //## issuekeys: MSG_
  }

  function upperBoundInteger<T extends Integer>(t1 : T, t2 : T, t3 : T) {}
  function caller2() {
    upperBoundInteger(1, 2, 3)
    upperBoundInteger(1, 2)          //## issuekeys: MSG_
    upperBoundInteger('c')           //## issuekeys: MSG_
    upperBoundInteger('c', 'c')      //## issuekeys: MSG_
    upperBoundInteger('c', 'c', 'c')
    upperBoundInteger(1b)            //## issuekeys: MSG_
    upperBoundInteger(1 as short, 1s, 1s, 1s, 1s)  //## issuekeys: MSG_
  }

  function upperBoundDouble<T extends Double>(t1 : T, t2 : T) {}
  function caller3() {
    upperBoundDouble(2.5)        //## issuekeys: MSG_
    upperBoundDouble('c')        //## issuekeys: MSG_
    upperBoundDouble(1, 2)
    upperBoundDouble('c', 'c')
    upperBoundDouble(222)        //## issuekeys: MSG_
  }

  function upperBoundSerializable<T extends Serializable>(t1: T, t2: T) {}
  function caller4() {
    var m = {1 -> 2}
    var l = {1, 2, 3}
    upperBoundSerializable(m, l)
    upperBoundSerializable({1 -> 2}, {1, 2, 3})
  }
}
