package editor.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 */
public class AbstractDialog extends JDialog
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

  protected void mapCancelKeystroke()
  {
    Object key = getRootPane().getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).get( KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 ) );
    if( key == null )
    {
      key = "Cancel";
      getRootPane().getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put( KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 ), key );
    }
    getRootPane().getActionMap().put( key,
                                      new AbstractAction()
                                      {
                                        public void actionPerformed( ActionEvent e )
                                        {
                                          close();
                                        }
                                      } );
  }

}
