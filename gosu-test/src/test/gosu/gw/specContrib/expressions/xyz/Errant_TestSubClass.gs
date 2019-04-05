package gw.specContrib.expressions.xyz

class Errant_TestSubClass {
   function main(args: String[]) {
      print( new Errant_SubClass().Foo ) //## issuekeys: MSG_CLASS_PROPERTY_NOT_READABLE
  }
}