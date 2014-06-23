package gw.internal.gosu.parser.composition

uses java.lang.Class

class GenMethodMix implements IGenMethod {
  override function getRemoteObject<T>(arg: Class <T>): T {
    return arg.newInstance()
  }
}
