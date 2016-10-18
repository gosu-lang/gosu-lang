package editor.util;

import editor.IHandleCancel;

import javax.swing.*;
import java.awt.*;

/**
 */
public class AbstractDialog extends JDialog implements IHandleCancel
{
  public AbstractDialog( Frame owner )
  {
    super( owner );
  }

  public AbstractDialog( Frame owner, boolean modal )
  {
    super( owner, modal );
  }

  public AbstractDialog( Frame owner, String title )
  {
    super( owner, title );
  }

  public AbstractDialog( Frame owner, String title, boolean modal )
  {
    super( owner, title, modal );
  }

  public AbstractDialog( Frame owner, String title, boolean modal, GraphicsConfiguration gc )
  {
    super( owner, title, modal, gc );
  }

  public AbstractDialog( Dialog owner )
  {
    super( owner );
  }

  public AbstractDialog( Dialog owner, boolean modal )
  {
    super( owner, modal );
  }

  public AbstractDialog( Dialog owner, String title )
  {
    super( owner, title );
  }

  public AbstractDialog( Dialog owner, String title, boolean modal )
  {
    super( owner, title, modal );
  }

  public AbstractDialog( Dialog owner, String title, boolean modal, GraphicsConfiguration gc )
  {
    super( owner, title, modal, gc );
  }

  public AbstractDialog( Window owner )
  {
    super( owner );
  }

  public AbstractDialog( Window owner, ModalityType modalityType )
  {
    super( owner, modalityType );
  }

  public AbstractDialog( Window owner, String title )
  {
    super( owner, title );
  }

  public AbstractDialog( Window owner, String title, ModalityType modalityType )
  {
    super( owner, title, modalityType );
  }

  public AbstractDialog( Window owner, String title, ModalityType modalityType, GraphicsConfiguration gc )
  {
    super( owner, title, modalityType, gc );
  }

  protected void close()
  {
    dispose();
  }
}
