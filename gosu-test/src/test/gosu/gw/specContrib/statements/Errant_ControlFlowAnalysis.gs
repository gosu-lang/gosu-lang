package gw.specContrib.statements

/**
 * Created by dpatsute on 11/4/2016.
 * Added Tests for JIRA - IDE-2327
 */
class Errant_ControlFlowAnalysis {
  class A {
    enum MyEnum {ONE, TWO, THREE}

    final var a: int

    construct(value: MyEnum) {
      switch (value) {  // here Gosu is smart enough to understand that all cases considered and 'a' initialized in any case
        case ONE:
          a = 0
          break
        case TWO:
          a = 2
          break;
        case THREE:
          a = 3
          break;
      }
    }

    function foo1(x: Object) {
      switch (typeof(x)) {
        case MyEnum:
          if (x == ONE) {
            break
          } else {
            break
          }
        case Runnable:
          x.run()       // no error here - amazing Gosu smartness! it detects that all control flows in "case MyEnum"
          // ended with "break", so 'x' can be only Runnable
      }
    }

    function foo2(x: Object) {
      switch (typeof(x)) {
        case MyEnum:
          switch (x) {
            case ONE:
              return
            case TWO:
              return
            case THREE:
              return
          }
        case Runnable:
          x.run()         //as "case MyEnum" always ends with "return" - gosu knows x could only be 'Runnable' here.
      }
    }

    function foo3(x: Object) {
      switch (typeof(x)) {
        case MyEnum:
          switch (x) {
            case ONE:
              return
            case TWO:
              return
            case THREE:
              break
          }
        case Runnable: //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          if (x typeis Runnable)    //As all "case MyEnum" are NOT ending with "return" - before doing any operation on x; parser and compiler needs to identify x is type of 'Runnable' or not.
            x.run()                 //This behaviour is identical with Java.
      }
    }
  }
}