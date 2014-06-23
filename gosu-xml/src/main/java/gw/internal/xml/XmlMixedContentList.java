/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml;

import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.gs.IGosuObject;
import gw.lang.reflect.java.JavaTypes;
import gw.util.Pair;
import gw.xml.IXmlMixedContent;
import gw.xml.XmlElement;
import gw.xml.XmlTypeInstance;

import javax.xml.namespace.QName;
import java.util.*;

public class XmlMixedContentList extends ArrayList<IXmlMixedContent> {

  private LinkedHashMap<QName,List<Pair<XmlElement,Integer>>> _elementsByQName;
  private int _checksum;
  private XmlTypeInstance _typeInstance;

  public XmlMixedContentList( XmlTypeInstance typeInstance ) {
    super( 0 );
    _typeInstance = typeInstance;
  }

  public List<XmlElement> getAllElements() {
    return new ElementList( null );
  }

  public List<XmlElement> getElementsByQName( QName qname ) {
    maybeIndexByQName();
    return new ElementList( qname );
  }

  public List<XmlElement> getElementsBySubstitutionGroup( IType type ) {
    return new SubstitutionGroupList( type );
  }

  public List<XmlElement> removeElementsBySubstitutionGroup( IType type ) {
    List<XmlElement> children = new ArrayList<XmlElement>();
    Iterator<IXmlMixedContent> it = XmlMixedContentList.this.iterator();
    while ( it.hasNext() ) {
      IXmlMixedContent content = it.next();
      if ( content instanceof XmlElement ) {
        XmlElement element = (XmlElement) content;
        if ( type.isAssignableFrom( element.getIntrinsicType() ) ) {
          children.add( element );
          it.remove();
        }
      }
    }
    return children;
  }

  private void maybeIndexByQName() {
    if ( _elementsByQName == null ) {
      _elementsByQName = new LinkedHashMap<QName, List<Pair<XmlElement, Integer>>>( 1 );
      for ( int i = 0; i < size(); i++ ) {
        IXmlMixedContent child = get(i);
        if ( child instanceof XmlElement ) {
          XmlElement element = (XmlElement) child;
          addIndexedElementByQName( element, element.getQName(), i );
          addIndexedElementByQName( element, null, i ); // all elements are added to the "null" qname list
        }
      }
    }
  }

  private void addIndexedElementByQName( XmlElement element, QName qname, int idx ) {
    if ( element == null ) {
      throw new IllegalArgumentException( "Null element cannot be added to content list" );
    }
    maybeIndexByQName();
    List<Pair<XmlElement, Integer>> elementList = _elementsByQName.get( qname );
    if ( elementList == null ) {
      elementList = new ArrayList<Pair<XmlElement, Integer>>( 1 );
      _elementsByQName.put( qname, elementList );
    }
    elementList.add( new Pair<XmlElement, Integer>( element, idx ) );
  }

  @Override
  public IXmlMixedContent set( int index, IXmlMixedContent content ) {
    if ( content == null ) {
      throw new IllegalArgumentException( "Null content cannot be added to content list" );
    }
    XmlTypeInstanceInternals.instance().clearSimpleValue( _typeInstance );
    clearCaches();
    return super.set( index, content );
  }

  private void clearCaches() {
    _elementsByQName = null;
    _checksum++;
  }

  @Override
  public boolean add( IXmlMixedContent content ) {
    if ( content == null ) {
      throw new IllegalArgumentException( "Null content cannot be added to content list" );
    }
    XmlTypeInstanceInternals.instance().clearSimpleValue( _typeInstance );
    clearCaches();
    return super.add( content );
  }

  @Override
  public void add( int index, IXmlMixedContent content ) {
    if ( content == null ) {
      throw new IllegalArgumentException( "Null content cannot be added to content list" );
    }
    XmlTypeInstanceInternals.instance().clearSimpleValue( _typeInstance );
    clearCaches();
    super.add( index, content );
  }

  @Override
  public IXmlMixedContent remove( int  index ) {
    clearCaches();
    return super.remove(index);
  }

  @Override
  public boolean remove( Object o ) {
    clearCaches();
    return super.remove(o);
  }

  @Override
  public void clear() {
    clearCaches();
    super.clear();
  }

  @Override
  public boolean addAll( Collection<? extends IXmlMixedContent> c ) {
    for ( IXmlMixedContent content : c ) {
      if ( content == null ) {
        throw new IllegalArgumentException( "Null element cannot be added to content list" );
      }
    }
    XmlTypeInstanceInternals.instance().clearSimpleValue( _typeInstance );
    clearCaches();
    return super.addAll(c);
  }

  @Override
  public boolean addAll( int index, Collection<? extends IXmlMixedContent> c ) {
    for ( IXmlMixedContent content : c ) {
      if ( content == null ) {
        throw new IllegalArgumentException( "Null content cannot be added to content list" );
      }
    }
    XmlTypeInstanceInternals.instance().clearSimpleValue( _typeInstance );
    clearCaches();
    return super.addAll(index, c);
  }

  @Override
  protected void removeRange(int fromIndex, int toIndex) {
    clearCaches();
    super.removeRange( fromIndex, toIndex );
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    clearCaches();
    return super.removeAll(c);
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    clearCaches();
    return super.retainAll( c );
  }

  private class ElementList extends AbstractList<XmlElement> implements IGosuObject {

    private final QName _qname;

    public ElementList( QName qname ) {
      _qname = qname;
    }

    @Override
    public XmlElement get( int index ) {
      if ( XmlMixedContentList.this.isEmpty() ) {
        throw new IndexOutOfBoundsException();
      }
      maybeIndexByQName();
      List<Pair<XmlElement, Integer>> elementList = _elementsByQName.get( _qname );
      if ( elementList == null ) {
        throw new IndexOutOfBoundsException();
      }
      else {
        return elementList.get( index ).getFirst();
      }
    }

    @Override
    public int size() {
      if ( XmlMixedContentList.this.isEmpty() ) {
        return 0;
      }
      maybeIndexByQName();
      List<Pair<XmlElement, Integer>> elementList = _elementsByQName.get( _qname );
      return elementList == null ? 0 : elementList.size();
    }

    @Override
    public boolean add( XmlElement xmlElement ) {
      return XmlMixedContentList.this.add( xmlElement ); // clears cache automatically
    }

    @Override
    public XmlElement remove( int idx ) {
      if ( XmlMixedContentList.this.isEmpty() ) {
        throw new IndexOutOfBoundsException();
      }
      maybeIndexByQName();
      List<Pair<XmlElement, Integer>> elementList = _elementsByQName.get( _qname );
      if ( elementList == null ) {
        return null;
      }
      Pair<XmlElement, Integer> pair = elementList.get( idx );
      XmlElement removed = (XmlElement) XmlMixedContentList.this.remove( (int) pair.getSecond() );
      if ( removed != pair.getFirst() ) {
        throw new IllegalStateException( "Expected elements to be identical in qname-based list and master list" );
      }
      return removed;
    }

    @Override
    public XmlElement set( int index, XmlElement element ) {
      if ( XmlMixedContentList.this.isEmpty() ) {
        throw new IndexOutOfBoundsException();
      }
      maybeIndexByQName();
      List<Pair<XmlElement, Integer>> elementList = _elementsByQName.get( _qname );
      if ( elementList == null ) {
        return null;
      }
      Pair<XmlElement, Integer> pair = elementList.get( index );
      XmlElement removed = (XmlElement) XmlMixedContentList.this.set( pair.getSecond(), element );
      if ( removed != pair.getFirst() ) {
        throw new IllegalStateException( "Expected elements to be identical in qname-based list and master list" );
      }
      return removed;
    }

    @Override
    public void add( int index, XmlElement element ) {
      maybeIndexByQName();
      List<Pair<XmlElement, Integer>> elementList = _elementsByQName.get( _qname );
      int realIndex;
      if ( elementList == null ) {
        realIndex = 0;
      }
      else {
        Pair<XmlElement, Integer> pair = elementList.get( index );
        realIndex = pair.getSecond();
      }
      XmlMixedContentList.this.add( realIndex, element );
    }

    @Override
    public IType getIntrinsicType() {
      return JavaTypes.LIST().getParameterizedType( TypeSystem.get( XmlElement.class ) );
    }
  }

  private class SubstitutionGroupList extends AbstractList<XmlElement> implements IGosuObject {

    private final IType _type;
    private List<XmlElement> _children;
    private List<Integer> _originalIndexes;
    private int _checksum;

    public SubstitutionGroupList( IType type ) {
      _type = type;
      _checksum = XmlMixedContentList.this._checksum - 1; // force indexing
    }

    @Override
    public XmlElement get( int index ) {
      maybeIndex();
      return _children.get( index );
    }

    @Override
    public int size() {
      maybeIndex();
      return _children.size();
    }

    @Override
    public void add( int idx, XmlElement xmlElement ) {
      XmlMixedContentList.this.add( idx, xmlElement ); // clears cache automatically
    }

    @Override
    public boolean add( XmlElement xmlElement ) {
      return XmlMixedContentList.this.add( xmlElement ); // clears cache automatically
    }

    @Override
    public IType getIntrinsicType() {
      return JavaTypes.LIST().getParameterizedType( _type );
    }

    private void maybeIndex() {
      if ( _checksum != XmlMixedContentList.this._checksum ) {
        _children = new ArrayList<XmlElement>();
        _originalIndexes = new ArrayList<Integer>();
        int originalIndex = 0;
        for ( IXmlMixedContent content : XmlMixedContentList.this ) {
          if ( ! ( content instanceof XmlElement ) ) {
            continue;
          }
          XmlElement element = (XmlElement) content;
          if ( _type.isAssignableFrom( element.getIntrinsicType() ) ) {
            _children.add( element );
            _originalIndexes.add( originalIndex );
          }
          originalIndex++;
        }
        _checksum = XmlMixedContentList.this._checksum;
      }
    }

    @Override
    public XmlElement remove( int index ) {
      maybeIndex();
      int originalIndex = _originalIndexes.get( index );
      return (XmlElement) XmlMixedContentList.this.remove( originalIndex ); // clears cache automatically
    }

  }

}
