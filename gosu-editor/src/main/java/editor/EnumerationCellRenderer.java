package editor;

import gw.lang.reflect.IEnumValue;

import javax.swing.*;
import java.awt.*;

/**
 */
public class EnumerationCellRenderer extends AbstractRangeCellRenderer
{

  private JLabel _labelCode;

  /**
   *
   */
  public EnumerationCellRenderer( JList list )
  {
    super( list );
    init();
  }


  protected int getCodeWidth( FontMetrics fm, Object obj )
  {
    IEnumValue enumeratedValue = (IEnumValue)obj;
    return fm.stringWidth( enumeratedValue.getCode() );
  }


  protected void setUpLabels()
  {
    _labelCode = new JLabel()
    {
      public Dimension getPreferredSize()
      {
        Dimension pref = super.getPreferredSize();
        pref.width = _iMaxCodeWidth;
        return pref;
      }
    };
    _labelCode.setOpaque( false );
    add( _labelCode, BorderLayout.CENTER );
  }


  protected void setLabelsValues( JList list, Object value, boolean isSelected )
  {
    IEnumValue typecode = (IEnumValue)value;
    if( isSelected )
    {
      _labelCode.setForeground( list.getSelectionForeground() );
    }
    else
    {
      _labelCode.setForeground( list.getForeground() );
    }

    _labelCode.setEnabled( list.isEnabled() );
    _labelCode.setFont( list.getFont() );
    _labelCode.setText( typecode == null ? "" : typecode.getCode() );

  }
}