package editor;

import editor.util.EditorUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public abstract class AbstractCloseDialog extends JDialog implements IHandleCancel
{
  protected JButton _btnClose;

  public AbstractCloseDialog( String strCaption )
  {
    super( LabFrame.instance(), strCaption, true );
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
    mapCancelKeystroke( "Close", this::close );

    btnPanel.add( _btnClose );

    return btnPanel;
  }

  protected JButton createCloseButton()
  {
    return new JButton( "Close" );
  }
}
