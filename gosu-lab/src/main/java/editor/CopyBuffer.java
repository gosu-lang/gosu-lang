package editor;

import editor.util.EditorUtilities;
import gw.util.GosuObjectUtil;
import gw.util.GosuStringUtil;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Provides a simple copy buffer.
 *
 * @author cgross
 */
public class CopyBuffer
{
  List<String> _buffer = new ArrayList<>();

  private static final CopyBuffer INSTANCE = new CopyBuffer();
  private static final int MAX_SIZE = 10;
  private WindowListener _adapter;

  CopyBuffer()
  {
    _adapter =
      new WindowAdapter()
      {
        public void windowActivated( WindowEvent e )
        {
          if( e.getOppositeWindow() == null )
          {
            captureState();
          }
        }
      };
  }

  public static CopyBuffer instance()
  {
    return INSTANCE;
  }

  public void captureState()
  {
    try
    {
      if( !EditorUtilities.getClipboard().isDataFlavorAvailable( DataFlavor.stringFlavor ) )
      {
        return;
      }
    }
    catch( Exception e )
    {
      // sometimes the clipboard doesn't open
      return;
    }

    Transferable contents = editor.util.EditorUtilities.getClipboard().getContents( this );
    addToCopyBuffer( contents );
  }

  void addToCopyBuffer( Transferable contents )
  {
    try
    {
      String element = String.valueOf( contents.getTransferData( DataFlavor.stringFlavor ) );
      if( !GosuStringUtil.isEmpty( element ) )
      {
        if( _buffer.size() == 0 || !GosuObjectUtil.equals( element, _buffer.get( 0 ) ) )
        {
          _buffer.add( 0, element );
          if( _buffer.size() > MAX_SIZE )
          {
            _buffer.remove( MAX_SIZE );
          }
        }
      }
    }
    catch( Exception e )
    {
      editor.util.EditorUtilities.handleUncaughtException( "Unable to copy from clipboard", e );
      //igmore
    }
  }

  public List<String> getCopyBuffer()
  {
    return _buffer;
  }

  public void notifyOfPaste( int index )
  {
    if( index >= 0 && index < MAX_SIZE )
    {
      Collections.swap( _buffer, 0, index );
    }
  }

  public WindowListener getWindowAdapter()
  {
    return _adapter;
  }

}