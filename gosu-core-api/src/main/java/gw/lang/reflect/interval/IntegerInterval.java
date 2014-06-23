/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.interval;

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class IntegerInterval extends NumberInterval<Integer, IntegerInterval>
{
  public IntegerInterval( Integer left, Integer right )
  {
    this( left, right, 1 );
  }
  public IntegerInterval( Integer left, Integer right, int iStep )
  {
    this( left, right, iStep, true, true, false );
  }
  public IntegerInterval( Integer left, Integer right, int iStep, boolean bLeftClosed, boolean bRightClosed, boolean bReverse )
  {
    super( left, right, iStep, bLeftClosed, bRightClosed, bReverse );

    if( iStep <= 0 )
    {
      throw new IllegalArgumentException( "The step must be greater than 0: " + iStep );
    }
  }

  @Override
  public Iterator<Integer> iterateFromLeft()
  {
    return new ForwardIterator();
  }

  @Override
  public Iterator<Integer> iterateFromRight()
  {
    return new ReverseIterator();
  }

  @Override
  public Integer getFromLeft( int iStepIndex )
  {
    if( iStepIndex < 0 )
    {
      throw new IllegalArgumentException( "Step index must be >= 0: " + iStepIndex );
    }

    if( !isLeftClosed() )
    {
      iStepIndex++;
    }    
    int value = getLeftEndpoint() + getStep() * iStepIndex;
    if( isRightClosed() ? value <= getRightEndpoint() : value < getRightEndpoint() )
    {
      return value;
    }

    return null;
  }

  @Override
  public Integer getFromRight( int iStepIndex )
  {
    if( iStepIndex < 0 )
    {
      throw new IllegalArgumentException( "Step index must be >= 0: " + iStepIndex );
    }

    if( !isRightClosed() )
    {
      iStepIndex++;
    }    
    int value = getRightEndpoint() - getStep() * iStepIndex;
    if( isLeftClosed() ? value >= getLeftEndpoint() : value > getLeftEndpoint() )
    {
      return value;
    }

    return null;
  }

  public class ForwardIterator extends AbstractIntIterator
  {
    private int _csr;

    public ForwardIterator()
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
      return _csr < getRightEndpoint() || (isRightClosed() && _csr == getRightEndpoint());
    }

    @Override
    public Integer next()
    {
      return nextInt();
    }

    public int nextInt()
    {
      if( _csr > getRightEndpoint() ||
          (!isRightClosed() && _csr == getRightEndpoint()) )
      {
        throw new NoSuchElementException();
      }
      int ret = _csr;
      _csr = _csr + getStep();
      return ret;
    }

    @Override
    public void remove()
    {
      throw new UnsupportedOperationException();
    }
  }

  private class ReverseIterator extends AbstractIntIterator
  {
    private int _csr;

    public ReverseIterator()
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
       return _csr > getLeftEndpoint() || (isLeftClosed() && _csr == getLeftEndpoint());
    }

    @Override
    public Integer next()
    {
      return nextInt();
    }

    public int nextInt()
    {
      if( _csr < getLeftEndpoint() ||
          (!isLeftClosed() && _csr == getLeftEndpoint()) )
      {
        throw new NoSuchElementException();
      }
      int ret = _csr;
      _csr = _csr - getStep();
      return ret;
    }

    @Override
    public void remove()
    {
      throw new UnsupportedOperationException();
    }
  }
}
