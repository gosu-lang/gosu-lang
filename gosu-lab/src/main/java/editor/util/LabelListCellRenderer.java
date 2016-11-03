package editor.util;


import editor.AbstractListCellRenderer;
import javax.swing.*;
import java.beans.BeanInfo;

/**
 */
public class LabelListCellRenderer extends AbstractListCellRenderer<ILabel>
{
  public LabelListCellRenderer( JComponent list )
  {
    super( list, true );
  }

  @Override
  public void configure()
  {
    ILabel label = getNode();
    if( label == null )
    {
      setText( "" );
    }
    else
    {
      setText( label.getDisplayName() );
      setIcon( label.getIcon( BeanInfo.ICON_COLOR_16x16 ) );
    }
  }

}