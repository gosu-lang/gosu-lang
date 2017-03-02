/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.reflect.IModifierInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 */
public class ModifierInfo implements IModifierInfo
{
  private int _iModifiers;
  private List<IGosuAnnotation> _annotations;
  private String _description;

  public ModifierInfo( int iModifiers )
  {
    _iModifiers = iModifiers;
    _annotations = Collections.emptyList();
  }

  public int getModifiers()
  {
    return _iModifiers;
  }

  @Override
  public void syncAnnotations( IModifierInfo from )
  {
    if( from instanceof ModifierInfo )
    {
      ModifierInfo fromAnn = (ModifierInfo)from;
      List<IGosuAnnotation> annotations = fromAnn.getAnnotations();
      if( getAnnotations().size() == annotations.size() )
      {
        setAnnotations( annotations );
      }
    }
  }

  public void setModifiers( int iModifiers )
  {
    _iModifiers = iModifiers;
  }

  public List<IGosuAnnotation> getAnnotations()
  {
    return _annotations;
  }
  
  public void setAnnotations( List<IGosuAnnotation> annotations )
  {
     _annotations = annotations;
  }

  public void addModifiers( int iModifiers )
  {
    _iModifiers |= iModifiers;
  }
  public void removeModifiers( int iModifiers )
  {
    _iModifiers &= ~iModifiers;
  }

  public void addAnnotation( IGosuAnnotation annotation )
  {
    if( _annotations.isEmpty() )
    {
      _annotations = new ArrayList<>( 2 );
    }
    _annotations.add( annotation );
  }

  private void addAnnotations( List<IGosuAnnotation> annotations )
  {
    if( _annotations.isEmpty() )
    {
      _annotations = new ArrayList<>( annotations.size() );
    }
    _annotations.addAll( annotations );
  }

  public void addAll( ModifierInfo modifiers )
  {
    if( modifiers == null )
    {
      return;
    }
    addModifiers( modifiers.getModifiers() );
    addAnnotations( modifiers.getAnnotations() );
  }

  public String getDescription()
  {
    return _description;
  }

  public void setDescription( String fullDescription )
  {
    _description = fullDescription;
  }

  public void update(ModifierInfo modifiers) {
    _annotations = modifiers.getAnnotations();
    _description = modifiers.getDescription();
    _iModifiers = modifiers.getModifiers();
  }
}
