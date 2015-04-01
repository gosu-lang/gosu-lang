package gw.specification.classes.classModifiers



abstract final class Errant_TopLevelClassModifiersTest194 {  //## issuekeys: MSG_ILLEGAL_USE_OF_MODIFIER

  final abstract class foo1{}        //## KB(PL-32371)  //## issuekeys: MSG_ILLEGAL_USE_OF_MODIFIER, MSG_ILLEGAL_USE_OF_MODIFIER
  abstract final class foo2{}        //## issuekeys: MSG_ILLEGAL_USE_OF_MODIFIER, MSG_ILLEGAL_USE_OF_MODIFIER

}
