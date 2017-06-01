/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.parser.StandardCoercionManager;
import gw.lang.reflect.AbstractType;
import gw.lang.reflect.ICompoundType;
import gw.lang.reflect.INonLoadableType;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.ITypeLoaderListener;
import gw.lang.reflect.AbstractTypeSystemListener;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.reflect.java.JavaTypes;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

/**
 */
public class CompoundType extends AbstractType implements INonLoadableType, ICompoundType
{
  private static final Map<String,CompoundType> CACHE = new ConcurrentHashMap<String,CompoundType>();
  private static ITypeLoaderListener g_refreshListener;
  
  transient private SortedSet<IType> _types;
  private String _strRelativeName;
  private String _strName;
  private boolean _bInterface;
  transient private ITypeInfo _typeInfo;

  public static IType get( IType... types )
  {
    HashSet<IType> typeSet = new HashSet<IType>();
    typeSet.addAll( Arrays.asList( types ) );
    return get( typeSet );
  }

  public static IType get( Set<IType> types )
  {
    types = removeRedundantTypes( types );
    if( types.size() == 1 )
    {
      return types.iterator().next();
    }

    if( types.contains( JavaTypes.OBJECT() ) )
    {
      // necessary to remove Object to prevent some sill shit from creeping into type info e.g., Collections.max()
      types.remove( JavaTypes.OBJECT() );
      if( types.size() == 1 )
      {
        return types.iterator().next();
      }
    }
    String strName = getNameFrom( types, false );
    CompoundType compoundType = CACHE.get( strName );
    if( compoundType == null )
    {
      listenToTypeSystemRefresh();
      compoundType = new CompoundType( types, strName );
      CACHE.put( strName, compoundType );
    }
    return compoundType;
  }

  private static Set<IType> removeRedundantTypes( Set<IType> types )
  {
    Set<IType> reduced = new HashSet<>();
    outer:
    for( IType t: types )
    {
      if( !(t instanceof ErrorType) )
      {
        for( IType t2 : types )
        {
          if( t != t2 && t.isAssignableFrom( t2 ) || StandardCoercionManager.isStructurallyAssignable( t, t2 ) )
          {
            continue outer;
          }
        }
      }
      reduced.add( t );
    }
    if( types.size() > 0 && reduced.size() == 0 )
    {
      throw new IllegalStateException();
    }
    return reduced;
  }

  private static void listenToTypeSystemRefresh()
  {
    if( g_refreshListener != null )
    {
      return;
    }
    TypeLoaderAccess.instance().addTypeLoaderListenerAsWeakRef(
      g_refreshListener = new AbstractTypeSystemListener()
      {
        @Override
        public void refreshed()
        {
          CACHE.clear();
        }
      } );
  }

  private CompoundType( Set<IType> types, String strName )
  {
    _types = new TreeSet<IType>( TypeComparator.INSTANCE );
    _types.addAll(types);
    _strName = strName;
    _strRelativeName = getNameFrom( types, true );
    _bInterface = areAllTypesInterfaces( types );
  }

  private boolean areAllTypesInterfaces( Set<IType> types ) {
    for( IType type: types ) {
      if( !type.isInterface() ) {
        return false;
      }
    }
    return true;
  }

  private static String getNameFrom( Set<IType> types, boolean bRelative )
  {
    StringBuilder sbName = new StringBuilder();
    List<IType> sorted = new ArrayList<IType>( types );
    Collections.sort( sorted, TypeComparator.INSTANCE );
    for( int i = 0; i < sorted.size(); i++ )
    {
      IType type = sorted.get( i );
      sbName.append( bRelative ? type.getRelativeName() : type.getName() );
      if( i != sorted.size()-1 )
      {
        sbName.append( " & " );
      }
    }
    return sbName.toString();
  }
  private static class TypeComparator implements Comparator<IType>
  {
    private static TypeComparator INSTANCE = new TypeComparator();
    @Override
    public int compare( IType o1, IType o2 )
    {
      return o1.getName().compareTo( o2.getName() );
    }
  }

  public Set<IType> getTypes()
  {
    return _types;
  }

  @Override
  public String getName()
  {
    return _strName;
  }

  @Override
  public String getDisplayName()
  {
    return getName();
  }

  @Override
  public String getRelativeName()
  {
    return _strRelativeName;
  }

  @Override
  public String getNamespace()
  {
    return null;
  }

  @Override
  public ITypeLoader getTypeLoader()
  {
    return null;
  }

  @Override
  public IType getSupertype()
  {
    return null;
  }

  @Override
  public IType getEnclosingType()
  {
    return null;
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
  public boolean isInterface()
  {
    return _bInterface;
  }

  @Override
  public boolean isEnum()
  {
    return false;
  }

  @Override
  public IType[] getInterfaces()
  {
    ArrayList<IType> interfaces = new ArrayList<IType>();
    for (IType type : _types) {
      if (type.isInterface()) {
        interfaces.add(type);
      }
      for (IType anInterface : type.getInterfaces()) {
        if (!interfaces.contains(anInterface)) {
          interfaces.add(type);
        }
      }
    }
    return interfaces.toArray(new IType[interfaces.size()]);
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

  @Override
  public IGenericTypeVariable[] getGenericTypeVariables()
  {
    return new IGenericTypeVariable[0];
  }

  @Override
  public IType getParameterizedType( IType... ofType )
  {
    return null;
  }

  @Override
  public IType[] getTypeParameters()
  {
    return IType.EMPTY_ARRAY;
  }

  @Override
  public Set<? extends IType> getAllTypesInHierarchy()
  {
    Set<IType> allTypes = new HashSet<IType>();
    for (IType iType : _types) {
      allTypes.addAll(iType.getAllTypesInHierarchy());
    }
    allTypes.add( this );
    return allTypes;
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
    return JavaTypes.OBJECT().getArrayType();
  }

  @Override
  public Object makeArrayInstance( int iLength )
  {
    return new Object[iLength];
  }

  @Override
  public Object getArrayComponent( Object array, int iIndex ) throws IllegalArgumentException, ArrayIndexOutOfBoundsException
  {
    return ((Object[])array)[iIndex];
  }

  @Override
  public void setArrayComponent( Object array, int iIndex, Object value ) throws IllegalArgumentException, ArrayIndexOutOfBoundsException
  {
    ((Object[])array)[iIndex] = value;
  }

  @Override
  public int getArrayLength( Object array ) throws IllegalArgumentException
  {
    return ((Object[])array).length;
  }

  @Override
  public IType getComponentType()
  {
    return null;
  }

  @Override
  public boolean isAssignableFrom( IType type )
  {
    // Short-circuit if the types are the same
    if( type == this )
    {
      return true;
    }

    // Short-circuit if the given type is null
    if( type == null )
    {
      return false;
    }

    // Supplied type must be assignable to all composed types
    for( IType t : _types )
    {
      if( !t.isAssignableFrom( type ) )
      {
        return false;
      }
    }
    return true;
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
      _typeInfo = new CompoundTypeInfo( this );
    }
    return _typeInfo;
  }

  @Override
  public void unloadTypeInfo()
  {
    _typeInfo = null;
  }

  private void writeObject( ObjectOutputStream out ) throws IOException
  {
    out.defaultWriteObject();
    out.writeInt( _types.size() );
    for( IType ref : _types )
    {
      out.writeObject( ref );
    }
  }

  private void readObject( ObjectInputStream in ) throws IOException, ClassNotFoundException
  {
    in.defaultReadObject();
    int iSize = in.readInt();
    _types = new TreeSet<IType>( );
    for( int i = 0; i < iSize; i++ )
    {
      _types.add( (IType)in.readObject() );
    }
  }

  @Override
  public Object readResolve() throws ObjectStreamException
  {
    return this;
  }

  @Override
  public boolean isValid()
  {
    for( IType type : _types )
    {
      if( !type.isValid() )
      {
        return false;
      }
    }
    return true;
  }

  @Override
  public int getModifiers()
  {
    return 0;
  }

  @Override
  public boolean isAbstract()
  {
    return true;
  }

  @Override
  public String toString()
  {
    return getName();
  }

  @Override
  public boolean isDiscarded()
  {
    return false;
  }

  @Override
  public void setDiscarded( boolean bDiscarded )
  {
  }

  @Override
  public boolean isCompoundType()
  {
    return true;
  }

  @Override
  public Set<IType> getCompoundTypeComponents()
  {
    return Collections.unmodifiableSet( getTypes() );
  }
  
}
