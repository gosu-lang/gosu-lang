package editor.run;

import editor.LabScheme;
import editor.Scheme;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.function.Consumer;
import java.util.function.Function;

/**
*/
class RemoteConfigPanel extends JPanel
{
  private final RemoteRunConfigParameters _params;
  private final Consumer<RemoteRunConfigParameters> _changeListener;
  private JTextField _editAgentArgs;
  private JLabel _labelHost;
  private JTextField _editHost;
  private JLabel _labelPort;
  private JTextField _editName;
  private JLabel _labelAddress;
  private JTextField _editAddress;
  private JTextField _editPort;

  RemoteConfigPanel( RemoteRunConfigParameters params, Consumer<RemoteRunConfigParameters> changeListener )
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
    separator.setBorder( BorderFactory.createMatteBorder( 1, 0, 0, 0, Scheme.active().getControlShadow() ) );
    configPanel.add( separator, c );

    
    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.NONE;
    c.gridx = 0;
    c.gridy = iY++;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 0;
    c.weighty = 0;
    c.insets = new Insets( 10, 0, 2, 0 );
    label = new JLabel( "VM args required for the remote process:" );
    configPanel.add( label, c );

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = iY++;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = 0;
    c.insets = new Insets( 2, 0, 10, 0 );
    _editAgentArgs = new JTextField();
    _editAgentArgs.setBorder( BorderFactory.createEmptyBorder() );
    _editAgentArgs.setEditable( false );
    configPanel.add( _editAgentArgs, c );


    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.NONE;
    c.gridx = 0;
    c.gridy = iY;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.weightx = 0;
    c.weighty = 0;
    c.insets = new Insets( 10, 0, 10, 10 );
    label = new JLabel( "Transport:" );
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
    JComboBox<DebugTransport> cbTransport = new JComboBox<>( DebugTransport.values() );
    cbTransport.addActionListener( e -> {
      _params.setTransport( (DebugTransport)cbTransport.getSelectedItem() );
      updateTransportMessage();
      enableControls();
    } );
    EventQueue.invokeLater( () -> cbTransport.setSelectedItem( _params.getTransport() ) );
    configPanel.add( cbTransport, c );


    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.NONE;
    c.gridx = 0;
    c.gridy = iY;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.weightx = 0;
    c.weighty = 0;
    c.insets = new Insets( 10, 0, 10, 10 );
    _labelHost = new JLabel( "Host:" );
    configPanel.add( _labelHost, c );

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 1;
    c.gridy = iY++;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = 0;
    c.insets = new Insets( 10, 0, 10, 0 );
    _editHost = new JTextField();
    _editHost.setText( _params.getHost() );
    _editHost.getDocument().addDocumentListener( new DocChangeHandler( this::validateHost, _params::setHost ) );
    configPanel.add( _editHost, c );

    
    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.NONE;
    c.gridx = 0;
    c.gridy = iY;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.weightx = 0;
    c.weighty = 0;
    c.insets = new Insets( 10, 0, 10, 10 );
    _labelPort = new JLabel( "Port:" );
    configPanel.add( _labelPort, c );

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 1;
    c.gridy = iY++;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 0;
    c.weighty = 0;
    c.insets = new Insets( 10, 0, 10, 0 );
    _editPort = new JTextField();
    _editPort.setText( _params.getPort() );
    _editPort.getDocument().addDocumentListener( new DocChangeHandler( this::validatePort, _params::setPort ) );
    configPanel.add( _editPort, c );

    
    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.NONE;
    c.gridx = 0;
    c.gridy = iY;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.weightx = 0;
    c.weighty = 0;
    c.insets = new Insets( 10, 0, 10, 10 );
    _labelAddress = new JLabel( "Address:" );
    configPanel.add( _labelAddress, c );

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 1;
    c.gridy = iY++;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = 0;
    c.insets = new Insets( 10, 0, 10, 0 );
    _editAddress = new JTextField();
    _editAddress.setText( _params.getAddress() );
    _editAddress.getDocument().addDocumentListener( new DocChangeHandler( this::validateName, _params::setAddress ) );
    configPanel.add( _editAddress, c );


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

  private void enableControls()
  {
    DebugTransport transport = _params.getTransport();
    switch( transport )
    {
      case AttachingSocket:
      case ListeningSocket:
        _labelAddress.setEnabled( false );
        _editAddress.setEnabled( false );
        _labelHost.setEnabled( true );
        _editHost.setEnabled( true );
        _labelPort.setEnabled( true );
        _editPort.setEnabled( true );
        break;
      case AttachingMemory:
      case ListeningMemory:
        _labelAddress.setEnabled( true );
        _editAddress.setEnabled( true );
        _labelHost.setEnabled( false );
        _editHost.setEnabled( false );
        _labelPort.setEnabled( false );
        _editPort.setEnabled( false );
        break;
    }
  }

  private void updateTransportMessage()
  {
    DebugTransport transport = _params.getTransport();
    String agentArgs = transport.getAgentArgs();
    if( agentArgs.contains( "$host" ) )
    {
      String host = _params.getHost();
      if( transport == DebugTransport.ListeningSocket )
      {
        try
        {
          host = InetAddress.getLocalHost().getCanonicalHostName();
        }
        catch( UnknownHostException e )
        {
          throw new RuntimeException( e );
        }
      }
      else
      {
        if( host == null || host.isEmpty() )
        {
          host = "<host>";
        }
      }
      agentArgs = agentArgs.replace( "$host", host );
    }
    if( agentArgs.contains( "$port" ) )
    {
      String port = _params.getPort();
      if( port == null || port.isEmpty() )
      {
        port = "<port>";
      }
      agentArgs = agentArgs.replace( "$port", port );
    }
    if( agentArgs.contains( "$address" ) )
    {
      String address = _params.getAddress();
      if( address == null || address.isEmpty() )
      {
        address = "<name>";
      }
      agentArgs = agentArgs.replace( "$address", address );
    }
    _editAgentArgs.setText( agentArgs );
  }

  private String validateName( String text )
  {
    if( text == null || text.isEmpty() )
    {
      return "ERROR";
    }
    return null;
  }

  private String validateHost( String text )
  {
    if( text == null || text.isEmpty() )
    {
      return "ERROR";
    }
    return null;
  }

  private String validatePort( String text )
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
        updateTransportMessage();
        _changeListener.accept( _params );
      }
      catch( BadLocationException e1 )
      {
        throw new RuntimeException( e1 );
      }
    }

  }
}
