package gw.lang.parser.restrictions;

import gw.test.TestClass;

import java.util.EnumSet;

/**
 * Baseline rules can only be created within this package, see Restrictions#baseline()
 */
public class BaselineShadowedAllowTest extends TestClass
{
  public void testBaselineAllowOverriddenByBaselineDeny()
  {
    Builder b = baseline();
    b.allowPackage( "org.example" ).denyPackageTree( "org" );
    b._origin = Restrictions.Origin.HOST;
    assertShadowed( b, "baseline allow org.example.* has no effect, it is overridden by baseline deny org.**" );
  }

  public void testBaselineAllowOverriddenByBaselineDenyHasNoHint()
  {
    // the conflict is in the baseline, not the host's to fix
    Builder b = baseline();
    b.allowType( "org.example.Foo" ).denyType( "org.example.Foo" );
    b._origin = Restrictions.Origin.HOST;
    String message = buildError( b );
    assertTrue( message, message.contains( "baseline allow org.example.Foo has no effect, it is overridden by baseline deny org.example.Foo" ) );
    assertFalse( message, message.contains( "withoutBaselineDeny" ) );
  }

  public void testBaselineAllowOverriddenByHostDenyIsAllowed()
  {
    Builder b = baseline();
    b.allowPackage( "org.example" );
    b._origin = Restrictions.Origin.HOST;
    b.denyPackageTree( "org" ).build();
  }

  public void testBaselineConflictRemovedByWithoutBaselineDeny()
  {
    Builder b = baseline();
    b.allowPackage( "org.example" ).denyPackageTree( "org" );
    b._origin = Restrictions.Origin.HOST;
    b.withoutBaselineDeny( "org.**" ).build();
  }

  public void testBaselineAndHostConflictsReportedTogether()
  {
    Builder b = baseline();
    b.allowPackage( "org.example" ).denyPackageTree( "org" );
    b._origin = Restrictions.Origin.HOST;
    b.allowType( "org.other.Bar" );
    String message = buildError( b );
    assertTrue( message, message.contains( "baseline allow org.example.* has no effect, it is overridden by baseline deny org.**" ) );
    assertTrue( message, message.contains( "allow org.other.Bar has no effect, it is overridden by baseline deny org.**, see withoutBaselineDeny(\"org.**\")" ) );
  }

  private static Builder baseline()
  {
    return new Builder( Restrictions.Origin.BASELINE, new RuleSet(), new RuleSet(), EnumSet.noneOf( LanguageFeature.class ) );
  }

  private static void assertShadowed( Builder b, String expected )
  {
    String message = buildError( b );
    assertTrue( message, message.contains( expected ) );
  }

  private static String buildError( Builder b )
  {
    try
    {
      b.build();
      fail( "Expected IllegalStateException" );
      return null;
    }
    catch( IllegalStateException e )
    {
      return e.getMessage();
    }
  }
}
