package gw.specContrib.blocks 


class Errant_BlocksRecursiveCall {
  var sum(x: int): int = \ x -> x == 0 ? 0 : x + sum(x - 1) // one valid way

  var sum2 : block(x: int): int = \x -> x == 0 ? 0 : x + sum2(x - 1) // yet another valid way

  var sum3 = \x: int -> x == 0 ? 0 : x + sum3(x - 1)      //## issuekeys: RECURSIVE BLOCK MUST HAVE EXPLICIT BLOCK TYPE: 'SUM3(INT)'

  var sum4 = 1 + sum4      //## issuekeys: CANNOT RESOLVE SYMBOL 'SUM4'

  var sum5(x: int) = \x -> x == 0 ? 0 : x + sum5(x - 1)      //## issuekeys: OPERATOR '+' CANNOT BE APPLIED TO 'INT', 'VOID'
  
  var sumField(int): int = \x -> x == 0 ? 0 : x + sumField(x - 1) //## issuekeys: MSG_BLOCK_TYPES_SHOULD_HAVE_ARG_NAMES

  function foo() {
    var sumMember(x: int): int = \x -> x == 0 ? 0 : x + sumMember(x - 1)
  }

  class Foo {
    var sumMember(x: int): int = \ x -> x == 0 ? 0 : x + sumMember(x - 1) // works
  }
}
