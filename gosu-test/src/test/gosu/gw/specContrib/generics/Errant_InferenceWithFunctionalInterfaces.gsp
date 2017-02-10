uses java.util.function.Function
uses java.util.function.Supplier

static interface Promise<P> {
  function flatMap<O>( f: Function<P,Promise<O>> ): Promise<O>
  static function value<V>( v: V ): Promise<V> { return null }
}

static function gett<G>( f: Supplier<G> ) : Promise<G> { return null }

static function yieldSingle<Y>( func: Function<StringBuilder, Promise<Y>> ) : List<Y> { return null }

var result = yieldSingle( \ exec -> Promise.value("foo").flatMap( \ s -> gett( \ -> s.toUpperCase() ) ) )
var string: List<String> = result // verify result is of type List<String>
