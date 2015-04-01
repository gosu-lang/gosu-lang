package gw.specContrib.statements

uses java.lang.Iterable
uses java.util.Iterator
uses java.util.Map
uses java.util.Set

class Errant_AllowedTypesInForLoop {

  function foo() {
    var string: String
    var object: Object
    var stringInterval = ""..""
    var intInterval = 1..2
    var iterable: Iterable
    var iter: Iterator
    var array: Object[]
    var list: List
    var s: Set
    var map: Map

    for (i in string) {}
    for (i in object) {}      //## issuekeys: FOREACH NOT APPLICABLE TO TYPE 'JAVA.LANG.OBJECT'.
    for (i in stringInterval) {}      //## issuekeys: FOREACH NOT APPLICABLE TO TYPE 'GW.LANG.REFLECT.INTERVAL.COMPARABLEINTERVAL<JAVA.LANG.STRING>'.
    for (i in intInterval) {}
    for (i in iterable) {}
    for (i in iter) {}
    for (i in array) {}
    for (i in list) {}
    for (i in s) {}
    for (i in map) {}      //## issuekeys: FOREACH NOT APPLICABLE TO TYPE 'JAVA.UTIL.MAP<JAVA.LANG.OBJECT,JAVA.LANG.OBJECT>'.
    for (i in map.values()) {}
  }

}
