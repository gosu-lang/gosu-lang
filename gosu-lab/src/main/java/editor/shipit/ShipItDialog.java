package editor.shipit;

import editor.GotoProgramTypePopup;
import editor.IHandleCancel;
import editor.LabFrame;
import editor.Scheme;
import editor.util.EditorUtilities;
import editor.util.Experiment;
import editor.util.LabButton;
import editor.util.LabCheckbox;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuProgram;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

/**
 */
public class ShipItDialog extends JDialog implements IHandleCancel
{
  private String _strProgramName;
  private boolean _bBundleGosu;
  private Experiment _experiment;
  private LabCheckbox _cbBundleGosu;
  private LabCheckbox _cbPrecompile;
  private JTextField _fieldProgramName;
  private JButton _btnOk;
  private JLabel _errorMsg;

  public ShipItDialog( Experiment experiment )
  {
    super( LabFrame.instance(), "Ship It!", true );
    setIconImage( EditorUtilities.loadIcon( "images/shipit.png" ).getImage() );
    _experiment = experiment;
    configUi();
  }

  public boolean isBundleGosu()
  {
    return _bBundleGosu;
  }

  public String getProgramName()
  {
    return _strProgramName;
  }

  protected void configUi()
  {
    JComponent contentPane = (JComponent)getContentPane();
    contentPane.setBorder( BorderFactory.createEmptyBorder( 8, 8, 8, 8 ) );
    contentPane.setLayout( new BorderLayout() );

    JPanel mainPanel = new JPanel( new GridBagLayout() );
    mainPanel.setBorder( BorderFactory.createCompoundBorder( BorderFactory.createLineBorder( Scheme.active().getScrollbarBorderColor() ),
                                                             BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) ) );

    final GridBagConstraints c = new GridBagConstraints();

    int iY = 0;

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = iY++;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = 0;
    c.insets = new Insets( 10, 2, 12, 0 );
    mainPanel.add( new JLabel( "<html>Ready to release your experiment? Create an all-in-one <i>executable</i> Jar! " +
                               "Your entire experiment, including dependencies, is bundled up in a single Jar file. " +
                               "The file directly executes on any machine with Java 8 or later installed." ), c );

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.NONE;
    c.gridx = 0;
    c.gridy = iY++;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = 0;
    c.insets = new Insets( 2, 2, 0, 0 );
    mainPanel.add( new JLabel( "What program do you want to launch from the Jar?", EditorUtilities.loadIcon( "images/program.png" ), SwingConstants.LEFT ), c );

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = iY;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = 0;
    c.insets = new Insets( 2, 2, 0, 0 );
    mainPanel.add( _fieldProgramName = new JTextField(), c );
    _fieldProgramName.getDocument().addDocumentListener( new DocListener() );

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.NONE;
    c.gridx = 1;
    c.gridy = iY++;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 0;
    c.weighty = 0;
    c.insets = new Insets( 2, 2, 0, 0 );
    JButton btn = new LabButton( "..." );
    btn.setToolTipText( "Find program" );
    btn.addActionListener( e -> displayProgramPopup() );
    mainPanel.add( btn, c );

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = iY++;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = 0;
    c.insets = new Insets( 2, 2, 0, 0 );
    _errorMsg = new JLabel( " " );
    mainPanel.add( _errorMsg, c );
    _errorMsg.setForeground( new Color( 100, 0, 0 ) );

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.NONE;
    c.gridx = 0;
    c.gridy = iY++;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = 0;
    c.insets = new Insets( 2, 2, 0, 0 );
    mainPanel.add( _cbBundleGosu = new LabCheckbox( "Bundle Gosu runtime", true ), c );
    _cbBundleGosu.setToolTipText( "<html>This option bundles a copy of Gosu in your jar.  Without this option your jar<br>" +
                                  "uses a compatible version of Gosu installed on the user's machine, if one <br>" +
                                  "exits.  Otherwise, the jar automatically installs Gosu after prompting for <br>" +
                                  "approval.  Subsequent execution of the jar uses the newly installed Gosu." );

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.NONE;
    c.gridx = 0;
    c.gridy = iY++;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = 0;
    c.insets = new Insets( 2, 2, 0, 0 );
    mainPanel.add( _cbPrecompile = new LabCheckbox( "Precompile Source", true ), c );
    _cbPrecompile.setToolTipText( "<html>This option fully compiles your experiment and includes resulting .class files<br>" +
                                  "in your jar.  The upside: your experiment may load faster; the downside: your<br>" +
                                  "jar file will be a little larger.  Note precompilation is optional since Gosu's<br>" +
                                  "runtime <i>dynamically compiles</i> source if not already compiled." );

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = iY++;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = 0;
    c.insets = new Insets( 20, 2, 8, 0 );
    mainPanel.add( new JLabel( "<html>The resulting <b>" + getExperiment().getName() + ".jar</b> file will be placed in the experiment's root folder.<br>" +
                               "You can run the file by typing the following on a command line:<br>" +
                               "&nbsp;&nbsp;&nbsp;&nbsp;<pre>java -jar " + getExperiment().getName() + ".jar</pre><br>" +
                               "You can also run the jar directly from your file manager or command shell.  Note<br>" +
                               "you can distribute your jar on the web using a file hosting service like Dropbox,<br>" +
                               "Google Drive, etc." ), c );

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.BOTH;
    c.gridx = 0;
    c.gridy = iY;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = GridBagConstraints.REMAINDER;
    c.weightx = 1;
    c.weighty = 1;
    c.insets = new Insets( 2, 2, 0, 0 );
    mainPanel.add( new JPanel(), c );

    contentPane.add( mainPanel, BorderLayout.CENTER );


    JPanel south = new JPanel( new BorderLayout() );
    south.setBorder( BorderFactory.createEmptyBorder( 4, 0, 0, 0 ) );
    JPanel filler = new JPanel();
    south.add( filler, BorderLayout.CENTER );

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout( new BoxLayout( buttonPanel, BoxLayout.X_AXIS ) );

    _btnOk = new LabButton( "Make Executable Jar" );
    _btnOk.setMnemonic( 'M' );
    _btnOk.addActionListener(
      e -> {
        doOKAction();
        close();
      } );
    buttonPanel.add( _btnOk );
    getRootPane().setDefaultButton( _btnOk );
    _btnOk.setEnabled( false );

    JButton btnCancel = new LabButton( "Cancel" );
    btnCancel.addActionListener( e -> close() );
    buttonPanel.add( btnCancel );

    south.add( buttonPanel, BorderLayout.EAST );
    contentPane.add( south, BorderLayout.SOUTH );

    mapCancelKeystroke( "Cancel", this::close );

    setSize( 540, 465 );

    EditorUtilities.centerWindowInFrame( this, getOwner() );
  }

  private void displayProgramPopup()
  {
    GotoProgramTypePopup.display( _fieldProgramName, _fieldProgramName.getText(), this::updateProgramName );
  }

  private void updateProgramName( String fqn )
  {
    _fieldProgramName.setText( fqn );
  }

  private Experiment getExperiment()
  {
    return _experiment;
  }

  protected void doOKAction()
  {
    _strProgramName = _fieldProgramName.getText();
    _bBundleGosu = _cbBundleGosu.isSelected();
  }

  private void close()
  {
    dispose();
  }

  public boolean isPrecompiled()
  {
    return _cbPrecompile.isSelected();
  }

  private class DocListener implements DocumentListener
  {
    @Override
    public void insertUpdate( DocumentEvent e )
    {
      update( _fieldProgramName.getText() );
    }

    @Override
    public void removeUpdate( DocumentEvent e )
    {
      update( _fieldProgramName.getText() );
    }

    @Override
    public void changedUpdate( DocumentEvent e )
    {
      update( _fieldProgramName.getText() );
    }

    private void update( String fqn )
    {
      if( fqn == null || fqn.isEmpty() )
      {
        _btnOk.setEnabled( false );
        _errorMsg.setText( " " );

      }
      else
      {
        IType type = TypeSystem.getByFullNameIfValid( fqn );
        if( type instanceof IGosuProgram )
        {
          _fieldProgramName.setForeground( Scheme.active().getWindowText() );
          _btnOk.setEnabled( true );
          _errorMsg.setText( " " );
        }
        else
        {
          _fieldProgramName.setForeground( new Color( 200, 0, 0 ) );
          _btnOk.setEnabled( false );
          _errorMsg.setText( "<html>Please enter a valid <b><i>program</i></b> name" );
        }
      }
      revalidate();
    }
  }
}


