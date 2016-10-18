package editor;

import editor.util.IDisposable;

import javax.swing.*;
import java.awt.*;

/**
 */
public abstract class ClearablePanel extends JPanel implements IDisposable
{
  public ClearablePanel()
  {
    super();
  }

  public ClearablePanel( LayoutManager layout )
  {
    super( layout );
  }

  public abstract void clear();
}
