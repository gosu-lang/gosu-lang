/*
 *
 *  Copyright 2010 Guidewire Software, Inc.
 *
 */
package editor;

import editor.util.ContainerMoverSizer;
import editor.util.ContainerSizer;
import gw.lang.reflect.java.JavaTypes;
import gw.util.GosuEscapeUtil;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class StringPopup extends JPopupMenu implements IValuePopup
{
  private JPanel _pane = new JPanel();
  private EventListenerList _nodeListenerList = new EventListenerList();
  private JTextComponent _editor;
  private String _strValue;
  private JTextComponent _field;
  private String _strLabel;


  public StringPopup( String strValue, String strLabel, JTextComponent editor )
  {
    super();
    _editor = editor;
    _strValue = strValue;
    _strLabel = strLabel;

    initLayout();
  }

  protected void initLayout()
  {
    setOpaque( false );
    setDoubleBuffered( true );

    Border border = BorderFactory.createCompoundBorder(
      UIManager.getBorder( "PopupMenu.border" ),
      BorderFactory.createEmptyBorder( 2, 2, 2, 2 ) );
    ContainerMoverSizer content = new ContainerMoverSizer( border );
    content.setLayout( new BorderLayout() );

    _pane.setLayout( new BorderLayout() );

    _field = new JTextArea( 1, 26 );
    final int iPrefHeight = _field.getPreferredSize().height < 5 ? 22 : _field.getPreferredSize().height;
    _field.setText( _strValue );
    _field.addKeyListener(
      new KeyAdapter()
      {
        public void keyPressed( KeyEvent e )
        {
          if( e.getKeyCode() == KeyEvent.VK_ENTER )
          {
            if( _field.getHeight() > iPrefHeight * 2 - 2 &&
                e.getModifiers() != KeyEvent.CTRL_MASK )
            {
              return;
            }
            fireNodeChanged( _nodeListenerList, new ChangeEvent( GosuEscapeUtil.escapeForJava( _field.getText() ) ) );
            setVisible( false );
          }
        }
      } );
    _field.addKeyListener(
      new KeyAdapter()
      {
        public void keyPressed( KeyEvent e )
        {
          if( e.getKeyCode() == KeyEvent.VK_ESCAPE )
          {
            setVisible( false );
          }
        }
      } );

    JScrollPane scrollPane = new JScrollPane( _field );
    Dimension dimPref = _field.getPreferredSize();
    // Hack to make sure there's enough room for horizontal scroll bar if vertical scroll bars display
    dimPref.width += 20;
    dimPref.height = iPrefHeight;
    scrollPane.setPreferredSize( dimPref );
    _pane.add( BorderLayout.CENTER, scrollPane );

    content.add( _pane, BorderLayout.CENTER );
    add( content );

    JLabel labelTypeName = new JLabel( _strLabel == null
                                       ? JavaTypes.STRING().getRelativeName()
                                       : _strLabel );
    labelTypeName.setFont( labelTypeName.getFont().deriveFont( Font.BOLD ) );
    labelTypeName.setBorder( BorderFactory.createEmptyBorder( 0, 3, 3, 3 ) );
    content.add( labelTypeName, BorderLayout.NORTH );

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
      editor.util.EditorUtilities.removePopupBorder( this );
    }

    if( _editor == null )
    {
      return;
    }

    if( !bVisible )
    {
      _editor.requestFocus();
    }
    else
    {
      _field.requestFocus();
    }
  }

  public void show( Component invoker, int iX, int iY )
  {
    super.show( invoker, iX, iY );

    EventQueue.invokeLater( new Runnable()
    {
      public void run()
      {
        EventQueue.invokeLater( new Runnable()
        {
          public void run()
          {
            _field.requestFocus();
            _field.selectAll();
          }
        } );
      }
    } );
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
}

