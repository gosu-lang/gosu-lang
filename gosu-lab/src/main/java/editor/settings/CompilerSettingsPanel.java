package editor.settings;

import editor.LabFrame;
import editor.util.DirectoryEditor;
import gw.util.PathUtil;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
*/
class CompilerSettingsPanel extends JPanel
{
  private final CompilerSettingsParameters _params;
  private final Consumer<CompilerSettingsParameters> _changeListener;
  private JRadioButton _rbSourceBased;
  private JRadioButton _rbClassFileBased;
  private JLabel _labelOutputPath;
  private DirectoryEditor _editOutputPath;

  CompilerSettingsPanel( CompilerSettingsParameters params, Consumer<CompilerSettingsParameters> changeListener )
  {
    super( new BorderLayout() );
    _params = params;
    _changeListener = changeListener;
    configUi();
  }

  private void configUi()
  {
    JPanel configPanel = new JPanel( new GridBagLayout() );
    configPanel.setBorder( BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) );

    final GridBagConstraints c = new GridBagConstraints();

    int iY = 0;

    ButtonGroup group = new ButtonGroup();

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.NONE;
    c.gridx = 0;
    c.gridy = iY++;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 0;
    c.weighty = 0;
    c.insets = new Insets( 0, 0, 10, 0 );
    _rbSourceBased = new JRadioButton( "<html><b>Dynamic</b>.&nbsp;Incrementally&nbsp;compile&nbsp;and&nbsp;load&nbsp;classes&nbsp;direct&nbsp;from&nbsp;source&nbsp;at&nbsp;runtime." );
    _rbSourceBased.addItemListener( e -> {
      _params.setSourceBased( _rbSourceBased.isSelected() );
      _labelOutputPath.setEnabled( !_rbSourceBased.isSelected() );
      _editOutputPath.setEnabled( !_rbSourceBased.isSelected() );
    });
    group.add( _rbSourceBased );
    configPanel.add( _rbSourceBased, c );

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = iY++;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = 0;
    c.insets = new Insets( 10, 0, 0, 0 );
    _rbClassFileBased = new JRadioButton( "<html><b>Static</b>.&nbsp;Compile&nbsp;classes&nbsp;to&nbsp;disk&nbsp;during&nbsp;development." );
    _rbClassFileBased.addItemListener( e -> {
      _params.setSourceBased( _rbSourceBased.isSelected() );
      _labelOutputPath.setEnabled( !_rbSourceBased.isSelected() );
      _editOutputPath.setEnabled( !_rbSourceBased.isSelected() );
    });
    group.add( _rbClassFileBased );
    configPanel.add( _rbClassFileBased, c );

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.NONE;
    c.gridx = 0;
    c.gridy = iY;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.weightx = 0;
    c.weighty = 0;
    c.insets = new Insets( 5, 0, 10, 10 );
    _labelOutputPath = new JLabel( "Compiler output:" );
    configPanel.add( _labelOutputPath, c );

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 1;
    c.gridy = iY++;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = 0;
    c.insets = new Insets( 5, 0, 10, 0 );
    _editOutputPath = new DirectoryEditor( _labelOutputPath.getText(), PathUtil.getAbsolutePathName( PathUtil.create( _params.getOutputPath() ) ), LabFrame::instance );
    _editOutputPath.getDocument().addDocumentListener(
      new DocChangeHandler( this::validateOutputPath, ( path ) -> {
        String relativePath = LabFrame.instance().getGosuPanel().getExperiment().makeExperimentRelativePathWithSlashes( PathUtil.create( path ) );
        path = relativePath == null ? path : relativePath;
        _params.setOuputPath( path );
      } ) );
    configPanel.add( _editOutputPath, c );

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

    EventQueue.invokeLater( () -> {
      _rbSourceBased.setSelected( _params.isSourceBased() );
      _rbClassFileBased.setSelected( !_params.isSourceBased() );
    } );
  }

  private String validateOutputPath( String text )
  {
    if( text == null || text.isEmpty() )
    {
      return "ERROR";
    }
    return null;
  }

  private class DocChangeHandler implements DocumentListener
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
        _changeListener.accept( _params );
      }
      catch( BadLocationException e1 )
      {
        throw new RuntimeException( e1 );
      }
    }

  }
}
