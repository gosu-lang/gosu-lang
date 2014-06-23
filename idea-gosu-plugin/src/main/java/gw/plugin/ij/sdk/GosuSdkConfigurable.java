/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.sdk;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.projectRoots.AdditionalDataConfigurable;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkAdditionalData;
import com.intellij.openapi.projectRoots.SdkModel;
import com.intellij.openapi.projectRoots.SdkModificator;
import gw.plugin.ij.util.ExceptionUtil;
import gw.plugin.ij.util.GosuBundle;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class GosuSdkConfigurable implements AdditionalDataConfigurable {
  @NotNull
  private final GosuSdkConfigurableForm myForm;

  private Sdk mySdk;

  public GosuSdkConfigurable(@NotNull SdkModel sdkModel, @NotNull SdkModificator sdkModificator) {
    myForm = new GosuSdkConfigurableForm(sdkModel, sdkModificator);
  }

  @Override
  public void setSdk(Sdk sdk) {
    mySdk = sdk;
  }

  @Override
  public JComponent createComponent() {
    try {
      return myForm.getContentPanel();
    } catch (IllegalStateException ex) {
      ExceptionUtil.showError(GosuBundle.message("error.ui_designed_missing"), ex);
      throw new IllegalStateException(GosuBundle.message("error.ui_designed_missing"), ex);
    }
  }

  @Override
  public boolean isModified() {
    final GosuSdkAdditionalData data = (GosuSdkAdditionalData) mySdk.getSdkAdditionalData();
    Sdk javaSdk = data != null ? data.getJavaSdk() : null;
    return javaSdk != myForm.getSelectedSdk();
  }

  @Override
  public void apply() throws ConfigurationException {
    GosuSdkAdditionalData oldData = (GosuSdkAdditionalData) mySdk.getSdkAdditionalData();
    GosuSdkAdditionalData newData = new GosuSdkAdditionalData(myForm.getSelectedSdk(), myForm.getGosuVersion());
    final SdkModificator modificator = mySdk.getSdkModificator();
    modificator.setSdkAdditionalData(newData);
    ApplicationManager.getApplication().runWriteAction(new Runnable() {
      public void run() {
        modificator.commitChanges();
      }
    });
  }

  @Override
  public void reset() {
    if (mySdk == null) {
      return;
    }
    SdkAdditionalData sdkData = mySdk.getSdkAdditionalData();
    if (!(sdkData instanceof GosuSdkAdditionalData)) {
      return;
    }
    final GosuSdkAdditionalData gosuData = (GosuSdkAdditionalData) sdkData;
    myForm.init(gosuData.getJavaSdk(), mySdk);
  }

  @Override
  public void disposeUIResources() {
  }

  public void addJavaSdk(Sdk sdk) {
    myForm.addJavaSdk(sdk);
  }

  public void removeJavaSdk(Sdk sdk) {
    myForm.removeJavaSdk(sdk);
  }

  public void updateJavaSdkList(Sdk sdk, String previousName) {
    myForm.updateJdks(sdk, previousName);
  }

  public void internalJdkUpdate(Sdk sdk) {
    myForm.internalJdkUpdate(sdk);
  }
}
