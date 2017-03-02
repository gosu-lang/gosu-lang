package editor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 */
public interface IHandleCancel extends RootPaneContainer
{
  default void mapCancelKeystroke( String label, Runnable cancel )
  {
    JRootPane rootPane = getRootPane();
    Object key = rootPane.getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).get( KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 ) );
    if( key == null )
    {
      key = label;
      rootPane.getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put( KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 ), key );
    }
    rootPane.getActionMap().put(
      key,
      new AbstractAction()
      {
        public void actionPerformed( ActionEvent e )
        {
          cancel.run();
        }
      } );
  }

}
