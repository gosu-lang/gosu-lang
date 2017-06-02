package gw.specContrib.statements.usesStatement

uses Constants#StaticFunc4(String)
uses Constants#StaticProp4

class Errant_StaticImportFromEnahncement {
  function test() {
    StaticFunc4(" ")
    print(StaticProp4)
  }
}