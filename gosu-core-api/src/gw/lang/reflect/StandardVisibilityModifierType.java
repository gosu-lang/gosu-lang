/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import java.io.ObjectStreamException;

public class StandardVisibilityModifierType implements IVisibilityModifierType
{
  private String _strName;
  private boolean _bConstraint;

  public StandardVisibilityModifierType( String strName, boolean bConstraint )
  {
    _strName = strName;
    _bConstraint = bConstraint;
    ScriptabilityModifierTypes.putType(strName, this);
  }

  public String getName()
  {
    return _strName;
  }

  public String getDisplayName()
  {
    return getName();
  }

  public boolean isConstraint()
  {
    return _bConstraint;
  }

  public Object readResolve() throws ObjectStreamException
  {
    return ScriptabilityModifierTypes.getType(_strName);
  }
}
