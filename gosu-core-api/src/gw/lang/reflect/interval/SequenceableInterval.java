/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.interval;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SequenceableInterval<E extends ISequenceable<E, S, U> & Comparable<E>, S, U> extends IterableInterval<E, S, U, SequenceableInterval<E, S, U>>
{
  public SequenceableInterval( E left, E right, S step, U unit, boolean bLeftClosed, boolean bRightClosed, boolean bReverse )
  {
    super( left, right, step, unit, bReverse ? bRightClosed : bLeftClosed, bReverse ? bLeftClosed : bRightClosed, bReverse );
  }

  @Override
  public Iterator<E> iterateFromLeft()
  {
    return new SequenceableIterator();
  }

  @Override
  public Iterator<E> iterateFromRight()
  {
    return new ReverseSequenceableIterator();
  }

  @Override
  public E getFromLeft( int iStepIndex )
  {
    if( iStepIndex < 0 )
    {
      throw new IllegalArgumentException( "Step index must be >= 0: " + iStepIndex );
    }

    if( !isLeftClosed() )
    {
      iStepIndex++;
    }
    E value = getLeftEndpoint().nextNthInSequence( getStep(), getUnit(), iStepIndex );
    int iComp = value.compareTo( getRightEndpoint() );
    if( isRightClosed() ? iComp <= 0 : iComp < 0 )
    {
      return value;
    }

    return null;
  }

  @Override
  public E getFromRight( int iStepIndex )
  {
    if( iStepIndex < 0 )
    {
      throw new IllegalArgumentException( "Step index must be >= 0: " + iStepIndex );
    }

    if( !isRightClosed() )
    {
      iStepIndex++;
    }
    E value = getRightEndpoint().previousNthInSequence( getStep(), getUnit(), iStepIndex );
    int iComp = value.compareTo( getLeftEndpoint() );
    if( isLeftClosed() ? iComp >= 0 : iComp > 0 )
    {
      return value;
    }

    return null;
  }

  private class SequenceableIterator implements Iterator<E>
  {
    private E _csr;

    public SequenceableIterator()
    {
      _csr = getLeftEndpoint();
      if( !isLeftClosed() && hasNext() )
      {
        next();
      }
    }

    @Override
    public boolean hasNext()
    {
      if( _csr == null )
      {
        return false;
      }
      int iComp = _csr.compareTo( getRightEndpoint() );
      return iComp < 0 || (isRightClosed() && iComp == 0);
    }

    @Override
    public E next()
    {
      int iComp = _csr.compareTo( getRightEndpoint() );
      if( iComp > 0 ||
          (!isRightClosed() && iComp == 0) )
      {
        throw new NoSuchElementException();
      }
      E ret = _csr;
      _csr = _csr.nextInSequence( getStep(), getUnit() );
      return ret;
    }

    @Override
    public void remove()
    {
      throw new UnsupportedOperationException();
    }
  }

  private class ReverseSequenceableIterator implements Iterator<E>
  {
    private E _csr;

    public ReverseSequenceableIterator()
    {
      _csr = getRightEndpoint();
      if( !isRightClosed() && hasNext() )
      {
        next();
      }
    }

    @Override
    public boolean hasNext()
    {
      if( _csr == null )
      {
        return false;
      }

      int iComp = _csr.compareTo( getLeftEndpoint() );
      return iComp > 0 || (isLeftClosed() && iComp == 0);
    }

    @Override
    public E next()
    {
      int iComp = _csr.compareTo( getLeftEndpoint() );
      if( iComp < 0 ||
          (!isLeftClosed() && iComp == 0) )
      {
        throw new NoSuchElementException();
      }
      E ret = _csr;
      _csr = _csr.previousInSequence( getStep(), getUnit() );
      return ret;
    }

    @Override
    public void remove()
    {
      throw new UnsupportedOperationException();
    }
  }
}