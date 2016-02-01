package editor.tabpane;

import editor.util.IDisposable;
import editor.util.ILabel;

import javax.swing.*;
import java.awt.*;

/**
 */
public interface ITab extends IDisposable
{
  public ILabel getLabel();
  public TabPosition getTabPosition();
  public JComponent getComponent();
  public Dimension getInnerSize();
  public boolean isSelected();
  public boolean isHover();
  public void setHover( boolean bHover );
  public JComponent getContentPane();
  public boolean canClose();

  void refresh();
}
