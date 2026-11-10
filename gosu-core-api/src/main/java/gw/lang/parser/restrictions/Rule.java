package gw.lang.parser.restrictions;

final class Rule
{
  final Kind _kind;
  final String _key;
  final Builder.Origin _origin;

  enum Kind {TYPE, PACKAGE, PACKAGE_TREE, MEMBER}

  Rule( Kind kind, String key, Builder.Origin origin )
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

  static Rule parse( String text, Builder.Origin origin )
  {
    if( text == null || text.trim().isEmpty() )
    {
      throw new IllegalArgumentException( "Empty restrictions rule" );
    }
    text = text.trim();
    Rule rule;
    if( text.endsWith( ".**" ) )
    {
      rule = new Rule( Kind.PACKAGE_TREE, text.substring( 0, text.length() - 3 ), origin );
    }
    else if( text.endsWith( ".*" ) )
    {
      rule = new Rule( Kind.PACKAGE, text.substring( 0, text.length() - 2 ), origin );
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
             ? new Rule( Kind.TYPE, type, origin )
             : new Rule( Kind.MEMBER, type + "#" + member, origin );
    }
    else
    {
      rule = new Rule( Kind.TYPE, Restrictions.normalize( text ), origin );
    }
    if( rule._key.isEmpty() )
    {
      throw new IllegalArgumentException( "Malformed restrictions rule: " + text );
    }
    return rule;
  }
}
