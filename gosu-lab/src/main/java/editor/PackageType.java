package editor;

import gw.lang.reflect.IMetaType;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.Modifier;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.util.GosuStringUtil;

import java.io.ObjectStreamException;
import java.util.Collections;
import java.util.Set;

/**
 */
public class PackageType implements IType
{
  private String _strFullPackage;
  private String _strRelativePackage;
  private String _strParent;
  private PackageTypeInfo _typeInfo;
  private boolean _bDiscarded;

  public PackageType( String strPackage )
  {
    _strFullPackage = strPackage;
    assignRelativePackageAndParent();
  }

  private void assignRelativePackageAndParent()
  {
    int iLastDot = _strFullPackage.lastIndexOf( '.' );
    if( iLastDot >= 0 )
    {
      _strRelativePackage = _strFullPackage.substring( iLastDot + 1 );
      _strParent = _strFullPackage.substring( 0, iLastDot );
    }
    else
    {
      _strRelativePackage = _strFullPackage;
      _strParent = "";
    }
  }

  @Override
  public String getName()
  {
    return _strFullPackage;
  }

  @Override
  public String getDisplayName()
  {
    return getName();
  }

  @Override
  public String getRelativeName()
  {
    return _strRelativePackage;
  }

  @Override
  public String getNamespace()
  {
    return _strParent;
  }

  @Override
  public ITypeLoader getTypeLoader()
  {
    return null;
  }

  @Override
  public boolean isInterface()
  {
    return false;
  }

  @Override
  public boolean isEnum()
  {
    return false;
  }

  @Override
  public IType[] getInterfaces()
  {
    return IType.EMPTY_ARRAY;
  }

  @Override
  public PackageType getSupertype()
  {
    return getEnclosingType();
  }

  @Override
  public PackageType getEnclosingType()
  {
    if( GosuStringUtil.isEmpty( _strParent ) )
    {
      return null;
    }

    return PackageTypeLoader.instance().getType( _strParent );
  }

  @Override
  public IType getGenericType()
  {
    return null;
  }

  @Override
  public boolean isFinal()
  {
    return true;
  }

  @Override
  public boolean isParameterizedType()
  {
    return false;
  }

  @Override
  public boolean isGenericType()
  {
    return false;
  }

  private static final IGenericTypeVariable[] EMPTY_TYPEVARS = new IGenericTypeVariable[0];

  @Override
  public IGenericTypeVariable[] getGenericTypeVariables()
  {
    return EMPTY_TYPEVARS;
  }

  @Override
  public IType getParameterizedType( IType... ofType )
  {
    return null;
  }

  @Override
  public IType[] getTypeParameters()
  {
    return new IType[0];
  }

  @Override
  public Set<? extends IType> getAllTypesInHierarchy()
  {
    return Collections.singleton( this );
  }

  @Override
  public boolean isArray()
  {
    return false;
  }

  @Override
  public boolean isPrimitive()
  {
    return false;
  }

  @Override
  public IType getArrayType()
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public Object makeArrayInstance( int iLength )
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public Object getArrayComponent( Object array, int iIndex ) throws IllegalArgumentException, ArrayIndexOutOfBoundsException
  {
    return null;
  }

  @Override
  public void setArrayComponent( Object array, int iIndex, Object value ) throws IllegalArgumentException, ArrayIndexOutOfBoundsException
  {
  }

  @Override
  public int getArrayLength( Object array ) throws IllegalArgumentException
  {
    return 0;
  }

  @Override
  public IType getComponentType()
  {
    return null;
  }

  @Override
  public boolean isAssignableFrom( IType type )
  {
    return type == this;
  }

  @Override
  public boolean isMutable()
  {
    return false;
  }

  @Override
  public ITypeInfo getTypeInfo()
  {
    if( _typeInfo == null )
    {
      _typeInfo = new PackageTypeInfo( this );
    }
    return _typeInfo;
  }

  @Override
  public void unloadTypeInfo()
  {
    _typeInfo = null;
  }

  @Override
  public Object readResolve() throws ObjectStreamException
  {
    return PackageTypeLoader.instance().getType( _strFullPackage );
  }

  @Override
  public boolean isValid()
  {
    return true;
  }

  @Override
  public int getModifiers()
  {
    return Modifier.PUBLIC;
  }

  @Override
  public boolean isAbstract()
  {
    return false;
  }

  @Override
  public boolean isDiscarded()
  {
    return _bDiscarded;
  }

  @Override
  public void setDiscarded( boolean bDiscarded )
  {
    _bDiscarded = bDiscarded;
  }

  @Override
  public boolean isCompoundType()
  {
    return false;
  }

  @Override
  public Set<IType> getCompoundTypeComponents()
  {
    return null;
  }

  @Override
  public IMetaType getMetaType()
  {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public IMetaType getLiteralMetaType()
  {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

}
