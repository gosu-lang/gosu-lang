package gw.lang.parser;

/**
 */
public enum AnnotationUseSiteTarget
{
  /** the backing field for a property */
  field( Keyword.KW_field, true, true ),

  /** the property accessor methods, both get and set if applicable */
  accessors( Keyword.KW_accessors, true, true ),

  /** the property get accessor */
  get( Keyword.KW_get, true, true ),

  /** the property set accessor */
  set( Keyword.KW_set, true, true ),

  /** the property set method's parameter */
  param( Keyword.KW_param, false, false ),

  /** the hidden "this" parameter in enhancement method */
  receiver( Keyword.KW_receiver, false, false );

  private final Keyword _keyword;
  private final boolean _bAccessModifierOk;
  private final boolean _bFinalModifierOk;

  private AnnotationUseSiteTarget( Keyword keyword, boolean bAccessModifierOk, boolean bFinalModifierOk )
  {
    _keyword = keyword;
    _bAccessModifierOk = bAccessModifierOk;
    _bFinalModifierOk = bFinalModifierOk;
  }

  public Keyword getKeyword()
  {
    return _keyword;
  }

  public boolean isAccessModifierOk()
  {
    return _bAccessModifierOk;
  }

  public boolean isFinalModifierOk()
  {
    return _bFinalModifierOk;
  }
}
