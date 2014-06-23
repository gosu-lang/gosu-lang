package gw.internal.gosu.compiler.sample.statement.classes
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class VoidExpressions {
  
  function badMethodCalls() {
    var methodCall = print("foo")
    var ternary = true ? print("foo") : ""
    var additive =  "" + print("foo")
    var shift =  print("foo") >> 10
    var typeofOp =  typeof print("foo") 
    var collectionInit = { print("foo") }
    var mapInit = { "a" -> print("foo") }
    var stringTemplate = "a${print("foo")}a"
    var parens = (print("foo"))
  }

  function badBeanMethodCalls() {
    var methodCall = this.voidFunc()
    var ternary = true ? this.voidFunc() : ""
    var beanMethodCall = this.voidFunc()
    var additive =  "" + this.voidFunc()
    var shift =  this.voidFunc() >> 10
    var typeofOp =  typeof this.voidFunc() 
    var collectionInit = { this.voidFunc() }
    var mapInit = { "a" -> this.voidFunc() }
    var stringTemplate = "a${this.voidFunc()}a"
    var parens = (this.voidFunc())
  }
  
  function badArrayExpansion() {
    var expansion = new VoidExpressions[10]*.voidFunc()
  }

  function badBlockInvocations() {
    var voidBlock = \-> print("foo")
    var methodCall = voidBlock()
    var ternary = true ? voidBlock() : ""
    var beanMethodCall = voidBlock()
    var additive =  "" + voidBlock()
    var shift =  voidBlock() >> 10
    var typeofOp =  typeof voidBlock() 
    var collectionInit = { voidBlock() }
    var mapInit = { "a" -> voidBlock() }
    var stringTemplate = "a${voidBlock()}a"
    var parens = (voidBlock())
  }

  function voidFunc() {
  }
}
