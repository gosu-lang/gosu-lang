/*
 *
 *  Copyright 2010 Guidewire Software, Inc.
 *
 */
package editor;

import gw.lang.reflect.IBlockType;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IType;


/**
 */
public class BeanInfoNode implements Comparable<BeanInfoNode>
{
  protected static final int PRIMARY = 0;
  protected static final int SECONDARY = 1;

  private IType _type;
  private String _strDisplayName;

  public BeanInfoNode( IType type, String strDisplayName )
  {
    _type = type;
    _strDisplayName = strDisplayName;
  }

  public BeanInfoNode( IType type )
  {
    _type = type;
  }

  public BeanInfoNode( String strDisplayName )
  {
    _strDisplayName = strDisplayName;
  }

  public IType getType()
  {
    return _type;
  }

  public String getDisplayName()
  {
    return _strDisplayName;
  }

  public void setDisplayName( String strDisplayName )
  {
    _strDisplayName = strDisplayName;
  }

  public String getName()
  {
    return getDisplayName();
  }

  public String getValue()
  {
    return null;
  }

  public void setValue( String strValue )
  {
    throw new RuntimeException( "not implemented" );
  }

  public IFeatureInfo getFeatureInfo()
  {
    return null;
  }

  public static String getTypeName( IType type )
  {
    String strType;
    if( type.isArray() )
    {
      strType = getTypeName( type.getComponentType() ) + "[]";
    }
    else
    {
      strType = type.getRelativeName();
    }

    return strType;
  }

  public String getPathComponent( boolean bFeatureLiteralCompletion )
  {
    if( _type instanceof IFunctionType && !(_type instanceof IBlockType) )
    {
      return editor.util.EditorUtilities.buildFunctionIntellisenseString( bFeatureLiteralCompletion, (IFunctionType)_type );
    }
    //Need to replace '$' with '.' for inner classes.
    if( _type instanceof TypeInPackageType )
    {
      return getName().replaceAll( "\\$", "." );
    }
    return getName();
  }

  protected int getTypePriority()
  {
    return PRIMARY;
  }

  @Override
  public int compareTo( BeanInfoNode o )
  {
    if( getTypePriority() != o.getTypePriority() )
    {
      return getTypePriority() > o.getTypePriority() ? 1 : -1;
    }
    String myDisplayName = getDisplayName();
    String otherDisplayName = o.getDisplayName();
    int result = Boolean.valueOf( myDisplayName.startsWith( "$" ) ).compareTo( otherDisplayName.startsWith( "$" ) );
    if( result == 0 )
    {
      result = myDisplayName.compareToIgnoreCase( otherDisplayName );
    }
    return result;
  }
}