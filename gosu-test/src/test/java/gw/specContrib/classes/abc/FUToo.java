package gw.specContrib.classes.abc;

import gw.specContrib.classes.abc.def.FU;

import java.util.List;

/**
 */
public abstract class FUToo<T extends List> extends FU<T>
{
  public FUToo( T t )
  {
    super( t );
  }
}
