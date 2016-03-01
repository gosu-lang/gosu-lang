package editor;

import javax.swing.*;
import java.awt.*;


public class RangeCellRenderer extends AbstractRangeCellRenderer
{
  private JLabel _hyphen;
  private JLabel _labelName;
  private RangeModel _model;

  public RangeCellRenderer( RangeModel model, JList list )
  {
    super( list );
    _model = model;
    init();
  }

  protected void setUpLabels()
  {

    _labelName = new JLabel()
    {
      public Dimension getPreferredSize()
      {
        Dimension pref = super.getPreferredSize();
        pref.width = _iMaxCodeWidth;
        return pref;
      }
    };
    _labelName.setOpaque( false );

    JPanel codePanel = new JPanel( new BorderLayout() );
    codePanel.setOpaque( false );

    _hyphen = new JLabel( "  -  " );
    _hyphen.setOpaque( false );

    codePanel.add( _labelName, BorderLayout.WEST );
    codePanel.add( _hyphen, BorderLayout.EAST );

    add( codePanel, BorderLayout.WEST );
  }


  protected void setLabelsValues( JList list, Object value, boolean isSelected )
  {
    if( isSelected )
    {
      _labelName.setForeground( list.getSelectionForeground() );
      _hyphen.setForeground( list.getSelectionForeground() );
    }
    else
    {
      _labelName.setForeground( list.getForeground() );
      _hyphen.setForeground( list.getForeground() );
    }

    _labelName.setEnabled( list.isEnabled() );
    _labelName.setFont( list.getFont() );
    _labelName.setText( value == null ? "" : _model.getDisplayText( value ) );

    _hyphen.setEnabled( list.isEnabled() );
    _hyphen.setFont( list.getFont() );

  }


  protected int getCodeWidth( FontMetrics fm, Object obj )
  {
    return fm.stringWidth( _model.getDisplayText( obj ) );
  }

}