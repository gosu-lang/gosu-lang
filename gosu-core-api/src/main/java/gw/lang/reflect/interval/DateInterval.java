/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.interval;

import gw.config.CommonServices;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class DateInterval extends IterableInterval<Date, Integer, DateUnit, DateInterval>
{
  @SuppressWarnings({"UnusedDeclaration"})
  public DateInterval( Date left, Date right, Integer step, DateUnit unit )
  {
    this( left, right, step, unit, true, true, false );
  }

  public DateInterval( Date left, Date right, int iStep, DateUnit unit, boolean bLeftClosed, boolean bRightClosed, boolean bReverse )
  {
    super( left, right, iStep, unit == null ? DateUnit.DAYS : unit, bLeftClosed, bRightClosed, bReverse );
  }

  @Override
  public Iterator<Date> iterateFromLeft()
  {
    return new ForwardIterator();
  }

  @Override
  public Iterator<Date> iterateFromRight()
  {
    return new ReverseIterator();
  }

  private Date add( Date date )
  {
    return add( date, 1 );
  }
  private Date add( Date date, int iMultiple )
  {
    Calendar dateTime = Calendar.getInstance(CommonServices.getEntityAccess().getTimeZone());
    dateTime.setTime( date );

    dateTime.add( getUnit().getCalendarConst(), getStep() * iMultiple );
    return dateTime.getTime();
  }

  private Date subtract( Date date )
  {
    return subtract( date, 1 );
  }
  private Date subtract( Date date, int iMultiple )
  {
    Calendar dateTime = Calendar.getInstance(CommonServices.getEntityAccess().getTimeZone());
    dateTime.setTime( date );

    dateTime.add( getUnit().getCalendarConst(), -(getStep() * iMultiple) );
    return dateTime.getTime();
  }

  @Override
  public Date getFromLeft( int iStepIndex )
  {
    if( iStepIndex < 0 )
    {
      throw new IllegalArgumentException( "Step index must be >= 0: " + iStepIndex );
    }

    Date date = add( getLeftEndpoint(), isLeftClosed() ? iStepIndex : iStepIndex+1 );
    int iComp = date.compareTo( getRightEndpoint() );
    if( isRightClosed() ? iComp <= 0 : iComp < 0 )
    {
      return date;
    }

    return null;
  }

  @Override
  public Date getFromRight( int iStepIndex )
  {
    if( iStepIndex < 0 )
    {
      throw new IllegalArgumentException( "Step index must be >= 0: " + iStepIndex );
    }

    Date date = subtract( getRightEndpoint(), isRightClosed() ? iStepIndex : iStepIndex+1 );
    int iComp = date.compareTo( getLeftEndpoint() );
    if( isLeftClosed() ? iComp >= 0 : iComp > 0 )
    {
      return date;
    }

    return null;
  }

  private class ForwardIterator implements Iterator<Date>
  {
    private Date _iCsr;

    public ForwardIterator()
    {
      _iCsr = getLeftEndpoint();
      if( !isLeftClosed() && hasNext() )
      {
        next();
      }
    }

    @Override
    public boolean hasNext()
    {
      int iComp = _iCsr.compareTo( getRightEndpoint() );
      return iComp < 0 || (isRightClosed() && iComp == 0);
    }

    @Override
    public Date next()
    {
      int iComp = _iCsr.compareTo( getRightEndpoint() );
      if( iComp > 0 ||
          (!isRightClosed() && iComp == 0) )
      {
        throw new NoSuchElementException();
      }
      Date date = _iCsr;
      _iCsr = add( _iCsr );
      return date;
    }

    @Override
    public void remove()
    {
      throw new UnsupportedOperationException();
    }
  }
  
  private class ReverseIterator implements Iterator<Date>
  {
    private Date _csr;

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
      int iComp = _csr.compareTo( getLeftEndpoint() );
      return iComp > 0 || (isLeftClosed() && iComp == 0);
    }

    @Override
    public Date next()
    {
      int iComp = _csr.compareTo( getLeftEndpoint() );
      if( iComp < 0 ||
          (!isLeftClosed() && iComp == 0) )
      {
        throw new NoSuchElementException();
      }
      Date iRet = _csr;
      _csr = subtract( _csr );
      return iRet;
    }

    @Override
    public void remove()
    {
      throw new UnsupportedOperationException();
    }
  }
}