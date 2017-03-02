package editor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 */
public class LabTreeCellRenderer<T extends AbstractTree> extends AbstractTreeCellRenderer<T>
{
  public LabTreeCellRenderer( JTree tree )
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

    setText( getNode().getText() );
    setIcon( getNode().getIcon() );
  }
}
