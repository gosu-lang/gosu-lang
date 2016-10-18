package editor.util;

import editor.StringPopup;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 */
public class MoreTextEditor extends JPanel
{
  private JTextField _editText;
  private final boolean _bConvertNewLineToSpaace;
  private LabToolbarButton _btnDir;

  public MoreTextEditor( String title, boolean bConvertNewLineToSpace )
  {
    super( new GridBagLayout() );
    _bConvertNewLineToSpaace = bConvertNewLineToSpace;
    configUi( title );
  }

  public Document getDocument()
  {
    return _editText.getDocument();
  }

  public void setText( String vmArgs )
  {
    _editText.setText( vmArgs );
  }
  public String getText()
  {
    return _editText.getText();
  }

  public void setEnabled( boolean enabled )
  {
    super.setEnabled( enabled );
    _editText.setEnabled( enabled );
    _btnDir.setEnabled( enabled );
  }

  private void configUi( String title )
  {
    final GridBagConstraints c = new GridBagConstraints();

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = 0;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = 0;
    c.insets = new Insets( 0, 0, 0, 3 );
    _editText = new JTextField();
    add( _editText, c );

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.NONE;
    c.gridx = 1;
    c.gridy = 0;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 0;
    c.weighty = 0;
    c.insets = new Insets( 0, 0, 0, 0 );
    _btnDir = new LabToolbarButton(
      new AbstractAction( title, EditorUtilities.loadIcon( "images/ellipsis.png" ) ) {
        public void actionPerformed( ActionEvent e )
        {
          getMoreText( title );
        }
      } );
    add( _btnDir, c );

  }

  void getMoreText( String title )
  {
    StringPopup popup = new StringPopup( _editText.getText(), title, _editText, false, 5, 45 );
    popup.addNodeChangeListener(
      e -> {
        String text = e.getSource().toString();
        if( _bConvertNewLineToSpaace )
        {
          text = text.replace( '\n', ' ' );
        }
        _editText.setText( text );
        _editText.requestFocus();
        EditorUtilities.fixSwingFocusBugWhenPopupCloses( _editText );
        _editText.repaint();
      } );
    popup.show( this, 0, 0 );
    editor.util.EditorUtilities.centerWindowInFrame( popup, EditorUtilities.getWindow() );
  }
}
