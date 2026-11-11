package gw.internal.gosu.parser.restrictions;

import gw.lang.parser.restrictions.Builder;
import gw.lang.parser.restrictions.Restrictions;
import gw.test.TestClass;

/**
 * An allow rule that a deny rule completely overrides has no effect, so build() rejects it
 */
public class RestrictionsBuilderTest extends TestClass
{
  public void testAllowTypeOverriddenByBaselineDeny()
  {
    // baseline denies java.lang.Thread, java.lang.reflect.**, java.io.**
    assertShadowed( Restrictions.baseline().allowType( "java.lang.Thread" ),
                    "allow java.lang.Thread has no effect, it is overridden by baseline deny java.lang.Thread, see withoutBaselineDeny(\"java.lang.Thread\")" );
    assertShadowed( Restrictions.baseline().allowType( "java.lang.reflect.Method" ),
                    "allow java.lang.reflect.Method has no effect, it is overridden by baseline deny java.lang.reflect.**, see withoutBaselineDeny(\"java.lang.reflect.**\")" );
    assertShadowed( Restrictions.baseline().allowType( "java.io.Serializable" ),
                    "allow java.io.Serializable has no effect, it is overridden by baseline deny java.io.**, see withoutBaselineDeny(\"java.io.**\")" );
  }

  public void testAllowNestedTypeOverriddenByEnclosingTypeDeny()
  {
    assertShadowed( Restrictions.baseline().allowType( "java.lang.Thread.State" ),
                    "allow java.lang.Thread.State has no effect, it is overridden by baseline deny java.lang.Thread" );
  }

  public void testAllowPackageOverriddenByDeny()
  {
    assertShadowed( Restrictions.baseline().allowPackage( "java.net" ),
                    "allow java.net.* has no effect, it is overridden by baseline deny java.net.**" );
    assertShadowed( Restrictions.empty().allowPackage( "org.example" ).denyPackage( "org.example" ),
                    "allow org.example.* has no effect, it is overridden by deny org.example.*" );
    assertShadowed( Restrictions.empty().allowPackageTree( "org.example.api" ).denyPackageTree( "org.example" ),
                    "allow org.example.api.** has no effect, it is overridden by deny org.example.**" );
  }

  public void testAllowTypeOverriddenByHostDeny()
  {
    // the order of the calls does not matter
    assertShadowed( Restrictions.empty().denyPackage( "org.example" ).allowType( "org.example.Foo" ),
                    "allow org.example.Foo has no effect, it is overridden by deny org.example.*" );
    assertShadowed( Restrictions.empty().allowType( "org.example.Foo" ).denyType( "org.example.Foo" ),
                    "allow org.example.Foo has no effect, it is overridden by deny org.example.Foo" );
  }

  public void testAllOverriddenAllowsAreReported()
  {
    try
    {
      Restrictions.baseline().allowType( "java.lang.Thread" ).allowPackage( "java.net" ).build();
      fail( "Expected IllegalStateException" );
    }
    catch( IllegalStateException e )
    {
      assertTrue( e.getMessage(), e.getMessage().contains( "allow java.lang.Thread " ) );
      assertTrue( e.getMessage(), e.getMessage().contains( "allow java.net.* " ) );
    }
  }

  public void testPartialOverlapIsAllowed()
  {
    // a deny rule that carves a hole in an allow rule
    Restrictions.empty().allowPackageTree( "org.example.api" ).denyPackage( "org.example.api.internal" ).build();
    Restrictions.empty().allowPackageTree( "org.example.api" ).denyPackageTree( "org.example.api.internal" ).build();
    Restrictions.empty().allowPackage( "org.example" ).denyType( "org.example.Foo" ).build();
    Restrictions.empty().allowType( "org.example.Foo" ).denyType( "org.example.Foo.Inner" ).build();
    Restrictions.empty().allowType( "org.example.Foo" ).denyMember( "org.example.Foo", "bar" ).build();
    // a package deny rule does not cover subpackages
    Restrictions.empty().allowPackageTree( "org.example" ).denyPackage( "org.example" ).build();
    Restrictions.baseline().allowPackageTree( "java.lang" ).build();
  }

  public void testBaselineHasNoOverriddenAllows()
  {
    // build() checks the baseline's allow rules against its deny rules
    Restrictions.baseline().build();
  }

  public void testHostDenyMayOverrideBaselineAllow()
  {
    Restrictions.baseline().denyPackage( "java.util" ).denyPackageTree( "java.time" ).build();
  }

  public void testAllowAfterRemovingBaselineDeny()
  {
    Restrictions.baseline().withoutBaselineDeny( "java.io.**" ).allowType( "java.io.Serializable" ).build();
    Restrictions.baseline().withoutBaselineDeny( "java.lang.Thread" ).allowType( "java.lang.Thread" ).build();
  }

  public void testStringRuleForm()
  {
    assertShadowed( Restrictions.baseline().allow( "java.lang.reflect.*" ),
                    "allow java.lang.reflect.* has no effect, it is overridden by baseline deny java.lang.reflect.**" );
    assertShadowed( Restrictions.empty().allow( "org.example.Foo" ).deny( "org.example.**" ),
                    "allow org.example.Foo has no effect, it is overridden by deny org.example.**" );
  }

  private static void assertShadowed( Builder builder, String expected )
  {
    try
    {
      builder.build();
      fail( "Expected IllegalStateException: " + expected );
    }
    catch( IllegalStateException e )
    {
      assertTrue( e.getMessage(), e.getMessage().contains( expected ) );
    }
  }
}
