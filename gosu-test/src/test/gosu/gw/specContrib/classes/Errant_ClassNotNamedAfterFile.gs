package gw.specContrib.classes

uses java.lang.Runnable

class Errant_ClassNotNamedAfterFile_222 {      //## issuekeys: MSG_WRONG_CLASSNAME

  class Inner {
  }

  function test() {
    new Runnable(){
      override function run() {
      }
    }.run()
  }

}
