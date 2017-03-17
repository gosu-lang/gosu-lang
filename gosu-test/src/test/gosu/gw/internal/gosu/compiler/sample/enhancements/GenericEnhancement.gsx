package gw.internal.gosu.compiler.sample.enhancements

uses gw.lang.reflect.IType

enhancement GenericEnhancement<T> : _GenericEnhanced<T> {

  function simpleMethod() : String {
    return "foo"
  }

  reified function returnsGenericTypeVar() : IType {
    return T
  }

  reified function returnsGenericTypeVarWithArg( s : String ) : IType {
    return T
  }

  function methodWithArg( s : String ) : String {
    return s
  }

  reified function methodWithVar() : IType {
    var tmp = ""
    return T
  }

  reified function methodWithVarAndArg( s : String ) : IType {
    var tmp = ""
    return T
  }

  reified function methodWTypeParamThatReturnsEnhancementParameterization<Q>() : IType {
    return T
  }

  reified function methodWTypeParamThatReturnsMethodParameterization<Q>() : IType {
    return Q
  }

  reified function methodWTypeParamAndArgThatReturnsEnhancementParameterization<Q>( arg : Q ) : IType {
    return T
  }

  reified function methodWTypeParamAndArgThatReturnsMethodParameterization<Q>( arg : Q ) : IType {
    return Q
  }

  // Direct invocation

  reified function directlyInvokesMethodWTypeParamThatReturnsEnhancementParameterization<Q>() : IType {
    return methodWTypeParamThatReturnsEnhancementParameterization()
  }
  
  reified function directlyInvokesMethodWTypeParamExplicitlySetThatReturnsMethodParameterization<Q>() : IType {
    return methodWTypeParamThatReturnsMethodParameterization<Q>()
  }

  reified function directlyInvokesMethodWTypeParamNotSetThatReturnsMethodParameterization<Q>() : IType {
    return methodWTypeParamThatReturnsMethodParameterization()
  }
  
  reified function directlyInvokesMethodWTypeParamAndArgThatReturnsEnhancementParameterization<Q>( arg : Q ) : IType {
    return methodWTypeParamAndArgThatReturnsEnhancementParameterization( arg )
  }

  reified function directlyInvokesMethodWTypeParamAndArgThatReturnsMethodParameterization<Q>( arg : Q ) : IType {
    return methodWTypeParamAndArgThatReturnsMethodParameterization( arg )
  }

  // Indirect invocation

  reified function indirectlyInvokesMethodWTypeParamThatReturnsEnhancementParameterization<Q>() : IType {
    return this.methodWTypeParamThatReturnsEnhancementParameterization()
  }
  
  reified function indirectlyInvokesMethodWTypeParamExplicitlySetThatReturnsMethodParameterization<Q>() : IType {
    return this.methodWTypeParamThatReturnsMethodParameterization<Q>()
  }

  reified function indirectlyInvokesMethodWTypeParamNotSetThatReturnsMethodParameterization<Q>() : IType {
    return this.methodWTypeParamThatReturnsMethodParameterization()
  }
  
  reified function indirectlyInvokesMethodWTypeParamAndArgThatReturnsEnhancementParameterization<Q>( arg : Q ) : IType {
    return this.methodWTypeParamAndArgThatReturnsEnhancementParameterization( arg )
  }

  reified function indirectlyInvokesMethodWTypeParamAndArgThatReturnsMethodParameterization<Q>( arg : Q ) : IType {
    return this.methodWTypeParamAndArgThatReturnsMethodParameterization( arg )
  }

  reified function returnsBlockToBlockToBlockToT() : block():block():block():Type {
    return \->\->\->T
  }
  
  reified function returnsBlockToBlockToBlockToQ<Q>() : block():block():block():Type {
    return \->\->\->Q
  }
  
  reified function returnsBlockToBlockToQ<Q>() : block():block():Type {
    return \->\->Q
  }
  
  reified function returnsBlockToBlockToT() : block():block():Type {
    return \->\->T
  }
  
  reified function returnsBlockToQ<Q>() : block():Type {
    return \->Q
  }
  
  reified function returnsBlockToT() : block():Type {
    return \->T
  }
  
  reified function callsGenericMethodReturnsQIndirectlyWithT<Q>() : Type {
    return this.basicGenericMethodReturnsQ<T>()
  }
  
  reified function callsGenericMethodReturnsQIndirectlyWithQ<Q>() : Type {
    return this.basicGenericMethodReturnsQ<Q>()
  }
  
  reified function callsGenericMethodReturnsQDirectlyWithT<Q>() : Type {
    return basicGenericMethodReturnsQ<T>()
  }
  
  reified function callsGenericMethodReturnsQDirectlyWithQ<Q>() : Type {
    return basicGenericMethodReturnsQ<Q>()
  }
  
  reified function callsGenericMethodReturnsTDirectly() : Type {
    return T
  }
  
  reified function basicGenericMethodReturnsQ<Q>() : Type {
    return Q
  }
  
  reified function basicGenericMethodReturnsT() : Type {
    return T
  }
}