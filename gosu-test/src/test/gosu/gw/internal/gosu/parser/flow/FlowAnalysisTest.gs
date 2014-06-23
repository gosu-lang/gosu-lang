package gw.internal.gosu.parser.flow

uses gw.BaseVerifyErrantTest

class FlowAnalysisTest extends BaseVerifyErrantTest {

  function testErrant_IfStmt() {
    processErrantType( Errant_IfStmt )
  }

  function testErrant_SwitchStmt() {
    processErrantType( Errant_SwitchStmt )
  }

  function testErrant_TryCatchFinallyStmt() {
    processErrantType( Errant_TryCatchFinallyStmt )
  }

  function testErrant_WhileStmt() {
    processErrantType( Errant_WhileStmt )
  }

  function testErrant_DoWhileStmt() {
    processErrantType( Errant_DoWhileStmt )
  }
}