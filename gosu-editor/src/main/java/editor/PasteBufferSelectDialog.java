package editor;

import gw.util.GosuStringUtil;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * @author cgross
 */
public class PasteBufferSelectDialog extends JDialog implements ListSelectionListener, ClipboardOwner
{
  private JTextArea _textArea;
  private List<String> _copyBuffer;
  private JTextComponent _textComponent;
  private JList _selectionList;
  private JSplitPane _splitPane;

  public PasteBufferSelectDialog( JTextComponent textComponent )
    throws HeadlessException
  {
    super( editor.util.EditorUtilities.frameForComponent( textComponent ), "Choose content to paste" );
    _textComponent = textComponent;
    _copyBuffer = CopyBuffer.instance().getCopyBuffer();
    initUI();
  }

  private void initUI()
  {
    JPanel contentPanel = new JPanel();
    contentPanel.setLayout( new BorderLayout() );

    installKeys( contentPanel );

    _textArea = new JTextArea();
    _textArea.setEditable( false );
    JScrollPane bottom = new JScrollPane( _textArea );

    List<String> strings = _copyBuffer;
    _selectionList = new JList( truncateList( strings ) );
    _selectionList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
    _selectionList.addListSelectionListener( this );
    _selectionList.setFont( _textArea.getFont() );
    _selectionList.setCellRenderer( new CustomListCellRenderer() );
    JScrollPane top = new JScrollPane( _selectionList );

    _splitPane = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT, top, bottom );

    contentPanel.add( _splitPane, BorderLayout.CENTER );
    JPanel buttonPane = new JPanel( new FlowLayout( FlowLayout.RIGHT ) );
    buttonPane.add( new JButton( new AbstractAction( "OK" )
    {
      public void actionPerformed( ActionEvent e )
      {
        doPaste();
      }
    } ) );

    buttonPane.add( new JButton( new AbstractAction( "Cancel" )
    {
      public void actionPerformed( ActionEvent e )
      {
        setVisible( false );
      }
    } ) );

    contentPanel.add( buttonPane, BorderLayout.SOUTH );
    setSize( 400, 500 );
    editor.util.EditorUtilities.centerWindowInFrame( this, getOwner() );
    contentPanel.setBorder( BorderFactory.createEmptyBorder( 8, 8, 8, 8 ) );
    setContentPane( contentPanel );
  }

  private void installKeys( JPanel contentPanel )
  {
    InputMap inputMap = contentPanel.getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT );
    inputMap.put( KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 ), "close-window" );
    contentPanel.getActionMap().put( "close-window", new AbstractAction()
    {
      public void actionPerformed( ActionEvent e )
      {
        setVisible( false );
      }
    } );

    inputMap.put( KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, 0 ), "paste-selection" );
    contentPanel.getActionMap().put( "paste-selection", new AbstractAction()
    {
      public void actionPerformed( ActionEvent e )
      {
        doPaste();
      }
    } );

    inputMap.put( KeyStroke.getKeyStroke( KeyEvent.VK_UP, 0 ), "up-arrow" );
    contentPanel.getActionMap().put( "up-arrow", new AbstractAction()
    {
      public void actionPerformed( ActionEvent e )
      {
        selectPrevious();
      }
    } );

    inputMap.put( KeyStroke.getKeyStroke( KeyEvent.VK_DOWN, 0 ), "down-arrow" );
    contentPanel.getActionMap().put( "down-arrow", new AbstractAction()
    {
      public void actionPerformed( ActionEvent e )
      {
        selectNext();
      }
    } );
  }

  private void doPaste()
  {
    Clipboard clipboard = editor.util.EditorUtilities.getClipboard();
    int index = _selectionList.getSelectedIndex();
    String selection = _copyBuffer.get( index );
    CopyBuffer.instance().notifyOfPaste( index );
    clipboard.setContents( new StringSelection( selection ), this );
    _textComponent.paste();
    setVisible( false );
  }

  private void selectNext()
  {
    if( _selectionList.getModel().getSize() > 0 )
    {
      _selectionList.setSelectedIndex( Math.min( _selectionList.getModel().getSize() - 1, _selectionList.getSelectedIndex() + 1 ) );
    }
  }

  private void selectPrevious()
  {
    if( _selectionList.getModel().getSize() > 0 )
    {
      _selectionList.setSelectedIndex( Math.max( 0, _selectionList.getSelectedIndex() - 1 ) );
    }
  }

  private Object[] truncateList( List<String> strings )
  {
    List<String> returnList = new ArrayList<String>();
    for( String s : strings )
    {
      String str = GosuStringUtil.isWhitespace( s ) ? s : GosuStringUtil.strip( s );
      if( str.indexOf( "\n" ) >= 0 )
      {
        String[] strings1 = str.split( "\n" );
        if( strings1.length > 0 )
        {
          str = strings1[0];
        }
        else
        {
          str = " ";
        }
      }
      returnList.add( elide( str ) );
    }
    return returnList.toArray();
  }

  private String elide( String str )
  {
    return str.length() > 50 ? str.substring( 0, 50 ) + "..." : str;
  }

  public void valueChanged( ListSelectionEvent e )
  {
    int index = _selectionList.getSelectedIndex();
    String string = _copyBuffer.get( index );
    _textArea.setText( string );
  }

  public void lostOwnership( Clipboard clipboard, Transferable contents )
  {
  }

  private static class CustomListCellRenderer extends DefaultListCellRenderer
  {
    public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus )
    {
      JComponent cellRendererComponent = (JComponent)super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );
      cellRendererComponent.setBorder( BorderFactory.createEmptyBorder( 2, 4, 2, 4 ) );
      return cellRendererComponent;
    }
  }

  @Override
  public void setVisible( boolean visible )
  {
    if( visible )
    {
      _splitPane.setDividerLocation( 50 );
      if( _selectionList.getModel().getSize() > 0 )
      {
        _selectionList.setSelectedIndex( 0 );
      }
    }
    super.setVisible( visible );
  }
}