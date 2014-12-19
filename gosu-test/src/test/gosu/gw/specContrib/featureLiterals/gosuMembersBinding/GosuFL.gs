package gw.specContrib.featureLiterals.gosuMembersBinding

class GosuFL {
  //fields
  internal var gosuFieldInternal: String
  private var gosuFieldPrivate: String
  protected var gosuFieldProtected: String
  public var gosuFieldPublic: String

  //properties with 'as' keyword
  var myProp: String as MyProp

  //properties with get/set
  internal property get MyPropInternal(): String {
    return null
  }

  internal property set MyPropInternal(str1: String) {
  }

  private property get MyPropPrivate(): String {
    return null
  }

  private property set MyPropPrivate(str1: String) {
  }

  protected property get MyPropProtected(): String {
    return null
  }

  protected property set MyPropProtected(str1: String) {
  }

  public property get MyPropPublic(): String {
    return null
  }

  public property set MyPropPublic(str1: String) {
  }

  //functions
  internal function gosuInternalFun(str1: String, int1: int): String {
    return null
  }

  private function gosuPrivateFun(str1: String, int1: int): String {
    return null
  }

  protected function gosuProtectedFun(str1: String, int1: int): String {
    return null
  }

  public function gosuPublicFun(str1: String, int1: int): String {
    return null
  }

}