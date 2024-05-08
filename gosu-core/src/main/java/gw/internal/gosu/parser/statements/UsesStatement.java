/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.statements;

import gw.internal.gosu.parser.Statement;
import gw.lang.parser.Keyword;
import gw.lang.parser.statements.ITerminalStatement;
import gw.lang.parser.statements.IUsesStatement;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.module.IModule;
import gw.util.cache.StringPool;

/**
 */
public class UsesStatement extends Statement implements IUsesStatement
{
  private String _strTypeName;
  private boolean _bFeatureSpace;
  private IFeatureInfo _fi;

  public UsesStatement()
  {
  }

  public String getTypeName()
  {
    return _strTypeName;
  }
  public void setTypeName( String strTypeName )
  {
    _strTypeName = StringPool.get( strTypeName );
  }

  public boolean isFeatureSpace()
  {
    return _bFeatureSpace;
  }
  public void setFeatureSpace( boolean bFeatureSpace )
  {
    _bFeatureSpace = bFeatureSpace;
  }

  public IFeatureInfo getFeatureInfo()
  {
    return _fi;
  }
  public void setFeatureInfo( IFeatureInfo fi )
  {
    _fi = fi;
  }

  public Object execute()
  {
    // no-op
    return Statement.VOID_RETURN_VALUE;
  }

  @Override
  protected ITerminalStatement getLeastSignificantTerminalStatement_internal( boolean[] bAbsolute )
  {
    bAbsolute[0] = false;
    return null;
  }

  @Override
  public boolean isNoOp()
  {
    return true;
  }

  @Override
  public String toString()
  {
    return Keyword.KW_uses + " " + getTypeName() + (_bFeatureSpace ? "#" : "");
  }

  public IModule getModule() {
    return getGosuClass().getClassStatement().getModule();
  }
}
