package gw.lang.parser.restrictions;

final class Rule
{
  final Restrictions.Kind _kind;
  final String _key;
  final Restrictions.Origin _origin;

  Rule( Restrictions.Kind kind, String key, Restrictions.Origin origin )
  {
    _kind = kind;
    _key = key;
    _origin = origin;
  }

  String text()
  {
    switch( _kind )
    {
      case PACKAGE:
        return _key + ".*";
      case PACKAGE_TREE:
        return _key + ".**";
      default:
        return _key;
    }
  }

  static Rule parse( String text, Restrictions.Origin origin )
  {
    if( text == null || text.trim().isEmpty() )
    {
      throw new IllegalArgumentException( "Empty restrictions rule" );
    }
    text = text.trim();
    Rule rule;
    if( text.endsWith( ".**" ) )
    {
      rule = new Rule( Restrictions.Kind.PACKAGE_TREE, text.substring( 0, text.length() - 3 ), origin );
    }
    else if( text.endsWith( ".*" ) )
    {
      rule = new Rule( Restrictions.Kind.PACKAGE, text.substring( 0, text.length() - 2 ), origin );
    }
    else if( text.indexOf( '#' ) >= 0 )
    {
      int hash = text.indexOf( '#' );
      String type = Restrictions.normalize( text.substring( 0, hash ) );
      String member = text.substring( hash + 1 ).trim();
      if( type.isEmpty() || member.isEmpty() )
      {
        throw new IllegalArgumentException( "Malformed member rule: " + text );
      }
      rule = member.equals( "*" )
             ? new Rule( Restrictions.Kind.TYPE, type, origin )
             : new Rule( Restrictions.Kind.MEMBER, type + "#" + member, origin );
    }
    else
    {
      rule = new Rule( Restrictions.Kind.TYPE, Restrictions.normalize( text ), origin );
    }
    if( rule._key.isEmpty() )
    {
      throw new IllegalArgumentException( "Malformed restrictions rule: " + text );
    }
    return rule;
  }
}
