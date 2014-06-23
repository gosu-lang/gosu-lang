package gw.specification.variablesParametersFieldsScope.finalModifier

uses gw.BaseVerifyErrantTest

class FinalVarTest extends BaseVerifyErrantTest {

  function testErrant_ProhibitFinalFieldMutationInAnonsAndBlocks() {
    processErrantType( Errant_ProhibitFinalFieldMutationInAnonsAndBlocks )
  }

  function testErrant_FinalFieldCoverageInConstructors() {
    processErrantType( Errant_FinalFieldCoverageInConstructors )
  }

  function testErrant_StaticFinalFieldCoverageInConstructors() {
    processErrantType( Errant_StaticFinalFieldCoverageInConstructors )
  }

  function testErrant_FinalFieldCoverageInIfStatement() {
    processErrantType( Errant_FinalFieldCoverageInIfStatement )
  }

  function testErrant_FinalFieldCoverageInSwitchStatement() {
    processErrantType( Errant_FinalFieldCoverageInSwitchStatement )
  }

  function testErrant_FinalFieldExclusiveAssignmentAfterSwitchStatement() {
    processErrantType( Errant_FinalFieldExclusiveAssignmentAfterSwitchStatement )
  }

  function testErrant_FinalLocalVarCoverageInIfStatement() {
    processErrantType( Errant_FinalLocalVarCoverageInIfStatement )
  }

  function testErrant_FinalLocalVarCoverageInSwitchStatement() {
    processErrantType( Errant_FinalLocalVarCoverageInSwitchStatement )
  }

  function testErrant_FinalLocalVarSeparateAssignmentInIfStatement() {
    processErrantType( Errant_FinalLocalVarSeparateAssignmentInIfStatement )
  }

  function testErrant_FinalFieldSeparateAssignmentInIfStatement() {
    processErrantType( Errant_FinalFieldSeparateAssignmentInIfStatement )
  }

  function testErrant_FinalFieldInOtherFields() {
    processErrantType( Errant_FinalFieldInOtherFields )
  }

  function testErrant_FinalLocalVarInOtherLocalVars() {
    processErrantType( Errant_FinalLocalVarInOtherLocalVars )
  }

  function testErrant_FinalFieldCoverageFromMemberAssignment() {
    processErrantType( Errant_FinalFieldCoverageFromMemberAssignment )
  }

  function testErrant_FinalFieldCoverageViaThisMethodCall() {
    processErrantType( Errant_FinalFieldCoverageViaThisMethodCall )
  }

  function testErrant_FinalFieldBasicReferenceChecking() {
    processErrantType( Errant_FinalFieldBasicReferenceChecking )
  }

  function testErrant_FinalWithObjectOrArray() {
    processErrantType( Errant_FinalWithObjectOrArray )
  }

}