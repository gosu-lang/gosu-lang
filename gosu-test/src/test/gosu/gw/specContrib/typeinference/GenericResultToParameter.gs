package gw.specContrib.typeinference

/**
 * Test for IDE-4142 and IDE-4166, both of which require "reverse" inference from a generic method's return type
 * to its parameter type(s).
 *
 * IDE-4166: In hello() below, the parameter type and return type are the same, so in foo(), when
 * hello() is assigned to a variable of type ArrayList<Integer>, the Gosu compiler reverse-infers that the argument
 * new ArrayList() should be coerced to new ArrayList<Integer>.
 *
 * IDE-4142: The call to parseRules() below is a more general case.  Here, the matching of generic parameters is buried more
 * deeply.  Specifically, the method reduceList() is being returned as Either<List<String>, List<Pair<Whatever1, Whatever2>>>
 * which is like assigning a variable of that type to the call to reduceList().  The generic parameters of the return
 * type of reduceList() thus get "reverse" inferred to the formal parameter types of reduceList().
 */
class GenericResultToParameter {

  function hello<T>(t: T): T {
    return null
  }

  /**
   * IDE-4166
   */
  function foo() {
    //var bar: ArrayList<Integer> = hello(new ArrayList( ))  // FIXME, reverse type inference failure. See IDE-4166 or https://github.com/gosu-lang/gosu-lang/issues/126
  }


  static class Whatever1 {
  }

  static class Whatever2 {
  }

  static abstract class Goal {
  }

  static class Either<L, R> {
    function getLeft<L1>(): L1 {
      return null
    }

    function getRight<R1>(): R1 {
      return null
    }

    reified function mapLeft<L1>(f(arg: L): L1): Either<L1, R> {
      return new Either<L1, R>()
    }

    reified function mapRight<R1>(f: (arg: R): R1): Either<L, R1> {
      return new Either<L, R1>()
    }
  }

  static class Pair<L, R> {
    public construct(l: L, r: R) {
    }

    public function getL(): L {
      return null
    }

    public function getR(): R {
      return null
    }
  }

  /**
   * IDE-4142
   */
  private static function parseRules(
      rules: Hashtable<Object, Object>,
      goals(tgt: String): Either<String, Goal>
  ): Either<List<String>, List<Pair<Whatever1, Whatever2>>> {
    final var parsedGoals =
        rules.entrySet()
            .map(\entry ->
                goals(entry.Value.toString())
                    .mapLeft(\err -> "Error: " + err)
                    .mapRight(\goal -> createPair(goal))
            )
    // parsedGoals' type is: java.util.List<Either<Object,Pair<Whatever1, Whatever2>>>
    var x = parsedGoals.get(0).getLeft()  // x's type is Object
    var y = parsedGoals.get(0).getRight() // y's type is Pair<Whatever1, Whatever2>
    // Should infer left of Either as String, not Object, which appears to be the problem below.
    // Changing the return type of parseRules to : Either<List<Object>, List<Pair<Whatever1, Whatever2>>> fixes it.
    return reduceList(parsedGoals)    // Should be okay
  }

  static function reduceList<L1, R1>(items: Iterable<Either<L1, R1>>): Either<List<L1>, List<R1>> {
    return null
  }

  private static function createPair(goal: Goal): Pair<Whatever1, Whatever2> {
    return new Pair(
        new Whatever1(),
        new Whatever2()
    )
  }

}
