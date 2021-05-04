package gw.gosudoc.doc;

import gw.gosudoc.com.sun.javadoc.Doc;

abstract public class GSDocImplShim implements Doc
{
  public boolean isClass()
  {
    return getIsClassShimmed();
  }

  public abstract boolean getIsClassShimmed();
}
