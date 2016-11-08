package editor;

import editor.plugin.typeloader.ITypeFactory;
import editor.util.EditorUtilities;
import gw.lang.reflect.gs.ClassType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class NewIdentifierDialog extends JDialog implements IHandleCancel
{
  private IdentifierTextField _nameField;
  private String _name;

  public NewIdentifierDialog( ClassType classType )
  {
    super( (JFrame)KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow(), "New " + classType.keyword() + " Name", true );
    setIconImage( ((ImageIcon)EditorUtilities.findIcon( classType )).getImage() );
    configUI();
    addWindowListener( new WindowAdapter()
    {
      public void windowClosing( WindowEvent e )
      {
        dispose();
      }
    } );
  }

  public NewIdentifierDialog( ITypeFactory factory )
  {
    super( (JFrame)KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow(), "New " + factory.getName() + " Name", true );
    setIconImage( EditorUtilities.loadIcon( factory.getIcon() ).getImage() );
    configUI();
    addWindowListener( new WindowAdapter()
    {
      public void windowClosing( WindowEvent e )
      {
        dispose();
      }
    } );
  }

  public NewIdentifierDialog()
  {
    super( (JFrame)KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow(), "New Namespace Name", true );
    setIconImage( EditorUtilities.loadIcon( "images/folder.png" ).getImage() );
    configUI();
    addWindowListener( new WindowAdapter()
    {
      public void windowClosing( WindowEvent e )
      {
        dispose();
      }
    } );
  }

  protected void configUI()
  {
    JComponent contentPane = (JComponent)getContentPane();
    contentPane.setBorder( BorderFactory.createEmptyBorder( 8, 8, 8, 8 ) );
    contentPane.setLayout( new BorderLayout() );

    JPanel mainPanel = new JPanel( new BorderLayout() );
    _nameField = new IdentifierTextField( false, true );
    _nameField.setColumns( 30 );
    mainPanel.add( _nameField, BorderLayout.NORTH );
    mainPanel.add( new JPanel(), BorderLayout.CENTER );

    contentPane.add( mainPanel, BorderLayout.CENTER );

    JPanel south = new JPanel( new BorderLayout() );
    south.setBorder( BorderFactory.createEmptyBorder( 4, 0, 0, 0 ) );
    JPanel filler = new JPanel();
    south.add( filler, BorderLayout.CENTER );

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout( new BoxLayout( buttonPanel, BoxLayout.X_AXIS ) );

    JButton btnOk = new JButton( "OK" );
    btnOk.setMnemonic( 'O' );
    btnOk.addActionListener(
      e -> {
        _name = _nameField.getText();
        close();
      } );
    buttonPanel.add( btnOk );
    getRootPane().setDefaultButton( btnOk );

    JButton btnCancel = new JButton( "Cancel" );
    btnCancel.addActionListener( e -> close() );
    buttonPanel.add( btnCancel );

    south.add( buttonPanel, BorderLayout.EAST );
    contentPane.add( south, BorderLayout.SOUTH );

    mapCancelKeystroke( "Cancel", this::close );

    pack();

    EditorUtilities.centerWindowInFrame( this, getOwner() );
  }

  public String getClassName()
  {
    return _name;
  }

  private void close()
  {
    dispose();
  }
}
