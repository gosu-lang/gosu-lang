package com.example.bootstrap

class Sub extends Super {

  /* From sub */
  function methodToOverrideDocs() {
  }

  /* From sub */
  property get PropertyToOverrideDocs() : String {
    return null
  }

  function methodToNotOverrideDocs() {
  }

  property get PropertyToNotOverrideDocs() : String {
    return null
  }

  /* {@inheritDoc} */
  function methodToInheritDoc() {
  }

  /* {@inheritDoc} */
  property get PropertyToInheritDoc() : String {
    return null
  }

}