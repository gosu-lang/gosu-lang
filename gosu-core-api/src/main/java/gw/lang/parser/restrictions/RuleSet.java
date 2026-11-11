package gw.lang.parser.restrictions;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

final class RuleSet
{
  private final Map<String, Rule> _types = new LinkedHashMap<String, Rule>();
  private final Map<String, Rule> _packages = new LinkedHashMap<String, Rule>();
  private final Map<String, Rule> _trees = new LinkedHashMap<String, Rule>();
  private final Map<String, Rule> _members = new LinkedHashMap<String, Rule>();

  RuleSet copy()
  {
    RuleSet c = new RuleSet();
    c._types.putAll( _types );
    c._packages.putAll( _packages );
    c._trees.putAll( _trees );
    c._members.putAll( _members );
    return c;
  }

  RuleSet withOrigin( Builder.Origin origin )
  {
    RuleSet c = new RuleSet();
    for( Rule r : rules() )
    {
      if( r._origin == origin )
      {
        c.add( r );
      }
    }
    return c;
  }

  private Map<String, Rule> map( Rule.Kind kind )
  {
    switch( kind )
    {
      case TYPE:
        return _types;
      case PACKAGE:
        return _packages;
      case PACKAGE_TREE:
        return _trees;
      default:
        return _members;
    }
  }

  /**
   * Adds the rule, an existing rule with the same key keeps its original origin
   */
  void add( Rule r )
  {
    Map<String, Rule> m = map( r._kind );
    if( !m.containsKey( r._key ) )
    {
      m.put( r._key, r );
    }
  }

  Rule get( Rule r )
  {
    return map( r._kind ).get( r._key );
  }

  void remove( Rule r )
  {
    map( r._kind ).remove( r._key );
  }

  /**
   * Type rule on the type or any enclosing type, else a package rule on its namespace
   */
  Rule matchType( String namespace, String relativeName )
  {
    String prefix = namespace;
    int start = 0;
    while( start <= relativeName.length() )
    {
      int dot = relativeName.indexOf( '.', start );
      String seg = dot < 0 ? relativeName.substring( start ) : relativeName.substring( start, dot );
      prefix = prefix.isEmpty() ? seg : prefix + "." + seg;
      Rule r = _types.get( prefix );
      if( r != null )
      {
        return r;
      }
      if( dot < 0 )
      {
        break;
      }
      start = dot + 1;
    }
    return matchPackage( namespace );
  }

  Rule matchPackage( String pkg )
  {
    Rule r = _packages.get( pkg );
    return r != null ? r : matchPackageTree( pkg );
  }

  /**
   * Package tree rule on the package or any enclosing package
   */
  Rule matchPackageTree( String pkg )
  {
    String p = pkg;
    while( true )
    {
      Rule r = _trees.get( p );
      if( r != null )
      {
        return r;
      }
      int i = p.lastIndexOf( '.' );
      if( i < 0 )
      {
        return null;
      }
      p = p.substring( 0, i );
    }
  }

  List<Rule> rules()
  {
    List<Rule> rules = new ArrayList<>();
    rules.addAll( _types.values() );
    rules.addAll( _packages.values() );
    rules.addAll( _trees.values() );
    rules.addAll( _members.values() );
    return rules;
  }

  Rule matchMember( String declaringType, String member )
  {
    return _members.get( declaringType + "#" + member );
  }

  /**
   * True if some rule lives at or beneath the package (used to let `uses pkg.*` through)
   */
  boolean hasRuleWithin( String pkg )
  {
    String prefix = pkg + ".";
    for( String k : _types.keySet() )
    {
      if( k.startsWith( prefix ) )
      {
        return true;
      }
    }
    for( String k : _packages.keySet() )
    {
      if( k.equals( pkg ) || k.startsWith( prefix ) )
      {
        return true;
      }
    }
    for( String k : _trees.keySet() )
    {
      if( k.equals( pkg ) || k.startsWith( prefix ) )
      {
        return true;
      }
    }
    return false;
  }
}
