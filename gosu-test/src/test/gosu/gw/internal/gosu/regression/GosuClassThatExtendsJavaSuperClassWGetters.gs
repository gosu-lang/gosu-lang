package gw.internal.gosu.regression

class GosuClassThatExtendsJavaSuperClassWGetters extends JavaSuperClassWithGetters {

  function referAsUnqualifiedPublicProp() : int {
    return PublicProp
  }

  function referAsQualifiedPublicProp() : int {
    return this.PublicProp
  }

  function referAsUnqualifiedPublicMethod() : int {
    return getPublicProp()
  }

  function referAsQualifiedPublicMethod() : int {
    return this.PublicProp
  }

  function referAsUnqualifiedProtectedProp() : int {
    return ProtectedProp
  }

  function referAsQualifiedProtectedProp() : int {
    return this.ProtectedProp
  }

  function referAsUnqualifiedProtectedMethod() : int {
    return getProtectedProp()
  }

  function referAsQualifiedProtectedMethod() : int {
    return this.ProtectedProp
  }

  function referAsUnqualifiedProtectedPropWOverload() : int {
    return ProtectedPropWOverload
  }

  function referAsQualifiedProtectedPropWOverload() : int {
    return this.ProtectedPropWOverload
  }

  function referAsUnqualifiedProtectedMethodWOverload() : int {
    return getProtectedPropWOverload()
  }

  function referAsQualifiedProtectedMethodWOverload() : int {
    return this.ProtectedPropWOverload
  }

}