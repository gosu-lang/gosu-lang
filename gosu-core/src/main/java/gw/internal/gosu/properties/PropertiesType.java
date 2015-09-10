/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.properties;

import gw.fs.IFile;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.Modifier;
import gw.lang.reflect.TypeBase;
import gw.lang.reflect.gs.IPropertiesType;
import gw.util.GosuClassUtil;
import gw.util.StreamUtil;
import gw.util.concurrent.LockingLazyVar;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Type based on a {@link PropertyNode}
 */
@SuppressWarnings("serial")
public class PropertiesType extends TypeBase implements IPropertiesType {

  private final PropertiesTypeLoader _typeLoader;
  private final PropertyNode _propertyNode;
  private IFile _file;
  private String _contentCached;
  private final LockingLazyVar<PropertiesTypeInfo> _typeInfo = new LockingLazyVar<PropertiesTypeInfo>() {
    @Override
    protected PropertiesTypeInfo init() {
      return new PropertiesTypeInfo(PropertiesType.this);
    }
  };
  private final LockingLazyVar<List<IPropertiesType>> _innerClasses = new LockingLazyVar<List<IPropertiesType>>() {
    @Override
    protected List<IPropertiesType> init() {
      List<IPropertiesType> innerClasses = new ArrayList<IPropertiesType>();
      for( IPropertyInfo pi: getTypeInfo().getProperties() )
      {
        IType type = pi.getFeatureType();
        if( type instanceof IPropertiesType )
        {
          innerClasses.add( (IPropertiesType)type );
        }
      }
      return innerClasses;
    }
  };

  public PropertiesType(PropertiesTypeLoader typeLoader, PropertyNode propertyNode, IFile file) {
    _typeLoader = typeLoader;
    _propertyNode = propertyNode;
    _file = file;
  }

  @Override
  public IType[] getInterfaces() {
    return EMPTY_TYPE_ARRAY;
  }

  @Override
  public int getModifiers()
  {
    return Modifier.PUBLIC | (!_propertyNode.isRoot() ? Modifier.STATIC : 0);
  }

  @Override
  public String getName() {
    return _propertyNode.getTypeName();  
  }

  @Override
  public String getNamespace() {
    return GosuClassUtil.getPackage( getName() );
  }

  @Override
  public String getRelativeName() {
    return GosuClassUtil.getShortClassName( getName() );
  }

  @Override
  public IType getSupertype() {
    return null;
  }

  @Override
  public ITypeInfo getTypeInfo() {
    return _typeInfo.get();
  }

  @Override
  public ITypeLoader getTypeLoader() {
    return _typeLoader;
  }

  @Override
  public boolean isMutable() {
    return false;
  }

  PropertyNode getPropertyNode() {
    return _propertyNode;
  }

  @Override
  public String getPropertiesFileKey( IPropertyInfo pi )
  {
    PropertiesPropertyInfo ppi = (PropertiesPropertyInfo)pi;
    return ppi.getPropertyEntryName();
  }

  @Override
  public IFile[] getSourceFiles() {
    if( _file == null ) {
      // _file may be null e.g., for SystemProperties
      return IFile.EMPTY_ARRAY;
    }
    return new IFile[] {_file};
  }

  public int findOffsetOf( PropertyNode node )
  {
    String fqn = node.getPath();
    // this is a crappy way to approximate the offset, we really need to parse the file ourselves and store the offsets
    return getCachedContent().indexOf( fqn );
  }

  public String getCachedContent()
  {
    if( _contentCached == null )
    {
      IFile[] files = getSourceFiles();
      if( files != null && files.length > 0 )
      {
        try
        {
          InputStream inputStream = files[0].openInputStream();
          _contentCached = StreamUtil.getContent( new InputStreamReader( inputStream ) );
          inputStream.close();
        }
        catch( Exception e )
        {
          throw new RuntimeException( e );
        }
      }
    }
    return _contentCached;
  }

  @Override
  public IType getInnerClass( CharSequence strTypeName )
  {
    for( IPropertiesType innerClass: _innerClasses.get() )
    {
      if( innerClass.getRelativeName().equals( strTypeName ) )
      {
        return innerClass;
      }
    }
    return null;
  }

  @Override
  public List<? extends IType> getInnerClasses()
  {
    return _innerClasses.get();
  }

  @Override
  public List<? extends IType> getLoadedInnerClasses()
  {
    return _innerClasses.get();
  }

  @Override
  public IType resolveRelativeInnerClass( String strTypeName, boolean bForce )
  {
    return null;
  }
}
