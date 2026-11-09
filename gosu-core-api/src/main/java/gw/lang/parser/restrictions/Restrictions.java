package gw.lang.parser.restrictions;

import gw.lang.parser.AstProcessor;
import gw.lang.parser.AstProcessorProvider;

import java.util.EnumSet;
import java.util.Set;

/**
 * Compile-time restrictions profile for a Gosu script.
 * <p>
 * A {@code null} restrictions (the default in ParserOptions) means unrestricted.
 * <p>
 * Restrictions are a set of rules that restrict access to types, packages, members, and language features.
 * There are two kinds of rules: <i>deny</i> rules and <i>allow</i> rules. Access is allowed only with <i>allow</i> rules
 * and <i>deny</i> rules trump allow rules: if no rules match a check for access, access is <i>not allowed</i>*.
 * <p>
 * Example usage:
 *
 * <pre><code>
 * Restrictions r = Restrictions.baseline()
 *     .allowType("java.util.concurrent.atomic.AtomicInteger")
 *     .allowPackageTree("org.example.api")
 *     .denyPackage("org.example.api.internal")
 *     .denyMember("org.example.api.Account", "password")
 *     .build();
 * options.withRestrictions(r);
 * </code></pre>
 * <p>
 * Rule syntax (used by {@code allow(String)} / {@code deny(String)})
 * <ul>
 *   <li>{@code "org.example.Foo"}           type and all its inner types</li>
 *   <li>{@code "org.example.*"}             all types in package</li>
 *   <li>{@code "org.example.**"}            all types in package and all its subpackages</li>
 *   <li>{@code "org.example.Foo#myMethod"}  member and all overloads; for use with {@code deny(String)} only</li>
 *   <li>{@code "org.example.Foo#<init>"}    all constructors; for use with {@code deny(String)} only</li>
 * </ul>
 *
 * <p>
 * Rules are based on erased type names: no generic arguments, no array brackets.
 * {@code '$'} is treated as {@code '.'} for nested types. When only a single qualified string
 * is available, the package/type boundary is inferred by the Java convention that the first
 * segment starting with a non-lowercase character begins the type name. Use the {@link #checkType(String, String)}
 * overload when the package is known.
 */
public final class Restrictions implements AstProcessorProvider
{
  private final RuleSet _allow;
  private final RuleSet _deny;
  private final Set<LanguageFeature> _enabledFeatures;

  enum Origin {BASELINE, HOST}

  enum Kind {TYPE, PACKAGE, PACKAGE_TREE, MEMBER}


  Restrictions( RuleSet allow, RuleSet deny, Set<LanguageFeature> enabledFeatures )
  {
    _allow = allow;
    _deny = deny;
    _enabledFeatures =
      EnumSet.copyOf( enabledFeatures.isEmpty()
                      ? EnumSet.noneOf( LanguageFeature.class )
                      : enabledFeatures );
  }

  @Override
  public AstProcessor getAstProcessor()
  {
    return new RestrictionsChecker( this );
  }

  /**
   * Conservative baseline whitelist with the baseline blacklist.
   * Extend it using the builder.
   */
  public static Builder baseline()
  {
    Builder b = new Builder( Origin.BASELINE, new RuleSet(), new RuleSet(), EnumSet.noneOf( LanguageFeature.class ) );

    // whitelist:
    // (the blacklist below removes the dangerous bits out of this list)
    // (this is a very conservative list, use the builder to extend it as needed)
    b.allowPrimitives()
      .allowPackage( "java.lang" )
      .allowPackage( "java.math" )
      .allowPackage( "java.util" )
      .allowPackage( "java.util.function" )
      .allowPackage( "java.util.stream" )
      .allowPackage( "java.util.regex" )
      .allowPackage( "java.text" )
      .allowPackageTree( "java.time" )
      .allowPackage( "gw.lang.function" );

    // blacklist: packages
    // java.io, java.nio, etc. are denied wholesale. A host needing, say, java.io.Serializable must
    // use withoutBaselineDeny("java.io.**") and then allowType("java.io.Serializable").
    for( String p : new String[]{
      "java.lang.reflect", "java.lang.invoke", "java.lang.instrument", "java.lang.management",
      "java.lang.module", "java.lang.annotation", "java.lang.foreign", "java.lang.ref",
      "java.lang.constant",
      "java.io", "java.nio", "java.net", "java.rmi", "java.sql", "java.security",
      "java.beans", "javax.script", "javax.tools", "javax.naming", "javax.management",
      "sun", "jdk.internal", "com.sun",
      "gw.lang.reflect", "gw.lang.parser", "gw.internal"} )
    {
      b.denyPackageTree( p );
    }

    // blacklist: types
    for( String t : new String[]{
      "java.lang.Class", "java.lang.ClassLoader", "java.lang.Module", "java.lang.ModuleLayer",
      "java.lang.Package", "java.lang.Runtime", "java.lang.Process", "java.lang.ProcessBuilder",
      "java.lang.ProcessHandle", "java.lang.System", "java.lang.Thread", "java.lang.ThreadGroup",
      "java.lang.ThreadLocal", "java.lang.InheritableThreadLocal", "java.lang.StackWalker",
      "java.lang.StackTraceElement", "java.lang.SecurityManager", "java.lang.Compiler",
      "java.util.ServiceLoader", "java.util.ResourceBundle", "java.util.Properties",
      "java.util.Scanner", "java.util.Timer", "java.util.TimerTask",
      "java.util.Formatter", // Formatter(String fileName) creates/truncates a file
      "java.time.zone.ZoneRulesProvider", // JVM-global registry
      "dynamic.Dynamic",} )
    {
      b.denyType( t );
    }

    // blacklist: members on otherwise whitelisted types
    b.denyMember( "java.lang.Object", "getClass" )
      .denyMember( "java.lang.Object", "wait" )
      .denyMember( "java.lang.Object", "notify" )
      .denyMember( "java.lang.Object", "notifyAll" )
      .denyMember( "java.lang.Enum", "getDeclaringClass" )
      .denyMember( "java.lang.Enum", "valueOf" )
      .denyMember( "java.lang.Throwable", "getStackTrace" )
      .denyMember( "java.lang.Throwable", "setStackTrace" )
      .denyMember( "java.lang.Throwable", "printStackTrace" )
      .denyMember( "java.lang.Throwable", "StackTraceAsString" )
      .denyMember( "java.util.Map", "writeToPropertiesFile" )
      .denyMember( "java.util.Map", "readFromPropertiesFile" )
      // system properties
      .denyMember( "java.lang.Boolean", "getBoolean" )
      .denyMember( "java.lang.Integer", "getInteger" )
      .denyMember( "java.lang.Long", "getLong" )
      // JVM-global mutable state
      .denyMember( "java.util.Locale", "setDefault" )
      .denyMember( "java.util.TimeZone", "setDefault" );

    b._origin = Origin.HOST;
    return b;
  }

  public Builder toBuilder()
  {
    return new Builder( _allow.copy(), _deny.copy(), _enabledFeatures );
  }

  /**
   * No baseline: for hosts that want complete control.
   * <p>
   * Caution! Enabling a language feature compromises the safety guarantees of nominal type checking. Types and packages
   * that are restricted may be dynamically accessible through the enabled features.
   */
  public static Builder empty( LanguageFeature... features )
  {
    return new Builder( new RuleSet(), new RuleSet(), Set.of( features ) );
  }

  public Result checkType( String qualifiedTypeName )
  {
    String[] parts = splitName( normalize( qualifiedTypeName ) );
    return checkType( parts[0], parts[1] );
  }

  public Result checkType( String namespace, String relativeName )
  {
    String ns = normalize( namespace );
    String rel = normalize( relativeName );
    String full = ns.isEmpty() ? rel : ns + "." + rel;
    Rule d = _deny.matchType( ns, rel );
    if( d != null )
    {
      return Result.denied( "Type", full, d );
    }
    Rule a = _allow.matchType( ns, rel );
    return a == null ? Result.notAllowed( "Type", full ) : Result.allowed( a );
  }

  public Result checkPackage( String packageName )
  {
    String pkg = normalize( packageName );
    Rule d = _deny.matchPackage( pkg );
    if( d != null )
    {
      return Result.denied( "Package", pkg, d );
    }

    Rule a = _allow.matchPackage( pkg );
    if( a != null )
    {
      return Result.allowed( a );
    }
    // A package that merely contains individually allowed types is still importable.
    return _allow.hasRuleWithin( pkg ) ? Result.ALLOWED : Result.notAllowed( "Package", pkg );
  }

  /**
   * Deny-only check of a member against its *declaring* type. The declaring type must be allowed.
   */
  public Result checkMember( String declaringType, String memberName )
  {
    String type = normalize( declaringType );
    Result result = checkType( type );
    if( !result.isAllowed() )
    {
      return result;
    }
    return checkMemberOnly( type, memberName );
  }

  /**
   * Deny-only check of a member, without checking whether the declaring type is allowed.
   */
  public Result checkMemberOnly( String declaringType, String memberName )
  {
    String type = normalize( declaringType );
    Rule d = _deny.matchMember( type, memberName );
    return d != null ? Result.denied( "Member", type + "#" + memberName, d ) : Result.ALLOWED;
  }

  public Result checkFeature( LanguageFeature feature )
  {
    return _enabledFeatures.contains( feature ) ? Result.ALLOWED : Result.featureDenied( feature );
  }

  /**
   * Strips generics, array suffixes, leading/trailing whitespace, and converts '$' to '.'
   */
  static String normalize( String typeName )
  {
    if( typeName == null )
    {
      return "";
    }
    int lt = typeName.indexOf( '<' );
    if( lt >= 0 )
    {
      typeName = typeName.substring( 0, lt );
    }
    typeName = typeName.trim();
    while( typeName.endsWith( "[]" ) )
    {
      typeName = typeName.substring( 0, typeName.length() - 2 );
    }
    return typeName.replace( '$', '.' );
  }

  /**
   * Splits a qualified name into {namespace, relativeTypeName} using the capitalization convention
   */
  static String[] splitName( String name )
  {
    int segStart = 0;
    while( true )
    {
      int dot = name.indexOf( '.', segStart );
      boolean isType = segStart >= name.length() || !Character.isLowerCase( name.charAt( segStart ) );
      if( isType )
      {
        return new String[]{segStart == 0 ? "" : name.substring( 0, segStart - 1 ), name.substring( segStart )};
      }
      if( dot < 0 )
      {
        // All lowercase: treat the last segment as the type.
        int last = name.lastIndexOf( '.' );
        return last < 0 ? new String[]{"", name} : new String[]{name.substring( 0, last ), name.substring( last + 1 )};
      }
      segStart = dot + 1;
    }
  }
}
