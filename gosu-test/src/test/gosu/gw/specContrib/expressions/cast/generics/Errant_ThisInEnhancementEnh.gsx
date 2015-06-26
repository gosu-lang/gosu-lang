package gw.specContrib.expressions.cast.generics

enhancement Errant_ThisInEnhancementEnh<E> : Errant_ThisInEnhancementClasses.A<E> {
  function foo() {
    // IDE-2275
    if (this typeis Errant_ThisInEnhancementClasses.B) {}
  }
}