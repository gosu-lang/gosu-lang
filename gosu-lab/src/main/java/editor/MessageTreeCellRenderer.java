package editor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 */
public class MessageTreeCellRenderer extends AbstractTreeCellRenderer<MessageTree>
{
  public MessageTreeCellRenderer( JTree tree )
  {
    super( tree );
  }

  public void configure()
  {
    if( getNode() == null )
    {
      return;
    }

    setBorder( new EmptyBorder( 0, 3, 0, 3 ) );

    setText( getNode().getMessage() );
    setIcon( getNode().getIcon() );
  }
}
