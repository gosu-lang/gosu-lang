package editor;

import gw.fs.IFile;
import gw.lang.reflect.IMetaType;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.Modifier;
import gw.lang.reflect.gs.IGenericTypeVariable;

import java.io.ObjectStreamException;
import java.util.Collections;
import java.util.Set;

/**
 */
public class TypeInPackageType implements IType
{
  private String _strFqName;
  private String _strRelativeName;
  private String _strParent;
  private TypeInPackageTypeInfo _typeInfo;
  private boolean _bDiscarded;

  public TypeInPackageType( String strPackage )
  {
    _strFqName = strPackage;
    assignRelativePackageAndParent();
  }

  private void assignRelativePackageAndParent()
  {
    int iLastDot = _strFqName.lastIndexOf( '.' );
    if( iLastDot >= 0 )
    {
      _strRelativeName = _strFqName.substring( iLastDot + 1 );
      _strParent = _strFqName.substring( 0, iLastDot );
    }
    else
    {
      _strRelativeName = _strFqName;
      _strParent = "";
    }
  }

  public String getName()
  {
    return _strFqName;
  }

  public String getDisplayName()
  {
    return getName();
  }

  public String getRelativeName()
  {
    return _strRelativeName;
  }

  public String getNamespace()
  {
    return _strParent;
  }

  public ITypeLoader getTypeLoader()
  {
    return null;
  }

  public boolean isInterface()
  {
    return false;
  }

  public boolean isEnum()
  {
    return false;
  }

  public IType[] getInterfaces()
  {
    return IType.EMPTY_ARRAY;
  }

  public IType getSupertype()
  {
    return getEnclosingType();
  }

  public PackageType getEnclosingType()
  {
    if( _strParent == null )
    {
      return null;
    }

    return PackageTypeLoader.instance().getType( _strParent );
  }

  public IType getGenericType()
  {
    return null;
  }

  public boolean isFinal()
  {
    return true;
  }

  public boolean isParameterizedType()
  {
    return false;
  }

  public boolean isGenericType()
  {
    return false;
  }

  public IGenericTypeVariable[] getGenericTypeVariables()
  {
    return new IGenericTypeVariable[0];
  }

  public IType getParameterizedType( IType... ofType )
  {
    return null;
  }

  public IType[] getTypeParameters()
  {
    return new IType[0];
  }

  public Set<? extends IType> getAllTypesInHierarchy()
  {
    return Collections.singleton( this );
  }

  public boolean isArray()
  {
    return false;
  }

  public boolean isPrimitive()
  {
    return false;
  }

  public IType getArrayType()
  {
    throw new UnsupportedOperationException();
  }

  public Object makeArrayInstance( int iLength )
  {
    throw new UnsupportedOperationException();
  }

  public Object getArrayComponent( Object array, int iIndex ) throws IllegalArgumentException, ArrayIndexOutOfBoundsException
  {
    return null;
  }

  public void setArrayComponent( Object array, int iIndex, Object value ) throws IllegalArgumentException, ArrayIndexOutOfBoundsException
  {
  }

  public int getArrayLength( Object array ) throws IllegalArgumentException
  {
    return 0;
  }

  public IType getComponentType()
  {
    return null;
  }

  public boolean isAssignableFrom( IType type )
  {
    return type == this;
  }

  public boolean isMutable()
  {
    return false;
  }

  public ITypeInfo getTypeInfo()
  {
    if( _typeInfo == null )
    {
      _typeInfo = new TypeInPackageTypeInfo( this );
    }
    return _typeInfo;
  }

  public void unloadTypeInfo()
  {
    _typeInfo = null;
  }

  public Object readResolve() throws ObjectStreamException
  {
    return TypeInPackageTypeLoader.instance().getType( _strFqName );
  }

  public boolean isValid()
  {
    return true;
  }

  public int getModifiers()
  {
    return Modifier.PUBLIC;
  }

  public boolean isAbstract()
  {
    return false;
  }

  public boolean isDiscarded()
  {
    return _bDiscarded;
  }

  public void setDiscarded( boolean bDiscarded )
  {
    _bDiscarded = bDiscarded;
  }

  public boolean isCompoundType()
  {
    return false;
  }

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

  @Override
  public IFile[] getSourceFiles()
  {
    return IFile.EMPTY_ARRAY;
  }

}
