package editor.tabpane;

import editor.IValuePopup;
import editor.search.StudioUtilities;
import editor.util.ContainerMoverSizer;
import editor.util.ContainerSizer;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 */
public class TabListPopup extends JPopupMenu implements IValuePopup
{
  private JPanel _pane = new JPanel();
  private JList _list;
  private EventListenerList _nodeListenerList = new EventListenerList();
  private TabContainer _tabContainer;

  /** */
  public TabListPopup( TabContainer tabContainer )
  {
    super();
    _tabContainer = tabContainer;
    initLayout();
  }

  /** */
  protected void initLayout()
  {
    setOpaque( false );
    setDoubleBuffered( true );

    GridBagLayout gridBag = new GridBagLayout();
    _pane.setLayout( gridBag );
    GridBagConstraints c = new GridBagConstraints();

    Border border = BorderFactory.createCompoundBorder(
      UIManager.getBorder( "PopupMenu.border" ),
      BorderFactory.createEmptyBorder( 2, 2, 2, 2 ) );
    ContainerMoverSizer content = new ContainerMoverSizer( border );
    content.setLayout( new BorderLayout() );

    java.util.List tabs = getTabs();

    int iY = 0;
    if( tabs != null && tabs.size() > 0 )
    {
      c.anchor = GridBagConstraints.WEST;
      c.fill = GridBagConstraints.HORIZONTAL;
      c.gridx = 0;
      c.gridy = iY++;
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.gridheight = 1;
      c.weightx = 1;
      c.weighty = 0;

      JLabel labelTitle = new JLabel( "Open Views" );
      _pane.add( labelTitle, c );
    }

    //
    // The Tab List
    //
    _list = new JList( new TabListModel( tabs ) );
    _list.addMouseListener( new TabListListener() );
    _list.setCellRenderer( new TabListCellRenderer() );
    _list.getSelectionModel().setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
    _list.setVisibleRowCount( 10 );
    JScrollPane scrollPane = new JScrollPane( _list );
    scrollPane.setBorder( UIManager.getBorder( "TextField.border" ) );

    c.anchor = GridBagConstraints.CENTER;
    c.fill = GridBagConstraints.BOTH;
    c.gridx = 0;
    c.gridy = iY++;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = 1;
    _pane.add( scrollPane, c );

    content.add( _pane, BorderLayout.CENTER );
    add( content );

    JPanel sizerPanel = new JPanel( new BorderLayout() );
    sizerPanel.add( new JPanel(), BorderLayout.CENTER );
    sizerPanel.add( new ContainerSizer(), BorderLayout.EAST );
    content.add( sizerPanel, BorderLayout.SOUTH );
  }

  public void setVisible( boolean bVisible )
  {
    super.setVisible( bVisible );

    if( bVisible )
    {
      StudioUtilities.removePopupBorder( this );
    }
  }

  public void addNodeChangeListener( ChangeListener l )
  {
    _nodeListenerList.add( ChangeListener.class, l );
  }

  public void removeNodeChangeListener( ChangeListener l )
  {
    _nodeListenerList.remove( ChangeListener.class, l );
  }

  protected void fireNodeChanged( final EventListenerList list, final ChangeEvent e )
  {
    EventQueue.invokeLater( new Runnable()
    {
      public void run()
      {
        fireNodeChangedNow( list, e );
      }
    } );
  }

  protected void fireNodeChangedNow( EventListenerList list, ChangeEvent e )
  {
    // Guaranteed to return a non-null array
    Object[] listeners = list.getListenerList();

    // Process the listeners last to first,
    // notifying those that are interested in this event
    for( int i = listeners.length - 2; i >= 0; i -= 2 )
    {
      if( listeners[i] == ChangeListener.class )
      {
        ((ChangeListener)listeners[i + 1]).stateChanged( e );
      }
    }
  }

  private java.util.List getTabs()
  {
    java.util.List tabs = Arrays.asList( _tabContainer.getTabs() );
    Collections.sort( tabs, new TabComparator() );
    return tabs;
  }

  class TabListListener extends MouseAdapter
  {
    public void mouseReleased( MouseEvent e )
    {
      int iIndex = _list.locationToIndex( e.getPoint() );
      if( iIndex < 0 )
      {
        return;
      }

      _list.setSelectedIndex( iIndex );

      ITab tab = (ITab)_list.getSelectedValue();

      fireNodeChanged( _nodeListenerList, new ChangeEvent( tab ) );
      setVisible( false );
    }
  }

  class TabListModel extends AbstractListModel
  {
    java.util.List/*<ITab>*/ _tabs;

    /** */
    TabListModel( java.util.List tabs )
    {
      _tabs = tabs;
    }

    public int getSize()
    {
      return _tabs.size();
    }

    public Object getElementAt( int i )
    {
      return _tabs.size() == 0 ? null : _tabs.get(i);
    }
  }

  private static class TabComparator implements Comparator
  {
    public int compare( Object o1, Object o2 )
    {
      String code1 = ((ITab)o1).getLabel().getDisplayName();
      String code2 = ((ITab)o2).getLabel().getDisplayName();
      try
      {
        int iCode1 = Integer.parseInt( code1 );
        int iCode2 = Integer.parseInt( code2 );
        return iCode1 > iCode2 ? 1 : iCode1 < iCode2 ? -1 : 0;
      }
      catch( NumberFormatException nfe )
      {
        return code1.compareToIgnoreCase( code2 );
      }
    }
  }
}

