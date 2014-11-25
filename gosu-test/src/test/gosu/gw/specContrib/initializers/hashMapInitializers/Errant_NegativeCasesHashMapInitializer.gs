package gw.specContrib.initializers.hashMapInitializers

uses java.util.HashMap

class Errant_NegativeCasesHashMapInitializer {

  function oneFun() {  var negativeCasesHashMap0111: HashMap = {1->} }      //## issuekeys: UNEXPECTED TOKEN: }

  function twoFun() {    var negativeCasesHashMap0114: HashMap = {1->2, , 3->4} }      //## issuekeys: UNEXPECTED TOKEN: ,

  function threeFun() {     var negativeCasesHashMap0116: HashMap = {1->2->3->4} }      //## issuekeys: RBRACE EXPECTED

}