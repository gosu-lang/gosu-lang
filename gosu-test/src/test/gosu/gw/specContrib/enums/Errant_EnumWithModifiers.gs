package gw.specContrib.enums

class Errant_EnumWithModifiers {
  final enum MyEnum{RED, BLUE}      //## issuekeys: MODIFIER 'FINAL' NOT ALLOWED HERE
  abstract enum MyEnum2 {RED, BLUE}      //## issuekeys: MODIFIER 'ABSTRACT' NOT ALLOWED HERE
}