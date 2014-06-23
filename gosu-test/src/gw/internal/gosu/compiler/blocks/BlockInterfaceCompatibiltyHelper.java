/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler.blocks;

public class BlockInterfaceCompatibiltyHelper<T>
{
  public String callItUnparameterized( IGenericInterface gi )
  {
    return gi.doIt();
  }

  public <Q> String callItFunctionParameterized( IGenericInterface<Q> gi )
  {
    return gi.doIt();
  }

  public String callItClassParameterized( IGenericInterface<T> gi )
  {
    return gi.doIt();
  }

  public String callItIndirectlyUnparameterized( IGenericInterfaceWithParam gi )
  {
    return gi.doIt( new IGenericInterface()
    {
      @Override
      public String doIt()
      {
        return "test";
      }
    } );
  }

}
