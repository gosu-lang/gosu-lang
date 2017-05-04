package gw.lang.reflect.gs.gen;

/**
 */
public abstract class SrcStatement<T extends SrcStatement<T>> extends SrcAnnotated<T>
{
  public SrcStatement() {}
  public SrcStatement( SrcStatement owner )
  {
    super( owner );
  }
}
