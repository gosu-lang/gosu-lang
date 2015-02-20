package gw.specContrib.enums

class Errant_EmptyConstantsEnum {
  enum E1 {
    RED, BLUE, , , , , , GREEN   //## issuekeys: MSG_
  }

  enum E2 {
    RED,
  }

  enum E3 {
    RED, ;
  }
}