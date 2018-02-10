/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.parser.ITypeUsesMap;
import manifold.api.type.ClassType;
import gw.lang.reflect.gs.ISourceFileHandle;
import gw.lang.reflect.gs.GosuClassTypeLoader;

/**
 */
public class GosuEnhancement extends GosuClass implements IGosuEnhancementInternal
{
  transient private IType _enhancedType;

  public GosuEnhancement( String strNamespace, String strRelativeName, GosuClassTypeLoader classTypeLoader, ISourceFileHandle sourceFile, ITypeUsesMap typeUsesMap )
  {
    super( strNamespace, strRelativeName, classTypeLoader, sourceFile, typeUsesMap );
  }

  public GosuEnhancement( IGosuEnhancementInternal gosuEnhancement, IType[] paramTypes )
  {
    super( gosuEnhancement, paramTypes );
    this._enhancedType = gosuEnhancement.getEnhancedType();
  }

  @Override
  public IGosuEnhancementInternal getParameterizedType( IType... paramTypes )
  {
    return (IGosuEnhancementInternal)super.getParameterizedType(paramTypes);
  }

  @Override
  protected IGosuEnhancementInternal makeCopy( IType... paramTypes )
  {
    return (IGosuEnhancementInternal)TypeSystem.getOrCreateTypeReference(
      new GosuEnhancement( (IGosuEnhancementInternal)TypeSystem.getOrCreateTypeReference( this ), paramTypes ) );
  }

  @Override
  public IType getSupertype()
  {
    compileHeaderIfNeeded();
    return null;
  }

  public IType getEnhancedType()
  {
    compileHeaderIfNeeded();

    return _enhancedType;
  }

  public void setEnhancedType( IType enhancedType )
  {
    _enhancedType = enhancedType;
  }

  public void setFoundCorrectHeader()
  {
    boolean bEnhancement = true;
  }

  @Override
  public ClassType getClassType() {
    return ClassType.Enhancement;
  }
}