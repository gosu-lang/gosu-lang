package gw.specContrib.classes

uses java.lang.Runnable

enhancement  Errant_EnhancementNotNamedAfterFile_222: String  {      //## issuekeys: ENHANCEMENT 'ERRANT_ENHANCEMENTNOTNAMEDAFTERFILE_222' SHOULD BE DECLARED IN A FILE NAMED 'ERRANT_ENHANCEMENTNOTNAMEDAFTERFILE_222'

  function test() {
    new Runnable(){
      override function run() {
      }
    }.run()
  }

}
