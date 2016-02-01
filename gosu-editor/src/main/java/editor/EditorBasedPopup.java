package editor;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;


public abstract class EditorBasedPopup extends JPopupMenu implements IValuePopup
{
  private GosuEditor _editor;

  public EditorBasedPopup( GosuEditor editor )
  {
    _editor = editor;
  }

  protected GosuEditor getEditor()
  {
    return _editor;
  }

  public void setVisible( boolean b )
  {
    super.setVisible( b );
    if( b && _editor != null )
    {
      try
      {
        Rectangle bounds = getParent().getBounds();
        Point popupLocation = getParent().getLocationOnScreen();
        Rectangle caretLocation = new Rectangle( _editor.getCaretLocation() );
        editor.util.EditorUtilities.convertRectangleToScreen( caretLocation, _editor );
        if( popupLocation.getY() < caretLocation.getY() + caretLocation.getHeight() )
        {
          Point loc = new Point( (int)(popupLocation.getX()), (int)(caretLocation.getY() - bounds.getHeight()) );
          SwingUtilities.convertPointFromScreen( loc, getParent().getParent() );
          getParent().setLocation( loc );
        }
      }
      catch( BadLocationException e )
      {
        // ignore
      }
    }
  }

  protected boolean endsWithInvalidChar( String strPrefix )
  {
    if( strPrefix != null && strPrefix.length() > 0 )
    {
      char c = strPrefix.charAt( strPrefix.length() - 1 );
      if( !Character.isJavaIdentifierPart( c ) &&
          !Character.isJavaIdentifierStart( c ) )
      {
        return true;
      }
    }
    return false;
  }

}