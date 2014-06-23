package gw.internal.gosu.parser.classTests.gwtest.visibility

class Visibility
{

  private function privateFun(){
  }

  protected function protectedFun(){
  }

  public function publicFun(){
  }

  private static function privateStaticFun(){
  }

  protected static function protectedStaticFun(){
  }

  public static function publicStaticFun(){
  }

  private property get PrivateProperty() : String {
    return ""
  }

  public property get PublicProperty() : String {
    return ""
  }

  protected property get ProtectedProperty() : String {
    return ""
  }

  private static property get PrivateStaticProperty() : String {
    return ""
  }

  public static property get PublicStaticProperty() : String {
    return ""
  }
  public static property set PublicStaticProperty(s : String) {
    // do nothing
  }

  protected static property get ProtectedStaticProperty() : String {
    return ""
  }

  private class PrivateClass {
  }

  internal class InternalClass {
  }

  protected class ProtectedClass {
  }

  class PublicClass {
  }
}
