/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.lang.javadoc.IDocRef;
import gw.lang.javadoc.IParamNode;
import gw.lang.javadoc.JavaHasParams;

import java.util.Collections;
import java.util.List;

public class SimpleParameterInfo extends BaseFeatureInfo implements IParameterInfo
{
  private IType _type;
  private int _parentIndex;

  private IDocRef<IParamNode> _docs = new IDocRef<IParamNode>() {
    @Override
    public IParamNode get() {
      if (getContainer() instanceof JavaHasParams ) {
        return ((JavaHasParams)getContainer()).getDocsForParam(_parentIndex).get();
      }

      return null;
    }
  };

  /**
   */
  public SimpleParameterInfo(IFeatureInfo container, IType type, int parentIndex)
  {
    super( container );
    _parentIndex = parentIndex;
    if (type == null) {
      throw new IllegalArgumentException("Type cannot be null");
    }
    _type = type;
  }

  public String getName()
  {
    return getDocs() != null ? getDocs().getName() : getTypeName( _type );
  }

  private IParamNode getDocs() {
    return _docs.get();
  }

  public List<IAnnotationInfo> getDeclaredAnnotations()
  {
    return Collections.emptyList();
  }

  public String getDisplayName()
  {
    return getName();
  }

  public String getShortDescription()
  {
    return getDocs() != null ? getDocs().getDescription() : getName();
  }

  public String getDescription()
  {
    return getDocs() != null ? getDocs().getDescription() : getName();
  }

  public boolean isStatic()
  {
    return false;
  }

  public IType getFeatureType()
  {
    return _type;
  }

  public static String getTypeName( IType type )
  {
    return type.getRelativeName();
  }
}
