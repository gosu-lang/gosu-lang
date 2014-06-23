package gw.internal.gosu.compiler.generics.stuff

class CallsGenericMethodInConstructor {

  construct() {
    genericMethodThatTakesType( typeof String )
  }
  
  final function genericMethodThatTakesType<T>( t : Type<T> ) {
  }

}
