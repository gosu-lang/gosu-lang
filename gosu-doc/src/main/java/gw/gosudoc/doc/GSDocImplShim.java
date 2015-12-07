package gw.gosudoc.doc;

import com.sun.javadoc.Doc;

abstract public class GSDocImplShim implements Doc
{
  public boolean isClass()
  {
    return getIsClassShimmed();
  }

  public abstract boolean getIsClassShimmed();
}
