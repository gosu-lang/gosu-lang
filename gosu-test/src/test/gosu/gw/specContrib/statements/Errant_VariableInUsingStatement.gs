package gw.specContrib.statements

class Errant_VariableInUsingStatement {

  function foo() {
    using(var d : IDisposable = null) {
      print(d)
      d = null      //## issuekeys: CANNOT ASSIGN A VALUE TO FINAL VARIABLE 'D'
    }

    using(var i = 0) {      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'INT', REQUIRED: 'LOCK, CLOSEABLE, IREENTRANT, IDISPOSABLE OR IMONITORLOCK'
    }
  }

}
