package editor.util;


import editor.IValuePopup;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 */
public class LabelListPopup extends PopupContainer implements IValuePopup
{
  private JList _list;
  private List _labelList;
  private EventListenerList _nodeListenerList = new EventListenerList();
  private String _emptyText;

  /** */
  public LabelListPopup( String strTitle, List labels, String emptyText )
  {
    _labelList = labels;
    _emptyText = emptyText;
    initLayout( strTitle );
  }

  @Override
  protected Component getContentPanel()
  {
    if( _labelList.isEmpty() )
    {
      _list = new JList( new String[]{_emptyText} );
      _list.setSelectionModel( new DefaultListSelectionModel()
      {
        @Override
        public void addSelectionInterval( int index0, int index1 )
        {
          // disallow selection
        }

        @Override
        public void setSelectionInterval( int index0, int index1 )
        {
          // disallow selection
        }
      } );
      _list.setForeground( new Color( 150, 150, 150 ) );
      _list.setFont( _list.getFont().deriveFont( Font.ITALIC ) );
    }
    else
    {
      _list = new JList( new LabelListModel( _labelList ) );
      _list.addMouseListener( new LabelListListener() );
      _list.setCellRenderer( new LabelListCellRenderer() );
      _list.addKeyListener( new LabelListKeyListener() );
      _list.getSelectionModel().setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
    }
    _list.setVisibleRowCount( 10 );
    JScrollPane scrollPane = new JScrollPane( _list );
    scrollPane.setBorder( UIManager.getBorder( "TextField.border" ) );
    return scrollPane;
  }

  @Override
  public Dimension getPreferredSize()
  {
    Dimension dim = super.getPreferredSize();
    dim.width = Math.max( dim.width, 180 );
    return dim;
  }

  @Override
  public void setVisible( boolean bVisible )
  {
    super.setVisible( bVisible );

    if( bVisible )
    {
      if( _labelList.size() > 0 )
      {
        _list.setSelectedIndex( 0 );
      }
      SwingUtilities.invokeLater( new Runnable()
      {
        public void run()
        {
          _list.requestFocus();
        }
      } );
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

  class LabelListListener extends MouseAdapter
  {
    @Override
    public void mouseClicked( MouseEvent e )
    {
      int iIndex = _list.locationToIndex( e.getPoint() );
      if( iIndex < 0 )
      {
        return;
      }

      _list.setSelectedIndex( iIndex );

      ILabel label = (ILabel)_list.getSelectedValue();

      fireNodeChanged( _nodeListenerList, new ChangeEvent( label ) );
      setVisible( false );
    }
  }

  class LabelListModel extends AbstractListModel
  {
    List _labels;

    /** */
    LabelListModel( List labels )
    {
      _labels = labels;
    }

    public int getSize()
    {
      return _labels.size();
    }

    public Object getElementAt( int i )
    {
      return _labels.size() == 0 ? null : _labels.get( i );
    }
  }

  class LabelListKeyListener extends KeyAdapter
  {
    @Override
    public void keyReleased( KeyEvent e )
    {
      if( e.getKeyCode() == KeyEvent.VK_ENTER )
      {
        ILabel label = (ILabel)_list.getSelectedValue();
        if( label != null )
        {
          fireNodeChanged( _nodeListenerList, new ChangeEvent( label ) );
        }
        setVisible( false );
        e.consume();
      }
      else if( e.getKeyCode() == KeyEvent.VK_ESCAPE )
      {
        setVisible( false );
        e.consume();
      }
    }
  }
}

