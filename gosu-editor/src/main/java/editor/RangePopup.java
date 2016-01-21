package editor;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class RangePopup extends ListPopup
{
  private static final String NAME = "Name";

  /** */
  public RangePopup( JTextComponent editor )
  {
    super( editor, new RangeModel() );
  }

  /** */
  public RangePopup( JTextComponent editor, RangeModel model )
  {
    super( editor, model );
  }

  protected JPanel getSortedByPanel()
  {
    JPanel sortPanel = new JPanel();
    sortPanel.setBorder( BorderFactory.createEmptyBorder( 0, 3, 3, 3 ) );
    sortPanel.setLayout( new BoxLayout( sortPanel, BoxLayout.X_AXIS ) );
    sortPanel.add( new JLabel( "Sort by" ) );

    JToggleButton btnSortByName = new JToggleButton( NAME );

    btnSortByName.setSelected( true );
    btnSortByName.addActionListener(
      new ActionListener()
      {
        public void actionPerformed( ActionEvent e )
        {
          ((RangeModel)getModel()).setSortByName( true );
          refresh();
        }
      } );
    sortPanel.add( btnSortByName );

    return sortPanel;
  }


  protected ListCellRenderer makeCellRenderer()
  {
    return new RangeCellRenderer( (RangeModel)_model, getJList() );
  }
}
