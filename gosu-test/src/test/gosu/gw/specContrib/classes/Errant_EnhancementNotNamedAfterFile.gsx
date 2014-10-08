package gw.specContrib.classes

uses java.lang.Runnable

enhancement  Errant_EnhancementNotNamedAfterFile_222: String  {      //## issuekeys: MSG_WRONG_CLASSNAME

  function test() {
    new Runnable(){
      override function run() {
      }
    }.run()
  }

}
