/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.interval;

public abstract class AbstractInterval<E extends Comparable<E>, ME extends AbstractInterval<E, ME>> implements IInterval<E, ME>
{
  final private E _left;
  final private E _right;
  final private boolean _bLeftClosed;
  final private boolean _bRightClosed;
  final private boolean _bReverse;

  public AbstractInterval( E left, E right )
  {
    this( left, right, true, true, false );
  }

  public AbstractInterval( E left, E right, boolean bLeftClosed, boolean bRightClosed, boolean bReverse )
  {
    checkArgs( left, right );

    _left = left;
    _right = right;

    _bLeftClosed = bLeftClosed;
    _bRightClosed = bRightClosed;

    _bReverse = bReverse;
  }

  private void checkArgs( E left, E right )
  {
    if( left == null )
    {
      throw new IllegalArgumentException( "Non-null value expected for left endpoint. Use BigIntegerInterval for an interval with an unbounded endpoint." );
    }
    if( right == null )
    {
      throw new IllegalArgumentException( "Non-null value expected for right endpoint. Use BigIntegerInterval for an interval with an unbounded endpoint." );
    }

    if( left.compareTo( right ) > 0 )
    {
      throw new IllegalArgumentException( "The left endpoint is greater than the right endpoint: [" + left + ", " + right + "]" );
    }
  }

  @Override
  public E getLeftEndpoint()
  {
    return _left;
  }

  @Override
  public E getRightEndpoint()
  {
    return _right;
  }

  @Override
  public boolean isLeftClosed()
  {
    return _bLeftClosed;
  }

  @Override
  public boolean isRightClosed()
  {
    return _bRightClosed;
  }

  @Override
  public boolean contains( E e )
  {
    return (isLeftClosed()
            ? getLeftEndpoint().compareTo( e ) <= 0
            : getLeftEndpoint().compareTo( e ) < 0) &&
           (isRightClosed()
            ? getRightEndpoint().compareTo( e ) >= 0
            : getRightEndpoint().compareTo( e ) > 0);
  }

  @Override
  public boolean contains( ME interval )
  {
    return (isLeftClosed()
            ? getLeftEndpoint().compareTo( interval.getLeftEndpoint() ) <= 0
            : interval.isLeftClosed()
              ? getLeftEndpoint().compareTo( interval.getLeftEndpoint() ) < 0
              : getLeftEndpoint().compareTo( interval.getLeftEndpoint() ) <= 0) &&
           (isRightClosed()
            ? getRightEndpoint().compareTo( interval.getRightEndpoint() ) >= 0
            : interval.isRightClosed()
              ? getRightEndpoint().compareTo( interval.getRightEndpoint() ) > 0
              : getRightEndpoint().compareTo( interval.getRightEndpoint() ) >= 0);
  }

  @Override
  public boolean isReverse()
  {
    return _bReverse;
  }

  @Override
  public boolean equals( Object o )
  {
    if( this == o )
    {
      return true;
    }
    if( !(o instanceof AbstractInterval) )
    {
      return false;
    }

    AbstractInterval that = (AbstractInterval)o;

    if( isLeftClosed() != that.isLeftClosed() )
    {
      return false;
    }
    if( isReverse() != that.isReverse() )
    {
      return false;
    }
    if( isRightClosed() != that.isRightClosed() )
    {
      return false;
    }
    if( !getLeftEndpoint().equals( that.getLeftEndpoint() ) )
    {
      return false;
    }
    return getRightEndpoint().equals( that.getRightEndpoint() );

  }

  @Override
  public int hashCode()
  {
    int result = getLeftEndpoint().hashCode();
    result = 31 * result + getRightEndpoint().hashCode();
    result = 31 * result + (isLeftClosed() ? 1 : 0);
    result = 31 * result + (isRightClosed() ? 1 : 0);
    result = 31 * result + (isReverse() ? 1 : 0);
    return result;
  }

  @Override
  public String toString()
  {
    if( isReverse() )
    {
      return getRightEndpoint() + (isRightClosed() ? "" : "|") + ".." + (isLeftClosed() ? "" : "|") + getLeftEndpoint();
    }
    return getLeftEndpoint() + (isLeftClosed() ? "" : "|") + ".." + (isRightClosed() ? "" : "|") + getRightEndpoint();
  }
}