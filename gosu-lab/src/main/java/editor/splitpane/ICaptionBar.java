package editor.splitpane;


import editor.tabpane.TabPosition;

import javax.swing.*;
import java.awt.*;

/**
 */
public interface ICaptionBar
{
  public Component[] getDecorations();

  public void addDecoration( JComponent decoration, int iIndex );

  public void setCaptionType( ICaptionActionListener.ActionType actionType );
  public ICaptionActionListener.ActionType getCaptionType();

  public ICaptionBar getMinimizedPanel( TabPosition tabPosition );

  public void addCaptionActionListener( ICaptionActionListener captionListener );
  public void removeCaptionActionListener( ICaptionActionListener captionListener );
}
