package gw.specification.structures

uses gw.testharness.DoNotVerifyResource
uses java.lang.Runnable

@DoNotVerifyResource
class Errant_StructureAssignableToInterfaceTest {

  interface IEmpty {
  }
  structure IStructure extends IEmpty {
    function callMe( a: String ) : int
  }

  structure IRunnableStructure extends Runnable {
  }

  structure IRunnableStructureWithStuff extends Runnable {
    function fred()
  }

  structure IRunnableStructureWithOwnRun extends Runnable {
    function run()
  }

  function assignabilityEmptyInterface() {
    var struct: IStructure
    var empty: IEmpty

    // Error, IEmpty does not satisfy IStructure
    struct = empty  //## issuekeys: MSG_TYPE_MISMATCH
    // Error, IStructure is not nominally assignable to IEmpty
    empty = struct  //## issuekeys: MSG_TYPE_MISMATCH
  }

  function assignabilityRunnabble() {
    var struct: IRunnableStructure
    var runnable: Runnable

    // Ok, Runnable satisfies empty IRunnableStructure
    struct = runnable
    // Error, IRunnableStructure is not nominally assignable to Runnable
    runnable = struct  //## issuekeys: MSG_TYPE_MISMATCH
  }

  function assignabilityRunnabbleWithStuff() {
    var struct: IRunnableStructureWithStuff
    var runnable: Runnable

    // Error, Runnable does not satisfy IRunnableStructureWithStuff
    struct = runnable  //## issuekeys: MSG_TYPE_MISMATCH
    // Error, IRunnableStructureWithStuff is not nominally assignable to Runnable
    runnable = struct  //## issuekeys: MSG_TYPE_MISMATCH
  }

  function assignabilityRunnabbleWithOwnRun() {
    var struct: IRunnableStructureWithOwnRun
    var runnable: Runnable

    // Ok, Runnable satisfies IRunnableStructureWithOwnRun
    struct = runnable
    // Error, IRunnableStructureWithOwnRun is not nominally assignable to Runnable
    runnable = struct  //## issuekeys: MSG_TYPE_MISMATCH
  }


}