/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.reflect.BaseFeatureInfo;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IAttributedFeatureInfo;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IGenericMethodInfo;
import gw.lang.reflect.IScriptabilityModifier;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGenericTypeVariable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 */
public abstract class GosuBaseAttributedFeatureInfo extends BaseFeatureInfo implements IAttributedFeatureInfo
{
  volatile List<IAnnotationInfo> _declaredAnnotations;

  public GosuBaseAttributedFeatureInfo( IFeatureInfo container )
  {
    super( container );
  }

  protected abstract List<IGosuAnnotation> getGosuAnnotations();

  public List<IAnnotationInfo> getDeclaredAnnotations()
  {
    maybeInitAnnotations();
    return _declaredAnnotations;
  }

  private void maybeInitAnnotations()
  {
    if( _declaredAnnotations == null )
    {
      TypeSystem.lock();
      try {
        if( _declaredAnnotations == null )
        {
          List<IGosuAnnotation> rawAnnotations = getGosuAnnotations();
          if (rawAnnotations != null) {
            ArrayList lst = new ArrayList<IAnnotationInfo>();
            for (int i = 0; i < rawAnnotations.size(); i++) {
              IGosuAnnotation gosuAnnotation = rawAnnotations.get(i);
              lst.add(new GosuAnnotationInfo(gosuAnnotation, this, this.getOwnersType()));
            }
            lst.trimToSize();
            _declaredAnnotations = lst;
          } else {
            _declaredAnnotations = Collections.emptyList();
          }
        }
      }
      finally
      {
        TypeSystem.unlock();
      }
    }
  }

  public boolean isDeprecated() {
    return getOwnersType().getTypeInfo().isDeprecated() || super.isDeprecated() || hasDeclaredAnnotation( TypeSystem.get( java.lang.Deprecated.class ) );
  }

  public String getDeprecatedReason()
  {
    String thisDeprecated = super.getDeprecatedReason();
    return thisDeprecated == null ? getOwnersType().getTypeInfo().getDeprecatedReason() : thisDeprecated;
  }

  public boolean isHidden()
  {
    return getOwnersType().getTypeInfo().isHidden() || super.isHidden();
  }

  public boolean isVisible( IScriptabilityModifier constraint )
  {
    return super.isVisible(constraint) && getOwnersType().getTypeInfo().isVisible( constraint );
  }

  public IGosuClassInternal getOwnersType()
  {
    return (IGosuClassInternal)super.getOwnersType();
  }

  public IType getActualTypeInContainer( IFeatureInfo container, IType type )
  {
    IType ownerType = container.getOwnersType();
    if( ownerType.isParameterizedType() )
    {
      TypeVarToTypeMap actualParamByVarName = TypeLord.mapTypeByVarName( ownerType, ownerType );
      if( container instanceof IGenericMethodInfo )
      {
        for( IGenericTypeVariable tv : ((IGenericMethodInfo)container).getTypeVariables() )
        {
          if( actualParamByVarName.isEmpty() )
          {
            actualParamByVarName = new TypeVarToTypeMap();
          }
          actualParamByVarName.put( tv.getTypeVariableDefinition().getType(), tv.getTypeVariableDefinition().getType() );
        }
        type = TypeLord.getActualType( type, actualParamByVarName, true );
      }
    }
    return type;
  }
}
