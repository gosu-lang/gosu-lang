/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.runner;

import com.intellij.ide.util.BrowseFilesListener;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.FieldPanel;
import com.intellij.ui.RawCommandLineEditor;
import gw.plugin.ij.filetypes.GosuCodeFileType;
import gw.plugin.ij.filetypes.GosuProgramFileProvider;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class GosuRunConfigurationEditor extends SettingsEditor<GosuProgramRunConfiguration> {
  private DefaultComboBoxModel myModulesModel;
  private JComboBox _modulesBox;
  private JPanel _panel;
  private RawCommandLineEditor _editorVmParameters;
  private RawCommandLineEditor _editorMyParameters;
  private JPanel _scriptPathPanel;
  private JPanel _workDirPanel;
  private JTextField scriptPathField;
  private JTextField workDirField;

  public GosuRunConfigurationEditor() {
    createCenterPanel();
  }

  private void createCenterPanel()
  {
    _panel = new JPanel( new GridBagLayout() );
    _panel.setBorder( BorderFactory.createEmptyBorder( 8, 8, 8, 8 ) );
    GridBagConstraints c = new GridBagConstraints();

    int iY = 0;

    _scriptPathPanel = new JPanel( new BorderLayout() );

    JLabel labelModule = new JLabel( "Module:" );
    _modulesBox = new JComboBox();

    JLabel labelVMParameters = new JLabel( "VM Parameters:" );
    _editorVmParameters = new RawCommandLineEditor();

    JLabel labelParameters = new JLabel( "Program Parameters:" );
    _editorMyParameters = new RawCommandLineEditor();

    _workDirPanel = new JPanel( new BorderLayout() );

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = iY++;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.weightx = 0;
    c.weighty = 0;
    c.insets = new Insets( 2, 2, 0, 0 );
    _panel.add( _scriptPathPanel, c );

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.NONE;
    c.gridx = 0;
    c.gridy = iY++;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.weightx = 0;
    c.weighty = 0;
    c.insets = new Insets( 2, 2, 0, 0 );
    _panel.add( labelModule, c );

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = iY++;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = 1;
    c.insets = new Insets( 2, 2, 0, 0 );
    _panel.add( _modulesBox, c );

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.NONE;
    c.gridx = 0;
    c.gridy = iY++;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.weightx = 0;
    c.weighty = 0;
    c.insets = new Insets( 2, 2, 0, 0 );
    _panel.add( labelVMParameters, c );

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.BOTH;
    c.gridx = 0;
    c.gridy = iY++;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = 0;
    c.insets = new Insets( 2, 2, 0, 0 );
    _panel.add( _editorVmParameters, c );

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.NONE;
    c.gridx = 0;
    c.gridy = iY++;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.weightx = 0;
    c.weighty = 0;
    c.insets = new Insets( 2, 2, 0, 0 );
    _panel.add( labelParameters, c );

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.BOTH;
    c.gridx = 0;
    c.gridy = iY++;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = 0;
    c.insets = new Insets( 2, 2, 0, 0 );
    _panel.add( _editorMyParameters, c );

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.NONE;
    c.gridx = 0;
    c.gridy = iY++;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.weightx = 0;
    c.weighty = 0;
    c.insets = new Insets( 4, 2, 0, 0 );
    _panel.add( _workDirPanel, c );

    scriptPathField = new JTextField();
    final BrowseFilesListener scriptBrowseListener = new BrowseFilesListener(scriptPathField,
        "Program Path",
        "Specify path to program",
        new FileChooserDescriptor(true, false, false, false, false, false) {
          public boolean isFileSelectable(VirtualFile file) {
            return GosuProgramFileProvider.isProgram(file);
          }
        });
    final FieldPanel scriptFieldPanel = new FieldPanel(scriptPathField, "Program path:", null, scriptBrowseListener, null);
    _scriptPathPanel.setLayout(new BorderLayout());
    _scriptPathPanel.add(scriptFieldPanel, BorderLayout.CENTER);

    workDirField = new JTextField();
    final BrowseFilesListener workDirBrowseFilesListener = new BrowseFilesListener(workDirField,
        "Working directory",
        "Specify working directory",
        BrowseFilesListener.SINGLE_DIRECTORY_DESCRIPTOR);
    final FieldPanel workDirFieldPanel = new FieldPanel(workDirField, "Working directory:", null, workDirBrowseFilesListener, null);
    _workDirPanel.setLayout(new BorderLayout());
    _workDirPanel.add(workDirFieldPanel, BorderLayout.CENTER);
  }

  public void resetEditorFrom(@NotNull GosuProgramRunConfiguration configuration) {
    _editorVmParameters.setDialogCaption("VM Parameters");
    _editorVmParameters.setText(configuration._vmParams);

    _editorMyParameters.setDialogCaption("Program Parameters");
    _editorMyParameters.setText(configuration._strProgramParams);

    scriptPathField.setText(configuration._strProgramPath);
    workDirField.setText(configuration.getWorkDir());

    myModulesModel.removeAllElements();
    for (Module module : configuration.getValidModules()) {
      myModulesModel.addElement(module);
    }
    myModulesModel.setSelectedItem(configuration.getModule());
  }

  public void applyEditorTo(@NotNull GosuProgramRunConfiguration configuration) throws ConfigurationException {
    configuration.setModule((Module) _modulesBox.getSelectedItem());
    configuration._vmParams = _editorVmParameters.getText();
    configuration._strProgramParams = _editorMyParameters.getText();
    configuration._strProgramPath = scriptPathField.getText();
    configuration.setWorkDir(workDirField.getText());
  }

  @NotNull
  public JComponent createEditor() {
    myModulesModel = new DefaultComboBoxModel();
    _modulesBox.setModel(myModulesModel);

    _modulesBox.setRenderer(new DefaultListCellRenderer() {
      @NotNull
      public Component getListCellRendererComponent(JList list, final Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        final Module module = (Module) value;
        if (module != null) {
          setIcon(ModuleType.get(module).getNodeIcon(false));
          setText(module.getName());
        }
        return this;
      }
    });

    return _panel;
  }

  public void disposeEditor() {
  }
}
