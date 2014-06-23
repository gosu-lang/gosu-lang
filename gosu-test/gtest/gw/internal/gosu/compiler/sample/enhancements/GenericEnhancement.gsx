package gw.internal.gosu.compiler.sample.enhancements

uses gw.lang.reflect.IType

enhancement GenericEnhancement<T> : _GenericEnhanced<T> {

  function simpleMethod() : String {
    return "foo"
  }

  function returnsGenericTypeVar() : IType {
    return T
  }

  function returnsGenericTypeVarWithArg( s : String ) : IType {
    return T
  }

  function methodWithArg( s : String ) : String {
    return s
  }

  function methodWithVar() : IType {
    var tmp = ""
    return T
  }

  function methodWithVarAndArg( s : String ) : IType {
    var tmp = ""
    return T
  }

  function methodWTypeParamThatReturnsEnhancementParameterization<Q>() : IType {
    return T
  }

  function methodWTypeParamThatReturnsMethodParameterization<Q>() : IType {
    return Q
  }

  function methodWTypeParamAndArgThatReturnsEnhancementParameterization<Q>( arg : Q ) : IType {
    return T
  }

  function methodWTypeParamAndArgThatReturnsMethodParameterization<Q>( arg : Q ) : IType {
    return Q
  }

  // Direct invocation

  function directlyInvokesMethodWTypeParamThatReturnsEnhancementParameterization<Q>() : IType {
    return methodWTypeParamThatReturnsEnhancementParameterization()
  }
  
  function directlyInvokesMethodWTypeParamExplicitlySetThatReturnsMethodParameterization<Q>() : IType {
    return methodWTypeParamThatReturnsMethodParameterization<Q>()
  }

  function directlyInvokesMethodWTypeParamNotSetThatReturnsMethodParameterization<Q>() : IType {
    return methodWTypeParamThatReturnsMethodParameterization()
  }
  
  function directlyInvokesMethodWTypeParamAndArgThatReturnsEnhancementParameterization<Q>( arg : Q ) : IType {
    return methodWTypeParamAndArgThatReturnsEnhancementParameterization( arg )
  }

  function directlyInvokesMethodWTypeParamAndArgThatReturnsMethodParameterization<Q>( arg : Q ) : IType {
    return methodWTypeParamAndArgThatReturnsMethodParameterization( arg )
  }

  // Indirect invocation

  function indirectlyInvokesMethodWTypeParamThatReturnsEnhancementParameterization<Q>() : IType {
    return this.methodWTypeParamThatReturnsEnhancementParameterization()
  }
  
  function indirectlyInvokesMethodWTypeParamExplicitlySetThatReturnsMethodParameterization<Q>() : IType {
    return this.methodWTypeParamThatReturnsMethodParameterization<Q>()
  }

  function indirectlyInvokesMethodWTypeParamNotSetThatReturnsMethodParameterization<Q>() : IType {
    return this.methodWTypeParamThatReturnsMethodParameterization()
  }
  
  function indirectlyInvokesMethodWTypeParamAndArgThatReturnsEnhancementParameterization<Q>( arg : Q ) : IType {
    return this.methodWTypeParamAndArgThatReturnsEnhancementParameterization( arg )
  }

  function indirectlyInvokesMethodWTypeParamAndArgThatReturnsMethodParameterization<Q>( arg : Q ) : IType {
    return this.methodWTypeParamAndArgThatReturnsMethodParameterization( arg )
  }

  function returnsBlockToBlockToBlockToT() : block():block():block():Type {
    return \->\->\->T
  }
  
  function returnsBlockToBlockToBlockToQ<Q>() : block():block():block():Type {
    return \->\->\->Q
  }
  
  function returnsBlockToBlockToQ<Q>() : block():block():Type {
    return \->\->Q
  }
  
  function returnsBlockToBlockToT() : block():block():Type {
    return \->\->T
  }
  
  function returnsBlockToQ<Q>() : block():Type {
    return \->Q
  }
  
  function returnsBlockToT() : block():Type {
    return \->T
  }
  
  function callsGenericMethodReturnsQIndirectlyWithT<Q>() : Type {
    return this.basicGenericMethodReturnsQ<T>()
  }
  
  function callsGenericMethodReturnsQIndirectlyWithQ<Q>() : Type {
    return this.basicGenericMethodReturnsQ<Q>()
  }
  
  function callsGenericMethodReturnsQDirectlyWithT<Q>() : Type {
    return basicGenericMethodReturnsQ<T>()
  }
  
  function callsGenericMethodReturnsQDirectlyWithQ<Q>() : Type {
    return basicGenericMethodReturnsQ<Q>()
  }
  
  function callsGenericMethodReturnsTDirectly() : Type {
    return T
  }
  
  function basicGenericMethodReturnsQ<Q>() : Type {
    return Q
  }
  
  function basicGenericMethodReturnsT() : Type {
    return T
  }
}