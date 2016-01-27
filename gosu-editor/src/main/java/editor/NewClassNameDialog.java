package editor;

import editor.search.StudioUtilities;
import editor.util.EditorUtilities;
import gw.lang.reflect.gs.ClassType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class NewClassNameDialog extends JDialog
{
  private JTextField _nameField;
  private String _name;

  public NewClassNameDialog( ClassType classType )
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

  protected void configUI()
  {
    JComponent contentPane = (JComponent)getContentPane();
    contentPane.setBorder( BorderFactory.createEmptyBorder( 8, 8, 8, 8 ) );
    contentPane.setLayout( new BorderLayout() );

    JPanel mainPanel = new JPanel( new BorderLayout() );
    _nameField = new JTextField( 30 );
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

    mapCancelKeystroke();

    pack();

    StudioUtilities.centerWindowInFrame( this, getOwner() );
  }

  private void mapCancelKeystroke()
  {
    Object key = getRootPane().getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).get( KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 ) );
    if( key == null )
    {
      key = "Cancel";
      getRootPane().getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put( KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 ), key );
    }
    getRootPane().getActionMap().put( key,
                                      new AbstractAction()
                                      {
                                        public void actionPerformed( ActionEvent e )
                                        {
                                          close();
                                        }
                                      } );
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
