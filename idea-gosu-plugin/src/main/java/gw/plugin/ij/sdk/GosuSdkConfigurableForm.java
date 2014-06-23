/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.sdk;

import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkAdditionalData;
import com.intellij.openapi.projectRoots.SdkModel;
import com.intellij.openapi.projectRoots.SdkModificator;
import com.intellij.openapi.util.Comparing;
import gw.lang.GosuVersion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class GosuSdkConfigurableForm {
  @NotNull
  private final DefaultComboBoxModel _jdksModel;
  @NotNull
  private final SdkModel _sdkModel;
  private JTextField _editCurrentJdk;
  private JTextField _fieldGosuVersion;
  private Sdk _sdk;

  public GosuSdkConfigurableForm(@NotNull SdkModel sdkModel, @NotNull final SdkModificator sdkModificator) {
    _sdkModel = sdkModel;
    _jdksModel = new DefaultComboBoxModel();
  }

  @NotNull
  public JPanel getContentPanel() {
    JPanel panel = new JPanel( new GridBagLayout() );
    //panel.setBorder( BorderFactory.createEmptyBorder( 8, 8, 8, 8 ) );
    GridBagConstraints c = new GridBagConstraints();

    int iY = 0;

    JLabel labelGosuVersion = new JLabel( "Gosu Version:" );
    _fieldGosuVersion = new JTextField();
    _fieldGosuVersion.setEditable(false);

    JLabel labelCurrentJDK = new JLabel( "JDK Used:" );
    _editCurrentJdk = new JTextField();

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.NONE;
    c.gridx = 0;
    c.gridy = iY;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.weightx = 0;
    c.weighty = 0;
    c.insets = new Insets( 2, 2, 2, 2 );
    panel.add( labelGosuVersion, c );

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 1;
    c.gridy = iY++;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = 1;
    c.insets = new Insets( 2, 2, 2, 2 );
    panel.add( _fieldGosuVersion, c );

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.NONE;
    c.gridx = 0;
    c.gridy = iY;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.weightx = 0;
    c.weighty = 0;
    c.insets = new Insets( 2, 2, 2, 2 );
    panel.add( labelCurrentJDK, c );

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 1;
    c.gridy = iY++;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = 0;
    c.insets = new Insets( 2, 2, 2, 2 );
    panel.add( _editCurrentJdk, c );

    return panel;
  }

  @Nullable
  public Sdk getSelectedSdk() {
//    return (Sdk) myInternalJdkComboBox.getSelectedItem();
    return _sdk;
  }

  public GosuVersion getGosuVersion() {
    return GosuVersion.parse( _fieldGosuVersion.getText() );
  }

  public void init(@NotNull Sdk jdk, @Nullable Sdk androidSdk) {
    updateJdks();

//    if (androidSdk != null) {
//      for (int i = 0; i < myJdksModel.getSize(); i++) {
//        if (Comparing.strEqual(((Sdk) myJdksModel.getElementAt(i)).getName(), jdk.getName())) {
//          myInternalJdkComboBox.setSelectedIndex(i);
//          break;
//        }
//      }
//    }

    String strSdkLocation = androidSdk != null ? androidSdk.getHomePath() : null;
    this._sdk = jdk;
    _editCurrentJdk.setText(jdk.getName() + " (" + jdk.getHomePath() + ")");

    SdkAdditionalData sdkAdditionalData = androidSdk == null ? null : androidSdk.getSdkAdditionalData();
    if (sdkAdditionalData instanceof GosuSdkAdditionalData) {
      GosuSdkAdditionalData gosuSdkData = (GosuSdkAdditionalData) sdkAdditionalData;
      GosuVersion version = gosuSdkData.getGosuVersion();
      _fieldGosuVersion.setText(version != null ? version.toString() : "");
    }

//    updateBuildTargets(androidSdkObject);
//    if (buildTarget != null) {
//      for (int i = 0; i < myBuildTargetsModel.getSize(); i++) {
//        IGosuTarget target = (IGosuTarget) myBuildTargetsModel.getElementAt(i);
//        if (buildTarget.hashString().equals(target.hashString())) {
//          myBuildTargetComboBox.setSelectedIndex(i);
//          break;
//        }
//      }
//    }
  }

  private void updateJdks() {
    _jdksModel.removeAllElements();
    for (Sdk sdk : _sdkModel.getSdks()) {
      if (GosuSdkUtils.isApplicableJdk(sdk)) {
        _jdksModel.addElement(sdk);
      }
    }
  }

  public void addJavaSdk(Sdk sdk) {
    _jdksModel.addElement(sdk);
  }

  public void removeJavaSdk(Sdk sdk) {
    _jdksModel.removeElement(sdk);
  }

  public void updateJdks(Sdk sdk, String previousName) {
    final Sdk[] sdks = _sdkModel.getSdks();
    for (Sdk currentSdk : sdks) {
      if (currentSdk.getSdkType().equals(GosuSdkType.getInstance())) {
        final GosuSdkAdditionalData data = (GosuSdkAdditionalData) currentSdk.getSdkAdditionalData();
        final Sdk internalJava = data != null ? data.getJavaSdk() : null;
        if (internalJava != null && Comparing.equal(internalJava.getName(), previousName)) {
          data.setJavaSdk(sdk);
        }
      }
    }
    updateJdks();
  }

  public void internalJdkUpdate(@NotNull final Sdk sdk) {
    GosuSdkAdditionalData data = (GosuSdkAdditionalData) sdk.getSdkAdditionalData();
    if (data == null) return;
    final Sdk javaSdk = data.getJavaSdk();
    if ( _jdksModel.getIndexOf(javaSdk) == -1) {
      _jdksModel.addElement(javaSdk);
    } else {
      _jdksModel.setSelectedItem(javaSdk);
    }
  }

}
