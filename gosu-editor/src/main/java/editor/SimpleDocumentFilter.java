package editor;

import editor.util.Pair;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.util.ArrayList;
import java.util.List;

/**
 */
public abstract class SimpleDocumentFilter extends DocumentFilter
{

  private final Object _promptLock = new Object();

  private List<Pair<Integer, Integer>> _offsetMods = new ArrayList<Pair<Integer, Integer>>();

  @Override
  public synchronized void remove( final FilterBypass fb, final int offset, final int length ) throws BadLocationException
  {
    if( isPromptingForEdit() )
    {
      new Thread()
      {
        @Override
        public void run()
        {
          try
          {
            synchronized( _promptLock )
            {
              _promptLock.wait();
            }
            if( wasLastPromptAccepted() )
            {
              SwingUtilities.invokeLater( new Runnable()
              {
                @Override
                public void run()
                {
                  int realOffset = getRealOffset( offset );
                  try
                  {
                    SimpleDocumentFilter.super.remove( fb, realOffset, length );
                  }
                  catch( BadLocationException e )
                  {
                    throw new RuntimeException( e );
                  }
                  _offsetMods.add( new Pair<Integer, Integer>( realOffset, -length ) );
                }
              } );
            }
          }
          catch( Exception e )
          {
            throw new RuntimeException( e );
          }
        }
      }.start();
    }
    else
    {
      _offsetMods.clear();
      if( acceptEdit( null ) )
      {
        super.remove( fb, offset, length );
        _offsetMods.add( new Pair<Integer, Integer>( offset, -length ) );
      }
      synchronized( _promptLock )
      {
        _promptLock.notifyAll();
      }
    }
  }

  @Override
  public synchronized void replace( final FilterBypass fb, final int offset, final int length, final String text, final AttributeSet attrs ) throws BadLocationException
  {
    if( isPromptingForEdit() )
    {
      new Thread()
      {
        @Override
        public void run()
        {
          try
          {
            synchronized( _promptLock )
            {
              _promptLock.wait();
            }
            if( wasLastPromptAccepted() )
            {
              SwingUtilities.invokeLater( new Runnable()
              {
                @Override
                public void run()
                {
                  int realOffset = getRealOffset( offset );
                  try
                  {
                    SimpleDocumentFilter.super.replace( fb, realOffset, length, text, attrs );
                  }
                  catch( BadLocationException e )
                  {
                    throw new RuntimeException( e );
                  }
                  _offsetMods.add( new Pair<Integer, Integer>( realOffset, text.length() - length ) );
                }
              } );
            }
          }
          catch( Exception e )
          {
            throw new RuntimeException( e );
          }
        }
      }.start();
    }
    else
    {
      _offsetMods.clear();
      if( acceptEdit( text ) )
      {
        super.replace( fb, offset, length, text, attrs );
        _offsetMods.add( new Pair<Integer, Integer>( offset, text == null ? 0 : text.length() - length ) );
      }
      synchronized( _promptLock )
      {
        _promptLock.notifyAll();
      }
    }
  }

  @Override
  public synchronized void insertString( final FilterBypass fb, final int offset, final String string, final AttributeSet attr ) throws BadLocationException
  {
    if( isPromptingForEdit() )
    {
      new Thread()
      {
        @Override
        public void run()
        {
          try
          {
            synchronized( _promptLock )
            {
              _promptLock.wait();
            }
            if( wasLastPromptAccepted() )
            {
              SwingUtilities.invokeLater( new Runnable()
              {
                @Override
                public void run()
                {
                  int realOffset = getRealOffset( offset );
                  try
                  {
                    SimpleDocumentFilter.super.insertString( fb, realOffset, string, attr );
                  }
                  catch( BadLocationException e )
                  {
                    throw new RuntimeException( e );
                  }
                  _offsetMods.add( new Pair<Integer, Integer>( realOffset, string.length() ) );
                }
              } );
            }
          }
          catch( Exception e )
          {
            throw new RuntimeException( e );
          }
        }
      }.start();
    }
    else
    {
      _offsetMods.clear();
      if( acceptEdit( string ) )
      {
        super.insertString( fb, offset, string, attr );
        _offsetMods.add( new Pair<Integer, Integer>( offset, string.length() ) );
      }
      synchronized( _promptLock )
      {
        _promptLock.notifyAll();
      }
    }
  }

  public void waitForPrompt()
  {
    synchronized( _promptLock )
    {
      try
      {
        _promptLock.wait();
      }
      catch( InterruptedException e )
      {
        throw new RuntimeException( e );
      }
    }
  }

  public abstract boolean acceptEdit( String insertedText );

  protected boolean isPromptingForEdit()
  {
    return false;
  }

  protected boolean wasLastPromptAccepted()
  {
    return true;
  }

  private int getRealOffset( int offset )
  {
    int realOffset = offset;
    for( Pair<Integer, Integer> offsetMod : _offsetMods )
    {
      if( realOffset >= offsetMod.getFirst() )
      {
        realOffset += offsetMod.getSecond();
      }
    }
    return realOffset;
  }
}
