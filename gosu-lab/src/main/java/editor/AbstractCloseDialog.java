package editor;

import editor.util.EditorUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public abstract class AbstractCloseDialog extends JDialog
{
  protected JButton _btnClose;

  public AbstractCloseDialog( String strCaption )
  {
    super( RunMe.getEditorFrame(), strCaption, true );
    init();
  }

  public AbstractCloseDialog( Frame owner, String title, boolean modal )
  {
    super( owner, title, modal );
    init();
  }

  private void init()
  {
    setDefaultCloseOperation( JDialog.DO_NOTHING_ON_CLOSE );
    addWindowListener(
      new WindowAdapter()
      {
        public void windowClosing( WindowEvent e )
        {
          close();
        }
      } );
  }

  abstract protected JComponent createCenterPanel();

  protected void close()
  {
    dispose();
  }

  protected void configUi()
  {
    JComponent contentPane = (JComponent)getContentPane();
    contentPane.setLayout( new BorderLayout() );
    contentPane.setBorder( BorderFactory.createEmptyBorder( 8, 8, 8, 8 ) );

    JComponent centerPanel = createCenterPanel();
    JComponent buttonPanel = createButtonPanel();

    contentPane.add( centerPanel, BorderLayout.CENTER );
    contentPane.add( buttonPanel, BorderLayout.SOUTH );

    setInitialSize();

    EditorUtilities.centerWindowInFrame( this, getOwner() );
  }

  protected void setInitialSize()
  {
    pack();
  }

  protected JComponent createButtonPanel()
  {
    JPanel btnPanel = new JPanel();
    btnPanel.setBorder( BorderFactory.createEmptyBorder( 4, 0, 0, 0 ) );
    FlowLayout layout = new FlowLayout();
    layout.setVgap( 0 );
    btnPanel.setLayout( layout );
    _btnClose = createCloseButton();
    getRootPane().setDefaultButton( _btnClose );
    _btnClose.addActionListener( e -> close() );
    mapCancelKeystroke();

    btnPanel.add( _btnClose );

    return btnPanel;
  }

  protected boolean onCancel()
  {
    return true;
  }

  protected JButton createCloseButton()
  {
    return new JButton( "Close" );
  }

  public void enableCloseButton( boolean bEnable )
  {
    _btnClose.setEnabled( bEnable );
  }

  private void mapCancelKeystroke()
  {
    Object key = getRootPane().getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).get( KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 ) );
    if( key == null )
    {
      key = "Close";
      getRootPane().getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put( KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 ), key );
    }
    getRootPane().getActionMap().put(
      key,
      new AbstractAction()
      {
        public void actionPerformed( ActionEvent e )
        {
          close();
        }
      } );
  }
}
