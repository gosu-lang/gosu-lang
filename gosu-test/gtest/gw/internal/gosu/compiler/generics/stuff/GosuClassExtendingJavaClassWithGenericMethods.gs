package gw.internal.gosu.compiler.generics.stuff

class GosuClassExtendingJavaClassWithGenericMethods extends JavaClassWithGenericMethods
{

  function callIdentity() : String {
    return identity( "foo" )
  }

  function callStaticIdentity() : String {
    return staticIdentity( "foo" )
  }

  static function staticCallStaticIdentity() : String {
    return staticIdentity( "foo" )
  }

  function callIdentityIndirectly() : String {
    return this.identity( "foo" )
  }

  function callStaticIdentityIndirectly() : String {
    return this.staticIdentity( "foo" )
  }

  static function staticCallStaticIdentityIndirectly() : String {
    return GosuClassExtendingJavaClassWithGenericMethods.staticIdentity( "foo" )
  }

  function callIdentityToOverride() : String {
    return identityToOverride( "foo" )
  }

  function callStaticIdentityToOverride() : String {
    return staticIdentityToOverride( "foo" )
  }

  static function staticCallStaticIdentityToOverride() : String {
    return staticIdentityToOverride( "foo" )
  }

  function callIdentityIndirectlyToOverride() : String {
    return this.identityToOverride( "foo" )
  }

  function callStaticIdentityIndirectlyToOverride() : String {
    return this.staticIdentityToOverride( "foo" )
  }

  static function staticCallStaticIdentityIndirectlyToOverride() : String {
    return GosuClassExtendingJavaClassWithGenericMethods.staticIdentityToOverride( "foo" )
  }

  function identityToOverride<T>( t : T ) : T
  {
    return null
  }

  static function staticIdentityToOverride<T>( t : T ) : T
  {
    return null
  }
}