package gw.internal.gosu.regression

class ImplementsJavaGenericInterface implements IGenericInterface<String> {
  override function invokeIt() : String {
    return "yes"
  }
}