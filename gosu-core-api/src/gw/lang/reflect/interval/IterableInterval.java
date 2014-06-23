/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.interval;

import java.util.Iterator;

public abstract class IterableInterval<E extends Comparable<E>, S, U, ME extends IterableInterval<E, S, U, ME>> extends AbstractInterval<E, ME> implements IIterableInterval<E, S, U, ME>
{
  private S _step;
  private U _unit;

  public IterableInterval( E left, E right, S step )
  {
    this( left, right, step, null, true, true, false );
  }

  public IterableInterval( E left, E right, S step, U unit, boolean bLeftClosed, boolean bRightClosed, boolean bReverse )
  {
    super( left, right, bReverse ? bRightClosed : bLeftClosed, bReverse ? bLeftClosed : bRightClosed, bReverse );

    _step = step;
    _unit = unit;
  }

  @Override
  public Iterator<E> iterator()
  {
    if( isReverse() )
    {
      return iterateFromRight();
    }
    else
    {
      return iterateFromLeft();
    }
  }
  
  @Override
  public S getStep()
  {
    return _step;
  }
  @Override
  public ME step( S s )
  {
    _step = s;
    //noinspection unchecked
    return (ME)this;
  }

  @Override
  public U getUnit()
  {
    return _unit;
  }
  @Override
  public ME unit( U u )
  {
    _unit = u;
    //noinspection unchecked
    return (ME)this;
  }

  @Override
  public boolean equals( Object o )
  {
    if( this == o )
    {
      return true;
    }
    if( !(o instanceof IterableInterval) )
    {
      return false;
    }
    if( !super.equals( o ) )
    {
      return false;
    }

    IterableInterval that = (IterableInterval)o;

    if( _step != null ? !_step.equals( that._step ) : that._step != null )
    {
      return false;
    }
    return !(_unit != null ? !_unit.equals( that._unit ) : that._unit != null);
  }

  @Override
  public int hashCode()
  {
    int result = super.hashCode();
    result = 31 * result + (_step != null ? _step.hashCode() : 0);
    result = 31 * result + (_unit != null ? _unit.hashCode() : 0);
    return result;
  }

  @Override
  public String toString()
  {
    return super.toString() + " step " + getStep();
  }
}