package gw.internal.gosu.parser.generics

class Pl23787<E> {
  function transform<T>(f(e : E) : T) : Pl23787<T> { return null }

  static function transform<A, B>() : block(f(a : A) : B) : block(a : Pl23787<A>) : Pl23787<B> {
    return \ f : block(a : A) : B -> \ a : Pl23787<A> -> a.transform(f)
  }
}