package editor;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;

/**
 */
public class ScrollableEditorRootPane extends JRootPane implements Scrollable
{
  private JTextComponent _editor;

  public ScrollableEditorRootPane( JTextComponent editor )
  {
    super();
    _editor = editor;
    setFocusable( false );
  }

  public Dimension getPreferredScrollableViewportSize()
  {
    return _editor.getPreferredScrollableViewportSize();
  }

  public int getScrollableUnitIncrement( Rectangle visibleRect, int orientation, int direction )
  {
    return _editor.getScrollableUnitIncrement( visibleRect, orientation, direction );
  }

  public int getScrollableBlockIncrement( Rectangle visibleRect, int orientation, int direction )
  {
    return _editor.getScrollableBlockIncrement( visibleRect, orientation, direction );
  }

  public boolean getScrollableTracksViewportWidth()
  {
    return getParent().getWidth() > _editor.getPreferredSize().width;
  }

  public boolean getScrollableTracksViewportHeight()
  {
    return getParent().getHeight() > _editor.getPreferredSize().height;
  }
}
