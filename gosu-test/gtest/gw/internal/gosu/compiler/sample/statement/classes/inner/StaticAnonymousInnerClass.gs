package gw.internal.gosu.compiler.sample.statement.classes.inner

uses java.util.concurrent.Callable

class StaticAnonymousInnerClass {

  static var _callable = new Callable() {
    override function call() : Object {
      return "Static field"
    }
  }

  static function staticField() : Callable {
    return _callable
  }

  static function staticFunctionProducingCallable() : Callable {
    return  new Callable() {
      override function call() : Object {
        return "Static function"
      }
    }
  }

  static property get SaticPropProducingCallable() : Callable {
    return  new Callable() {
      override function call() : Object {
        return "Static property"
      }
    }
  }

}