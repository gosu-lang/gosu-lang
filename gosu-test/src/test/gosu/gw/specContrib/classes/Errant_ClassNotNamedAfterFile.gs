package gw.specContrib.classes

uses java.lang.Runnable

class Errant_ClassNotNamedAfterFile_222 {      //## issuekeys: CLASS 'ERRANT_CLASSNOTNAMEDAFTERFILE_222' SHOULD BE DECLARED IN A FILE NAMED 'ERRANT_CLASSNOTNAMEDAFTERFILE_222'

  class Inner {
  }

  function test() {
    new Runnable(){
      override function run() {
      }
    }.run()
  }

}
