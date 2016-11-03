package editor.tabpane;

import editor.AbstractListCellRenderer;
import javax.swing.*;
import java.beans.BeanInfo;

/**
 */
public class TabListCellRenderer extends AbstractListCellRenderer<ITab>
{
  public TabListCellRenderer( JComponent list )
  {
    super( list, true );
  }

  @Override
  public void configure()
  {
    ITab tab = getNode();
    if( tab == null )
    {
      setText( "" );
    }
    else
    {
      setText( tab.getLabel().getDisplayName() );
      setIcon( tab.getLabel().getIcon( BeanInfo.ICON_COLOR_16x16 ) );
    }
  }

}