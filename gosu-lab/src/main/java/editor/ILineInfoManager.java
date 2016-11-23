package editor;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 */
public interface ILineInfoManager
{
  public int getRequiredWidth();

  public void render( Graphics g, int iLine, int iLineHeight, int iX, int iY );

  public void handleLineClick( MouseEvent e, int iLine, int iX, int iY );

  Cursor getCursor( int iLine );

  void renderHighlight( Graphics g, int iLine );

  EditorHost getEditor();
  void setEditor( EditorHost gosuEditor );
}
