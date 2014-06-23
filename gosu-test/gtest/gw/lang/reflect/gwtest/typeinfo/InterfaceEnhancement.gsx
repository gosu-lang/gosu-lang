package gw.lang.reflect.gwtest.typeinfo

enhancement InterfaceEnhancement : gw.lang.reflect.BaseI {
  private property get EnhPrivateProp() : String {return null;}
  protected property get EnhProtectedProp() : String {return null;}
  internal property get EnhInternalProp() : String {return null;}
  public property get EnhPublicProp() : String {return null;}

  public function EnhPublicFunc() {}
  internal function EnhInternalFunc() {}
  protected function EnhProtectedFunc() {}
  private function EnhPrivateFunc() {}
}
