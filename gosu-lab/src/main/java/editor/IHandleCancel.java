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
    InputMap inputMap = rootPane.getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT );
    KeyStroke escKeyStroke = KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 );
    Object key = inputMap.get( escKeyStroke );
    if( key == null )
    {
      key = label;
      inputMap.put( escKeyStroke, key );
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
