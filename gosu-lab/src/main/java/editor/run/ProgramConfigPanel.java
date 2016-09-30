package editor.run;

import editor.GotoProgramTypePopup;
import editor.util.EditorUtilities;
import editor.util.LabCheckbox;
import editor.util.LabToolbarButton;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
*/
class ProgramConfigPanel extends JPanel
{
  private final ProgramRunConfigParameters _params;

  ProgramConfigPanel( ProgramRunConfigParameters params )
  {
    super( new BorderLayout() );
    _params = params;
    configUi();
  }

  private void configUi()
  {
    JPanel configPanel = new JPanel( new GridBagLayout() );
    configPanel.setBorder( BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) );

    final GridBagConstraints c = new GridBagConstraints();

    int iY = 0;

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.NONE;
    c.gridx = 0;
    c.gridy = iY;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.weightx = 0;
    c.weighty = 0;
    c.insets = new Insets( 10, 0, 10, 10 );
    JLabel label = new JLabel( "Name:" );
    configPanel.add( label, c );

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 1;
    c.gridy = iY++;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = 0;
    c.insets = new Insets( 10, 0, 10, 0 );
    JTextField editName = new JTextField();
    editName.setText( _params.getName() );
    editName.getDocument().addDocumentListener( new DocChangeHandler( this::validateName, _params::setName ) );
    configPanel.add( editName, c );


    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = iY++;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = 0;
    c.insets = new Insets( 10, 0, 10, 0 );
    JPanel separator = new JPanel();
    separator.setBorder( BorderFactory.createMatteBorder( 1, 0, 0, 0, EditorUtilities.CONTROL_SHADOW ) );
    configPanel.add( separator, c );

    
    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.NONE;
    c.gridx = 0;
    c.gridy = iY;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.weightx = 0;
    c.weighty = 0;
    c.insets = new Insets( 10, 0, 10, 10 );
    label = new JLabel( "Run type:" );
    configPanel.add( label, c );

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 1;
    c.gridy = iY;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = 0;
    c.insets = new Insets( 10, 0, 10, 0 );
    JTextField editFqn = new JTextField();
    editFqn.setText( _params.getFqn() );
    editFqn.getDocument().addDocumentListener( new DocChangeHandler( this::validateFqn, _params::setFqn ) );
    configPanel.add( editFqn, c );

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.NONE;
    c.gridx = 2;
    c.gridy = iY++;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 0;
    c.weighty = 0;
    c.insets = new Insets( 10, 2, 10, 0 );
    JButton btn = new LabToolbarButton( EditorUtilities.loadIcon( "images/program.png") );
    //noinspection SuspiciousNameCombination
    btn.setToolTipText( "Find program" );
    btn.addActionListener( e -> displayProgramPopup( editFqn ) );
    configPanel.add( btn, c );


    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.NONE;
    c.gridx = 0;
    c.gridy = iY;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.weightx = 0;
    c.weighty = 0;
    c.insets = new Insets( 10, 0, 10, 10 );
    label = new JLabel( "VM arguments:" );
    configPanel.add( label, c );

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 1;
    c.gridy = iY++;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = 0;
    c.insets = new Insets( 10, 0, 10, 0 );
    JTextField editVmArgs = new JTextField();
    editVmArgs.setText( _params.getVmArgs() );
    editVmArgs.getDocument().addDocumentListener( new DocChangeHandler( this::validateVmArgs, _params::setVmArgs ) );
    configPanel.add( editVmArgs, c );

    
    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.NONE;
    c.gridx = 0;
    c.gridy = iY;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.weightx = 0;
    c.weighty = 0;
    c.insets = new Insets( 10, 0, 10, 10 );
    label = new JLabel( "Program arguments:" );
    configPanel.add( label, c );

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 1;
    c.gridy = iY++;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = 0;
    c.insets = new Insets( 10, 0, 10, 0 );
    JTextField editProgArgs = new JTextField();
    editProgArgs.setText( _params.getProgArgs() );
    editProgArgs.getDocument().addDocumentListener( new DocChangeHandler( this::validateProgArgs, _params::setProgArgs ) );
    configPanel.add( editProgArgs, c );


    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.NONE;
    c.gridx = 0;
    c.gridy = iY;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.weightx = 0;
    c.weighty = 0;
    c.insets = new Insets( 10, 0, 10, 10 );
    label = new JLabel( "Working directory:" );
    configPanel.add( label, c );

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 1;
    c.gridy = iY++;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = 0;
    c.insets = new Insets( 10, 0, 10, 0 );
    JTextField editWorkingDir = new JTextField();
    editWorkingDir.setText( _params.getWorkingDir() );
    editWorkingDir.getDocument().addDocumentListener( new DocChangeHandler( this::validateWorkingDir, _params::setWorkingDir) );
    configPanel.add( editWorkingDir, c );


    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.NONE;
    c.gridx = 0;
    c.gridy = iY;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.weightx = 0;
    c.weighty = 0;
    c.insets = new Insets( 10, 0, 10, 10 );
    LabCheckbox cbJre = new LabCheckbox( "Use alternate JRE" );
    cbJre.setSelected( _params.isJreEnabled() );
    cbJre.addChangeListener( e -> _params.setJreEnabled( cbJre.isSelected() ) );
    configPanel.add( cbJre, c );

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 1;
    c.gridy = iY++;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = 0;
    c.insets = new Insets( 10, 0, 10, 0 );
    JTextField editJre = new JTextField();
    editJre.setText( _params.getJre() );
    editJre.getDocument().addDocumentListener( new DocChangeHandler( this::validateJre, _params::setJre ) );
    editJre.setEnabled( _params.isJreEnabled() );
    cbJre.addChangeListener( e -> editJre.setEnabled( cbJre.isSelected() ) );
    configPanel.add( editJre, c );


    // Bottom Filler
    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.BOTH;
    c.gridx = 0;
    c.gridy = iY;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = GridBagConstraints.REMAINDER;
    c.weightx = 1;
    c.weighty = 1;
    configPanel.add( new JPanel(), c );


    add( configPanel, BorderLayout.CENTER );
  }

  private void displayProgramPopup( JTextComponent fieldRunType )
  {
    GotoProgramTypePopup.display( fieldRunType, fieldRunType.getText(), fieldRunType::setText );
  }

  private String validateName( String text )
  {
    if( text == null || text.isEmpty() )
    {
      return "ERROR";
    }
    return null;
  }

  private String validateFqn( String text )
  {
    if( text == null || text.isEmpty() )
    {
      return "ERROR";
    }
    return null;
  }

  private String validateVmArgs( String text )
  {
    if( text == null || text.isEmpty() )
    {
      return "ERROR";
    }
    return null;
  }

  private String validateProgArgs( String text )
  {
    if( text == null || text.isEmpty() )
    {
      return "ERROR";
    }
    return null;
  }

  private String validateWorkingDir( String text )
  {
    if( text == null || text.isEmpty() )
    {
      return "ERROR";
    }
    return null;
  }

  private String validateJre( String text )
  {
    if( text == null || text.isEmpty() )
    {
      return "ERROR";
    }
    return null;
  }


  private static class DocChangeHandler implements DocumentListener
  {
    Function<String, String> _validator;
    Consumer<String> _consumer;

    public DocChangeHandler( Function<String, String> validator, Consumer<String> consumer )
    {
      _validator = validator;
      _consumer = consumer;
    }

    @Override
    public void insertUpdate( DocumentEvent e )
    {
      update( e );
    }

    @Override
    public void removeUpdate( DocumentEvent e )
    {
      update( e );
    }

    @Override
    public void changedUpdate( DocumentEvent e )
    {
      update( e );
    }

    private void update( DocumentEvent e )
    {
      Document doc = e.getDocument();
      try
      {
        String text = doc.getText( 0, doc.getLength() );

        String error = _validator.apply( text );
        if( error != null )
        {
          //## todo: display error
          //reject( error );
        }
        _consumer.accept( text );
      }
      catch( BadLocationException e1 )
      {
        throw new RuntimeException( e1 );
      }
    }

  }
}
