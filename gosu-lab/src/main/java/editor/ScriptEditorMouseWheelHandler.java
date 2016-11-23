package editor;

import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 */
public class ScriptEditorMouseWheelHandler implements MouseWheelListener
{
  private EditorHost _editor;

  public ScriptEditorMouseWheelHandler( EditorHost editor )
  {
    _editor = editor;
  }

  public void mouseWheelMoved( MouseWheelEvent e )
  {
    // For high-resolution pointing devices, the events sometimes come in with 0 rotation, which we
    // want to ignore as it indicates a small incremental scroll.
    if( e.getWheelRotation() == 0 )
    {
      return;
    }

    if( !e.isControlDown() )
    {
      forward( e );
      return;
    }

    int iInc = e.getWheelRotation() < 0 ? -1 : 1;

    Font font = _editor.getEditor().getFont();
    int iSize = font.getSize() + iInc;
    if( iSize < 4 || iSize > 72 )
    {
      return;
    }

    GosuStyleContext.setDefaultFontSize( iSize );
    GosuEditorKit.getStylePreferences().setFontSize( iSize );

    font = font.deriveFont( (float)iSize );
    _editor.getEditor().setFont( font );
    _editor.getScroller().getAdviceColumn().revalidate();
    _editor.getScroller().getAdviceColumn().repaint();
  }

  /**
   * For some reason the parent does not get mouse wheel
   */
  private void forward( MouseWheelEvent e )
  {
    e = new MouseWheelEvent( e.getComponent().getParent(), e.getID(), e.getWhen(), e.getModifiers(), e.getX(), e.getY(), e.getClickCount(), e.isPopupTrigger(), e.getScrollType(), e.getScrollAmount(), e.getWheelRotation() );
    Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent( e );
  }
}
