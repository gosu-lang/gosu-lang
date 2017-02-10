uses java.util.function.Function
uses java.util.function.Supplier
  
static interface Promise<P> {
  function flatMap<O>( f(p:P): Promise<O> ): Promise<O>
}

static function value<V>( v: V ): Promise<V> { return null }

static function gett<G>( f():G ) : Promise<G> { return null }

static function yieldSingle<Y>( func(sb:StringBuilder): Promise<Y> ) : List<Y> { return null }

var result = yieldSingle( \ exec -> value("foo").flatMap( \ s -> gett( \ -> s.toUpperCase() ) ) )
var string: List<String> = result // verify result is of type List<String> 
