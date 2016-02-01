package editor.tabpane;

import editor.util.EditorUtilities;
import editor.util.VerticalLabelUI;

import javax.swing.*;
import java.awt.*;

/**
 */
class InnerLabel extends JLabel
{
  private StandardTab _tab;

  public InnerLabel( StandardTab tab, String strText, Icon icon, int iHorizontalAlignment )
  {
    super( strText, icon, iHorizontalAlignment );

    _tab = tab;

    if( _tab.getTabPosition() == TabPosition.TOP ||
        _tab.getTabPosition() == TabPosition.BOTTOM )
    {
      setBorder( BorderFactory.createEmptyBorder( 0, 5, 0, 5 ) );
    }
    else if( _tab.getTabPosition() == TabPosition.LEFT )
    {
      setBorder( BorderFactory.createEmptyBorder( 5, 0, 5, 0 ) );
      setUI( new VerticalLabelUI( false ) );
    }
    else if( _tab.getTabPosition() == TabPosition.RIGHT )
    {
      setBorder( BorderFactory.createEmptyBorder( 5, 0, 5, 0 ) );
      setUI( new VerticalLabelUI( true ) );
    }
  }

  @Override
  public Color getForeground()
  {
    try {
      if (_tab != null) {
        return
                _tab.isSelected()
                        ? _tab.getTabPane().isShowing() && _tab.getTabPane().isActive()
                        ? EditorUtilities.ACTIVE_CAPTION_TEXT
                        : super.getForeground()
                        : super.getForeground();
      }
    }
    catch( Throwable t )
    {
      // fall through
    }
    return Color.black;
  }
}
