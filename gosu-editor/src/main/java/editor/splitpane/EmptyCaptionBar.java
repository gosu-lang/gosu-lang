package editor.splitpane;

import editor.tabpane.TabPosition;

import javax.swing.*;
import java.awt.*;

/**
*/
public class EmptyCaptionBar extends JPanel implements ICaptionBar
{
  public EmptyCaptionBar()
  {
    setBackground( Color.red );
  }

  public Dimension getMaximumSize()
  {
    return new Dimension( 0, 0 );
  }

  public Dimension getPreferredSize()
  {
    return new Dimension( 0, 0 );
  }

  public Component[] getDecorations()
  {
    return new Component[0];
  }

  public void addDecoration( JComponent decoration, int iIndex )
  {
  }

  public void setCaptionType( ICaptionActionListener.ActionType actionType )
  {
  }

  public ICaptionActionListener.ActionType getCaptionType()
  {
    return null;
  }

  public ICaptionBar getMinimizedPanel( TabPosition tabPosition )
  {
    return null;
  }

  public void addCaptionActionListener( ICaptionActionListener captionListener )
  {
  }

  public void removeCaptionActionListener( ICaptionActionListener captionListener )
  {
  }
}
