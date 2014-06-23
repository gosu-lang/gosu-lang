package gw.internal.gosu.compiler.enhancements

enhancement SuperClassWithStaticProtectedMembersEnhancement : gw.internal.gosu.parser.sub.SubClassOfSuperClassWithStaticProtectedMembers {
  static function accessStaticProtectedFieldOnSuper() : String {
    return gw.internal.gosu.parser.SuperClassWithStaticProtectedMembers.g_staticString
  }
  
  static function accessStaticProtectedMethodOnSuper() : String {
    return gw.internal.gosu.parser.SuperClassWithStaticProtectedMembers.staticMethod( "poo" )
  }
}
