package gw.lang.parser.restrictions;

public enum LanguageFeature
{
  TYPEOF( "typeof expressions" ),
  FEATURE_LITERAL( "feature literals (#method(), #property)" ),
  EVAL( "eval expressions" ),
  DYNAMIC( "dynamic expressions / types" ),
  LAMBDA( "lambda expressions / types" ),
  STRUCTURAL( "structural types" ); // an object of any type, allowed or not, coerces to a structure

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
