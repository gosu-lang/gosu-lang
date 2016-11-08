package editor.util;

import editor.EditorHost;
import editor.LabFrame;
import editor.Scheme;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

/**
 */
public class LabStatusBar extends JPanel
{
  private final CaretHandler _caretListener;
  private JLabel _selectionInfo;
  private JLabel _coords;
  private JLabel _status;
  private EditorHost _editor;

  public LabStatusBar()
  {
    _caretListener = new CaretHandler();
    configUi();
    EventQueue.invokeLater( () -> LabFrame.instance().getGosuPanel().getEditorTabPane().addSelectionListener( e -> update() ) );
  }

  protected void configUi()
  {
    setBackground( Scheme.active().getMenu() );
    setBorder( BorderFactory.createEmptyBorder( 2, 2, 2, 2 ) );
    setLayout( new GridBagLayout() );

    final GridBagConstraints c = new GridBagConstraints();

    int iX = 0;

    c.anchor = GridBagConstraints.EAST;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = iX++;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = 0;
    c.insets = new Insets( 0, 2, 0, 10 );
    _status = new JLabel();
    add( _status, c );

    c.anchor = GridBagConstraints.EAST;
    c.fill = GridBagConstraints.NONE;
    c.gridx = iX++;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.weightx = 0;
    c.weighty = 0;
    c.insets = new Insets( 0, 2, 0, 2 );
    _selectionInfo = new JLabel();
    _selectionInfo.setMinimumSize( new Dimension( 20, _selectionInfo.getFontMetrics( getFont() ).getHeight() + _selectionInfo.getInsets().top * 2 ) );
    _selectionInfo.setBorder( BorderFactory.createCompoundBorder( BorderFactory.createMatteBorder( 0, 1, 0, 0, Scheme.active().getControlShadow() ), BorderFactory.createEmptyBorder( 0, 8, 0, 8 ) ) );
    _selectionInfo.setToolTipText( "Selection length" );
    add( _selectionInfo, c );

    c.anchor = GridBagConstraints.EAST;
    c.fill = GridBagConstraints.NONE;
    c.gridx = iX;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.weightx = 0;
    c.weighty = 0;
    c.insets = new Insets( 0, 2, 0, 2 );
    _coords = new JLabel();
    _coords.setMinimumSize( new Dimension( 20, _coords.getFontMetrics( getFont() ).getHeight() + _coords.getInsets().top * 2 ) );
    _coords.setBorder( BorderFactory.createCompoundBorder( BorderFactory.createMatteBorder( 0, 1, 0, 0, Scheme.active().getControlShadow() ), BorderFactory.createEmptyBorder( 0, 8, 0, 8 ) ) );
    _coords.setToolTipText( "Line:Column location" );
    add( _coords, c );


    EventQueue.invokeLater( this::update );
  }

  public void setStatus( String status )
  {
    _status.setText( status );
    _status.revalidate();
    _status.repaint();
  }

  private void update()
  {
    if( _editor != null )
    {
      _editor.getEditor().removeCaretListener( _caretListener );
      _editor.getEditor().removeMouseMotionListener( _caretListener );
    }

    _editor = LabFrame.instance().getGosuPanel().getCurrentEditor();
    if( _editor == null )
    {
      _coords.setText( "" );
      _selectionInfo.setText( "" );
    }
    else
    {
      _editor.getEditor().addCaretListener( _caretListener );
      _editor.getEditor().addMouseMotionListener( _caretListener );

      updateCaret( _editor );
    }
  }

  private void updateCaret( EditorHost editor )
  {
    _coords.setText( makeCoords( editor ) );
    _selectionInfo.setText( makeSelectionInfo( editor ) );
  }

  private String makeSelectionInfo( EditorHost editor )
  {
    int start = _editor.getEditor().getSelectionStart();
    int end = editor.getEditor().getSelectionEnd();
    int len = end - start;
    if( len == 0 )
    {
      return "";
    }
    String chars = String.valueOf( len ) + " char" + (len > 1 ? "s" : "");
    int startLine = editor.getEditor().getDocument().getDefaultRootElement().getElementIndex( start );
    int endLine = editor.getEditor().getDocument().getDefaultRootElement().getElementIndex( end );
    int numLines = endLine - startLine + 1;
    String lines = numLines == 1 ? "" : ", " + numLines + " lines";
    return chars + lines;
  }

  private String makeCoords( EditorHost editor )
  {
    int line = editor.getLineNumberAtCaret();
    int column = editor.getEditor().getCaretPosition() - editor.getLineOffset( line - 1 );
    return String.valueOf( line ) + ":" + String.valueOf( column + 1 );
  }

  private class CaretHandler extends MouseAdapter implements CaretListener
  {
    @Override
    public void mouseDragged( MouseEvent e )
    {
      caretUpdate( null );
    }

    @Override
    public void caretUpdate( CaretEvent e )
    {
      updateCaret( LabFrame.instance().getGosuPanel().getCurrentEditor() );
    }
  }
}
