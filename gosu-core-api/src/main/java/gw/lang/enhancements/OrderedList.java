/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.enhancements;

import static gw.lang.enhancements.OrderedList.Direction.ASCENDING;
import static gw.lang.enhancements.OrderedList.Direction.DESCENDING;
import gw.util.IOrderedList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.AbstractList;

@SuppressWarnings({"UnusedDeclaration"})
public class OrderedList<T> extends AbstractList<T> implements IOrderedList<T>
{
  enum Direction{
    ASCENDING,
    DESCENDING
  }

  private static class Tuple<T> {
    private Direction _direction;
    private IToComparable<T> _block;
    private Comparator _comparator;

    public Tuple( Direction direction, IToComparable<T> block, Comparator comparator ) {
      _direction = direction;
      _block = block;
      _comparator = comparator;
    }

    public Direction getDirection() { return _direction; }
    public IToComparable<T> getBlock() { return _block; }
    public Comparator getComparator() { return _comparator; }
  }

  private List<Tuple<T>> _orderByBlocks = new LinkedList<Tuple<T>>();
  private Iterable<T> _originalValues = null;
  private List<T> _values = null;


  public OrderedList( Iterable<T> values )
  {
    _originalValues = values;
  }

  public void addOrderBy( IToComparable<T> block)
  {
    addOrderBy(block, null);
  }

  public void addOrderBy( IToComparable<T> block, Comparator comparator )
  {
    checkSort();
    checkOrderBy();
    _orderByBlocks.add( new Tuple<T>( ASCENDING, block, comparator ) );
  }

  public void addOrderByDescending( IToComparable<T> block )
  {
    addOrderByDescending( block, null );
  }

  public void addOrderByDescending( IToComparable<T> block, Comparator comparator )
  {
    checkSort();
    checkOrderBy();
    _orderByBlocks.add( new Tuple<T>( DESCENDING, block, comparator ) );
  }

  public OrderedList<T> addThenBy( IToComparable<T>  block )
  {
    return addThenBy( block, null );
  }

  public OrderedList<T> addThenBy( IToComparable<T>  block, Comparator comparator )
  {
    checkSort();
    OrderedList<T> lst = new OrderedList<T>( _originalValues );
    lst._orderByBlocks = new LinkedList<Tuple<T>>( _orderByBlocks );
    lst._orderByBlocks.add( new Tuple<T>( ASCENDING, block, comparator ) );
    return lst;
  }

  public OrderedList<T> addThenByDescending( IToComparable<T> block)
  {
    return addThenByDescending( block, null );
  }

  public OrderedList<T> addThenByDescending( IToComparable<T> block, Comparator comparator )
  {
    checkSort();
    OrderedList<T> lst = new OrderedList<T>( _originalValues );
    lst._orderByBlocks = new LinkedList<Tuple<T>>( _orderByBlocks );
    lst._orderByBlocks.add( new Tuple<T>( DESCENDING, block, comparator ) );
    return lst;
  }

  public T get( int index )
  {
    maybeSort();
    return _values.get( index );
  }

  public int size()
  {
    maybeSort();
    return _values.size();
  }

  public Iterator<T> iterator()
  {
    maybeSort();
    return _values.iterator();
  }

  private void checkOrderBy()
  {
    if( _orderByBlocks.size() > 0 )
    {
      throw new IllegalStateException( "You can only orderBy() once.  After that, you must use thenBy()" );
    }
  }

  private void checkSort()
  {
    if( _values != null )
    {
      throw new IllegalStateException( "This list has already been sorted!" );
    }
  }

  private void maybeSort()
  {
    if( _values == null )
    {
      _values = new ArrayList<T>();
      for (T originalValue : _originalValues) {
        _values.add(originalValue);
      }
      Collections.sort( _values, new Comparator<T>()
      {
        public int compare( T o1, T o2 )
        {
          for( Tuple<T> orderByBlock : _orderByBlocks )
          {
            Comparable c1 = orderByBlock.getBlock().toComparable(o1);
            Comparable c2 = orderByBlock.getBlock().toComparable(o2);
            Comparator comparator = orderByBlock.getComparator();

            int ascendingCmp = comparator != null
                               ? comparator.compare(c1, c2)
                               : c1.compareTo( c2 );
            @SuppressWarnings({"unchecked"})
            int cmp = (orderByBlock.getDirection() == Direction.ASCENDING) ? ascendingCmp
                                                                           : (ascendingCmp < 0 ? 1 : - ascendingCmp);
            if( cmp != 0 )
            {
              return cmp;
            }
          }
          return 0;
        }
      } );
    }
  }

  public static interface IToComparable<E>
  {
    Comparable toComparable(E elt);
  }
}
