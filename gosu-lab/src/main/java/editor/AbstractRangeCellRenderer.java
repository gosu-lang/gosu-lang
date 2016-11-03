package editor;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AbstractRangeCellRenderer extends JPanel implements ListCellRenderer
{
  private static Border g_noFocusBorder;
  private JList _list;
  int _iMaxCodeWidth;

  public AbstractRangeCellRenderer( JList list )
  {
    super();

    _list = list;
  }

  protected void init()
  {
    setLayout( new BorderLayout() );
    if( g_noFocusBorder == null )
    {
      g_noFocusBorder = new EmptyBorder( 1, 1, 1, 1 );
    }
    setOpaque( true );
    setBorder( g_noFocusBorder );
    setUpLabels();

    updateDimensions();

    _list.addPropertyChangeListener( "model", evt -> updateDimensions() );
  }

  /**
   *
   */
  private void updateDimensions()
  {
    ListModel model = _list.getModel();
    if( model == null )
    {
      return;
    }

    int iSize = model.getSize();
    _iMaxCodeWidth = 0;
    FontMetrics fm = _list.getFontMetrics( _list.getFont() );
    for( int i = 0; i < iSize; i++ )
    {
      _iMaxCodeWidth = Math.max( _iMaxCodeWidth, getCodeWidth( fm, model.getElementAt( i ) ) );
    }


  }


  /**
   * @param list
   * @param value
   * @param modelIndex
   * @param isSelected
   * @param cellHasFocus
   * @return
   */
  public Component getListCellRendererComponent( JList list, Object value, int modelIndex, boolean isSelected, boolean cellHasFocus )
  {
    setComponentOrientation( list.getComponentOrientation() );
    if( isSelected )
    {
      setBackground( list.getSelectionBackground() );
      setForeground( list.getSelectionForeground() );
    }
    else
    {
      setBackground( list.getBackground() );
      setForeground( list.getForeground() );
    }

    setLabelsValues( list, value, isSelected );
    setBorder( (cellHasFocus) ? UIManager.getBorder( "List.focusCellHighlightBorder" ) : g_noFocusBorder );

    return this;
  }


  protected void setUpLabels()
  {
    //implemented by subclasses

  }

  protected void setLabelsValues( JList list, Object value, boolean isSelected )
  {
    //implemented by subclasses

  }

  protected int getCodeWidth( FontMetrics fm, Object obj )
  {
    //implemented by subclasses
    return 0;
  }

}