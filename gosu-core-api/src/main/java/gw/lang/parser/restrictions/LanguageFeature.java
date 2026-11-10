package gw.lang.parser.restrictions;

/**
 * Dynamic / reflective language features that should be disabled for restricted scripts.
 */
public enum LanguageFeature
{
  TYPEOF( "typeof expressions" ),
  FEATURE_LITERAL( "feature literals (#method(), #property)" ),
  EVAL( "eval expressions" ),
  DYNAMIC( "dynamic expressions / types" ),
  STRUCTURAL( "structural types" );

  private final String _displayName;

  LanguageFeature( String displayName )
  {
    _displayName = displayName;
  }

  public String getDisplayName()
  {
    return _displayName;
  }
}
