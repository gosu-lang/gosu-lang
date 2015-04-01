package gw.specification.temp.typeLiterals

uses java.io.Serializable
uses java.lang.Number

class UsesStatementHasPrecedenceOverRelativeDefaultTypeLiteral {
  function foo() : Number {
    return null
  }
}