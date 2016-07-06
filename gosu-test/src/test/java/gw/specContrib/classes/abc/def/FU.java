package gw.specContrib.classes.abc.def;

import java.util.Collection;

/**
 */
public abstract class FU<T extends Collection>
{
  protected T _t;

  public FU( T t ) {
    _t = t;
  }

  protected T getOwner() {
    return _t;
  }
}
