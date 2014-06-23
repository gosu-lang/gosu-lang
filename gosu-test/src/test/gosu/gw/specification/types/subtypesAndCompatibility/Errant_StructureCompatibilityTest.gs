package gw.specification.types.subtypesAndCompatibility

class Errant_StructureCompatibilityTest {
  structure Writer {
    function write()
  }

  structure ReaderAndWriter extends Writer{
    function read()
  }

  class myFile {
    function write() {}
  }

  function structurallyCompatible() {
    var w : Writer = null
    var f : myFile = null

    w = f
    f = w  //## issuekeys: MSG_TYPE_MISMATCH

    var r : ReaderAndWriter = null
    w = r
    r = w  //## issuekeys: MSG_TYPE_MISMATCH
  }

  interface iReaderAndWriter extends Writer {
    function write()  //## issuekeys: MSG_MISSING_OVERRIDE_MODIFIER
  }

  interface tmp extends Writer {}
  interface  iReaderAndWriter2 extends tmp {}

  function structureAndInterfaceNominalCompatibility(){
    var w : Writer = null
    var ir : iReaderAndWriter = null

    w = ir
    ir = w  //## issuekeys: MSG_TYPE_MISMATCH

    var ir2 : iReaderAndWriter2
    w = ir2
  }

  class myFile2 implements Writer {
    override function write() {
    }
  }
  class myFile3 implements tmp {
    override function write() {
    }
  }

  class myFile4 implements iReaderAndWriter2 {
    override function write() {
    }
  }

  class myFile5 extends myFile2 { }

  function structureAndClassAndInterfaceNominalCompatibility(){
    var w : Writer = null
    var f2: myFile2 = null
    var f3: myFile3 = null
    var f4: myFile4 = null
    var f5: myFile5 = null

    w = f2
    w = f3
    w = f4
    w = f5
  }
}
