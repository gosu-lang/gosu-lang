package editor.settings;

import editor.Scheme;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 */
class AppearanceSettingsPanel extends JPanel
{
  private final AppearanceSettingsParameters _params;
  private final Consumer<AppearanceSettingsParameters> _changeListener;
  private JComboBox<String> _comboTheme;

  AppearanceSettingsPanel( AppearanceSettingsParameters params, Consumer<AppearanceSettingsParameters> changeListener )
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
    c.insets = new Insets( 5, 0, 10, 10 );
    JLabel labelTheme = new JLabel( "Theme:" );
    configPanel.add( labelTheme, c );

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.NONE;
    c.gridx = 1;
    c.gridy = iY++;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = 0;
    c.insets = new Insets( 5, 0, 10, 0 );
    _comboTheme = new JComboBox<>( Scheme.SCHEMES_BY_NAME.keySet().toArray( new String[Scheme.SCHEMES_BY_NAME.size()] ) );
    _comboTheme.addActionListener( e -> _params.setTheme( (String)_comboTheme.getSelectedItem() ) );
    configPanel.add( _comboTheme, c );

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

    EventQueue.invokeLater( () -> _comboTheme.setSelectedItem( _params.getTheme() ) );
  }
}
