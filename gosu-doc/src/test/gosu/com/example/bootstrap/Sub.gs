package com.example.bootstrap

class Sub extends Super {

  /* From sub */
  override function methodToOverrideDocs() {
  }

  /* From sub */
  override property get PropertyToOverrideDocs() : String {
    return null
  }

  override function methodToNotOverrideDocs() {
  }

  override property get PropertyToNotOverrideDocs() : String {
    return null
  }

  /* {@inheritDoc} */
  override function methodToInheritDoc() {
  }

  /* {@inheritDoc} */
  override property get PropertyToInheritDoc() : String {
    return null
  }

}
