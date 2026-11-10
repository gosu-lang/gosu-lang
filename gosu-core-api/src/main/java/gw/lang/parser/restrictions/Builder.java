package gw.lang.parser.restrictions;

import java.util.EnumSet;
import java.util.Set;

public final class Builder
{
  private final RuleSet _allow;
  private final RuleSet _deny;
  private final Set<LanguageFeature> _features;
  Restrictions.Origin _origin;

  Builder( RuleSet allow, RuleSet deny, Set<LanguageFeature> features )
  {
    this( Restrictions.Origin.HOST, allow, deny, features );
  }
  Builder( Restrictions.Origin origin, RuleSet allow, RuleSet deny, Set<LanguageFeature> features )
  {
    _origin = origin;
    _allow = allow;
    _deny = deny;
    _features = EnumSet.noneOf( LanguageFeature.class );
    _features.addAll( features );
  }

  /**
   * Allows the named type, with any generic or array type information erased.
   */
  public Builder allowType( String typeName )
  {
    return add( _allow, new Rule( Restrictions.Kind.TYPE, Restrictions.normalize( typeName ), _origin ) );
  }

  /**
   * Allows the named package, not its subpackages.
   */
  public Builder allowPackage( String packageName )
  {
    return add( _allow, new Rule( Restrictions.Kind.PACKAGE, Restrictions.normalize( packageName ), _origin ) );
  }

  /**
   * Allows the named package and all its subpackages.
   */
  public Builder allowPackageTree( String packageName )
  {
    return add( _allow, new Rule( Restrictions.Kind.PACKAGE_TREE, Restrictions.normalize( packageName ), _origin ) );
  }

  /**
   * Primarily for use in the baseline, allows the primitive types.
   */
  public Builder allowPrimitives()
  {
    return allowType( "void" )
      .allowType( "boolean" )
      .allowType( "byte" )
      .allowType( "char" )
      .allowType( "short" )
      .allowType( "int" )
      .allowType( "long" )
      .allowType( "float" )
      .allowType( "double" );
  }

  /**
   * Denies the named type, with any generic or array type information erased.
   */
  public Builder denyType( String typeName )
  {
    return add( _deny, new Rule( Restrictions.Kind.TYPE, Restrictions.normalize( typeName ), _origin ) );
  }

  /**
   * Denies the named package, not its subpackages.
   */
  public Builder denyPackage( String packageName )
  {
    return add( _deny, new Rule( Restrictions.Kind.PACKAGE, Restrictions.normalize( packageName ), _origin ) );
  }

  /**
   * Denies the named package and all its subpackages.
   */
  public Builder denyPackageTree( String packageName )
  {
    return add( _deny, new Rule( Restrictions.Kind.PACKAGE_TREE, Restrictions.normalize( packageName ), _origin ) );
  }

  /**
   * Denies one method (all overloads) or property. The rule matches a member access where the type is the declaring
   * type, the static type of the caller, or any supertype of either. So a rule on a type also denies the member on
   * all its subtypes, including overrides e.g., {@code java.lang.Object#toString} denies {@code "abc".toString()}.
   * A static member is not overridden, so a rule on a static member matches only its declaring type and the type it
   * is accessed on, e.g., {@code java.lang.Enum#valueOf} does not deny {@code DayOfWeek.valueOf("MONDAY")}.
   * <p>
   * A rule naming an accessor method also denies the corresponding property access: {@code getX}/{@code isX} deny
   * reading property {@code X} and {@code setX} denies writing it. A rule naming the property {@code X} denies both.
   * <p>
   * For an enhancement method or property the declaring type is the <i>enhanced</i> type, not the enhancement type.
   * <p>
   * Use {@code "<init>"} as the {@code memberName} for a constructor rule.
   */
  public Builder denyMember( String declaringType, String memberName )
  {
    return add( _deny, new Rule( Restrictions.Kind.MEMBER, Restrictions.normalize( declaringType ) + "#" + memberName, _origin ) );
  }

  /**
   * String-rule form, see class javadoc. Member rules are not supported as allow rules.
   * <p>
   * Favor more specific {@code allowXxx(...)} methods over this one.
   */
  public Builder allow( String rule )
  {
    Rule r = Rule.parse( rule, _origin );
    if( r._kind == Restrictions.Kind.MEMBER )
    {
      throw new IllegalArgumentException( "Member rules are only supported as deny rules: " + rule );
    }
    return add( _allow, r );
  }

  /**
   * String-rule form, see {#link #denyMember(String, String)} for details concerning member rules.
   * <p>
   * Favor more specific {@code denyXxx(...)} methods over this one.
   */
  public Builder deny( String rule )
  {
    return add( _deny, Rule.parse( rule, _origin ) );
  }

  /**
   * Explicit escape hatch: removes one baseline deny rule (by its rule text, e.g.
   * {@code "java.lang.Thread"} or {@code "java.io.**"}). Host-added deny rules cannot be removed
   * this way, and a plain allow never overrides a deny.
   */
  public Builder withoutBaselineDeny( String rule )
  {
    Rule parsed = Rule.parse( rule, Restrictions.Origin.HOST );
    Rule existing = _deny.get( parsed );
    if( existing == null || existing._origin != Restrictions.Origin.BASELINE )
    {
      throw new IllegalArgumentException( "Not a baseline deny rule: " + rule );
    }
    _deny.remove( existing );
    return this;
  }

  public Restrictions build()
  {
    return new Restrictions( _allow.copy(), _deny.copy(), _features );
  }

  Builder add( RuleSet set, Rule rule )
  {
    if( rule._key.isEmpty() )
    {
      throw new IllegalArgumentException( "Empty restrictions rule" );
    }
    set.add( rule );
    return this;
  }
}
