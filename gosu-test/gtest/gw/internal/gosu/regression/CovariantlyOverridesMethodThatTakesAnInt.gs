package gw.internal.gosu.regression

class CovariantlyOverridesMethodThatTakesAnInt extends HasMethodThatTakesAnInt {

  construct() {

  }
  
  override function foo(i : int) : String {
    return "Child" + i  
  }

}
